package com.sean.im.flock.action;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "设置为群管理员", authenticate = false, 
mustParams = { P.loginerId, Params.flockMemberId })
public class GrantFlockAdminAction extends Action
{
	@ResourceConfig
	private FlockServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long flockMemberId = session.getLongParameter(Params.flockMemberId);

		fsi.manageFlockAdmin(loginerId, flockMemberId, true);

		session.success();
	}
}
