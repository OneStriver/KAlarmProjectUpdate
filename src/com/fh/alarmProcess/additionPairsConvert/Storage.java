package com.fh.alarmProcess.additionPairsConvert;

import java.io.Serializable;

public class Storage implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 无参数构造器
	 */
	public Storage() {
	}

	// 主键
	private String id;
	// 蓝牙标签ID
	private String bleId;
	// 货物仓库名称
	private String storagename;

	// 货物位置
	private String x;
	private String y;
	private String z;
	// 上传位置时间
	private String t;
	// 状态
	private String status;
	private String iws;

	// 货物所属位置对应前端仓库图区域ID
	private String squareID;

	// 与数据库中不对应的字段
	// 对应的码单
	private UnitGoods unitGoods;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBleId() {
		return bleId;
	}

	public void setBleId(String bleId) {
		this.bleId = bleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getZ() {
		return z;
	}

	public void setZ(String z) {
		this.z = z;
	}

	public String getStoragename() {
		return storagename;
	}

	public void setStoragename(String storagename) {
		this.storagename = storagename;
	}

	public String getIws() {
		return iws;
	}

	public void setIws(String iws) {
		this.iws = iws;
	}

	public String getSquareID() {
		return squareID;
	}

	public void setSquareID(String squareID) {
		this.squareID = squareID;
	}

	public UnitGoods getUnitGoods() {
		return unitGoods;
	}

	public void setUnitGoods(UnitGoods unitGoods) {
		this.unitGoods = unitGoods;
	}

	@Override
	public String toString() {
		return "Storage [id=" + id + ", bleId=" + bleId + ", storagename=" + storagename + ", x=" + x + ", y=" + y
				+ ", z=" + z + ", t=" + t + ", status=" + status + ", iws=" + iws + ", squareID=" + squareID
				+ ", unitGoods=" + unitGoods + "]";
	}

}
