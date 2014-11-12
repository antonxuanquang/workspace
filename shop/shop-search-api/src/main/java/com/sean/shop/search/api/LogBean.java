package com.sean.shop.search.api;

public interface LogBean
{
	/**
	 * 增加展示日志
	 * @param goodId
	 * @param searchKey
	 */
	public void addShowLog(long goodId, String searchKey);

	/**
	 * 添加搜索日志
	 * @param keyword
	 * @param pageNo
	 * @param searchResult
	 */
	public void addSearchLog(String keyword, int pageNo, String searchResult);
}
