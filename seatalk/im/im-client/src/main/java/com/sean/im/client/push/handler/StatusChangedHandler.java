package com.sean.im.client.push.handler;

import com.sean.im.client.core.PushHandler;
import com.sean.im.client.form.MainForm;
import com.sean.im.commom.core.Protocol;

/**
 * 好友状态改变
 * @author sean
 */
public class StatusChangedHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		// 好友的用户Id，不是好友列表Id
		long userId = notify.getLongParameter("userId");
		int status = notify.getIntParameter("status");
		MainForm.FORM.getFriendList().updateFriendStatus(userId, status);
		MainForm.FORM.getRecentFriendList().updateFriendStatus(userId, status);
	}
}
