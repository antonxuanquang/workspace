package cn.domob.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.waps.SDKUtils;

public class w
{
	private static f I = new f(w.class.getSimpleName());

	private static ArrayList<String> J = new ArrayList();
	private static String K;
	private static int L;
	private static int M;
	private static String N;
	private static String O;
	private static final String P = "sdk";
	protected static final String a = "pb[identifier]";
	protected static final String b = "vc";
	protected static final String c = "vn";
	protected static final String d = "appname";
	protected static final String e = "useragent";
	protected static final String f = "ua";
	protected static final String g = "install";
	protected static final String h = "idv";
	protected static final String i = "imei";
	protected static final String j = "andoidid";
	protected static final String k = "osv";
	protected static final String l = "devicemodel";
	protected static final String m = "network";
	protected static final String n = "networkavailable";
	protected static final String o = "ip";
	protected static final String p = "timezone";
	protected static final String q = "carrier";
	protected static final String r = "orientation";
	protected static final String s = "isemulator";
	protected static final String t = "rsd";
	protected static final String u = "csd";
	protected static final String v = "rsw";
	protected static final String w = "rsh";
	protected static final String x = "csw";
	protected static final String y = "csh";
	protected static final String z = "d[coord]";
	protected static final String A = "d[coord_acc]";
	protected static final String B = "d[coord_status]";
	protected static final String C = "d[coord_timestamp]";
	protected static final String D = "dma";
	protected static final String E = "areacode";
	protected static final String F = "cellid";
	protected static final String G = "language";
	protected static final String H = "odin1";

	public static void a(ArrayList<String> paramArrayList)
	{
		if (paramArrayList != null)
		{
			I.b("需要关闭的字段: " + paramArrayList);
			J = paramArrayList;
		}
	}

	public static ArrayList<String> a()
	{
		return J;
	}

	public static String a(Context paramContext)
	{
		if (J.contains("pb[identifier]"))
		{
			return "";
		}

		return cn.domob.data.o.a(paramContext);
	}

	public static int b(Context paramContext)
	{
		if (J.contains("vc"))
		{
			return -1;
		}

		return cn.domob.data.o.b(paramContext);
	}

	public static String c(Context paramContext)
	{
		if (J.contains("vn"))
		{
			return "";
		}

		return cn.domob.data.o.c(paramContext);
	}

	public static String d(Context paramContext)
	{
		if (J.contains("appname"))
		{
			return "";
		}

		return cn.domob.data.o.d(paramContext);
	}

	public static String e(Context paramContext)
	{
		if (J.contains("useragent"))
		{
			return "";
		}

		return cn.domob.data.o.C(paramContext);
	}

	public static String f(Context paramContext)
	{
		if (J.contains("ua"))
		{
			return "";
		}

		String str1 = "_";
		if (null == K)
		{
			StringBuffer localStringBuffer = new StringBuffer();

			localStringBuffer.append("android");
			localStringBuffer.append(",");

			localStringBuffer.append(",");

			if (SDKUtils.VERSION.length() > 0)
			{
				localStringBuffer.append(SDKUtils.VERSION.replaceAll(",", "_"));
			}
			else
			{
				localStringBuffer.append("1.5");
			}
			localStringBuffer.append(",");

			localStringBuffer.append(",");
			String str2;
			if ((str2 = SDKUtils.MODEL).length() > 0)
			{
				localStringBuffer.append(str2.replaceAll(",", "_"));
			}
			localStringBuffer.append(",");

			String str3 = p(paramContext);

			if (str3 != null)
			{
				localStringBuffer.append(str3.replaceAll(",", "_"));
			}
			localStringBuffer.append(",");

			localStringBuffer.append(",");

			localStringBuffer.append(",");

			K = localStringBuffer.toString();
			I.b(z.class.getSimpleName(), "getUserAgent:" + K);
		}

		return K;
	}

	public static boolean a(Context paramContext, String paramString)
	{
		if (J.contains("install"))
		{
			return false;
		}
		return cn.domob.data.o.a(paramContext, paramString);
	}

	protected static List<String> g(Context paramContext)
	{
		if (J.contains("install"))
		{
			return new ArrayList();
		}
		return cn.domob.data.o.D(paramContext);
	}

	public static String h(Context paramContext)
	{
		if (J.contains("idv"))
		{
			return "";
		}

		if (O == null)
		{
			if (i(paramContext))
			{
				I.b("Use emulator id");
				O = "-1,-1,emulator";
			}
			else
			{
				I.b("Generate device id");
				O = C(paramContext);
			}
		}

		return O;
	}

	public static boolean i(Context paramContext)
	{
		if (J.contains("isemulator"))
		{
			return false;
		}
		if (N == null)
		{
			N = k(paramContext);
		}
		return (N == null) && (B(paramContext)) && ("sdk".equalsIgnoreCase(SDKUtils.MODEL));
	}

	private static boolean B(Context paramContext)
	{
		String str = j(paramContext);
		boolean bool = false;
		if (str == null)
			bool = true;
		else
		{
			bool = str.replaceAll("0", "").equals("");
		}
		return bool;
	}

