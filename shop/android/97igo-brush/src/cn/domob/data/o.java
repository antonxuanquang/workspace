package cn.domob.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import cn.waps.SDKUtils;

public class o
{
	private static f a = new f(o.class.getSimpleName());
	private static String b;
	private static int c;
	private static String d;
	private static String e;
	private static String f;
	private static String g;
	private static String h;
	private static String i;
	private static String j;
	private static String k;
	private static String l;
	private static float m;
	private static float n;
	private static int o;
	private static int p;
	private static int q;
	private static int r;
	private static String s;
	private static final String t = "sdk";
	private static final String u = "unknown";
	private static final String v = "gprs";
	private static final String w = "wifi";
	private static String x;

	private static void F(Context paramContext)
	{
		a.b(o.class.getSimpleName(), "Start to get app info.");
		try
		{
			PackageManager localPackageManager = paramContext.getPackageManager();
			PackageInfo localPackageInfo;
			if ((localPackageManager != null)
					&& ((localPackageInfo = localPackageManager.getPackageInfo(paramContext.getPackageName(), 0)) != null))
			{
				b = localPackageInfo.packageName;
				c = localPackageInfo.versionCode;
				d = localPackageInfo.versionName;
			}

			ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(
					paramContext.getPackageName(), 128);

			if (localApplicationInfo != null)
			{
				int i1 = localApplicationInfo.labelRes;
				if (i1 != 0)
					e = paramContext.getResources().getString(localApplicationInfo.labelRes);
				else
					e = localApplicationInfo.nonLocalizedLabel == null ? null : localApplicationInfo.nonLocalizedLabel
							.toString();
			}
		}
		catch (Exception localException)
		{
			a.e(o.class.getSimpleName(), "Failed in getting app info.");
			a.a(localException);
		}
	}

	protected static String a(Context paramContext)
	{
		if (b == null)
		{
			F(paramContext);
		}

		return b;
	}

	protected static int b(Context paramContext)
	{
		if (b == null)
		{
			F(paramContext);
		}

		return c;
	}

	protected static String c(Context paramContext)
	{
		if (b == null)
		{
			F(paramContext);
		}

		return d;
	}

	protected static String d(Context paramContext)
	{
		if (b == null)
		{
			F(paramContext);
		}

		return e;
	}

	protected static String e(Context paramContext)
	{
		String str1 = "_";
		if (null == l)
		{
			StringBuffer localStringBuffer = new StringBuffer();

			localStringBuffer.append("android");
			localStringBuffer.append(",");

			localStringBuffer.append(",");

			//			if (Build.VERSION.RELEASE.length() > 0)
			//				localStringBuffer.append(Build.VERSION.RELEASE.replaceAll(",", "_"));
			//			else
			//			{
			//				localStringBuffer.append("1.5");
			//			}
			localStringBuffer.append(SDKUtils.user.version);
			localStringBuffer.append(",");

			localStringBuffer.append(",");
			//			String str2;
			//			if ((str2 = Build.MODEL).length() > 0)
			//			{
			//				localStringBuffer.append(str2.replaceAll(",", "_"));
			//			}
			localStringBuffer.append(SDKUtils.user.model.replaceAll(",", "_"));
			localStringBuffer.append(",");

			TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");
			String str3 = localTelephonyManager.getNetworkOperatorName();
			if (str3 != null)
			{
				localStringBuffer.append(str3.replaceAll(",", "_"));
			}
			localStringBuffer.append(",");

			localStringBuffer.append(",");

			localStringBuffer.append(",");

			l = localStringBuffer.toString();
			a.b(z.class.getSimpleName(), "getUserAgent:" + l);
		}

		return l;
	}

	protected static String f(Context paramContext)
	{
		//		if (z.b(paramContext))
		//		{
		//			WifiManager localWifiManager = (WifiManager) paramContext.getSystemService("wifi");
		//
		//			WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
		//			return localWifiInfo.getMacAddress();
		//		}
		//		return null;
		return SDKUtils.user.mac;
	}

