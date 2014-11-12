package com.sean.shop.good;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sean.service.core.TestContext;
import com.sean.service.core.Tester;

public class _Main
{
	private Tester tester;

	@Test
	public void InquireCategoryTest() throws Exception
	{
		tester.testSuite(InquireCategoryTest.class);
	}

	public static void main(String[] args) throws IOException
	{
		String txt = FileUtils.readFileToString(new File("/home/sean/Desktop/SogouLabDic.dic"), "gbk");
		FileUtils.writeStringToFile(new File("/home/sean/Desktop/SogouLabDic.dic1"), txt, "utf-8");
	}

	@Test
	public void InquireGoodListTest() throws Exception
	{
		tester.testSuite(InquireGoodListTest.class);
	}

	@Test
	public void FeedbackGoodTest() throws Exception
	{
		tester.testSuite(FeedbackGoodTest.class);
	}
	
	@Test
	public void InquireFeedbackTest() throws Exception
	{
		tester.testSuite(InquireFeedbackTest.class);
	}

	@Test
	public void DeleteGoodTest() throws Exception
	{
		tester.testSuite(DeleteGoodTest.class);
	}

	@Test
	public void UpdateGoodTest() throws Exception
	{
		tester.testSuite(UpdateGoodTest.class);
	}

	@Test
	public void UpdateGoodStatusTest() throws Exception
	{
		tester.testSuite(UpdateGoodStatusTest.class);
	}

	@Test
	public void AddGoodTest() throws Exception
	{
		tester.testSuite(AddGoodTest.class);
	}

	@Test
	public void SearchGoodTest() throws Exception
	{
		tester.testSuite(SearchGoodTest.class);
	}
	
	@Test
	public void SearchGood4ConsoleTest() throws Exception
	{
		tester.testSuite(SearchGood4ConsoleTest.class);
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
