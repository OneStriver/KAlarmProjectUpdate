package com.fh.messageProcess;

public class TimeOutMsg 
{
	private String timerId;
	private String timerType;
	private Object userData;
	
	public TimeOutMsg(String timerId,String timerType,Object userData)
	{
		this.timerId=timerId;
		this.timerType=timerType;
		this.userData=userData;
	}

	public String getTimerId() {
		return timerId;
	}

	public void setTimerId(String timerId) {
		this.timerId = timerId;
	}

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}
	public String getTimerType() {
		return timerType;
	}

	public void setTimerType(String timerType) {
		this.timerType = timerType;
	}
}
