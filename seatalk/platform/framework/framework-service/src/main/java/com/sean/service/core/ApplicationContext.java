package com.sean.service.core;

import org.apache.log4j.Logger;

import com.sean.log.core.LogFactory;
import com.sean.service.entity.ApplicationContextEntity;
import com.sean.service.spi.FrameworkSpi;

/**
 * application context
 * @author sean
 */
@SuppressWarnings("unchecked")
public final class ApplicationContext
{
	public static ApplicationContext CTX;

	private BeanContainer beanContainer;
	private FrameworkSpi userInterface;
	private ApplicationContextEntity entity;
	private long launchTime;

	protected static Logger logger = LogFactory.getFrameworkLogger();

	public ApplicationContext(ApplicationContextEntity entity, BeanContainer beanContainer, FrameworkSpi userInterface, long launchTime)
	{
		this.entity = entity;
		this.beanContainer = beanContainer;
		this.userInterface = userInterface;
		this.launchTime = launchTime;
	}

	/**
	 * get Bean
	 * @param beanName
	 * @return
	 */
	public <E> E getBean(Class<E> beanClass)
	{
		return (E) this.beanContainer.getBean(beanClass.getName());
	}

	/**
	 * get framework launch time
	 */
	public long getLaunchTime()
	{
		return this.launchTime;
	}

	/**
	 * encode uid
	 * @param userId
	 * @return
	 */
	public String encodeUid(long userId)
	{
		return this.userInterface.encodeUid(userId);
	}

	/**
	 * get application context entity
	 * @return
	 */
	public ApplicationContextEntity getApplicationContextEntity()
	{
		return entity;
	}
}
