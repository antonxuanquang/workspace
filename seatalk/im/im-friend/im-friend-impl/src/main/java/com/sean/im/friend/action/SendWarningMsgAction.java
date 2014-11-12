package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.service.MessageServiceImpl;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "发送警告信息", authenticate = false,
mustParams = { Params.receiverId, Params.content})
public class SendWarningMsgAction extends Action
{
	@ResourceConfig
	private MessageServiceImpl msi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long receiverId = session.getLongParameter(Params.receiverId);
		String content = session.getParameter(Params.content);

		msi.sendWarnMessage(receiverId, content);
		session.success();
	}
}
