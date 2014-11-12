package com.sean.shop.good.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.context.constant.A;
import com.sean.shop.good.bean.ActiveBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.activeId })
@DescriptConfig("删除活动")
public class DeleteActiveAction extends Action
{
	@ResourceConfig
	private ActiveBeanImpl activeBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long activeId = session.getLongParameter(P.activeId);
		activeBean.deleteActive(activeId);
		session.success();
	}
}
