package com.sean.im.friend.action;

import java.util.ArrayList;
import java.util.List;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.entity.UserInfoEntity;
import com.sean.im.server.constant.P;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PageData;
import com.sean.persist.core.PersistContext;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.enums.LogicEnum;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "搜索用户", authenticate = false,
mustParams = { P.pageNo },
optionalParams = { Params.username, Params.nickname, Params.country, P.sex, P.age },
returnParams = { RetParams.searchUsers })
public class SearchUserAction extends Action
{
	@Override
	public void execute(Session session) throws Exception
	{
		int pageNo = session.getIntParameter(P.pageNo);
		EntityDao<UserInfoEntity> dao = PersistContext.CTX.getEntityDao(UserInfoEntity.class);

		String username = session.getParameter("username");
		// 精确搜索
		if (username != null)
		{
			UserInfoEntity userinfo = dao.loadByColumn("username", username);
			List<UserInfoEntity> users = new ArrayList<UserInfoEntity>();
			users.add(userinfo);
			session.setReturnAttribute(RetParams.searchUsers, users);
		}
		// 模糊搜索
		else
		{
			List<Condition> conds = new ArrayList<Condition>(4);
			String nickname = session.getParameter("nickname");
			String country = session.getParameter("country");
			String sex = session.getParameter("sex");
			String age = session.getParameter("age");
			conds.add(new Condition("1", ConditionEnum.Equal, "1"));

			if (nickname != null)
			{
				conds.add(new Condition("nickname", ConditionEnum.Like, nickname + "%"));
			}
			if (country != null && !country.toString().equals("0"))
			{
				conds.add(new Condition(LogicEnum.And, "country", ConditionEnum.Equal, country.toString()));
			}
			if (sex != null && !sex.toString().equals("0"))
			{
				conds.add(new Condition(LogicEnum.And, "sex", ConditionEnum.Equal, sex.toString()));
			}
			if (age != null && !age.toString().equals("0"))
			{
				int[] ages = this.getAgeRange(Integer.parseInt(age.toString()));
				conds.add(new Condition(LogicEnum.And, "age", ConditionEnum.Greater_Equal, ages[0] + ""));
				conds.add(new Condition(LogicEnum.And, "age", ConditionEnum.Less, ages[1] + ""));
			}

			if (conds.size() == 0)
			{
				session.setReturnAttribute(RetParams.searchUsers, new ArrayList<UserInfoEntity>(0));
			}
			else
			{
				List<Order> orders = new ArrayList<Order>(1);
				orders.add(new Order("id", OrderEnum.Asc));
				PageData<UserInfoEntity> pd = dao.getListByPage(conds, orders, pageNo, 10, 1);
				session.setReturnAttribute(RetParams.searchUsers, pd.getDatas());
			}
		}
		session.success();
	}

	private int[] getAgeRange(int age)
	{
		switch (age)
		{
		case 1:
			return new int[] { 8, 18 };
		case 2:
			return new int[] { 19, 30 };
		case 3:
			return new int[] { 31, 50 };
		case 4:
			return new int[] { 50, 80 };
		default:
			return new int[] { 0, 0 };
		}
	}
}
