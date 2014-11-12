package com.sean.im.friend.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.sean.commom.util.TimeUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.UserInfo;
import com.sean.im.context.spi.FriendSpi;
import com.sean.im.friend.entity.FriendEntity;
import com.sean.im.friend.entity.GroupEntity;
import com.sean.im.friend.entity.MessageEntity;
import com.sean.im.server.push.IMServer;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.enums.LogicEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Value;
import com.sean.service.annotation.BeanConfig;
import com.sean.service.annotation.ResourceConfig;

@BeanConfig(description = "friend interface")
public class FriendServiceImpl implements FriendSpi
{
	@ResourceConfig
	private UserService us;

	@Override
	public List<Long> getFriendIdOfUser(long userId)
	{
		EntityDao<FriendEntity> friendDao = PersistContext.CTX.getEntityDao(FriendEntity.class);
		List<FriendEntity> friends = friendDao.getListByColumn("userId", userId);
		List<Long> ids = new ArrayList<Long>(friends.size());
		for (FriendEntity f : friends)
		{
			ids.add(f.getFriendId());
		}
		return ids;
	}

	/**
	 * delete friend
	 * @param id
	 */
	public void deleteFriend(long id)
	{
		EntityDao<FriendEntity> dao = PersistContext.CTX.getEntityDao(FriendEntity.class);
		FriendEntity friend = dao.loadById(id);
		if (friend != null)
		{
			// 删除自己的好友
			dao.remove(id);

			// 删除对方的好友
			List<Condition> conds = new ArrayList<Condition>(2);
			conds.add(new Condition("userId", ConditionEnum.Equal, friend.getFriendId()));
			conds.add(new Condition(LogicEnum.And, "friendId", ConditionEnum.Equal, friend.getUserId()));
			dao.remove(conds);

			// 通知好友删除自己
			Protocol notify = new Protocol(Actions.DeleteFriendHandler);
			notify.receiver = friend.getFriendId();
			notify.setParameter("userId", friend.getUserId());
			IMServer.CTX.push(notify);
		}
	}

	/**
	 * check friendId is a friend of userId
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public boolean isFriend(long userId, long friendId)
	{
		EntityDao<FriendEntity> friendDao = PersistContext.CTX.getEntityDao(FriendEntity.class);
		List<Condition> conds = new ArrayList<Condition>();
		conds.add(new Condition("userId", ConditionEnum.Equal, userId));
		conds.add(new Condition(LogicEnum.And, "friendId", ConditionEnum.Equal, friendId));
		List<FriendEntity> fe = friendDao.getListByCond(conds);
		return fe.size() != 0;
	}

	/**
	 * agree friend request
	 * @param requesterId
	 * @param userId
	 */
	public void agreeFriendRequest(long requesterId, long userId)
	{
		EntityDao<FriendEntity> friendDao = PersistContext.CTX.getEntityDao(FriendEntity.class);
		EntityDao<MessageEntity> msgDao = PersistContext.CTX.getEntityDao(MessageEntity.class);
		EntityDao<GroupEntity> groupDao = PersistContext.CTX.getEntityDao(GroupEntity.class);

		MessageEntity me = new MessageEntity();
		me.setReceiverId(userId);
		me.setSenderId(requesterId);
		me.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
		me.setType(MessageEnum.Message_AgreeRequestFriend);

		if (!isFriend(requesterId, userId))
		{
			// save the relationship of requester
			List<Condition> conds = new ArrayList<Condition>();
			conds.add(new Condition("userId", ConditionEnum.Equal, requesterId));
			conds.add(new Condition(LogicEnum.And, "isDefault", ConditionEnum.Equal, 1));
			GroupEntity requesterGroup = groupDao.getListByCond(conds).get(0);
			FriendEntity requesterFriend = new FriendEntity();
			requesterFriend.setGroupId(requesterGroup.getId());
			requesterFriend.setUserId(requesterId);
			requesterFriend.setFriendId(userId);
			friendDao.persist(requesterFriend);

			// save the relationship of friend
			conds.clear();
			conds.add(new Condition("userId", ConditionEnum.Equal, userId));
			conds.add(new Condition(LogicEnum.And, "isDefault", ConditionEnum.Equal, 1));
			GroupEntity agreerGroup = groupDao.getListByCond(conds).get(0);
			FriendEntity agreerFriend = new FriendEntity();
			agreerFriend.setGroupId(agreerGroup.getId());
			agreerFriend.setUserId(userId);
			agreerFriend.setFriendId(requesterId);
			friendDao.persist(agreerFriend);

			UserInfo ue = us.getUserInfoById(requesterId);
			me.setContent(ue.getNickname() + "(" + ue.getUsername() + ") 同意您的好友添加请求");
			// if is online
			if (IMServer.CTX.isOnline(userId))
			{
				me.setIsRead(1);
				Protocol notify = new Protocol(Actions.RequestFriendResultHandler);
				notify.receiver = userId;
				notify.setParameter("msg", JSON.toJSONString(me));
				IMServer.CTX.push(notify);
			}
			msgDao.persist(me);
		}
	}

