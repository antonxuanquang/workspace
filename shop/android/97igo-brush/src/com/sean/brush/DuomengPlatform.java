package com.sean.brush;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;
import cn.domob.data.OErrorInfo;
import cn.domob.data.OManager;
import cn.domob.data.SqlLiteUtil;
import cn.domob.data.n;
import cn.waps.User;

public class DuomengPlatform extends Platform
{
	private OManager app;

	@Override
	public void clearCache(Context context)
	{
		// 清空sharepreference
		String[] cache = new String[] { "idpkg", "idname", "RP" };
		for (int i = 0; i < cache.length; i++)
		{
			SharedPreferences props = context.getSharedPreferences(cache[i], Context.MODE_PRIVATE);
			Editor editor = props.edit();
			editor.clear();
			editor.commit();

			Log.d("debug", "多盟:删除SharedPreferences " + cache[i]);
		}

		// 清除数据库
		new SqlLiteUtil(context).clear();
		Log.d("debug", "多盟:清空sqlite");
	}

	@Override
	public void onUserChanged(final Context context, User user) throws Exception
	{
		this.closeOfferwall(context);
		
		app = new OManager(context, "96ZJ1/5AzeGQbwTCmq");
		// 检查积分
		app.setCheckPointsListener(new OManager.CheckPointsListener()
		{
			@Override
			public void onCheckPointsSucess(final int point, final int consumed)
			{
				((Activity) context).runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						Toast.makeText(context, "多盟积分:" + point, Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void onCheckPointsFailed(final OErrorInfo e)
			{
			}
		});
		Log.d("debug", "多盟初始化完毕");
	}

	@Override
	public void openOfferwall(Context context)
	{	
		if (app != null)
		{
			app.loadOfferWall();	
		}
	}

	@Override
	public void closeOfferwall(Context context) throws Exception
	{
		if (app != null)
		{
			Field n = app.getClass().getDeclaredField("mODataManager");
			n.setAccessible(true);
			n mODataManager = (n) n.get(app);

			if (mODataManager != null)
			{
				mODataManager.e();

				String[] fs = new String[] { "i", "k" };
				for (String it : fs)
				{
					Field item = n.class.getDeclaredField(it);
					item.setAccessible(true);
					Dialog dialog = (Dialog) item.get(mODataManager);
					if (dialog != null)
					{
						dialog.dismiss();
					}
				}
			}
		}
	}

	@Override
	public void checkPoints(Context context) throws Exception
	{
		if (app != null)
		{
			app.checkPoints();
		}
	}

}
