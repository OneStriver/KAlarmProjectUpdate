package com.fh.alarmProcess.mqttMsgProcess;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fh.messageProcess.HandlerThreadT;
import com.fh.messageProcess.PostedMsg;

/**
 * 处理接收MQTT告警消息的线程 
 */
public class AlarmProcessThread extends HandlerThreadT {
	
	public AlarmProcessThread(String name) {
		super(name);
	}

	@Override
	public void onThreadMessage(int msgType,Object obj,String topic) {
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
	
	public void postThreadMessage(Object msg,String topic){
		PostedMsg postedMsg=new PostedMsg(msg,topic);
		synchronized(lock){
			try {
				msgQ.add(postedMsg);
			} catch (Exception e) {
				System.out.println("thread  queue  add  exception");
				e.printStackTrace();
			}
			lock.notifyAll();
		}
	}
	
}