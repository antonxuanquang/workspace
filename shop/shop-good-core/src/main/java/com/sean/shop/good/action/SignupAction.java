package com.sean.shop.good.action;

import com.sean.common.util.SecurityUtil;
import com.sean.common.util.TimeUtil;
import com.sean.persist.core.Dao;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.BusinessException;
import com.sean.service.core.Session;
import com.sean.shop.context.core.ShopContext;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.constant.R;
import com.sean.shop.good.entity.UserEntity;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.username, P.password })
@ReturnParamsConfig({ R.sid, R.encryptKey })
@DescriptConfig("注册")
public class SignupAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		String username = session.getParameter(P.username);
		String password = session.getParameter(P.password);

		UserEntity user = Dao.loadByColumn(UserEntity.class, "username", username);
		if (user != null)
		{
			throw new BusinessException("用户名已经被注册", 1);
		}

		user = new UserEntity();
		user.username = username;
		user.password = password;
		user.registTime = TimeUtil.getYYYYMMDDHHMMSSTime();
		user.integration = 0;
		Dao.persist(UserEntity.class, user);

		
		String encryptKey = ShopContext.encryptKey;
		String sid = SecurityUtil.desEncrypt(String.valueOf(user.userId), encryptKey);
		
		session.setReturnAttribute(R.sid, sid);
		session.setReturnAttribute(R.encryptKey, encryptKey);
		session.success();
	}
}
