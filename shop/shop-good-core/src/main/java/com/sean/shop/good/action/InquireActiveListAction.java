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
import com.sean.shop.good.bean.ActiveBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.constant.R;
import com.sean.shop.good.entity.ActiveEntity;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.pageNo, P.activing })
@OptionalParamsConfig({ P.activeChannel })
@ReturnParamsConfig({ R.activeList, R.totalrecord })
@DescriptConfig("查询商品列表")
public class InquireActiveListAction extends Action
{
	@ResourceConfig
	private ActiveBeanImpl activeBean;

	@Override
	public void execute(Session session) throws Exception
	{
		int pageNo = session.getIntParameter(P.pageNo);
		int activeChannel = -1;
		if (session.getParameter(P.activeChannel) != null)
		{
			activeChannel = session.getIntParameter(P.activeChannel);
		}

		boolean active = session.getIntParameter(P.activing) == 1 ? true : false;
		PageData<ActiveEntity> data = activeBean.getActiveList(pageNo, 24, activeChannel, active);

		session.setReturnAttribute(R.activeList, data.getDatas());
		session.setReturnAttribute(R.totalrecord, data.getTotalrecords());
		session.success();
	}
}
