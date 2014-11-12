package com.sean.persist.core;

import com.sean.persist.dictionary.Dictionary;
import com.sean.persist.dictionary.DictionaryManager;

/**
 * 持久化抽象上下文
 * @author sean
 */
public final class PersistContext
{
	public static PersistContext CTX;
	private EntityDaoManager entityDaoManager;
	private DictionaryManager dynamicDicManager;

	public PersistContext(EntityDaoManager entityDaoManager, DictionaryManager dynamicDicManager)
	{
		this.entityDaoManager = entityDaoManager;
		this.dynamicDicManager = dynamicDicManager;
	}

	/**
	 * 读取数据库访问接口
	 * @return
	 */
	public <E extends Entity> EntityDao<E> getEntityDao(Class<E> entity)
	{
		return (EntityDao<E>) this.entityDaoManager.getEntityDao(entity);
	}

	/**
	 * 获取数据字典
	 * @param dicName						字典名称
	 * @return
	 */
	public Dictionary getDictionary(String dicName)
	{
		return this.dynamicDicManager.getDictionary(dicName);
	}
}
