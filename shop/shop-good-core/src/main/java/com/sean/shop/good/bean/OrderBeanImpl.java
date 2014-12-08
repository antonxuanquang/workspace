package com.sean.shop.good.bean;

import java.util.List;

import com.sean.common.ioc.BeanConfig;
import com.sean.common.util.TimeUtil;
import com.sean.persist.core.Dao;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Order;
import com.sean.persist.ext.Value;
import com.sean.service.core.BusinessException;
import com.sean.shop.good.entity.GoodEntity;
import com.sean.shop.good.entity.OrderEntity;
import com.sean.shop.good.entity.UserEntity;

@BeanConfig("商品对象")
public class OrderBeanImpl
{
	public List<OrderEntity> getOrder(long userId)
	{
		return Dao.getListByColumn(OrderEntity.class, "userId", userId, new Order("orderId", OrderEnum.Desc));
	}

	public long createOrder(OrderEntity order, long userId, long goodId) throws BusinessException
	{
		GoodEntity good = Dao.loadById(GoodEntity.class, goodId);
		UserEntity user = Dao.loadById(UserEntity.class, userId);
		if (good != null && user != null)
		{
			order.createTime = TimeUtil.getYYYYMMDDHHMMSSTime();
			order.goodId = goodId;
			order.userId = userId;
			order.status = 1;

			order.integration = (int) (good.price * 100 * 2);

			if (user.integration >= order.integration)
			{
				// 扣积分
				user.integration -= order.integration;
				Dao.update(UserEntity.class, userId, new Value("integration", user.integration));

				// 下订单
				Dao.persist(OrderEntity.class, order);
				return order.orderId;
			}
			else
			{
				throw new BusinessException("您的积分不足", 1);
			}
		}
		return 0;
	}
}
