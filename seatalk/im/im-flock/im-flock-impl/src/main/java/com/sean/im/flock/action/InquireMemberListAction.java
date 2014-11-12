package com.sean.im.flock.action;

import java.util.List;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.constant.RetParams;
import com.sean.im.flock.entity.FlockMemberEntity;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "读取群成员列表", authenticate = false,
mustParams = { Params.flockId },
returnParams = { RetParams.flockMemberList })
public class InquireMemberListAction extends Action
{
	@ResourceConfig
	private FlockServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long flockId = session.getLongParameter(Params.flockId);

		List<FlockMemberEntity> members = fsi.getMemberList(flockId);

		session.setReturnAttribute(RetParams.flockMemberList, members);
		session.success();
	}
}
