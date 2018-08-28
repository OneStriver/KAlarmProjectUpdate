package com.fh.alarmProcess.message;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.fh.alarmProcess.alarmMsgPojo.AlarmProcessPOJO;
import com.fh.alarmProcess.alarmMsgPojo.AlarmToDBPOJO;
import com.fh.entity.alarmAttr.AlarmAttributeEntity;
import com.fh.service.faultManagement.historyAlarm.HistoryAlarmService;

/**
 * 数据缓存类
 * Quartz配置属性(Job,JobGroupName,Trigger,TriggerGroupName)
 */
public class GlobalHashMap {
	
	// 缓存所有的中间数据
	public static ConcurrentHashMap<String, AlarmProcessPOJO> cacheAlarmProcessDataMap = new ConcurrentHashMap<String, AlarmProcessPOJO>();
	// 缓存写入数据库中的记录
	public static ConcurrentHashMap<String, AlarmToDBPOJO> almToDBHashMap = new ConcurrentHashMap<String, AlarmToDBPOJO>();
	// 缓存设备名称和对应的Source和code在程序启动的
	public static ConcurrentHashMap<String, String> equipNameSourceCodeInfoMap = new ConcurrentHashMap<String, String>();
	//缓存数据库中所有的属性值
	public static ConcurrentHashMap<String, List<AlarmAttributeEntity>> alarmAttributeMap = new ConcurrentHashMap<String, List<AlarmAttributeEntity>>();
	//緩存 HistoryAlarmService
	public static ConcurrentHashMap<String, HistoryAlarmService> historyAlarmServiceMap = new ConcurrentHashMap<String, HistoryAlarmService>();
	//緩存 MqttClient
	public static ConcurrentHashMap<String, MqttClient> mqttClientMap = new ConcurrentHashMap<String, MqttClient>();
	//限制告警合并多少个上报一次
	public static ConcurrentHashMap<String,Integer> cacheAlarmCountMap = new ConcurrentHashMap<String, Integer>();

	// Job
	private static String jobName = "AlarmJob";
	private static String jobGroupName = "AlarmJobGroup";
	// Trigger
	private static String triggerName = "AlarmTrigger";
	private static String triggerGroupName = "AlarmTriggerGroup";

	public static String getJobName(String alarmSingleFalg) {
		return jobName+"-"+alarmSingleFalg;
	}

	public static void setJobName(String jobName) {
		GlobalHashMap.jobName = jobName;
	}

	public static String getJobGroupName(String alarmSingleFalg) {
		return jobGroupName+"-"+alarmSingleFalg;
	}

	public static void setJobGroupName(String jobGroupName) {
		GlobalHashMap.jobGroupName = jobGroupName;
	}

	public static String getTriggerName(String alarmSingleFalg) {
		return triggerName+"-"+alarmSingleFalg;
	}

	public static void setTriggerName(String triggerName) {
		GlobalHashMap.triggerName = triggerName;
	}

	public static String getTriggerGroupName(String alarmSingleFalg) {
		return triggerGroupName+"-"+alarmSingleFalg;
	}

	public static void setTriggerGroupName(String triggerGroupName) {
		GlobalHashMap.triggerGroupName = triggerGroupName;
	}

}
