package com.sean.shop.search.api;

import java.io.IOException;
import java.util.List;

import com.sean.service.core.BusinessException;

public interface SearchKeyBean
{
	/**
	 * 搜索搜索关键字
	 * @param key
	 * @return
	 * @throws IOException
	 * @throws BusinessException
	 */
	public List<String> search(String key) throws IOException, BusinessException;
}