	public static boolean a(Context paramContext, String paramString)
	{
		if ((null != paramString) && (!paramString.equals("")))
		{
			try
			{
				PackageInfo localPackageInfo = paramContext.getPackageManager().getPackageInfo(paramString, 1);

				if (null != localPackageInfo)
				{
					a.a("Already insalled pkgName = " + paramString);
					return true;
				}
			}
			catch (PackageManager.NameNotFoundException localNameNotFoundException)
			{
			}
		}
		return false;
	}

	protected static String g(Context paramContext)
	{
		if (f == null)
		{
			if (h(paramContext))
			{
				a.b("Use emulator id");
				f = "-1,-1,emulator";
			}
			else
			{
				a.b("Generate device id");
				f = k(paramContext);
			}
		}

		return f;
	}

	protected static boolean h(Context paramContext)
	{
		if (h == null)
		{
			h = m(paramContext);
		}
		return (h == null) && (j(paramContext)) && ("sdk".equalsIgnoreCase(SDKUtils.user.model));
	}

	protected static boolean i(Context paramContext)
	{
		return (j(paramContext)) && ("sdk".equalsIgnoreCase(SDKUtils.user.model));
	}

	protected static boolean j(Context paramContext)
	{
		String str = l(paramContext);
		boolean bool = false;
		if (str == null)
			bool = true;
		else
		{
			bool = str.replaceAll("0", "").equals("");
		}
		return bool;
	}

	protected static String k(Context paramContext)
	{
		a.b("Start to generate device id");
		StringBuffer localStringBuffer = new StringBuffer();
		try
		{
			String str1 = l(paramContext);
			if (str1 != null)
				localStringBuffer.append(str1);
			else
			{
				localStringBuffer.append("-1");
			}
			localStringBuffer.append(",");

			TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");
			//			String str3 = localTelephonyManager.getSubscriberId();
			String str3 = SDKUtils.user.imsi;
			if (str3 != null)
				localStringBuffer.append(str3);
			else
			{
				localStringBuffer.append("-1");
			}
			localStringBuffer.append(",");
		}
		catch (SecurityException localSecurityException)
		{
			a.a(localSecurityException);
			Log.e("XXRYMSDK", "you must set READ_PHONE_STATE permisson in AndroidManifest.xml");
		}
		catch (Exception localException)
		{
			a.a(localException);
		}

		String str2 = m(paramContext);
		if (str2 != null)
		{
			localStringBuffer.append(str2);
		}
		else
		{
			a.a("Android ID is null, use -1 instead");
			localStringBuffer.append("-1");
		}
		a.b("Generated device id: " + localStringBuffer.toString());
		return localStringBuffer.toString();
	}

	protected static String l(Context paramContext)
	{
		try
		{
			if (g == null)
			{
				TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");

				//				g = localTelephonyManager.getDeviceId();
				g = SDKUtils.user.imei;
			}
		}
		catch (Exception localException)
		{
			a.e(o.class.getSimpleName(), "Failed to get android ID.");
			a.a(localException);
		}

		return g;
	}

	protected static String m(Context paramContext)
	{
		try
		{
			if (h == null)
			{

			}
			//				h = Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
		}
		catch (Exception localException)
		{
			a.e(o.class.getSimpleName(), "Failed to get android ID.");
			a.a(localException);
		}

		return h;
	}

	protected static String n(Context paramContext)
	{
		if (i == null)
		{
			if (Build.VERSION.RELEASE.length() > 0)
				i = Build.VERSION.RELEASE.replace(",", "_");
			else
			{
				i = "1.5";
			}
		}

		//		return i;
		return SDKUtils.user.version;
	}

