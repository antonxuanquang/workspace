package com.sean.im.friend.action;

import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.entity.UserInfoEntity;
import com.sean.im.friend.service.UserServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "读取用户信息", authenticate = false,
mustParams = { P.userId },
returnParams = { RetParams.usershort })
public class InquireInfoBriefAction extends Action
{
	@ResourceConfig
	private UserServiceImpl usi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long userId = session.getLongParameter(P.userId);
		
		UserInfoEntity user = usi.getUserById(userId);
		
		session.setReturnAttribute(RetParams.usershort, user);
		session.success();
	}
}
