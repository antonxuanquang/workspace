package com.sean.shop.search.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.persist.core.PageData;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.context.constant.A;
import com.sean.shop.search.bean.SearchBeanImpl;
import com.sean.shop.search.constant.M;
import com.sean.shop.search.constant.P;
import com.sean.shop.search.constant.R;
import com.sean.shop.search.entity.BuildLogEntity;

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.pageNo })
@ReturnParamsConfig({ R.buildList, R.totalrecord })
@DescriptConfig("查询索引建立列表")
public class InquireBuildLogAction extends Action
{
	@ResourceConfig
	private SearchBeanImpl searchBean;

	@Override
	public void execute(Session session) throws Exception
	{
		int pageNo = session.getIntParameter(P.pageNo);
		PageData<BuildLogEntity> data = searchBean.getBuildList(pageNo, 24);
		session.setReturnAttribute(R.totalrecord, data.getTotalrecords());
		session.setReturnAttribute(R.buildList, data.getDatas());
		session.success();
	}
}
