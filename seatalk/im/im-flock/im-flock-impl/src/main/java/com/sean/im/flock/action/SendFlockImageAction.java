package com.sean.im.flock.action;

import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.flock.constant.Params;
import com.sean.im.flock.service.FlockMessageServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "发送群图片", authenticate = false, 
mustParams = { P.loginerId, Params.flockId, P.imgUrl })
public class SendFlockImageAction extends Action
{
	@ResourceConfig
	private FlockMessageServiceImpl fmsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long flockId = session.getLongParameter(Params.flockId);
		String imgUrl = session.getParameter(P.imgUrl);

		fmsi.sendBinaryMessage(loginerId, flockId, imgUrl, MessageEnum.Message_Image);

		session.success();
	}
}
