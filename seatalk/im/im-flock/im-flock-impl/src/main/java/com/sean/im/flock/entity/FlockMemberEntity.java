package com.sean.im.flock.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.flock.constant.Tables;
import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = Tables.FlockMember, dataSource = IMDataSource.class, description = "群成员")
public class FlockMemberEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "用户ID")
	private long userId;
	@ColumnConfig(description = "群ID")
	private long flockId;
	@ColumnConfig(description = "加入时间")
	private long joinTime;
	@ColumnConfig(description = "是否为管理员,1是，0否")
	private int isAdmin;

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
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("id", id);
		map.put("userId", userId);
		map.put("flockId", flockId);
		map.put("joinTime", joinTime);
		map.put("isAdmin", isAdmin);
		return map;
	}

	@Override
	public void setValues(Map<String, Object> vals)
	{
		Object o = null;
		if ((o = vals.get("id")) != null)
			this.id = Long.parseLong(o.toString());
		if ((o = vals.get("userId")) != null)
			this.userId = Long.parseLong(o.toString());
		if ((o = vals.get("flockId")) != null)
			this.flockId = Long.parseLong(o.toString());
		if ((o = vals.get("joinTime")) != null)
			this.joinTime = Long.parseLong(o.toString());
		if ((o = vals.get("isAdmin")) != null)
			this.isAdmin = Integer.parseInt(o.toString());
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public long getJoinTime()
	{
		return joinTime;
	}

	public void setJoinTime(long joinTime)
	{
		this.joinTime = joinTime;
	}

	public int getIsAdmin()
	{
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin)
	{
		this.isAdmin = isAdmin;
	}

	public long getFlockId()
	{
		return flockId;
	}

	public void setFlockId(long flockId)
	{
		this.flockId = flockId;
	}

}