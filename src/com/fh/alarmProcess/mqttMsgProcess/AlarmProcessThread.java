package com.fh.alarmProcess.mqttMsgProcess;

import java.io.UnsupportedEncodingException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fh.entity.PostedMsg;

/**
 * 处理接收MQTT告警消息的线程 
 */
public class AlarmProcessThread implements Runnable {
	
	private String name;
	private Thread thread;
	private Queue<PostedMsg> messageQueue = new ConcurrentLinkedQueue<PostedMsg>();
	
	public AlarmProcessThread(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		while (true) {
			if (!messageQueue.isEmpty()) {
				PostedMsg msg = (PostedMsg) messageQueue.poll();
				onThreadMessage(msg.getType(), msg.getMsg(), msg.getTopic());
			}
		}
	}
	
	public void startThread() {
		thread = new Thread(this, name);
		thread.start();
	}
	
	private void onThreadMessage(int msgType,Object obj,String topic) {
		MqttMessage mqttMessage = (MqttMessage)obj;
		String strRevMsg = null;
		try {
			strRevMsg = new String(mqttMessage.getPayload(), 0, mqttMessage.getPayload().length, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (strRevMsg == null) {
			return;
		}
		ProcessAlarmProcessMsg processAlarmProcessMsg = new ProcessAlarmProcessMsg();
		processAlarmProcessMsg.processAlarm(topic,strRevMsg);
	}
	
	public void putMqttMessage(Object msg,String topic){
		PostedMsg postedMsg=new PostedMsg(msg,topic);
		messageQueue.offer(postedMsg);
	}
	
}