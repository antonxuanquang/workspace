package com.sean.cache.core;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.CacheConfiguration.TransactionalMode;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import com.sean.cache.config.CacheConfig;

/**
 * 缓存工厂
 * @author sean
 */
public final class CacheFactory
{
	private static Factory factory;
	static
	{
		if (CacheConfig.CachePersist.equals("redis"))
		{
			factory = new CacheFactoryRedis();
		}
		else
		{
			factory = new CacheFactoryEhCache();
		}
	}

	private CacheFactory()
	{
	}

	/**
	 * 获取缓存
	 * @param name						缓存名称cache
	 * @return
	 */
	public static Cache getCache(String name)
	{
		return factory.getCache(name);
	}

	/**
	 * 创建缓存
	 * @param name						缓存名称
	 * @param maxElementsInMemory		缓存最大元素数量
	 */
	public static Cache createCache(String name, int maxElementsInMemory)
	{
		return factory.createCache(name, maxElementsInMemory);
	}

	/**
	 * 创建缓存
	 * @param name						缓存名称
	 * @param maxElementsInMemory		缓存最大元素数量
	 * @param timeToLiveSeconds			缓存时间
	 */
	public static Cache createCache(String name, int maxElementsInMemory, int timeToLiveSeconds)
	{
		return factory.createCache(name, maxElementsInMemory, timeToLiveSeconds);
	}

	/**
	 * 创建缓存
	 * @param name						缓存名称
	 * @param maxElementsInMemory		缓存最大元素数量
	 * @param timeToLiveSeconds			缓存时间
	 * @param timeToIdleSeconds			距上次访问或修改删除时间
	 */
	public static Cache createCache(String name, int maxElementsInMemory, int timeToLiveSeconds, int timeToIdleSeconds)
	{
		return factory.createCache(name, maxElementsInMemory, timeToLiveSeconds, timeToIdleSeconds);
	}
	
	public static net.sf.ehcache.Cache createEhCache(String name, int maxElementsInMemory, int timeToLiveSeconds, int timeToIdleSeconds)
	{
		CacheConfiguration cfg = new CacheConfiguration();
		cfg.name(name);
		cfg.logging(false);
		cfg.overflowToOffHeap(false);
		cfg.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU);
		cfg.eternal(false);
		cfg.maxEntriesLocalHeap(maxElementsInMemory);
		cfg.timeToIdleSeconds(timeToIdleSeconds);
		cfg.timeToLiveSeconds(timeToLiveSeconds);
		cfg.overflowToOffHeap(false);
		cfg.statistics(false);
		cfg.transactionalMode(TransactionalMode.OFF);

		net.sf.ehcache.Cache cache = new net.sf.ehcache.Cache(cfg);
		CacheManager.create().addCache(cache);
		return cache;
	}
}
