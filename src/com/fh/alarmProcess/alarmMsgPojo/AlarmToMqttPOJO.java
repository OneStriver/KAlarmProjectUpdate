package com.fh.alarmProcess.alarmMsgPojo;

/**
 * 推送MQTT的消息
 */
public class AlarmToMqttPOJO {

	private Long no;
	private String source;
	private String equipName;
	private String type;
	private String severity;
	private String desc;
	private String cause;
	private String treatment;
	private String addition;
	private String raised_time;
	private String last_change_time;
	private String ack_time;
	private String ack_user;
	private String cleared_time;
	private String cleared_user;
	private String addition_pairs;
	
	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	public String getRaised_time() {
		return raised_time;
	}

	public void setRaised_time(String raised_time) {
		this.raised_time = raised_time;
	}

	public String getLast_change_time() {
		return last_change_time;
	}

	public void setLast_change_time(String last_change_time) {
		this.last_change_time = last_change_time;
	}

	public String getAck_time() {
		return ack_time;
	}

	public void setAck_time(String ack_time) {
		this.ack_time = ack_time;
	}

	public String getAck_user() {
		return ack_user;
	}

	public void setAck_user(String ack_user) {
		this.ack_user = ack_user;
	}

	public String getCleared_time() {
		return cleared_time;
	}

	public void setCleared_time(String cleared_time) {
		this.cleared_time = cleared_time;
	}

	public String getCleared_user() {
		return cleared_user;
	}

	public void setCleared_user(String cleared_user) {
		this.cleared_user = cleared_user;
	}

	public String getAddition_pairs() {
		return addition_pairs;
	}

	public void setAddition_pairs(String addition_pairs) {
		this.addition_pairs = addition_pairs;
	}

}
