package com.sean.shop.good.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sean.common.ioc.BeanConfig;
import com.sean.log.core.LogFactory;

@SuppressWarnings("unchecked")
@BeanConfig("分类对象")
public class CategoryBeanImpl
{
	private static final Logger logger = LogFactory.getLogger(CategoryBeanImpl.class);

	private static Map<Long, String> categoryMap;
	static
	{
		try
		{
			categoryMap = loadXml();
		}
		catch (Exception e)
		{
			logger.error("读取分类错误:" + e.getMessage(), e);
		}
	}

	/**
	 * 读取类别名称
	 * @param categoryId
	 * @return
	 */
	public String getCategoryName(long categoryId)
	{
		return categoryMap.get(categoryId);
	}

	/**
	 * 读取分类列表
	 * @return
	 */
	public List<Category> getCategoryList()
	{
		List<Category> list = new ArrayList<>();
		for (long categoryId : categoryMap.keySet())
		{
			Category item = new Category();
			item.categoryId = categoryId;
			item.categoryName = categoryMap.get(categoryId);
			list.add(item);
		}
		return list;
	}

	/**
	 * 读取分类列表
	 * @return
	 * @throws Exception
	 */
	public static Map<Long, String> loadXml() throws Exception
	{
		Map<Long, String> map = new TreeMap<>();
		SAXReader reader = new SAXReader();
		File file = new File(CategoryBeanImpl.class.getResource("/category/category.xml").getFile());
		Document document = reader.read(file);
		Element root = document.getRootElement();
		List<Element> childElements = root.elements();
		for (Element it : childElements)
		{
			load(it, map);
		}
		return map;
	}

	private static void load(Element item, Map<Long, String> map)
	{
		map.put(Long.parseLong(item.attributeValue("id")), item.attributeValue("name"));

		List<Element> childElements = item.elements();
		for (Element it : childElements)
		{
			load(it, map);
		}
	}

	public class Category
	{
		public long categoryId;
		public String categoryName;
	}
}
