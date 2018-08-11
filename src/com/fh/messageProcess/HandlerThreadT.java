package com.fh.messageProcess;

import java.util.Observable;
import java.util.Observer;

public class HandlerThreadT extends HandlerThread implements ThreadTimer, Observer {

	public static final int TIMER_TYPE = 0x8000;
	public static final int TIMER_TYPE_TIMEOUT = 0x8001;
	public static final int TIMER_TYPE_PERIODIC = 0x8002;
	public static final int TIMER_TYPE_CANCEL = 0x8003;

	public HandlerThreadT() {
	}

	public HandlerThreadT(String name) {
		super(name);
	}

	@Override
	public void run() {
		while (true) {
			synchronized (lock) {
				if (msgQ.isEmpty()) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
				if (!msgQ.isEmpty()) {
					PostedMsg msg = msgQ.poll();
					if (msg.getType() == TIMER_TYPE)// timer msg.
					{
						TimeOutMsg tomsg = (TimeOutMsg) msg.getMsg();
						if (tomsg.getTimerId().equals("0")) {
							onTimer(tomsg.getTimerType(), tomsg.getUserData());
						} else {
							onTimer(tomsg.getTimerType(), tomsg.getTimerId(), tomsg.getUserData());
						}
					} else {
						onThreadMessage(msg.getType(), msg.getMsg(), msg.getTopic());
					}
				} else// TODO �˴����������޸�
				{
					nIdleCount++;
					if (onIdle(nIdleCount)) {
						nIdleCount = 0;
					}
				}
			}
		}
	}

	@Override
	public void onThreadMessage(int type, Object msg, String topic) {

	}

	@Override
	public void onTimer(String timerType, Object userData) {

	}

	public void onTimer(String timerType, String timerId, Object userData) {

	}

	// timeout unit is ms
	@Override
	public void startTimer(int timeOut, String timerType, Object userData) {
		TimerFacility timerFacility = TimerFacilityImpl.getInstance();
		TimerTask task = new TimerTask();
		timerFacility.schedule(task, timeOut, timerType, this, userData);
	}

	public void startTimer(int timeOut, String timerType, String timeId, Object userData) {
		TimerFacility timerFacility = TimerFacilityImpl.getInstance();
		TimerTask task = new TimerTask();
		timerFacility.schedule(task, timeOut, timerType, timeId, this, userData);
	}

	@Override
	public void cancelTimer(String timerType) {
		TimerFacility timerFacility = TimerFacilityImpl.getInstance();
		try {
			timerFacility.cancelTimer(timerType);
		} catch (TimerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void cancelTimer(String timerType, String timeId) {
		TimerFacility timerFacility = TimerFacilityImpl.getInstance();
		try {
			timerFacility.cancelTimer(timerType, timeId);
		} catch (TimerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void schedule(int delay, long interval, String timerType, Object userData) {
		TimerFacility timerFacility = TimerFacilityImpl.getInstance();
		TimerTask task = new TimerTask();
		timerFacility.schedule(task, delay, interval, timerType, this, userData);
	}

	public void schedule(TimerTask task, int delay, long interval, String timerType, Object userData) {
		TimerFacility timerFacility = TimerFacilityImpl.getInstance();
		timerFacility.schedule(task, delay, interval, timerType, this, userData);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
}
