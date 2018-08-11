package com.fh.alarmProcess.alarmMsgPojo;
/**
 * 写入数据库中的类
 */
public class AlarmToDBPOJO {
	
	private int no = 0;
	private String source;
	private int code;
	private String raised_time;
	private String last_change_time;
	private String ack_time;
	private int ack_uid;
	private String cleared_time;
	private int cleared_uid;
	private String addition_pairs;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
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
	public int getAck_uid() {
		return ack_uid;
	}
	public void setAck_uid(int ack_uid) {
		this.ack_uid = ack_uid;
	}
	public String getCleared_time() {
		return cleared_time;
	}
	public void setCleared_time(String cleared_time) {
		this.cleared_time = cleared_time;
	}
	public int getCleared_uid() {
		return cleared_uid;
	}
	public void setCleared_uid(int cleared_uid) {
		this.cleared_uid = cleared_uid;
	}
	public String getAddition_pairs() {
		return addition_pairs;
	}
	public void setAddition_pairs(String addition_pairs) {
		this.addition_pairs = addition_pairs;
	}
	
	

}
