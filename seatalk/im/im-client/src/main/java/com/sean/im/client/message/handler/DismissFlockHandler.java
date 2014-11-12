package com.sean.im.client.message.handler;

import com.sean.im.client.constant.Global;
import com.sean.im.client.core.MessageHandler;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.Message;

/**
 * 解散群
 * @author sean
 */
public class DismissFlockHandler implements MessageHandler
{
	@Override
	public void receive(final Message msg)
	{
		Flock flock = (Flock)msg.getSingleParam();
		UIUtil.alert(null, flock.getName() + Global.Lan.get("已被创建人解散"));
	}
}
