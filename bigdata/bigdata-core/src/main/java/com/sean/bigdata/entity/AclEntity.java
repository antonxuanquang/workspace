package com.sean.bigdata.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_acl", dataSource = "bigdata", descr = "报表访问实体", cache = true)
public class AclEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "用户ID")
	public long aclId;
	@ColumnConfig(descr = "帐号")
	public String userId;
	@ColumnConfig(descr = "密码")
	public String reportId;
	@ColumnConfig(descr = "角色, 1-管理员, 2-普通用户")
	public long authTime;

	@Override
	public Object getKey()
	{
		return aclId;
	}

	@Override
	public void setKey(Object key)
	{
		this.aclId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(4);
		map.put("aclId", aclId);
		map.put("userId", userId);
		map.put("reportId", reportId);
		map.put("authTime", authTime);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.aclId = vals.getLong("aclId");
		this.userId = vals.getString("userId");
		this.reportId = vals.getString("reportId");
		this.authTime = vals.getLong("authTime");
	}

	public static void main(String[] args)
	{
		new AclEntity().genCode();
	}
}
