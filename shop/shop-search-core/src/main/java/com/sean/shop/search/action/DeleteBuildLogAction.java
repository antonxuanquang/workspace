package com.sean.shop.search.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.context.constant.A;
import com.sean.shop.search.bean.SearchBeanImpl;
import com.sean.shop.search.constant.M;
import com.sean.shop.search.constant.P;

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.buildId })
@DescriptConfig("删除索引建立日志")
public class DeleteBuildLogAction extends Action
{
	@ResourceConfig
	private SearchBeanImpl searchBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long buildId = session.getLongParameter(P.buildId);
		searchBean.deleteBuild(buildId);
		session.success();
	}
}
