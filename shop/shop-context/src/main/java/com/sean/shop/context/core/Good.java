package com.sean.shop.context.core;

/**
 * 商品索引
 * @author sean
 */
public class Good
{
	public long goodId;
	public String goodName;
	public String keyword;
	public int channel;
	public long categoryId;
	public float price;
	// 权重
	public float boost;
	public int status;
	public long showTimes;
	public int saleCount;
}
