package com.sean.persist.core;

import net.sf.ehcache.Cache;

/**
 * 缓存策略接口
 * @author sean
 */
public interface CachePolicy
{
	enum RemoveType
	{
		Insert, Update, Remove
	}

	/**
	 * 包裹语句缓存key，建议不要破坏原来的key，原来的key经过md5加密，建议在key开始加上前缀
	 * @param key					框架默认的key
	 * @return
	 */
	public String wrapStatmentKey(String key);

	/**
	 * <p>清空所有语句缓存</p>
	 * <p>根据wrapStatmentKey清空指定范围所有语句缓存，默认清空所有语句缓存</p>
	 * @param cache					缓存对象
	 */
	public void removeAllStatementCache(Cache cache, RemoveType removeType);
}
