package com.sean.persist.core;

import net.sf.ehcache.Cache;

/**
 * 默认缓存策略
 * @author sean
 */
public class DefaultCachePolicy implements CachePolicy
{
	@Override
	public String wrapStatmentKey(String key)
	{
		return key;
	}

	@Override
	public void removeAllStatementCache(Cache cache, RemoveType removeType)
	{
		cache.removeAll();
	}
}
