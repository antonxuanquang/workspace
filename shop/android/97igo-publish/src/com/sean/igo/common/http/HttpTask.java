package com.sean.igo.common.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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
import com.sean.igou.R;

/**
 * http 请求任务
 * @author sean
 */
public class HttpTask extends AsyncTask<Object, Integer, Result>
{
	private Context context;
	private Request request;
	private Response response;
	private RequestConfig cfg;
	private LayoutInflater inflater;
	private ProgressDialog dialog;

	public HttpTask(Context context, RequestConfig cfg, Request request, Response response)
	{
		this.context = context;
		this.cfg = cfg;
		this.request = request;
		this.response = response;
		inflater = LayoutInflater.from(context);
	}

	public static HttpPost getPost(String url)
	{
		HttpPost post = new HttpPost(url);
		return post;
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
		HttpClient client = new DefaultHttpClient();
		HttpPost post = getPost(cfg.url);
		HttpResponse response = null;
		try
		{
			post.setEntity(new UrlEncodedFormEntity(request.getParameter(), "utf-8"));
			Log.d("debug", "Client request " + request);

			long curr = System.currentTimeMillis();
			response = client.execute(post);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK)
			{
				String json = EntityUtils.toString(response.getEntity());
				Log.d("debug", "Server response costs " + (System.currentTimeMillis() - curr) + " : " + json);

				JSONObject obj = JSON.parseObject(json);

				Result rs = new Result();
				rs.state = obj.getString("state");
				rs.msg = obj.getString("msg");
				rs.code = obj.getIntValue("code");
				rs.data = obj.getJSONObject("data");
				return rs;
			}
		}
		catch (Exception ex)
		{
			Log.e("error", "http请求错误: " + ex.getMessage(), ex);
			post.abort();
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

		if (result != null)
		{
			if (result.isSuccess())
			{
				if (response != null)
				{
					response.callback(context, request, result);
				}
			}
			else if (result.state.equals("UNLOGIN"))
			{
				Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
			}
			else if (result.state.equals("DENIED"))
			{
				Toast.makeText(context, "无权访问", Toast.LENGTH_SHORT).show();
			}
			else if (result.state.equals("INVALID"))
			{
				Toast.makeText(context, "参数错误", Toast.LENGTH_SHORT).show();
			}
			else if (result.state.equals("EXCEPTION"))
			{
				Toast.makeText(context, "服务器异常", Toast.LENGTH_SHORT).show();
			}
			else
			{
				if (response != null)
				{
					response.callback(context, request, result);
				}
			}
		}
		else
		{
			Toast.makeText(context, "网络请求失败,请稍候重试", Toast.LENGTH_SHORT).show();
		}
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
			response.onCancel(context, request);
		}
	}

}