package com.sean.shop.good.action;

import com.sean.common.util.SecurityUtil;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.BusinessException;
import com.sean.service.core.Session;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.P;
import com.sean.shop.good.constant.R;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.password })
@ReturnParamsConfig({ R.sid, R.encryptKey })
@DescriptConfig("登录管理后台")
public class LoginConsoleAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		String password = session.getParameter(P.password);

		if (password.equals("97igoxuxu"))
		{
			String sid = SecurityUtil.desEncrypt(String.valueOf(1), "97igo");
			session.setReturnAttribute(R.sid, sid);
			session.setReturnAttribute(R.encryptKey, "97igo");
			session.success();
		}
		else
		{
			throw new BusinessException("密码错误", 1);
		}
	}
}
