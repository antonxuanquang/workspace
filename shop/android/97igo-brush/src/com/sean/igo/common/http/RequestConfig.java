package com.sean.igo.common.http;

import org.apache.http.client.HttpClient;

/**
 * 请求配置
 * @author sean
 */
public class RequestConfig
{
	public String url = null;
	public boolean isPost = false;
	public boolean loading = false;
	public String loadingText = "正在加载数据";
	public HttpClient client = null;

	@Override
	public String toString()
	{
		return url;
	}

}
