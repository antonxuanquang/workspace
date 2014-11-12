package com.sean.log.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerRepository;

/**
 * logger factory
 * @author sean
 */
public class LogFactory
{
	private static PropertyConfigurator pc = new PropertyConfigurator();
	private static Map<String, Logger> container = new HashMap<String, Logger>();

	public static Logger getLogger(String name)
	{
		Logger logger = container.get(name);
		if (logger == null)
		{
			try
			{
				logger = Logger.getLogger(name);
				
				Properties p = new Properties();
				p.load(LogFactory.class.getResourceAsStream("/log_local.properties"));
				p.setProperty("log4j.appender.fixed_size_file.File", "./logs/" + name + "/log");
				
				LoggerRepository lr1 = new Hierarchy(logger);
				pc.doConfigure(p, lr1);
				
				container.put(name, logger);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return logger;
	}

	/**
	 * get framework logger
	 * @return
	 */
	public static Logger getFrameworkLogger()
	{
		return getLogger("framework");
	}
}
