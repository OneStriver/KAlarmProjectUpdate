package com.fh.messageProcess;

import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class HandlerThread extends Observable implements Runnable {

	private String name;
	private Thread thread;

	protected int nIdleCount;
	protected Queue<PostedMsg> msgQ = new ConcurrentLinkedQueue<PostedMsg>();
	protected Object lock = new Object();

	public static final int MSG_TYPE = 0x1000;

	public HandlerThread() {
	}

	public HandlerThread(String name) {
		this.name = name;
	}

	public void run() {
		while (true) {
			synchronized (lock) {
				if (msgQ.isEmpty()) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (!msgQ.isEmpty()) {
					PostedMsg msg = (PostedMsg) msgQ.poll();
					onThreadMessage(msg.getType(), msg.getMsg(), msg.getTopic());
				} else {
					nIdleCount++;
					if (onIdle(nIdleCount)) {
						nIdleCount = 0;
					}
				}
			}
		}
	}

	public void startThread() {
		createThread();
	}

	private void createThread() {
		thread = new Thread(this, name + "Thread");
		thread.start();
	}

	public void postThreadMessage(int type, Object msg) {
		PostedMsg postedMsg = new PostedMsg(type, msg);
		synchronized (lock) {
			try {
				msgQ.add(postedMsg);
			} catch (Exception e) {
				System.err.println("= = >> thread queue add exception");
				e.printStackTrace();
			}
			lock.notifyAll();
		}
	}

	public void onThreadMessage(int string, Object msg, String topic) {

	}

	public boolean onIdle(int count) {
		if (count > 10) {
			// Defalut idle process,do nothing.
			return true;
		}
		return false;
	}
}
