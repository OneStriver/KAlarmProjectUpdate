package com.fh.messageProcess;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TimerFacilityTimerJob implements Job {

	public static final String TIMER_ID = "TIMERID";

	//public static final String ENDPOINT = "ENDPOINT";

	public static final String TIMER_EVENT_TYPE = "TIMEREVENTTYPE";

	//public static final String ACTIVITY = "ACTIVITY";
	
	public static final String ADDRESS = "ADDRESS";
	
//	private static TimerFacilityImpl timerFaclity;
//	
//	public static void setTimerFacility(TimerFacilityImpl facility){
//		timerFaclity = facility;
//	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		/*
		try{
			String id = (String)context.getJobDetail().getJobDataMap().get(
					TIMER_ID);
			SleeEndPoint endpoint = (SleeEndPoint) context
					.getJobDetail().getJobDataMap().get(ENDPOINT);

			Object activity = (String) context.getJobDetail().getJobDataMap().get(ACTIVITY);
			
			
			long scheduledTime = context.getScheduledFireTime().getTime();
			long firedTime = System.currentTimeMillis();
			long delay = firedTime - scheduledTime;
			
			if(delay != 0){
				timerFaclity.reportJobDelayTime(delay);
			}
						
			TimerEvent timerEvent = new TimerEventImpl(id);
            ////Ĭ�ϸ����ȼ�
			endpoint.fireEvent(activity, SleeEventFactory.getInstance().createHighPrioritySleeEvent(timerEvent), null);
		}catch(Throwable t){
			throw new JobExecutionException(t);
		}
		*/
	}

}
