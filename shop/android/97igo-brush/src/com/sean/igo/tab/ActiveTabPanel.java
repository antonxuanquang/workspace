package com.sean.igo.tab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sean.igo.activity.WebActivity;
import com.sean.igo.common.http.HttpUtil;
import com.sean.igo.common.http.Request;
import com.sean.igo.common.http.Response;
import com.sean.igo.common.http.Result;
import com.sean.igo.common.img.Active;
import com.sean.igo.common.img.ImageUtil;
import com.sean.igo.common.ui.CommonAdapter;
import com.sean.igo.common.ui.DisplayUtil;
import com.sean.igou.R;

/**
 * 活动tab
 * @author sean
 */
public class ActiveTabPanel extends TabPanel implements OnClickListener
{
	private GridView gridview;
	private ActiveGridAdapter adapter;

	public ActiveTabPanel(FragmentActivity main, int panel)
	{
		super(main, panel);
	}

	@Override
	public void init()
	{
		tab = (LinearLayout) main.findViewById(R.id.tab_active_btn);
		txt = (TextView) main.findViewById(R.id.tab_active_txt);
		img = (ImageView) main.findViewById(R.id.tab_active_img);

		gridview = (GridView) main.findViewById(R.id.gridview_active);
		adapter = new ActiveGridAdapter(main);

		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> ad, View view, int position, long id)
			{
				Active item = (Active) adapter.getItem(position);

				Intent intent = new Intent();
				intent.putExtra("title", "淘宝活动");
				intent.putExtra("url", item.activeUrl);
				intent.setClass(main, WebActivity.class);
				main.startActivity(intent);
			}
		});

		loadActives();
	}

	/**
	 * 加载商品
	 */
	private void loadActives()
	{
		Request request = new Request("v1/InquireActiveList");
		request.setParameter("pageNo", 1);
		request.setParameter("activing", 1);
		HttpUtil.request(main, request, new Response()
		{
			@Override
			public void callback(Context context, Request request, Result result)
			{
				List<Active> items = new LinkedList<Active>();
				JSONArray list = result.data.getJSONArray("activeList");
				for (int i = 0; i < list.size(); i++)
				{
					JSONObject obj = list.getJSONObject(i);
					String json = obj.toJSONString();
					Active item = JSON.parseObject(json, Active.class);
					items.add(item);
				}
				adapter.addItems(items, true);
			}
		});
	}

	@Override
	public void onHighlight(boolean highlight)
	{
		if (highlight)
		{
			this.img.setImageResource(R.drawable.guide_tfaccount_on);
		}
		else
		{
			this.img.setImageResource(R.drawable.guide_tfaccount_nm);
		}
	}

	@Override
	public void onClick(View v)
	{
	}
	
	@SuppressLint("SimpleDateFormat")
	public class ActiveGridAdapter extends CommonAdapter<Active>
	{
		private ViewHolder viewHolder;
		private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

		public ActiveGridAdapter(Context ctx)
		{
			super(ctx);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2)
		{
			Active item = myList.get(position);
			if (convertView == null)
			{
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.grid_item_active, null);

				viewHolder.activeName = (TextView) convertView.findViewById(R.id.activeName);
				viewHolder.activeImage = (ImageView) convertView.findViewById(R.id.activeImage);
				viewHolder.activeDate = (TextView) convertView.findViewById(R.id.activeDate);
				convertView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// 开始显示UI
			viewHolder.activeName.setText(item.activeName);
			viewHolder.activeDate.setText(getDate(item.startDate) + "到" + getDate(item.endDate));
			viewHolder.activeImage.getLayoutParams().height = (DisplayUtil.getScreenWidthPixels() - 12) / 2;
			ImageLoader.getInstance().displayImage(item.imageUrl, viewHolder.activeImage, ImageUtil.getImageLoaderOption());
			return convertView;
		}

		private String getDate(int date)
		{
			try
			{
				return sdf2.format(sdf1.parse(String.valueOf(date)));
			}
			catch (ParseException e)
			{
				return String.valueOf(date);
			}
		}
	}
	
	public static class ViewHolder
	{
		public TextView activeName, activeDate;
		public ImageView activeImage;
	}
}
