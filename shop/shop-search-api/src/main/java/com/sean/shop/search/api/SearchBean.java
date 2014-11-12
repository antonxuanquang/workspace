package com.sean.shop.search.api;

import java.io.IOException;
import java.util.List;

import com.sean.service.core.BusinessException;
import com.sean.shop.context.core.Good;

public interface SearchBean
{
	/**
	 * 搜索商品
	 * @param query
	 * @return
	 */
	public List<Object> search(Query query) throws IOException, BusinessException;

	/**
	 * 新增商品索引
	 * @param good
	 */
	public void addGood(Good good) throws IOException, BusinessException;

	/**
	 * 更新商品索引
	 * @param good
	 */
	public void updateGood(Good good) throws IOException, BusinessException;

	/**
	 * 删除商品索引
	 * @param goodId
	 */
	public void deleteGood(long goodId) throws IOException, BusinessException;
}
