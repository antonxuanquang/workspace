package com.sean.igo.common.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 网络工具
 * @author sean
 */
public class NetworkUtil
{
	/**
	 * 判断是否为wifi
	 * @param activitiy
	 * @return
	 */
	public static boolean checkWifi(Activity activitiy)
	{
		WifiManager mWifiManager = (WifiManager) activitiy.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
		if (mWifiManager.isWifiEnabled() && ipAddress != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
