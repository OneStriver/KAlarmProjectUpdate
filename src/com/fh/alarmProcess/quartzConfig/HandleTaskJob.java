package com.fh.alarmProcess.quartzConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fh.alarmProcess.alarmMsgPojo.AlarmProcessPOJO;
import com.fh.alarmProcess.observerPattern.AlarmObserverStored;
import com.fh.util.TimeUtil;

public class HandleTaskJob implements Job {
	
	private static Logger logger = LoggerFactory.getLogger(HandleTaskJob.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("执行清除告警的当前时间:"+sdf.format(new Date()));
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	    AlarmProcessPOJO alarmProcessPOJO = 
	    		new AlarmProcessPOJO(dataMap.getString("source"), dataMap.getString("equipName"), dataMap.getInt("code"), 
	    				dataMap.getString("alarmType"), dataMap.getString("severity"), dataMap.getString("desc"), 
	    				dataMap.getString("cause"), dataMap.getString("treatment"), dataMap.getString("addition"), dataMap.getString("target"), 
	    				dataMap.getString("raised_time"), dataMap.getString("last_change_time"),
	    				dataMap.getString("ack_time"), dataMap.getString("ack_user"),
	    				TimeUtil.getNowTime(), "无(超时)",
	    				dataMap.getString("addition_pairs"),dataMap.getString("clear"));
	   AlarmObserverStored.getInstance().processMqttMessage(alarmProcessPOJO);
	}
	
}
