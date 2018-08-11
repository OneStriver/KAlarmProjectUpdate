package com.fh.mqtt;

import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fh.alarmProcess.mqttMsgProcess.AlarmProcessThread;
import com.fh.readProperty.PropertyReadUtil;
import com.fh.xmlParse.ParseTopicXmlUtil;

/**
 * MQTT消息处理类
 */
public class MqttMessageServer {

	private static Logger logger = LoggerFactory.getLogger(MqttMessageServer.class);
	private static final String clientId = "KProjectAlarmModule";
	private MqttClient mqttClient = null;  
    private int[] allQos;  
    private String[] allTopics;
    private  MemoryPersistence persistence = new MemoryPersistence();
    //服务器IP
  	private  String broker = getBroker();
  	private static final int dbThreadCount = 8;
  	private AlarmProcessThread[] alarmStoredThread;
  	private static final int maxRevMsgCount = 65535;
  	private int dbMsgCount;
  	
    {
    	List<String> topicList = ParseTopicXmlUtil.getInstance().parseTopicXml("/topicFile/rawAlarmTopic.xml");
    	allTopics = topicList.toArray(new String[topicList.size()]);
		allQos = new int[allTopics.length];
		for (int i = 0; i < topicList.size(); i++) {
			allQos[i] = 2;
		}
    }
    
	private  String getBroker(){
		String ip = PropertyReadUtil.getInstance().getOriginalAlarmMqttIp();
		Integer port = PropertyReadUtil.getInstance().getOriginalAlarmMqttPort();
		String broker = "tcp://"+ip+":"+port;
		return broker;
	}
	
	private volatile static MqttMessageServer mqttMessageServer;
	private MqttMessageServer(){
		MqttConnectOptions connOpts = new MqttConnectOptions();    
        connOpts.setCleanSession(true);  
        connOpts.setConnectionTimeout(30);  
        connOpts.setKeepAliveInterval(60);
        connOpts.setAutomaticReconnect(true);
        
        try {
			mqttClient = new MqttClient(broker, clientId, persistence);
			mqttClient.setCallback(new MqttCallbackExtended() {
				@Override
				public void connectComplete(boolean arg0, String arg1) {
					
					subscribeInformation(mqttClient,allTopics, allQos);
			        logger.info(">>>>>>>>订阅所有主题成功>>>>>>>>");
			        
			        alarmStoredThread = new AlarmProcessThread[dbThreadCount];
					for (int i = 0; i < dbThreadCount; i++) {
						alarmStoredThread[i] = new AlarmProcessThread("ProcessThread"+i);
						alarmStoredThread[i].startThread();
					}
				}
				
				@Override
		        public void connectionLost(Throwable throwable) {
		            logger.info(">>>>>>>>MQTT服务器失去连接！！！>>>>>>>>");
		        }
				
				@Override
		        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
					logger.info(">>>>>>>>接收到MQTT推送的消息:" + topic);
					dbMsgCount++;
					if (dbMsgCount > maxRevMsgCount) {
						dbMsgCount = 0;
					}
					int i = dbMsgCount % dbThreadCount;
					alarmStoredThread[i].postThreadMessage(mqttMessage, topic);
		        }
				
				@Override
		        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
					logger.info("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
		        }
			});
			mqttClient.connect(connOpts);
			logger.info(">>>>>>>>连接MQTT服务器成功！！！,连接信息:"+broker);			
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	public static MqttMessageServer getInstance(){
		try {
			if(mqttMessageServer != null){
				
			}else{
				synchronized(MqttMessageServer.class){
					if(mqttMessageServer == null){
						mqttMessageServer = new MqttMessageServer();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mqttMessageServer;
	}
    
	private void subscribeInformation(MqttClient mqttClient,String[] allTopics,int[] allQos) {  
        try {
			mqttClient.subscribe(allTopics, allQos);
		} catch (MqttException e) {
			e.printStackTrace();
		} 
    }

	/**
	 * PUSH消息MQTT
	 */
	public synchronized void send(String content, int qos, String topic) {
		try {
			MqttMessage message = new MqttMessage(content.getBytes("UTF-8"));
			message.setQos(qos);
			message.setRetained(false);
			MqttTopic mqttTopic =  mqttClient.getTopic(topic);
			mqttTopic.publish(message.getPayload(), qos, false);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		}
	}
}
