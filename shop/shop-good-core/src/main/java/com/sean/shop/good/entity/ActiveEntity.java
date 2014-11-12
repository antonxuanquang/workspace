package com.sean.shop.good.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_active", dataSource = "shop", descr = "活动实体", cache = true)
public class ActiveEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "活动ID")
	public long activeId;
	@ColumnConfig(descr = "活动名称")
	public String activeName;
	@ColumnConfig(descr = "活动开始日期")
	public int startDate;
	@ColumnConfig(descr = "活动结束日期")
	public int endDate;
	@ColumnConfig(descr = "淘宝活动Url")
	public String activeUrl;
	@ColumnConfig(descr = "图片Url")
	public String imageUrl;
	@ColumnConfig(descr = "活动渠道: 1-pc, 2-mobile")
	public int activeChannel;
	@ColumnConfig(descr = "创建时间")
	public long createTime;

	@Override
	public Object getKey()
	{
		return activeId;
	}

	@Override
	public void setKey(Object key)
	{
		this.activeId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(7);
		map.put("activeId", activeId);
		map.put("activeName", activeName);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("activeUrl", activeUrl);
		map.put("imageUrl", imageUrl);
		map.put("activeChannel", activeChannel);
		map.put("createTime", createTime);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.activeId = vals.getLong("activeId");
		this.activeName = vals.getString("activeName");
		this.startDate = vals.getInt("startDate");
		this.endDate = vals.getInt("endDate");
		this.activeUrl = vals.getString("activeUrl");
		this.imageUrl = vals.getString("imageUrl");
		this.activeChannel = vals.getInt("activeChannel");
		this.createTime = vals.getLong("createTime");
	}
}
