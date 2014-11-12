package com.sean.im.flock.action;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "踢出群", authenticate = false,
mustParams = { P.loginerId, Params.flockMemberId })
public class KickOutFlockAction extends Action
{
	@ResourceConfig
	private FlockServiceImpl fsi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long flockMemberId = session.getLongParameter(Params.flockMemberId);

		fsi.kickOutMember(loginerId, flockMemberId);
		
		session.success();
	}
}
