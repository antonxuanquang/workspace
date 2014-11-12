package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.service.FriendServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "请求添加好友", authenticate = false,
mustParams = { P.loginerId, P.userId, Params.remark },
returnParams = { RetParams.requestrs })
public class RequestFriendAction extends Action
{
	@ResourceConfig
	private FriendServiceImpl fsi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long userId = session.getLongParameter(P.userId);// 被请求的用户ID
		String remark = session.getParameter(Params.remark);

		int rs = fsi.requestFriend(loginerId, userId, remark);

		session.setReturnAttribute(RetParams.requestrs, rs);
		session.success();
	}
}
