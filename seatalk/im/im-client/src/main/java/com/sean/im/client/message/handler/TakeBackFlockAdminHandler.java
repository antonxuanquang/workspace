package com.sean.im.client.message.handler;

import com.sean.im.client.constant.Global;
import com.sean.im.client.core.MessageHandler;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.Message;

/**
 * 收回群管理员
 * @author sean
 */
public class TakeBackFlockAdminHandler implements MessageHandler
{
	@Override
	public void receive(final Message msg)
	{
		Flock flock = MainForm.FORM.getFlockList().getFlock(Long.parseLong(msg.getContent()));
		if (flock != null)
		{
			UIUtil.alert(null, flock.getName() + Global.Lan.get("管理员身份被创建者收回"));	
		}
	}
}
