package com.sean.shop.search.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_show_log", dataSource = "shop", descr = "商品详细展示日志, 指点击查看商品详细")
public class ShowLogEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "搜索ID")
	public long showId;
	@ColumnConfig(descr = "搜索关键字")
	public String searchKey;
	@ColumnConfig(descr = "商品ID")
	public long goodId;
	@ColumnConfig(descr = "创建时间")
	public long createTime;

	@Override
	public Object getKey()
	{
		return showId;
	}

	@Override
	public void setKey(Object key)
	{
		this.showId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(4);
		map.put("showId", showId);
		map.put("searchKey", searchKey);
		map.put("goodId", goodId);
		map.put("createTime", createTime);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.showId = vals.getLong("showId");
		this.searchKey = vals.getString("searchKey");
		this.goodId = vals.getLong("goodId");
		this.createTime = vals.getLong("createTime");
	}

}
