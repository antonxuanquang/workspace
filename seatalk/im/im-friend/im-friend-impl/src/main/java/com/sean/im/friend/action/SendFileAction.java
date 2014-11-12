package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.service.MessageServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "文件传输",authenticate = false,
mustParams = { P.loginerId, Params.receiverId, P.fileId, P.filename })
public class SendFileAction extends Action
{
	@ResourceConfig
	private MessageServiceImpl msi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long receiverId = session.getLongParameter(Params.receiverId);
		long fileId = session.getLongParameter(P.fileId);
		String filename = session.getParameter(P.filename);

		msi.sendFileMessage(loginerId, receiverId, fileId, filename);
		session.success();
	}
}
