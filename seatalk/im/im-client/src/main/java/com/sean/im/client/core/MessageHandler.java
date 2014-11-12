package com.sean.im.client.core;

import com.sean.im.commom.entity.Message;

/**
 * 消息处理接口
 * @author sean
 */
public interface MessageHandler
{
	public void receive(Message msg);
}
