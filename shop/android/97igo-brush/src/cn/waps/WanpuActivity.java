package cn.waps;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.domob.data.OErrorInfo;
import cn.domob.data.OManager;
import cn.domob.data.SqlLiteUtil;

import com.sean.igou.R;

/**
 * 积分墙
 * @author sean
 */
public class WanpuActivity extends FragmentActivity implements OnClickListener
{
	private ImageView btnBack;
	private TextView btnEarn, btnInit, btnClear, btnChangeUser, tvUserHouse, tvUser, tvSDK, btnChengguolv,
			btnDailyClear, btnDuomeng, btnDuomengjifen;
	private EditText etTimes, etTime;

	private static AppConnect app;
	private OManager mDomobOfferWallManager;

	Handler handler = new Handler(new Callback()
	{
		@SuppressLint("ShowToast")
		@Override
		public boolean handleMessage(Message msg)
		{
			if (msg.arg1 == 0)
			{
				if (app != null)
				{
					if (app.m != null)
					{
						app.m.dismiss();
					}

					app.close();
				}

				try
				{
					changeUser(false);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				app.showAppOffers(WanpuActivity.this);
			}
			else if (msg.arg1 == 1)
			{
				if (app.m != null)
				{
					app.m.dismiss();
				}
				app.close();
				Toast.makeText(WanpuActivity.this, "执行10次完成", Toast.LENGTH_SHORT);
			}
			return false;
		}
	});

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wanpu);
		this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

		btnBack = (ImageView) findViewById(R.id.back);
		btnEarn = (TextView) findViewById(R.id.btn_earn);
		btnClear = (TextView) findViewById(R.id.btn_clear);
		btnChangeUser = (TextView) findViewById(R.id.btn_change_user);
		btnInit = (TextView) findViewById(R.id.btn_init);
		btnChengguolv = (TextView) findViewById(R.id.btn_chengguolv);
		etTimes = (EditText) findViewById(R.id.btn_times);
		etTime = (EditText) findViewById(R.id.btn_time);
		btnDailyClear = (TextView) findViewById(R.id.btn_daily_clear);
		btnDuomeng = (TextView) findViewById(R.id.btn_duomeng);
		btnDuomengjifen = (TextView) findViewById(R.id.btn_duomeng_jifen);

		tvUserHouse = (TextView) findViewById(R.id.tv_userhouse);
		tvUser = (TextView) findViewById(R.id.tv_user);
		tvSDK = (TextView) findViewById(R.id.tv_sdk);

		btnBack.setOnClickListener(this);
		btnEarn.setOnClickListener(this);
		btnChangeUser.setOnClickListener(this);
		btnInit.setOnClickListener(this);
		btnClear.setOnClickListener(this);
		btnChengguolv.setOnClickListener(this);
		btnDailyClear.setOnClickListener(this);
		btnDuomeng.setOnClickListener(this);
		btnDuomengjifen.setOnClickListener(this);

		clearCache();

		try
		{
			UserHouse.initHouse(this);
			User user = UserHouse.getUser4Increment();
			showUserHouse();

			// 注入SDK
			SDKUtils.setUser(user);
			changeUser(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public void clearCache()
	{
		// 清空缓存
		String[] cache = new String[] { "appPrefrences", "ShowAdFlag", "AppSettings" };
		for (int i = 0; i < cache.length; i++)
		{
			SharedPreferences props = this.getSharedPreferences(cache[i], Context.MODE_PRIVATE);
			Editor editor = props.edit();
			editor.clear();
			editor.commit();
		}
	}

	public void showUserHouse()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("part1: ").append(UserHouse.part1.size()).append("个, ");
		sb.append("part2: ").append(UserHouse.part2.size()).append("个, ");
		sb.append("part3: ").append(UserHouse.part3.size()).append("个");
		tvUserHouse.setText("用户库信息:\n" + sb.toString());
	}

	public void showUser(User user)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("imei: ").append(user.imei).append("\n");
		sb.append("imsi: ").append(user.imsi).append("\n");
		sb.append("mac: ").append(user.mac).append("\n");
		sb.append("version: ").append(user.version).append("\n");
		sb.append("model: ").append(user.model).append("\n");
		sb.append("screenWidth: ").append(user.screenWidth).append("\n");
		sb.append("screenHeight: ").append(user.screenHeight).append("\n");
		sb.append("brand: ").append(user.brand).append("\n");
		tvUser.setText("当前虚拟用户信息:\n" + sb.toString());
	}

