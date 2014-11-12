package com.sean.im.client.push.handler;

import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.core.Protocol;

/**
 * 强制下线
 * @author sean
 */
public class ExitHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		int exitType = notify.getIntParameter("exitType");
		// 服务器强制下线
		if (exitType == 1)
		{
			try
			{
				ApplicationContext.Client.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			UIUtil.alert(null, Global.Lan.get("与服务器链接失败，请重新登录"));
			System.exit(0);
		}
		// 帐号在别处登录
		else if (exitType == 2)
		{
			try
			{
				ApplicationContext.Client.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			UIUtil.alert(null, Global.Lan.get("帐号在别处登录，请重新登录或修改密码"));
			System.exit(0);
		}
	}
}
