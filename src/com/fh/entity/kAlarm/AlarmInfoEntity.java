package com.fh.entity.kAlarm;

/**
 * 
 */
public class AlarmInfoEntity {

	private String code;
	private String severity;
	private String desc;
	private String cause;
	private String treatment;
	private String addition;
	private String clearEnable;
	private String clearTimeOut;
	

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

	public String getClearEnable() {
		return clearEnable;
	}

	public void setClearEnable(String Enable) {
		clearEnable = Enable;
	}

	public String getClearTimeOut() {
		return clearTimeOut;
	}

	public void setClearTimeOut(String TimeOut) {
		clearTimeOut = TimeOut;
	}

	@Override
	public String toString() {
		return "AlarmInfoEntity [code=" + code + ", severity=" + severity + ", desc=" + desc + ", cause=" + cause
				+ ", treatment=" + treatment + ", addition=" + addition + ", clearEnable=" + clearEnable
				+ ", clearTimeOut=" + clearTimeOut + "]";
	}
	
	
	
}
