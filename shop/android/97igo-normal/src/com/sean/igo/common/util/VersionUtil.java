package com.sean.igo.common.util;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class VersionUtil
{
	public static String getVersionName(Context context)
	{
		try
		{
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionName;
		}
		catch (Exception e)
		{
			return "读取版本错误";
		}
	}

	public static int getVersionCode(Context context)
	{
		try
		{
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionCode;
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	@SuppressLint("NewApi")
	public static void download(String apkurl, Context context)
	{
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

		String url = apkurl;
		Uri resource = Uri.parse(url);
		Request request = new Request(resource);
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton(); //获取文件类型实例
		String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url)); //获取文件类型
		request.setMimeType(mimeString); //制定下载文件类型
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

		request.setTitle("bigcode.apk");
		request.setDescription("正在下载代码大爆炸");
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setVisibleInDownloadsUi(true);

		int index = url.lastIndexOf("/");
		String fname = url.substring(index + 1); //获取文件名

		request.setDestinationInExternalPublicDir("/", fname); //制定下载的目录里
		downloadManager.enqueue(request);
		Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
	}
}
