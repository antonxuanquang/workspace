package com.sean.cache.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.sean.cache.config.CacheConfig;
import com.sean.commom.util.ObjectUtil;
import com.sean.log.core.LogFactory;

/**
 * 缓存redis实现
 * @author sean
 */
public class CacheRedisImpl implements Cache
{
	private static final Logger logger = LogFactory.getFrameworkLogger();
	public JedisPool pool;
	private String name;
	private byte[] id;

	public CacheRedisImpl(String name)
	{
		this.name = name;
		this.id = name.getBytes();
		JedisPoolConfig conf = new JedisPoolConfig();
		pool = new JedisPool(conf, CacheConfig.RedisHost, CacheConfig.RedisPort);
	}

	@Override
	public void put(String key, Serializable val)
	{
		Jedis jedis = pool.getResource();
		try
		{
			if (val != null)
			{
				jedis.hset(id, key.getBytes(), ObjectUtil.serialize(val));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			pool.returnResource(jedis);
		}
	}

	@Override
	public Object get(String key)
	{
		Jedis jedis = pool.getResource();
		try
		{
			byte[] val = jedis.hget(id, key.getBytes());
			if (val != null)
			{
				return ObjectUtil.unSerialize(val);
			}
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			pool.returnResource(jedis);
		}
		return null;
	}

	@Override
	public void remove(String key)
	{
		Jedis jedis = pool.getResource();
		try
		{
			jedis.hdel(id, key.getBytes());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			pool.returnResource(jedis);
		}
	}

	@Override
	public void clear()
	{
		Jedis jedis = pool.getResource();
		try
		{
			Set<byte[]> fields = jedis.hkeys(id);
			if (fields != null && !fields.isEmpty())
			{
				byte[][] fs = new byte[fields.size()][];
				int i = 0;
				for (byte[] f : fields)
				{
					fs[i] = f;
					i++;
				}
				jedis.hdel(id, fs);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			pool.returnResource(jedis);
		}
	}

	@Override
	public List<String> getKeys()
	{
		Jedis jedis = pool.getResource();
		try
		{
			Set<byte[]> set = jedis.hkeys(id);
			List<String> keys = new ArrayList<String>(set.size());
			if (set != null && !set.isEmpty())
			{
				for (byte[] b : set)
				{
					keys.add(ObjectUtil.unSerialize(b).toString());
				}
			}
			return keys;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			pool.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String getName()
	{
		return name;
	}

}
