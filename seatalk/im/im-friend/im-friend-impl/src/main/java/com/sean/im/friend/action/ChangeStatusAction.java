package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.service.UserServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "修改状态", authenticate = false, 
mustParams = { P.loginerId, Params.status })
public class ChangeStatusAction extends Action
{
	@ResourceConfig
	private UserServiceImpl usi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		int status = session.getIntParameter(Params.status);

		usi.changeStatus(loginerId, status);

		session.success();
	}
}
