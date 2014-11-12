package com.sean.im.commom.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * 好友分组
 * @author sean
 */
public class Group
{
	private long id;
	private String name;
	private int isDefault;
	private List<Friend> friends = new LinkedList<Friend>();

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

	public List<Friend> getFriends()
	{
		return friends;
	}

	public void setFriends(List<Friend> friends)
	{
		this.friends = friends;
	}

	public int getIsDefault()
	{
		return isDefault;
	}

	public void setIsDefault(int isDefault)
	{
		this.isDefault = isDefault;
	}

	public void addFriends(List<Friend> fs)
	{
		this.friends.addAll(fs);
	}

	public void addFriend(Friend friend)
	{
		this.friends.add(friend);
	}

	public Friend removeFriend(long id)
	{
		int length = friends.size();
		for (int i = 0; i < length; i++)
		{
			if (friends.get(i).getId() == id)
			{
				return friends.remove(i);
			}
		}
		return null;
	}
}
