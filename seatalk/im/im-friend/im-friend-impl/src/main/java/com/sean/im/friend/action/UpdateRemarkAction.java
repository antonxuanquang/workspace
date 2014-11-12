package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.service.FriendServiceImpl;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "修改备注", authenticate = false, 
mustParams = { Params.friendId, Params.remarkName })
public class UpdateRemarkAction extends Action
{
	@ResourceConfig
	private FriendServiceImpl fsi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long friendId = session.getLongParameter(Params.friendId);
		String remarkName = session.getParameter(Params.remarkName);

		fsi.updateFriendRemark(friendId, remarkName);
		
		session.success();
	}
}
