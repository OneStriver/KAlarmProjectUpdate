package com.fh.alarmProcess.additionPairsConvert;

public class Position {

	private String iws;
	private String t;
	private int x;
	private int y;
	private int z;

	public String getIws() {
		return iws;
	}

	public void setIws(String iws) {
		this.iws = iws;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Position [iws=" + iws + ", t=" + t + ", x=" + x + ", y=" + y + ", z=" + z + "]";
	}

}
