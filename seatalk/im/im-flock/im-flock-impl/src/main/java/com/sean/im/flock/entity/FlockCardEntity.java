package com.sean.im.flock.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.flock.constant.Tables;
import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = Tables.FlockCard, dataSource = IMDataSource.class, description = "群名片")
public class FlockCardEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "群Id")
	private long flockId;
	@ColumnConfig(description = "用户ID")
	private long userId;
	@ColumnConfig(description = "姓名")
	private String name;
	@ColumnConfig(description = "电话")
	private String tel;
	@ColumnConfig(description = "邮箱")
	private String email;
	@ColumnConfig(description = "个人说明")
	private String description;

	@Override
	public Object getKey()
	{
		return id;
	}

	@Override
	public void setKey(Object key)
	{
		this.id = (int) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<String, Object>(7);
		map.put("id", id);
		map.put("flockId", flockId);
		map.put("userId", userId);
		map.put("name", name);
		map.put("tel", tel);
		map.put("email", email);
		map.put("description", description);
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
		if ((o = vals.get("flockId")) != null)
			this.flockId = Long.parseLong(o.toString());
		if ((o = vals.get("userId")) != null)
			this.userId = Long.parseLong(o.toString());
		if ((o = vals.get("tel")) != null)
			this.tel = o.toString();
		if ((o = vals.get("email")) != null)
			this.email = o.toString();
		if ((o = vals.get("description")) != null)
			this.description = o.toString();
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getFlockId()
	{
		return flockId;
	}

	public void setFlockId(long flockId)
	{
		this.flockId = flockId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTel()
	{
		return tel;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

}
