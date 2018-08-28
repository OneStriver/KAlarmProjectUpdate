package com.fh.alarmProcess.quartzConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.fh.alarmProcess.alarmMsgPojo.AlarmProcessPOJO;
import com.fh.util.TimeUtil;

/**
 * 定时任务管理类
 */
public class QuartzManager {

	private static Logger logger = Logger.getLogger(QuartzManager.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

	/**
	 * 添加一个定时任务
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			Class jobClass, Integer timeInterval,AlarmProcessPOJO alarmProcessPOJO) {
		try {
			logger.info("添加任务的当前时间:"+sdf.format(new Date()));
			//创建scheduler任务
			Scheduler scheduler = schedulerFactory.getScheduler();
			// 创建一个JobDetail实例,并指定Job在Scheduler中所属组及名称
			JobDetail middleJobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName)
					.usingJobData("no", String.valueOf(alarmProcessPOJO.getNo()))
	        		.usingJobData("source", alarmProcessPOJO.getSource())
	        		.usingJobData("equipName", alarmProcessPOJO.getEquipName())
	        		.usingJobData("code", alarmProcessPOJO.getCode())
	        		.usingJobData("alarmType", alarmProcessPOJO.getAlarmType())
	        		.usingJobData("severity", alarmProcessPOJO.getSeverity())
	        		.usingJobData("desc", alarmProcessPOJO.getDesc())
	        		.usingJobData("cause", alarmProcessPOJO.getCause())
	        		.usingJobData("treatment", alarmProcessPOJO.getTreatment())
	        		.usingJobData("addition", alarmProcessPOJO.getAddition())
	        		.usingJobData("target", alarmProcessPOJO.getTarget())
	        		.usingJobData("raised_time", alarmProcessPOJO.getRaised_time())
	        		.usingJobData("last_change_time", alarmProcessPOJO.getLast_change_time())
	        		.usingJobData("ack_time", alarmProcessPOJO.getAck_time())
	        		.usingJobData("ack_user", alarmProcessPOJO.getAck_user())
	        		.usingJobData("cleared_time", TimeUtil.getNowTime())
	        		.usingJobData("addition_pairs", alarmProcessPOJO.getAddition_pairs())
	        		.usingJobData("updateTime", alarmProcessPOJO.getUpdateTime())
	        		.usingJobData("clear", alarmProcessPOJO.getClear())
	        		.usingJobData("alarmSingleFlag", alarmProcessPOJO.getAlarmSingleFlag())
	        		.build();
			/* 某个时间之后重复执行
			SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
	        		.startAt(DateBuilder.futureDate(timeInterval, DateBuilder.IntervalUnit.SECOND))
	        		.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();
			*/
			//某个时间之后只执行一次
			SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
	        		.startAt(DateBuilder.futureDate(timeInterval, DateBuilder.IntervalUnit.SECOND)).forJob(jobName, jobGroupName).build();
			scheduler.scheduleJob(middleJobDetail, simpleTrigger);
	        // 启动任务
	        if (!scheduler.isShutdown()) {
	        	scheduler.start();
	        }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();

			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

			scheduler.pauseTrigger(triggerKey);// 停止触发器
			scheduler.unscheduleJob(triggerKey);// 移除触发器
			scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
			logger.info("》》》移除定时自动清除的任务:"+sdf.format(new Date()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 启动所有定时任务
	 */
	public static void startJobs() {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭所有定时任务
	 */
	public static void shutdownJobs() {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			if (!scheduler.isShutdown()) {
				scheduler.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
