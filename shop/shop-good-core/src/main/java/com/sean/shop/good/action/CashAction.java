package com.sean.shop.good.action;

import com.sean.common.util.TimeUtil;
import com.sean.persist.core.Dao;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.entity.CashEntity;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.zfbUsername })
@DescriptConfig("提现")
public class CashAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		CashEntity ce = new CashEntity();
		ce.zfbUsername = session.getParameter(P.zfbUsername);
		ce.createTime = TimeUtil.getYYYYMMDDHHMMSSTime();
		Dao.persist(CashEntity.class, ce);
		session.success();
	}
}