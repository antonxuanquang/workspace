package com.sean.im.friend.action;

import java.util.List;

import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.entity.MessageEntity;
import com.sean.im.friend.service.MessageServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "读取未读消息", authenticate = false,
mustParams = { P.loginerId },
returnParams = { RetParams.msgs })
public class InquireUnReadMsgAction extends Action
{
	@ResourceConfig
	private MessageServiceImpl msi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);

		List<MessageEntity> msgs = msi.getUnReadMessage(loginerId);

		session.setReturnAttribute(RetParams.msgs, msgs);
		session.success();
	}
}
