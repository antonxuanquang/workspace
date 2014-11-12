package com.sean.igo.activity;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.sean.igo.R;
import com.sean.igo.adapter.GoodListAdapter;
import com.sean.igo.common.http.HttpUtil;
import com.sean.igo.common.http.Request;
import com.sean.igo.common.http.Response;
import com.sean.igo.common.http.Result;
import com.sean.igo.common.img.Good;

/**
 * 搜索
 * @author sean
 */
public class SearchResultActivity extends FragmentActivity implements OnClickListener
{
	private PullToRefreshListView pulllist;
	private ListView listview;
	private GoodListAdapter adapter;
	private ImageView btnBack;

	private int channel;
	private String keyword;
	private int pageNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

		this.channel = this.getIntent().getIntExtra("channel", -1);
		this.keyword = this.getIntent().getStringExtra("keyword");

		btnBack = (ImageView) findViewById(R.id.back);
		btnBack.setOnClickListener(this);

		pulllist = (PullToRefreshListView) findViewById(R.id.goodlist);
		pulllist.setMode(Mode.PULL_FROM_END);
		pulllist.setOnLastItemVisibleListener(new OnLastItemVisibleListener()
		{
			@Override
			public void onLastItemVisible()
			{
				pageNo++;
				loadGoods();
			}
		});
		listview = pulllist.getRefreshableView();
		adapter = new GoodListAdapter(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> av, View view, int position, long id)
			{
				if (position <= adapter.getCount())
				{
					Good item = (Good) adapter.getItem((int) position - 1);

					Intent intent = new Intent();
					intent.putExtra("title", "商品详细页面");
					intent.putExtra("url", item.goodUrl);
					intent.setClass(SearchResultActivity.this, WebActivity.class);
					startActivity(intent);
				}
			}
		});
		loadGoods();
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnBack)
		{
			SearchResultActivity.this.finish();
		}
	}

	/**
	 * 加载商品
	 */
	private void loadGoods()
	{
		Request request = new Request("v1/SearchGood");
		request.setParameter("pageNo", pageNo);
		if (channel != -1)
		{
			request.setParameter("channel", channel);	
		}
		request.setParameter("ranking", 1);
		request.setParameter("keyword", keyword);
		HttpUtil.request(this, request, new Response()
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
				adapter.addItems(items, true);
				pulllist.onRefreshComplete();
			}

			@Override
			public void onCancel(Context context, Request request)
			{
				pulllist.onRefreshComplete();
			}
		});
	}

}
