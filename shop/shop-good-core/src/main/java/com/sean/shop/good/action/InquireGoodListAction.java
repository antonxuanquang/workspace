package com.sean.shop.good.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.persist.core.PageData;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.OptionalParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.bean.GoodBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.constant.R;
import com.sean.shop.good.entity.GoodEntity;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.pageNo })
@OptionalParamsConfig({ P.channel, P.categoryId })
@ReturnParamsConfig({ R.goodList, R.totalrecord })
@DescriptConfig("查询商品列表")
public class InquireGoodListAction extends Action
{
	@ResourceConfig
	private GoodBeanImpl goodBean;

	@Override
	public void execute(Session session) throws Exception
	{
		int pageNo = session.getIntParameter(P.pageNo);
		int channel = -1;
		if (session.getParameter(P.channel) != null)
		{
			channel = session.getIntParameter(P.channel);
		}
		long categoryId = -1;
		if (session.getParameter(P.categoryId) != null)
		{
			categoryId = session.getLongParameter(P.categoryId);
		}

		PageData<GoodEntity> data = goodBean.getGoodList(pageNo, 24, channel, categoryId);

		session.setReturnAttribute(R.goodList, data.getDatas());
		session.setReturnAttribute(R.totalrecord, data.getTotalrecords());
		session.success();
	}
}
