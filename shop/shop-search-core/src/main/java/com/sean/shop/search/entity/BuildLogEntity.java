package com.sean.shop.search.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_build_log", dataSource = "shop", descr = "索引建立实体")
public class BuildLogEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "建立ID")
	public long buildId;
	@ColumnConfig(descr = "开始建立时间")
	public long startTime;
	@ColumnConfig(descr = "结束建立时间")
	public long endTime;
	@ColumnConfig(descr = "建立结果")
	public int buildResult;
	@ColumnConfig(descr = "索引类型:1-商品索引, 2-搜索词索引, 3-计算商品分类")
	public int type;

	@Override
	public Object getKey()
	{
		return buildId;
	}

	@Override
	public void setKey(Object key)
	{
		this.buildId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(5);
		map.put("buildId", buildId);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("buildResult", buildResult);
		map.put("type", type);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.buildId = vals.getLong("buildId");
		this.startTime = vals.getLong("startTime");
		this.endTime = vals.getLong("endTime");
		this.buildResult = vals.getInt("buildResult");
		this.type = vals.getInt("type");
	}

}
