package com.sean.im.client.core;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.client.message.handler.AgreeRequestFriendHandler;
import com.sean.im.client.message.handler.DismissFlockHandler;
import com.sean.im.client.message.handler.GrantFlockAdminHandler;
import com.sean.im.client.message.handler.JoinInFlockHandler;
import com.sean.im.client.message.handler.KickOutFlockHandler;
import com.sean.im.client.message.handler.RefuseRequestFriendHandler;
import com.sean.im.client.message.handler.RequestFriendHandler;
import com.sean.im.client.message.handler.TakeBackFlockAdminHandler;
import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.commom.entity.Message;

/**
 * 消息处理
 * @author sean
 */
public class MessageHandlerImpl implements MessageHandler
{
	private Map<Integer, MessageHandler> handlers;

	public MessageHandlerImpl()
	{
		handlers = new HashMap<Integer, MessageHandler>(10);
		handlers.put(MessageEnum.Message_AgreeRequestFriend, new AgreeRequestFriendHandler());
		handlers.put(MessageEnum.Message_RefuseRequestFriend, new RefuseRequestFriendHandler());
		handlers.put(MessageEnum.Message_RequestFriend, new RequestFriendHandler());
		handlers.put(MessageEnum.Message_GrantFlockAdmin, new GrantFlockAdminHandler());
		handlers.put(MessageEnum.Message_TakeBackFlockAdmin, new TakeBackFlockAdminHandler());
		handlers.put(MessageEnum.Message_KickOutFlock, new KickOutFlockHandler());
		handlers.put(MessageEnum.Message_JoinInFlock, new JoinInFlockHandler());
		handlers.put(MessageEnum.Message_DismissFlock, new DismissFlockHandler());
	}

	@Override
	public void receive(Message msg)
	{
		MessageHandler handler = handlers.get(msg.getType());
		if (handler != null)
		{
			handler.receive(msg);
		}
	}
}
