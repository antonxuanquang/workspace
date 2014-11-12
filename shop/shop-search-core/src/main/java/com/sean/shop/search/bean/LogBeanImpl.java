package com.sean.shop.search.bean;

import com.sean.common.ioc.BeanConfig;
import com.sean.common.util.TimeUtil;
import com.sean.persist.core.Dao;
import com.sean.shop.search.api.LogBean;
import com.sean.shop.search.entity.SearchLogEntity;
import com.sean.shop.search.entity.ShowLogEntity;

@BeanConfig("日志收集对象")
public class LogBeanImpl implements LogBean
{
	@Override
	public void addShowLog(long goodId, String searchKey)
	{
		ShowLogEntity log = new ShowLogEntity();
		log.goodId = goodId;
		log.searchKey = searchKey;
		log.createTime = TimeUtil.getYYYYMMDDHHMMSSTime();
		Dao.persist(ShowLogEntity.class, log);
	}

	@Override
	public void addSearchLog(String keyword, int pageNo, String searchResult)
	{
		SearchLogEntity log = new SearchLogEntity();
		log.searchKey = keyword;
		log.pageNo = pageNo;
		log.searchResult = searchResult;
		log.createTime = TimeUtil.getYYYYMMDDHHMMSSTime();
		Dao.persist(SearchLogEntity.class, log);
	}
}
