package com.sean.shop.good.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.OptionalParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.bean.GoodBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.search.api.LogBean;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.goodId })
@OptionalParamsConfig({ P.searchKey })
@DescriptConfig("展示商品")
public class ShowGoodAction extends Action
{
	@ResourceConfig
	private GoodBeanImpl goodBean;
	@ResourceConfig
	private LogBean logBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long goodId = session.getLongParameter(P.goodId);
		String searchKey = session.getParameter(P.searchKey);

		goodBean.incShowTime(goodId, 1);
		logBean.addShowLog(goodId, searchKey);
		session.success();
	}
}
