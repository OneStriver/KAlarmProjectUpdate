package com.fh.controller.faultManagement.historyAlarm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.alarmProcess.message.GlobalHashMap;
import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.entity.PageBean;
import com.fh.entity.PageData;
import com.fh.entity.alarmAttr.AlarmAttributeEntity;
import com.fh.entity.faultManagement.historyAlarm.DbAlarmLog;
import com.fh.entity.faultManagement.historyAlarm.DeviceType;
import com.fh.entity.faultManagement.historyAlarm.HistoryAlarmPieChartData;
import com.fh.entity.faultManagement.historyAlarm.OptionQueryAlarm;
import com.fh.entity.faultManagement.realTimeAlarm.ConfirmClearAlarmEntity;
import com.fh.entity.kAlarm.AlarmInfoEntity;
import com.fh.entity.system.User;
import com.fh.service.alarmAttr.AlarmAttributeService;
import com.fh.service.faultManagement.historyAlarm.HistoryAlarmService;
import com.fh.service.faultManagement.realTimeAlarm.RealTimeAlarmService;
import com.fh.util.TimeUtil;
import com.fh.util.excel.HistoryAlarmExcelView;
import com.fh.xmlParse.ParseAlarmXmlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

/** 
 * 说明：历史告警查询
 * 创建人：FH 
 */
@Controller
@RequestMapping(value="/historyAlarm")
public class HistoryAlarmController extends BaseController {
	
	//声明缓存告警级别关系的Map
	//private ConcurrentHashMap<String, String> cacheAlarmLevelMap = new ConcurrentHashMap<String, String>();
	//缓存用户
	private ConcurrentHashMap<String, User> cacheUserMap = new ConcurrentHashMap<String, User>();
	//声明存储导出告警数据的List
	private List<DbAlarmLog> exportAlarmLog = new ArrayList<DbAlarmLog>();
	private List<AlarmAttributeEntity> alarmAttributeList;
	private AlarmAttributeEntity alarmAttributeEntity;
	
	@Resource(name="historyAlarmService")
	private HistoryAlarmService historyAlarmService;
	@Resource(name="realTimeAlarmService")
	private RealTimeAlarmService realTimeAlarmService;
	@Resource(name="alarmAttributeService")
	private AlarmAttributeService alarmAttributeService;
	
	@PostConstruct
	public void cacheAlarmAttribute() throws Exception {
		Page page = new Page();
		alarmAttributeList = alarmAttributeService.list(page);
    }
	
	@PostConstruct
	public void cacheUser() {
		cacheUserMap.clear();
    }
	
