package com.sean.service.core;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.sean.config.core.Config;
import com.sean.log.core.LogFactory;
import com.sean.service.config.ServiceConfig;
import com.sean.service.entity.ApplicationContextEntity;

/**
 * Servlet容器监听器，应用程序的执行入口
 * @author Sean
 */
public final class ServletContextListener extends AbstractLauncher implements javax.servlet.ServletContextListener
{
	private final static Logger logger = LogFactory.getFrameworkLogger();
	
	@Override
	public void contextInitialized(ServletContextEvent ctxe)
	{	
		try
		{
			// 读取配置文件
			String targetUser = ctxe.getServletContext().getInitParameter("target.user");
			Config.readConf(targetUser);

			// 构建框架
			ApplicationContextEntity context = this.build(ctxe);
			
			// 动态注册过滤器
			ServletContext sc = ctxe.getServletContext();
			Class<?> dispatcher = null;
			if (ServiceConfig.RunningMode_Pseudo)
			{
				dispatcher = ActionPseudoDispatcher.class;
			}
			else
			{
				dispatcher = ActionDispatcher.class;
			}
			FilterRegistration fr = sc.addFilter("DispatcherFilter", dispatcher.getName());
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, context.getUrlPattern());
			logger.info("Regist servlet filter " + dispatcher.getName() + " successfully,it's urlPattern was " + context.getUrlPattern());	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent ctxe)
	{
		// 执行销毁监听器
		this.destroyedListrner(ctxe);
	}

	@Override
	protected AbstractApplicationContext getBuildContext()
	{
		return new StandardApplicationContext();
	}
}
