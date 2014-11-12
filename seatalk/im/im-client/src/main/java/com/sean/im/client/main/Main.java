package com.sean.im.client.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;

import com.sean.im.client.constant.Config;
import com.sean.im.client.constant.Global;
import com.sean.im.client.constant.GlobalSetting;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.form.LoginForm;
import com.sean.im.client.util.OS;
import com.sean.im.client.util.OS.OSType;
import com.sean.im.commom.core.HttpUtil;

/**
 * -Xms64M -Xmx256M -verbose:gc
 * @author sean
 */
public class Main
{
	public static void main(String[] args) throws Exception
	{
		Main main = new Main();
		if (args == null || args.length <= 0)
		{
			main.start(null);
		}
		else
		{
			main.start(args[0]);
		}
		main = null;
	}

	public void start(String root) throws Exception
	{
		// 赋值软件根路径
		if (root != null)
		{
			Global.Root = root;
		}
		
		if (OS.getOSType() == OSType.Windows)
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		JFrame.setDefaultLookAndFeelDecorated(false);

		// 读取全局配置
		ObjectInputStream ois = null;
		try
		{
			ois = new ObjectInputStream(new FileInputStream(new File(Global.Root + "config/setting")));
			GlobalSetting setting = (GlobalSetting) ois.readObject();
			Config.GlobalSetting = setting;
		}
		catch (Exception e)
		{
			// 默认配置
			Config.GlobalSetting = new GlobalSetting();
		}
		finally
		{
			if (ois != null)
			{
				try
				{
					ois.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		ois = null;

		// 读取语言
		readLanguage();

		// 读取服务器信息
		InputStream is = new FileInputStream(Global.Root + "config/conf.properties");
		Properties p = new Properties();
		p.load(is);
		is.close();
		ApplicationContext.CTX = new ApplicationContext();
		ApplicationContext.PushHost = p.getProperty("push_host");
		ApplicationContext.PushPort = Integer.parseInt(p.getProperty("push_port"));
		HttpUtil.ServerHost = p.getProperty("server_host");
		HttpUtil.ServerPort = Integer.parseInt(p.getProperty("server_port"));
		HttpUtil.Url = "http://" + HttpUtil.ServerHost + ":" + HttpUtil.ServerPort + "/im-web/";
		is = null;
		p = null;

		// 删除tmp下所有临时文件
		File tmp = new File(Global.Root + "tmp");
		FileUtils.deleteDirectory(tmp);
		tmp = null;

		LoginForm form = new LoginForm();
		form.setVisible(true);
		form = null;
	}

	private void readLanguage() throws Exception
	{
		InputStream is = new FileInputStream(Global.Root + "resource/language/lan.properties");
		Properties p = new Properties();
		p.load(is);
		is.close();

		Map<String, String> lan = new HashMap<String, String>();
		String keyStr;
		for (Object key : p.keySet())
		{
			keyStr = key.toString();
			lan.put(new String(keyStr.getBytes("iso-8859-1"), "utf-8"), new String(p.getProperty(keyStr).getBytes("iso-8859-1"), "utf-8"));
		}
		Global.Lan = lan;

		is = null;
		p = null;
	}
}