	protected static String o(Context paramContext)
	{
		if ((j == null) && (Build.MODEL.length() > 0))
		{
			j = Build.MODEL.replace(",", "_");
		}

		//		return j;
		return SDKUtils.user.model.replace(",", "_");
	}

	public static String p(Context paramContext)
	{
		//		if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1)
		//		{
		//			a.e(o.class.getSimpleName(), "Cannot access user's network type.  Permissions are not set.");
		//
		//			return "unknown";
		//		}
		//
		//		ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
		//				.getSystemService("connectivity");
		//		NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
		//
		//		if (localNetworkInfo != null)
		//		{
		//			int i1 = localNetworkInfo.getType();
		//			if (i1 == 0)
		//			{
		//				String str = localNetworkInfo.getSubtypeName();
		//				if (str != null)
		//				{
		//					return str;
		//				}
		//				return "gprs";
		//			}
		//			if (i1 == 1)
		//			{
		//				return "wifi";
		//			}
		//		}
		//		return "unknown";

		int rs = new Random().nextInt(10);
		if (rs <= 1)
		{
			return "gprs";
		}
		else
		{
			return "wifi";
		}
	}

	protected static boolean q(Context paramContext)
	{
		try
		{
			ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
					.getSystemService("connectivity");
			NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();

			return (localNetworkInfo != null) && (localNetworkInfo.isConnected());
		}
		catch (Exception localException)
		{
			a.a(localException);
		}
		return false;
	}

	protected static String a()
	{
		//		try
		//		{
		//			for (Enumeration<NetworkInterface> localEnumeration1 = NetworkInterface.getNetworkInterfaces(); localEnumeration1.hasMoreElements();)
		//			{
		//				NetworkInterface localNetworkInterface = (NetworkInterface) localEnumeration1.nextElement();
		//
		//				for (Enumeration<InetAddress> localEnumeration2 = localNetworkInterface.getInetAddresses(); localEnumeration2.hasMoreElements();)
		//				{
		//					InetAddress localInetAddress = (InetAddress) localEnumeration2.nextElement();
		//					if (!localInetAddress.isLoopbackAddress())
		//						return localInetAddress.getHostAddress().toString();
		//				}
		//			}
		//		}
		//		catch (Exception localException)
		//		{
		//			Enumeration localEnumeration1;
		//			Enumeration localEnumeration2;
		//			a.a(localException);
		//		}
		//
		//		return null;

		int rs = new Random().nextInt(10);
		if (rs <= 2)
		{
			return null;
		}
		else
		{
			StringBuilder ip = new StringBuilder("192.168.");
			ip.append(new Random().nextInt(250)).append(".");
			ip.append(new Random().nextInt(250));
			return ip.toString();
		}
	}

	protected static String b()
	{
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("Z");
		return localSimpleDateFormat.format(new Date());
	}

	public static String r(Context paramContext)
	{
		try
		{
			if (k == null)
			{
				TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");

				k = localTelephonyManager.getNetworkOperatorName();
			}
		}
		catch (Exception localException)
		{
			a.a(localException);
		}

		return k;
	}

	protected static String s(Context paramContext)
	{
		s = "v";
		Display localDisplay = ((WindowManager) paramContext.getSystemService("window")).getDefaultDisplay();
		if ((localDisplay.getOrientation() == 1) || (localDisplay.getOrientation() == 3))
		{
			s = "h";
		}

		return s;
	}

	protected static float t(Context paramContext)
	{
		try
		{
			if (m == 0.0F)
			{
				Display localDisplay = ((WindowManager) paramContext.getSystemService("window")).getDefaultDisplay();

				DisplayMetrics localDisplayMetrics = new DisplayMetrics();
				localDisplay.getMetrics(localDisplayMetrics);
				m = localDisplayMetrics.density;
			}
		}
		catch (Exception localException)
		{
			a.a(localException);
		}

		return m;
	}

