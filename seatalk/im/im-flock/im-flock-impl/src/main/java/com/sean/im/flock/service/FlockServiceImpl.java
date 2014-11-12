package com.sean.im.flock.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.sean.commom.util.TimeUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.Message;
import com.sean.im.context.spi.FlockSpi;
import com.sean.im.flock.entity.FlockCardEntity;
import com.sean.im.flock.entity.FlockEntity;
import com.sean.im.flock.entity.FlockMemberEntity;
import com.sean.im.friend.service.MessageService;
import com.sean.im.server.push.IMServer;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Value;
import com.sean.service.annotation.BeanConfig;
import com.sean.service.annotation.ResourceConfig;

@BeanConfig(description = "flock interface")
public class FlockServiceImpl implements FlockSpi
{
	@ResourceConfig
	private MessageService ms;

	@Override
	public List<Long> getFlockMembers(long flockId)
	{
		EntityDao<FlockMemberEntity> fmDao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);
		List<FlockMemberEntity> members = fmDao.getListByColumn("flockId", flockId);
		List<Long> ids = new ArrayList<Long>(members.size());
		for (FlockMemberEntity m : members)
		{
			ids.add(m.getUserId());
		}
		return ids;
	}

	/**
	 * get member list of flock
	 * @param flockId
	 * @return
	 */
	public List<FlockMemberEntity> getMemberList(long flockId)
	{
		EntityDao<FlockMemberEntity> fmDao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);
		List<FlockMemberEntity> members = fmDao.getListByColumn("flockId", flockId);
		return members;
	}

	/**
	 * get member info
	 * @param userId
	 * @param flockId
	 * @return
	 */
	public FlockMemberEntity getMemberOfFlock(long userId, long flockId)
	{
		EntityDao<FlockMemberEntity> dao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);
		List<Condition> conds = new ArrayList<Condition>(2);
		conds.add(new Condition("flockId", ConditionEnum.Equal, flockId));
		conds.add(new Condition("userId", ConditionEnum.Equal, userId));
		return dao.loadByCond(conds);
	}

	/**
	 * get flock info
	 * @param flockId
	 * @return
	 */
	public FlockEntity getFlockInfo(long flockId)
	{
		EntityDao<FlockEntity> fDao = PersistContext.CTX.getEntityDao(FlockEntity.class);
		FlockEntity fe = fDao.loadById(flockId);
		return fe;
	}

	/**
	 * update flock infomation
	 * @param flockId
	 * @param flockName
	 * @param signature
	 * @param description
	 */
	public void updateFlockInfo(long flockId, String flockName, String signature, String description)
	{
		EntityDao<FlockEntity> dao = PersistContext.CTX.getEntityDao(FlockEntity.class);
		List<Value> vals = new ArrayList<Value>(3);
		vals.add(new Value("name", flockName));
		vals.add(new Value("signature", signature));
		vals.add(new Value("description", description));
		dao.update(flockId, vals);
	}

	/**
	 * get flock list of user
	 * @param userId
	 * @return
	 */
	public List<FlockEntity> getFlockList(long userId)
	{
		EntityDao<FlockEntity> fDao = PersistContext.CTX.getEntityDao(FlockEntity.class);
		EntityDao<FlockMemberEntity> fmDao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);

		// get flocks which userId joined
		List<FlockMemberEntity> fme = fmDao.getListByColumn("userId", userId);
		if (!fme.isEmpty())
		{
			List<Object> ids = new ArrayList<Object>(fme.size());
			for (FlockMemberEntity f : fme)
			{
				ids.add(f.getFlockId());
			}
			return fDao.loadByIds(ids);
		}
		return new ArrayList<FlockEntity>(0);
	}

	/**
	 * create flock
	 * @param creater
	 * @param flock
	 * @return	memberId
	 */
	public long createFlock(long creater, FlockEntity flock)
	{
		// create flock
		EntityDao<FlockEntity> dao = PersistContext.CTX.getEntityDao(FlockEntity.class);
		flock.setCreateTime(TimeUtil.getYYYYMMDDHHMMSSTime());
		flock.setCreater(creater);
		dao.persist(flock);

		// add creater to be a flock member
		EntityDao<FlockMemberEntity> fDao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);
		FlockMemberEntity fme = new FlockMemberEntity();
		fme.setIsAdmin(1);
		fme.setJoinTime(TimeUtil.getYYYYMMDDHHMMSSTime());
		fme.setUserId(creater);
		fme.setFlockId(flock.getId());
		fDao.persist(fme);
		return fme.getId();
	}

	/**
	 * check whether the user was a member of the flock
	 * @param flockId
	 * @param userId
	 * @return
	 */
	public boolean isFlockMember(long flockId, long userId)
	{
		EntityDao<FlockMemberEntity> dao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);
		List<Condition> conds = new ArrayList<Condition>(2);
		conds.add(new Condition("flockId", ConditionEnum.Equal, flockId));
		conds.add(new Condition("userId", ConditionEnum.Equal, userId));
		List<FlockMemberEntity> list = dao.getListByCond(conds);
		return !list.isEmpty();
	}

	/**
	 * 加入群
	 * @param userId
	 * @param flockId
	 */
	public void joinInFlock(long userId, long flockId)
	{
		EntityDao<FlockMemberEntity> dao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);

		long joinTime = TimeUtil.getYYYYMMDDHHMMSSTime();
		if (!isFlockMember(flockId, userId))
		{
			FlockMemberEntity member = new FlockMemberEntity();
			member.setFlockId(flockId);
			member.setIsAdmin(0);
			member.setJoinTime(joinTime);
			member.setUserId(userId);
			dao.persist(member);
		}
	}
	
	/**
	 * add friend to specify flock
	 * @param operatorId
	 * @param flockId
	 * @param userIds
	 * @return
	 */
	public void addFlockMemberRequest(long operatorId, long flockId, long[] userIds)
	{
		if (this.isAdmin(operatorId, flockId))
		{
			// notify each member
			Message msg = new Message();
			msg.setType(MessageEnum.Message_JoinInFlock);
			msg.setContent(String.valueOf(flockId));
			msg.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
			for (long userId : userIds)
			{
				msg.setReceiverId(userId);
				if (IMServer.CTX.isOnline(userId))
				{
					ms.insertMessage(msg, true);

					Protocol notify = new Protocol(Actions.JoinInFlockHandler);
					notify.receiver = userId;
					notify.setParameter("msg", JSON.toJSONString(msg));
					IMServer.CTX.push(notify);
				}
				else
				{
					ms.insertMessage(msg, false);
				}
			}
		}
	}

	/**
	 * update flock member's card,if not exists ,then create one
	 * @param card
	 */
	public void updateFlockCard(FlockCardEntity card)
	{
		EntityDao<FlockCardEntity> dao = PersistContext.CTX.getEntityDao(FlockCardEntity.class);
		FlockCardEntity fce = this.getFlockCard(card.getUserId(), card.getFlockId());
		if (fce != null)
		{
			card.setId(fce.getId());
			dao.update(card);
		}
		else
		{
			dao.persist(card);
		}
	}

	/**
	 * get user's flock card
	 * @param userId
	 * @param flockId
	 * @return
	 */
	public FlockCardEntity getFlockCard(long userId, long flockId)
	{
		EntityDao<FlockCardEntity> dao = PersistContext.CTX.getEntityDao(FlockCardEntity.class);
		List<Condition> conds = new ArrayList<Condition>(2);
		conds.add(new Condition("flockId", ConditionEnum.Equal, flockId));
		conds.add(new Condition("userId", ConditionEnum.Equal, userId));
		FlockCardEntity fce = dao.loadByCond(conds);
		return fce;
	}

	/**
	 * check whether the user is one of the administrator of specify flock
	 * @param userId
	 * @param flockId
	 * @return
	 */
	public boolean isAdmin(long userId, long flockId)
	{
		FlockEntity flock = this.getFlockInfo(flockId);
		if (flock != null)
		{
			if (flock.getCreater() == userId)
			{
				return true;
			}
			FlockMemberEntity member = this.getMemberOfFlock(userId, flockId);
			if (member != null && member.getIsAdmin() == 1)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * check whether the user is the creater of specify flock
	 * @param userId
	 * @param flockId
	 * @return
	 */
	public boolean isCreater(long userId, long flockId)
	{
		FlockEntity flock = this.getFlockInfo(flockId);
		if (flock != null)
		{
			if (flock.getCreater() == userId)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * grant or take back admin permission
	 * @param flockMemberId
	 * @param isAdmin
	 */
	public void manageFlockAdmin(long operatorId, long flockMemberId, boolean isAdmin)
	{
		EntityDao<FlockMemberEntity> dao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);
		FlockMemberEntity member = dao.loadById(flockMemberId);
		if (member != null)
		{
			if (isAdmin(operatorId, member.getFlockId()))
			{
				dao.update(flockMemberId, new Value("isAdmin", isAdmin ? 1 : 0));

				// notify member
				Message msg = new Message();
				msg.setType(isAdmin ? MessageEnum.Message_GrantFlockAdmin : MessageEnum.Message_TakeBackFlockAdmin);
				msg.setContent(String.valueOf(member.getFlockId()));
				msg.setReceiverId(member.getUserId());
				msg.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());

				if (IMServer.CTX.isOnline(member.getUserId()))
				{
					ms.insertMessage(msg, true);

					Protocol notify = new Protocol(Actions.GrantOrTakeBackFlockAdminHandler);
					notify.receiver = member.getUserId();
					notify.setParameter("msg", JSON.toJSONString(msg));
					IMServer.CTX.push(notify);
				}
				else
				{
					ms.insertMessage(msg, false);
				}
			}
		}
	}

	/**
	 * kick member out from flock
	 * @param operatorId
	 * @param flockMemberId
	 */
	public void kickOutMember(long operatorId, long flockMemberId)
	{
		EntityDao<FlockMemberEntity> dao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);
		FlockMemberEntity member = dao.loadById(flockMemberId);
		if (member != null)
		{
			if (isAdmin(operatorId, member.getFlockId()))
			{
				dao.remove(flockMemberId);

				// notify member
				Message msg = new Message();
				msg.setType(MessageEnum.Message_KickOutFlock);
				msg.setContent(String.valueOf(member.getFlockId()));
				msg.setReceiverId(member.getUserId());
				msg.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());

				if (IMServer.CTX.isOnline(member.getUserId()))
				{
					ms.insertMessage(msg, true);

					Protocol notify = new Protocol(Actions.KickOutFlockHandler);
					notify.receiver = member.getUserId();
					notify.setParameter("msg", JSON.toJSONString(msg));
					IMServer.CTX.push(notify);
				}
				else
				{
					ms.insertMessage(msg, false);
				}
			}
		}
	}

	/**
	 * exit from the specify flock
	 * @param flockId
	 * @param userId
	 */
	public void exitFlock(long flockId, long userId)
	{
		EntityDao<FlockMemberEntity> fDao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);
		List<Condition> conds = new ArrayList<Condition>(2);
		conds.add(new Condition("flockId", ConditionEnum.Equal, flockId));
		conds.add(new Condition("userId", ConditionEnum.Equal, userId));
		fDao.remove(conds);
	}

	/**
	 * dismiss a flock
	 * @param operatorId
	 * @param flockId
	 */
	public void dismissFlock(long operatorId, long flockId)
	{
		if (isCreater(operatorId, flockId))
		{
			// notify members
			Message msg = new Message();
			msg.setContent(String.valueOf(flockId));
			msg.setType(MessageEnum.Message_DismissFlock);
			msg.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
			Protocol notify = new Protocol(Actions.DismissFlockHandler);
			notify.setParameter("msg", JSON.toJSONString(msg));
			IMServer.CTX.pushToFlockMembers(operatorId, flockId, notify);
			
			// delete flock
			EntityDao<FlockEntity> fDao = PersistContext.CTX.getEntityDao(FlockEntity.class);
			fDao.remove(flockId);

			// delete members
			EntityDao<FlockMemberEntity> fmDao = PersistContext.CTX.getEntityDao(FlockMemberEntity.class);
			fmDao.remove("flockId", flockId);
		}
	}
}
