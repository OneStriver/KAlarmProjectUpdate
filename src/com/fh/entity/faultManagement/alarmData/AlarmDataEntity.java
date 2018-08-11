package com.fh.entity.faultManagement.alarmData;

public class AlarmDataEntity {

	private String equipType;
	private String code;
	private String severity;
	private String alarmType;
	private String desc;
	private String cause;
	private String treatment;
	private String addition;
	private String autoClearEnable;
	private String autoClearTimeout;
	private String suppress;

	public String getEquipType() {
		return equipType;
	}

	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
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

	public String getAutoClearEnable() {
		return autoClearEnable;
	}

	public void setAutoClearEnable(String autoClearEnable) {
		this.autoClearEnable = autoClearEnable;
	}

	public String getAutoClearTimeout() {
		return autoClearTimeout;
	}

	public void setAutoClearTimeout(String autoClearTimeout) {
		this.autoClearTimeout = autoClearTimeout;
	}

	public String getSuppress() {
		return suppress;
	}

	public void setSuppress(String suppress) {
		this.suppress = suppress;
	}

}
