package com.sean.shop.good.dic;

import java.util.Map;

import com.sean.persist.core.Dao;
import com.sean.persist.dictionary.Dictionary;
import com.sean.persist.dictionary.DictionaryProviderConfig;
import com.sean.shop.good.api.GoodDic;
import com.sean.shop.good.entity.GoodEntity;

@DictionaryProviderConfig(description = "")
public class GoodDicImpl extends Dictionary implements GoodDic
{
	@Override
	public void getDicVal(Object id, Map<String, Object> dic)
	{
		GoodEntity good = Dao.loadById(GoodEntity.class, id);
		if (good != null)
		{
			dic.putAll(good.getValues());
		}
	}
}