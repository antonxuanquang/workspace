package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.service.UserServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "修改密码", authenticate = false, 
mustParams = { P.loginerId, Params.password, Params.oldpassword },
returnParams = { RetParams.changePasswordRs })
public class ChangePasswordAction extends Action
{
	@ResourceConfig
	private UserServiceImpl usi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		String oldPassword = session.getParameter(Params.oldpassword);
		String password = session.getParameter(Params.password);

		int changePasswordRs = usi.changePassword(loginerId, oldPassword, password);

		session.setReturnAttribute(RetParams.changePasswordRs, changePasswordRs);
		session.success();
	}
}
