package com.fh.messageProcess;

public interface TimerFacility {

	/*public String setTimer(HandlerThread notifier, long startTime, Object userData)
			throws IllegalArgumentException, TimerException;

	public String setTimer(HandlerThread notifier, long startTime, long period,
			int numRepetitions,Object userData) throws IllegalArgumentException,
			TimerException;*/

	public void cancelTimer(String timerType) throws TimerException;
	public void cancelTimer(String timerType,String group) throws TimerException;
	/**
	 * ִ��һ��
	 * @param task
	 * @param delay �ӳ�
	 * @return
	 */
	public void schedule(TimerHandler task, int delay,String timerType,HandlerThread nofitier, Object userData);
	
	public void schedule(TimerHandler task, int delay,String timerType,String timeid,
			HandlerThread nofitier, Object userData);
	
	/**
	 * ִ�ж��
	 * @param task
	 * @param delay ��һ���ӳ�
	 * @param interval ÿ�εļ��
	 * @return
	 */
	public void schedule(TimerHandler task, int delay,long interval,String timerType,HandlerThread nofitier,Object userData);

	public void schedule(TimerHandler task, int delay,long interval,String timerType,
			String timerid,HandlerThread nofitier,Object userData);
	
	/**
	 * 
	 * @param timerID
	 * @param session
	 * @param calendarExpression
	 * @throws FacilityException
	 */
	public void setCalendarTimer(String timerID,HandlerThread notifier,String calendarExpression) throws TimerException ;
}