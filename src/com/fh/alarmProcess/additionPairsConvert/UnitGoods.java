package com.fh.alarmProcess.additionPairsConvert;

public class UnitGoods {
	   private String goodId;
	   private String bleId;//                '蓝牙标签',
	   private String bundleNo;//             '捆号',
	   private String PBNo;//                 '货物编号',
	   private String oldBundleNo;//          '原捆号',
	   private String entryId;//              '所属入库单',
	   private String containerNo;//          '集装箱号',
	   private String belongWhId;//           '所属仓库',
	   private String belongAreaId;//		         所属库区
	   private float stwGross;//             '抄码毛重',
	   private float stwNet;//               '抄码净重',
	   private float weigh;//                '过磅重量',
	   private String productId;//           '品名ID',
	   private String remark;//              '备注',
	   private int prostatus;//            '默认为-1，入库完成时为0，当被选中到出库详单时置为1，出库完成后置为2；货物非法出库时为3',
	   private String location;//             '货物在仓库位置',
	   private String properties;//          '问题属性参数',
	   private int isEnter;//              '判断货物是否进入指定区域。1为正确，2为错误',
	   private String LOTNo;//              '码单LOT No',
	   private int isLocked;//             '重量锁定标志位0为默认值（未锁定）1为锁定',
	   private String tallyman;//          '执行人用户名,理货员',
	   private String containerSize;   //集装箱尺寸
	   
