package com.fh.entity.faultManagement.realTimeAlarm;

public class ConfirmClearAlarmEntity {

	private int alarmType;
	private String serialNumber;
	private String alarmSource;
	private int alarmCode;
	private String alarmAckTime;
	private String alarmAckPerson;
	private String alarmClearTime;
	private String alarmClearPerson;

	public int getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmSource() {
		return alarmSource;
	}

	public void setAlarmSource(String alarmSource) {
		this.alarmSource = alarmSource;
	}

	public int getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(int alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getAlarmAckTime() {
		return alarmAckTime;
	}

	public void setAlarmAckTime(String alarmAckTime) {
		this.alarmAckTime = alarmAckTime;
	}

	public String getAlarmAckPerson() {
		return alarmAckPerson;
	}

	public void setAlarmAckPerson(String alarmAckPerson) {
		this.alarmAckPerson = alarmAckPerson;
	}

	public String getAlarmClearTime() {
		return alarmClearTime;
	}

	public void setAlarmClearTime(String alarmClearTime) {
		this.alarmClearTime = alarmClearTime;
	}

	public String getAlarmClearPerson() {
		return alarmClearPerson;
	}

	public void setAlarmClearPerson(String alarmClearPerson) {
		this.alarmClearPerson = alarmClearPerson;
	}

}
