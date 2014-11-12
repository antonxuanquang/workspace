package com.sean.im.client.util;

import java.lang.reflect.Method;

import com.sean.im.client.util.OS.OSType;

/**
 * 浏览器工具类
 * @author sean
 */
public class Browser
{
	public static boolean openURL(String url, String json)
	{
		OSType type = OS.getOSType();
		try
		{
			if (type == OSType.MacOSX)
			{
				Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
				return true;
			}
			else if (type == OSType.Windows)
			{
				String cmd = "cmd /c start iexplore " + url;
				Runtime.getRuntime().exec(cmd);
//				Desktop.getDesktop().browse(new URI(url));
				return true;
			}
			else if (type == OSType.Linux)
			{
				String[] browsers = { "chrome", "firefox", "chromium-browser", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
				String browser = null;
				for (String item : browsers)
				{
					if (Runtime.getRuntime().exec(new String[] { "which", item }).waitFor() == 0)
					{
						browser = item;
						break;
					}
				}
				if (browser != null)
				{
					Runtime.getRuntime().exec(new String[] { browser, url });
					return true;
				}
			}
			else
			{
				throw new RuntimeException("没有安装浏览器");
			}
			return false;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
}
