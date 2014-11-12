package com.sean.im.friend.service;

import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.UserInfo;
import com.sean.im.context.spi.UserSpi;
import com.sean.im.friend.entity.UserInfoEntity;
import com.sean.im.server.push.IMServer;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Value;
import com.sean.service.annotation.BeanConfig;
import com.sean.service.annotation.ResourceConfig;

@BeanConfig(description = "用户业务接口")
public class UserServiceImpl implements UserService, UserSpi
{
	@ResourceConfig()
	private GroupService gs;

	@Override
	public UserInfo getUserInfoById(long userId)
	{
		UserInfoEntity uie = this.getUserById(userId);
		if (uie != null)
		{
			return uie.toUserInfo();
		}
		return null;
	}

	@Override
	public void initUserStatus()
	{
		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		dao.update(new Condition("id", ConditionEnum.Not_Equal, 0), new Value("status", 0));
	}

	@Override
	public void initUserStatus(long userId)
	{
		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		dao.update(userId, new Value("status", 0));
	}

	/**
	 * get user entity
	 * @param userId
	 * @return
	 */
	public UserInfoEntity getUserById(long userId)
	{
		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		UserInfoEntity uie = dao.loadById(userId);
		return uie;
	}

	/**
	 * update user status
	 * @param userId
	 * @param status
	 */
	public void changeStatus(long userId, int status)
	{
		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		dao.update(userId, new Value("status", status));

		// 通知其他好友状态改变
		Protocol notify = new Protocol(Actions.StatusChangedHandler);
		notify.setParameter("status", status);
		notify.setParameter("userId", userId);
		IMServer.CTX.pushToAllFriend(userId, notify);
	}

	/**
	 * update user translator
	 * @param userId
	 * @param translator
	 */
	public void changeTranslator(long userId, String translator)
	{
		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		dao.update(userId, new Value("translator", translator));
	}

	/**
	 * create new user
	 * @param username
	 * @param password
	 * @param nickname
	 * @return 	 success--1, fail--0
	 */
	public int createUser(String username, String password, String nickname)
	{
		EntityDao<UserInfoEntity> userDao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		UserInfoEntity user = userDao.loadByColumn("username", username);
		if (user == null)
		{
			// 创建用户
			user = new UserInfoEntity();
			user.setAge(20);
			user.setCountry(1);
			user.setDescription("");
			user.setHead(1);
			user.setLanguage(1);
			user.setMail("");
			user.setNickname(nickname);
			user.setPassword(password);
			user.setSex(1);
			user.setSignature("");
			user.setStatus(0);
			user.setTel("");
			user.setUsername(username);
			userDao.persist(user);

			// 创建用户默认分组
			gs.createDefaultGroup(user.getId());
			return 1;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * login
	 * @param username
	 * @param password
	 * @return 	 success--user, fail--null
	 */
	public UserInfoEntity login(String username, String password, int status)
	{
		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		UserInfoEntity user = dao.loadByColumn("username", username);
		if (user != null && user.getPassword().equals(password))
		{
			// 如果帐号已经登录，通知其下线
			Protocol notify = new Protocol(Actions.ExitHandler);
			if (IMServer.CTX.isOnline(user.getId()))
			{
				notify.setParameter("exitType", "2");
				notify.receiver = user.getId();
				IMServer.CTX.push(notify);
				IMServer.CTX.removeClient(notify.receiver);
			}

			// 更改状态
			this.changeStatus(user.getId(), status);
			user.setStatus(status);
			return user;
		}
		return null;
	}

	/**
	 * change user password
	 * @param userId
	 * @param oldPassword
	 * @param password
	 * @return success-1, old password error-0
	 */
	public int changePassword(long userId, String oldPassword, String password)
	{
		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		UserInfoEntity user = dao.loadById(userId);
		if (user != null && user.getPassword().equals(oldPassword))
		{
			dao.update(userId, new Value("password", password));
			return 1;
		}
		return 0;
	}
}