	/**
	 * 转到告警查询页面
	 */
	@RequestMapping(value="/toHistoryAlarmPage")
	public ModelAndView toHistoryAlarmPage()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.addObject("pd", pd);	
		mv.setViewName("faultManagement/historyAlarm/historyAlarm_list");
		return mv;
	}
	
	/**
	 * 获取设备类型数据
	 */
	@RequestMapping(value="/getDeviceType",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDeviceType() throws Exception{
		logBefore(logger, "获取历史告警查询的设备类型");
		List<DeviceType> list = new ArrayList<DeviceType>();
		PageData pd = new PageData();
		pd = this.getPageData();
		List<DeviceType> allDeviceType = historyAlarmService.findAllDeviceType(pd);
		System.err.println("获取所有的设备类型:"+allDeviceType.size());
		for (DeviceType deviceType : allDeviceType) {
			list.add(deviceType);
		}
		Gson gson = new Gson();
		String jsonStr = gson.toJson(list);
		return jsonStr;
	}
	
	/**
	 * 历史告警清除(1是清除)
	 */
	@RequestMapping(value="/clearHistoryAlarm")
	@ResponseBody
	public String clearHistoryAlarm(@RequestParam("confirmClearList") String confirmClearListStr) throws Exception{
		logBefore(logger, ">>>>>>清除实时告警>>>>>>");
		DbAlarmLog dbAlarmLog = new DbAlarmLog();
		String time = TimeUtil.getNowTime();
		Gson gson = new Gson();
		try {
			List<ConfirmClearAlarmEntity> confirmClearList = gson.fromJson(confirmClearListStr,new TypeToken<List<ConfirmClearAlarmEntity>>(){}.getType());
			for (int i = 0; i < confirmClearList.size(); i++) {
				ConfirmClearAlarmEntity confirmClearAlarmEntity = confirmClearList.get(i);
				dbAlarmLog.setSerialNumber(Long.valueOf(confirmClearAlarmEntity.getSerialNumber()));
				dbAlarmLog.setLastChangeTime(time);
				dbAlarmLog.setClearTime(time);
				dbAlarmLog.setClearUserName(confirmClearAlarmEntity.getAlarmClearPerson());
				historyAlarmService.updateHistoryAlarmLogByObject(dbAlarmLog);
			}
		} catch (Exception e) {
			String returnStr = gson.toJson("1"); //1表示失败
			return returnStr;
		}
		String returnStr = gson.toJson("0"); //0表示成功
		return returnStr;
	}
	
	/**
	 * 条件查询历史告警
	 */
	@RequestMapping(value="/getOptionQueryAlarm")
	@ResponseBody
	public PageBean getOptionQueryAlarm(String chineseOrEnglishFlag,@RequestParam Integer page,
			@RequestParam(value="rows") Integer pageSize,Page pageObject) throws Exception{
		logBefore(logger, "条件查询历史告警");
		Map<String, Object> map = new HashMap<String, Object>();
		List<OptionQueryAlarm> list = new ArrayList<OptionQueryAlarm>();
		//查询告警日志
		PageData pd = new PageData();
		pd = this.getPageData();
		//查询告警记录的数量
		PageData alarmLogCountPageData = historyAlarmService.findAllDbAlarmLogCount(pd);
		long alarmLogCount = ((Long)alarmLogCountPageData.get("alarmLogCount")).longValue();
		pageObject.setPageData(pd);
		pageObject.setCurrentStartIndex((page - 1) * pageSize);
		pageObject.setPageSize(Math.min(pageSize, (int)alarmLogCount));
		//条件查询告警日志
		List<DbAlarmLog> allDbAlarmLog = historyAlarmService.findAllDbAlarmLog(pageObject);
		Collections.reverse(allDbAlarmLog);
		//循环处理数据
		for (DbAlarmLog dbAlarmLog : allDbAlarmLog) {
			OptionQueryAlarm optionQueryAlarm = new OptionQueryAlarm();
			//告警详情
			for (int i = 0; i < alarmAttributeList.size(); i++) {
				AlarmAttributeEntity eachAlarmAttribute = alarmAttributeList.get(i);
				String[] splitTopic = dbAlarmLog.getAlarmSource().split("/");
				if((Integer.valueOf(dbAlarmLog.getAlarmCode()) == eachAlarmAttribute.getAlarmCode())
					&& (splitTopic[0].equals(eachAlarmAttribute.getDeviceType()))){
					alarmAttributeEntity = eachAlarmAttribute;
					break;
				}
			}
			//告警序号
			optionQueryAlarm.setAlarmNumber(String.valueOf(dbAlarmLog.getSerialNumber()));
			//告警等级
			String alarmLevelStr = alarmAttributeEntity.getAlarmSeverityName();
			if("".equals(alarmLevelStr) || alarmLevelStr==null){
				optionQueryAlarm.setAlarmLevel("未知");
			}else{
				optionQueryAlarm.setAlarmLevel(alarmLevelStr);
			}
			//告警详情
			optionQueryAlarm.setAlarmDetail(dbAlarmLog.getAdditionPairs());
			//告警发生时间
			optionQueryAlarm.setAlarmHappenTime(dbAlarmLog.getRaisedTime());
			//告警原因(0表示中文,1表示英文)
			if(chineseOrEnglishFlag==null){
				optionQueryAlarm.setAlarmReason(alarmAttributeEntity.getAlarmCause());
			}else if("zh_CN".equals(chineseOrEnglishFlag)){
				optionQueryAlarm.setAlarmReason(alarmAttributeEntity.getAlarmCause());
			}else if("en_US".equals(chineseOrEnglishFlag)){
				optionQueryAlarm.setAlarmReason(alarmAttributeEntity.getAlarmCause_en());
			}
			//最后更新时间
			optionQueryAlarm.setAlarmLastChangeTime(dbAlarmLog.getLastChangeTime());
			//清除时间
			optionQueryAlarm.setAlarmClearTime(dbAlarmLog.getClearTime());
			//清除人
			optionQueryAlarm.setAlarmClearPerson(dbAlarmLog.getClearUserName());
			list.add(optionQueryAlarm);
		}
		map.put("total", list.size());  
        map.put("rows", list);
        return new PageBean(page, pageSize, list, alarmLogCount);
	}
	
	/**
	 * 附加信息转换方法
	 */
	private String replaceAddition(String additions, AlarmInfoEntity alarmInfo) {
		JSONObject objadd = JSONObject.fromObject(additions);
		String addition = null;
		Iterator<?> itadd = objadd.keys();
		while (itadd.hasNext()) {
			Object key = itadd.next();
			Object value = objadd.get(key);
			if ("disk".equals(key)) {
				addition = alarmInfo.getAddition().replace("disk", value.toString());
			}
			if ("total".equals(key)) {
				addition = addition.replace("total", value.toString());
			}
			if ("used".equals(key)) {
				addition = addition.replace("used", value.toString());
			}
		}
		return addition;
	}
	
	/**
	 * 历史告警条件查询的饼状图显示的数据
	 */
	@RequestMapping(value="/getOptionHistoryAlarmChart",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getOptionHistoryAlarmChart(OptionQueryAlarm optionQueryAlarm) throws Exception{
		logBefore(logger, "饼状图显示数据");
		PageData pd = new PageData();
		pd = this.getPageData();
		//查询统计设备和告警级别的数据
		List<HistoryAlarmPieChartData> list = new ArrayList<HistoryAlarmPieChartData>();
		
		if("".equals(optionQueryAlarm.getStartDatetime()) && "".equals(optionQueryAlarm.getEndDatetime())){
			Gson json = new Gson();
			String resultStr = json.toJson(list);
			return resultStr;
		}
		
		String optionPieChart = optionQueryAlarm.getOptionPieChart();
		if("1".equals(optionPieChart)){
			List<HistoryAlarmPieChartData> countByDeviceType = historyAlarmService.countByDeviceType(pd);
			for (HistoryAlarmPieChartData historyAlarmPieChartData : countByDeviceType) {
				HistoryAlarmPieChartData pieChartData = new HistoryAlarmPieChartData();
				String source = historyAlarmPieChartData.getChartShowName();
				String sourceName = source.substring(source.indexOf("__") + 2, source.lastIndexOf("__"));
				String sourceNum = source.substring(source.indexOf("/") + 1, source.lastIndexOf("/"));
				String displayName = GlobalHashMap.equipNameSourceCodeInfoMap.get(sourceName + "-" + sourceNum);
				pieChartData.setChartShowName(displayName + "-" + sourceNum);
				pieChartData.setCharShowCount(historyAlarmPieChartData.getCharShowCount());
				list.add(pieChartData);
			}
		}else if("2".equals(optionPieChart)){
			List<HistoryAlarmPieChartData> countByAlarmLevel = historyAlarmService.countByAlarmLevel(pd);
			for (HistoryAlarmPieChartData singlePieChartData : countByAlarmLevel) {
				HistoryAlarmPieChartData pieChartData = new HistoryAlarmPieChartData();
				pieChartData.setChartShowName(singlePieChartData.getChartShowName());
				pieChartData.setCharShowCount(singlePieChartData.getCharShowCount());
				list.add(pieChartData);
			}
		}
		Gson json = new Gson();
		String resultStr = json.toJson(list);
		return resultStr;
	}
	
	/**
	 * 导出告警
	 */
	@RequestMapping(value="/exportAlarm")
	public ModelAndView exportAlarm(String startDatetime,String endDatetime,
									String alarmLevel,String deviceType,
									String isConfirm,String isClear,
									String keyWord,String exportTime,Page page) throws Exception{
		logBefore(logger, "导出告警");
		exportAlarmLog.clear();
		PageData pd = new PageData();
		pd = this.getPageData();
	
		if("1".equals(exportTime)){
			pd.put("startDatetime", startDatetime);
			pd.put("endDatetime", endDatetime);
			pd.put("alarmLevel", alarmLevel);
			pd.put("deviceType", deviceType);
			pd.put("keyWord", keyWord);
			if ("true".equals(isConfirm)) {
				pd.put("isConfirm", isConfirm);
			}else{
				pd.put("isConfirm", null);
			}
			if ("false".equals(isClear)) {
				pd.put("isClear", isClear);
			}else{
				pd.put("isClear", null);
			}
			pd.put("", "");
			page.setPageData(pd);
			page.setCurrentStartIndex(0);
			page.setPageSize(0);
			//当前查询条件下的告警
			exportAlarmLog = historyAlarmService.findAllDbAlarmLog(page);
		}else if("2".equals(exportTime)){
			pd.put("startDatetime", null);
			pd.put("endDatetime", null);
			pd.put("alarmLevel", null);
			pd.put("deviceType", null);
			pd.put("keyWord", null);
			pd.put("isConfirm", null);
			pd.put("isClear", null);
			page.setPageData(pd);
			page.setCurrentStartIndex(0);
			page.setPageSize(0);
			exportAlarmLog = historyAlarmService.findAllDbAlarmLog(page);
		}
		
		ModelAndView mv = this.getModelAndView();
		Map<String,Object> alarmDataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("告警序号"); 		
		titles.add("设备名称");			
		titles.add("告警级别");  		
		titles.add("告警描述");			
		titles.add("告警原因");			
		titles.add("建议修复措施");			
		titles.add("告警详情");			
		titles.add("发生时间");			
		titles.add("最后更新时间");			
		titles.add("确认时间");			
		titles.add("确认人");			
		titles.add("清除时间");		
		titles.add("清除人");		
		alarmDataMap.put("titles", titles);
		
		List<OptionQueryAlarm> list = new ArrayList<OptionQueryAlarm>();
		
		Map<String,ConcurrentHashMap<String, AlarmInfoEntity>> alarmInfoMaps = ParseAlarmXmlUtil.getInstance().getEquipAndAlarmMap();
		//循环处理数据
		for (DbAlarmLog dbAlarmLog : exportAlarmLog) {
			OptionQueryAlarm optionQueryAlarm = new OptionQueryAlarm();
			String source = dbAlarmLog.getAlarmSource();
			Integer code = dbAlarmLog.getAlarmCode();
			ConcurrentHashMap<String, AlarmInfoEntity> alarmInfoMap = alarmInfoMaps.get(source.substring(source.indexOf("__") + 2, source.lastIndexOf("__")));
			AlarmInfoEntity alarmInfoEntity = alarmInfoMap.get(String.valueOf(code));
			String addation = replaceAddition(dbAlarmLog.getAdditionPairs(),alarmInfoEntity);
			//告警序号
			optionQueryAlarm.setAlarmNumber(String.valueOf(dbAlarmLog.getSerialNumber()));
			//设备名称
			String sourceName = source.substring(source.indexOf("__") + 2, source.lastIndexOf("__"));
			String sourceNum = source.substring(source.indexOf("/") + 1, source.lastIndexOf("/"));
			String displayName = GlobalHashMap.equipNameSourceCodeInfoMap.get(sourceName+"-"+sourceNum);
			optionQueryAlarm.setDeviceName(displayName+"-"+sourceNum);
			//告警级别
			String alarmLevelStr = alarmInfoEntity.getSeverity();
			if("".equals(alarmLevelStr) || alarmLevelStr==null){
				optionQueryAlarm.setAlarmLevel(null);
			}else{
				optionQueryAlarm.setAlarmLevel((String)ParseAlarmXmlUtil.getInstance().getAlarmSeverityMap().get(alarmLevelStr));
			}
			//告警描述
			optionQueryAlarm.setAlarmDescription(alarmInfoEntity.getDesc());
			//告警原因
			optionQueryAlarm.setAlarmReason(alarmInfoEntity.getCause());
			//建议修复措施
			optionQueryAlarm.setRecommendMeasure(alarmInfoEntity.getTreatment());
			//告警详情
			optionQueryAlarm.setAlarmDetail(addation);
			//发生时间
			optionQueryAlarm.setAlarmHappenTime(dbAlarmLog.getRaisedTime());
			//最后更新时间
			optionQueryAlarm.setAlarmLastChangeTime(dbAlarmLog.getLastChangeTime());
			//确认时间
			optionQueryAlarm.setAlarmAckTime(dbAlarmLog.getAckTime());
			//确认人
			optionQueryAlarm.setAlarmAckPerson(dbAlarmLog.getAckUserName());
			//清除时间
			optionQueryAlarm.setAlarmClearTime(dbAlarmLog.getClearTime());
			//清除人
			optionQueryAlarm.setAlarmClearPerson(dbAlarmLog.getClearUserName());
			
			list.add(optionQueryAlarm);
		}
		alarmDataMap.put("historyAlarmList", list);
		HistoryAlarmExcelView excelView = new HistoryAlarmExcelView();					//执行excel操作
		mv = new ModelAndView(excelView,alarmDataMap);
		return mv;
	}
	
	/**
	 * 删除告警
	 */
	@RequestMapping(value="/deleteAlarm",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String deleteAlarm(String deleteTime) throws Exception{
		logBefore(logger, "删除告警");
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String dateStr = null;
		//执行删除操作
		if("1".equals(deleteTime)){
			dateStr = TimeUtil.getEarlyTime("aWeekAgo");
		}else if("2".equals(deleteTime)){
			dateStr = TimeUtil.getEarlyTime("aMonthAgo");
		}else if("3".equals(deleteTime)){
			dateStr = TimeUtil.getEarlyTime("threeMonthAgo");
		}else if("4".equals(deleteTime)){
			dateStr = "all";
		}
		pd.put("raisedTime", dateStr);
		//查询出最大的日志告警序列号
		PageData selectMaxNo = historyAlarmService.selectMaxNoByRaiseTime(pd);
		Long maxNoInt = ((Long)selectMaxNo.get("maxNo")).longValue();
		pd.put("maxNo", maxNoInt);
		historyAlarmService.deleteAlarmLogData(pd);
		
		Gson json = new Gson();
		String resultStr = json.toJson(String.valueOf(0));
		return resultStr;
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
