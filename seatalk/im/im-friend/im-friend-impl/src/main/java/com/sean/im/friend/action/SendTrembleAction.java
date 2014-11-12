package com.sean.im.friend.action;

import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.Protocol;
import com.sean.im.friend.constant.Params;
import com.sean.im.server.constant.P;
import com.sean.im.server.push.IMServer;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "发送抖动信号", authenticate = false, 
mustParams = { P.loginerId, Params.receiverId })
public class SendTrembleAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long receiverId = session.getLongParameter(Params.receiverId);

		if (IMServer.CTX.isOnline(receiverId))
		{
			// 通知好友信息
			Protocol notify = new Protocol(Actions.TrembleHandler);
			notify.receiver = receiverId;
			notify.sender = loginerId;
			IMServer.CTX.push(notify);
		}
		session.success();
	}
}
