package com.sean.igo.activity;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sean.igo.R;
import com.sean.igo.common.http.HttpUtil;
import com.sean.igo.common.http.Request;
import com.sean.igo.common.http.Response;
import com.sean.igo.common.http.Result;
import com.sean.igo.common.ui.CommonAdapter;
import com.sean.igo.common.ui.DisplayUtil;

/**
 * 搜索
 * @author sean
 */
public class SearchActivity extends FragmentActivity implements OnClickListener
{
	private ImageView btnBack;
	private GridView gridview;
	private HotwordGridAdapter hotwordAdapter;
	private EditText search;

	private int channel = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

		channel = this.getIntent().getIntExtra("channel", -1);

		btnBack = (ImageView) findViewById(R.id.back);
		btnBack.setOnClickListener(this);
		this.gridview = (GridView) findViewById(R.id.gridview_hotworld);

		this.search = (EditText) this.findViewById(R.id.search);
		search.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@SuppressLint("NewApi")
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					/*隐藏软键盘*/
					InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
							Context.INPUT_METHOD_SERVICE);
					if (imm.isActive())
					{
						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
					}

					String keyword = search.getText().toString().trim();
					if (!keyword.isEmpty())
					{
						Intent intent = new Intent();
						intent.putExtra("channel", channel);
						intent.putExtra("keyword", keyword);
						intent.setClass(SearchActivity.this, SearchResultActivity.class);
						startActivity(intent);
					}
				}
				return false;
			}
		});

		hotwordAdapter = new HotwordGridAdapter(this);
		gridview.setAdapter(hotwordAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				String keyword = (String) hotwordAdapter.getItem(position);
				Intent intent = new Intent();
				intent.putExtra("channel", channel);
				intent.putExtra("keyword", keyword);
				intent.setClass(SearchActivity.this, SearchResultActivity.class);
				startActivity(intent);
			}
		});

		Request request = new Request("v1/InquireHotword");
		HttpUtil.request(this, request, new Response()
		{
			@Override
			public void callback(Context context, Request request, Result result)
			{
				JSONArray array = result.data.getJSONArray("hotwordList");
				List<String> words = new LinkedList<String>();
				for (int i = 0; i < array.size(); i++)
				{
					JSONObject it = array.getJSONObject(i);
					words.add(it.getString("hotword"));
				}
				hotwordAdapter.addItems(words, true);
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnBack)
		{
			SearchActivity.this.finish();
		}
	}

	public class HotwordGridAdapter extends CommonAdapter<String>
	{
		public HotwordGridAdapter(Context ctx)
		{
			super(ctx);
		}

		private ViewHolder viewHolder;

		@Override
		public View getView(int position, View convertView, ViewGroup arg2)
		{
			String item = myList.get(position);
			if (convertView == null)
			{
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.grid_item_tag, null);

				viewHolder.hotworld = (TextView) convertView.findViewById(R.id.hotword);
				viewHolder.hotworld.getLayoutParams().width = DisplayUtil.getScreenWidthPixels() / 3;
				convertView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// 开始显示UI
			viewHolder.hotworld.setText(item);
			return convertView;
		}

	}

	public static class ViewHolder
	{
		public TextView hotworld;
	}

}
