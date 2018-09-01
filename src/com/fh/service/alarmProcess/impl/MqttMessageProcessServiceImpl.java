package com.fh.service.alarmProcess.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fh.alarmProcess.alarmMsgPojo.AlarmOriginalPOJO;
import com.fh.alarmProcess.alarmMsgPojo.AlarmProcessPOJO;
import com.fh.alarmProcess.message.GlobalHashMap;
import com.fh.alarmProcess.mqttMsgProcess.ProcessAlarmProcessMsg;
import com.fh.alarmProcess.quartzConfig.HandleTaskJob;
import com.fh.alarmProcess.quartzConfig.QuartzManager;
import com.fh.entity.AlarmStrategyEntity;
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
	
	private static Logger logger = Logger.getLogger(ProcessAlarmProcessMsg.class);
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
			if(originalAlarmCode == null || originalAlarmClear == null){
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
			if(alarmAttributeEntity==null){
				logger.info(">>>>>>>>没有找到该类型告警对应的属性实体类");
				return;
			}
			logger.info(">>>>>>>>>>>上报消息是否是清除告警的消息:"+originalAlarmClear);
			//告警码
			alarmProcessPOJO.setCode(Integer.valueOf(originalAlarmCode));
			//告警类型
			alarmProcessPOJO.setAlarmType(""+alarmAttributeEntity.getAlarmTypeEntity().getAlarmTypeName());
			//告警等级
			alarmProcessPOJO.setSeverity(""+alarmAttributeEntity.getAlarmServerityEntity().getAlarmServerityName());
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
			String alarmSingleFlag = topic.substring(1+splitTopic[1].length()+1,topic.length()) + ":" + alarmOriginalPOJO.getCode();
			//缓存标志位
			alarmProcessPOJO.setAlarmSingleFlag(alarmSingleFlag);
			//是否开启更新数据库策略和推送策略
			int limitStrategy = alarmAttributeEntity.getLimitStrategy();
			int pushStrategy = alarmAttributeEntity.getPushStrategy();
			alarmProcessPOJO.setUpdateAlarmStrategy(limitStrategy);
			alarmProcessPOJO.setSendAlarmStrategy(pushStrategy);
			//告警更新数据库和推送MQTT策略
			AlarmStrategyEntity alarmStrategyEntity = GlobalHashMap.cacheAlarmCountMap.get(alarmSingleFlag);
			if("false".equals(originalAlarmClear)){
				if (alarmStrategyEntity == null) {
					alarmStrategyEntity = new AlarmStrategyEntity();
					alarmStrategyEntity.setUpdateAlarmFrequency(1);
					alarmStrategyEntity.setSendMqttAlarmFrequency(1);
					GlobalHashMap.cacheAlarmCountMap.put(alarmSingleFlag, alarmStrategyEntity);
				} else {
					alarmStrategyEntity.setUpdateAlarmFrequency(alarmStrategyEntity.getUpdateAlarmFrequency()+1);
					alarmStrategyEntity.setSendMqttAlarmFrequency(alarmStrategyEntity.getSendMqttAlarmFrequency()+1);
					GlobalHashMap.cacheAlarmCountMap.put(alarmSingleFlag, alarmStrategyEntity);
				}
			}
			alarmProcessPOJO.setAlarmStrategyEntity(alarmStrategyEntity);
			// 1表示开启自动清除 	0表示没有开启自动清除
			int autoClearEnable = alarmAttributeEntity.getAutoClearEnable();
			//自动清除时间(以秒为单位)
			int autoClearTimeout = alarmAttributeEntity.getAutoClearTimeout();
			if ("true".equals(originalAlarmClear)) {
				if(autoClearEnable==1){
					//设备自动上报告警清除消息
					QuartzManager.removeJob(GlobalHashMap.getJobName(alarmSingleFlag),
							GlobalHashMap.getJobGroupName(alarmSingleFlag), GlobalHashMap.getTriggerName(alarmSingleFlag),
							GlobalHashMap.getTriggerGroupName(alarmSingleFlag));
				}
			}else if ("false".equals(originalAlarmClear)) {
				if(autoClearEnable==1){
					//缓存中有数据说明上报过该告警
					QuartzManager.removeJob(GlobalHashMap.getJobName(alarmSingleFlag),
							GlobalHashMap.getJobGroupName(alarmSingleFlag),
							GlobalHashMap.getTriggerName(alarmSingleFlag),
							GlobalHashMap.getTriggerGroupName(alarmSingleFlag));
					QuartzManager.addJob(GlobalHashMap.getJobName(alarmSingleFlag),
							GlobalHashMap.getJobGroupName(alarmSingleFlag), GlobalHashMap.getTriggerName(alarmSingleFlag),
							GlobalHashMap.getTriggerGroupName(alarmSingleFlag), HandleTaskJob.class, autoClearTimeout, alarmProcessPOJO);
				}
			}
			//处理MQTT接收到的消息
			processMqttMessage(alarmProcessPOJO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void processMqttMessage(AlarmProcessPOJO alarmProcessPOJO) {
		//处理告警的策略实体类(缓存每种告警更新数据库和推送的次数)
		AlarmStrategyEntity processAlarmStrategyEntity = alarmProcessPOJO.getAlarmStrategyEntity();
		//告警清除
		String alarmSingleFlag = alarmProcessPOJO.getAlarmSingleFlag();
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
			//是否是清除消息
			String clearFlag = alarmProcessPOJO.getClear();
			if("true".equals(clearFlag)){
				alarmProcessPOJO.setCleared_time(TimeUtil.getNowTime());
				//清除时间
				writeDbAlarmLog.setClearTime(alarmProcessPOJO.getCleared_time());
				//清除UId
				writeDbAlarmLog.setClearUserName("设备上报清除");
			}
			//附加信息(这里必须携带,因为告警需要addition-pairs)
			writeDbAlarmLog.setAdditionPairs(alarmProcessPOJO.getAddition_pairs());
			//查询数据库时候有该条记录
			pageData.put("alarmSource", alarmProcessPOJO.getSource());
			pageData.put("alarmCode", alarmProcessPOJO.getCode());
			List<DbAlarmLog> dbAlarmLogs = historyAlarmService.selectMqttAlarmLogBySourceAndCode(pageData);
			
			if(dbAlarmLogs.size()==0 && writeDbAlarmLog.getClearTime()!=null){
				//没有上报过告警直接上报告警清除,不处理
				logger.info(">>>>>>>>没有上报过告警直接上报告警清除,不处理");
				return;
			}else if(dbAlarmLogs.size()==0 && writeDbAlarmLog.getClearTime()==null){
				//第一次告警发生时间和最后更新时间一样
				writeDbAlarmLog.setLastChangeTime(writeDbAlarmLog.getRaisedTime());
				//这是第一次上报的告警 写入数据库
				historyAlarmService.addAlarmLogData(writeDbAlarmLog);
				long serialNumber = writeDbAlarmLog.getSerialNumber();
				alarmProcessPOJO.setNo(serialNumber);
				logger.info("第一次上报的告警,写入数据库之后的主键值:"+serialNumber);
			}else if(dbAlarmLogs.size()!=0){
				//查询出数据库中没有清除的告警
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
						logger.info(">>>>>>>>该类型的告警已经清除,这是重新上报的告警清除,还没有告警");
						return;
					}
					//第一次告警发生时间和最后更新时间一样
					writeDbAlarmLog.setLastChangeTime(writeDbAlarmLog.getRaisedTime());
					//说明同样的告警已经清除了,这相当于是新的一次告警
					historyAlarmService.addAlarmLogData(writeDbAlarmLog);
					long serialNumber = writeDbAlarmLog.getSerialNumber();
					alarmProcessPOJO.setNo(serialNumber);
					logger.info("存在已清除的相同告警,写入数据库之后的主键值:"+serialNumber);
				}else{
					if(writeDbAlarmLog.getClearTime()!=null){
						//这属于清除告警数据
						logger.info(">>>>>>>>>>这是设备上报清除告警的消息");
						if("true".equals(clearFlag)){
							writeDbAlarmLog.setClearFlag(1);
						}
						writeDbAlarmLog.setSerialNumber(cacheDbAlarmLog.getSerialNumber());
						historyAlarmService.updateHistoryAlarmLogByObject(writeDbAlarmLog);
						alarmProcessPOJO.setNo(cacheDbAlarmLog.getSerialNumber());
						//清除缓存中的计数
						processAlarmStrategyEntity.setUpdateAlarmFrequency(0);
						GlobalHashMap.cacheAlarmCountMap.put(alarmSingleFlag,processAlarmStrategyEntity);
					}else{
						//这是属于重复的告警
						int updateAlarmFrequency = processAlarmStrategyEntity.getUpdateAlarmFrequency();
						int updateAlarmStrategyDb = alarmProcessPOJO.getUpdateAlarmStrategy();
						if(updateAlarmStrategyDb==0){
							logger.info(">>>>>>>>>>这是重复告警消息");
							String nowTime = TimeUtil.getNowTime();
							if("false".equals(clearFlag)){
								writeDbAlarmLog.setClearFlag(0);
							}
							writeDbAlarmLog.setSerialNumber(cacheDbAlarmLog.getSerialNumber());
							writeDbAlarmLog.setLastChangeTime(nowTime);
							historyAlarmService.updateHistoryAlarmLogByObject(writeDbAlarmLog);
							alarmProcessPOJO.setNo(cacheDbAlarmLog.getSerialNumber());
						}else if(updateAlarmStrategyDb>0){
							if(updateAlarmFrequency==updateAlarmStrategyDb){
								logger.info(">>>>>>>>>>这是有限制策略的重复告警消息");
								String nowTime = TimeUtil.getNowTime();
								if("false".equals(clearFlag)){
									writeDbAlarmLog.setClearFlag(0);
								}
								writeDbAlarmLog.setSerialNumber(cacheDbAlarmLog.getSerialNumber());
								writeDbAlarmLog.setLastChangeTime(nowTime);
								historyAlarmService.updateHistoryAlarmLogByObject(writeDbAlarmLog);
								alarmProcessPOJO.setNo(cacheDbAlarmLog.getSerialNumber());
								//清空更新告警的次数
								processAlarmStrategyEntity.setUpdateAlarmFrequency(0);
								GlobalHashMap.cacheAlarmCountMap.put(alarmSingleFlag,processAlarmStrategyEntity);
							}
						}
					}
				}
			}
			// 发送消息到MQTT
			int sendAlarmFrequency = processAlarmStrategyEntity.getSendMqttAlarmFrequency();
			int sendAlarmStrategyDb = alarmProcessPOJO.getSendAlarmStrategy();
			if(sendAlarmStrategyDb==0){
				logger.info(">>>>>>>>>>这是发送MQTT消息");
				sendMsgToMqtt(alarmProcessPOJO);
			}else if(sendAlarmStrategyDb>0){
				if(sendAlarmFrequency==sendAlarmStrategyDb){
					logger.info(">>>>>>>>>>这是有限制策略的发送MQTT消息");
					sendMsgToMqtt(alarmProcessPOJO);
					//清空更新告警的次数
					processAlarmStrategyEntity.setSendMqttAlarmFrequency(0);
					GlobalHashMap.cacheAlarmCountMap.put(alarmSingleFlag,processAlarmStrategyEntity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 发送消息到MQTT
	private void sendMsgToMqtt(AlarmProcessPOJO alarmProcessPOJO) {
		String sendTopicStr = "/alarm/"+alarmProcessPOJO.getSeverity()+"/"+alarmProcessPOJO.getAlarmType()+"/"+alarmProcessPOJO.getSource();
		logger.info(">>>>>>>>>>>>>>>>>>>>>>推送的主题:"+sendTopicStr);
		//转换addition_pairs之后的数据
		Map<String,Object> parseObject = JSON.parseObject(alarmProcessPOJO.getAddition_pairs());
		alarmProcessPOJO.setAdditionMap(parseObject);
		String sendToMqttStr = gson.toJson(alarmProcessPOJO);
		logger.info(">>>>>>>>>>>>>>>>发送MQTT的数据:"+sendToMqttStr);
		mqttMessageServer.send(sendToMqttStr, 0,sendTopicStr);
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
