package com.sean.im.friend.action;

import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.entity.FriendEntity;
import com.sean.im.friend.service.FriendServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "读取好友", authenticate = false,
mustParams = { P.loginerId, P.userId },
returnParams = { RetParams.friend })
public class InquireFriendAction extends Action
{
	@ResourceConfig
	private FriendServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long userId = session.getLongParameter(P.userId);

		FriendEntity fe = fsi.getFriend(loginerId, userId);
		if (fe != null)
		{
			session.setReturnAttribute(RetParams.friend, fe);
		}
		session.success();
	}
}
