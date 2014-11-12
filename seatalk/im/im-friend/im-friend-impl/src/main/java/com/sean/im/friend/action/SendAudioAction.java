package com.sean.im.friend.action;

import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.friend.constant.Params;
import com.sean.im.friend.service.MessageServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "发送语音", authenticate = false, 
mustParams = {P.loginerId, Params.receiverId, P.audioUrl})
public class SendAudioAction extends Action
{
	@ResourceConfig
	private MessageServiceImpl msi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long receiverId = session.getLongParameter(Params.receiverId);
		String audioUrl = session.getParameter(P.audioUrl);

		msi.sendBinaryMessage(loginerId, receiverId, audioUrl, MessageEnum.Message_Audio);

		session.success();
	}
}
