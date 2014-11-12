package com.sean.igo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sean.igo.R;
import com.sean.igo.common.img.Good;
import com.sean.igo.common.img.ImageUtil;
import com.sean.igo.common.ui.CommonAdapter;

/**
 * 商品列表adapter
 * @author sean
 */
public class GoodListAdapter extends CommonAdapter<Good>
{
	private ViewHolder viewHolder;

	public GoodListAdapter(Context context)
	{
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2)
	{
		Good item = myList.get(position);
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_good, null);

			viewHolder.goodImage = (ImageView) convertView.findViewById(R.id.goodImage);
			viewHolder.goodName = (TextView) convertView.findViewById(R.id.goodName);
			viewHolder.price = (TextView) convertView.findViewById(R.id.price);
			viewHolder.signal = (TextView) convertView.findViewById(R.id.signal);
			viewHolder.saleCount = (TextView) convertView.findViewById(R.id.saleCount);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.goodName.setText(item.goodName);
		viewHolder.price.setText("¥ " + item.price);

		if (item.channel == 1)
		{
			viewHolder.signal.setText("包邮");
		}
		else if (item.channel == 2)
		{
			viewHolder.signal.setText("服饰");
		}

		ImageLoader.getInstance().displayImage(item.imageUrl + "_150x150.jpg", viewHolder.goodImage,
				ImageUtil.getImageLoaderOption());

		return convertView;
	}

	public static class ViewHolder
	{
		public ImageView goodImage;
		public TextView goodName, price, signal, saleCount;
	}

}
