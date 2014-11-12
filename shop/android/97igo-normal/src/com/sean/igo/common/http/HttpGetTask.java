package com.sean.igo.common.http;

import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.igo.R;

/**
 * http 请求任务
 * @author sean
 */
public class HttpGetTask extends AsyncTask<Object, Integer, Result>
{
	private Context context;
	private Response response;
	private RequestConfig cfg;
	private LayoutInflater inflater;
	private ProgressDialog dialog;

	public static HttpClient client;
	static
	{
		client = new DefaultHttpClient();
	}

	public HttpGetTask(Context context, RequestConfig cfg, Response response)
	{
		this.context = context;
		this.cfg = cfg;
		this.response = response;
		inflater = LayoutInflater.from(context);
	}

	public static HttpGet getGet(String url)
	{
		HttpGet get = new HttpGet(url);
		return get;
	}

	/**
	 * 显示等待对话框
	 * @param text
	 */
	private void showLoading(String text)
	{
		View v = inflater.inflate(R.layout.popmenu_loading, null);
		dialog = new ProgressDialog(context);
		dialog.show();
		dialog.setContentView(v);

		TextView txt = (TextView) v.findViewById(R.id.progress_dialog_tv);
		txt.setText(text);
	}

	/**
	 * 关闭等待对话框
	 */
	private void closeLoading()
	{
		if (dialog != null)
		{
			dialog.dismiss();
		}
	}

	@Override
	protected void onPreExecute()
	{
		if (cfg.loading)
		{
			this.showLoading(cfg.loadingText);
		}
		if (response != null)
		{
			this.response.beforeRequest();
		}
	}

	@Override
	protected Result doInBackground(Object... params)
	{
		HttpGet get = getGet(cfg.url);
		HttpResponse response = null;

		HttpClient httpclient = null;
		if (cfg.client != null)
		{
			httpclient = cfg.client;
		}
		else
		{
			httpclient = client;
		}

		try
		{
			Log.d("debug", "Client request " + cfg);

			long curr = System.currentTimeMillis();
			response = httpclient.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK)
			{
				String json = EntityUtils.toString(response.getEntity());
				Log.d("debug", "Server response costs " + (System.currentTimeMillis() - curr) + " : " + json);

				JSONObject obj = JSON.parseObject(json);

				Result rs = new Result();
				rs.state = "SUCCESS";
				rs.data = obj;
				rs.code = 0;
				return rs;
			}
		}
		catch (Exception ex)
		{
			Log.e("error", "http请求错误: " + ex.getMessage(), ex);
			get.abort();
			return null;
		}
		finally
		{
			System.gc();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Result result)
	{
		if (cfg.loading)
		{
			this.closeLoading();
		}

		if (result != null && result.isSuccess())
		{
			if (response != null)
			{
				response.callback(context, null, result);
			}
			return;
		}
		else if (result != null)
		{
			Toast.makeText(context, "网络请求:" + result.state, Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(context, "网络请求失败,请稍候重试", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCancelled()
	{
		if (cfg.loading)
		{
			this.closeLoading();
		}

		if (this.response != null)
		{
			response.onCancel(context, null);
		}
	}

	public static HttpClient getSSLHttpClient()
	{
		try
		{
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		}
		catch (Exception e)
		{
			return new DefaultHttpClient();
		}
	}
}