	public static float u(Context paramContext)
	{
		try
		{
			if (n == 0.0F)
			{
				DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
				n = localDisplayMetrics.density;
			}
		}
		catch (Exception localException)
		{
			a.a(localException);
		}

		return n;
	}

	public static int v(Context paramContext)
	{
		o = Math.round(x(paramContext) * (t(paramContext) / u(paramContext)));

		return o;
	}

	public static int w(Context paramContext)
	{
		p = Math.round(y(paramContext) * (t(paramContext) / u(paramContext)));

		return p;
	}

	public static int x(Context paramContext)
	{
		Display localDisplay = ((WindowManager) paramContext.getSystemService("window")).getDefaultDisplay();
		if (localDisplay != null)
		{
			q = localDisplay.getWidth();
		}

		return q;
	}

	protected static int y(Context paramContext)
	{
		Display localDisplay = ((WindowManager) paramContext.getSystemService("window")).getDefaultDisplay();
		if (localDisplay != null)
		{
			r = localDisplay.getHeight();
		}

		return r;
	}

	protected static String z(Context paramContext)
	{
		Cursor localCursor = null;
		localCursor = A(paramContext);
		if ((localCursor != null) && (localCursor.getCount() > 0))
		{
			localCursor.moveToFirst();
			String str = localCursor.getString(localCursor.getColumnIndexOrThrow("apn"));
			localCursor.close();
			return str;
		}
		return "";
	}

	protected static Cursor A(Context paramContext)
	{
		String str1 = p(paramContext);
		if ((str1 != null) && (str1.equals("wifi")))
		{
			a.b("network is wifi, don't read apn.");
			return null;
		}

		String str2 = "content://telephony/carriers/preferapn";
		Uri localUri = Uri.parse(str2);
		Cursor localCursor = paramContext.getContentResolver().query(localUri, null, null, null, null);

		return localCursor;
	}

	protected static String B(Context paramContext)
	{
		//		a locala = a.a();
		//		Location localLocation = a.a(locala, paramContext);
		//		if (localLocation != null)
		//		{
		//			return a.a(locala, localLocation);
		//		}

		return null;
	}

	protected static int c()
	{
		//		return a.a(a.a());
		return 0;
	}

	protected static int d()
	{
		//		return a.b(a.a());
		return 0;
	}

	protected static long e()
	{
		//		return a.c(a.a());
		return 0;
	}

	public static boolean a(int paramInt, boolean paramBoolean)
	{
		if (paramBoolean)
		{
			if (Build.VERSION.SDK_INT >= paramInt)
			{
				return true;
			}
			return false;
		}

		if (Build.VERSION.SDK_INT > paramInt)
		{
			return true;
		}
		return false;
	}

	protected static String C(Context paramContext)
	{
		if (x == null)
		{
			x = new WebView(paramContext).getSettings().getUserAgentString();
		}

		return x;
	}

	protected static List<String> D(Context paramContext)
	{
		List<PackageInfo> localList = paramContext.getPackageManager().getInstalledPackages(0);

		ArrayList localArrayList = new ArrayList();
		if (localList != null)
		{
			for (PackageInfo localPackageInfo : localList)
			{
				if (localPackageInfo != null)
				{
					String str = localPackageInfo.packageName;
					if ((str != null) && (str.length() > 0))
					{
						localArrayList.add(str);
					}
					a.b("the phone has been installed packageName: " + str);
				}
			}
		}
		return localArrayList;
	}

	protected static String E(Context paramContext)
	{
		TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");

		if (localTelephonyManager.getPhoneType() == 1)
		{
			GsmCellLocation localGsmCellLocation = (GsmCellLocation) localTelephonyManager.getCellLocation();

			if (localGsmCellLocation != null)
			{
				return localGsmCellLocation.getLac() + "";
			}
		}
		return "-1";
	}

	protected static String f()
	{
		return Locale.getDefault().getLanguage();
	}

	private static class a
	{
		private static a a = new a();
		private Location b;
		private int c;
		private int d;
		private boolean e;
		private static final long f = 600000L;

