package com.sean.bigdata.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sean.service.core.TestContext;
import com.sean.service.core.Tester;

public class _Main
{
	private Tester tester;

	@Test
	public void InquireReportListTest() throws Exception
	{
		tester.testSuite(InquireReportListTest.class);
	}
	
	@Test
	public void InquireExecuteListTest() throws Exception
	{
		tester.testSuite(InquireExecuteListTest.class);
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
