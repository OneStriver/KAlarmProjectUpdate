package com.fh.service.alarmAttr.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.PageData;
import com.fh.entity.alarmAttr.AlarmAttributeEntity;
import com.fh.service.alarmAttr.AlarmAttributeService;

/** 
 * 告警属性查询处理类
 */
@Service("alarmAttributeService")
public class AlarmAttributeServiceImpl implements AlarmAttributeService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 查询
	 */
	@SuppressWarnings("unchecked")
	public List<AlarmAttributeEntity> list(Page page)throws Exception{
		return (List<AlarmAttributeEntity>)dao.findForList("AlarmAttributeMapper.listAlarmAttribute", page);
	}
	
	/**
	 * 新增
	 */
	public void save(PageData pageData)throws Exception{
		dao.save("AlarmAttributeMapper.saveAlarmAttribute", pageData);
	}

	/**
	 * 修改
	 */
	public void edit(PageData pageData)throws Exception{
		dao.update("AlarmAttributeMapper.editAlarmAttribute", pageData);
	}
	
	/**
	 * 删除
	 */
	public void delete(PageData pageData)throws Exception{
		dao.delete("AlarmAttributeMapper.deleteAlarmAttribute", pageData);
	}
	
}

