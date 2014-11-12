package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.service.UserServiceImpl;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "注册用户", authenticate = false,
mustParams = { Params.username, Params.password, Params.nickname },
returnParams = { RetParams.registrs })
public class RegistAction extends Action
{
	@ResourceConfig
	private UserServiceImpl usi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		String username = session.getParameter(Params.username);
		String nickname = session.getParameter(Params.nickname);
		String password = session.getParameter(Params.password);

		int rs = usi.createUser(username, password, nickname);

		session.setReturnAttribute(RetParams.registrs, rs);
		session.success();
	}
}
