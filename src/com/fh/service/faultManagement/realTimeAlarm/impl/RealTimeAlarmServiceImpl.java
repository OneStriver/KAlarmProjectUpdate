package com.fh.service.faultManagement.realTimeAlarm.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.PageData;
import com.fh.entity.faultManagement.historyAlarm.DbAlarmLog;
import com.fh.service.faultManagement.realTimeAlarm.RealTimeAlarmService;

/** 
 * 说明： 实时告警监控
 * 创建人：FH 
 */
@Service("realTimeAlarmService")
public class RealTimeAlarmServiceImpl implements RealTimeAlarmService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 根据source和code查询告警记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DbAlarmLog> selectRealTimeAlarmLogBySourceAndCode(PageData pd) throws Exception {
		return (List<DbAlarmLog>)dao.findForList("RealTimeAlarmMapper.selectRealTimeAlarmLogBySourceAndCode", pd);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DbAlarmLog> selectRealTimeAlarmLogByNo(PageData pd) throws Exception {
		return (List<DbAlarmLog>)dao.findForList("RealTimeAlarmMapper.selectRealTimeAlarmLogByNo", pd);
	}
	
	/**
	 * 根据DbAlarmLog对象更新告警记录
	 */
	@Override
	public void confirmOrClearAlarmLogByObject(DbAlarmLog dbAlarmLog) throws Exception {
		dao.update("RealTimeAlarmMapper.confirmOrClearAlarmLogByObject", dbAlarmLog);
	}


	
}

