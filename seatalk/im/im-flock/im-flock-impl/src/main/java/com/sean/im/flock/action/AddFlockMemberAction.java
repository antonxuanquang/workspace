package com.sean.im.flock.action;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "添加群成员", authenticate = false,
mustParams = { P.loginerId, Params.flockId, Params.flockMemberIdList })
public class AddFlockMemberAction extends Action
{
	@ResourceConfig
	private FlockServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long flockId = session.getLongParameter(Params.flockId);
		long[] memberIds = session.getLongParameters(Params.flockMemberIdList);

		fsi.addFlockMemberRequest(loginerId, flockId, memberIds);
		session.success();
	}
}
