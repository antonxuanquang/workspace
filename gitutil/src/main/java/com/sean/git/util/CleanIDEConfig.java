package com.sean.git.util;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class CleanIDEConfig
{
	public static void main(String[] args) throws Exception
	{
		File file = new File("/home/sean/Desktop/github/workspace/trunk/data_mine");
		clean(file);
		System.out.println("over");
	}

	private static void clean(File dir) throws Exception
	{
		if (dir.isDirectory())
		{
			String dirname = dir.getName();
			if (dirname.equals(".settings"))
			{
				FileUtils.deleteDirectory(dir);
				System.out.println("删除目录:" + dir.getAbsolutePath());
				return;
			}
			if (dirname.equals(".svn"))
			{
				FileUtils.deleteDirectory(dir);
				System.out.println("删除目录:" + dir.getAbsolutePath());
				return;
			}
			if (dirname.equals("target"))
			{
				FileUtils.deleteDirectory(dir);
				System.out.println("删除目录:" + dir.getAbsolutePath());
				return;
			}

			File[] files = dir.listFiles();
			for (File it : files)
			{
				clean(it);
			}
		}
		else
		{
			String filename = dir.getName();
			if (filename.equals(".classpath"))
			{
				dir.delete();
				System.out.println("删除文件:" + dir.getAbsolutePath());
				return;
			}
			if (filename.equals(".project"))
			{
				dir.delete();
				System.out.println("删除文件:" + dir.getAbsolutePath());
				return;
			}
		}
	}
}
