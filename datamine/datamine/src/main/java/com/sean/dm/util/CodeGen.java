package com.sean.dm.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class CodeGen
{
	@Test
	public void genArff() throws Exception
	{
		File file = new File(CodeGen.class.getResource("/feature").getFile());
		String str = FileUtils.readFileToString(file);
		System.out.println("@relation gender_predict\n");
		for (String it : str.split(" "))
		{
			System.out.println("@attribute " + it + " numeric");
		}
		
		System.out.println("\n@attribute gender {0,1}");
	}
	
	@Test
	public void genData() throws Exception
	{
		System.out.println("@data\n");
	}
}
