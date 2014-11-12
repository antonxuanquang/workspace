package com.sean.im.friend.action;

import java.util.List;

import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.entity.MessageEntity;
import com.sean.im.server.constant.P;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "读取聊天记录",authenticate = false,
mustParams = {P.loginerId, P.userId, P.pageNo},
returnParams = {RetParams.msgs})
public class InquireChatRecordAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long userId = session.getLongParameter(P.userId);
		int pageNo = session.getIntParameter(P.pageNo);

		EntityDao<MessageEntity> dao = PersistContext.CTX.getEntityDao(MessageEntity.class);
		int start = (pageNo - 1) * 20;

		String sql = "select * from t_message where ownerId=${userId} and type=1 and ((senderId=${userId} and receiverId=${friendId}) or (senderId=${friendId} and receiverId=${userId})) order by sendTime limit ${start},20";
		sql = sql.replace("${userId}", loginerId + "");
		sql = sql.replace("${friendId}", userId + "");
		sql = sql.replace("${start}", start + "");
		
		List<MessageEntity> msgs = dao.executeEntityList(sql);
		session.setReturnAttribute(RetParams.msgs, msgs);
		session.success();
	}
}
