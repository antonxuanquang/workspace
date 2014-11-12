package com.sean.config.enums;

/**
 * 配置文件枚举
 * @author sean
 */
public enum ConfigEnum
{
	// cache配置项
	CachePersist("cache_persist"),
	CacheRedisHost("cache_redis_host"),
	CacheRedisPort("cache_redis_port"),
	CacheRedis_Db("cache_redis_db"),
	
	// persist配置项
	PersistPackagePrefix("persist.package.prefix"),			// 持久层扫描包前缀
	
	// service配置项
	ProjectName("project.name"),							// 应用名称
	ServicePackageProfix("service.package.prefix");			// 扫描包前缀
	
	private String name;
	
	ConfigEnum(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
