package com.sean.igo.tab;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sean.igo.activity.SearchActivity;
import com.sean.igo.activity.WebActivity;
import com.sean.igo.adapter.GoodListAdapter;
import com.sean.igo.common.http.HttpUtil;
import com.sean.igo.common.http.Request;
import com.sean.igo.common.http.Response;
import com.sean.igo.common.http.Result;
import com.sean.igo.common.img.Good;
import com.sean.igou.R;

/**
 * 9.9包邮
 * @author sean
 */
public class BaoyouTabPanel extends TabPanel implements OnClickListener
{
	private PullToRefreshListView baoyou_pulllist;
	private ListView baoyou_listview;
	private GoodListAdapter baoyou_adapter;
	private EditText search;

	private int pageIndex = 1;

	public BaoyouTabPanel(FragmentActivity main, int panel)
	{
		super(main, panel);
	}

	@Override
	public void init()
	{
		tab = (LinearLayout) main.findViewById(R.id.tab_baoyou_btn);
		txt = (TextView) main.findViewById(R.id.tab_baoyou_txt);
		img = (ImageView) main.findViewById(R.id.tab_baoyou_img);

		search = (EditText) main.findViewById(R.id.baoyou_search);
		search.setOnClickListener(this);

		baoyou_pulllist = (PullToRefreshListView) main.findViewById(R.id.baoyou_goodlist);
		baoyou_pulllist.setMode(Mode.PULL_FROM_END);
		baoyou_pulllist.setOnLastItemVisibleListener(new OnLastItemVisibleListener()
		{
			@Override
			public void onLastItemVisible()
			{
				pageIndex++;
				loadGoods();
			}
		});

		baoyou_listview = baoyou_pulllist.getRefreshableView();
		baoyou_adapter = new GoodListAdapter(main);
		baoyou_listview.setAdapter(baoyou_adapter);
		baoyou_listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
			{
				if (position <= baoyou_adapter.getCount())
				{
					Good item = (Good) baoyou_adapter.getItem((int) position - 1);

					Intent intent = new Intent();
					intent.putExtra("title", "商品详细页面");
					intent.putExtra("url", item.goodUrl);
					intent.setClass(main, WebActivity.class);
					main.startActivity(intent);
				}
			}
		});
		pageIndex = 1;
		loadGoods();
	}

	@Override
	public void onHighlight(boolean highlight)
	{
		if (highlight)
		{
			this.img.setImageResource(R.drawable.guide_nearby_on);
		}
		else
		{
			this.img.setImageResource(R.drawable.guide_nearby_nm);
		}
	}

	/**
	 * 加载商品
	 */
	private void loadGoods()
	{
		Request request = new Request("v1/InquireGoodList");
		request.setParameter("pageNo", pageIndex);
		request.setParameter("channel", 1);
		HttpUtil.request(main, request, new Response()
		{
			@Override
			public void callback(Context context, Request request, Result result)
			{
				List<Good> items = new LinkedList<Good>();
				JSONArray list = result.data.getJSONArray("goodList");
				for (int i = 0; i < list.size(); i++)
				{
					JSONObject obj = list.getJSONObject(i);
					String json = obj.toJSONString();
					Good item = JSON.parseObject(json, Good.class);
					items.add(item);
				}
				baoyou_adapter.addItems(items, true);
				baoyou_pulllist.onRefreshComplete();
			}

			@Override
			public void onCancel(Context context, Request request)
			{
				baoyou_pulllist.onRefreshComplete();
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		if (v == search)
		{
			Intent intent = new Intent();
			intent.putExtra("channel", 1);
			intent.setClass(main, SearchActivity.class);
			main.startActivity(intent);
		}
	}
}
