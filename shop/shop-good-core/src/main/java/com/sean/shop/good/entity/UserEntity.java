package com.sean.shop.good.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

/**
 * 用户实体
 * @author sean
 */
@EntityConfig(tableName = "t_user", dataSource = "shop", cache = true, descr = "用户信息实体")
public class UserEntity extends Entity implements Serializable
{
	public static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "用户Id")
	public long userId;
	@ColumnConfig(descr = "用户帐号")
	public String username;
	@ColumnConfig(descr = "用户密码")
	public String password;
	@ColumnConfig(descr = "积分")
	public int integration;
	@ColumnConfig(descr = "注册时间")
	public long registTime;

	@Override
	public Object getKey()
	{
		return userId;
	}

	@Override
	public void setKey(Object key)
	{
		this.userId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(5);
		map.put("userId", userId);
		map.put("username", username);
		map.put("password", password);
		map.put("integration", integration);
		map.put("registTime", registTime);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.userId = vals.getLong("userId");
		this.username = vals.getString("username");
		this.password = vals.getString("password");
		this.integration = vals.getInt("integration");
		this.registTime = vals.getLong("registTime");
	}

	public static void main(String[] args)
	{
		new UserEntity().genCode();
	}
}