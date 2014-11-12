package com.sean.igo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sean.igo.common.img.ImageUtil;
import com.sean.igo.common.ui.DisplayUtil;
import com.sean.igou.R;

/**
 * 欢迎界面
 * @author sean
 */
public class FlashActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash);

		new LoadTask().execute();
	}

	public class LoadTask extends AsyncTask<Object, Integer, Integer>
	{
		@Override
		protected void onPreExecute()
		{
		}

		private void loadUpdate()
		{
			//			Request request = new Request("INQUIRE_ANDROID_CONFIG");
			//			HttpUtil.request(FlashActivity.this, request, new Response()
			//			{
			//				@Override
			//				public void callback(Context context, Request request, Result result)
			//				{
			//					JSONObject obj = result.data;
			//
			//					PropertyUtil.setSysProp(FlashActivity.this, PropertyUtil.Sys_LatestVersionName,
			//							obj.getString("androidVersion"));
			//					PropertyUtil.setSysProp(FlashActivity.this, PropertyUtil.Sys_LatestVersionCode,
			//							obj.getString("androidCode"));
			//					PropertyUtil.setSysProp(FlashActivity.this, PropertyUtil.Sys_ApkUrl, obj.getString("androidUrl"));
			//				}
			//			});
		}

		@Override
		protected Integer doInBackground(Object... params)
		{
			// 读取屏幕分辨率
			DisplayUtil.initDisplayParams(FlashActivity.this);

			// 初始化图片加载
			ImageUtil.initImageLoader(FlashActivity.this);

			// 读取最新版本
			loadUpdate();
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			Intent intent = new Intent();
			intent.setClass(FlashActivity.this, MainActivity.class);
			FlashActivity.this.startActivity(intent);
			FlashActivity.this.finish();
		}

	}
}