	   private String finishTime;//工单完成时间
	
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public String getBleId() {
		return bleId;
	}
	public void setBleId(String bleId) {
		this.bleId = bleId;
	}
	public String getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}
	public String getPBNo() {
		return PBNo;
	}
	public void setPBNo(String pBNo) {
		PBNo = pBNo;
	}
	public String getOldBundleNo() {
		return oldBundleNo;
	}
	public void setOldBundleNo(String oldBundleNo) {
		this.oldBundleNo = oldBundleNo;
	}
	public String getEntryId() {
		return entryId;
	}
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
	public String getContainerNo() {
		return containerNo;
	}
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
	public String getBelongWhId() {
		return belongWhId;
	}
	public void setBelongWhId(String belongWhId) {
		this.belongWhId = belongWhId;
	}
	public String getBelongAreaId() {
		return belongAreaId;
	}
	public void setBelongAreaId(String belongAreaId) {
		this.belongAreaId = belongAreaId;
	}
	public float getStwGross() {
		return stwGross;
	}
	public void setStwGross(float stwGross) {
		this.stwGross = stwGross;
	}
	public float getStwNet() {
		return stwNet;
	}
	public void setStwNet(float stwNet) {
		this.stwNet = stwNet;
	}
	public float getWeigh() {
		return weigh;
	}
	public void setWeigh(float weigh) {
		this.weigh = weigh;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getProstatus() {
		return prostatus;
	}
	public void setProstatus(int prostatus) {
		this.prostatus = prostatus;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public int getIsEnter() {
		return isEnter;
	}
	public void setIsEnter(int isEnter) {
		this.isEnter = isEnter;
	}
	public String getLOTNo() {
		return LOTNo;
	}
	public void setLOTNo(String lOTNo) {
		LOTNo = lOTNo;
	}
	public int getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(int isLocked) {
		this.isLocked = isLocked;
	}
	public String getTallyman() {
		return tallyman;
	}
	public void setTallyman(String tallyman) {
		this.tallyman = tallyman;
	}
	
	
	public String getContainerSize() {
		return containerSize;
	}
	public void setContainerSize(String containerSize) {
		this.containerSize = containerSize;
	}
	
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	@Override
	public String toString() {
		return "UnitGoods [goodId=" + goodId + ", bleId=" + bleId + ", bundleNo=" + bundleNo + ", PBNo=" + PBNo
				+ ", oldBundleNo=" + oldBundleNo + ", entryId=" + entryId + ", containerNo=" + containerNo
				+ ", belongWhId=" + belongWhId + ", belongAreaId=" + belongAreaId + ", stwGross=" + stwGross
				+ ", stwNet=" + stwNet + ", weigh=" + weigh + ", productId=" + productId + ", remark=" + remark
				+ ", prostatus=" + prostatus + ", location=" + location + ", properties=" + properties + ", isEnter="
				+ isEnter + ", LOTNo=" + LOTNo + ", isLocked=" + isLocked + ", tallyman=" + tallyman
				+ ", containerSize=" + containerSize + ", finishTime=" + finishTime + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LOTNo == null) ? 0 : LOTNo.hashCode());
		result = prime * result + ((PBNo == null) ? 0 : PBNo.hashCode());
		result = prime * result + ((belongAreaId == null) ? 0 : belongAreaId.hashCode());
		result = prime * result + ((belongWhId == null) ? 0 : belongWhId.hashCode());
		result = prime * result + ((bleId == null) ? 0 : bleId.hashCode());
		result = prime * result + ((bundleNo == null) ? 0 : bundleNo.hashCode());
		result = prime * result + ((containerNo == null) ? 0 : containerNo.hashCode());
		result = prime * result + ((containerSize == null) ? 0 : containerSize.hashCode());
		result = prime * result + ((entryId == null) ? 0 : entryId.hashCode());
		result = prime * result + ((finishTime == null) ? 0 : finishTime.hashCode());
		result = prime * result + ((goodId == null) ? 0 : goodId.hashCode());
		result = prime * result + isEnter;
		result = prime * result + isLocked;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((oldBundleNo == null) ? 0 : oldBundleNo.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + prostatus;
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + Float.floatToIntBits(stwGross);
		result = prime * result + Float.floatToIntBits(stwNet);
		result = prime * result + ((tallyman == null) ? 0 : tallyman.hashCode());
		result = prime * result + Float.floatToIntBits(weigh);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnitGoods other = (UnitGoods) obj;
		if (LOTNo == null) {
			if (other.LOTNo != null)
				return false;
		} else if (!LOTNo.equals(other.LOTNo))
			return false;
		if (PBNo == null) {
			if (other.PBNo != null)
				return false;
		} else if (!PBNo.equals(other.PBNo))
			return false;
		if (belongAreaId == null) {
			if (other.belongAreaId != null)
				return false;
		} else if (!belongAreaId.equals(other.belongAreaId))
			return false;
		if (belongWhId == null) {
			if (other.belongWhId != null)
				return false;
		} else if (!belongWhId.equals(other.belongWhId))
			return false;
		if (bleId == null) {
			if (other.bleId != null)
				return false;
		} else if (!bleId.equals(other.bleId))
			return false;
		if (bundleNo == null) {
			if (other.bundleNo != null)
				return false;
		} else if (!bundleNo.equals(other.bundleNo))
			return false;
		if (containerNo == null) {
			if (other.containerNo != null)
				return false;
		} else if (!containerNo.equals(other.containerNo))
			return false;
		if (containerSize == null) {
			if (other.containerSize != null)
				return false;
		} else if (!containerSize.equals(other.containerSize))
			return false;
		if (entryId == null) {
			if (other.entryId != null)
				return false;
		} else if (!entryId.equals(other.entryId))
			return false;
		if (finishTime == null) {
			if (other.finishTime != null)
				return false;
		} else if (!finishTime.equals(other.finishTime))
			return false;
		if (goodId == null) {
			if (other.goodId != null)
				return false;
		} else if (!goodId.equals(other.goodId))
			return false;
		if (isEnter != other.isEnter)
			return false;
		if (isLocked != other.isLocked)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (oldBundleNo == null) {
			if (other.oldBundleNo != null)
				return false;
		} else if (!oldBundleNo.equals(other.oldBundleNo))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (prostatus != other.prostatus)
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (Float.floatToIntBits(stwGross) != Float.floatToIntBits(other.stwGross))
			return false;
		if (Float.floatToIntBits(stwNet) != Float.floatToIntBits(other.stwNet))
			return false;
		if (tallyman == null) {
			if (other.tallyman != null)
				return false;
		} else if (!tallyman.equals(other.tallyman))
			return false;
		if (Float.floatToIntBits(weigh) != Float.floatToIntBits(other.weigh))
			return false;
		return true;
	}
	
	
	
	
	
	
	
}
