package com.sean.im.commom.entity;

import com.sean.im.commom.constant.StatusEnum;

/**
 * 群成员
 * @author sean
 */
public class FlockMember
{
	private long id;
	private long userId;
	private long flockId;
	private long joinTime;
	private int isAdmin;
	private String username;
	private String nickname;
	private int head;
	private int status;

	public boolean isOnline()
	{
		return !(status == StatusEnum.Hide || status == StatusEnum.OffLine);
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

	public long getFlockId()
	{
		return flockId;
	}

	public void setFlockId(long flockId)
	{
		this.flockId = flockId;
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

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public int getHead()
	{
		return head;
	}

	public void setHead(int head)
	{
		this.head = head;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

}
