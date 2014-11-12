package com.sean.cache.core;

import java.io.Serializable;
import java.util.List;

/**
 * 缓存接口
 * @author sean
 */
public interface Cache
{
	/**
	 * 添加缓存对象
	 * @param key
	 * @param val
	 */
	public void put(String key, Serializable val);

	/**
	 * 获取缓存对象
	 * @param key
	 * @return
	 */
	public Object get(String key);

	/**
	 * 删除缓存对象
	 * @param key
	 */
	public void remove(String key);

	/**
	 * 清空缓存
	 */
	public void clear();
	
	/**
	 * 获取所有Key
	 * @return
	 */
	public List<String> getKeys();

	/**
	 * 获取缓存名称
	 * @return
	 */
	public String getName();
}
