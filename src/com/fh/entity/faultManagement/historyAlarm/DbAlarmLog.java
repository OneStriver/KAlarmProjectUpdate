package com.fh.entity.faultManagement.historyAlarm;

public class DbAlarmLog {

	private Long serialNumber;
	private String alarmSource;
	private Integer alarmCode;
	private String raisedTime;
	private String lastChangeTime;
	private String ackTime;
	private String ackUserName;
	private Integer clearFlag;
	private String clearTime;
	private String clearUserName;
	private String additionPairs;
	private String comments;

	public DbAlarmLog() {

	}

	public Long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getAlarmSource() {
		return alarmSource;
	}

	public void setAlarmSource(String alarmSource) {
		this.alarmSource = alarmSource;
	}

	public Integer getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(Integer alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getRaisedTime() {
		return raisedTime;
	}

	public void setRaisedTime(String raisedTime) {
		this.raisedTime = raisedTime;
	}

	public String getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(String lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	public String getAckTime() {
		return ackTime;
	}

	public void setAckTime(String ackTime) {
		this.ackTime = ackTime;
	}

	public String getAckUserName() {
		return ackUserName;
	}

	public void setAckUserName(String ackUserName) {
		this.ackUserName = ackUserName;
	}
	
	public Integer getClearFlag() {
		return clearFlag;
	}

	public void setClearFlag(Integer clearFlag) {
		this.clearFlag = clearFlag;
	}

	public String getClearTime() {
		return clearTime;
	}

	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}

	public String getClearUserName() {
		return clearUserName;
	}

	public void setClearUserName(String clearUserName) {
		this.clearUserName = clearUserName;
	}

	public String getAdditionPairs() {
		return additionPairs;
	}

	public void setAdditionPairs(String additionPairs) {
		this.additionPairs = additionPairs;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
