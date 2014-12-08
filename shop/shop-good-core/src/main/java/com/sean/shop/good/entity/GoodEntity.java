package com.sean.shop.good.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityValue;

@EntityConfig(tableName = "t_good", dataSource = "shop", descr = "商品实体", cache = true)
public class GoodEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ColumnConfig(primaryKey = true, descr = "商品ID")
	public long goodId;
	@ColumnConfig(descr = "商品名称")
	public String goodName;
	@ColumnConfig(descr = "商品价格")
	public float price;
	@ColumnConfig(descr = "商品图片Url")
	public String imageUrl;
	@ColumnConfig(descr = "淘宝商品Url")
	public String goodUrl;
	@ColumnConfig(descr = "商品来源渠道: 1-天天9块九, 2-精品推荐")
	public int channel;
	@ColumnConfig(descr = "创建时间")
	public long createTime;
	@ColumnConfig(descr = "商品状态: 1-上架, 2-下架")
	public int status;
	@ColumnConfig(descr = "商品分类")
	public long categoryId;

	@ColumnConfig(descr = "搜索引擎索引关键字")
	public String keyword;
	@ColumnConfig(descr = "近期展示次数")
	public long showTimes;
	@ColumnConfig(descr = "30天售出的件数")
	public int saleCount;
	@ColumnConfig(descr = "佣金比率")
	public float commissionRate;
	@ColumnConfig(descr = "30天支出的佣金总数")
	public float commissionCount;
	@ColumnConfig(descr = "搜索排名得分调整参数, 默认为1")
	public float boost;

	@Override
	public Object getKey()
	{
		return goodId;
	}

	@Override
	public void setKey(Object key)
	{
		this.goodId = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<>(16);
		map.put("goodId", goodId);
		map.put("goodName", goodName);
		map.put("price", price);
		map.put("imageUrl", imageUrl);
		map.put("goodUrl", goodUrl);
		map.put("channel", channel);
		map.put("createTime", createTime);
		map.put("status", status);
		map.put("categoryId", categoryId);
		map.put("keyword", keyword);
		map.put("showTimes", showTimes);
		map.put("saleCount", saleCount);
		map.put("commissionRate", commissionRate);
		map.put("commissionCount", commissionCount);
		map.put("boost", boost);
		return map;
	}

	@Override
	public void setValues(EntityValue vals)
	{
		this.goodId = vals.getLong("goodId");
		this.goodName = vals.getString("goodName");
		this.price = vals.getFloat("price");
		this.imageUrl = vals.getString("imageUrl");
		this.goodUrl = vals.getString("goodUrl");
		this.channel = vals.getInt("channel");
		this.createTime = vals.getLong("createTime");
		this.status = vals.getInt("status");
		this.categoryId = vals.getLong("categoryId");
		this.keyword = vals.getString("keyword");
		this.showTimes = vals.getLong("showTimes");
		this.saleCount = vals.getInt("saleCount");
		this.commissionRate = vals.getFloat("commissionRate");
		this.commissionCount = vals.getFloat("commissionCount");
		this.boost = vals.getFloat("boost");
	}

	public static void main(String[] args)
	{
		new GoodEntity().genCode();
	}
}
