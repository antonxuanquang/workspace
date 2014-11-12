package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.service.UserServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "修改翻译语言",authenticate = false,
mustParams = { P.loginerId },
optionalParams = { Params.translator })
public class ChangeTranslatorAction extends Action
{
	@ResourceConfig
	private UserServiceImpl usi;
	
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		String translator = session.getParameter(Params.translator);

		usi.changeTranslator(loginerId, translator);
		
		session.success();
	}
}
