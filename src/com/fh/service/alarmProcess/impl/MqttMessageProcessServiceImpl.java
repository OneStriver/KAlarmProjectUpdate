package com.fh.service.alarmProcess.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fh.alarmProcess.alarmMsgPojo.AlarmOriginalPOJO;
import com.fh.alarmProcess.alarmMsgPojo.AlarmProcessPOJO;
import com.fh.alarmProcess.message.GlobalHashMap;
import com.fh.alarmProcess.mqttMsgProcess.ProcessAlarmProcessMsg;
import com.fh.entity.PageData;
import com.fh.entity.alarmAttr.AlarmAttributeEntity;
import com.fh.entity.faultManagement.historyAlarm.DbAlarmLog;
import com.fh.mqtt.MqttMessageServer;
import com.fh.service.alarmProcess.MqttMessageProcessService;
import com.fh.service.faultManagement.historyAlarm.HistoryAlarmService;
import com.fh.util.TimeUtil;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

@Service("mqttMessageProcessService")
public class MqttMessageProcessServiceImpl implements MqttMessageProcessService {
	
	private static Logger logger = LoggerFactory.getLogger(ProcessAlarmProcessMsg.class);
	private AlarmOriginalPOJO alarmOriginalPOJO;
	private AlarmProcessPOJO alarmProcessPOJO;
	private AlarmAttributeEntity alarmAttributeEntity;
	//缓存判断是否有未清除的记录
	private DbAlarmLog cacheDbAlarmLog;
	//反序列化
	private Gson gson = new Gson();
	
	@Resource(name="historyAlarmService")
	private HistoryAlarmService historyAlarmService;
	
	@Resource(name="mqttMessageServer")
	private MqttMessageServer mqttMessageServer;
	//缓存每个标签的告警上报次数
	private static Map<String,Integer> cacheCountMap = new ConcurrentHashMap<String, Integer>();

	//获取设备显示的名称
	public String getEquipName(String[] splitTopic) {
		String displayName = GlobalHashMap.equipNameSourceCodeInfoMap.get(splitTopic[2] + "-" + splitTopic[3]);
		return displayName + "-" + splitTopic[3];
	}
	
