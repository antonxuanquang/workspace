package com.sean.im.friend.action;

import java.util.ArrayList;
import java.util.List;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.entity.UserInfoEntity;
import com.sean.im.server.constant.P;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.ext.Value;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "修改信息", authenticate = false, 
mustParams = { P.loginerId }, 
optionalParams = { Params.nickname, P.signature, Params.country, P.sex, P.age, P.tel, P.mail, Params.language, P.description, Params.head })
public class UpdateInfoAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);

		List<Value> vals = new ArrayList<Value>(10);
		vals.add(new Value("nickname", session.getParameter("nickname")));
		vals.add(new Value("signature", session.getParameter("signature")));
		vals.add(new Value("country", session.getIntParameter("country")));
		vals.add(new Value("sex", session.getIntParameter("sex")));
		vals.add(new Value("age", session.getIntParameter("age")));
		vals.add(new Value("tel", session.getParameter("tel")));
		vals.add(new Value("mail", session.getParameter("mail")));
		vals.add(new Value("language", session.getIntParameter("language")));
		vals.add(new Value("description", session.getParameter("description")));
		if (session.getParameter("head") != null)
		{
			vals.add(new Value("head", session.getIntParameter("head")));
		}

		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);
		dao.update(loginerId, vals);
		session.success();
	}
}
