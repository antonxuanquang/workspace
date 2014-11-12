package com.sean.im.friend.action;

import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.friend.constant.Params;
import com.sean.im.friend.service.MessageServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "发送图片", authenticate = false, 
mustParams = { P.loginerId, Params.receiverId, P.imgUrl })
public class SendImageAction extends Action
{
	@ResourceConfig
	private MessageServiceImpl msi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long receiverId = session.getLongParameter(Params.receiverId);
		String imgUrl = session.getParameter(P.imgUrl);

		msi.sendBinaryMessage(loginerId, receiverId, imgUrl, MessageEnum.Message_Image);

		session.success();
	}
}
