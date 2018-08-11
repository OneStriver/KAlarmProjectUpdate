package com.fh.controller.faultManagement.alarmAttr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.entity.PageData;
import com.fh.entity.alarmAttr.AlarmAttributeEntity;
import com.fh.service.alarmAttr.AlarmAttributeService;

/** 
 * 说明：告警属性管理
 */
@Controller
@RequestMapping(value="/alarmAttribute")
public class AlarmAttributeController extends BaseController {
	
	@Resource(name="alarmAttributeService")
	private AlarmAttributeService alarmAttributeService;
	
	/**
	 * 转到告警属性页面
	 */
	@RequestMapping(value="/toAlarmAttributePage")
	public ModelAndView toAlarmAttributePage()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.addObject("pd", pd);	
		mv.setViewName("faultManagement/alarmAttribute/alarmAttribute_list");
		return mv;
	}
	
	/**
	 * 获取告警属性信息
	 */
	@RequestMapping(value="/getAlarmAttribute")
	@ResponseBody
	public Map<String, Object> getAlarmAttribute(Page page) throws Exception{
		logBefore(logger, "获取告警属性信息");
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pageData = new PageData();
		pageData = this.getPageData();
		page.setPageData(pageData);
		//计算的开始索引
		page.setCurrentStartIndex(page.getStartOffSet());
		//计算的结束索引
		page.setPageSize(page.getEndOffSet());
		List<AlarmAttributeEntity> alarmAttributeList = alarmAttributeService.list(page);
		map.put("total", alarmAttributeList.size());  
        map.put("rows", alarmAttributeList);
		return map;
	}
	
	/**
	 * 添加告警数据
	 */
	@RequestMapping("saveAlarmAttribute")
	@ResponseBody 
	public String saveAlarmAttribute(){
		PageData pageData = new PageData();
		pageData = this.getPageData();
		//查询出数据中现有的所有数据
		String equip_type = (String)pageData.get("equip_type");
		pageData.put("equip_type", equip_type);
		int code = (int)pageData.get("code");
		pageData.put("code", code);
		int type = (int)pageData.get("type");
		pageData.put("type", type);
		int severity = (int)pageData.get("severity");
		pageData.put("severity", severity);
		String desc = (String)pageData.get("desc");
		pageData.put("desc", desc);
		String cause = (String)pageData.get("cause");
		pageData.put("cause", cause);
		String treatment = (String)pageData.get("treatment");
		pageData.put("treatment", treatment);
		String addition = (String)pageData.get("addition");
		pageData.put("addition", addition);
		int auto_clear_enable = (int)pageData.get("auto_clear_enable");
		pageData.put("auto_clear_enable", auto_clear_enable);
		int auto_clear_timeout_in_ms = (int)pageData.get("auto_clear_timeout_in_ms");
		pageData.put("auto_clear_timeout_in_ms", auto_clear_timeout_in_ms);
		int suppress = (int)pageData.get("suppress");
		pageData.put("suppress", suppress);
		try {
			alarmAttributeService.save(pageData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 更新告警数据
	 */
	@RequestMapping("updateAlarmAttribute")
	@ResponseBody 
	public String updateAlarmAttribute(){
		PageData pageData = new PageData();
		pageData = this.getPageData();
		//查询出数据中现有的所有数据
		String equip_type = (String)pageData.get("equip_type");
		pageData.put("equip_type", equip_type);
		int code = (int)pageData.get("code");
		pageData.put("code", code);
		int type = (int)pageData.get("type");
		pageData.put("type", type);
		int severity = (int)pageData.get("severity");
		pageData.put("severity", severity);
		String desc = (String)pageData.get("desc");
		pageData.put("desc", desc);
		String cause = (String)pageData.get("cause");
		pageData.put("cause", cause);
		String treatment = (String)pageData.get("treatment");
		pageData.put("treatment", treatment);
		String addition = (String)pageData.get("addition");
		pageData.put("addition", addition);
		int auto_clear_enable = (int)pageData.get("auto_clear_enable");
		pageData.put("auto_clear_enable", auto_clear_enable);
		int auto_clear_timeout_in_ms = (int)pageData.get("auto_clear_timeout_in_ms");
		pageData.put("auto_clear_timeout_in_ms", auto_clear_timeout_in_ms);
		int suppress = (int)pageData.get("suppress");
		pageData.put("suppress", suppress);
		try {
			alarmAttributeService.edit(pageData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 删除告警数据
	 */
	@RequestMapping("deleteSelectAlarmDatas")
	@ResponseBody 
	public String deleteSelectAlarmDatas(){
		PageData pageData = new PageData();
		pageData = this.getPageData();
		//查询出数据中现有的所有数据
		String equip_type = (String)pageData.get("equip_type");
		pageData.put("equip_type", equip_type);
		int code = (int)pageData.get("code");
		pageData.put("code", code);
		try {
			alarmAttributeService.delete(pageData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
