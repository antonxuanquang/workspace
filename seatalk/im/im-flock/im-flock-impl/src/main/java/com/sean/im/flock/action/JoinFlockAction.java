package com.sean.im.flock.action;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "加入群", authenticate = false,
mustParams = { P.loginerId, Params.flockId })
public class JoinFlockAction extends Action
{
	@ResourceConfig
	private FlockServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long flockId = session.getLongParameter(Params.flockId);

		fsi.joinInFlock(loginerId, flockId);
		session.success();
	}
}
