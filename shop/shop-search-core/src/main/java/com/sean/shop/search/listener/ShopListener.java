package com.sean.shop.search.listener;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.sean.common.ioc.BeanFactory;
import com.sean.log.core.LogFactory;
import com.sean.service.annotation.ApplicationContextListenerConfig;
import com.sean.service.core.ApplicationContextListener;
import com.sean.shop.search.bean.SearchBeanImpl;
import com.sean.shop.search.bean.SearchKeyBeanImpl;
import com.sean.shop.search.constant.L;

@ApplicationContextListenerConfig(descr = "监听器", destroyedIndex = 1, initializedIndex = 2)
public class ShopListener extends ApplicationContextListener
{
	private static final Logger logger = LogFactory.getLogger(L.Search);

	@Override
	public void contextPreInitialized(ServletContextEvent ctxe)
	{
		try
		{
			// 初始化搜索引擎
			SearchBeanImpl sbi = BeanFactory.getBean(SearchBeanImpl.class);
			sbi.init();
			SearchKeyBeanImpl skbi = BeanFactory.getBean(SearchKeyBeanImpl.class);
			skbi.init();
		}
		catch (Exception e)
		{
			logger.error("初始化搜索引擎错误:" + e.getMessage(), e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent ctxe)
	{
	}

	@Override
	public void contextDestroyed(ServletContextEvent ctx)
	{
	}
}
