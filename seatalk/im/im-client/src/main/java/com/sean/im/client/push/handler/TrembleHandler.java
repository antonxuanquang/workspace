package com.sean.im.client.push.handler;

import com.sean.im.client.comp.FriendListComp;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.form.ChatForm;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.Friend;

/**
 * 抖动信号
 * @author sean
 */
public class TrembleHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		FriendListComp friendList = MainForm.FORM.getFriendList();
		Friend friend = friendList.getFriendByUserId(notify.sender);
		ChatForm form = ChatFormCache.getChatForm(friend);
		form.shake();
	}
}
