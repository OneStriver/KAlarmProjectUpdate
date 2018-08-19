package com.fh.alarmProcess.alarmMsgPojo;

import java.util.Map;

import com.fh.util.TimeUtil;

/**
 * 存储所有数据的类
 */
public class AlarmProcessPOJO {

	private Long no;
	private String source;
	private String equipName;
	private Integer code;
	private String alarmType;
	private String severity;
	private String desc;
	private String cause;
	private String treatment;
	private String addition;
	private String target;
	private String raised_time;
	private String last_change_time;
	private String ack_time;
	private String ack_user;
	private String cleared_time;
	private String cleared_user;
	private String addition_pairs;
	private Map<String,Object> additionMap;
	private Long updateTime;
	private String clear;

	public AlarmProcessPOJO() {
		updateTime = TimeUtil.getLongNowTime();
	}

	public AlarmProcessPOJO(String source, String equipName, Integer code, String alarmType, String severity,
			String desc, String cause, String treatment, String addition, String target, String raised_time,
			String last_change_time, String ack_time, String ack_user, String cleared_time, String cleared_user,
			String addition_pairs, String clear) {
		this.source = source;
		this.equipName = equipName;
		this.code = code;
		this.alarmType = alarmType;
		this.severity = severity;
		this.desc = desc;
		this.cause = cause;
		this.treatment = treatment;
		this.addition = addition;
		this.target = target;
		this.raised_time = raised_time;
		this.last_change_time = last_change_time;
		this.ack_time = ack_time;
		this.ack_user = ack_user;
		this.cleared_time = cleared_time;
		this.cleared_user = cleared_user;
		this.addition_pairs = addition_pairs;
		this.clear = clear;
	}

	public AlarmProcessPOJO(String source, Integer code, String raised_time, String last_change_time, String ack_time,
			String ack_user, String cleared_time, String cleared_user, String addition_pairs) {
		super();
		this.source = source;
		this.code = code;
		this.raised_time = raised_time;
		this.last_change_time = last_change_time;
		this.ack_time = ack_time;
		this.ack_user = ack_user;
		this.cleared_time = cleared_time;
		this.cleared_user = cleared_user;
		this.addition_pairs = addition_pairs;
	}

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime() {
		updateTime = TimeUtil.getLongNowTime();
	}

	public String getAddition_pairs() {
		return addition_pairs;
	}

	public void setAddition_pairs(String addition_pairs) {
		this.addition_pairs = addition_pairs;
	}

	public String getClear() {
		return clear;
	}

	public void setClear(String clear) {
		this.clear = clear;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public Map<String, Object> getAdditionMap() {
		return additionMap;
	}

	public void setAdditionMap(Map<String, Object> additionMap) {
		this.additionMap = additionMap;
	}

}
