package com.sean.im.flock.action;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.constant.RetParams;
import com.sean.im.flock.entity.FlockEntity;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "创建群", authenticate = false,
mustParams = { P.loginerId, Params.flockName },
optionalParams = { Params.flockSignature, Params.flockDescr },
returnParams = { RetParams.flockId, RetParams.flockMemberId })
public class CreateFlockAction extends Action
{
	@ResourceConfig
	private FlockServiceImpl fsi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);

		FlockEntity fe = new FlockEntity();
		fe.setName(session.getParameter(Params.flockName));
		fe.setSignature(session.getParameter(Params.flockSignature));
		fe.setDescription(session.getParameter(Params.flockDescr));
		long memberId = fsi.createFlock(loginerId, fe);

		session.setReturnAttribute(RetParams.flockId, fe.getId());
		session.setReturnAttribute(RetParams.flockMemberId, memberId);
		session.success();
	}
}