	/**
	 * refuce friend request
	 * @param requesterId
	 * @param userId
	 */
	public void refuceFriendRequest(long requesterId, long userId)
	{
		EntityDao<MessageEntity> msgDao = PersistContext.CTX.getEntityDao(MessageEntity.class);

		MessageEntity me = new MessageEntity();
		me.setReceiverId(userId);
		me.setSenderId(requesterId);
		me.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());

		if (!isFriend(requesterId, userId))
		{
			me.setType(MessageEnum.Message_RefuseRequestFriend);
			UserInfo ue = us.getUserInfoById(requesterId);
			me.setContent(ue.getNickname() + "(" + ue.getUsername() + ") 拒绝您的好友添加请求");

			// if is online
			if (IMServer.CTX.isOnline(userId))
			{
				me.setIsRead(1);
				Protocol notify = new Protocol(Actions.RequestFriendResultHandler);
				notify.receiver = userId;
				notify.setParameter("msg", JSON.toJSONString(me));
				IMServer.CTX.push(notify);
			}
			msgDao.persist(me);
		}
	}

	/**
	 * get friend entity
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public FriendEntity getFriend(long userId, long friendId)
	{
		EntityDao<FriendEntity> dao = PersistContext.CTX.getEntityDao(FriendEntity.class);
		List<Condition> conds = new ArrayList<Condition>(2);
		conds.add(new Condition("userId", ConditionEnum.Equal, userId));
		conds.add(new Condition(LogicEnum.And, "friendId", ConditionEnum.Equal, friendId));
		List<FriendEntity> fs = dao.getListByCond(conds);
		if (fs.size() != 0)
		{
			return fs.get(0);
		}
		return null;
	}

	/**
	 * get friend list of user
	 * @param userId
	 * @return
	 */
	public List<FriendEntity> getFriendList(long userId)
	{
		EntityDao<FriendEntity> fDao = PersistContext.CTX.getEntityDao(FriendEntity.class);
		List<FriendEntity> fe = fDao.getListByColumn("userId", userId);
		return fe;
	}

	/**
	 * move friend to the specify group
	 * @param id
	 * @param groupId
	 */
	public void moveFriend(long id, long groupId)
	{
		EntityDao<FriendEntity> dao = PersistContext.CTX.getEntityDao(FriendEntity.class);
		dao.update(id, new Value("groupId", groupId));
	}

	/**
	 * request to add friend
	 * @param userId
	 * @param friendId
	 * @param requestRemark
	 * @return			success-1,repeated-0
	 */
	public int requestFriend(long userId, long friendId, String requestRemark)
	{
		if (isFriend(userId, friendId))
		{
			return 0;
		}
		EntityDao<MessageEntity> msgDao = PersistContext.CTX.getEntityDao(MessageEntity.class);

		MessageEntity me = new MessageEntity();
		me.setContent(requestRemark);
		me.setReceiverId(friendId);
		me.setSenderId(userId);
		me.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
		me.setType(MessageEnum.Message_RequestFriend);

		// 如果对方在线
		if (IMServer.CTX.isOnline(friendId))
		{
			UserInfo userinfo = us.getUserInfoById(userId);

			Protocol notify = new Protocol(Actions.RequestFriendHandler);
			notify.receiver = friendId;
			notify.setParameter("msg", JSON.toJSONString(me));
			notify.setParameter("userinfo", JSON.toJSONString(userinfo));

			IMServer.CTX.push(notify);
			me.setIsRead(1);
		}
		// 不在线
		else
		{
			me.setIsRead(0);
		}
		msgDao.persist(me);
		return 1;
	}

	/**
	 * update remark name of friend
	 * @param id
	 * @param remarkName
	 */
	public void updateFriendRemark(long id, String remarkName)
	{
		EntityDao<FriendEntity> dao = PersistContext.CTX.getEntityDao(FriendEntity.class);
		dao.update(id, new Value("remark", remarkName));
	}
}
