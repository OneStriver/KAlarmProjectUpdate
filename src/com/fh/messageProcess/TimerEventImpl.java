package com.fh.messageProcess;

public class TimerEventImpl implements TimerEvent {

	private String timerId;

	protected TimerEventImpl(String timerId) {
		this.timerId = timerId;
	}

	public String getTimerID() {
		return this.timerId;
	}

	public int hashCode(){
		return timerId.hashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj == this){
			return true;
		}
		
		if(obj instanceof TimerEventImpl){
			return this.timerId.equals(((TimerEventImpl)obj).timerId);
		}else{
			return false;
		}
	}
	  
}
