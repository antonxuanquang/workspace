package com.sean.igo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.sean.igo.common.util.PropertyUtil;
import com.sean.igou.R;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

/**
 * 积分墙
 * @author sean
 */
public class OfferwallActivity extends FragmentActivity implements OnClickListener, UpdatePointsNotifier,
		ISimpleDialogListener
{
	private ImageView btnBack;
	private TextView btnEarn, btnCash, btnRefresh, btnBind, tvMoney, tvZfbUser, tvZfbName;
	private AppConnect app;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offerwall);
		this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

		btnBack = (ImageView) findViewById(R.id.back);
		btnEarn = (TextView) findViewById(R.id.btn_earn);
		btnCash = (TextView) findViewById(R.id.btn_cash);
		btnRefresh = (TextView) findViewById(R.id.btn_refresh);
		btnBind = (TextView) findViewById(R.id.btn_bind);
		tvMoney = (TextView) findViewById(R.id.money);
		tvZfbUser = (TextView) findViewById(R.id.zfb_user);
		tvZfbName = (TextView) findViewById(R.id.zfb_name);

		btnBack.setOnClickListener(this);
		btnEarn.setOnClickListener(this);
		btnCash.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
		btnBind.setOnClickListener(this);

		// 初始化积分墙
		app = AppConnect.getInstance("dd0d7365834f5d60efb0f12873b8d4ee", "waps", this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		tvZfbUser.setText(PropertyUtil.getUserProp(this, PropertyUtil.User_ZfbUser, "未绑定"));
		tvZfbName.setText(PropertyUtil.getUserProp(this, PropertyUtil.User_ZfbName, "未绑定"));
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v)
	{
		// 返回
		if (v == btnBack)
		{
			OfferwallActivity.this.finish();
		}
		// 开始赚钱
		else if (v == btnEarn)
		{
			if (app != null)
			{
				app.showAppOffers(this);
			}
		}
		// 刷新信息
		else if (v == btnRefresh)
		{
			if (app != null)
			{
				app.getPoints();
			}
		}
		// 提现
		else if (v == btnCash)
		{
			String zfbUser = PropertyUtil.getUserProp(this, PropertyUtil.User_ZfbUser, null);
			String zfbName = PropertyUtil.getUserProp(this, PropertyUtil.User_ZfbName, null);
			if (zfbUser != null && zfbName != null)
			{
				StringBuilder txt = new StringBuilder();
				txt.append("您的提现订单如下:\n");
				txt.append("提现金额: ").append("50元\n");
				txt.append("需要消耗: ").append(String.valueOf(50 * 200)).append("金币\n");
				txt.append("支付宝帐号: ").append(zfbUser).append("\n");
				txt.append("支付宝姓名: ").append(zfbName).append("\n");
				txt.append("确定要提交订单吗?");

				SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle("订单信息")
						.setMessage(txt.toString()).setPositiveButtonText("取 消").setNegativeButtonText("确 定")
						.setRequestCode(1000).show();
			}
			else
			{
				Toast.makeText(this, "请绑定支付宝", Toast.LENGTH_SHORT);
			}
		}
		// 绑定支付宝
		else if (v == btnBind)
		{
			Intent intent = new Intent();
			intent.setClass(this, BindActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void getUpdatePoints(String arg0, int points)
	{
		tvMoney.setText(points + "金币");
		Toast.makeText(OfferwallActivity.this, "已刷新", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void getUpdatePointsFailed(String arg0)
	{
		Toast.makeText(OfferwallActivity.this, "你的金币不足", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPositiveButtonClicked(int requestCode)
	{
	}

	@Override
	public void onNegativeButtonClicked(int requestCode)
	{
		if (requestCode == 1000)
		{
			app.spendPoints(50 * 200, this);
			Toast.makeText(this, "提交成功, 请等待后台人员处理", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onNeutralButtonClicked(int requestCode)
	{
	}
}
