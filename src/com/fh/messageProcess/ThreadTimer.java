package com.fh.messageProcess;

public interface ThreadTimer 
{
	/*
	 * timeOut(ms)����ʱʱ��
	 * 
	 */
	public void startTimer(int timeOut, String timerType, Object userData);
	
	/*
	 * ��ʱִ������
	 * delay(ms):�ö�ʱ����delay�����������
	 * interval(ms):ʱ����
	 */
	public void schedule(int delay,long interval,String timerType,Object userData); 

	public void cancelTimer(String timerType);
	
	/*
	 * ����ʱ����ʱʱ�����ص��÷�����
	 */
	public void onTimer(String timerType, Object userData);
}
