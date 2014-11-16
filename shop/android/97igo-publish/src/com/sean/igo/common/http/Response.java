package com.sean.igo.common.http;

import android.content.Context;
import android.widget.Toast;

/**
 * 请求回调接口
 * @author Sean
 */
public abstract class Response
{
	public void beforeRequest()
	{
	}

	public void onCancel(Context context, Request request)
	{
		Toast.makeText(context, "网络请求失败...", Toast.LENGTH_SHORT).show();
	}

	public abstract void callback(Context context, Request request, Result result);
}
