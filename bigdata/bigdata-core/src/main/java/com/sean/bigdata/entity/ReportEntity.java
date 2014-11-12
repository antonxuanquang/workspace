package com.sean.bigdata.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_report", dataSource = "bigdata", descr = "报表实体", cache = true)
public class ReportEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "报表ID")
	public long reportId;
	@ColumnConfig(descr = "报表名称")
	public String reportName;
	@ColumnConfig(descr = "x轴说明")
	public String xAxis;
	@ColumnConfig(descr = "y轴说明")
	public String yAxis;
	@ColumnConfig(descr = "条件分组, 只用于列表报表, 多个用;隔开")
	public String conditions;

	@ColumnConfig(descr = "报表类型:1-单值, 2-数值, 3-列表")
	public byte type;
	@ColumnConfig(descr = "统计类型:1-日统计, 2-月统计")
	public byte countType;
	
	@ColumnConfig(descr = "创建时间")
	public long createTime;
	@ColumnConfig(descr = "创建人")
	public long creater;

	@Override
	public Object getKey()
	{
		return reportId;
	}

	@Override
	public void setKey(Object key)
	{
		this.reportId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(9);
		map.put("reportId", reportId);
		map.put("reportName", reportName);
		map.put("xAxis", xAxis);
		map.put("yAxis", yAxis);
		map.put("conditions", conditions);
		map.put("type", type);
		map.put("countType", countType);
		map.put("createTime", createTime);
		map.put("creater", creater);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.reportId = vals.getLong("reportId");
		this.reportName = vals.getString("reportName");
		this.xAxis = vals.getString("xAxis");
		this.yAxis = vals.getString("yAxis");
		this.conditions = vals.getString("conditions");
		this.type = vals.getByte("type");
		this.countType = vals.getByte("countType");
		this.createTime = vals.getLong("createTime");
		this.creater = vals.getLong("creater");
	}

	public static void main(String[] args)
	{
		new ReportEntity().genCode();
	}
}
