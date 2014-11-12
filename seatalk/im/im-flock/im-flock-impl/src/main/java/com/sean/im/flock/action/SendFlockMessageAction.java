package com.sean.im.flock.action;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.service.FlockMessageServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "发送群消息", authenticate = false, 
mustParams = { P.loginerId, Params.flockId, Params.flockMessageContent })
public class SendFlockMessageAction extends Action
{	
	@ResourceConfig
	private FlockMessageServiceImpl fmsi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long flockId = session.getLongParameter(Params.flockId);
		String content = session.getParameter(Params.flockMessageContent);
		
		fmsi.sendTextMessage(loginerId, flockId, content);
		
		session.success();
	}
}
