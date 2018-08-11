package com.fh.messageProcess;

public interface TimerHandler 
{
	public void handleTimer(String msgType,String timerId, HandlerThread nofitier, Object userData);
}
