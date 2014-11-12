package com.sean.shop.spider.alimama;

public class Status
{
	// 当前爬取的页码
	public long pageNo;
	// 渠道
	public int channel;
	// 当前页码第几条
	public long offset;

	// 是否此次启动第一次爬去
	public boolean currFirst = true;

	public int queryIndex = 0;
	public String query = "";

	@Override
	public String toString()
	{
		return "Status [pageNo=" + pageNo + ", channel=" + channel + ", offset=" + offset + ", currFirst=" + currFirst + ", queryIndex=" + queryIndex
				+ ", query=" + query + "]";
	}

}
