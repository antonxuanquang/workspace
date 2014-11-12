package com.sean.shop.good.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_good_feedback", dataSource = "shop", descr = "商品反馈实体")
public class GoodFeedbackEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "反馈ID")
	public long feedbackId;
	@ColumnConfig(descr = "商品id")
	public long goodId;
	@ColumnConfig(descr = "创建时间")
	public long feedbackTime;

	@Override
	public Object getKey()
	{
		return feedbackId;
	}

	@Override
	public void setKey(Object key)
	{
		this.feedbackId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(3);
		map.put("feedbackId", feedbackId);
		map.put("goodId", goodId);
		map.put("feedbackTime", feedbackTime);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.feedbackId = vals.getLong("feedbackId");
		this.goodId = vals.getLong("goodId");
		this.feedbackTime = vals.getLong("feedbackTime");
	}

}
