package com.sean.im.client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil
{
	private static final SimpleDateFormat FORMAT_HHMMSS = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat FORMAT_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat FORMAT_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat FORMAT_YYYYMMDDHHMMSSLong = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 获取yyyyMMddHHmmss格式日期
	 * @return
	 */
	public static long getYYYYMMDDHHMMSSTime()
	{
		return Long.parseLong(FORMAT_YYYYMMDDHHMMSSLong.format(new Date()));
	}

	public static String parseHHMMSS(long time)
	{
		try
		{
			return FORMAT_HHMMSS.format(FORMAT_YYYYMMDDHHMMSSLong.parse(String.valueOf(time)));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String parseYYYYMMDDHHMMSS(long time)
	{
		try
		{
			return FORMAT_YYYYMMDDHHMMSS.format(FORMAT_YYYYMMDDHHMMSSLong.parse(String.valueOf(time)));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String parseYYYYMMDD(long time)
	{
		try
		{
			return FORMAT_YYYYMMDD.format(FORMAT_YYYYMMDDHHMMSSLong.parse(String.valueOf(time)));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
