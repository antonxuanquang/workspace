package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.service.FriendServiceImpl;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "移动好友", authenticate = false,
mustParams = { Params.friendId, Params.groupId })
public class MoveFriendAction extends Action
{
	@ResourceConfig
	private FriendServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long friendId = session.getLongParameter(Params.friendId);
		long groupId = session.getLongParameter(Params.groupId);

		fsi.moveFriend(friendId, groupId);

		session.success();
	}
}
