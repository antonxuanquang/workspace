package com.sean.igo.common.http;

import android.content.Context;

/**
 * Http请求工具
 * @author sean
 */
public class HttpUtil
{
	public static final String Domain = "www.97igo.com";
	public static final String Port = "8080";
	public static final String AppName = "shop-web";

	public static void request(Context context, Request request, Response response)
	{
		RequestConfig cfg = getDefaultRequestConfig(request);
		HttpTask task = new HttpTask(context, cfg, request, response);
		task.execute();
	}

	public static void request(Context context, Request request, RequestConfig cfg, Response response)
	{
		HttpTask task = new HttpTask(context, cfg, request, response);
		task.execute();
	}

	/**
	 * http get请求, 该方法必须将所有参数链接到action中(url传参)
	 * @param context
	 * @param request
	 * @param cfg
	 * @param response
	 */
	public static void requestGet(Context context, RequestConfig cfg, Response response)
	{
		HttpGetTask task = new HttpGetTask(context, cfg, response);
		task.execute();
	}

	/**
	 * 读取默认的请求配置
	 * @param request
	 * @return
	 */
	public static RequestConfig getDefaultRequestConfig(Request request)
	{
		RequestConfig cfg = new RequestConfig();
		cfg.isPost = false;
		cfg.loading = false;
		cfg.url = "http://" + Domain + ":" + Port + "/" + AppName + "/api/" + request.getAction() + "Action";
		return cfg;
	}

	/**
	 * 读取默认的请求配置
	 * @param request
	 * @return
	 */
	public static RequestConfig getDefaultRequestConfig()
	{
		RequestConfig cfg = new RequestConfig();
		cfg.isPost = false;
		cfg.loading = false;
		cfg.url = null;
		return cfg;
	}
}
