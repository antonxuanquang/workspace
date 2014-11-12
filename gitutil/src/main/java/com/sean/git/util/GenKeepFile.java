package com.sean.git.util;

import java.io.File;

public class GenKeepFile
{
	public static void main(String[] args) throws Exception
	{
		File file = new File("/home/sean/Desktop/github/workspace2/trunk");
		gen(file);
		System.out.println("over");
	}

	private static void gen(File dir) throws Exception
	{
		File[] files = dir.listFiles();
		if (files.length == 0)
		{
			File keep = new File(dir.getAbsolutePath() + "/.gitkeep");
			keep.createNewFile();
			System.out.println("创建文件:" + keep.getAbsolutePath());
		}
		else
		{
			for (File it : files)
			{
				if (it.isDirectory())
				{
					gen(it);
				}
			}
		}
	}
}
