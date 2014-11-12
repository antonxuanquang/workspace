package com.sean.im.friend.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.friend.constant.Tables;
import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = Tables.Group, dataSource = IMDataSource.class, description = "分组")
public class GroupEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "分组名称")
	private String name;
	@ColumnConfig(description = "所属用户")
	private long userId;
	@ColumnConfig(description = "是不是默认分组")
	private int isDefault;

	@Override
	public Object getKey()
	{
		return id;
	}

	@Override
	public void setKey(Object key)
	{
		this.id = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("id", id);
		map.put("name", name);
		map.put("userId", userId);
		map.put("isDefault", isDefault);
		return map;
	}

	@Override
	public void setValues(Map<String, Object> vals)
	{
		Object o = null;
		if ((o = vals.get("id")) != null)
			this.id = Long.parseLong(o.toString());
		if ((o = vals.get("name")) != null)
			this.name = o.toString();
		if ((o = vals.get("userId")) != null)
			this.userId = Long.parseLong(o.toString());
		if ((o = vals.get("isDefault")) != null)
			this.isDefault = Integer.parseInt(o.toString());
	}

	public long getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public long getUserId()
	{
		return this.userId;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public int getIsDefault()
	{
		return isDefault;
	}

	public void setIsDefault(int isDefault)
	{
		this.isDefault = isDefault;
	}

}