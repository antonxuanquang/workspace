package com.sean.bigdata.action;

import com.sean.bigdata.bean.ReportBean;
import com.sean.bigdata.constant.M;
import com.sean.bigdata.constant.P;
import com.sean.bigdata.constant.R;
import com.sean.bigdata.entity.ReportEntity;
import com.sean.common.ioc.ResourceConfig;
import com.sean.persist.core.PageData;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.pageNo })
@ReturnParamsConfig({ R.reportList, R.totalrecord })
@DescriptConfig("查询报表列表")
public class InquireReportListAction extends Action
{
	@ResourceConfig
	private ReportBean reportBean;

	@Override
	public void execute(Session session) throws Exception
	{
		int pageNo = session.getIntParameter(P.pageNo);
		PageData<ReportEntity> data = reportBean.getReportList(1, pageNo, 25);

		session.setReturnAttribute(R.reportList, data.getDatas());
		session.setReturnAttribute(R.totalrecord, data.getTotalrecords());
		session.success();
	}
}
