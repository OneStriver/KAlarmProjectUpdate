package com.fh.alarmProcess.additionPairsConvert;

import java.util.List;

public class Addition {
	
	public String id;//设备ID，可以是蓝牙标签ID也可以是UWB板卡ID
	
	//设备位置
	private Position position;
	
	//告警级别
	private String warningLevel;//1:move,2:emergency
	//告警货物，蓝牙标签list.size()==1;叉车list.size()>=1
	private List<Storage> storageList;
	
	private String target;//需要接收告警消息用户或设备,若没有则设置为"#"
	
	private int enable;//是否跨区告警

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getWarningLevel() {
		return warningLevel;
	}

	public void setWarningLevel(String warningLevel) {
		this.warningLevel = warningLevel;
	}

	public List<Storage> getStorageList() {
		return storageList;
	}

	public void setStorageList(List<Storage> storageList) {
		this.storageList = storageList;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "Addition [id=" + id + ", position=" + position + ", warningLevel=" + warningLevel + ", storageList="
				+ storageList + ", target=" + target + ", enable=" + enable + "]";
	}

	



	
	
	
}
