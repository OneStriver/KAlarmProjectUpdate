package com.fh.xmlParse;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.fh.entity.kAlarm.AlarmInfoEntity;

/**
 * 解析告警文件  equipAndAlarmMap缓存alarmXml文件中的数据
 */
public class ParseAlarmXmlUtil {

	private Map<String, ConcurrentHashMap<String, AlarmInfoEntity>> equipAndAlarmMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, AlarmInfoEntity>>();
	private Map<String,Object> alarmTypeMap = new ConcurrentHashMap<String,Object>();
	private Map<String,Object> alarmSeverityMap = new ConcurrentHashMap<String,Object>();

	public Map<String, ConcurrentHashMap<String, AlarmInfoEntity>> getEquipAndAlarmMap() {
		return equipAndAlarmMap;
	}

	public void setEquipAndAlarmMap(Map<String, ConcurrentHashMap<String, AlarmInfoEntity>> equipAndAlarmMap) {
		this.equipAndAlarmMap = equipAndAlarmMap;
	}
	
	public Map<String, Object> getAlarmTypeMap() {
		return alarmTypeMap;
	}

	public void setAlarmTypeMap(Map<String, Object> alarmTypeMap) {
		this.alarmTypeMap = alarmTypeMap;
	}

	public Map<String, Object> getAlarmSeverityMap() {
		return alarmSeverityMap;
	}

	public void setAlarmSeverityMap(Map<String, Object> alarmSeverityMap) {
		this.alarmSeverityMap = alarmSeverityMap;
	}

	// 单例
	private ParseAlarmXmlUtil() {

	}
	public static ParseAlarmXmlUtil getInstance() {
		return ParseAlarmXmlInstance.parseAlarmXmlUtil;
	}
	private static class ParseAlarmXmlInstance {
		private static ParseAlarmXmlUtil parseAlarmXmlUtil = new ParseAlarmXmlUtil();
	}

	public void getAlarmMap(String alarmXml) {
		String alarmXmlPath = ParseAlarmXmlUtil.class.getResource(alarmXml).getPath();
		String[] fileName = getFileName(alarmXmlPath);
		for (String name : fileName) {
			parseAlarmXml(alarmXmlPath + "/" + name);
			System.err.println(">>>>>>读取文件:" + name);
		}
	}

	private String[] getFileName(String path) {
		File file = new File(path);
		String[] fileName = file.list();
		return fileName;
	}
	
	private Map<String, ConcurrentHashMap<String, AlarmInfoEntity>> parseAlarmXml(String fileName) {
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(new File(fileName));
			Element alarm = document.getRootElement();
			Iterator<?> it = alarm.elementIterator();
			String equipType = null;
			while (it.hasNext()) {
				Element equipTypeAndAlm = (Element) it.next();
				if ("equipType".equals(equipTypeAndAlm.getName())) {
					equipType = equipTypeAndAlm.getTextTrim();
				} else if ("alarmInfoList".equals(equipTypeAndAlm.getName())) {
					ConcurrentHashMap<String, AlarmInfoEntity> codeAndAlarmInfo = new ConcurrentHashMap<String, AlarmInfoEntity>();
					Iterator<?> almInfo = equipTypeAndAlm.elementIterator();
					while (almInfo.hasNext()) {
						Element almInfoList = (Element) almInfo.next();
						if ("alarmInfo".equals(almInfoList.getName())) {
							AlarmInfoEntity alarmProperty = new AlarmInfoEntity();
							Iterator<?> almList = almInfoList.elementIterator();
							while (almList.hasNext()) {
								Element alm = (Element) almList.next();
								if ("code".equals(alm.getName())) {
									alarmProperty.setCode(alm.getTextTrim());
								} else if ("desc".equals(alm.getName())) {
									alarmProperty.setDesc(alm.getTextTrim());
								} else if ("severity".equals(alm.getName())) {
									alarmProperty.setSeverity(alm.getTextTrim());
								} else if ("cause".equals(alm.getName())) {
									alarmProperty.setCause(alm.getTextTrim());
								} else if ("treatment".equals(alm.getName())) {
									alarmProperty.setTreatment(alm.getTextTrim());
								} else if ("addition".equals(alm.getName())) {
									alarmProperty.setAddition(alm.getTextTrim());
								} else if ("autoClear".equals(alm.getName())) {
									@SuppressWarnings("unchecked")
									List<Attribute> attrs = alm.attributes();
									for (Attribute attr : attrs) {
										if ("enable".equals(attr.getName())) {
											alarmProperty.setClearEnable(attr.getValue());
										} else if ("timeoutInMS".equals(attr.getName())) {
											alarmProperty.setClearTimeOut(attr.getValue());
										}
									}
								}
							}
							codeAndAlarmInfo.put(alarmProperty.getCode(), alarmProperty);
							equipAndAlarmMap.put(equipType, codeAndAlarmInfo);
						}
					}
				}
			}
			return equipAndAlarmMap;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> parseAlarmTypeXml(String fileName){
		alarmTypeMap.clear();
		//创建SAXReader对象  
        SAXReader reader = new SAXReader();  
        try {
        	//读取文件 转换成Document  
        	Document document = reader.read(new File(fileName));
			//获取根节点元素对象  
			Element root = document.getRootElement();  
	        List<Element> elements = root.elements();
	        for (int i = 0; i < elements.size(); i++) {
	        	Element firstElement = elements.get(i);
				if("alarmType".equals(firstElement.getName())){
					List<Element> secondElement = firstElement.elements();
					alarmTypeMap.put(secondElement.get(0).getTextTrim(), secondElement.get(1).getTextTrim());
				}
			}
	        System.err.println("告警类型Map中的数量:"+alarmTypeMap.size());
	        return alarmTypeMap;
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}  
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> parseAlarmSeverityXml(String fileName){
		alarmSeverityMap.clear();
		//创建SAXReader对象  
        SAXReader reader = new SAXReader();  
        try {
        	//读取文件 转换成Document  
        	Document document = reader.read(new File(fileName));
			//获取根节点元素对象  
			Element root = document.getRootElement();  
	        List<Element> elements = root.elements();
	        for (int i = 0; i < elements.size(); i++) {
	        	Element firstElement = elements.get(i);
				if("alarmSeverity".equals(firstElement.getName())){
					List<Element> secondElement = firstElement.elements();
					alarmSeverityMap.put(secondElement.get(0).getTextTrim(), secondElement.get(1).getTextTrim());
				}
			}
	        System.err.println("告警优先级Map中的数量:"+alarmSeverityMap.size());
	        return alarmSeverityMap;
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}  
	}
	
}
