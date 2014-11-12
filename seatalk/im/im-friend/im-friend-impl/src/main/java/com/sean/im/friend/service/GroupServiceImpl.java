package com.sean.im.friend.service;

import java.util.List;

import com.sean.im.friend.entity.GroupEntity;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.ext.Value;
import com.sean.service.annotation.BeanConfig;

@BeanConfig(description = "group interface")
public class GroupServiceImpl implements GroupService
{
	@Override
	public long createDefaultGroup(long userId)
	{
		EntityDao<GroupEntity> dao = PersistContext.CTX.getEntityDao(GroupEntity.class);
		GroupEntity group = new GroupEntity();
		group.setIsDefault(1);
		group.setName("我的好友");
		group.setUserId(userId);
		dao.persist(group);
		return group.getId();
	}

	/**
	 * add group
	 * @param userId
	 * @param groupName
	 * @return
	 */
	public GroupEntity addGroup(long userId, String groupName)
	{
		EntityDao<GroupEntity> dao = PersistContext.CTX.getEntityDao(GroupEntity.class);
		GroupEntity ge = new GroupEntity();
		ge.setName(groupName);
		ge.setUserId(userId);
		ge.setIsDefault(0);
		dao.persist(ge);
		return ge;
	}

	/**
	 * delete group
	 * @param groupId
	 */
	public void deleteGroup(long groupId)
	{
		EntityDao<GroupEntity> dao = PersistContext.CTX.getEntityDao(GroupEntity.class);
		dao.remove(groupId);
	}

	/**
	 * get group list of user
	 * @param userId
	 * @return
	 */
	public List<GroupEntity> getGroupList(long userId)
	{
		EntityDao<GroupEntity> gDao = PersistContext.CTX.getEntityDao(GroupEntity.class);
		List<GroupEntity> ge = gDao.getListByColumn("userId", userId);
		return ge;
	}

	/**
	 * rename group name
	 * @param groupId
	 * @param groupName
	 */
	public void renameGroup(long groupId, String groupName)
	{
		EntityDao<GroupEntity> dao = PersistContext.CTX.getEntityDao(GroupEntity.class);
		dao.update(groupId, new Value("name", groupName));
	}
}
