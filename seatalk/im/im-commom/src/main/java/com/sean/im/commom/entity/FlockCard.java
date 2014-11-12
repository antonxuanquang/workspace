package com.sean.im.commom.entity;


/**
 * 群名片
 * @author sean
 */
public class FlockCard
{
	private long id;
	private long flockId;
	private long userId;
	private String name;
	private String tel;
	private String email;
	private String description;

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

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
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

}
