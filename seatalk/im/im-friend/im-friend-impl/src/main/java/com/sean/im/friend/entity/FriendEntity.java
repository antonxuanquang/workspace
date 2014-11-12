package com.sean.im.friend.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.friend.constant.Tables;
import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = Tables.Friend, dataSource = IMDataSource.class, description = "好友")
public class FriendEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "用户Id")
	private long userId;
	@ColumnConfig(description = "好友Id")
	private long friendId;
	@ColumnConfig(description = "分组Id")
	private long groupId;
	@ColumnConfig(description = "备注")
	private String remark;

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
		map.put("friendId", friendId);
		map.put("groupId", groupId);
		map.put("remark", remark);
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
		if ((o = vals.get("friendId")) != null)
			this.friendId = Long.parseLong(o.toString());
		if ((o = vals.get("groupId")) != null)
			this.groupId = Long.parseLong(o.toString());
		if ((o = vals.get("remark")) != null)
			this.remark = o.toString();
	}

	public long getId()
	{
		return this.id;
	}

	public long getUserId()
	{
		return this.userId;
	}

	public long getFriendId()
	{
		return this.friendId;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public void setFriendId(long friendId)
	{
		this.friendId = friendId;
	}

	public long getGroupId()
	{
		return groupId;
	}

	public void setGroupId(long groupId)
	{
		this.groupId = groupId;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

}