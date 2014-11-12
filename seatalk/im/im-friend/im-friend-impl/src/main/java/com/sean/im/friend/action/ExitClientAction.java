package com.sean.im.friend.action;

import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.Protocol;
import com.sean.im.context.spi.UserSpi;
import com.sean.im.server.constant.P;
import com.sean.im.server.push.IMServer;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.ApplicationContext;
import com.sean.service.core.Session;

@ActionConfig(description = "强制下线", authenticate = false, mustParams = { P.userId })
public class ExitClientAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		long userId = session.getLongParameter(P.userId);
		// 删除客户端
		// IMServer.CTX.removeClient(userId);

		// 修改状态
		UserSpi us = ApplicationContext.CTX.getBean(UserSpi.class);
		us.initUserStatus(userId);

		// 通知客户端
		Protocol notify = new Protocol(Actions.ExitHandler);
		notify.receiver = userId;
		notify.setParameter("exitType", 1);
		IMServer.CTX.push(notify);

		// 通知好友
		notify = new Protocol(Actions.StatusChangedHandler);
		notify.setParameter("status", 0);
		notify.setParameter("userId", userId);
		IMServer.CTX.pushToAllFriend(userId, notify);
	}
}
