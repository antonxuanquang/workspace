package com.sean.cache.core;

import java.io.Serializable;
import java.util.List;

import net.sf.ehcache.Element;

/**
 * 缓存ehcache实现
 * @author sean
 */
@SuppressWarnings("unchecked")
public class CacheEhCacheImpl implements Cache
{
	private net.sf.ehcache.Cache cache;

	public CacheEhCacheImpl(net.sf.ehcache.Cache cache)
	{
		this.cache = cache;
	}

	@Override
	public void put(String key, Serializable val)
	{
		cache.putQuiet(new Element(key, val));
	}

	@Override
	public Object get(String key)
	{
		Element el = cache.getQuiet(key);
		if (el != null)
		{
			return el.getObjectValue();
		}
		return null;
	}

	@Override
	public void remove(String key)
	{
		cache.removeQuiet(key);
	}

	@Override
	public void clear()
	{
		cache.removeAll();
	}

	@Override
	public List<String> getKeys()
	{
		return cache.getKeys();
	}

	@Override
	public String getName()
	{
		return cache.getName();
	}

}
