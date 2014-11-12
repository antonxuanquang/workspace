package com.sean.im.commom.entity;

import com.sean.im.commom.constant.StatusEnum;

/**
 * 好友
 * @author sean
 */
public class Friend
{
	private long id;
	private long friendId;
	private long groupId;
	private int country;
	private String username;
	private String remark;
	private String nickname;
	private int head;
	private int age;
	private int sex;
	private String signature;
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

	public long getFriendId()
	{
		return friendId;
	}

	public void setFriendId(long friendId)
	{
		this.friendId = friendId;
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

	public String getSignature()
	{
		return signature;
	}

	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getHead()
	{
		return head;
	}

	public void setHead(int head)
	{
		this.head = head;
	}

	public long getGroupId()
	{
		return groupId;
	}

	public void setGroupId(long groupId)
	{
		this.groupId = groupId;
	}

	public int getCountry()
	{
		return country;
	}

	public void setCountry(int country)
	{
		this.country = country;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public int getSex()
	{
		return sex;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
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
