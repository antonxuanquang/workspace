package cn.waps;

public class AppConnectUtil
{
	public static void closeAppConnect(AppConnect app)
	{
		if (app != null)
		{
			if (app.m != null)
			{
				app.m.dismiss();
			}
			app.close();
		}
	}
}
