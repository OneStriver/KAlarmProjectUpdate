package com.fh.mqtt;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import com.fh.alarmProcess.message.GlobalHashMap;
import com.fh.entity.PostedMsg;
import com.fh.readProperty.PropertyReadUtil;
import com.fh.service.alarmProcess.MqttMessageProcessService;
import com.fh.xmlParse.ParseTopicXmlUtil;

/**
 * MQTT消息处理类
 */
@Component
public class MqttMessageServer {

	private static Logger logger = Logger.getLogger(MqttMessageServer.class);
	private static final String clientId = "KProjectAlarmModule";
	private static MqttClient mqttClient = null;  
    private static int[] allQos;  
    private static String[] allTopics;
    private  MemoryPersistence persistence = new MemoryPersistence();
  	private  String broker = getBroker();
  	/*
  	private static AlarmProcessThread[] alarmStoredThread;
  	private static final int maxRevMsgCount = 10;
  	private int dbMsgCount = 0;
  	*/
  	private final BlockingQueue<PostedMsg> messageQueue = new LinkedBlockingQueue<PostedMsg>();
  	@Resource
	private MqttMessageProcessService mqttMessageProcessService;
    
	private  String getBroker(){
		String ip = PropertyReadUtil.getInstance().getOriginalAlarmMqttIp();
		Integer port = PropertyReadUtil.getInstance().getOriginalAlarmMqttPort();
		String broker = "tcp://"+ip+":"+port;
		return broker;
	}
	
	public MqttMessageServer() {
		List<String> topicList = ParseTopicXmlUtil.getInstance().parseTopicXml("/topicFile/rawAlarmTopic.xml");
    	allTopics = topicList.toArray(new String[topicList.size()]);
		allQos = new int[allTopics.length];
		for (int i = 0; i < topicList.size(); i++) {
			allQos[i] = 2;
		}
		//启动线程监听消息
		/*
		alarmStoredThread = new AlarmProcessThread[maxRevMsgCount];
		for (int i = 0; i < maxRevMsgCount; i++) {
			alarmStoredThread[i] = new AlarmProcessThread("ProcessThread"+i);
			alarmStoredThread[i].startThread();
		}
		*/
		//初始化参数
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
					//订阅主题
					subscribeInformation(mqttClient,allTopics, allQos);
			        logger.info(">>>>>>>>订阅所有主题成功>>>>>>>>");
				}
				
				@Override
		        public void connectionLost(Throwable throwable) {
		            logger.info(">>>>>>>>MQTT服务器失去连接！！！>>>>>>>>");
		        }
				
				@Override
		        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
					logger.info(">>>>>>>>接收到MQTT推送的消息:" + topic);
					logger.info(">>>>>>>>接收到MQTT推送的消息内容:" + new String(mqttMessage.getPayload(), 0, mqttMessage.getPayload().length,"UTF-8"));
					
					/*
					if (dbMsgCount >= maxRevMsgCount) {
						dbMsgCount = 0;
					}
					alarmStoredThread[dbMsgCount].putMqttMessage(mqttMessage, topic);
					dbMsgCount = dbMsgCount+1;
					*/
					PostedMsg postedMsg = new PostedMsg(mqttMessage,topic);
					messageQueue.offer(postedMsg);
		        }
				
				@Override
		        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
					logger.info("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
		        }
			});
			mqttClient.connect(connOpts);
			logger.info(">>>>>>>>连接MQTT服务器成功！！！,连接信息:"+broker);	
			
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							PostedMsg msg = (PostedMsg) messageQueue.take();
							if (msg != null) {
								MqttMessage mqttMessage = (MqttMessage) msg.getMsg();
								String strRevMsg = null;
								try {
									strRevMsg = new String(mqttMessage.getPayload(), 0, mqttMessage.getPayload().length,"UTF-8");
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								mqttMessageProcessService.processMqttMsg(msg.getTopic(), strRevMsg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			//缓存MqttClient实例在Map中
			GlobalHashMap.mqttClientMap.put("mqttClient", mqttClient);
		} catch (MqttException e) {
			e.printStackTrace();
		}
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
	public void send(String content, int qos, String topic) {
		try {
			MqttMessage message = new MqttMessage(content.getBytes("UTF-8"));
			message.setQos(qos);
			message.setRetained(false);
			mqttClient.publish(topic, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
