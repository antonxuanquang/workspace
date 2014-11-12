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

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.goodId })
@DescriptConfig("修改商品状态商品")
public class UpdateGoodStatusAction extends Action
{
	@ResourceConfig
	private GoodBeanImpl goodBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long goodId = session.getLongParameter(P.goodId);

		goodBean.updateGoodStatus(goodId);
		session.success();
	}
}
