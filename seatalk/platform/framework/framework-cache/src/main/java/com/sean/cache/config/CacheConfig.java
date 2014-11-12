package com.sean.cache.config;

import com.sean.config.core.Config;
import com.sean.config.enums.ConfigEnum;

/**
 * 缓存配置文件
 * @author sean
 *
 */
public class CacheConfig
{
	public static String CachePersist = "ehcache";
	public static String RedisHost;
	public static int RedisPort;
	public static int RedisDB = 1;

	static
	{
		// 默认采用ehcache
		String type = Config.getProperty(ConfigEnum.CachePersist);
		if (type == null || type.equals("ehcache"))
		{
			CachePersist = "ehcache";
		}
		else
		{
			// 读取redis配置
			RedisHost = Config.getProperty(ConfigEnum.CacheRedisHost);
			RedisPort = Integer.parseInt(Config.getProperty(ConfigEnum.CacheRedisPort));
			RedisDB = Integer.parseInt(Config.getProperty(ConfigEnum.CacheRedis_Db));
		}
	}
}
