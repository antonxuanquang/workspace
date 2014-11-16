package com.sean.igo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.sean.igou.R;

/**
 * web浏览器
 * @author sean
 */
public class WebActivity extends Activity implements OnClickListener
{
	private ImageView tvBack;
	private TextView tvTitle;
	private WebView webview;

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

		tvBack = (ImageView) findViewById(R.id.back);
		tvTitle = (TextView) findViewById(R.id.title);
		webview = (WebView) findViewById(R.id.webview);

		Intent intent = this.getIntent();
		String title = intent.getStringExtra("title");
		String url = intent.getStringExtra("url");
		tvTitle.setText(title);

		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowContentAccess(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setAllowFileAccessFromFileURLs(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setGeolocationEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setSupportMultipleWindows(true);
		webSettings.setSupportZoom(true);
		webSettings.setUseWideViewPort(true);

		webview.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return false;
			}
		});

		webview.loadUrl(url);

		Log.d("debug", url);

		tvBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// 返回
		if (v == tvBack)
		{
			WebActivity.this.finish();
		}
	}

}
