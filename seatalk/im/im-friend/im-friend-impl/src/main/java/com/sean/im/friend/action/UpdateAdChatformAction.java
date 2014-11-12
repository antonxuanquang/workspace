package com.sean.im.friend.action;

import java.util.ArrayList;
import java.util.List;

import com.sean.im.friend.entity.AdEntity;
import com.sean.im.server.constant.P;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.ext.Value;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "添加分组", authenticate = false,
mustParams = { P.adChatformLink, P.adChatformImgUrl })
public class UpdateAdChatformAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		String adChatformLink = session.getParameter(P.adChatformLink);
		String adChatformImgUrl = session.getParameter(P.adChatformImgUrl);

		EntityDao<AdEntity> dao = PersistContext.CTX.getEntityDao(AdEntity.class);
		List<Value> vals = new ArrayList<>();
		vals.add(new Value("link", adChatformLink));
		vals.add(new Value("imgUrl", adChatformImgUrl));
		dao.update(1L, vals);
		session.success();
	}
}
