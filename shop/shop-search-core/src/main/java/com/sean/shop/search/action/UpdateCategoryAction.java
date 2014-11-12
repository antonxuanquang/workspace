package com.sean.shop.search.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.search.bean.SearchBeanImpl;
import com.sean.shop.search.constant.M;
import com.sean.shop.search.constant.P;

@ActionConfig(module = M.class, authenticate = false, password = "97igo")
@MustParamsConfig({ P.password, P.forceUpdate })
@DescriptConfig("计算商品分类")
public class UpdateCategoryAction extends Action
{
	@ResourceConfig
	private SearchBeanImpl searchBean;

	@Override
	public void execute(Session session) throws Exception
	{
		int forceUpdate = session.getIntParameter(P.forceUpdate);
		searchBean.category(forceUpdate == 1, false);
	}
}
