package com.fh.alarmProcess.alarmMsgPojo;

public class KAlarmMqttEntity {

	private String entryId; // 入库编号
	private String belongWhId; // 所属仓库
	private String productId; // 品名
	private String PBNO; // 货物编号
	private String bundleNo; // 捆号
	private String bleId; // 蓝牙标签ID
	private String finishTime; // 入库时间
	private String alarmTime; // 告警时间
	private String alarmCause; // 告警原因

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public String getBelongWhId() {
		return belongWhId;
	}

	public void setBelongWhId(String belongWhId) {
		this.belongWhId = belongWhId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPBNO() {
		return PBNO;
	}

	public void setPBNO(String pBNO) {
		PBNO = pBNO;
	}

	public String getBundleNo() {
		return bundleNo;
	}

	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}

	public String getBleId() {
		return bleId;
	}

	public void setBleId(String bleId) {
		this.bleId = bleId;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getAlarmCause() {
		return alarmCause;
	}

	public void setAlarmCause(String alarmCause) {
		this.alarmCause = alarmCause;
	}

}
