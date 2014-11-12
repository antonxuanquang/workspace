package com.sean.im.flock.service;

import com.alibaba.fastjson.JSON;
import com.sean.commom.util.TimeUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.commom.core.Protocol;
import com.sean.im.flock.entity.FlockMessageEntity;
import com.sean.im.server.entity.FileEntity;
import com.sean.im.server.push.IMServer;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.ext.Value;
import com.sean.service.annotation.BeanConfig;

@BeanConfig(description = "flock message interface")
public class FlockMessageServiceImpl
{
	/**
	 * send text message to the specify flock
	 * @param senderId
	 * @param flockId
	 * @param content
	 */
	public void sendTextMessage(long senderId, long flockId, String content)
	{
		EntityDao<FlockMessageEntity> dao = PersistContext.CTX.getEntityDao(FlockMessageEntity.class);

		FlockMessageEntity me = new FlockMessageEntity();
		me.setContent(content);
		me.setFlockId(flockId);
		me.setSenderId(senderId);
		me.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
		me.setType(MessageEnum.Message_Text);
		dao.persist(me);

		// 通知群成员
		Protocol notify = new Protocol(Actions.ReceiveFlockMsgHandler);
		notify.setParameter("msg", JSON.toJSONString(me));
		IMServer.CTX.pushToFlockMembers(senderId, flockId, notify);
	}

	/**
	 * send binary message to the specify flock
	 * @param senderId
	 * @param flockId
	 * @param content
	 * @param type
	 */
	public void sendBinaryMessage(long senderId, long flockId, String content, int type)
	{
		EntityDao<FlockMessageEntity> dao = PersistContext.CTX.getEntityDao(FlockMessageEntity.class);

		FlockMessageEntity me = new FlockMessageEntity();
		me.setContent(content);
		me.setFlockId(flockId);
		me.setSenderId(senderId);
		me.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
		me.setType(type);
		dao.persist(me);

		// 通知群成员
		Protocol notify = new Protocol(Actions.ReceiveFlockMsgHandler);
		notify.setParameter("msg", JSON.toJSONString(me));
		IMServer.CTX.pushToFlockMembers(senderId, flockId, notify);
	}

	/**
	 * send file to the specify flock
	 * @param senderId
	 * @param flockId
	 * @param fileId
	 * @param displayFileName
	 */
	public void sendFileMessage(long senderId, long flockId, long fileId, String displayFileName)
	{
		EntityDao<FileEntity> fDao = PersistContext.CTX.getEntityDao(FileEntity.class);
		fDao.update(fileId, new Value("filename", displayFileName));

		FileEntity fe = fDao.loadById(fileId);
		// 通知群成员信息
		Protocol notify = new Protocol(Actions.ReceiveFlockFileHandler);
		notify.sender = senderId;
		notify.setParameter("file", JSON.toJSONString(fe));
		IMServer.CTX.pushToFlockMembers(senderId, flockId, notify);
	}
}
