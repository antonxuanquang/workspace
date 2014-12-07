package com.sean.shop.good.action;

import com.sean.persist.core.Dao;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.R;
import com.sean.shop.good.entity.UserEntity;

@ActionConfig(module = M.class)
@ReturnParamsConfig({ R.userinfo })
@DescriptConfig("查询用户信息")
public class InquireProfileAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		long userId = session.getUserId();
		UserEntity user = Dao.loadById(UserEntity.class, userId);
		session.setReturnAttribute(R.userinfo, user);
		session.success();
	}
}
