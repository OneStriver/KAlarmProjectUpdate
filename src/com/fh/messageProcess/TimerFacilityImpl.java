package com.fh.messageProcess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


/**
 * Ŀǰʹ�õ�quartz������quartz����Ĭ�����ã�ʹ��10���̷߳���timer�¼���
 * 
 */
public class TimerFacilityImpl implements TimerFacilityEx, TimerFacilityManagement {

	private static final String TRIGGER_GROUP = "triggerGroup";

	private static final String JOB_DETAIL_GROUP = "jobDetailGroup";

	private Scheduler scheduler;

	private SchedulerFactory schedulerFactory;

	private static TimerFacilityImpl instance;

	private IdGenerator idGenerator;

	private TimerFacilityImpl(String fpath) {

		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fpath);
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		/*
		 * prop.put("org.quartz.scheduler.instanceName",
		 * "DefaultQuartzScheduler");
		 * prop.put("org.quartz.scheduler.rmi.export", "false");
		 * prop.put("org.quartz.scheduler.rmi.proxy", "false");
		 * prop.put("org.quartz.scheduler.wrapJobExecutionInUserTransaction",
		 * "false"); prop.put("org.quartz.threadPool.class",
		 * "org.quartz.simpl.SimpleThreadPool");
		 * prop.put("org.quartz.threadPool.threadCount", "10");
		 * prop.put("org.quartz.threadPool.threadPriority", "5"); prop.put(
		 * "org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread"
		 * ,true); prop.put("org.quartz.jobStore.misfireThreshold", "60000");
		 * prop.put("org.quartz.jobStore.class",
		 * "org.quartz.simpl.RAMJobStore");
		 */
		try {
			schedulerFactory = new StdSchedulerFactory(prop);
			scheduler = schedulerFactory.getScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			// SystemStartupInfoPrinter.startUpFailed("TimerFacility");
			System.out.println("timer facility start FAIL!");
			e.printStackTrace();
		}
		setIdGenerator(new IdGeneratorImpl());
		// TimerFacilityTimerJob.setTimerFacility(this);
	}

	public static synchronized TimerFacilityImpl getInstance() {
		if (instance == null) {
			// Wrap the jar packet <Modify>
//			String paths = TimerFacilityImpl.class.getResource("/conf/quartz.properties").getPath();
//			System.out.println("= = >>> TimerPath:" + paths);
//			instance = new TimerFacilityImpl(paths);
			instance = new TimerFacilityImpl("conf/quartz.properties");
		}
		return instance;
	}

	public static void init(String fpath) {
		// String paths =
		// TimerFacilityImpl.class.getResource("/conf/quartz.properties").getPath();
		instance = new TimerFacilityImpl(fpath);
	}

	public void cancelTimer(String timerType) throws TimerException {

		try {
			JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(timerType, JOB_DETAIL_GROUP));
			if (jobDetail != null) {
				scheduler.deleteJob(JobKey.jobKey(timerType, JOB_DETAIL_GROUP));
			}
		} catch (SchedulerException e) {
			throw new TimerException("failed when getting jobDetail !", e);
		}
	}

	public void cancelTimer(String timerType, String group) throws TimerException {

		try {
			JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(timerType, group));

			if (jobDetail != null) {
				scheduler.deleteJob(JobKey.jobKey(timerType, group));
			}

		} catch (SchedulerException e) {
			throw new TimerException("failed when getting jobDetail !", e);
		}

	}

	private AtomicLong totalJobDelayInMilliSeconds = new AtomicLong();

	// ��Ϊƽ��ֵ����ͳ��ֵ, �������ܴ�, ���ں����1��2��
	private AtomicLong reportedTimes = new AtomicLong(1);

	protected void reportJobDelayTime(long delay) {
		totalJobDelayInMilliSeconds.addAndGet(delay);
		reportedTimes.incrementAndGet();
		if (totalJobDelayInMilliSeconds.get() == Long.MAX_VALUE) {
			totalJobDelayInMilliSeconds.set(0);
			reportedTimes.set(1);
		}
	}

	public long getAverageJobDelayTime() {
		if (reportedTimes.get() == 0) {
			return 0;
		} else {
			return totalJobDelayInMilliSeconds.get() / reportedTimes.get();
		}
	}

	public void setIdGenerator(IdGenerator generator) {
		idGenerator = generator;
	}

	public String setTimerRunUntilCancel(String expression, JobDetail jobDetail) throws ParseException, TimerException {

		String timerId = idGenerator.genId();

		/*
		 * jobDetail.setName(timerId); jobDetail.setGroup(JOB_DETAIL_GROUP);
		 * 
		 * CronExpression cronExpression = new CronExpression(expression);
		 * 
		 * CronTrigger cronTrigger = new CronTrigger(timerId, TRIGGER_GROUP);
		 * cronTrigger.setCronExpression(cronExpression); try {
		 * scheduler.scheduleJob(jobDetail, cronTrigger); } catch
		 * (SchedulerException e) { throw new
		 * TimerException("faild to scheduler the job!", e); }
		 */

		return timerId;
	}

	public void schedule(TimerHandler task, int delay, String timerType, HandlerThread nofitier, Object userData) {

		this.schedule(task, delay, 0, timerType, nofitier, userData);

	}

	public void schedule(TimerHandler task, int delay, String timerType, String group, HandlerThread nofitier,
			Object userData) {

		this.schedule(task, delay, 0, timerType, group, nofitier, userData);

	}

	public void schedule(TimerHandler task, int delay, long interval, String timerType, HandlerThread nofitier,
			Object userData) {

		// String timerID = idGenerator.genId();
		JobDetail jobDetail = JobBuilder.newJob(TimerHandlerProxy.class).withIdentity(timerType, JOB_DETAIL_GROUP)
				.build();

		jobDetail.getJobDataMap().put(TimerHandlerProxy.TASK, task);
		jobDetail.getJobDataMap().put(TimerHandlerProxy.TIMERID, timerType);
		jobDetail.getJobDataMap().put(TimerHandlerProxy.USERDATA, userData);
		jobDetail.getJobDataMap().put(TimerHandlerProxy.NOTIFIER, nofitier);
		jobDetail.getJobDataMap().put(TimerHandlerProxy.MSGTYPE, timerType);
		// jobDetail.getJobDataMap().put(TimerFacilityTimerJob.TIMER_ID,
		// timerID);

		SimpleTrigger simpleTrigger = null;

		if (interval > 0) {
			simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(timerType, TRIGGER_GROUP)
					.withSchedule(
							SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).repeatForever())
					.startAt(DateBuilder.futureDate(delay, DateBuilder.IntervalUnit.MILLISECOND)).build();
		} else {
			simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(timerType, TRIGGER_GROUP)
					.startAt(DateBuilder.futureDate(delay, DateBuilder.IntervalUnit.MILLISECOND)).build();
		}
		try {
			JobDetail jd = scheduler.getJobDetail(jobDetail.getKey());
			if (jd == null) {// �ж��Ƿ����д�job���ڴ���job�����Unable to store Job
								// ���쳣���˹��ܻ��������֤��
				Date ft = scheduler.scheduleJob(jobDetail, simpleTrigger);
				System.out.println(jobDetail.getKey() + " will run at: " + ft);
			}
		} catch (SchedulerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// return timerID;
	}

	public void schedule(TimerHandler task, int delay, long interval, String timerType, String group,
			HandlerThread nofitier, Object userData) {

		// String timerID = idGenerator.genId();
		JobDetail jobDetail = JobBuilder.newJob(TimerHandlerProxy.class).withIdentity(timerType, group).build();

		jobDetail.getJobDataMap().put(TimerHandlerProxy.TASK, task);
		jobDetail.getJobDataMap().put(TimerHandlerProxy.TIMERID, group);
		jobDetail.getJobDataMap().put(TimerHandlerProxy.USERDATA, userData);
		jobDetail.getJobDataMap().put(TimerHandlerProxy.NOTIFIER, nofitier);
		jobDetail.getJobDataMap().put(TimerHandlerProxy.MSGTYPE, timerType);
		// jobDetail.getJobDataMap().put(TimerFacilityTimerJob.TIMER_ID,
		// timerID);

		SimpleTrigger simpleTrigger = null;

		if (interval > 0) {
			simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(timerType, group)
					.withSchedule(
							SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).repeatForever())
					.startAt(DateBuilder.futureDate(delay, DateBuilder.IntervalUnit.MILLISECOND)).build();
		} else {
			simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(timerType, group)
					.startAt(DateBuilder.futureDate(delay, DateBuilder.IntervalUnit.MILLISECOND)).build();
		}
		try {
			Date ft = scheduler.scheduleJob(jobDetail, simpleTrigger);
			System.out.println(jobDetail.getKey() + " will run at: " + ft);
		} catch (SchedulerException e) {
			System.err.println(e.getMessage());
		}
		// return timerID;

	}

	public void setCalendarTimer(String timerID, HandlerThread notifier, String calendarExpression)
			throws TimerException {

		if ((timerID == null) || (timerID.length() == 0)) {
			timerID = idGenerator.genId();

		}
		/*
		 * Object activity = ((SleeSessionEx) session).getActivity();
		 * 
		 * 
		 * JobDetail jobDetail = new JobDetail(timerID, JOB_DETAIL_GROUP,
		 * TimerFacilityTimerJob.class);
		 * 
		 * jobDetail.getJobDataMap().put(TimerFacilityTimerJob.TIMER_ID,
		 * timerID);
		 * jobDetail.getJobDataMap().put(TimerFacilityTimerJob.ENDPOINT,
		 * endpoint);
		 * 
		 * jobDetail.getJobDataMap().put(TimerFacilityTimerJob.ACTIVITY,
		 * activity);
		 * 
		 * 
		 * CronExpression cronExpression=null; try { cronExpression = new
		 * CronExpression(calendarExpression); } catch (ParseException e1) {
		 * throw new TimerException("calendarExpression is bad format", e1); }
		 * 
		 * CronTrigger cronTrigger = new CronTrigger(timerID, TRIGGER_GROUP);
		 * cronTrigger.setCronExpression(cronExpression); try {
		 * scheduler.scheduleJob(jobDetail, cronTrigger); } catch
		 * (SchedulerException e) { throw new
		 * TimerException("faild to scheduler the job!", e); }
		 */
	}

}