	public void changeUser(boolean brush) throws Exception
	{
		User user = null;
		if (brush)
		{
			user = UserHouse.getUser4Brush();
		}
		else
		{
			user = UserHouse.getUser4Increment();
		}

		showUser(user);
		showUserHouse();

		// 注入SDK
		SDKUtils.setUser(user);
		clearCache();

		// 初始化统计器，并通过代码设置APP_ID, APP_PID
		if (app == null)
		{
			app = AppConnect.getInstance("b04222c03b0afea639a9ff345d73ee27", "waps", this);
			app.cleanCache();
		}

		// 注入数据
		StringBuilder sb = new StringBuilder("\n注入SDK数据:\n");
		// 注入imei
		Field f = app.getClass().getDeclaredField("c");
		f.setAccessible(true);
		f.set(app, SDKUtils.user.imei);
		sb.append("imei : ").append(f.get(app)).append("\n");

		// 注入imsi
		f = app.getClass().getDeclaredField("N");
		f.setAccessible(true);
		f.set(app, SDKUtils.user.imsi);
		sb.append("imsi : ").append(f.get(app)).append("\n");

		// 注入screenWidth
		f = app.getClass().getDeclaredField("I");
		f.setAccessible(true);
		f.set(app, Integer.parseInt(SDKUtils.user.screenWidth));
		sb.append("screenWidth : ").append(f.get(app)).append("\n");

		// 注入screenHeight
		f = app.getClass().getDeclaredField("J");
		f.setAccessible(true);
		f.set(app, Integer.parseInt(SDKUtils.user.screenHeight));
		sb.append("screenHeight : ").append(f.get(app)).append("\n");

		tvSDK.setText(sb.toString());

		SDKUtils.showUrl(app, this);

		// 注入SDK
		SDKUtils.setUser(user);
		clearCache();
		app = AppConnect.getInstance("b04222c03b0afea639a9ff345d73ee27", "waps", this);
		app.cleanCache();
	}

	@Override
	public void onClick(View v)
	{
		// 返回
		if (v == btnBack)
		{
			WanpuActivity.this.finish();
		}
		// 初始化
		if (v == btnInit)
		{
			try
			{
				this.changeUser(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// 开始赚钱
		else if (v == btnEarn)
		{
			app.showAppOffers(this);
		}
		// 更换用户
		else if (v == btnChangeUser)
		{
			try
			{
				this.changeUser(true);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		// 清除缓存
		else if (v == btnClear)
		{
			this.clearCache();
			if (app != null)
			{
				app.cleanCache();
			}
		}
		else if (v == btnDailyClear)
		{
			try
			{
				UserHouse.resetPart2();
				showUserHouse();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// 降低成果率
		else if (v == btnChengguolv)
		{
			Thread t = new Thread(new Runnable()
			{
				@SuppressLint("NewApi")
				@Override
				public void run()
				{
					String strtimes = etTimes.getText().toString();
					String intervalStr = etTime.getText().toString();

					int times = strtimes.isEmpty() ? 20 : Integer.parseInt(strtimes);
					int interval = intervalStr.isEmpty() ? 5 : Integer.parseInt(intervalStr);

					for (int i = 0; i < times; i++)
					{
						Message msg = new Message();
						msg.arg1 = 0;
						handler.sendMessage(msg);

						if (i == 0)
						{
							try
							{
								Thread.sleep(1000 * 6);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							try
							{
								Thread.sleep(1000 * interval);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}

					Message msg = new Message();
					msg.arg1 = 1;
					handler.sendMessage(msg);
				}
			});
			t.start();
		}
		else if (v == btnDuomeng)
		{
			// 清空sharepreference
			String[] cache = new String[] { "idpkg", "idname", "RP" };
			for (int i = 0; i < cache.length; i++)
			{
				SharedPreferences props = this.getSharedPreferences(cache[i], Context.MODE_PRIVATE);
				Editor editor = props.edit();
				editor.clear();
				editor.commit();
			}

			// 清除数据库
			new SqlLiteUtil(this).clear();
			
			mDomobOfferWallManager = new OManager(this, "96ZJ1/5AzeGQbwTCmq");
			mDomobOfferWallManager.loadOfferWall();
			// 检查积分
			mDomobOfferWallManager.setCheckPointsListener(new OManager.CheckPointsListener()
			{
				@Override
				public void onCheckPointsSucess(final int point, final int consumed)
				{
					((Activity) WanpuActivity.this).runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Toast.makeText(WanpuActivity.this, "当前积分:" + point, Toast.LENGTH_LONG).show();
						}
					});
				}

				@Override
				public void onCheckPointsFailed(final OErrorInfo e)
				{
				}
			});
		}
		else if (v == btnDuomengjifen)
		{
			if (mDomobOfferWallManager != null)
			{
				mDomobOfferWallManager.checkPoints();	
			}
		}
	}
}
