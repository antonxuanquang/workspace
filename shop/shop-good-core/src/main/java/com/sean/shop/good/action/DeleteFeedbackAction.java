package com.sean.shop.good.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.context.constant.A;
import com.sean.shop.good.bean.GoodBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.feedbackId })
@DescriptConfig("删除商品反馈")
public class DeleteFeedbackAction extends Action
{
	@ResourceConfig
	private GoodBeanImpl goodBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long feedbackId = session.getLongParameter(P.feedbackId);
		goodBean.deleteFeedback(feedbackId);
		session.success();
	}
}
