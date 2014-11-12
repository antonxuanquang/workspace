package com.sean.shop.good.api;

import java.util.List;

import com.sean.shop.context.core.Good;

public interface GoodBean
{
	/**
	 * 读取商品
	 * @param currId
	 * @param size
	 * @return
	 */
	public List<Good> getGoodList(long currId, int size);
	
	/**
	 * 更新商品分类
	 * @param goodId
	 * @param categoryId
	 */
	public void updateGoodCategory(long goodId, long categoryId);
}
