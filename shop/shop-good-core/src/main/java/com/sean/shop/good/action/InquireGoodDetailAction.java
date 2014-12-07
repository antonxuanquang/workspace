package com.sean.shop.good.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.bean.GoodBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.constant.R;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.goodId })
@ReturnParamsConfig({ R.goodDetail })
@DescriptConfig("查询商品")
public class InquireGoodDetailAction extends Action
{
	@ResourceConfig
	private GoodBeanImpl goodBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long goodId = session.getLongParameter(P.goodId);
		session.setReturnAttribute(R.goodDetail, goodBean.getGood(goodId));
		session.success();
	}
}
