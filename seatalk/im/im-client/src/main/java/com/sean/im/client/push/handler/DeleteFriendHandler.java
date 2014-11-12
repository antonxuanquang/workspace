package com.sean.im.client.push.handler;

import com.sean.im.client.core.PushHandler;
import com.sean.im.client.form.MainForm;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.Friend;

/**
 * 好友删除自己
 * @author sean
 */
public class DeleteFriendHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		long userId = notify.getLongParameter("userId");
		Friend friend = MainForm.FORM.getFriendList().getFriendByUserId(userId);
		if (friend != null)
		{
			MainForm.FORM.getFriendList().removeFriend(friend.getId());
			MainForm.FORM.getRecentFriendList().removeFriend(friend);
		}
	}
}
