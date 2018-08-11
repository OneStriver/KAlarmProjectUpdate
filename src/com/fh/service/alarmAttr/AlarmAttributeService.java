package com.fh.service.alarmAttr;

import java.util.List;

import com.fh.entity.Page;
import com.fh.entity.PageData;
import com.fh.entity.alarmAttr.AlarmAttributeEntity;

/** 
 * 告警属性信息查询
 */
public interface AlarmAttributeService {

	/**
	 * 新增
	 */
	public void save(PageData pageData)throws Exception;
	
	/**
	 * 删除
	 */
	public void delete(PageData pageData)throws Exception;
	
	/**
	 * 修改
	 */
	public void edit(PageData pageData)throws Exception;
	
	/**
	 * 列表
	 */
	public List<AlarmAttributeEntity> list(Page page)throws Exception;
	
}

