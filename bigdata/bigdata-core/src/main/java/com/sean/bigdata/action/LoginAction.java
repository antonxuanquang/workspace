package com.sean.bigdata.action;

import com.sean.bigdata.constant.M;
import com.sean.bigdata.constant.P;
import com.sean.bigdata.constant.R;
import com.sean.bigdata.entity.UserEntity;
import com.sean.common.util.SecurityUtil;
import com.sean.persist.core.Dao;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.BusinessException;
import com.sean.service.core.Session;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.password, P.username })
@ReturnParamsConfig({ R.sid, R.encryptKey })
@DescriptConfig("登录")
public class LoginAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		String username = session.getParameter(P.username);
		String password = session.getParameter(P.password);

		UserEntity user = Dao.loadByColumn(UserEntity.class, "username", username);
		if (user != null && user.password.equals(password))
		{
			String sid = SecurityUtil.desEncrypt(String.valueOf(user.userId), "bigdata");
			session.setReturnAttribute(R.sid, sid);
			session.setReturnAttribute(R.encryptKey, SecurityUtil.desEncrypt(sid, "bigdata"));
			session.success();
		}
		throw new BusinessException("密码错误", 1);
	}
}
