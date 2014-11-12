package com.sean.im.flock.action;

import java.util.List;

import com.sean.im.flock.constant.RetParams;
import com.sean.im.flock.entity.FlockEntity;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "读取群列表", authenticate = false,
mustParams = { P.loginerId },
returnParams = { RetParams.flockList })
public class InquireFlockListAction extends Action
{
	@ResourceConfig
	private FlockServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);

		List<FlockEntity> fe = fsi.getFlockList(loginerId);

		session.setReturnAttribute(RetParams.flockList, fe);
		session.success();
	}
}
