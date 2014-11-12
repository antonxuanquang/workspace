package com.sean.cache.core;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.CacheConfiguration.TransactionalMode;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.log4j.Logger;

import com.sean.log.core.LogFactory;

public class CacheFactoryEhCache implements Factory
{
	private static Logger logger = LogFactory.getFrameworkLogger();
	private CacheManager cacheManager;
	private Configuration ehcacheConfig;

	public CacheFactoryEhCache()
	{
		synchronized(this)
		{
			logger.info("EhCache CacheFactory start initializing...");

			// 初始化缓存管理器
			ehcacheConfig = new Configuration();
			ehcacheConfig.setName("cache");
			ehcacheConfig.setDynamicConfig(false);
			ehcacheConfig.setUpdateCheck(false);
			ehcacheConfig.setMonitoring("OFF");
			cacheManager = CacheManager.create(ehcacheConfig);

			logger.info("The EhCache CacheFactory initailized successfully");	
		}
	}

	@Override
	public Cache getCache(String name)
	{
		net.sf.ehcache.Cache cache = cacheManager.getCache(name);
		if (cache != null)
		{
			return new CacheEhCacheImpl(cache);
		}
		return null;
	}

	@Override
	public Cache createCache(String name, int maxElementsInMemory)
	{
		if (getCache(name) == null)
		{
			CacheConfiguration cfg = getDefaultConfig();
			cfg.name(name);
			cfg.maxEntriesLocalHeap(maxElementsInMemory);

			net.sf.ehcache.Cache cache = new net.sf.ehcache.Cache(cfg);
			cacheManager.addCache(cache);

			displayCache(cfg);
			return new CacheEhCacheImpl(cache);
		}
		return null;
	}

	@Override
	public Cache createCache(String name, int maxElementsInMemory, int timeToLiveSeconds)
	{
		if (getCache(name) == null)
		{
			CacheConfiguration cfg = getDefaultConfig();
			cfg.name(name);
			cfg.maxEntriesLocalHeap(maxElementsInMemory);
			cfg.timeToLiveSeconds(timeToLiveSeconds);

			net.sf.ehcache.Cache cache = new net.sf.ehcache.Cache(cfg);
			cacheManager.addCache(cache);

			displayCache(cfg);
			return new CacheEhCacheImpl(cache);
		}
		return null;
	}

	@Override
	public Cache createCache(String name, int maxElementsInMemory, int timeToLiveSeconds, int timeToIdleSeconds)
	{
		if (getCache(name) == null)
		{
			CacheConfiguration cfg = getDefaultConfig();
			cfg.name(name);
			cfg.maxEntriesLocalHeap(maxElementsInMemory);
			cfg.timeToLiveSeconds(timeToLiveSeconds);
			cfg.timeToIdleSeconds(timeToIdleSeconds);

			net.sf.ehcache.Cache cache = new net.sf.ehcache.Cache(cfg);
			cacheManager.addCache(cache);

			displayCache(cfg);
			return new CacheEhCacheImpl(cache);
		}
		return null;
	}

	/**
	 * 获取默认的缓存配置
	 * @return
	 */
	private static CacheConfiguration getDefaultConfig()
	{
		CacheConfiguration cfg = new CacheConfiguration();
		cfg.name("");
		cfg.maxEntriesLocalHeap(3000);
		cfg.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU);
		cfg.eternal(false);
		cfg.timeToIdleSeconds(60 * 20);
		cfg.timeToLiveSeconds(60 * 60);
		cfg.overflowToOffHeap(false);
		cfg.statistics(false);
		cfg.transactionalMode(TransactionalMode.OFF);
		return cfg;
	}

	private void displayCache(CacheConfiguration cfg)
	{
		logger.debug("EhCache CacheFactory create a cache " + cfg.getName() + " , it's maxElementInMemory is " + cfg.getMaxEntriesLocalHeap()
				+ " timeToLiveSeconds is " + cfg.getTimeToLiveSeconds() + " timeToIdleSeconds is " + cfg.getTimeToIdleSeconds());
	}
}
