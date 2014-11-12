package com.sean.shop.search.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sean.log.core.LogFactory;

/**
 * 智能分类器
 * @author sean
 */
@SuppressWarnings("unchecked")
public final class Categorier
{
	private static final Logger logger = LogFactory.getLogger(Categorier.class.getSimpleName());

	private static Map<Long, String> categoryDic;
	private static Map<String, Long> categorys;
	private static Map<Long, Set<String>> categoryToken;

	// 分类向量分词长度得分
	private static final float[] scores = new float[] { 0f, 0.1f, 0.12f, 0.2f, 0.35f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f };
	private static final Random random = new Random();

	static
	{
		try
		{
			// 读取顶级分类
			categorys = loadCategoryXml();
			categoryDic = new TreeMap<>();
			for (String cname : categorys.keySet())
			{
				categoryDic.put(categorys.get(cname), cname);
			}

			// 加载分类词库
			categoryToken = loadCategoryToken();

			logger.info("顶级分类器加载完成");
		}
		catch (Exception e)
		{
			logger.error("分类器初始化错误:" + e.getMessage(), e);
		}
	}

	private Categorier()
	{
	}

	private static Map<String, Long> loadCategoryXml() throws Exception
	{
		Map<String, Long> map = new HashMap<>();

		SAXReader reader = new SAXReader();
		Document document = reader.read(Categorier.class.getResourceAsStream("/category/category.xml"));
		Element root = document.getRootElement();
		List<Element> childElements = root.elements();
		for (Element it : childElements)
		{
			map.put(it.attributeValue("name"), Long.parseLong(it.attributeValue("id")));
		}
		return map;
	}

	private static Map<Long, Set<String>> loadCategoryToken() throws Exception
	{
		Map<Long, Set<String>> map = new TreeMap<>();

		InputStreamReader input = new InputStreamReader(Categorier.class.getResourceAsStream("/category/category.first"));
		BufferedReader reader = new BufferedReader(input);
		String line = null;

		long categoryId = -1;
		Set<String> set = null;

		while ((line = reader.readLine()) != null)
		{
			if (line.startsWith("#"))
			{
				set = new HashSet<>();
				categoryId = categorys.get(line.substring(1).trim());
				map.put(categoryId, set);
			}
			else
			{
				String[] tmp = line.split(" ");
				for (String it : tmp)
				{
					if (!it.isEmpty())
					{
						set.add(it.toLowerCase());
					}

					// StringReader sr = new StringReader(it);
					// IKSegmenter ik = new IKSegmenter(sr, true);
					// Lexeme lexeme = null;
					// while ((lexeme = ik.next()) != null)
					// {
					// if (lexeme.getLexemeText().length() > 1)
					// {
					// System.out.println(lexeme.getLexemeText());
					// }
					// }
				}
			}
		}
		return map;
	}

	/**
	 * 分类
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static long category(String text) throws Exception
	{
		// 创建向量
		text = text.toLowerCase();
		Map<Long, Float> vector = new TreeMap<>();
		for (long cid : categorys.values())
		{
			vector.put(cid, 0f);
		}

		for (long cid : vector.keySet())
		{
			for (String it : categoryToken.get(cid))
			{
				if (text.contains(it))
				{
					vector.put(cid, vector.get(cid) + scores[it.length()]);
				}
			}
		}
//		logger.debug("分类向量:" + vector);

		// 计算结果分类
		float max = 0;
		List<Long> target = new LinkedList<>();
		for (long cid : vector.keySet())
		{
			float score = vector.get(cid);
			if (score > 0f)
			{
				if (score > max)
				{
					max = score;

					// 清理target, 将目标分类放入target
					target.clear();
					target.add(cid);
				}
				else if (score == max)
				{
					target.add(cid);
				}
			}
		}

		long cid = 0;
		// 获取目标分类
		if (target.size() == 1)
		{
			cid = target.get(0);
		}
		else if (target.size() == 0)
		{
			System.out.println("无法分类:" + text);
			throw new RuntimeException("无法分类:" + text);
		}
		else
		{
			cid = target.get(random.nextInt(target.size()));
		}

		logger.debug("分类结果:" + categoryDic.get(cid) + " - " + text);
		return cid;
	}
}
