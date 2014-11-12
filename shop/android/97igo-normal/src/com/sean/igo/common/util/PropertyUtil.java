package com.sean.igo.common.util;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

public class PropertyUtil
{
	// 用户目录
	public static final String usrdir = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/bigcodebang/user/";
	// 客户端与服务端时间差
	private static long diffTime = 0;
	// 服务器的用户ID
	public static String UId = "";
	public static String SId = "";

	// 用户信息key
	public static final String User_AccessToken = "accessToken";
	public static final String User_SinaUId = "uid";
	public static final String User_Name = "name";
	public static final String User_Descr = "descr";
	public static final String User_Region = "region";
	public static final String User_Sex = "sex";
	public static final String User_HeadUrl = "headUrl";
	public static final String User_ZfbUser = "zfb_user";
	public static final String User_ZfbName = "zfb_name";

	// 系统信息key
	public static final String Sys_FirstTimeUse = "firstTimeUse"; // 第一次使用系统标识
	public static final String Sys_AutoPlayInterval = "autoPlayInterval"; // 自动播放间隔
	public static final String Sys_LastScoreUpdateTime = "LastScoreUpdateTime";// 上次更新积分时间
	public static final String Sys_LatestVersionName = "LatestVersionName";// 最新版本
	public static final String Sys_LatestVersionCode = "LatestVersionCode";// 最新版本
	public static final String Sys_ApkUrl = "apkUrl";// apk下载地址
	
	public static final String Sys_ServicePrice = "servicePrice";// 天天特价服务

	/**
	 * 写入用户信息
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setUserProp(Context context, String key, String value)
	{
		SharedPreferences props = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = props.edit();
		if (value == null)
		{
			editor.remove(key);
			editor.commit();
			Log.d("debug", "删除用户property:" + key);
			return;
		}
		String val = SecurityUtil.byteToHexString(SecurityUtil.encryptByDes(value, "4102gnab"));
		editor.putString(key, val);
		editor.commit();
		Log.d("debug", "添加用户property:" + key + "-" + val);
	}

	/**
	 * 读取用户信息
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getUserProp(Context context, String key, String defValue)
	{
		SharedPreferences props = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		String encrypt = props.getString(key, null);
		Log.d("debug", "读取用户加密property:" + key + "-" + encrypt);
		if (encrypt != null && encrypt.length() > 0)
		{
			String value = SecurityUtil.dencryptByDes(encrypt, "4102gnab");
			Log.d("debug", "读取用户property:" + key + "-" + value);
			return value;
		}
		return defValue;
	}

	/**
	 * 写入系统信息
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setSysProp(Context context, String key, String value)
	{
		SharedPreferences props = context.getSharedPreferences("system", Context.MODE_PRIVATE);
		Editor editor = props.edit();
		if (value == null)
		{
			editor.remove(key);
			editor.commit();
			Log.d("debug", "删除系统property:" + key);
			return;
		}
		editor.putString(key, value);
		editor.commit();
		Log.d("debug", "添加系统property:" + key + "-" + value);
	}

	/**
	 * 读取系统信息
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getSysProp(Context context, String key, String defValue)
	{
		SharedPreferences props = context.getSharedPreferences("system", Context.MODE_PRIVATE);
		return props.getString(key, defValue);
	}

	/**
	 * 判断用户是否登录
	 * @return
	 */
	public static boolean isSignIn(Context context)
	{
		String accessToken = getUserProp(context, User_AccessToken, null);
		String uid = getUserProp(context, User_SinaUId, null);
		if (accessToken != null && uid != null)
		{
			return true;
		}
		return false;
	}

	/**
	 * 注销
	 * @param context
	 */
	public static void signout(Context context)
	{
		SharedPreferences props = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = props.edit();
		editor.clear();
		editor.commit();

		UId = null;
		SId = null;

		File info = new File(usrdir + "head.png");
		if (info.exists())
		{
			info.delete();
		}
	}

	public static synchronized long getDiffTime()
	{
		return diffTime;
	}

	public static synchronized void setDiffTime(long dt)
	{
		diffTime = dt;
	}
}
