package com.sean.im.client.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.sean.im.client.form.ChatForm;
import com.sean.im.client.form.flock.ChatRoomForm;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.Friend;

public class ChatFormCache
{
	private static Map<Long, ChatForm> chatForms = new HashMap<Long, ChatForm>();// 缓存所有聊天窗体
	private static Map<Long, ChatRoomForm> chatRoomForms = new HashMap<Long, ChatRoomForm>();// 缓存所有聊天窗体

	public static ChatForm getChatForm(Friend friend)
	{
		ChatForm form = chatForms.get(friend.getFriendId());
		if (form == null)
		{
			form = new ChatForm(friend);
			chatForms.put(friend.getFriendId(), form);
		}
		return form;
	}

	public static void removeChatForm(Friend friend)
	{
		for (long friendId : chatForms.keySet())
		{
			if (friendId == friend.getFriendId())
			{
				chatForms.remove(friendId);
				return;
			}
		}
	}

	public static ChatRoomForm getChatRoomForm(Flock flock)
	{
		ChatRoomForm form = chatRoomForms.get(flock.getId());
		if (form == null)
		{
			form = new ChatRoomForm(flock);
			chatRoomForms.put(flock.getId(), form);
		}
		return form;
	}

	public static void removeChatRoomForm(Flock flock)
	{
		for (long flockId : chatRoomForms.keySet())
		{
			if (flockId == flock.getId())
			{
				chatRoomForms.remove(flockId);
				return;
			}
		}
	}

	public static void setHead(int head)
	{
		for (ChatForm cf : chatForms.values())
		{
			cf.setHead(head);
		}
	}

	/**
	 * 初始化10个最近联系人聊天窗体
	 */
	public static void init()
	{
		// if (Config.UserSetting.recentFriends != null)
		// {
		// int count = 0;
		// for (long id : Config.UserSetting.recentFriends)
		// {
		// count++;
		// Friend f = MainForm.FORM.getFriendList().getFriend(id);
		// ChatForm form = new ChatForm(f);
		// chatForms.put(f.getFriendId(), form);
		//
		// if (count > 10)
		// {
		// break;
		// }
		// }
		// }
	}

	public static Collection<ChatForm> getChatForms()
	{
		return chatForms.values();
	}
}
