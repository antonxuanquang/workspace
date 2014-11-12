package com.sean.shop.search.action;

import java.util.List;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.search.api.SearchKeyBean;
import com.sean.shop.search.constant.M;
import com.sean.shop.search.constant.P;
import com.sean.shop.search.constant.R;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.keyword })
@ReturnParamsConfig({ R.searchKeys })
@DescriptConfig("匹配关键字自动补全")
public class SearchKeyAction extends Action
{
	@ResourceConfig
	private SearchKeyBean searchKeyBean;

	@Override
	public void execute(Session session) throws Exception
	{
		String keyword = session.getParameter(P.keyword);
		List<String> keys = searchKeyBean.search(keyword);
		StringBuilder sb = new StringBuilder();
		for (String it : keys)
		{
			sb.append(it).append(';');
		}
		session.setReturnAttribute(R.searchKeys, sb.toString());
		session.success();
	}
}
