package com.fh.service.faultManagement.historyAlarm.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.PageData;
import com.fh.entity.faultManagement.historyAlarm.DbAlarmLog;
import com.fh.entity.faultManagement.historyAlarm.DeviceType;
import com.fh.entity.faultManagement.historyAlarm.HistoryAlarmPieChartData;
import com.fh.service.faultManagement.historyAlarm.HistoryAlarmService;

/** 
 * 说明： 历史告警查询
 */
@Service("historyAlarmService")
public class HistoryAlarmServiceImpl implements HistoryAlarmService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 新增
	 */
	public void save(PageData pd)throws Exception{
		dao.save("HistoryAlarmMapper.save", pd);
	}
	
	/**
	 * 删除
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("HistoryAlarmMapper.delete", pd);
	}
	
	/**
	 * 修改
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("HistoryAlarmMapper.edit", pd);
	}
	
	/**
	 * 列表
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("HistoryAlarmMapper.datalistPage", page);
	}
	
	/**
	 * 通过id获取数据
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("HistoryAlarmMapper.findById", pd);
	}
	
	/**
	 * 获取所有的设备类型
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceType> findAllDeviceType(PageData pd) throws Exception {
		return (List<DeviceType>)dao.findForList("HistoryAlarmMapper.findAllDeviceType", pd);
	}
	
	/**
	 * 条件过滤日志记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DbAlarmLog> findAllDbAlarmLog(Page page) throws Exception {
		return (List<DbAlarmLog>)dao.findForList("HistoryAlarmMapper.findAllDbAlarmLog", page);
	}
	
	/**
	 * App条件过滤日志记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DbAlarmLog> appOptionFindAlarmLog(String serialNumber) throws Exception {
		return (List<DbAlarmLog>)dao.findForList("HistoryAlarmMapper.appOptionFindAlarmLog", serialNumber);
	}
	
	

	@Override
	public PageData findAllDbAlarmLogCount(PageData pd) throws Exception {
		return (PageData)dao.findForObject("HistoryAlarmMapper.findAllDbAlarmLogCount", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HistoryAlarmPieChartData> countByDeviceType(PageData pd) throws Exception {
		return (List<HistoryAlarmPieChartData>)dao.findForList("HistoryAlarmMapper.countByDeviceType", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HistoryAlarmPieChartData> countByAlarmLevel(PageData pd) throws Exception {
		return (List<HistoryAlarmPieChartData>)dao.findForList("HistoryAlarmMapper.countByAlarmLevel", pd);
	}

	@Override
	public PageData selectMaxNoByRaiseTime(PageData pd) throws Exception {
		return (PageData)dao.findForObject("HistoryAlarmMapper.selectMaxNoByRaiseTime", pd);
	}

	@Override
	public void deleteAlarmLogData(PageData pd) throws Exception {
		dao.delete("HistoryAlarmMapper.deleteAlarmLogData", pd);
	}

	@Override
	public void addAlarmLogData(DbAlarmLog dbAlarmLog) throws Exception {
		dao.save("HistoryAlarmMapper.addAlarmLogData", dbAlarmLog);
	}

	@Override
	public void updateHistoryAlarmLogByObject(DbAlarmLog dbAlarmLog) throws Exception {
		dao.update("HistoryAlarmMapper.updateHistoryAlarmLogByObject", dbAlarmLog);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DbAlarmLog> selectMqttAlarmLogBySourceAndCode(PageData pd) throws Exception {
		return (List<DbAlarmLog>)dao.findForList("HistoryAlarmMapper.selectMqttAlarmLogBySourceAndCode", pd);
	}

}