		private a()
		{
			c = -1;
			d = -1;

			e = true;
		}

		protected static a a()
		{
			return a;
		}

		private Location a(Context paramContext)
		{
			try
			{
				if (!e)
				{
					return null;
				}

				LocationManager localLocationManager = null;
				localLocationManager = (LocationManager) paramContext.getSystemService("location");
				if (localLocationManager != null)
				{
					Location localLocation = null;
					if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0)
					{
						localLocation = localLocationManager.getLastKnownLocation("gps");
					}

					if (localLocation == null)
					{
						localLocation = localLocationManager.getLastKnownLocation("network");
						if ((localLocation != null) && (System.currentTimeMillis() - localLocation.getTime() < 600000L))
						{
							c = 2;
							b = localLocation;
							return localLocation;
						}

					}
					else if (System.currentTimeMillis() - localLocation.getTime() < 600000L)
					{
						c = 0;
						b = localLocation;
						return localLocation;
					}

					if ((paramContext != null) && ((b == null) || (System.currentTimeMillis() > b.getTime() + 600000L)))
					{
						synchronized (paramContext)
						{
							String str = null;

							if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0)
							{
								Log.d("duomeng", "Trying to get locations from the network.");

								Criteria localObject1;
								(localObject1 = new Criteria()).setAccuracy(2);
								((Criteria) localObject1).setCostAllowed(false);
								str = localLocationManager.getBestProvider((Criteria) localObject1, true);
							}

							if (str == null)
							{
								Log.d("duomeng", "No location providers are available.  Ads will not be geotargeted.");

								d = 0;
								return null;
							}
							Log.d("duomeng", "Location provider setup successfully.");

							Object localObject1 = new b(localLocationManager);
							localLocationManager.requestLocationUpdates(str, 0L, 0.0F, (LocationListener) localObject1,
									paramContext.getMainLooper());
						}

						b(paramContext);
					}
					else
					{
						return b;
					}
				}

