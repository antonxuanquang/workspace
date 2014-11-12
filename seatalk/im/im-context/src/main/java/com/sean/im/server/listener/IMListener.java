package com.sean.im.server.listener;

import javax.servlet.ServletContextEvent;

import com.sean.im.context.spi.UserSpi;
import com.sean.im.server.push.IMServer;
import com.sean.persist.core.PersistLaucher;
import com.sean.service.annotation.ApplicationContextListenerConfig;
import com.sean.service.core.ApplicationContext;
import com.sean.service.core.ApplicationContextListener;

@ApplicationContextListenerConfig(description = "IM监听器", destroyedIndex = 1, initializedIndex = 1)
public class IMListener extends ApplicationContextListener
{
	@Override
	public void contextPreInitialized(ServletContextEvent ctxe)
	{
		PersistLaucher.getInstance().launch();
	}

	@Override
	public void contextInitialized(ServletContextEvent ctxe)
	{	
		UserSpi userSpi = ApplicationContext.CTX.getBean(UserSpi.class);
		userSpi.initUserStatus();
		
		// 启动推送服务
		IMServer server = new IMServer();
		IMServer.CTX = server;
		IMServer.CTX.start(9999);
	}

	@Override
	public void contextDestroyed(ServletContextEvent ctx)
	{	
	}
}
