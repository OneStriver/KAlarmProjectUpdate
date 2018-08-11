package com.fh.alarmProcess.observerPattern;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fh.alarmProcess.alarmMsgPojo.AlarmProcessPOJO;
import com.fh.alarmProcess.message.GlobalHashMap;
import com.fh.alarmProcess.quartzConfig.QuartzManager;
import com.fh.entity.PageData;
import com.fh.entity.faultManagement.historyAlarm.DbAlarmLog;
import com.fh.service.faultManagement.historyAlarm.HistoryAlarmService;
import com.fh.util.TimeUtil;

public class AlarmObserverStored {

	private static Logger logger = LoggerFactory.getLogger(AlarmObserverStored.class);
	//缓存判断是否有未清除的记录
	private DbAlarmLog cacheDbAlarmLog;
	//历史告警Service
	private volatile static AlarmObserverStored alarmObserverStored;
	
	public AlarmObserverStored() {
		
	}

	public static AlarmObserverStored getInstance() {
		if(alarmObserverStored!=null){
		}else{
			synchronized(AlarmObserverStored.class){
				if(alarmObserverStored==null){
					alarmObserverStored = new AlarmObserverStored();
				}
			}
		}
		return alarmObserverStored;
	}

	public void processMqttMessage(AlarmProcessPOJO alarmProcessPOJO) {
		//每次将对象置为null
		cacheDbAlarmLog = null;
		HistoryAlarmService historyAlarmService = GlobalHashMap.historyAlarmServiceMap.get("historyAlarmServiceInstance");
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
			//确认时间
			writeDbAlarmLog.setAckTime(alarmProcessPOJO.getAck_time());
			//确认UId
			if (alarmProcessPOJO.getAck_user() != null) {
				writeDbAlarmLog.setAckUserName("无");
			}
			//清除时间
			writeDbAlarmLog.setClearTime(alarmProcessPOJO.getCleared_time());
			//清除UId
			if (alarmProcessPOJO.getCleared_user() != null) {
				writeDbAlarmLog.setClearUserName("无");
			}
			//附加信息
			writeDbAlarmLog.setAdditionPairs(alarmProcessPOJO.getAddition_pairs());
			//缓存中间数据的唯一标识
			String sourceAndCode = alarmProcessPOJO.getSource() + ":" + alarmProcessPOJO.getCode();
			//查询数据库时候有该条记录
			pageData.put("alarmSource", alarmProcessPOJO.getSource());
			pageData.put("alarmCode", alarmProcessPOJO.getCode());
			List<DbAlarmLog> dbAlarmLogs = historyAlarmService.selectMqttAlarmLogBySourceAndCode(pageData);
			
			if(dbAlarmLogs.size()==0 && writeDbAlarmLog.getClearTime()!=null){
				//没有上报过告警直接上报告警清除,不处理
				return;
			}else if(dbAlarmLogs.size()==0 && writeDbAlarmLog.getClearTime()==null){
				//这是第一次上报的告警 写入数据库
				historyAlarmService.addAlarmLogData(writeDbAlarmLog);
				long no = writeDbAlarmLog.getSerialNumber();
				logger.info("第一次上报的告警,写入数据库之后的主键值:"+no);
				if(no > 0){
					alarmProcessPOJO.setNo(no);
				}
			}else if(dbAlarmLogs.size()!=0){
				for (DbAlarmLog dbAlarmLogSingle : dbAlarmLogs) {
					if(dbAlarmLogSingle.getClearTime()!=null && !"".equals(dbAlarmLogSingle.getClearTime())){
						continue;
					}else{
						cacheDbAlarmLog = dbAlarmLogSingle;
					}
				}
				if(cacheDbAlarmLog==null){
					//这是新上报的清除,还没有告警,直接丢掉
					if(writeDbAlarmLog.getClearTime()!=null){
						return;
					}
					//说明同样的告警已经清除了,这相当于是新的一次告警
					historyAlarmService.addAlarmLogData(writeDbAlarmLog);
					long no = writeDbAlarmLog.getSerialNumber();
					logger.info("存在已清除的相同告警,写入数据库之后的主键值:"+no);
					if(no > 0){
						alarmProcessPOJO.setNo(no);
					}
				}else{
					if(writeDbAlarmLog.getClearTime()!=null){
						//这属于清除告警数据
						logger.info(">>>>>>>>>>这是清除告警的消息");
						writeDbAlarmLog.setAdditionPairs(alarmProcessPOJO.getAddition_pairs());
						writeDbAlarmLog.setSerialNumber(cacheDbAlarmLog.getSerialNumber());
						writeDbAlarmLog.setLastChangeTime(writeDbAlarmLog.getClearTime());
						historyAlarmService.updateHistoryAlarmLogByObject(writeDbAlarmLog);
						alarmProcessPOJO.setNo(cacheDbAlarmLog.getSerialNumber());
						alarmProcessPOJO.setLast_change_time(writeDbAlarmLog.getClearTime());
						//清除缓存中的数据
						QuartzManager.removeJob(GlobalHashMap.getJobName(sourceAndCode), GlobalHashMap.getJobGroupName(sourceAndCode), GlobalHashMap.getTriggerName(sourceAndCode), GlobalHashMap.getTriggerGroupName(sourceAndCode));
					}else{
						//这是属于重复的告警
						String nowTime = TimeUtil.getNowTime();
						logger.info(">>>>>>>>>>这是重复告警的消息");
						writeDbAlarmLog.setAdditionPairs(alarmProcessPOJO.getAddition_pairs());
						writeDbAlarmLog.setSerialNumber(cacheDbAlarmLog.getSerialNumber());
						writeDbAlarmLog.setLastChangeTime(nowTime);
						historyAlarmService.updateHistoryAlarmLogByObject(writeDbAlarmLog);
						alarmProcessPOJO.setNo(cacheDbAlarmLog.getSerialNumber());
						alarmProcessPOJO.setLast_change_time(nowTime);
					}
				}
			}
			// 发送消息到MQTT
			AlarmToMqttDispaly alarmToMqttDispaly = new AlarmToMqttDispaly();
			alarmToMqttDispaly.sendMsgToMqtt(alarmProcessPOJO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
