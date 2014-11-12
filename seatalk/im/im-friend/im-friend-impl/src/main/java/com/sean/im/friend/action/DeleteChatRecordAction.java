package com.sean.im.friend.action;

import java.util.ArrayList;
import java.util.List;

import com.sean.im.friend.entity.MessageEntity;
import com.sean.im.server.constant.P;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.ext.Condition;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "删除聊天记录", authenticate = false, mustParams = { P.loginerId, P.userId })
public class DeleteChatRecordAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long userId = session.getLongParameter(P.userId);

		EntityDao<MessageEntity> dao = PersistContext.CTX.getEntityDao(MessageEntity.class);
		List<Condition> conds = new ArrayList<>(2);
		conds.add(new Condition("ownerId", ConditionEnum.Equal, loginerId));
		conds.add(new Condition("senderId", ConditionEnum.Equal, loginerId));
		conds.add(new Condition("receiverId", ConditionEnum.Equal, userId));
		dao.remove(conds);

		conds.clear();
		conds.add(new Condition("ownerId", ConditionEnum.Equal, loginerId));
		conds.add(new Condition("senderId", ConditionEnum.Equal, userId));
		conds.add(new Condition("receiverId", ConditionEnum.Equal, loginerId));
		dao.remove(conds);
		session.success();
	}
}
