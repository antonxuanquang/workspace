package com.sean.im.server.context;

import com.sean.persist.core.DataSourceProvider;
import com.sean.persist.entity.DataSourceEntity;
import com.sean.persist.enums.DataBaseType;
import com.sean.persist.enums.ProviderType;

public class IMDataSource extends DataSourceProvider
{
	@Override
	public DataSourceEntity providerDs()
	{
		ProviderType providerType = ProviderType.Custom;
		DataBaseType dbType = DataBaseType.MySQL;
		String hostname = "localhost";
		int port = 3306;
		String dbName = "im_db";
		String user = "root";
		String password = "xuxu";
		String jndi = "java:comp/env/jdbc/im";

		return new DataSourceEntity(providerType, dbType, hostname, port, dbName, user, password, jndi, true);
	}

	@Override
	public DataSourceEntity providerUnitTestDs()
	{
		ProviderType providerType = ProviderType.Custom;
		DataBaseType dbType = DataBaseType.MySQL;
		String hostname = "localhost";
		int port = 3306;
		String dbName = "microtask_db";
		String user = "root";
		String password = "xuxu";
		String jndi = "java:comp/env/jdbc/microtask";

		return new DataSourceEntity(providerType, dbType, hostname, port, dbName, user, password, jndi, true);
	}
}
