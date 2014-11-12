package com.sean.service.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.sean.log.core.LogFactory;
import com.sean.service.config.ServiceConfig;
import com.sean.service.entity.ApplicationContextEntity;
import com.sean.service.entity.ApplicationContextListenerEntity;
import com.sean.service.parser.ApplicationContextListenerParser;
import com.sean.service.parser.ApplicationContextParser;

/**
 * 抽象启动类
 * @author sean
 */
public abstract class AbstractLauncher
{
	protected List<ApplicationContextListener> listeners;

	/**
	 * 构建框架
	 * @param ctxe
	 */
	protected ApplicationContextEntity build(ServletContextEvent ctxe) throws Exception
	{
		Logger logger = LogFactory.getFrameworkLogger();
		long current = System.currentTimeMillis();

		// 扫描所有框架内置元素
		ClassCollector cc = new ClassCollector(ServiceConfig.PackgeNames.split(","));
		Map<String, List<Class<?>>> cls = cc.collect();

		// 创建Context
		List<Class<?>> context = cls.get("context");
		// 如果Context未定义
		if (context.size() <= 0)
		{
			throw new RuntimeException("application context not found");
		}
		else
		{
			// 加载Listener
			ApplicationContextListenerParser parser = new ApplicationContextListenerParser();
			List<Class<?>> liscls = cls.get("listeners");
			listeners = new ArrayList<ApplicationContextListener>(liscls.size());
			for (int i = 0; i < liscls.size(); i++)
			{
				ApplicationContextListenerEntity cle = parser.parse(liscls.get(i));
				ApplicationContextListener listener = (ApplicationContextListener) cle.getCls().newInstance();
				listener.init(cle);
				listeners.add(listener);
			}

			// 执行预监听
			this.preInitializedListrner(ctxe);

			// 开始构建上下文
			ApplicationContextEntity entity = new ApplicationContextParser().parse(context.get(0));
			AbstractApplicationContext ctx = this.getBuildContext();
			ctx.build(cls, entity);

			// 执行监听器
			this.initializedListrner(ctxe);

			logger.info("started in " + (System.currentTimeMillis() - current) + " milliseconds");

			StringBuilder sb = new StringBuilder();
			sb.append("\n***********************************************************\n");
			sb.append("*               " + ServiceConfig.ProjectName + " started... \n");
			sb.append("***********************************************************");
			logger.info(sb.toString());

			return entity;
		}
	}

	/**
	 * 获取要构建的上下文
	 * @return
	 */
	protected abstract AbstractApplicationContext getBuildContext();

	/**
	 * 执行预初始化监听器
	 * @param ctxe
	 */
	protected void preInitializedListrner(ServletContextEvent ctxe)
	{
		Collections.sort(this.listeners, new Comparator<ApplicationContextListener>()
		{
			@Override
			public int compare(ApplicationContextListener o1, ApplicationContextListener o2)
			{
				if (o1.getContextListenerEntity().getInitializedIndex() > o2.getContextListenerEntity().getInitializedIndex())
				{
					return 1;
				}
				return 0;
			}
		});
		for (int i = 0; i < listeners.size(); i++)
		{
			listeners.get(i).contextPreInitialized(ctxe);
		}
	}

	/**
	 * 执行初始化监听器
	 * @param ctxe
	 */
	protected void initializedListrner(ServletContextEvent ctxe)
	{
		Collections.sort(this.listeners, new Comparator<ApplicationContextListener>()
		{
			@Override
			public int compare(ApplicationContextListener o1, ApplicationContextListener o2)
			{
				if (o1.getContextListenerEntity().getInitializedIndex() > o2.getContextListenerEntity().getInitializedIndex())
				{
					return 1;
				}
				return 0;
			}
		});
		for (int i = 0; i < listeners.size(); i++)
		{
			listeners.get(i).contextInitialized(ctxe);
		}
	}

	/**
	 * 执行销毁监听器
	 * @param ctxe
	 */
	protected void destroyedListrner(ServletContextEvent ctxe)
	{
		Collections.sort(this.listeners, new Comparator<ApplicationContextListener>()
		{
			@Override
			public int compare(ApplicationContextListener o1, ApplicationContextListener o2)
			{
				if (o1.getContextListenerEntity().getDestroyedIndex() > o2.getContextListenerEntity().getDestroyedIndex())
				{
					return 1;
				}
				return 0;
			}
		});
		for (int i = 0; i < listeners.size(); i++)
		{
			listeners.get(i).contextDestroyed(ctxe);
		}
	}

	public static void showStartInfo()
	{
		Logger logger = LogFactory.getFrameworkLogger();

		StringBuilder sb = new StringBuilder();
		sb.append("\n***********************************************************\n");
		sb.append("*               " + ServiceConfig.ProjectName + " starting...\n");
		sb.append("***********************************************************");
		logger.info(sb.toString());
	}
}
