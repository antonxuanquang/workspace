package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.entity.GroupEntity;
import com.sean.im.friend.service.GroupServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "添加分组", authenticate = false,
mustParams = { P.loginerId, Params.groupName },
returnParams = { RetParams.groupId })
public class AddGroupAction extends Action
{
	@ResourceConfig
	private GroupServiceImpl gsi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		String groupName = session.getParameter(Params.groupName);

		GroupEntity ge = gsi.addGroup(loginerId, groupName);

		session.setReturnAttribute(RetParams.groupId, ge.getId());
		session.success();
	}
}
