package com.sean.im.client.message.handler;

import com.sean.im.client.core.MessageHandler;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.entity.Message;

/**
 * 好友同意请求
 * @author sean
 */
public class AgreeRequestFriendHandler implements MessageHandler
{
	@Override
	public void receive(final Message msg)
	{
		UIUtil.alert(null, msg.getContent());
	}
}
