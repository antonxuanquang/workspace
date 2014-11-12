package com.sean.im.client.core;

import java.util.LinkedList;
import java.util.List;

import com.sean.im.client.bubble.Sender;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.FlockMember;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Message;
import com.sean.im.commom.entity.UserInfo;

/**
 * 上下文
 * @author sean
 */
public class ApplicationContext
{
	public static ApplicationContext CTX;
	public static String PushHost = "localhost";
	public static int PushPort = 9999;
	public static final String Version = "1.0";
	public static Client Client;
	public static UserInfo User;

	private PushHandler serverMsgHandler;
	private MessageHandler messageHandler;
	private List<Message> messageQueue;// 未读取的消息队列

	public ApplicationContext()
	{
		Client = new Client(1024 * 1024);
		messageQueue = new LinkedList<Message>();
		serverMsgHandler = new PushHandlerImpl();
		messageHandler = new MessageHandlerImpl();
	}
	
	public static Sender getSender()
	{
		Sender sender = new Sender();
		sender.head = User.getHead();
		sender.nickname = User.getNickname();
		sender.userId = User.getId();
		return sender;
	}
	
	public static Sender getSender(Friend friend)
	{
		Sender sender = new Sender();
		sender.head = friend.getHead();
		sender.nickname = friend.getNickname();
		sender.userId = friend.getFriendId();
		return sender;
	}
	
	public static Sender getSender(FlockMember fm)
	{
		Sender sender = new Sender();
		sender.head = fm.getHead();
		sender.nickname = fm.getNickname();
		sender.userId = fm.getUserId();
		return sender;
	}

	/**
	 * 处理服务器主动发送的消息
	 */
	public void doPush(Protocol protocol)
	{
		serverMsgHandler.execute(protocol);
	}

	/**
	 * 处理消息
	 * @param msg
	 */
	public void doMessage(Message msg)
	{
		this.messageHandler.receive(msg);
	}

	public synchronized List<Message> getMessageQueue()
	{
		return messageQueue;
	}

}
