package com.sean.im.client.message.handler;

import com.sean.im.client.constant.Global;
import com.sean.im.client.core.MessageHandler;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.Message;

/**
 * 授予群管理员
 * @author sean
 */
public class GrantFlockAdminHandler implements MessageHandler
{
	@Override
	public void receive(final Message msg)
	{
		Flock flock = MainForm.FORM.getFlockList().getFlock(Long.parseLong(msg.getContent()));
		if (flock != null)
		{
			UIUtil.alert(null, Global.Lan.get("你被授予群") + flock.getName() + Global.Lan.get("的管理员身份"));	
		}
	}
}
