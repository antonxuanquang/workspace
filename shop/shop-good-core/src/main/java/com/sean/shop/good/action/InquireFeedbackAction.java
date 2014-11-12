package com.sean.shop.good.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.persist.core.PageData;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.bean.GoodBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.constant.R;
import com.sean.shop.good.entity.GoodFeedbackEntity;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.pageNo })
@ReturnParamsConfig({ R.feedbackList, R.totalrecord })
@DescriptConfig("读取商品异常列表")
public class InquireFeedbackAction extends Action
{
	@ResourceConfig
	private GoodBeanImpl goodBean;

	@Override
	public void execute(Session session) throws Exception
	{
		int pageNo = session.getIntParameter(P.pageNo);
		PageData<GoodFeedbackEntity> list = goodBean.getFeedbackList(pageNo, 24);
		session.setReturnAttribute(R.feedbackList, list.getDatas());
		session.setReturnAttribute(R.totalrecord, list.getTotalrecords());
		session.success();
	}
}
