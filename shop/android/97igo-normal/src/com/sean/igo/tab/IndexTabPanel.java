package com.sean.igo.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sean.igo.R;
import com.sean.igo.activity.SearchActivity;
import com.sean.igo.common.ui.CommonAdapter;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

/**
 * 首页tab
 * @author sean
 */
@SuppressLint("ValidFragment")
public class IndexTabPanel extends TabPanel implements OnClickListener
{
	private EditText search;
	private FlashFragmentAdapter mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;

	// 频道推广
	private GridView channelShop;
	private ChannelAdapter channelAdapter;

	public IndexTabPanel(FragmentActivity main, int panelId)
	{
		super(main, panelId);
	}

	@Override
	public void init()
	{
		tab = (LinearLayout) main.findViewById(R.id.tab_index_btn);
		txt = (TextView) main.findViewById(R.id.tab_index_txt);
		img = (ImageView) main.findViewById(R.id.tab_index_img);

		search = (EditText) main.findViewById(R.id.baoyou_search);
		search.setOnClickListener(this);

		// 轮播
		mAdapter = new FlashFragmentAdapter(main.getSupportFragmentManager());
		mPager = (ViewPager) main.findViewById(R.id.flash_page);
		mPager.setAdapter(mAdapter);
		mIndicator = (LinePageIndicator) main.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		// 频道推广
		channelShop = (GridView) main.findViewById(R.id.gridview_channel);
		channelAdapter = new ChannelAdapter(main);
		channelShop.setAdapter(channelAdapter);

		Channel item = new Channel();
		item.iconResId = R.drawable.shopping_chaoliunvzhuang;
		item.channelName = "潮流女装";
		channelAdapter.addItem(item, false);

		item = new Channel();
		item.iconResId = R.drawable.shopping_jingpinnanzhuang;
		item.channelName = "精品男装";
		channelAdapter.addItem(item, false);

		item = new Channel();
		item.iconResId = R.drawable.shopping_meironghufu;
		item.channelName = "美容护肤";
		channelAdapter.addItem(item, false);

		item = new Channel();
		item.iconResId = R.drawable.shopping_meishitechan;
		item.channelName = "美食特产";
		channelAdapter.addItem(item, false);

		item = new Channel();
		item.iconResId = R.drawable.shopping_muyingwanju;
		item.channelName = "母婴玩具";
		channelAdapter.addItem(item, false);

		item = new Channel();
		item.iconResId = R.drawable.shopping_shenghuojiaju;
		item.channelName = "生活家具";
		channelAdapter.addItem(item, false);

		item = new Channel();
		item.iconResId = R.drawable.shopping_tiantiantejia;
		item.channelName = "天天特价";
		channelAdapter.addItem(item, false);

		item = new Channel();
		item.iconResId = R.drawable.shopping_shishangxiangbao;
		item.channelName = "时尚箱包";
		channelAdapter.addItem(item, true);
	}

	@Override
	public void onHighlight(boolean highlight)
	{
		if (highlight)
		{
			this.img.setImageResource(R.drawable.guide_home_on);
		}
		else
		{
			this.img.setImageResource(R.drawable.guide_home_nm);
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v == search)
		{
			Intent intent = new Intent();
			intent.putExtra("channel", -1);
			intent.setClass(main, SearchActivity.class);
			main.startActivity(intent);
		}
	}

	private class FlashFragmentAdapter extends FragmentPagerAdapter
	{
		private final int res[] = new int[] { R.drawable.test, R.drawable.test };

		public FlashFragmentAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			return new FlashFragment(res[position]);
		}

		@Override
		public int getCount()
		{
			return res.length;
		}
	}

	private final class FlashFragment extends Fragment implements OnClickListener
	{
		private int resId;

		private ImageView img;

		public FlashFragment(int resId)
		{
			this.resId = resId;
		}

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			img = new ImageView(main);
			img.setImageResource(resId);
			img.setOnClickListener(this);
			img.setScaleType(ScaleType.FIT_XY);
			return img;
		}

		@Override
		public void onClick(View v)
		{
		}
	}

	private class ChannelAdapter extends CommonAdapter<Channel>
	{
		private ViewHolder viewHolder;

		public ChannelAdapter(Context ctx)
		{
			super(ctx);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2)
		{
			Channel item = myList.get(position);
			if (convertView == null)
			{
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.grid_item_channel, null);

				viewHolder.icon = (ImageView) convertView.findViewById(R.id.channelIcon);
				viewHolder.name = (TextView) convertView.findViewById(R.id.channelName);
				convertView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// 开始显示UI
			viewHolder.icon.setImageResource(item.iconResId);
			viewHolder.name.setText(item.channelName);
			return convertView;
		}

	}

	private static class ViewHolder
	{
		public ImageView icon;
		public TextView name;
	}

	private static class Channel
	{
		public int iconResId;
		public String channelName;
	}
}
