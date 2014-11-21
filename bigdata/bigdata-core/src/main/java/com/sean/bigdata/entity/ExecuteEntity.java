package com.sean.bigdata.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_execute", dataSource = "bigdata", descr = "执行实体", cache = false)
public class ExecuteEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "报表ID")
	public long executeId;
	@ColumnConfig(descr = "报表名称")
	public long reportId;
	@ColumnConfig(descr = "执行时间")
	public long executeTime;
	@ColumnConfig(descr = "执行结果json")
	public String result;

	@Override
	public Object getKey()
	{
		return executeId;
	}

	@Override
	public void setKey(Object key)
	{
		this.executeId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(4);
		map.put("executeId", executeId);
		map.put("reportId", reportId);
		map.put("executeTime", executeTime);
		map.put("result", result);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.executeId = vals.getLong("executeId");
		this.reportId = vals.getLong("reportId");
		this.executeTime = vals.getLong("executeTime");
		this.result = vals.getString("result");
	}

	public static void main(String[] args)
	{
		new ExecuteEntity().genCode();
	}
}
