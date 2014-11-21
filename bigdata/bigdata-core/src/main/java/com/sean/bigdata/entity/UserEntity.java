package com.sean.bigdata.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_user", dataSource = "bigdata", descr = "用户实体", cache = false)
public class UserEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "用户ID")
	public long userId;
	@ColumnConfig(descr = "帐号")
	public String username;
	@ColumnConfig(descr = "密码")
	public String password;
	@ColumnConfig(descr = "姓名")
	public String name;
	@ColumnConfig(descr = "角色, 1-管理员, 2-普通用户")
	public byte role;
	@ColumnConfig(descr = "用户加密key")
	public String encryptKey;

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
		Map<String, Object> map = new HashMap<>(6);
		map.put("userId", userId);
		map.put("username", username);
		map.put("password", password);
		map.put("name", name);
		map.put("role", role);
		map.put("encryptKey", encryptKey);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.userId = vals.getLong("userId");
		this.username = vals.getString("username");
		this.password = vals.getString("password");
		this.name = vals.getString("name");
		this.role = vals.getByte("role");
		this.encryptKey = vals.getString("encryptKey");
	}

	public static void main(String[] args)
	{
		new UserEntity().genCode();
	}
}
