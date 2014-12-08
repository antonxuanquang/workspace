package com.sean.shop.good.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_order", dataSource = "shop", descr = "订单实体")
public class OrderEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "订单ID")
	public long orderId;
	@ColumnConfig(descr = "商品ID")
	public long goodId;
	@ColumnConfig(descr = "用户ID")
	public long userId;
	@ColumnConfig(descr = "积分")
	public int integration;
	@ColumnConfig(descr = "手机号码")
	public String tel;
	@ColumnConfig(descr = "qq号码")
	public String qq;
	@ColumnConfig(descr = "address")
	public String address;
	@ColumnConfig(descr = "备注")
	public String remark;
	@ColumnConfig(descr = "创建时间")
	public long createTime;
	@ColumnConfig(descr = "订单状态, 1-未处理, 2-已经处理, 3-退单")
	public int status;

	@Override
	public Object getKey()
	{
		return orderId;
	}

	@Override
	public void setKey(Object key)
	{
		this.orderId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(10);
		map.put("orderId", orderId);
		map.put("goodId", goodId);
		map.put("userId", userId);
		map.put("integration", integration);
		map.put("tel", tel);
		map.put("qq", qq);
		map.put("address", address);
		map.put("remark", remark);
		map.put("createTime", createTime);
		map.put("status", status);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.orderId = vals.getLong("orderId");
		this.goodId = vals.getLong("goodId");
		this.userId = vals.getLong("userId");
		this.integration = vals.getInt("integration");
		this.tel = vals.getString("tel");
		this.qq = vals.getString("qq");
		this.address = vals.getString("address");
		this.remark = vals.getString("remark");
		this.createTime = vals.getLong("createTime");
		this.status = vals.getInt("status");
	}

	public static void main(String[] args)
	{
		new OrderEntity().genCode();
	}
}
