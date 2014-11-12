package com.sean.im.friend.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.sean.im.friend.constant.Params;
import com.sean.im.friend.constant.RetParams;
import com.sean.im.friend.entity.AdEntity;
import com.sean.im.friend.entity.IconEntity;
import com.sean.im.friend.entity.UserInfoEntity;
import com.sean.im.friend.service.UserServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.im.server.push.IMServer;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.ext.Condition;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@SuppressWarnings("unchecked")
@ActionConfig(description = "登录", authenticate = false, mustParams = { Params.username, Params.password, Params.status, P.version }, returnParams = {
		RetParams.loginrs, RetParams.userbrief, RetParams.adChatform, RetParams.icons, RetParams.country, RetParams.lan, RetParams.age,
		RetParams.gender })
public class LoginAction extends Action
{
	@ResourceConfig
	private UserServiceImpl usi;

	private static Map<String, String> country = new HashMap<>();
	private static Map<String, String> lan = new HashMap<>();
	private static Map<String, String> gender = new HashMap<>();
	private static Map<String, String> age = new HashMap<>();

	static
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(LoginAction.class.getResourceAsStream("/dic.xml"));

			Element root = doc.getRootElement();

			// 国家
			Element elCountry = root.element("country");
			List<Element> items = elCountry.elements("item");
			for (Element it : items)
			{
				country.put(it.attributeValue("id"), it.getStringValue());
			}

			// 语言
			Element elLan = root.element("language");
			items = elLan.elements("item");
			for (Element it : items)
			{
				lan.put(it.attributeValue("id"), it.getStringValue());
			}

			// 性别
			Element elGender = root.element("gender");
			items = elGender.elements("item");
			for (Element it : items)
			{
				gender.put(it.attributeValue("id"), it.getStringValue());
			}

			// 年龄
			Element elAge = root.element("age");
			items = elAge.elements("item");
			for (Element it : items)
			{
				age.put(it.getStringValue(), it.getStringValue());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void execute(Session session) throws Exception
	{
		String version = session.getParameter(P.version);
		String username = session.getParameter(Params.username);
		String password = session.getParameter(Params.password);
		int status = session.getIntParameter(Params.status);

		// 验证版本
		if (!version.equals(IMServer.version))
		{
			session.setReturnAttribute(RetParams.loginrs, 2);
			return;
		}

		UserInfoEntity user = usi.login(username, password, status);
		if (user != null && user.getPassword().equals(password))
		{
			session.setReturnAttribute(RetParams.loginrs, 1);
			session.setReturnAttribute(RetParams.userbrief, user);
		}
		else
		{
			session.setReturnAttribute(RetParams.loginrs, 0);
		}

		EntityDao<AdEntity> dao = PersistContext.CTX.getEntityDao(AdEntity.class);
		AdEntity ad = dao.loadById(1L);
		session.setReturnAttribute(RetParams.adChatform, JSON.toJSONString(ad.getValues()));

		EntityDao<IconEntity> idao = PersistContext.CTX.getEntityDao(IconEntity.class);
		List<IconEntity> icons = idao.getListByCond(new Condition("visible", ConditionEnum.Equal, 1));

		session.setReturnAttribute(RetParams.icons, icons);

		session.setReturnAttribute(RetParams.country, JSON.toJSONString(country));
		session.setReturnAttribute(RetParams.lan, JSON.toJSONString(lan));
		session.setReturnAttribute(RetParams.gender, JSON.toJSONString(gender));
		session.setReturnAttribute(RetParams.age, JSON.toJSONString(age));

		session.success();
	}
}
