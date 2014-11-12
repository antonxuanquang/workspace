package com.sean.persist.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sean.persist.core.DataSourceProvider;
import com.sean.persist.core.DefaultCachePolicy;

/**
 * entity config
 * @author sean
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityConfig
{
	/**
	 * table name to mapped
	 */
	String tableName();

	/**
	 * datasource
	 */
	Class<? extends DataSourceProvider> dataSource();

	/**
	 * enable cache，default by false
	 */
	boolean cache() default false;

	/**
	 * maximum records cached in memory, default by 1000
	 */
	int maxElementsInMemory() default 1000;

	/**
	 * cache live seconds, default by 3600
	 */
	int timeToLiveSeconds() default 3600;

	/**
	 * time to idle, default by 1800
	 */
	int timeToIdleSeconds() default 1800;

	/**
	 * cache strategy，which must implements interface CachePolicy
	 */
	Class<?> cachePolicy() default DefaultCachePolicy.class;

	/**
	 * description
	 */
	String description();
}
