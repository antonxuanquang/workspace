package com.sean.im.commom.core;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.im.commom.constant.Loggers;
import com.sean.im.commom.constant.Versions;
import com.sean.log.core.LogFactory;

/**
 * http request util
 * @author sean
 */
public class HttpUtil
{
	public static String Url = "";
	public static String ServerHost = "localhost";
	public static int ServerPort = 8080;
	public static long loginerId = 0;

	private static final Logger logger = LogFactory.getLogger(Loggers.IM);

	public static void request(Request request, RequestHandler handler)
	{
		request.setParameter("loginerId", loginerId);
		Writer writer = new Writer(request, handler);
		SwingUtilities.invokeLater(writer);
		writer = null;
	}

	public static void requestBlock(Request request, RequestHandler handler)
	{
		request.setParameter("loginerId", loginerId);
		Writer writer = new Writer(request, handler);
		writer.run();
		writer = null;
	}

	private static class Writer implements Runnable
	{
		private Request request;
		private RequestHandler handler;

		public Writer(Request request, RequestHandler handler)
		{
			this.request = request;
			this.handler = handler;
		}

		@Override
		public void run()
		{
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Url + "/action/" + Versions.CurrentVersion + "/" + request.getAction());
			try
			{
				post.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
				post.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
				post.setEntity(new UrlEncodedFormEntity(request.getParameter(), "utf-8"));

				logger.debug("Client request " + request.getAction() + " : " + request.getParameter());

				long curr = System.currentTimeMillis();
				HttpResponse res = client.execute(post);
				int status = res.getStatusLine().getStatusCode();
				if (status == HttpStatus.SC_OK)
				{
					String json = EntityUtils.toString(res.getEntity());
					logger.debug("Server response costs " + (System.currentTimeMillis() - curr) + " : " + json);

					JSONObject obj = JSON.parseObject(json);
					if (handler != null)
					{
						JSONObject data = obj.getJSONObject("data");
						if (data != null)
						{
							handler.callback(data);
						}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				logger.error(ex.getMessage(), ex);
				post.abort();
				JOptionPane.showMessageDialog(null, "网络链接失败，请检查网络", "error", JOptionPane.ERROR_MESSAGE);
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
