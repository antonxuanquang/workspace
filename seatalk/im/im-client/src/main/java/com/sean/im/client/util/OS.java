package com.sean.im.client.util;

public class OS
{
	public enum OSType
	{
		Windows, Linux, MacOSX
	}

	public static OSType getOSType()
	{
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("windows") || os.contains("win"))
		{
			return OSType.Windows;
		}
		else if (os.contains("linux"))
		{
			return OSType.Linux;
		}
		else if (os.contains("mac"))
		{
			return OSType.MacOSX;
		}
		return OSType.Windows;
	}
}
