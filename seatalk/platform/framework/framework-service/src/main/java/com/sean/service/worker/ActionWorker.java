package com.sean.service.worker;

import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.service.spi.FrameworkSpi;

/**
 * 请求处理工作节点
 * @author sean
 */
public class ActionWorker implements Worker
{
	private FrameworkSpi userInterface;

	public ActionWorker(FrameworkSpi userInterface)
	{
		this.userInterface = userInterface;
	}

	@Override
	public void work(Session session, Action action) throws Exception
	{
		userInterface.preAction(session, action.getActionEntity());
		long curr = System.currentTimeMillis();
		action.execute(session);
		long time = System.currentTimeMillis() - curr;
		userInterface.afterAction(session, action.getActionEntity(), time);
	}
}
