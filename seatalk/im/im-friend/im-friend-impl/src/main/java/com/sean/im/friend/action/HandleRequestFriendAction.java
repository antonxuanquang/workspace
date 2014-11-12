package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.service.FriendServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "处理好友请求",authenticate = false,
mustParams = { P.loginerId, P.userId, Params.requestOperate })
public class HandleRequestFriendAction extends Action
{
	@ResourceConfig
	private FriendServiceImpl fsi;
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long userId = session.getLongParameter(P.userId);
		int requestOperate = session.getIntParameter(Params.requestOperate);

		// 同意添加好友
		if (requestOperate == 1)
		{
			fsi.agreeFriendRequest(loginerId, userId);
		}
		// 拒绝添加好友
		else if (requestOperate == 0)
		{
			fsi.refuceFriendRequest(loginerId, userId);
		}
		
		session.success();
	}
}
