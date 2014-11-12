package com.sean.im.flock.action;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.constant.RetParams;
import com.sean.im.flock.entity.FlockEntity;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "inquire flock detail infomation", authenticate = false,
mustParams = { Params.flockId },
returnParams = { RetParams.flockFull, RetParams.flockCreater })
public class InquireFlockInfoAction extends Action
{
	@ResourceConfig
	private FlockServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long flockId = session.getLongParameter(Params.flockId);

		FlockEntity fe = fsi.getFlockInfo(flockId);

		session.setReturnAttribute(RetParams.flockFull, fe);
		session.setReturnAttribute(RetParams.flockCreater, fe);
		session.success();
	}
}
