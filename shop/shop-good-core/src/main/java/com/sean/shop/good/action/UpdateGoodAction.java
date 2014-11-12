package com.sean.shop.good.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.context.constant.A;
import com.sean.shop.good.bean.GoodBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.entity.GoodEntity;

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.goodId, P.goodName, P.price, P.imageUrl, P.goodUrl, P.channel, P.keyword, P.boost, P.saleCount })
@DescriptConfig("修改商品")
public class UpdateGoodAction extends Action
{
	@ResourceConfig
	private GoodBeanImpl goodBean;

	@Override
	public void execute(Session session) throws Exception
	{
		GoodEntity good = new GoodEntity();
		session.fillSingleEntity(good);

		goodBean.updateGood(good);
		session.success();
	}
}
