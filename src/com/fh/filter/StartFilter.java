package com.fh.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fh.alarmProcess.message.GlobalHashMap;
import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.entity.PageData;
import com.fh.entity.alarmAttr.AlarmAttributeEntity;
import com.fh.entity.faultManagement.historyAlarm.DeviceType;
import com.fh.mqtt.MqttMessageServer;
import com.fh.service.alarmAttr.AlarmAttributeService;
import com.fh.service.faultManagement.historyAlarm.HistoryAlarmService;

/**
 * 启动TOMCAT时运行此类,主要是做初始化订阅MQTT和添加观察者模式
 */
public class StartFilter extends BaseController implements Filter{
	
	private HistoryAlarmService historyAlarmService;
	private AlarmAttributeService alarmAttributeService;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
        ServletContext servletContext = filterConfig.getServletContext();
    	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    	historyAlarmService = (HistoryAlarmService)ctx.getBean("historyAlarmService");
    	alarmAttributeService = (AlarmAttributeService)ctx.getBean("alarmAttributeService");
    	Page page = new Page();
    	PageData pd =new PageData();
		//缓存数据库中的设备类型信息
        try {
        	List<DeviceType> allDeviceType = historyAlarmService.findAllDeviceType(pd);
        	for (DeviceType deviceType : allDeviceType) {
        		GlobalHashMap.equipNameSourceCodeInfoMap.put(deviceType.getEquipResource()+"-"+deviceType.getEquipId(), deviceType.getDeviceName());
        	}
        	GlobalHashMap.historyAlarmServiceMap.put("historyAlarmServiceInstance", historyAlarmService);
        	List<AlarmAttributeEntity> alarmAttributeList = alarmAttributeService.list(page);
        	GlobalHashMap.alarmAttributeMap.put("alarmAttributeList", alarmAttributeList);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        //缓存所有的告警类型
       // String alarmTypeXmlPath = StartFilter.class.getResource("/alarmXml").getPath();
       // ParseAlarmXmlUtil.getInstance().parseAlarmTypeXml(alarmTypeXmlPath+"/AlarmType.xml");
        //缓存所有的告警级别
       // String alarmSeverityXmlPath = StartFilter.class.getResource("/alarmXml").getPath();
       // ParseAlarmXmlUtil.getInstance().parseAlarmSeverityXml(alarmSeverityXmlPath+"/AlarmSeverity.xml");
        //订阅MQTT消息
        MqttMessageServer.getInstance();
        
	}
	
	public void destroy() {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
	}

}
