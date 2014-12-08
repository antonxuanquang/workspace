package com.sean.shop.good.action;

import java.util.List;

import com.sean.common.ioc.ResourceConfig;
import com.sean.persist.core.Dao;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.bean.OrderBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.R;
import com.sean.shop.good.entity.OrderEntity;
import com.sean.shop.good.entity.UserEntity;

@ActionConfig(module = M.class)
@ReturnParamsConfig({ R.userinfo, R.orderList })
@DescriptConfig("查询用户信息")
public class InquireProfileAction extends Action
{
	@ResourceConfig
	private OrderBeanImpl orderBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long userId = session.getUserId();
		UserEntity user = Dao.loadById(UserEntity.class, userId);
		List<OrderEntity> orderList = orderBean.getOrder(userId);

		session.setReturnAttribute(R.userinfo, user);
		session.setReturnAttribute(R.orderList, orderList);
		session.success();
	}
}
