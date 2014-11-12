package com.sean.shop.spider.category;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 生成首页分类html
 * @author sean
 */
@SuppressWarnings("unchecked")
public class CategoryGener
{
	public static JSONObject printCategory(Element it, boolean escape, int count)
	{
		// 一级分类
		JSONObject panel = new JSONObject();
		panel.put("id", it.attributeValue("id"));
		panel.put("name", it.attributeValue("name"));
		panel.put("span", it.attributeValue("span"));
		panel.put("color", it.attributeValue("color"));
		panel.put("style", it.attributeValue("style"));

		// 二级分类
		List<Element> secondLevel = it.elements();
		JSONArray array = new JSONArray();
		for (Element i : secondLevel)
		{
			if ("true".equals(i.attributeValue("escape")))
			{
				if (escape)
				{
					continue;
				}
			}
			
			JSONObject json = new JSONObject();
			json.put("id", i.attributeValue("id"));
			json.put("name", i.attributeValue("name"));
			json.put("keyword", i.attributeValue("keyword"));

			// 三级分类
			List<Element> thirdLevel = i.elements();
			JSONArray third = new JSONArray();
			for (int j = 0; j < count && j < thirdLevel.size(); j++)
			{
				JSONObject json1 = new JSONObject();
				json1.put("id", thirdLevel.get(j).attributeValue("id"));
				json1.put("name", thirdLevel.get(j).attributeValue("name"));
				third.add(json1);
			}
			json.put("child", third);

			array.add(json);
		}
		panel.put("child", array);
		return panel;
	}

	public static void main(String[] args) throws Exception
	{
		try
		{
			JSONArray list = new JSONArray();
			SAXReader reader = new SAXReader();
			File file = new File(CategoryXmlUtil.class.getResource("/category/category.xml").getFile());
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();
			for (Element it : childElements)
			{
				list.add(printCategory(it, false, Integer.MAX_VALUE));
			}

			System.out.println(JSON.toJSONString(list, false));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}