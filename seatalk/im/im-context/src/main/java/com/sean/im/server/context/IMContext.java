package com.sean.im.server.context;

import java.util.Map;

import org.apache.log4j.Logger;

import com.sean.commom.enums.AppServerType;
import com.sean.im.commom.constant.Loggers;
import com.sean.log.core.LogFactory;
import com.sean.service.annotation.ApplicationContextConfig;
import com.sean.service.core.Session;
import com.sean.service.entity.ActionEntity;
import com.sean.service.spi.FrameworkSpi;

@ApplicationContextConfig(urlPattern = "/action/*", appServer = AppServerType.Resin)
public class IMContext extends FrameworkSpi
{
	private static final Logger logger = LogFactory.getLogger(Loggers.IM);

	@Override
	public void exceptionHandle(Session session, ActionEntity action, Exception e)
	{
		e.printStackTrace();
		logger.error(e.getMessage(), e);
	}

	@Override
	public String encodeUid(long userId)
	{
		return null;
	}

	@Override
	public Map<String, String> decodeUid(String uid)
	{
		return null;
	}

	@Override
	public boolean checkPermission(Session session, int permissionId)
	{
		return true;
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
