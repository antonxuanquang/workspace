package com.sean.shop.search.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.search.bean.SearchBeanImpl;
import com.sean.shop.search.bean.SearchKeyBeanImpl;
import com.sean.shop.search.constant.M;
import com.sean.shop.search.constant.P;

@ActionConfig(module = M.class, authenticate = false, password = "97igo")
@MustParamsConfig({ P.password, P.indexType })
@DescriptConfig("建立索引")
public class BuildIndexAction extends Action
{
	@ResourceConfig
	private SearchBeanImpl searchBean;
	@ResourceConfig
	private SearchKeyBeanImpl searchKeyBean;

	@Override
	public void execute(Session session) throws Exception
	{
		int indexType = session.getIntParameter(P.indexType);
		if (indexType == 1)
		{
			searchBean.rebuild();
			session.success();
		}
		else if (indexType == 2)
		{
			searchKeyBean.rebuild();
			session.success();
		}
	}
}
