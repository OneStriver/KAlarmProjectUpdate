package com.fh.entity;

public class AlarmStrategyEntity {

	// 更新数据库的策略(多少次更新一次)
	private int updateAlarmFrequency;
	// 推送MQTT消息的策略(多少次推送一次)
	private int sendMqttAlarmFrequency;

	public AlarmStrategyEntity(){
		
	}
	
	public AlarmStrategyEntity(int updateAlarmFrequency,int sendMqttAlarmFrequency){
		this.updateAlarmFrequency = updateAlarmFrequency;
		this.sendMqttAlarmFrequency = sendMqttAlarmFrequency;
	}
	
	public int getUpdateAlarmFrequency() {
		return updateAlarmFrequency;
	}

	public void setUpdateAlarmFrequency(int updateAlarmFrequency) {
		this.updateAlarmFrequency = updateAlarmFrequency;
	}

	public int getSendMqttAlarmFrequency() {
		return sendMqttAlarmFrequency;
	}

	public void setSendMqttAlarmFrequency(int sendMqttAlarmFrequency) {
		this.sendMqttAlarmFrequency = sendMqttAlarmFrequency;
	}

}
