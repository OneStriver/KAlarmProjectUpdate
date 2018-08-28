package com.fh.alarmProcess.quartzConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fh.alarmProcess.alarmMsgPojo.AlarmProcessPOJO;
import com.fh.alarmProcess.message.GlobalHashMap;
import com.fh.entity.PageData;
import com.fh.entity.faultManagement.historyAlarm.DbAlarmLog;
import com.fh.service.faultManagement.historyAlarm.HistoryAlarmService;
import com.fh.util.TimeUtil;

public class HandleTaskJob implements Job {
	
	private static Logger logger = Logger.getLogger(HandleTaskJob.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DbAlarmLog cacheDbAlarmLog;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info(">>>>>>>开始处理超时未上报清除的告警>>>>>>>>>");
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	    //创建对象的时候,赋值清除时间
	    AlarmProcessPOJO alarmProcessPOJO = 
	    		new AlarmProcessPOJO(
	    				dataMap.getString("source"), dataMap.getString("equipName"), 
	    				dataMap.getInt("code"), dataMap.getString("alarmSingleFlag"), 
	    				dataMap.getString("alarmType"), dataMap.getString("severity"), dataMap.getString("desc"), 
	    				dataMap.getString("cause"), dataMap.getString("treatment"), dataMap.getString("addition"), dataMap.getString("target"), 
	    				dataMap.getString("raised_time"), dataMap.getString("last_change_time"),
	    				dataMap.getString("ack_time"), dataMap.getString("ack_user"),
	    				null, null,
	    				dataMap.getString("addition_pairs"),dataMap.getString("clear"));
	    //处理超时消息
	    logger.info(">>>>>>>>处理告警超时任务的时间:"+sdf.format(new Date()));
	    processTimeOutMqttMessage(alarmProcessPOJO);
	}
	
	private void processTimeOutMqttMessage(AlarmProcessPOJO alarmProcessPOJO) {
		//每次将对象置为null
		cacheDbAlarmLog = null;
		//获取historyService对象
		HistoryAlarmService historyAlarmServiceTemp = GlobalHashMap.historyAlarmServiceMap.get("historyAlarmServiceInstance");
		try {
			PageData pageData = new PageData();
			//数据库中对象的字段
			DbAlarmLog writeDbAlarmLog = new DbAlarmLog();
			//告警源
			writeDbAlarmLog.setAlarmSource(alarmProcessPOJO.getSource());
			//告警码
			writeDbAlarmLog.setAlarmCode(alarmProcessPOJO.getCode());
			//发生时间
			writeDbAlarmLog.setRaisedTime(alarmProcessPOJO.getRaised_time());
			//清除时间
			writeDbAlarmLog.setClearTime(TimeUtil.getNowTime());
			//清除UId
			writeDbAlarmLog.setClearUserName("超时自动清除");
			
			//附加信息
			writeDbAlarmLog.setAdditionPairs(alarmProcessPOJO.getAddition_pairs());
			//查询数据库时候有该条记录
			pageData.put("alarmSource", alarmProcessPOJO.getSource());
			pageData.put("alarmCode", alarmProcessPOJO.getCode());
			List<DbAlarmLog> dbAlarmLogs = historyAlarmServiceTemp.selectMqttAlarmLogBySourceAndCode(pageData);

			for (DbAlarmLog dbAlarmLogSingle : dbAlarmLogs) {
				if(dbAlarmLogSingle.getClearTime()!=null && !"".equals(dbAlarmLogSingle.getClearTime())){
					continue;
				}else{
					cacheDbAlarmLog = dbAlarmLogSingle;
				}
			}
			
			if(writeDbAlarmLog.getClearTime()!=null){
				//这属于清除告警数据
				logger.info(">>>>>>>>>>这是超时自动清除告警的消息");
				writeDbAlarmLog.setClearFlag(1);
				writeDbAlarmLog.setSerialNumber(cacheDbAlarmLog.getSerialNumber());
				historyAlarmServiceTemp.updateHistoryAlarmLogByObject(writeDbAlarmLog);
				//推送pad清除消息
				String sendTopicStr = "/alarm/padClearAlarm";
				logger.info(">>>>>>>>>>>>>>>>>>>>>>超时自动清除告警推送的主题:"+sendTopicStr);
				//发送清除告警消息
				sendAutoClearAlarmToMqtt(writeDbAlarmLog.getSerialNumber().toString(),0,sendTopicStr);
				//清空缓存的计数
				GlobalHashMap.cacheAlarmCountMap.put(alarmProcessPOJO.getAlarmSingleFlag(),0);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendAutoClearAlarmToMqtt(String content, int qos, String topic) {
		MqttClient mqttClient = GlobalHashMap.mqttClientMap.get("mqttClient");
		try {
			MqttMessage message = new MqttMessage(content.getBytes("UTF-8"));
			message.setQos(qos);
			message.setRetained(false);
			mqttClient.publish(topic, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
