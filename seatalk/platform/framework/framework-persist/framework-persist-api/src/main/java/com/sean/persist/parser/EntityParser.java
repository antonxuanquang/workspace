package com.sean.persist.parser;

import java.util.List;

import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.CachePolicy;
import com.sean.persist.entity.ColumnEntity;
import com.sean.persist.entity.EntityEntity;

/**
 * entity解析器
 * @author sean
 *
 */
public class EntityParser
{
	public EntityEntity parse(Class<?> cls)
	{
		ColumnParser columnParser = new ColumnParser();
		EntityConfig ec = cls.getAnnotation(EntityConfig.class);

		if (!isInterface(ec.cachePolicy(), CachePolicy.class))
		{
			throw new RuntimeException(ec.cachePolicy().getName() + " must implement CachePolicy");
		}

		List<ColumnEntity> columns = columnParser.parse(cls);
		EntityEntity entity = new EntityEntity(ec.tableName(), ec.dataSource(), ec.cache(), ec.maxElementsInMemory(), ec.timeToLiveSeconds(),
				ec.timeToIdleSeconds(), columns, ec.cachePolicy(), cls);
		return entity;
	}

	private boolean isInterface(Class<?> src, Class<?> cls)
	{
		Class<?>[] interfaces = src.getInterfaces();
		for (Class<?> inter : interfaces)
		{
			if (inter == cls)
			{
				return true;
			}
		}
		return false;
	}
}
