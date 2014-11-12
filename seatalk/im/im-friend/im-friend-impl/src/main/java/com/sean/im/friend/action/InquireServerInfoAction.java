package com.sean.im.friend.action;

import java.util.ArrayList;
import java.util.List;

import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.entity.UserInfoEntity;
import com.sean.im.server.constant.P;
import com.sean.im.server.constant.R;
import com.sean.im.server.push.IMServer;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PageData;
import com.sean.persist.core.PersistContext;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "查看服务器在线用户", authenticate = false,
mustParams = {P.pageNo},
returnParams = {RetParams.userlist,R.totalrecords, RetParams.onlineCount})
public class InquireServerInfoAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		int pageNo = session.getIntParameter(P.pageNo);

		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);

		List<Condition> conds = new ArrayList<Condition>(1);
		conds.add(new Condition("1", ConditionEnum.Equal, "1"));
		PageData<UserInfoEntity> users = dao.getListByPage(conds, new Order("status", OrderEnum.Desc), pageNo, 20, -1);

		session.setReturnAttribute(RetParams.onlineCount, IMServer.CTX.getClientCount());
		session.setReturnAttribute(RetParams.userlist, users.getDatas());
		session.setReturnAttribute(R.totalrecords, users.getTotalrecords());
		session.success();
	}
}
