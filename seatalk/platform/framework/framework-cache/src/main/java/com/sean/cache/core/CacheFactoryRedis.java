package com.sean.cache.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sean.log.core.LogFactory;

public class CacheFactoryRedis implements Factory
{
	private static Logger logger = LogFactory.getFrameworkLogger();
	private Map<String, Cache> cacheManager;

	public CacheFactoryRedis()
	{
		logger.info("Redis CacheFactory start initializing...");

		cacheManager = new HashMap<String, Cache>();

		logger.info("The Redis CacheFactory initailized successfully");
	}

	@Override
	public Cache getCache(String name)
	{
		return this.cacheManager.get(name);
	}

	@Override
	public Cache createCache(String name, int maxElementsInMemory)
	{
		if (getCache(name) == null)
		{
			Cache cache = new CacheRedisImpl(name);
			this.cacheManager.put(name, cache);
			displayCache(name);
			return cache;
		}
		return null;
	}

	@Override
	public Cache createCache(String name, int maxElementsInMemory, int timeToLiveSeconds)
	{
		return createCache(name, 0);
	}

	@Override
	public Cache createCache(String name, int maxElementsInMemory, int timeToLiveSeconds, int timeToIdleSeconds)
	{
		return createCache(name, 0);
	}

	private void displayCache(String name)
	{
		logger.debug("Redis CacheFactory create a cache " + name);
	}
}
