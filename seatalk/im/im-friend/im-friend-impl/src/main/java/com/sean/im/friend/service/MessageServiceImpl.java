package com.sean.im.friend.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.sean.commom.util.TimeUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.Message;
import com.sean.im.friend.entity.MessageEntity;
import com.sean.im.server.entity.FileEntity;
import com.sean.im.server.push.IMServer;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;
import com.sean.persist.ext.Value;
import com.sean.service.annotation.BeanConfig;

@BeanConfig(description = "message interface")
public class MessageServiceImpl implements MessageService
{
	@Override
	public void insertMessage(Message msg, boolean isRead)
	{
		MessageEntity me = new MessageEntity();
		me.setContent(msg.getContent());
		me.setIsRead(isRead ? 1 : 0);
		me.setReceiverId(msg.getReceiverId());
		me.setSenderId(msg.getSenderId());
		me.setSendTime(msg.getSendTime());
		me.setType(msg.getType());
		EntityDao<MessageEntity> dao = PersistContext.CTX.getEntityDao(MessageEntity.class);
		dao.persist(me);
	}

	/**
	 * get unread message
	 * @param userId
	 * @return
	 */
	public List<MessageEntity> getUnReadMessage(long userId)
	{
		EntityDao<MessageEntity> dao = PersistContext.CTX.getEntityDao(MessageEntity.class);
		List<Condition> conds = new ArrayList<Condition>();
		conds.add(new Condition("ownerId", ConditionEnum.Equal, userId));
		conds.add(new Condition("receiverId", ConditionEnum.Equal, userId));
		conds.add(new Condition("isRead", ConditionEnum.Equal, "0"));
		List<MessageEntity> msgs = dao.getListByCond(conds, new Order("id", OrderEnum.Asc));

		// update message as readed
		Value val = new Value("isRead", "1");
		for (MessageEntity me : msgs)
		{
			dao.update(me.getId(), val);
		}
		return msgs;
	}

	/**
	 * send binary message to client
	 * @param senderId
	 * @param receiverId
	 * @param content
	 * @param type
	 */
	public void sendBinaryMessage(long senderId, long receiverId, String content, int type)
	{
		EntityDao<MessageEntity> dao = PersistContext.CTX.getEntityDao(MessageEntity.class);

		MessageEntity me = new MessageEntity();
		me.setContent(content);
		me.setReceiverId(receiverId);
		me.setSenderId(senderId);
		me.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
		me.setType(type);

		if (IMServer.CTX.isOnline(receiverId))
		{
			me.setIsRead(1);
			// 通知好友信息
			Protocol notify = new Protocol(Actions.ReceiveMsgHandler);
			notify.receiver = receiverId;
			notify.setParameter("msg", JSON.toJSONString(me));
			IMServer.CTX.push(notify);
		}
		else
		{
			me.setIsRead(0);
		}
		dao.persist(me);
	}

	/**
	 * send text message to client
	 * @param senderId
	 * @param receiverId
	 * @param content
	 * @param original
	 */
	public void sendTextMessage(long senderId, long receiverId, String content, long ownerId)
	{
		EntityDao<MessageEntity> dao = PersistContext.CTX.getEntityDao(MessageEntity.class);

		MessageEntity me = new MessageEntity();
		me.setContent(content);
		me.setReceiverId(receiverId);
		me.setSenderId(senderId);
		me.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
		me.setType(MessageEnum.Message_Text);
		me.setOwnerId(ownerId);

		if (senderId == ownerId)
		{
			me.setIsRead(1);
		}
		else
		{
			if (IMServer.CTX.isOnline(receiverId))
			{
				me.setIsRead(1);
				// 通知好友信息
				Protocol notify = new Protocol(Actions.ReceiveMsgHandler);
				notify.receiver = receiverId;
				notify.setParameter("msg", JSON.toJSONString(me));
				IMServer.CTX.push(notify);
			}
			else
			{
				me.setIsRead(0);
			}
		}
		dao.persist(me);
	}

	/**
	 * send warn message to client
	 * @param senderId
	 * @param receiverId
	 * @param content
	 * @param original
	 */
	public void sendWarnMessage(long receiverId, String content)
	{
		MessageEntity me = new MessageEntity();
		me.setContent(content);
		me.setReceiverId(receiverId);
		me.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
		me.setType(MessageEnum.Message_Text);

		if (IMServer.CTX.isOnline(receiverId))
		{
			me.setIsRead(1);
			// 通知好友信息
			Protocol notify = new Protocol(Actions.ReceiveWarnMsgHandler);
			notify.receiver = receiverId;
			notify.setParameter("msg", JSON.toJSONString(me));
			IMServer.CTX.push(notify);
		}
	}

	/**
	 * send file message to client
	 * @param senderId
	 * @param receiverId
	 * @param fileId
	 * @param displayFileName
	 */
	public void sendFileMessage(long senderId, long receiverId, long fileId, String displayFileName)
	{
		EntityDao<FileEntity> fDao = PersistContext.CTX.getEntityDao(FileEntity.class);
		fDao.update(fileId, new Value("filename", displayFileName));

		FileEntity fe = fDao.loadById(fileId);
		if (fe != null && IMServer.CTX.isOnline(receiverId))
		{
			// 通知好友信息
			Protocol notify = new Protocol(Actions.ReceiveFileHandler);
			notify.receiver = receiverId;
			notify.sender = senderId;
			notify.setParameter("file", JSON.toJSONString(fe));
			IMServer.CTX.push(notify);
		}
	}
}
