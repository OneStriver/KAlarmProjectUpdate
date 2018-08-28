package com.fh.service.faultManagement.historyAlarm;

import java.util.List;

import com.fh.entity.Page;
import com.fh.entity.PageData;
import com.fh.entity.faultManagement.historyAlarm.DbAlarmLog;
import com.fh.entity.faultManagement.historyAlarm.DeviceType;
import com.fh.entity.faultManagement.historyAlarm.HistoryAlarmPieChartData;

/** 
 * 说明： 历史告警类
 * 创建人：FH
 */
public interface HistoryAlarmService{

	/**
	 * 新增
	 */
	public void save(PageData pd)throws Exception;
	
	/**
	 * 删除
	 */
	public void delete(PageData pd)throws Exception;
	
	/**
	 * 修改
	 */
	public void edit(PageData pd)throws Exception;
	
	/**
	 * 列表
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**
	 * 通过id获取数据
	 */
	public PageData findById(PageData pd)throws Exception;

	/**
	 * 获取所有的设备类型
	 */
	public List<DeviceType> findAllDeviceType(PageData pd)throws Exception;
	
	/**
	 * 查询告警日志记录
	 */
	public List<DbAlarmLog> findAllDbAlarmLog(Page page)throws Exception;
	
	/**
	 * 查询告警日志记录
	 */
	public List<DbAlarmLog> appOptionFindAlarmLog(String serialNumber)throws Exception;
	
	/**
	 * 查询所有的日志条数
	 */
	public PageData findAllDbAlarmLogCount(PageData pd)throws Exception;
	
	/**
	 * 按照设备类型统计
	 */
	public List<HistoryAlarmPieChartData> countByDeviceType(PageData pd)throws Exception; 
	
	/**
	 * 按照设备类型统计
	 */
	public List<HistoryAlarmPieChartData> countByAlarmLevel(PageData pd)throws Exception; 
	
	/**
	 * 查询最大的告警记录序列号
	 */
	public PageData selectMaxNoByRaiseTime(PageData pd)throws Exception;
	
	/**
	 * 删除对应的日志记录
	 */
	public void deleteAlarmLogData(PageData pd)throws Exception;
	
	/**
	 * 写入告警记录
	 */
	public void addAlarmLogData(DbAlarmLog dbAlarmLog)throws Exception;
	
	/**
	 * 根据对象更新记录
	 */
	public void updateHistoryAlarmLogByObject(DbAlarmLog dbAlarmLog)throws Exception;
	
	/**
	 * 条件查询日志记录
	 */
	public List<DbAlarmLog> selectMqttAlarmLogBySourceAndCode(PageData pd)throws Exception;
	
}

