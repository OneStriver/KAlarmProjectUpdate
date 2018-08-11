package com.fh.messageProcess;

import java.text.ParseException;

import org.quartz.JobDetail;

public interface TimerFacilityEx extends TimerFacility {

	public String setTimerRunUntilCancel(String expression, JobDetail jobDetail)
			throws ParseException, TimerException;

}
