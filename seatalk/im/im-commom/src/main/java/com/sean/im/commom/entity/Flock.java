package com.sean.im.commom.entity;

/**
 * 群
 * @author sean
 */
public class Flock
{
	private long id;
	private String name;
	private String signature;
	private String description;
	private long creater;
	private long createTime;

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
