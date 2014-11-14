package com.sean.bigdata.context;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sean.bigdata.constant.A;
import com.sean.bigdata.constant.P;
import com.sean.bigdata.entity.AclEntity;
import com.sean.bigdata.entity.UserEntity;
import com.sean.common.enums.AppServerType;
import com.sean.log.core.LogFactory;
import com.sean.persist.core.Dao;
import com.sean.persist.ext.Condition;
import com.sean.service.annotation.ApplicationContextConfig;
import com.sean.service.core.FrameworkSpi;
import com.sean.service.core.PermissionProvider;
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
		if (permissionId != PermissionProvider.None)
		{
			// 需要管理员权限
			if ((permissionId & A.Admin) != 0)
			{
				long userId = session.getUserId();
				UserEntity user = Dao.loadById(UserEntity.class, userId);
				if (user != null && user.role == 1)
				{
					return true;
				}
			}

			// 需要报表访问权限
			if ((permissionId & A.ReportACL) != 0)
			{
				long userId = session.getUserId();
				long reportId = session.getLongParameter(P.reportId);

				List<Condition> conds = new ArrayList<>();
				conds.add(new Condition("userId", userId));
				conds.add(new Condition("reportId", reportId));
				AclEntity acl = Dao.loadByCond(AclEntity.class, conds);
				if (acl != null)
				{
					return true;
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public String getEncryptKey(String sid)
	{
		return "27819cfe72583a34d13a40bb74154c91";
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
