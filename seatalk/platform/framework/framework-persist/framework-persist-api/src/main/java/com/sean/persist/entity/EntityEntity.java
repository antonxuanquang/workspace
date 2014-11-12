package com.sean.persist.entity;

import java.util.List;

import com.sean.persist.core.DataSourceProvider;

/**
 * Entity实体
 * @author sean
 */
public class EntityEntity
{
	private ColumnEntity primaryKey;
	private String tableName;
	private Class<? extends DataSourceProvider> dataSource;
	private boolean cache;
	private List<ColumnEntity> columns;
	private int maxElementsInMemory;
	private int timeToLiveSeconds;
	private int timeToIdleSeconds;
	private Class<?> cachePolicy;
	private Class<?> cls;

	public EntityEntity(String tableName, Class<? extends DataSourceProvider> dataSource, boolean cache, int maxElementsInMemory,
			int timeToLiveSeconds, int timeToIdleSeconds, List<ColumnEntity> columns, Class<?> cachePolicy, Class<?> cls)
	{
		this.tableName = tableName;
		this.dataSource = dataSource;
		this.cache = cache;
		this.maxElementsInMemory = maxElementsInMemory;
		this.timeToLiveSeconds = timeToLiveSeconds;
		this.timeToIdleSeconds = timeToIdleSeconds;
		this.columns = columns;
		this.cachePolicy = cachePolicy;
		this.cls = cls;

		for (ColumnEntity c : columns)
		{
			if (c.isPrimaryKey())
			{
				this.primaryKey = c;
				break;
			}
		}
	}

	public String getTableName()
	{
		return tableName;
	}

	public Class<? extends DataSourceProvider> getDataSource()
	{
		return dataSource;
	}

	public List<ColumnEntity> getColumns()
	{
		return columns;
	}

	public boolean isCache()
	{
		return cache;
	}

	public int getMaxElementsInMemory()
	{
		return maxElementsInMemory;
	}

	public int getTimeToLiveSeconds()
	{
		return timeToLiveSeconds;
	}

	public int getTimeToIdleSeconds()
	{
		return timeToIdleSeconds;
	}

	public ColumnEntity getPrimaryKey()
	{
		return primaryKey;
	}

	public Class<?> getCachePolicy()
	{
		return cachePolicy;
	}

	public Class<?> getCls()
	{
		return cls;
	}

}
