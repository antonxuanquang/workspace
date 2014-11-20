package com.sean.ipfilter.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping
{
	public static void main(String args[])
	{
		Pattern ptn = Pattern.compile("time=(.*?) ms");
		
		String[] addrs = { "www.waps.cn" };
		for (int i = 0; i < addrs.length; i++)
		{
			String line = null;
			try
			{
				Process pro = Runtime.getRuntime().exec("ping " + addrs[i] + " -c 1");
				BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				StringBuilder sb = new StringBuilder();
				while ((line = buf.readLine()) != null)
				{
					sb.append(line);
				}
				
				String result = sb.toString();
				System.out.println(result);
				Matcher m = ptn.matcher(result);
				if (m.find())
				{
					System.out.println("延迟:" + m.group(1));
				}
			}
			catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}
}
