package com.fh.messageProcess;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TimerHandlerProxy implements Job{

	public final static String TASK="TIMER_TASK";
	public final static String TIMERID="TASKTIMERID";
	public final static String USERDATA="TASKUSERDATA";
	public final static String NOTIFIER="TASKNOTIFIER";
	public final static String MSGTYPE="TASKTIMERMSGTYPE";
	
	public TimerHandlerProxy(){}
	
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{
		TimerHandler task=(TimerHandler)context.getJobDetail().getJobDataMap().remove(TASK);
		String timerId=(String)context.getJobDetail().getJobDataMap().remove(TIMERID);
		Object userData=context.getJobDetail().getJobDataMap().remove(USERDATA);
		HandlerThread notifier=(HandlerThread)context.getJobDetail().getJobDataMap().remove(NOTIFIER);
		String timerType=(context.getJobDetail().getJobDataMap().remove(MSGTYPE)).toString();
		task.handleTimer(timerType,timerId, notifier, userData);
	}
	

}
