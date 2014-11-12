package com.sean.igo.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.sean.igo.common.util.PropertyUtil;
import com.sean.igo.common.util.VersionUtil;
import com.sean.igo.tab.ActiveTabPanel;
import com.sean.igo.tab.BaoyouTabPanel;
import com.sean.igo.tab.ClosthTabPanel;
import com.sean.igo.tab.MeTabPanel;
import com.sean.igo.tab.TabPanel;
import com.sean.igo.tab.TabPanelGroup;
import com.sean.igou.R;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

/**
 * 主界面
 * @author sean
 */
public class MainActivity extends FragmentActivity implements ISimpleDialogListener
{
	private TabPanelGroup tabGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		List<TabPanel> tabpanels = new ArrayList<TabPanel>();

//		tabpanels.add(new IndexTabPanel(this, R.id.tab_index));
		tabpanels.add(new BaoyouTabPanel(this, R.id.tab_baoyou));
		tabpanels.add(new ActiveTabPanel(this, R.id.tab_active));
		tabpanels.add(new ClosthTabPanel(this, R.id.tab_closth));
		tabpanels.add(new MeTabPanel(this, R.id.tab_me));

		tabGroup = new TabPanelGroup(tabpanels);
		tabGroup.setSelectTab(0);
	}

	//	/**
	//	 * 检查更新
	//	 */
	//	private void checkUpdate()
	//	{
	//		int lastedVersionCode = Integer
	//				.parseInt(PropertyUtil.getSysProp(this, PropertyUtil.Sys_LatestVersionCode, "0"));
	//		if (lastedVersionCode > 0)
	//		{
	//			int versionCode = VersionUtil.getVersionCode(this);
	//			if (versionCode < lastedVersionCode)
	//			{
	//				SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle("提示信息")
	//						.setMessage("有新版本,建议您立即更新").setPositiveButtonText("取 消").setNegativeButtonText("确 定")
	//						.setRequestCode(1000).show();
	//			}
	//		}
	//	}

	/**
	 * 退出
	 */
	private void exit()
	{
		SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle("提示信息").setMessage("确定要退出吗?")
				.setPositiveButtonText("取 消").setNegativeButtonText("确 定").setRequestCode(42).show();
	}

	@Override
	public void onPositiveButtonClicked(int requestCode)
	{
	}

	@Override
	public void onNegativeButtonClicked(int requestCode)
	{
		// 退出
		if (requestCode == 42)
		{
			MainActivity.this.finish();
			System.exit(0);
		}
		// 更新
		else if (requestCode == 1000)
		{
			VersionUtil.download(PropertyUtil.getSysProp(this, PropertyUtil.Sys_ApkUrl, ""), this);
		}
	}

	@Override
	public void onNeutralButtonClicked(int requestCode)
	{
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			this.exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
