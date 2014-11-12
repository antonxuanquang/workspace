package com.sean.im.friend.action;

import com.sean.config.core.Config;
import com.sean.im.friend.constant.Params;
import com.sean.im.friend.constant.RetParams;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "管理员登录", authenticate = false,
mustParams = { Params.username, Params.password },
returnParams = { RetParams.loginrs })
public class AdminLoginAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		String username = session.getParameter(Params.username);
		String password = session.getParameter(Params.password);
		
		String _username = Config.getProperty("console.username");
		String _password = Config.getProperty("console.password");
		
		if (username.equals(_username) && password.equals(_password))
		{
			session.setReturnAttribute(RetParams.loginrs, 1);
			session.success();
		}
		else
		{
			session.setReturnAttribute(RetParams.loginrs, 0);
			session.success();
		}
	}
}
