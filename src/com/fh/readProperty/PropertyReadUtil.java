package com.fh.readProperty;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于读取配置文件上的配置信息并初始化对应属性
 */
public class PropertyReadUtil {
	// 获取log对象
	private static final Logger log = LoggerFactory.getLogger(PropertyReadUtil.class);

	private static PropertyReadUtil context;
	// 配置文件路径
	private static String location = PropertyReadUtil.class.getResource("/conf/userDefine.properties").getPath();

	private static Properties properties;
	
	// 超时时长
	private Integer alarmTimeOut;
	private String originalAlarmMqttIp;
	private Integer originalAlarmMqttPort;
	// 项目名称
	private String projectName;
	
	/**
	 * 私有构造器
	 */
	private PropertyReadUtil() {
		getContext();
	}

	/**
	 * 获取OmcServerContext实例
	 */
	public synchronized static PropertyReadUtil getInstance() {
		if (context == null) {
			context = new PropertyReadUtil();
		}
		return context;
	}

	/**
	 * 初始化属性
	 */
	private void getContext() {
		this.alarmTimeOut = Integer.valueOf(getProperty("alarmTimeOut", "5"));
		this.originalAlarmMqttIp = getProperty("originalAlarmMqttIp", "127.0.0.1");
		this.originalAlarmMqttPort = Integer.valueOf(getProperty("originalAlarmMqttPort", "1883"));
		this.projectName = getProperty("projectName", "1510");
	}

	/**
	 * 加载配置文件
	 */
	private static Properties getProperties(String location) {
		if (properties == null) {
			properties = new Properties();
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(location);
				properties.load(fis);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				try {
					if (fis != null)
						fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	/**
	 * 从配置文件上读取属性信息
	 */
	private String getProperty(String propertyName, String defaultValue) {
		String value = getProperties(location).getProperty(propertyName, defaultValue);
		if (value == null) {
			log.warn(propertyName + " was not found in " + location);
			return null;
		}
		return value.trim();
	}

	/**
	 * 设置配置文件路径
	 */
	public void setLocation(String path) {
		if (path.startsWith("classpath:")) {
			location = PropertyReadUtil.class.getClassLoader().getResource(path.substring(10)).getPath();
		} else {
			location = path;
		}
	}
	
	public Integer getAlarmTimeOut() {
		return alarmTimeOut;
	}

	public void setAlarmTimeOut(Integer alarmTimeOut) {
		this.alarmTimeOut = alarmTimeOut;
	}

	public String getOriginalAlarmMqttIp() {
		return originalAlarmMqttIp;
	}

	public void setOriginalAlarmMqttIp(String originalAlarmMqttIp) {
		this.originalAlarmMqttIp = originalAlarmMqttIp;
	}

	public Integer getOriginalAlarmMqttPort() {
		return originalAlarmMqttPort;
	}

	public void setOriginalAlarmMqttPort(Integer originalAlarmMqttPort) {
		this.originalAlarmMqttPort = originalAlarmMqttPort;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}