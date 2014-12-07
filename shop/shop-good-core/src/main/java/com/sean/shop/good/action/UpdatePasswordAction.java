package com.sean.shop.good.action;

import com.sean.persist.core.Dao;
import com.sean.persist.ext.Value;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.BusinessException;
import com.sean.service.core.Session;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.entity.UserEntity;

@ActionConfig(module = M.class)
@MustParamsConfig({ P.password, P.oldpassword })
@DescriptConfig("修改密码")
public class UpdatePasswordAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		String oldpassword = session.getParameter(P.oldpassword);
		String password = session.getParameter(P.password);

		UserEntity user = Dao.loadById(UserEntity.class, session.getUserId());
		if (user == null || !user.password.equals(oldpassword))
		{
			throw new BusinessException("旧密码错误", 1);
		}
		long userId = session.getUserId();
		Dao.update(UserEntity.class, userId, new Value("password", password));
		session.success();
	}
}
