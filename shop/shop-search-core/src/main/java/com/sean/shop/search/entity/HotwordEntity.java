package com.sean.shop.search.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_hotword", dataSource = "shop", descr = "热门搜索词实体", cache = true)
public class HotwordEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "热词ID")
	public long hotwordId;
	@ColumnConfig(descr = "搜索词")
	public String hotword;
	@ColumnConfig(descr = "排名")
	public int rank;
	@ColumnConfig(descr = "产生时间, 一天一次")
	public long date;

	@Override
	public Object getKey()
	{
		return hotwordId;
	}

	@Override
	public void setKey(Object key)
	{
		this.hotwordId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(4);
		map.put("hotwordId", hotwordId);
		map.put("hotword", hotword);
		map.put("rank", rank);
		map.put("date", date);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.hotwordId = vals.getLong("hotwordId");
		this.hotword = vals.getString("hotword");
		this.rank = vals.getInt("rank");
		this.date = vals.getLong("date");
	}
}
