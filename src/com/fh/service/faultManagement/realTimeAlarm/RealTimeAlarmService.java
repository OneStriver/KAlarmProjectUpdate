package com.fh.service.faultManagement.realTimeAlarm;

import java.util.List;

import com.fh.entity.PageData;
import com.fh.entity.faultManagement.historyAlarm.DbAlarmLog;

/** 
 * 说明： 组织机构接口类
 * 创建人：FH
 */
public interface RealTimeAlarmService{

	/**
	 * 条件查询日志记录
	 */
	public List<DbAlarmLog> selectRealTimeAlarmLogBySourceAndCode(PageData pd)throws Exception;
	
	/**
	 * 条件查询日志记录
	 */
	public List<DbAlarmLog> selectRealTimeAlarmLogByNo(PageData pd)throws Exception;
	
	/**
	 * 根据no更新日志记录
	 */
	public void confirmOrClearAlarmLogByObject(DbAlarmLog dbAlarmLog)throws Exception;
	
}

