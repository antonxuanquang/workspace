package com.sean.shop.good.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.context.constant.A;
import com.sean.shop.good.bean.ActiveBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.constant.R;
import com.sean.shop.good.entity.ActiveEntity;

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.activeName, P.startDate, P.endDate, P.activeChannel, P.activeUrl, P.imageUrl })
@ReturnParamsConfig({ R.activeId })
@DescriptConfig("添加新活动")
public class AddActiveAction extends Action
{
	@ResourceConfig
	private ActiveBeanImpl activeBean;

	@Override
	public void execute(Session session) throws Exception
	{
		ActiveEntity active = new ActiveEntity();
		session.fillSingleEntity(active);
		activeBean.addActive(active);

		session.setReturnAttribute(R.activeId, active.activeId);
		session.success();
	}
}