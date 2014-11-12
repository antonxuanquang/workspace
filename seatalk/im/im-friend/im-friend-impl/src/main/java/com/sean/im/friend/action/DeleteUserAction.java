package com.sean.im.friend.action;

import com.sean.im.friend.entity.FriendEntity;
import com.sean.im.friend.entity.GroupEntity;
import com.sean.im.friend.entity.UserInfoEntity;
import com.sean.im.server.constant.P;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "删除用户", authenticate = false, 
mustParams = { P.userId })
public class DeleteUserAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		long userId = session.getLongParameter(P.userId);

		// 删除用户
		EntityDao<UserInfoEntity> userDao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		userDao.remove(userId);
		
		// 删除用户分组
		EntityDao<GroupEntity> gd = PersistContext.CTX.getEntityDao(GroupEntity.class);
		gd.remove("userId", userId);
		
		// 删除用户好友关系
		EntityDao<FriendEntity> fd = PersistContext.CTX.getEntityDao(FriendEntity.class);
		fd.remove("userId", userId);
		fd.remove("friendId", userId);
		
		session.success();
	}
}
