package com.sean.im.friend.action;

import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.service.FriendServiceImpl;
import com.sean.im.friend.service.GroupServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "查询用户好友列表", authenticate = false,
mustParams = { P.loginerId },
returnParams = { RetParams.friendlist, RetParams.grouplist })
public class InquireFriendListAction extends Action
{
	@ResourceConfig
	private FriendServiceImpl fsi;
	@ResourceConfig
	private GroupServiceImpl gsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);

		session.setReturnAttribute(RetParams.friendlist, fsi.getFriendList(loginerId));
		session.setReturnAttribute(RetParams.grouplist, gsi.getGroupList(loginerId));
		session.success();
	}
}
