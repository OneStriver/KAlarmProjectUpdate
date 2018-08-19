package com.fh.entity.alarmAttr;

public class AlarmAttributeEntity {

	private String deviceType;
	private int alarmCode;
	private int alarmType;
	private String alarmTypeName;
	private int alarmSeverity;
	private String alarmSeverityName;
	private String alarmDescription;
	private String alarmCause;
	private String alarmCause_en;
	private String treatment;
	private String addition;
	private int limitStrategy;
	private int autoClearEnable;
	private int autoClearTimeout;
	private int alarmSuppress;

	public AlarmAttributeEntity() {
		super();
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public int getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(int alarmCode) {
		this.alarmCode = alarmCode;
	}

	public int getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmTypeName() {
		return alarmTypeName;
	}

	public void setAlarmTypeName(String alarmTypeName) {
		this.alarmTypeName = alarmTypeName;
	}

	public int getAlarmSeverity() {
		return alarmSeverity;
	}

	public void setAlarmSeverity(int alarmSeverity) {
		this.alarmSeverity = alarmSeverity;
	}

	public String getAlarmSeverityName() {
		return alarmSeverityName;
	}

	public void setAlarmSeverityName(String alarmSeverityName) {
		this.alarmSeverityName = alarmSeverityName;
	}

	public String getAlarmDescription() {
		return alarmDescription;
	}

	public void setAlarmDescription(String alarmDescription) {
		this.alarmDescription = alarmDescription;
	}

	public String getAlarmCause() {
		return alarmCause;
	}

	public void setAlarmCause(String alarmCause) {
		this.alarmCause = alarmCause;
	}
	
	public String getAlarmCause_en() {
		return alarmCause_en;
	}

	public void setAlarmCause_en(String alarmCause_en) {
		this.alarmCause_en = alarmCause_en;
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
	
	public int getLimitStrategy() {
		return limitStrategy;
	}

	public void setLimitStrategy(int limitStrategy) {
		this.limitStrategy = limitStrategy;
	}

	public int getAutoClearEnable() {
		return autoClearEnable;
	}

	public void setAutoClearEnable(int autoClearEnable) {
		this.autoClearEnable = autoClearEnable;
	}

	public int getAutoClearTimeout() {
		return autoClearTimeout;
	}

	public void setAutoClearTimeout(int autoClearTimeout) {
		this.autoClearTimeout = autoClearTimeout;
	}

	public int getAlarmSuppress() {
		return alarmSuppress;
	}

	public void setAlarmSuppress(int alarmSuppress) {
		this.alarmSuppress = alarmSuppress;
	}

}
