package com.sean.im.friend.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.friend.constant.Tables;
import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = Tables.Message, dataSource = IMDataSource.class, description = "消息")
public class MessageEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "发送者Id")
	private long senderId;
	@ColumnConfig(description = "接受者Id")
	private long receiverId;
	@ColumnConfig(description = "发送时间")
	private long sendTime;
	@ColumnConfig(description = "消息内容")
	private String content;
	@ColumnConfig(description = "是否已经读取")
	private int isRead;
	@ColumnConfig(description = "消息类型,见MessageEnum")
	private int type;
	@ColumnConfig(description = "所属人")
	private long ownerId;

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
		Map<String, Object> map = new HashMap<String, Object>(7);
		map.put("id", id);
		map.put("senderId", senderId);
		map.put("receiverId", receiverId);
		map.put("sendTime", sendTime);
		map.put("content", content);
		map.put("isRead", isRead);
		map.put("type", type);
		map.put("ownerId", ownerId);
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
		if ((o = vals.get("receiverId")) != null)
			this.receiverId = Long.parseLong(o.toString());
		if ((o = vals.get("sendTime")) != null)
			this.sendTime = Long.parseLong(o.toString());
		if ((o = vals.get("content")) != null)
			this.content = o.toString();
		if ((o = vals.get("isRead")) != null)
			this.isRead = Integer.parseInt(o.toString());
		if ((o = vals.get("type")) != null)
			this.type = Integer.parseInt(o.toString());
		if ((o = vals.get("ownerId")) != null)
			this.ownerId = Long.parseLong(o.toString());
	}

	public long getId()
	{
		return this.id;
	}

	public long getSenderId()
	{
		return this.senderId;
	}

	public long getReceiverId()
	{
		return this.receiverId;
	}

	public long getSendTime()
	{
		return this.sendTime;
	}

	public String getContent()
	{
		return this.content;
	}

	public int getIsRead()
	{
		return this.isRead;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setSenderId(long senderId)
	{
		this.senderId = senderId;
	}

	public void setReceiverId(long receiverId)
	{
		this.receiverId = receiverId;
	}

	public void setSendTime(long sendTime)
	{
		this.sendTime = sendTime;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public void setIsRead(int isRead)
	{
		this.isRead = isRead;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public long getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(long ownerId)
	{
		this.ownerId = ownerId;
	}

}