package com.sean.service.config;

import com.sean.config.core.Config;
import com.sean.config.enums.ConfigEnum;
import com.sean.service.core.ServletContextListener;

/**
 * 架构全局配置
 * @author sean
 */
public final class ServiceConfig
{
	public static String ProjectName;
	public static String PackgeNames;
	public static String SessionHost;
	public static int SessionPort;
	public static String UserCenterHost;
	public static int UserCenterPort;
	static
	{
		ProjectName = Config.getProperty(ConfigEnum.ProjectName);
		PackgeNames = Config.getProperty(ConfigEnum.ServicePackageProfix);
		ServletContextListener.showStartInfo();
	}

	/**
	 * 运行模式，只能有一种为true
	 */
	public static final boolean RunningMode_Develop = true;// 开发模式
	public static final boolean RunningMode_Server = false;// 服务器运行模式
	public static final boolean RunningMode_Pseudo = false;// 伪实现模式

	public static String getRunningMode()
	{
		if (RunningMode_Develop)
		{
			return "Develop";
		}
		else if (RunningMode_Pseudo)
		{
			return "Pseudo";
		}
		else if (RunningMode_Server)
		{
			return "Server";
		}
		else
		{
			return "";
		}
	}
}