	@Override
	public void processMqttMsg(String topic,String info) {
		try {
			alarmOriginalPOJO = new AlarmOriginalPOJO();
			alarmProcessPOJO = new AlarmProcessPOJO();
			//告警源
			String[] splitTopic = topic.split("/");
			// /raw_alarm/ATCA/9527/board/9527
			String originalAlarmResource = topic.substring(1+splitTopic[1].length()+1,topic.length());
			alarmProcessPOJO.setSource(originalAlarmResource);
			//设备名称
			alarmProcessPOJO.setEquipName(splitTopic[2]);
			
			JSONObject obj = JSONObject.fromObject(info);
			Iterator<?> it = obj.keys();
			while (it.hasNext()) {
				Object key = it.next();
				Object value = obj.get(key);
				if ("code".equals(key)) {
					alarmOriginalPOJO.setCode(value.toString());
				}
				if ("clear".equals(key)) {
					alarmOriginalPOJO.setClear(value.toString());
				} 
				if ("addition_pairs".equals(key)) {
					alarmOriginalPOJO.setAddition_pairs(value.toString());
				}
			}
			String originalAlarmCode = alarmOriginalPOJO.getCode();
			String originalAlarmClear = alarmOriginalPOJO.getClear();
			String originalAlarmAdditionParirs = alarmOriginalPOJO.getAddition_pairs();
			if(originalAlarmCode == null || originalAlarmClear == null || originalAlarmAdditionParirs == null){
				return;
			}
			String deviceNameStr = splitTopic[2];
			//查询数据库的告警的属性信息
			String additionStr = "";
			
			List<AlarmAttributeEntity> alarmAttributeList = GlobalHashMap.alarmAttributeMap.get("alarmAttributeList");
			for (int i = 0; i < alarmAttributeList.size(); i++) {
				AlarmAttributeEntity eachAlarmAttribute = alarmAttributeList.get(i);
				if((Integer.valueOf(originalAlarmCode) == eachAlarmAttribute.getAlarmCode())
					&& (deviceNameStr.equals(eachAlarmAttribute.getDeviceType()))){
					alarmAttributeEntity = eachAlarmAttribute;
					break;
				}
			}
			//告警码
			alarmProcessPOJO.setCode(Integer.valueOf(originalAlarmCode));
			//告警类型
			alarmProcessPOJO.setAlarmType(""+alarmAttributeEntity.getAlarmType());
			//告警等级
			alarmProcessPOJO.setSeverity(""+alarmAttributeEntity.getAlarmSeverity());
			//告警描述
			alarmProcessPOJO.setDesc(alarmAttributeEntity.getAlarmDescription());
			//告警原因
			alarmProcessPOJO.setCause(alarmAttributeEntity.getAlarmCause());
			//建议处理措施
			alarmProcessPOJO.setTreatment(alarmAttributeEntity.getTreatment());
			//附加信息(addition‐pairs)
			alarmProcessPOJO.setAddition(additionStr);
			//告警发生时间
			alarmProcessPOJO.setRaised_time(TimeUtil.getNowTime());
			//附加信息(原始数据)
			alarmProcessPOJO.setAddition_pairs(originalAlarmAdditionParirs);
			//上报消息提示是否清除(原始数据)
			alarmProcessPOJO.setClear(originalAlarmClear);
			// 51/1/board/1-2(由source和code两个字段组成)
			String onlySourceCode = alarmProcessPOJO.getSource() + "-" + alarmProcessPOJO.getCode();
			Integer getOnlySourceCodeValue = cacheCountMap.get(onlySourceCode);
			if(getOnlySourceCodeValue==null){
				getOnlySourceCodeValue = 1;
				cacheCountMap.put(onlySourceCode, 1);
			}else{
				cacheCountMap.put(onlySourceCode, getOnlySourceCodeValue+1);
			}
			/*
			Integer alarmTimeOut = PropertyReadUtil.getInstance().getAlarmTimeOut();
			if ("true".equals(originalAlarmClear)) {
				alarmProcessPOJO.setCleared_time(TimeUtil.getNowTime());
				//缓存中有数据说明上报过该告警(首先清除任务,然后删除缓存)
				QuartzManager.removeJob(GlobalHashMap.getJobName(onlySourceCode),
						GlobalHashMap.getJobGroupName(onlySourceCode), 
						GlobalHashMap.getTriggerName(onlySourceCode),
						GlobalHashMap.getTriggerGroupName(onlySourceCode));
				cacheCountMap.put(onlySourceCode, 1);
			}else if ("false".equals(originalAlarmClear)) {
				QuartzManager.removeJob(GlobalHashMap.getJobName(onlySourceCode),
						GlobalHashMap.getJobGroupName(onlySourceCode),
						GlobalHashMap.getTriggerName(onlySourceCode),
						GlobalHashMap.getTriggerGroupName(onlySourceCode));
				QuartzManager.addJob(GlobalHashMap.getJobName(onlySourceCode),
						GlobalHashMap.getJobGroupName(onlySourceCode), 
						GlobalHashMap.getTriggerName(onlySourceCode),
						GlobalHashMap.getTriggerGroupName(onlySourceCode), 
						HandleTaskJob.class, alarmTimeOut, alarmProcessPOJO);
				cacheCountMap.put(onlySourceCode, getOnlySourceCodeValue+1);
				if(getOnlySourceCodeValue==1){
					//不做处理操作
				}else{
					if(getOnlySourceCodeValue==5){
						cacheCountMap.put(onlySourceCode, 1);
					}
					return;
				}
			}
			*/
			processMqttMessage(alarmProcessPOJO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processMqttMessage(AlarmProcessPOJO alarmProcessPOJO) {
		//每次将对象置为null
		cacheDbAlarmLog = null;
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
					
				}else{
					if(writeDbAlarmLog.getClearTime()!=null){
						//这属于清除告警数据
						logger.info(">>>>>>>>>>这是清除告警的消息");
						writeDbAlarmLog.setAdditionPairs(alarmProcessPOJO.getAddition_pairs());
						writeDbAlarmLog.setSerialNumber(cacheDbAlarmLog.getSerialNumber());
						writeDbAlarmLog.setLastChangeTime(writeDbAlarmLog.getClearTime());
						historyAlarmService.updateHistoryAlarmLogByObject(writeDbAlarmLog);
					}else{
						//这是属于重复的告警
						logger.info(">>>>>>>>>>这是重复告警的消息");
						String nowTime = TimeUtil.getNowTime();
						writeDbAlarmLog.setAdditionPairs(alarmProcessPOJO.getAddition_pairs());
						writeDbAlarmLog.setSerialNumber(cacheDbAlarmLog.getSerialNumber());
						writeDbAlarmLog.setLastChangeTime(nowTime);
						historyAlarmService.updateHistoryAlarmLogByObject(writeDbAlarmLog);
					}
				}
			}
			// 发送消息到MQTT
			sendMsgToMqtt(alarmProcessPOJO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 发送消息到MQTT
	private void sendMsgToMqtt(AlarmProcessPOJO alarmProcessPOJO) {
		String sendTopicStr = "/alarm/"+alarmProcessPOJO.getEquipName();
		sendTopicStr = sendTopicStr+
				"/"+alarmProcessPOJO.getCode()+
				"/"+alarmProcessPOJO.getSource();
		logger.info(">>>>>>>>>>>>>>>>>>>>>>推送的主题:"+sendTopicStr);
		Map<String,Object> parseObject = JSON.parseObject(alarmProcessPOJO.getAddition_pairs());
		alarmProcessPOJO.setAdditionMap(parseObject);
		String onlySourceCode = alarmProcessPOJO.getSource() + "-" + alarmProcessPOJO.getCode();
		Integer alarmCount = cacheCountMap.get(onlySourceCode);
		if(alarmCount<=3){
			mqttMessageServer.send(gson.toJson(alarmProcessPOJO), 0,sendTopicStr);
		}
	}
	
	//发送消息给前台提示(WebSocket连接)
	/*
	String message = "告警序号:"+almMqttPOJO.getNo()+"<br/>"+
					"设备名称:"+almMqttPOJO.getEquipName()+"<br/>"+
					"告警类型:"+(String)almType+"<br/>"+
					"告警级别:"+(String)almSeverity+"<br/>"+
					"告警原因:"+almMqttPOJO.getCause()+"<br/>"+
					"发生时间:"+almMqttPOJO.getRaised_time();	
	RealTimeAlarmMessageServer.sendMqttMessage(message);
	*/

}
