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
import com.sean.shop.search.api.Query;
import com.sean.shop.search.api.SearchBean;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.pageNo })
@OptionalParamsConfig({ P.keyword, P.channel, P.categoryId, P.priceStart, P.priceEnd, P.status })
@ReturnParamsConfig({ R.goodList4Console, R.totalrecord })
@DescriptConfig("搜索商品")
public class SearchGood4ConsoleAction extends Action
{
	@ResourceConfig
	private SearchBean searchBean;

	@Override
	public void execute(Session session) throws Exception
	{
		Query query = new Query();
		query.keyword = session.getParameter(P.keyword);
		query.pageNo = session.getIntParameter(P.pageNo);

		if (query.pageNo > 0)
		{
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
			if (session.getParameter(P.status) != null)
			{
				query.status = session.getIntParameter(P.status);
			}

			List<Object> goodIdList = searchBean.search(query);
			session.setReturnAttribute(R.totalrecord, query.totalrecord);
			session.setReturnAttribute(R.goodList4Console, Dao.loadByIds(GoodEntity.class, goodIdList));
			session.success();
		}
	}
}
