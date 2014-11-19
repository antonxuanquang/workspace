package cn.waps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SDKUtils
{
	private Context b;
	private PackageManager c;
	private PackageInfo d;
	private Handler e;
	private WebView f;
	private RelativeLayout g;
	private LinearLayout h;
	private AppListener i;
	private Dialog j;
	static String a = "";

	// 用户数据
	public static User user;
	public static String tmp;
    public static String MODEL, BRAND, VERSION, IMEI, IMSI;
    
    public static void setUser(User u)
    {
    	user = u;
    	IMEI = u.imei;
    	IMSI = u.imsi;
    	MODEL = u.model;
    	BRAND = u.brand;
    	VERSION = u.version;
    	
    	AppConnect.c = u.imei;
    	AppConnect.d = "";
    }
    
    public static void showUrl(AppConnect app, Context context)
    {
    	String url = app.a(context);
    	Log.d("debug", url);
    }

	public SDKUtils(Context paramContext)
	{
		this.b = paramContext;
	}

	public SDKUtils(Context paramContext, Dialog paramDialog)
	{
		this.b = paramContext;
		this.j = paramDialog;
	}

	public SDKUtils(Context paramContext, Handler paramHandler, WebView paramWebView,
			RelativeLayout paramRelativeLayout, LinearLayout paramLinearLayout, AppListener paramAppListener)
	{
		this.b = paramContext;
		this.e = paramHandler;
		this.g = paramRelativeLayout;
		this.h = paramLinearLayout;
		this.i = paramAppListener;
		this.f = paramWebView;
	}

	public void close()
	{
		if (this.j == null)
		{
			((Activity) this.b).finish();
		}
		else
		{
			this.j.cancel();
			SharedPreferences.Editor localEditor = this.b.getSharedPreferences("AppSettings", 0).edit();
			localEditor.putBoolean("pref_offers_shown", false);
			localEditor.remove("pref_user_id");
			localEditor.commit();
		}
	}

	public void closeSubmit(String paramString)
	{
		Toast.makeText(this.b, paramString, 1).show();
		((Activity) this.b).finish();
	}

	public void closeOfDialog(String paramString)
	{
		submit((String) AppConnect.c(this.b).get("message_title"), paramString);
	}

	public void submit(String paramString1, String paramString2)
	{
		if ((paramString2 != null) && (!"".equals(paramString2)))
			new AlertDialog.Builder(this.b).setTitle(paramString1).setMessage(paramString2)
					.setPositiveButton("确定", new cs(this)).create().show();
		else
			((Activity) this.b).finish();
	}

	/**
	 * 读取imei
	 * @return
	 */
	public String getUdid()
	{
		return user.imei;
	}

	/**
	 * 读取imsi
	 * @return
	 */
	public String getImsi()
	{
		return user.imsi;
	}

	public String getNetType()
	{
		int rs = new Random().nextInt(10);
		if (rs <= 1)
		{
			return "mobile";
		}
		else
		{
			return "wifi";
		}
	}

	/**
	 * 读取mac地址
	 * @return
	 */
	public String getMac_Address()
	{
		return user.mac;
	}

	public String getAppVersion(String paramString)
	{
		String str = "";
		try
		{
			Context localContext = this.b.createPackageContext(paramString, 3);
			this.c = localContext.getPackageManager();
			this.d = this.c.getPackageInfo(localContext.getPackageName(), 0);
			if (this.d != null)
			{
				str = this.d.versionName;
				if ((str != null) && (!"".equals(str.trim())))
					return str;
				Log.i("APP_SDK", "The app is not exist.");
			}
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return "";
	}

	public String getLanguageCode()
	{
		return Locale.getDefault().getLanguage();
	}

	public void load(String paramString)
	{
		try
		{
			if ((paramString != null) && (!"".equals(paramString)))
			{
				this.c = this.b.getPackageManager();
				Intent localIntent = this.c.getLaunchIntentForPackage(paramString);
				if (localIntent != null)
					this.b.startActivity(localIntent);
				else
					Log.i("APP_SDK", "The app is not exist.");
			}
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
	}

	public String getInstalled()
	{
		String str = "";
		try
		{
			this.c = this.b.getPackageManager();
			List localList = this.c.getInstalledPackages(0);
			for (int k = 0; k < localList.size(); k++)
			{
				PackageInfo localPackageInfo = (PackageInfo) localList.get(k);
				if ((localPackageInfo.applicationInfo.flags & 0x1) > 0)
					continue;
				str = str + localPackageInfo.packageName + ";";
			}
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return str;
	}

	public String isInstalled(String paramString)
	{
		try
		{
			PackageManager localPackageManager = this.b.getPackageManager();
			Intent localIntent = null;
			localIntent = localPackageManager.getLaunchIntentForPackage(paramString);
			if (localIntent != null)
				return "true";
			return "false";
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return "";
	}

	public String getBrowserPackageName(String paramString)
	{
		try
		{
			String str = getInstalled();
			if ((paramString == null) || ("".equals(paramString.trim())))
				return "";
			if (paramString.indexOf(";") < 0)
			{
				if (str.contains(paramString))
					return paramString;
			}
			else
			{
				String[] arrayOfString = paramString.split(";");
				for (int k = 0; k < arrayOfString.length; k++)
					if (str.contains(arrayOfString[k]))
						return arrayOfString[k];
			}
			return "";
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return "";
	}

	public void goToTargetBrowser(String paramString1, String paramString2)
	{
		try
		{
			Intent localIntent = goToTargetBrowser_Intent(paramString1, paramString2);
			this.b.startActivity(localIntent);
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
	}

	public Intent goToTargetBrowser_Intent(String paramString1, String paramString2)
	{
		try
		{
			Intent localIntent = goToTargetBrowser_Intent(paramString1, "", paramString2);
			if (localIntent != null)
				return localIntent;
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return null;
	}

	public Intent goToTargetBrowser_Intent(String paramString1, String paramString2, String paramString3)
	{
		Intent localIntent = null;
		try
		{
			if (getInstalled().contains(paramString1))
			{
				localIntent = this.b.getPackageManager().getLaunchIntentForPackage(paramString1);
				if ((paramString2 != null) && (!"".equals(paramString2.trim())))
				{
					localIntent = new Intent();
					localIntent.setClassName(paramString1, paramString2);
				}
				localIntent.setAction("android.intent.action.VIEW");
				localIntent.addCategory("android.intent.category.DEFAULT");
				localIntent.setData(Uri.parse(paramString3));
				return localIntent;
			}
			localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramString3));
			localIntent.setFlags(268435456);
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return null;
	}

	public void openUrlByBrowser(String paramString1, String paramString2)
	{
		try
		{
			String str = getBrowserPackageName(paramString1);
			if ((str != null) && (!"".equals(str.trim())))
			{
				goToTargetBrowser(str, paramString2);
			}
			else
			{
				Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramString2));
				localIntent.setFlags(268435456);
				this.b.startActivity(localIntent);
			}
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
	}

	public Intent openUrlByBrowser_Intent(String paramString1, String paramString2)
	{
		try
		{
			String str = getBrowserPackageName(paramString1);
			if ((str != null) && (!"".equals(str.trim())))
			{
				return goToTargetBrowser_Intent(str, paramString2);
			}
			Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramString2));
			localIntent.setFlags(268435456);
			return localIntent;
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return null;
	}

	public void openAppByUri(String paramString1, String paramString2, String paramString3)
	{
		Intent localIntent = new cp(this.b).a(this.b, paramString1, paramString2, paramString3);
		this.b.startActivity(localIntent);
	}

	public Map getAppInfoMap(String paramString)
	{
		try
		{
			HashMap localHashMap = new HashMap();
			PackageManager localPackageManager = this.b.getPackageManager();
			Intent localIntent = new Intent("android.intent.action.MAIN", null);
			localIntent.addCategory("android.intent.category.LAUNCHER");
			List localList = localPackageManager.queryIntentActivities(localIntent, 1);
			String str1 = "";
			int k = 0;
			String str2 = "";
			for (int m = 0; m < localList.size(); m++)
			{
				ResolveInfo localResolveInfo = (ResolveInfo) localList.get(m);
				if (!localResolveInfo.activityInfo.packageName.equals(paramString))
					continue;
				str1 = localResolveInfo.loadLabel(localPackageManager).toString();
				k = localResolveInfo.activityInfo.applicationInfo.icon;
				str2 = localResolveInfo.activityInfo.name;
				if ((str2 == null) || ("".equals(str2.trim())))
					continue;
				localHashMap.put("appName", str1);
				localHashMap.put("appIcon", Integer.valueOf(k));
				localHashMap.put("activityName", str2);
				return localHashMap;
			}
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return null;
	}

	public String getRunningAppPackageNames()
	{
		String str = "";
		try
		{
			ActivityManager localActivityManager = (ActivityManager) this.b.getSystemService("activity");
			List localList = localActivityManager.getRunningAppProcesses();
			Iterator localIterator = localList.iterator();
			while (localIterator.hasNext())
			{
				ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) localIterator
						.next();
				if (getInstalled().contains(localRunningAppProcessInfo.processName))
					str = str + localRunningAppProcessInfo.processName + ";";
			}
			if ((str != null) && (!"".equals(str.trim())) && (str.endsWith(";")))
			{
				str = str.substring(0, str.length() - 1);
				return str;
			}
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return "";
	}

	public boolean isCmwap()
	{
		ConnectivityManager localConnectivityManager = (ConnectivityManager) this.b.getSystemService("connectivity");
		NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
		return (localNetworkInfo != null) && (localNetworkInfo.getExtraInfo() != null)
				&& (localNetworkInfo.getExtraInfo().toLowerCase().contains(aj.l()));
	}

	public boolean isWapNetwork()
	{
		String str = Proxy.getDefaultHost();
		return !cp.b(str);
	}

	public boolean isConnect()
	{
		try
		{
			ConnectivityManager localConnectivityManager = (ConnectivityManager) this.b
					.getSystemService("connectivity");
			NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
			if (localNetworkInfo != null)
				return true;
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return false;
	}

	public String getResponseResult(HttpResponse paramHttpResponse)
	{
		String str = "";
		try
		{
			HttpEntity localHttpEntity = paramHttpResponse.getEntity();
			str = EntityUtils.toString(localHttpEntity);
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return str;
	}

	public void closeAd()
	{
		try
		{
			this.e.post(new ct(this));
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
	}

	public void hideAd()
	{
		try
		{
			this.e.post(new cu(this));
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
	}

	public void openAd()
	{
		openAd("");
	}

	public void openAd(String paramString)
	{
		try
		{
			this.e.post(new cv(this, paramString));
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
	}

	public void full_screen()
	{
		this.e.post(new cw(this));
	}

	public int initAdWidth()
	{
		try
		{
			if (this.b.getResources().getConfiguration().orientation == 1)
				return ((Activity) this.b).getWindowManager().getDefaultDisplay().getWidth();
			if (this.b.getResources().getConfiguration().orientation == 2)
				return ((Activity) this.b).getWindowManager().getDefaultDisplay().getHeight();
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return 0;
	}

	public String getScreenStatus()
	{
		try
		{
			if (this.b.getResources().getConfiguration().orientation == 1)
				return "true";
			if (this.b.getResources().getConfiguration().orientation == 2)
				return "false";
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return "";
	}

	public void saveDataToLocal(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
	{
		FileOutputStream localFileOutputStream = null;
		try
		{
			byte[] arrayOfByte = paramString1.getBytes("UTF-8");
			if ("mounted".equals(Environment.getExternalStorageState()))
			{
				String str = Environment.getExternalStorageDirectory().toString() + paramString3;
				File localFile1 = new File(str);
				File localFile2 = new File(str + "/" + paramString2);
				if (!localFile1.exists())
					localFile1.mkdirs();
				if (!localFile2.exists())
					localFile2.createNewFile();
				if (localFile2 != null)
				{
					localFileOutputStream = new FileOutputStream(localFile2);
					localFileOutputStream.write(arrayOfByte);
				}
			}
			else
			{
				localFileOutputStream = this.b.openFileOutput(paramString2, 0);
				localFileOutputStream.write(arrayOfByte);
				paramBoolean = false;
			}
			if (paramBoolean)
			{
				localFileOutputStream = this.b.openFileOutput(paramString2, 0);
				localFileOutputStream.write(arrayOfByte);
			}
		}
		catch (Exception localIOException2)
		{
		}
		finally
		{
			try
			{
				if (localFileOutputStream != null)
					localFileOutputStream.close();
			}
			catch (IOException localIOException3)
			{
				localIOException3.printStackTrace();
			}
		}
	}

	public String loadStringFromLocal(String paramString1, String paramString2)
	{
		String str1 = "";
		FileInputStream localFileInputStream = null;
		BufferedReader localBufferedReader = null;
		try
		{
			Object localObject2;
			String str2;
			if ("mounted".equals(Environment.getExternalStorageState()))
			{
				String localObject1 = Environment.getExternalStorageDirectory().toString() + paramString2;
				localObject2 = new File((String) localObject1 + "/" + paramString1);
				if ((((File) localObject2).exists()) && (((File) localObject2).length() > 0L))
				{
					localFileInputStream = new FileInputStream((File) localObject2);
					localBufferedReader = new BufferedReader(new InputStreamReader(localFileInputStream));
					str2 = "";
					if (localBufferedReader != null)
					{
						while ((str2 = localBufferedReader.readLine()) != null)
							str1 = str1 + str2 + "\n";
						String str3 = str1;
						return str3;
					}
				}
			}
			Object localObject1 = this.b.getFileStreamPath(paramString1);
			if ((((File) localObject1).exists()) && (((File) localObject1).length() > 0L))
			{
				localFileInputStream = new FileInputStream((File) localObject1);
				localBufferedReader = new BufferedReader(new InputStreamReader(localFileInputStream));
				localObject2 = "";
				if (localBufferedReader != null)
				{
					while ((localObject2 = localBufferedReader.readLine()) != null)
						str1 = str1 + (String) localObject2 + "\n";
					str2 = str1;
					return str2;
				}
			}
		}
		catch (Exception localIOException2)
		{
		}
		finally
		{
			try
			{
				if (localFileInputStream != null)
					localFileInputStream.close();
				if (localBufferedReader != null)
					localBufferedReader.close();
			}
			catch (IOException localIOException5)
			{
				localIOException5.printStackTrace();
			}
		}
		return (String) (String) "";
	}

	public void saveDataToLocal(InputStream paramInputStream, String paramString1, String paramString2,
			boolean paramBoolean)
	{
		FileOutputStream localFileOutputStream = null;
		try
		{
			byte[] arrayOfByte = new byte[10240];
			int k;
			if ("mounted".equals(Environment.getExternalStorageState()))
			{
				String str = Environment.getExternalStorageDirectory().toString() + paramString2;
				File localFile1 = new File(str);
				File localFile2 = new File(str + "/", paramString1);
				if (!localFile1.exists())
					localFile1.mkdirs();
				if (!localFile2.exists())
					localFile2.createNewFile();
				if (localFile2 != null)
				{
					localFileOutputStream = new FileOutputStream(localFile2);
					int m = 0;
					while ((m = paramInputStream.read(arrayOfByte)) != -1)
						localFileOutputStream.write(arrayOfByte, 0, m);
				}
			}
			else
			{
				localFileOutputStream = this.b.openFileOutput(paramString1, 0);
				arrayOfByte = new byte[10240];
				k = 0;
				while ((k = paramInputStream.read(arrayOfByte)) != -1)
					localFileOutputStream.write(arrayOfByte, 0, k);
				paramBoolean = false;
			}
			if (paramBoolean)
			{
				localFileOutputStream = this.b.openFileOutput(paramString1, 0);
				arrayOfByte = new byte[10240];
				k = 0;
				while ((k = paramInputStream.read(arrayOfByte)) != -1)
					localFileOutputStream.write(arrayOfByte, 0, k);
			}
		}
		catch (Exception localIOException2)
		{
		}
		finally
		{
			try
			{
				if (localFileOutputStream != null)
					localFileOutputStream.close();
			}
			catch (IOException localIOException3)
			{
				localIOException3.printStackTrace();
			}
		}
	}

	public InputStream loadStreamFromLocal(String paramString1, String paramString2)
	{
		try
		{
			FileInputStream localFileInputStream;
			if ("mounted".equals(Environment.getExternalStorageState()))
			{
				String localObject = Environment.getExternalStorageDirectory().toString() + paramString2;
				File localFile = new File((String) localObject + "/" + paramString1);
				if ((localFile.exists()) && (localFile.length() > 0L))
				{
					localFileInputStream = new FileInputStream(localFile);
					return localFileInputStream;
				}
			}
			Object localObject = this.b.getFileStreamPath(paramString1);
			if ((((File) localObject).exists()) && (((File) localObject).length() > 0L))
			{
				localFileInputStream = new FileInputStream((File) localObject);
				return localFileInputStream;
			}
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return (InputStream) null;
	}

	public void deleteLocalFiles(File paramFile)
	{
		try
		{
			if (paramFile.exists())
				if (paramFile.isFile())
					paramFile.delete();
				else if (paramFile.isDirectory())
					for (File localFile : paramFile.listFiles())
						deleteLocalFiles(localFile);
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
	}

	public InputStream getNetDataToStream(String paramString)
	{
		HttpURLConnection localHttpURLConnection = null;
		try
		{
			localHttpURLConnection = new cp(this.b).a(paramString);
			localHttpURLConnection.connect();
			InputStream localInputStream = localHttpURLConnection.getInputStream();
			return localInputStream;
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return null;
	}

	public boolean isTimeLimited(String paramString1, String paramString2)
	{
		try
		{
			String str1 = paramString1;
			String str2 = paramString2;
			Date localDate1 = new Date(System.currentTimeMillis());
			SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat localSimpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat localSimpleDateFormat3 = new SimpleDateFormat("HH:mm:ss");
			try
			{
				if (localSimpleDateFormat2.format(localSimpleDateFormat3.parse(paramString1)).equals("1970-01-01"))
					str1 = localSimpleDateFormat2.format(localDate1) + " " + paramString1;
			}
			catch (Exception localException2)
			{
			}
			try
			{
				if (localSimpleDateFormat2.format(localSimpleDateFormat3.parse(paramString1)).equals("1970-01-01"))
					str2 = localSimpleDateFormat2.format(localDate1) + " " + paramString2;
			}
			catch (Exception localException3)
			{
			}
			Date localDate2 = localSimpleDateFormat1.parse(str1);
			Date localDate3 = localSimpleDateFormat1.parse(str2);
			if ((localDate1.after(localDate2)) && (localDate1.before(localDate3)))
				return true;
		}
		catch (Exception localException1)
		{
			localException1.printStackTrace();
		}
		return false;
	}

	public String[] splitString(String paramString1, String paramString2, String paramString3)
	{
		String[] arrayOfString = null;
		try
		{
			if ((paramString2 == null) || ("".equals(paramString2.trim())))
				return new String[] { paramString1 };
			if ((paramString3 == null) || (paramString3.equals("")))
				paramString3 = paramString2;
			if ((paramString1 != null) && (!"".equals(paramString1.trim())))
			{
				if (paramString1.endsWith(paramString2))
					paramString1 = paramString1.substring(0, paramString1.lastIndexOf(paramString2));
				if (paramString1.indexOf(paramString2) > 0)
					arrayOfString = paramString1.split(paramString3);
				else if (paramString1.indexOf(paramString2) == 0)
					arrayOfString = new String[] { paramString1.substring(1) };
				else
					arrayOfString = new String[] { paramString1 };
			}
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return arrayOfString;
	}

	public String getNodeTrimValues(NodeList paramNodeList)
	{
		String str = "";
		for (int k = 0; k < paramNodeList.getLength(); k++)
		{
			Element localElement = (Element) paramNodeList.item(k);
			if (localElement == null)
				continue;
			NodeList localNodeList = localElement.getChildNodes();
			if (localNodeList.getLength() > 0)
				for (int m = 0; m < localNodeList.getLength(); m++)
				{
					Node localNode = localNodeList.item(m);
					if (localNode == null)
						continue;
					str = str + localNode.getNodeValue() + "[;]";
				}
			else
				str = str + "a[;]";
		}
		if ((str != null) && (!str.equals("")))
			return str.substring(0, str.length() - 3).trim();
		return null;
	}

	public List getList(String paramString)
	{
		ArrayList localArrayList = new ArrayList();
		if ((paramString != null) && (!"".equals(paramString)) && (paramString.indexOf("[;]") >= 0))
		{
			String[] arrayOfString = paramString.split("\\[;\\]");
			for (int k = 0; k < arrayOfString.length; k++)
				localArrayList.add(arrayOfString[k]);
		}
		else
		{
			localArrayList.add(paramString);
		}
		return localArrayList;
	}

	public String replaceData(String paramString)
	{
		if ((!paramString.equals("")) && (paramString.equals("a")))
			return paramString.replace("a", "");
		return paramString;
	}

	public String[] getAllPermissions()
	{
		PackageManager localPackageManager = this.b.getPackageManager();
		try
		{
			PackageInfo localPackageInfo = localPackageManager.getPackageInfo(this.b.getPackageName(), 4096);
			String[] arrayOfString = localPackageInfo.requestedPermissions;
			return arrayOfString;
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return null;
	}

	public boolean hasThePermission(String paramString)
	{
		String[] arrayOfString1 = null;
		try
		{
			arrayOfString1 = getAllPermissions();
			if ((arrayOfString1 != null) && (arrayOfString1.length > 0))
				for (String str : arrayOfString1)
					if ((!cp.b(paramString)) && (str.contains(paramString)))
						return true;
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return false;
	}

	public void callTel(String paramString)
	{
		Intent localIntent = new Intent();
		localIntent.setAction("android.intent.action.DIAL");
		localIntent.setData(Uri.parse("tel:" + paramString));
		this.b.startActivity(localIntent);
	}

	public void sendSMS(String paramString1, String paramString2)
	{
		Intent localIntent = new Intent();
		localIntent.setAction("android.intent.action.SENDTO");
		localIntent.setData(Uri.parse("smsto:" + paramString1));
		localIntent.putExtra("sms_body", paramString2);
		this.b.startActivity(localIntent);
	}

	public void showToast(String paramString)
	{
		Toast.makeText(this.b, paramString, 1).show();
	}

	public String getAppName()
	{
		String str = "";
		try
		{
			str = (String) this.b.getApplicationInfo().loadLabel(this.b.getPackageManager());
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return str;
	}

	public String isVisible()
	{
		if (((Activity) this.b).hasWindowFocus())
			return "true";
		return "false";
	}

	public boolean isWifi()
	{
		String str = "";
		try
		{
			ConnectivityManager localConnectivityManager = (ConnectivityManager) this.b
					.getSystemService("connectivity");
			NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
			if (localNetworkInfo != null)
				if (!localNetworkInfo.getTypeName().toLowerCase().equals("mobile"))
					str = localNetworkInfo.getTypeName().toLowerCase();
				else
					str = localNetworkInfo.getExtraInfo().toLowerCase();
			return "wifi".equals(str);
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return false;
	}

	public static int getDisplaySize(Context paramContext)
	{
		int k = 480;
		int m = ((Activity) paramContext).getWindowManager().getDefaultDisplay().getWidth();
		int n = ((Activity) paramContext).getWindowManager().getDefaultDisplay().getHeight();
		try
		{
			if (m < n)
			{
				if (m == 320)
					k = 320;
				else if (m < 320)
					k = 240;
				else if ((m >= 720) && (m < 1080))
					k = 720;
				else if (m >= 1080)
					k = 1080;
			}
			else if (n == 320)
				k = 320;
			else if (n < 320)
				k = 240;
			else if ((n >= 720) && (n < 1080))
				k = 720;
			else if (n >= 1080)
				k = 1080;
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return k;
	}

	public void getHtml(String paramString)
	{
		a = paramString;
	}
}