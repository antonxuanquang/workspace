package com.sean.brush;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.waps.SDKUtils;

import com.sean.igou.R;

/**
 * 积分墙
 * @author sean
 */
public class EarnActivity extends FragmentActivity implements OnClickListener
{
	private ImageView btnBack;
	private TextView btnEarn, btnInit, btnClear, btnChangeUser, btnChengguolv, btnDailyClear, btnSwitchPlat,
			btnCheckPoints, btnChangeIP, btnInitIp, btnTestNetspeed;
	private TextView tvUserHouse, tvUser, tvNet;
	private EditText etTimes, etTime;

	private WanpuPlatform wanpu = new WanpuPlatform();
	private DuomengPlatform duomeng = new DuomengPlatform();
	private Platform plat = wanpu;

	Handler handler = new Handler(new Callback()
	{
		@SuppressLint("ShowToast")
		@Override
		public boolean handleMessage(Message msg)
		{
			if (msg.arg1 == 0)
			{
				try
				{
					plat.closeOfferwall(EarnActivity.this);
					changeUser(false);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				plat.openOfferwall(EarnActivity.this);
			}
			else if (msg.arg1 == 1)
			{
				try
				{
					plat.closeOfferwall(EarnActivity.this);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				Toast.makeText(EarnActivity.this, "执行10次完成", Toast.LENGTH_SHORT);

				// TODO 播放完成提示音
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
		btnSwitchPlat = (TextView) findViewById(R.id.btn_switch_platform);
		btnCheckPoints = (TextView) findViewById(R.id.btn_check_points);
		btnChangeIP = (TextView) findViewById(R.id.btn_change_ip);
		btnInitIp = (TextView) findViewById(R.id.btn_init_ip);
		btnTestNetspeed = (TextView) findViewById(R.id.btn_test_netspeed);

		tvUserHouse = (TextView) findViewById(R.id.tv_userhouse);
		tvUser = (TextView) findViewById(R.id.tv_user);
		tvNet = (TextView) findViewById(R.id.tv_net);

		btnBack.setOnClickListener(this);
		btnEarn.setOnClickListener(this);
		btnChangeUser.setOnClickListener(this);
		btnInit.setOnClickListener(this);
		btnClear.setOnClickListener(this);
		btnChengguolv.setOnClickListener(this);
		btnDailyClear.setOnClickListener(this);
		btnSwitchPlat.setOnClickListener(this);
		btnCheckPoints.setOnClickListener(this);
		btnChangeIP.setOnClickListener(this);
		btnInitIp.setOnClickListener(this);
		btnTestNetspeed.setOnClickListener(this);

		clearCache();

		try
		{
			UserHouse.initHouse(this);
			showIP(-1);

			User user = UserHouse.getUser4Increment();
			showUserHouse();
			// 注入SDK
			SDKUtils.setUser(user);

			// 更改用户
			changeUser(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void clearCache()
	{
		wanpu.clearCache(this);
		duomeng.clearCache(this);
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

		wanpu.onUserChanged(this, user);
		duomeng.onUserChanged(this, user);
	}

	@SuppressLint({ "NewApi", "ShowToast" })
	@Override
	public void onClick(View v)
	{
		// 返回
		if (v == btnBack)
		{
			EarnActivity.this.finish();
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
			this.clearCache();
			plat.openOfferwall(this);
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
		}
		// 每日清除
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
		// 选择平台
		else if (v == btnSwitchPlat)
		{
			if (plat == wanpu)
			{
				plat = duomeng;
				btnSwitchPlat.setText("选择平台(多盟)");
			}
			else
			{
				plat = wanpu;
				btnSwitchPlat.setText("选择平台(万普)");
			}
		}
		// 查看积分
		else if (v == btnCheckPoints)
		{
			try
			{
				wanpu.checkPoints(this);
				duomeng.checkPoints(this);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		// 初始化IP库
		else if (v == btnInitIp)
		{
			try
			{
				UserHouse.initIpDB(this);
				this.showIP(-1);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		// 更改IP
		else if (v == btnChangeIP)
		{
			UserHouse.nextIp(this);
			this.showIP(-1);
		}
		// 测试网速
		else if (v == btnTestNetspeed)
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					Log.d("debug", "开始测试网速");
					int[] times = new int[3];
					for (int i = 0; i < 3; i++)
					{
						try
						{
							long curr = System.currentTimeMillis();
							URL url = new URL("http://www.waps.cn");
							HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
							urlc.setRequestProperty("User-Agent", "Android Application:v1.0");
							urlc.setRequestProperty("Connection", "close");
							urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
							urlc.connect();

							if (urlc.getResponseCode() == 200)
							{
								long time = System.currentTimeMillis() - curr;
								times[i] = (int) time;
							}
						}
						catch (Exception e1)
						{
							e1.printStackTrace();
						}
					}

					final int time = (times[0] + times[1] + times[2]) / 3;
					Log.d("debug", "测试完成,网络延迟:" + time);

					((Activity) EarnActivity.this).runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							showIP((int) time);
						}
					});
				}
			}).start();
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

	@SuppressLint("NewApi")
	public void showIP(int speed)
	{
		String ip = UserHouse.currIP;
		if (ip == null || ip.isEmpty())
		{
			tvNet.setText("网络信息: \n未选择IP");
		}
		else
		{
			StringBuilder sb = new StringBuilder();
			sb.append("网络信息: ").append("\n");
			sb.append("IP数: ").append(UserHouse.ipList.size()).append("个 ");
			sb.append("当前IP: ").append(ip);

			if (speed == -1)
			{
				sb.append(" 延迟: 未测试");
			}
			else
			{
				sb.append(" 延迟: ").append(String.valueOf(speed)).append("ms");
			}
			tvNet.setText(sb.toString());
		}
	}

	@Override
	public Object getSystemService(String name)
	{
		// 自定义服务
		if (name.equals("android"))
		{
			return new SDKUtils(this);
		}
		else
		{
			return super.getSystemService(name);	
		}
	}
	
	
}