	private static String C(Context paramContext)
	{
		I.b("Start to generate device id");
		StringBuffer localStringBuffer = new StringBuffer();
		try
		{
			String str1 = j(paramContext);
			if (str1 != null)
				localStringBuffer.append(str1);
			else
			{
				localStringBuffer.append("-1");
			}
			localStringBuffer.append(",");

			localStringBuffer.append("-1");
			localStringBuffer.append(",");
		}
		catch (SecurityException localSecurityException)
		{
			I.a(localSecurityException);
			I.e("you must set READ_PHONE_STATE permisson in AndroidManifest.xml");
		}
		catch (Exception localException)
		{
			I.a(localException);
		}

		String str2 = k(paramContext);
		if (str2 != null)
		{
			localStringBuffer.append(str2);
		}
		else
		{
			I.a("Android ID is null, use -1 instead");
			localStringBuffer.append("-1");
		}
		I.b("Generated device id: " + localStringBuffer.toString());
		return localStringBuffer.toString();
	}

	public static String j(Context paramContext)
	{
		if (J.contains("imei"))
		{
			return "-1";
		}
		return cn.domob.data.o.l(paramContext);
	}

	public static String k(Context paramContext)
	{
		if (J.contains("andoidid"))
		{
			return "-1";
		}
		return cn.domob.data.o.m(paramContext);
	}

	public static String l(Context paramContext)
	{
		if (J.contains("osv"))
		{
			return "";
		}
		return cn.domob.data.o.n(paramContext);
	}

	public static String m(Context paramContext)
	{
		if (J.contains("devicemodel"))
		{
			return "";
		}
		return cn.domob.data.o.o(paramContext);
	}

	public static String n(Context paramContext)
	{
		if (J.contains("network"))
		{
			return "";
		}

		return cn.domob.data.o.p(paramContext);
	}

	public static boolean o(Context paramContext)
	{
		if (J.contains("networkavailable"))
		{
			return false;
		}

		return cn.domob.data.o.q(paramContext);
	}

	public static String b()
	{
		if (J.contains("ip"))
		{
			return "";
		}

		return cn.domob.data.o.a();
	}

	public static String c()
	{
		if (J.contains("timezone"))
		{
			return "";
		}
		return cn.domob.data.o.b();
	}

	public static String p(Context paramContext)
	{
		if (J.contains("carrier"))
		{
			return "";
		}
		return cn.domob.data.o.r(paramContext);
	}

	public static String q(Context paramContext)
	{
		if (J.contains("orientation"))
		{
			return "";
		}
		return cn.domob.data.o.s(paramContext);
	}

	public static float r(Context paramContext)
	{
		if (J.contains("rsd"))
		{
			return -1.0F;
		}
		return cn.domob.data.o.t(paramContext);
	}

	public static float s(Context paramContext)
	{
		if (J.contains("csd"))
		{
			return -1.0F;
		}
		return cn.domob.data.o.u(paramContext);
	}

	public static int t(Context paramContext)
	{
		if (J.contains("rsw"))
		{
			return -1;
		}
		L = Math.round(v(paramContext) * (r(paramContext) / s(paramContext)));

		return L;
	}

	public static int u(Context paramContext)
	{
		if (J.contains("rsh"))
		{
			return -1;
		}
		M = Math.round(w(paramContext) * (r(paramContext) / s(paramContext)));

		return M;
	}

	public static int v(Context paramContext)
	{
		if (J.contains("csw"))
		{
			return -1;
		}
		return cn.domob.data.o.x(paramContext);
	}

	public static int w(Context paramContext)
	{
		if (J.contains("csh"))
		{
			return -1;
		}
		return cn.domob.data.o.y(paramContext);
	}

	public static String x(Context paramContext)
	{
		if (J.contains("d[coord]"))
		{
			return "";
		}
		return cn.domob.data.o.B(paramContext);
	}

	public static int d()
	{
		if (J.contains("d[coord_acc]"))
		{
			return -1;
		}
		return cn.domob.data.o.c();
	}

	public static int e()
	{
		if (J.contains("d[coord_status]"))
		{
			return -1;
		}
		return cn.domob.data.o.d();
	}

	public static long f()
	{
		if (J.contains("d[coord_timestamp]"))
		{
			return -1L;
		}
		return cn.domob.data.o.e();
	}

	public static boolean a(int paramInt, boolean paramBoolean)
	{
		return cn.domob.data.o.a(paramInt, paramBoolean);
	}

	public static String y(Context paramContext)
	{
		if (J.contains("dma"))
		{
			return "";
		}
		return cn.domob.data.o.f(paramContext);
	}

	public static String z(Context paramContext)
	{
		if (J.contains("odin1"))
		{
			return "";
		}
		return "";
	}

	public static String A(Context paramContext)
	{
		if (J.contains("areacode"))
		{
			return "";
		}
		return cn.domob.data.o.E(paramContext);
	}

	public static String g()
	{
		if (J.contains("language"))
		{
			return "";
		}
		return cn.domob.data.o.f();
	}
}