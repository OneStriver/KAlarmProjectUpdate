package com.fh.entity;

import java.util.List;

/**
 * 分页vo
 */
public class PageBean {

	// 传递的参数或是配置的参数
	private int page = 1; // 当前页
	private int pageSize = 50; // 每页显示多少条记录

	// 查询数据库
	private List<?> rows; // 本页的数据列表
	private Long total; // 总记录数

	public PageBean() {
	}

	/**
	 * 只接受4个必要的属性，会自动的计算出其他3个属性的值
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param recordList
	 * @param recordCount
	 */
	public PageBean(int page, int pageSize, List<?> rows, Long total) {
		this.page = page;
		this.pageSize = pageSize;
		this.rows = rows;
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

}