package com.sean.igo.activity;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sean.igo.R;
import com.sean.igo.common.util.ConstantUtil;
import com.sean.igo.common.util.PropertyUtil;
import com.sean.igo.common.util.VersionUtil;

import eu.inmite.android.lib.dialogs.IListDialogListener;
import eu.inmite.android.lib.dialogs.ListDialogFragment;

/**
 * 设置
 * @author sean
 */
public class SettingActivity extends FragmentActivity implements OnClickListener, IListDialogListener
{
	private TextView btnBack, tvVersionName, tvPlayInterval, tvLatestVersion, tvCacheSize;
	private LinearLayout btnCheckUpdate, btnPlayInterval, btnClearCache;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

		btnBack = (TextView) findViewById(R.id.back);
		btnCheckUpdate = (LinearLayout) findViewById(R.id.check_update);
		btnPlayInterval = (LinearLayout) findViewById(R.id.play_interval);
		btnClearCache = (LinearLayout) findViewById(R.id.clear_cache);

		tvVersionName = (TextView) findViewById(R.id.version);
		tvPlayInterval = (TextView) findViewById(R.id.play_interval_val);
		tvLatestVersion = (TextView) findViewById(R.id.latest_version_val);
		tvCacheSize = (TextView) findViewById(R.id.clear_cache_val);

		btnBack.setOnClickListener(this);
		btnCheckUpdate.setOnClickListener(this);
		btnPlayInterval.setOnClickListener(this);
		btnClearCache.setOnClickListener(this);

		tvVersionName.setText(VersionUtil.getVersionName(this));
		tvPlayInterval.setText(PropertyUtil.getSysProp(this, PropertyUtil.Sys_AutoPlayInterval, "5") + "秒");

		tvLatestVersion.setText(PropertyUtil.getSysProp(this, PropertyUtil.Sys_LatestVersionName, ""));

		caculateCacheSize();
	}

	private void caculateCacheSize()
	{
		long length = FileUtils.sizeOfDirectory(new File(ConstantUtil.RootDir));
		long lengthMB = length / 1024 / 1024;
		tvCacheSize.setText(lengthMB + "M");
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnBack)
		{
			SettingActivity.this.finish();
		}
		else if (v == btnCheckUpdate)
		{
			int lastedVersionCode = Integer.parseInt(PropertyUtil.getSysProp(this, PropertyUtil.Sys_LatestVersionCode,
					"0"));
			int versionCode = VersionUtil.getVersionCode(this);
			if (versionCode < lastedVersionCode)
			{
				VersionUtil.download(PropertyUtil.getSysProp(this, PropertyUtil.Sys_ApkUrl, ""), this);
			}
			else
			{
				Toast.makeText(this, "当前已经是最新版", Toast.LENGTH_SHORT).show();
			}
		}
		else if (v == btnPlayInterval)
		{
			ListDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle("播放时间间隔")
					.setItems(new String[] { "2秒(网络质量较好)", "3秒(网络质量好)", "5秒(网络质量一般)", "10秒(网络质量差)" }).show();
		}
		else if (v == btnClearCache)
		{
			Toast.makeText(SettingActivity.this, "开始后台清空缓存", Toast.LENGTH_SHORT).show();
			new ClearTask().execute();
		}
	}

	@Override
	public void onListItemSelected(String value, int number)
	{
		int timer = 0;
		switch (number)
		{
		case 0:
			timer = 2;
			break;
		case 1:
			timer = 3;
			break;
		case 2:
			timer = 5;
			break;
		case 3:
			timer = 10;
			break;

		default:
			break;
		}

		if (timer != 0)
		{
			PropertyUtil.setSysProp(SettingActivity.this, PropertyUtil.Sys_AutoPlayInterval, String.valueOf(timer));
			tvPlayInterval.setText(timer + "秒");
		}
	}

	public class ClearTask extends AsyncTask<Object, Integer, Integer>
	{
		@Override
		protected Integer doInBackground(Object... params)
		{
			try
			{
				FileUtils.deleteDirectory(new File(ConstantUtil.RootDir + "images"));
				FileUtils.deleteDirectory(new File(ConstantUtil.RootDir + "cache"));
				return 0;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			SettingActivity.this.caculateCacheSize();
			Toast.makeText(SettingActivity.this, "缓存已清空", Toast.LENGTH_SHORT).show();
		}

	}
}
