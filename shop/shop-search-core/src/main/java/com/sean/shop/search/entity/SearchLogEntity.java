package com.sean.shop.search.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_search_log", dataSource = "shop", descr = "搜索日志")
public class SearchLogEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "搜索ID")
	public long searchId;
	@ColumnConfig(descr = "搜索关键字")
	public String searchKey;
	@ColumnConfig(descr = "当前页码")
	public int pageNo;
	@ColumnConfig(descr = "搜索结果, good的ID集合, 用逗号隔开")
	public String searchResult;
	@ColumnConfig(descr = "创建时间")
	public long createTime;

	@Override
	public Object getKey()
	{
		return searchId;
	}

	@Override
	public void setKey(Object key)
	{
		this.searchId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(5);
		map.put("searchId", searchId);
		map.put("searchKey", searchKey);
		map.put("pageNo", pageNo);
		map.put("searchResult", searchResult);
		map.put("createTime", createTime);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.searchId = vals.getLong("searchId");
		this.searchKey = vals.getString("searchKey");
		this.pageNo = vals.getInt("pageNo");
		this.searchResult = vals.getString("searchResult");
		this.createTime = vals.getLong("createTime");
	}

}
