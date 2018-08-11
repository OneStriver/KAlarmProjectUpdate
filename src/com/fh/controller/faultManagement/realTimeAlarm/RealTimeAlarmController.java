package com.fh.controller.faultManagement.realTimeAlarm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.PageData;
import com.fh.entity.faultManagement.historyAlarm.DbAlarmLog;
import com.fh.entity.faultManagement.realTimeAlarm.ConfirmClearAlarmEntity;
import com.fh.service.faultManagement.realTimeAlarm.RealTimeAlarmService;
import com.fh.util.TimeUtil;
import com.google.gson.Gson;

/** 
 * 说明：实时告警监控
 * 创建人：FH 
 */
@Controller
@RequestMapping(value="/realTimeAlarm")
public class RealTimeAlarmController extends BaseController {
	
	@Resource(name="realTimeAlarmService")
	private RealTimeAlarmService realTimeAlarmService;
	
	/**
	 * 转到实时告警监控页面
	 */
	@RequestMapping(value="/toRealTimeAlarmPage")
	public ModelAndView toRealTimeAlarmPage()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.addObject("pd", pd);	
		mv.setViewName("faultManagement/realTimeAlarm/realTimeAlarm_list");
		return mv;
	}
	
	/**
	 * 确认实时告警数据(2是确认)
	 */
	@RequestMapping(value="/confirmRealTimeAlarm", method={RequestMethod.POST},produces="text/html;charset=UTF-8")
	@ResponseBody
	public String confirmRealTimeAlarm(@RequestBody List<ConfirmClearAlarmEntity> confirmClearList) throws Exception{
		logBefore(logger, ">>>>>>确认实时告警>>>>>>");
		PageData pd = new PageData();
		String time = TimeUtil.getNowTime();
		for (int i = 0; i < confirmClearList.size(); i++) {
			ConfirmClearAlarmEntity confirmClearAlarmEntity = confirmClearList.get(i);
			pd.put("serialNumber", confirmClearAlarmEntity.getSerialNumber());
			List<DbAlarmLog> alarmLogs = realTimeAlarmService.selectRealTimeAlarmLogByNo(pd);
			DbAlarmLog dbAlarmLog = alarmLogs.get(0);
			dbAlarmLog.setAckTime(time);
			dbAlarmLog.setAckUserName(confirmClearAlarmEntity.getAlarmAckPerson());
			realTimeAlarmService.confirmOrClearAlarmLogByObject(dbAlarmLog);
		}
		Gson gson = new Gson();
		String userStr = gson.toJson("0");
		return userStr;
	}
	
	/**
	 * 清除实时告警数据(1是清除)
	 */
	@RequestMapping(value="/clearRealTimeAlarm", method={RequestMethod.POST},produces="text/html;charset=UTF-8")
	@ResponseBody
	public String clearRealTimeAlarm(@RequestBody List<ConfirmClearAlarmEntity> confirmClearList) throws Exception{
		logBefore(logger, ">>>>>>清除实时告警>>>>>>");
		PageData pd = new PageData();
		String time = TimeUtil.getNowTime();
		for (int i = 0; i < confirmClearList.size(); i++) {
			ConfirmClearAlarmEntity confirmClearAlarmEntity = confirmClearList.get(i);
			pd.put("serialNumber", confirmClearAlarmEntity.getSerialNumber());
			List<DbAlarmLog> alarmLogs = realTimeAlarmService.selectRealTimeAlarmLogByNo(pd);
			DbAlarmLog dbAlarmLog = alarmLogs.get(0);
			dbAlarmLog.setClearTime(time);
			dbAlarmLog.setClearUserName(confirmClearAlarmEntity.getAlarmClearPerson());
			realTimeAlarmService.confirmOrClearAlarmLogByObject(dbAlarmLog);
		}
		Gson gson = new Gson();
		String returnStr = gson.toJson("0");
		return returnStr;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
