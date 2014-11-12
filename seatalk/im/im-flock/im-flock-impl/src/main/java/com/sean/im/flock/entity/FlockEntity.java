package com.sean.im.flock.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.flock.constant.Tables;
import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = Tables.Flock, dataSource = IMDataSource.class, description = "群")
public class FlockEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "群名称")
	private String name;
	@ColumnConfig(description = "个性签名")
	private String signature;
	@ColumnConfig(description = "群介绍")
	private String description;
	@ColumnConfig(description = "创建人")
	private long creater;
	@ColumnConfig(description = "创建时间")
	private long createTime;

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
		Map<String, Object> map = new HashMap<String, Object>(6);
		map.put("id", id);
		map.put("name", name);
		map.put("signature", signature);
		map.put("description", description);
		map.put("creater", creater);
		map.put("createTime", createTime);
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
		if ((o = vals.get("signature")) != null)
			this.signature = o.toString();
		if ((o = vals.get("description")) != null)
			this.description = o.toString();
		if ((o = vals.get("creater")) != null)
			this.creater = Long.parseLong(o.toString());
		if ((o = vals.get("createTime")) != null)
			this.createTime = Long.parseLong(o.toString());
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSignature()
	{
		return signature;
	}

	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public long getCreater()
	{
		return creater;
	}

	public void setCreater(long creater)
	{
		this.creater = creater;
	}

	public long getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}
	
	
}