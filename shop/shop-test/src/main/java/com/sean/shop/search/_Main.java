package com.sean.shop.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.sean.common.ioc.BeanFactory;
import com.sean.service.core.TestContext;
import com.sean.service.core.Tester;
import com.sean.shop.search.bean.SearchBeanImpl;
import com.sean.shop.search.core.Categorier;
import com.sean.shop.search.core.ICTCLASAnalyzer;

public class _Main
{
	private Tester tester;

	@Test
	public void InquireHotwordTest() throws Exception
	{
		tester.testSuite(InquireHotwordTest.class);
	}

	@Test
	public void BuildIndex() throws Exception
	{
		SearchBeanImpl bean = BeanFactory.getBean(SearchBeanImpl.class);
		bean.rebuild();

		Thread.sleep(5000);
	}

	@Test
	public void token() throws Exception
	{
		String txt = "2014秋装新款毛呢连衣裙韩版修身七分袖打底裙 七分款套装女包邮费自负吹奏乐器挎包";
		final ICTCLASAnalyzer analyzer = new ICTCLASAnalyzer();
		try
		{
			System.out.print("ictcl : ");
			for (int j = 0; j < 1; j++)
			{
				List<String> ts = analyzer.token(txt.toLowerCase());
				StringBuilder sb = new StringBuilder();
				for (String it : ts)
				{
					sb.append("[" + it + "]");
				}
				System.out.println(sb.toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		analyzer.close();
		
		System.out.print("ik : ");
		StringReader reader = new StringReader(txt.toLowerCase());
		IKSegmenter ik = new IKSegmenter(reader, true);
		Lexeme lexeme = null;
		while ((lexeme = ik.next()) != null)
		{
			System.out.print("[" + lexeme.getLexemeText() + "]");
		}
	}

	@Test
	public void UpdateCategoryAction() throws Exception
	{
		SearchBeanImpl searchBean = BeanFactory.getBean(SearchBeanImpl.class);
		searchBean.category(true, true);
	}

	@Test
	public void category() throws Exception
	{
		// Categorier.category("云域 买三送一优质黑米 月子米 生态健康 五谷杂粮 放心粗粮");

		InputStreamReader input = new InputStreamReader(Object.class.getResourceAsStream("/category/test.txt"));
		BufferedReader reader = new BufferedReader(input);
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			Categorier.category(line);
		}
	}

	@Before
	public void ready() throws Exception
	{
		TestContext tc = new TestContext();
		tc.setLoginUser(1);
		tester = tc.getTester();
	}

	@After
	public void destory()
	{
		System.exit(0);
	}
}
