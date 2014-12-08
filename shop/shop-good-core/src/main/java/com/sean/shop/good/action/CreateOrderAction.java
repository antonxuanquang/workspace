package com.sean.shop.good.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.bean.OrderBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.entity.OrderEntity;

@ActionConfig(module = M.class)
@MustParamsConfig({ P.goodId, P.tel, P.qq, P.address, P.remark })
@DescriptConfig("创建订单")
public class CreateOrderAction extends Action
{
	@ResourceConfig
	private OrderBeanImpl orderBean;

	@Override
	public void execute(Session session) throws Exception
	{
		OrderEntity order = new OrderEntity();
		session.fillSingleEntity(order);
		orderBean.createOrder(order, session.getUserId(), order.goodId);
		session.success();
	}
}