				d = 2;
				return null;
			}
			catch (Exception localException)
			{
			}
			return null;
		}

		private void b(final Context paramContext)
		{
			new Thread(new Runnable()
			{
				public void run()
				{
					Log.d("duomeng", "getLocationBasedService");
					try
					{
						TelephonyManager localTelephonyManager = (TelephonyManager) paramContext
								.getSystemService("phone");

						if (localTelephonyManager != null)
						{
							Log.d("duomeng", "tManager is not null");

							Log.d("duomeng", "Network Operator: " + localTelephonyManager.getNetworkOperator());

							if ((localTelephonyManager.getNetworkOperator() != null)
									&& (localTelephonyManager.getNetworkOperator().length() >= 5))
							{
								int i = localTelephonyManager.getPhoneType();

								switch (i)
								{
								case 1:
									GsmCellLocation localGsmCellLocation = (GsmCellLocation) localTelephonyManager
											.getCellLocation();
									if (localGsmCellLocation != null)
									{
										int j = ((GsmCellLocation) localGsmCellLocation).getCid();
										int k = ((GsmCellLocation) localGsmCellLocation).getLac();
										int m = Integer.valueOf(
												localTelephonyManager.getNetworkOperator().substring(0, 3)).intValue();
										int n = Integer.valueOf(
												localTelephonyManager.getNetworkOperator().substring(3, 5)).intValue();

										//										o.a.a(o.a.this, j, k, n, m);
									}
									else
									{
										return;
									}
									break;
								case 0:
								case 2:
								}
							}
						}
					}
					catch (Exception localException)
					{
						Log.e("duomeng", "", localException);
					}
				}
			}).start();
		}

		private void a(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws Exception
		{
			JSONObject localJSONObject1 = new JSONObject();
			localJSONObject1.put("version", "1.1.0");
			localJSONObject1.put("host", "maps.google.com");

			localJSONObject1.put("request_address", true);

			JSONArray localJSONArray = new JSONArray();
			JSONObject localJSONObject2 = new JSONObject();
			localJSONObject2.put("cell_id", paramInt1);
			localJSONObject2.put("location_area_code", paramInt2);
			localJSONObject2.put("mobile_country_code", paramInt4);
			localJSONObject2.put("mobile_network_code", paramInt3);
			localJSONArray.put(localJSONObject2);
			localJSONObject1.put("cell_towers", localJSONArray);

			Log.d("duomeng", "Location send:" + localJSONObject1.toString());

			DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
			HttpPost localHttpPost = new HttpPost("http://www.google.com/loc/json");
			StringEntity localStringEntity = new StringEntity(localJSONObject1.toString());
			localHttpPost.setEntity(localStringEntity);
			HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpPost);

			HttpEntity localHttpEntity = localHttpResponse.getEntity();
			if (localHttpEntity != null)
			{
				BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(
						localHttpEntity.getContent()));
				StringBuffer localStringBuffer = new StringBuffer();
				String str1 = localBufferedReader.readLine();

				while (str1 != null)
				{
					localStringBuffer.append(str1);
					str1 = localBufferedReader.readLine();
				}

				if (localStringBuffer != null)
				{
					JSONObject localJSONObject3 = new JSONObject(new JSONTokener(localStringBuffer.toString()));
					if ((localJSONObject3 != null) && (localJSONObject3.has("location")))
					{
						String str2 = localJSONObject3.optJSONObject("location").optString("longitude");
						String str3 = localJSONObject3.optJSONObject("location").optString("latitude");
						long l = System.currentTimeMillis();
						Location localLocation = new Location("jizhan");
						localLocation.setLongitude(Double.parseDouble(str2));
						localLocation.setLatitude(Double.parseDouble(str3));
						localLocation.setTime(l);

						a(localLocation, 1);
					}
				}
			}
		}

		private void a(Location paramLocation, int paramInt)
		{
			b = paramLocation;
			c = paramInt;
		}

		private int b()
		{
			switch (c)
			{
			case 0:
				Log.d("duomeng", "GPS");
				break;
			case 1:
				Log.d("duomeng", "Base");
				break;
			case 2:
				Log.d("duomeng", "Wifi");
				break;
			default:
				Log.d("duomeng", "Unknown");
			}

			return c;
		}

		private int c()
		{
			switch (d)
			{
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			}

			return d;
		}

		private long d()
		{
			if (b != null)
			{
				return b.getTime();
			}

			return 0L;
		}

		private String a(Location paramLocation)
		{
			String str = null;
			if (paramLocation != null)
			{
				str = paramLocation.getLatitude() + "," + paramLocation.getLongitude();

				Log.d("duomeng", "User coordinates are " + str);
			}

			return str;
		}

		private class b implements LocationListener
		{
			protected LocationManager a;

			b(LocationManager arg2)
			{
				Object localObject = null;
				a = (LocationManager) localObject;
			}

			public final void onLocationChanged(Location location)
			{
				Log.d("duomeng", "onLocationChanged");
				//				o.a.a(o.a.this, location, 2);
				a.removeUpdates(this);
			}

			public final void onProviderDisabled(String s)
			{
			}

			public final void onProviderEnabled(String s)
			{
			}

			public final void onStatusChanged(String s, int i, Bundle bundle)
			{
			}
		}

		private class c
		{
			static final int a = 0;
			static final int b = 1;
			static final int c = 2;

			private c()
			{
			}
		}

		//		private class a
		//		{
		//			static final int a = 0;
		//			static final int b = 1;
		//			static final int c = 2;
		//
		//			private a()
		//			{
		//			}
		//		}
	}
}

/* Location:           /home/sean/Desktop/duomeng.jar
 * Qualified Name:     cn.domob.data.o
 * JD-Core Version:    0.6.2
 */