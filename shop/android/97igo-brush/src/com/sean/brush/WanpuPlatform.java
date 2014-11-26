package com.sean.brush;

import java.io.File;
import java.lang.reflect.Field;

import org.apache.commons.io.FileUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;
import cn.waps.AppConnect;
import cn.waps.AppConnectUtil;
import cn.waps.SDKUtils;

public class WanpuPlatform extends Platform
{
	private static AppConnect app;

	@Override
	public void clearCache(Context context)
	{
		// 清空缓存
		String[] cache = new String[] { "appPrefrences", "ShowAdFlag", "AppSettings" };
		for (int i = 0; i < cache.length; i++)
		{
			SharedPreferences props = context.getSharedPreferences(cache[i], Context.MODE_PRIVATE);
			Editor editor = props.edit();
			editor.clear();
			editor.commit();

			Log.d("debug", "万普:删除SharedPreferences " + cache[i]);
		}
	}

	@Override
	public void onUserChanged(Context context, User user) throws Exception
	{
		this.closeOfferwall(context);

//		// 删除已经下载的apk文件
//		String root = Environment.getExternalStorageDirectory().getAbsolutePath();
//		String[] dir = new String[] { "download", "Download" };
//		for (String it : dir)
//		{
//			File download = new File(root + "/" + it);
//			if (download.exists())
//			{
//				FileUtils.deleteDirectory(download);
//				Log.d("debug", "万普删除" + download.getAbsolutePath() + "目录");
//			}
//		}

		// 初始化统计器，并通过代码设置APP_ID, APP_PID
		if (app == null)
		{
			app = AppConnect.getInstance("b04222c03b0afea639a9ff345d73ee27", "waps", context);
			app.cleanCache();
		}

		// 注入数据
		StringBuilder sb = new StringBuilder("\n注入SDK数据:\n");
		// 注入imei
		Field f = app.getClass().getDeclaredField("c");
		f.setAccessible(true);
		f.set(app, SDKUtils.user.imei);
		sb.append("imei : ").append(f.get(app)).append("\n");

		// 注入imsi
		f = app.getClass().getDeclaredField("N");
		f.setAccessible(true);
		f.set(app, SDKUtils.user.imsi);
		sb.append("imsi : ").append(f.get(app)).append("\n");

		// 注入screenWidth
		f = app.getClass().getDeclaredField("I");
		f.setAccessible(true);
		f.set(app, Integer.parseInt(SDKUtils.user.screenWidth));
		sb.append("screenWidth : ").append(f.get(app)).append("\n");

		// 注入screenHeight
		f = app.getClass().getDeclaredField("J");
		f.setAccessible(true);
		f.set(app, Integer.parseInt(SDKUtils.user.screenHeight));
		sb.append("screenHeight : ").append(f.get(app)).append("\n");

		Log.d("debug", sb.toString());

		SDKUtils.showUrl(app, context);

		// 注入SDK
		SDKUtils.setUser(user);
		this.clearCache(context);
		app = AppConnect.getInstance("b04222c03b0afea639a9ff345d73ee27", "waps", context);
		app.cleanCache();

		Log.d("debug", "万普初始化完毕");
	}

	@Override
	public void openOfferwall(Context context)
	{
		if (app != null)
		{
			app.showAppOffers(context);
		}
	}

	@Override
	public void closeOfferwall(Context context)
	{
		AppConnectUtil.closeAppConnect(app);
	}

	@Override
	public void checkPoints(Context context) throws Exception
	{
	}

}
