package com.sean.bigdata.dic;

import java.util.Map;

import com.sean.bigdata.entity.UserEntity;
import com.sean.persist.core.Dao;
import com.sean.persist.dictionary.Dictionary;
import com.sean.persist.dictionary.DictionaryProviderConfig;

@DictionaryProviderConfig(description = "")
public class UserDicImpl extends Dictionary implements UserDic
{
	@Override
	public void getDicVal(Object id, Map<String, Object> dic)
	{
		UserEntity user = Dao.loadById(UserEntity.class, id);
		if (user != null)
		{
			dic.putAll(user.getValues());
		}
	}
}