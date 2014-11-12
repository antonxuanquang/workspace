package com.sean.shop.good.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_cash", dataSource = "shop", descr = "提现实体")
public class CashEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "商品ID")
	public long cashId;
	@ColumnConfig(descr = "商品名称")
	public String zfbUsername;
	@ColumnConfig(descr = "创建时间")
	public long createTime;

	@Override
	public Object getKey()
	{
		return cashId;
	}

	@Override
	public void setKey(Object key)
	{
		this.cashId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(3);
		map.put("cashId", cashId);
		map.put("zfbUsername", zfbUsername);
		map.put("createTime", createTime);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.cashId = vals.getLong("cashId");
		this.zfbUsername = vals.getString("zfbUsername");
		this.createTime = vals.getLong("createTime");
	}

}
