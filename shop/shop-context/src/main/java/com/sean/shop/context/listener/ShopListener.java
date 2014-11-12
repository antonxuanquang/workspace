package com.sean.shop.context.listener;

import javax.servlet.ServletContextEvent;

import com.sean.persist.core.PersistLaucher;
import com.sean.service.annotation.ApplicationContextListenerConfig;
import com.sean.service.core.ApplicationContextListener;

@ApplicationContextListenerConfig(descr = "监听器", destroyedIndex = 1, initializedIndex = 1)
public class ShopListener extends ApplicationContextListener
{
	@Override
	public void contextPreInitialized(ServletContextEvent ctxe)
	{
		// 启动持久层框架
		PersistLaucher.getInstance().launch(new String[] { "com.sean.shop" });
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
