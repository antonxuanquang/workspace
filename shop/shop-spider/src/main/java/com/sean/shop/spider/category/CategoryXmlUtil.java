package com.sean.shop.spider.category;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

/**
 * 生成分类xml
 * @author sean
 */
@SuppressWarnings("unchecked")
public class CategoryXmlUtil
{
	public void print(Element item, Map<String, String> map, boolean idKey)
	{
//		System.out.println(item.attributeValue("id") + "-" + item.attributeValue("name"));

		String id = item.attributeValue("id");
		String name = item.attributeValue("name");
		if (idKey)
		{
			map.put(id, name);
		}
		else
		{
			map.put(id + "_" + name, id);
		}

		List<Element> childElements = item.elements();
		for (Element it : childElements)
		{
			print(it, map, idKey);
		}
	}

	public Map<String, String> loadXml(boolean idKey) throws Exception
	{
		Map<String, String> map = new TreeMap<>();
		SAXReader reader = new SAXReader();
		File file = new File(CategoryXmlUtil.class.getResource("/category/category.xml").getFile());
		Document document = reader.read(file);
		Element root = document.getRootElement();
		List<Element> childElements = root.elements();
		for (Element it : childElements)
		{
			print(it, map, idKey);
		}
		return map;
	}
	
	@Test
	public void loadXmlTest() throws Exception
	{
		System.out.println(new CategoryXmlUtil().loadXml(false));
	}

	@Test
	public void genXml()
	{
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("categorys");

		try
		{
			InputStreamReader input = new InputStreamReader(CategoryXmlUtil.class.getResourceAsStream("/category/category.db"));
			BufferedReader reader = new BufferedReader(input);
			String line = null;

			Element rootCategory = null;

			long id = 1000000000L;
			long headerId = 0L;

			while ((line = reader.readLine()) != null)
			{
				if (line.isEmpty())
				{
					continue;
				}

				String[] tmp = line.split(" ");

				// 新类别
				if (tmp.length == 1)
				{
					id = id + 100000000L;
					rootCategory = root.addElement("item");
					rootCategory.addAttribute("id", id + "");
					rootCategory.addAttribute("name", tmp[0]);
					
					headerId = 0;
				}
				else if (tmp.length > 1)
				{
					Element header = null;
					for (int i = 0; i < tmp.length; i++)
					{
						if (i == 0)
						{
							header = rootCategory.addElement("item");
							headerId += 1000000L;
							header.addAttribute("id", String.valueOf(headerId + id));
							header.addAttribute("name", tmp[i]);
						}
						else
						{
							Element item = header.addElement("item");
							item.addAttribute("id", String.valueOf(i * 10000 + headerId + id));
							item.addAttribute("name", tmp[i]);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// 创建XML文档
		try
		{
			// 生成字符串内容
			StringWriter out = new StringWriter(1024);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			XMLWriter xmlWriter = new XMLWriter(out, format);
			xmlWriter.write(document);
			System.out.println(out.toString());
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}
	}
}