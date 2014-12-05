package com.sean.shop.search.api;

public class Query
{
	public String keyword;
	public int ranking;
	
	public int channel = -1;
	public long categoryId = -1;
	
	public int priceStart = -1;
	public int priceEnd = -1;
	
	public int status = 1;
	public int isFree = -1;

	public int pageNo = 1;
	public int pageSize = 24;

	public long totalrecord = 0;
}
