package com.sean.im.flock.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.flock.constant.Tables;
import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = Tables.FlockMessage, dataSource = IMDataSource.class, description = "群消息")
public class FlockMessageEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "发送者Id")
	private long senderId;
	@ColumnConfig(description = "接受群Id")
	private long flockId;
	@ColumnConfig(description = "发送时间")
	private long sendTime;
	@ColumnConfig(description = "消息内容")
	private String content;
	@ColumnConfig(description = "消息类型,见MessageEnum")
	private int type;

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
		map.put("senderId", senderId);
		map.put("flockId", flockId);
		map.put("sendTime", sendTime);
		map.put("content", content);
		map.put("type", type);
		return map;
	}

	@Override
	public void setValues(Map<String, Object> vals)
	{
		Object o = null;
		if ((o = vals.get("id")) != null)
			this.id = Long.parseLong(o.toString());
		if ((o = vals.get("senderId")) != null)
			this.senderId = Long.parseLong(o.toString());
		if ((o = vals.get("flockId")) != null)
			this.flockId = Long.parseLong(o.toString());
		if ((o = vals.get("sendTime")) != null)
			this.sendTime = Long.parseLong(o.toString());
		if ((o = vals.get("content")) != null)
			this.content = o.toString();
		if ((o = vals.get("type")) != null)
			this.type = Integer.parseInt(o.toString());
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getSenderId()
	{
		return senderId;
	}

	public void setSenderId(long senderId)
	{
		this.senderId = senderId;
	}

	public long getFlockId()
	{
		return flockId;
	}

	public void setFlockId(long flockId)
	{
		this.flockId = flockId;
	}

	public long getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(long sendTime)
	{
		this.sendTime = sendTime;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

}