package com.sean.im.client.message.handler;

import com.sean.im.client.core.MessageHandler;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.entity.Message;

/**
 * 好友拒绝请求
 * @author sean
 */
public class RefuseRequestFriendHandler implements MessageHandler
{
	@Override
	public void receive(final Message msg)
	{
		UIUtil.alert(null, msg.getContent());
	}

}
