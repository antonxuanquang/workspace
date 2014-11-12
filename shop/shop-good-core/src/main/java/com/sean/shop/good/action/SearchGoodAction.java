package com.sean.shop.good.action;

import java.util.List;

import com.sean.common.ioc.ResourceConfig;
import com.sean.persist.core.Dao;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.OptionalParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.constant.R;
import com.sean.shop.good.entity.GoodEntity;
import com.sean.shop.search.api.LogBean;
import com.sean.shop.search.api.Query;
import com.sean.shop.search.api.SearchBean;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.pageNo, P.ranking })
@OptionalParamsConfig({ P.keyword, P.channel, P.categoryId, P.priceStart, P.priceEnd })
@ReturnParamsConfig({ R.goodList, R.totalrecord })
@DescriptConfig("搜索商品")
public class SearchGoodAction extends Action
{
	@ResourceConfig
	private SearchBean searchBean;
	@ResourceConfig
	private LogBean logBean;

	@Override
	public void execute(Session session) throws Exception
	{
		Query query = new Query();
		query.pageNo = session.getIntParameter(P.pageNo);
		query.ranking = session.getIntParameter(P.ranking);

		if (query.pageNo > 0)
		{
			if (session.getParameter(P.keyword) != null)
			{
				query.keyword = session.getParameter(P.keyword);
			}
			if (session.getParameter(P.channel) != null)
			{
				query.channel = session.getIntParameter(P.channel);
			}
			if (session.getParameter(P.categoryId) != null)
			{
				query.categoryId = session.getLongParameter(P.categoryId);
			}
			if (session.getParameter(P.priceStart) != null)
			{
				query.priceStart = session.getIntParameter(P.priceStart);
			}
			if (session.getParameter(P.priceEnd) != null)
			{
				query.priceEnd = session.getIntParameter(P.priceEnd);
			}

			query.status = 1;

			List<Object> goodIdList = searchBean.search(query);
			session.setReturnAttribute(R.totalrecord, query.totalrecord);
			session.setReturnAttribute(R.goodList, Dao.loadByIds(GoodEntity.class, goodIdList));

			if (query.keyword != null && !query.keyword.isEmpty())
			{
				// 添加搜索日志
				logBean.addSearchLog(query.keyword, query.pageNo, goodIdList.toString());
			}

			session.success();
		}
	}
}
