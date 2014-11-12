package com.sean.http.core;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.log.core.LogFactory;

/**
 * http request util
 * @author sean
 */
public class HttpUtil
{
	private static final Logger logger = LogFactory.getFrameworkLogger();

	public static void request(Request request, RequestHandler handler)
	{
		Writer writer = new Writer(request, handler);
		SwingUtilities.invokeLater(writer);
		writer = null;
	}

	public static void request(String url, RequestHandler handler)
	{
		Writer writer = new Writer(url, handler);
		SwingUtilities.invokeLater(writer);
		writer = null;
	}

	public static void requestBlock(Request request, RequestHandler handler)
	{
		Writer writer = new Writer(request, handler);
		writer.run();
		writer = null;
	}

	public static void requestBlock(String url, RequestHandler handler)
	{
		Writer writer = new Writer(url, handler);
		writer.run();
		writer = null;
	}

	private static class Writer implements Runnable
	{
		private Request request;
		private RequestHandler handler;
		private String url;

		public Writer(Request request, RequestHandler handler)
		{
			this.request = request;
			this.handler = handler;

			StringBuilder sb = new StringBuilder(128);
			sb.append("http://").append(HttpContext.Hostname).append(":").append(HttpContext.Port).append("/").append(HttpContext.AppName)
					.append("/action/").append(HttpContext.Version).append("/").append(request.getAction());
			url = sb.toString();
		}

		public Writer(String url, RequestHandler handler)
		{
			this.url = url;
			this.handler = handler;
		}

		@Override
		public void run()
		{
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			try
			{
				post.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
				post.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

				if (request != null)
				{
					request.getParameter().addAll(HttpContext.Defparam);

					if (HttpContext.listener != null)
					{
						HttpContext.listener.onPreRequest(request, HttpContext.Defparam);
					}

					post.setEntity(new UrlEncodedFormEntity(request.getParameter(), "utf-8"));
					logger.debug("Client request " + request);
				}
				else
				{
					post.setEntity(new UrlEncodedFormEntity(new ArrayList<NameValuePair>(0), "utf-8"));
					logger.debug("Client request " + url);
				}

				long curr = System.currentTimeMillis();
				HttpResponse res = client.execute(post);
				int status = res.getStatusLine().getStatusCode();
				if (status == HttpStatus.SC_OK)
				{
					String json = EntityUtils.toString(res.getEntity());
					logger.debug("Server response costs " + (System.currentTimeMillis() - curr) + " : " + json);

					JSONObject obj = JSON.parseObject(json);
					String state = obj.getString("state");
					int code = obj.getIntValue("code");
					String msg = obj.getString("msg");
					if (handler != null)
					{

						// 成功
						if (state.equals("Success"))
						{
							handler.callback(obj.getJSONObject("data"), 0, "操作成功");
						}
						// 业务异常
						else if (state.equals("BusinessException"))
						{
							// 如果请求关心业务异常
							if (handler.cared)
							{
								handler.callback(obj.getJSONObject("data"), code, msg);
							}
							else
							{
								JOptionPane.showMessageDialog(null, msg, "提示消息", JOptionPane.WARNING_MESSAGE);
							}
						}
						// 其他异常
						else
						{
							JOptionPane.showMessageDialog(null, msg, "提示消息", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				logger.error(ex.getMessage(), ex);
				post.abort();
				if (HttpContext.listener != null)
				{
					HttpContext.listener.onError(request, ex);
				}
			}
			finally
			{
				client.getConnectionManager().shutdown();
				client = null;
				post = null;
			}
		}
	}
}
