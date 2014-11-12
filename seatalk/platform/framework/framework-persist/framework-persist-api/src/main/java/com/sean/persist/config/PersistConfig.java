package com.sean.persist.config;

import com.sean.config.core.Config;
import com.sean.config.enums.ConfigEnum;

public class PersistConfig
{
	public static String PacketNames;

	static
	{
		PacketNames = Config.getProperty(ConfigEnum.PersistPackagePrefix);
	}
}
