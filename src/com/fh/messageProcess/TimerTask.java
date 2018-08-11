package com.fh.messageProcess;

public class TimerTask implements TimerHandler 
{
	public void handleTimer(String timerType, String timerId, HandlerThread notifier,Object userData) 
	{
		notifier.postThreadMessage(HandlerThreadT.TIMER_TYPE,new TimeOutMsg(timerId,timerType,userData));
	}

}
