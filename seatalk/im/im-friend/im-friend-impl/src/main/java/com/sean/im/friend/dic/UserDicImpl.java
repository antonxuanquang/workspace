package com.sean.im.friend.dic;

import java.util.Map;

import com.sean.im.account.dic.UserDic;
import com.sean.im.friend.entity.UserInfoEntity;
import com.sean.im.friend.service.UserServiceImpl;
import com.sean.persist.dictionary.Dictionary;
import com.sean.persist.dictionary.DictionaryProviderConfig;
import com.sean.service.core.ApplicationContext;

/**
 * User Dic implemention
 * @author sean
 */
@DictionaryProviderConfig(description = "UserDic implemention")
public class UserDicImpl extends Dictionary implements UserDic
{
	@Override
	public void getDicVal(Object id, Map<String, String> dic)
	{
		UserServiceImpl us = ApplicationContext.CTX.getBean(UserServiceImpl.class);
		UserInfoEntity user = us.getUserById((long) id);
		if (user != null)
		{
			dic.put("username", user.getUsername());
			dic.put("nickname", user.getNickname());
			dic.put("head", String.valueOf(user.getHead()));
			dic.put("signature", user.getSignature());
			dic.put("status", String.valueOf(user.getStatus()));
		}
	}
}
