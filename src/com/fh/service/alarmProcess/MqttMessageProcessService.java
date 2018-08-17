package com.fh.service.alarmProcess;

/** 
 * 说明： 告警处理类
 */
public interface MqttMessageProcessService{

	/**
	 * 处理mqtt消息
	 */
	public void processMqttMsg(String topic,String info);
	
	
}

