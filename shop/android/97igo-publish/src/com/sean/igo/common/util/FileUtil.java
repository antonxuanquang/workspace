package com.sean.igo.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtil
{
	@SuppressWarnings("resource")
	public static void writeStringToFile(File file, String content, boolean append)
	{
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(file, append);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
			writer.write(content);
			writer.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static String readStringFromFile(File file)
	{
		if (!file.exists())
		{
			return null;
		}
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			StringBuilder sb = new StringBuilder();
			String tmp = null;
			while ((tmp = reader.readLine()) != null)
			{
				sb.append(tmp);
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fis != null)
			{
				try
				{
					fis.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
