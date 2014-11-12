package com.sean.shop.context.core;

import org.apache.log4j.Logger;

import com.sean.common.enums.AppServerType;
import com.sean.log.core.LogFactory;
import com.sean.service.annotation.ApplicationContextConfig;
import com.sean.service.core.FrameworkSpi;
import com.sean.service.core.Session;
import com.sean.service.entity.ActionEntity;
import com.sean.shop.context.constant.A;
import com.sean.shop.context.constant.L;

@ApplicationContextConfig(urlPattern = "/api/*", appServer = AppServerType.Resin)
public class ShopContext extends FrameworkSpi
{
	private static final Logger logger = LogFactory.getLogger(L.Context);

	@Override
	public void exceptionHandle(Session session, ActionEntity action, Exception e)
	{
		logger.error("未知错误:" + e.getMessage(), e);
	}

	@Override
	public boolean checkPermission(Session session, int permissionId)
	{
		// 管理员权限
		if (permissionId == A.Admin)
		{
			return session.getUserId() == 1;
		}
		return true;
	}

	@Override
	public String getEncryptKey(String sid)
	{
		return "97igo";
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
