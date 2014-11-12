package com.sean.bigdata.context;

import org.apache.log4j.Logger;

import com.sean.common.enums.AppServerType;
import com.sean.log.core.LogFactory;
import com.sean.service.annotation.ApplicationContextConfig;
import com.sean.service.core.FrameworkSpi;
import com.sean.service.core.Session;
import com.sean.service.entity.ActionEntity;

@ApplicationContextConfig(urlPattern = "/api/*", appServer = AppServerType.Resin)
public class BDContext extends FrameworkSpi
{
	private static final Logger logger = LogFactory.getLogger(BDContext.class);

	@Override
	public void exceptionHandle(Session session, ActionEntity action, Exception e)
	{
		logger.error("未知错误:" + e.getMessage(), e);
	}

	@Override
	public boolean checkPermission(Session session, int permissionId)
	{
		return true;
	}

	@Override
	public String getEncryptKey(String sid)
	{
		return "bigdata";
	}

	@Override
	public void initUserContext(long userId)
	{
	}

	@Override
	public void destoryUserContext()
	{
	}

	@Override
	public void preAction(Session session, ActionEntity action)
	{
		logger.debug("client start access " + action.getCls().getSimpleName());
	}

	@Override
	public void afterAction(Session session, ActionEntity action, long millSeconds)
	{
		logger.debug("client finish access " + action.getCls().getSimpleName() + " costs " + millSeconds);
	}
}
