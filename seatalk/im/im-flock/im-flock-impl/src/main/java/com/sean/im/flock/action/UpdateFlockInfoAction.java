package com.sean.im.flock.action;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "修改群信息", authenticate = false, 
mustParams = { Params.flockId, Params.flockName },
optionalParams = { P.signature, P.description })
public class UpdateFlockInfoAction extends Action
{
	@ResourceConfig
	private FlockServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long flockId = session.getLongParameter(Params.flockId);
		String flockName = session.getParameter(Params.flockName);
		String signature = session.getParameter(P.signature);
		String description = session.getParameter(P.description);

		fsi.updateFlockInfo(flockId, flockName, signature, description);

		session.success();
	}
}
