package com.sean.im.console.main;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.UIManager;

import com.sean.im.commom.core.HttpUtil;
import com.sean.im.console.constant.Global;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		new Main().start(null);	
	}
	
	public void start(String root) throws Exception
	{
		// 赋值软件根路径
		if (root != null)
		{
			Global.Root = root;
		}

		// 设置皮肤
		try
		{
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Microsoft Yahei", Font.PLAIN, 12));
		}
		catch (Exception e)
		{
		}
		
		// 读取服务器信息
		InputStream is = new FileInputStream(Global.Root + "/config/conf.properties");
		Properties p = new Properties();
		p.load(is);
		is.close();
		
		HttpUtil.ServerHost = p.getProperty("server_host");
		HttpUtil.ServerPort = Integer.parseInt(p.getProperty("server_port"));
		HttpUtil.Url = "http://" + HttpUtil.ServerHost + ":" + HttpUtil.ServerPort + "/im-web/";
		
		new ConsoleForm().setVisible(true);
	}
}
