package com.sean.igo.tab;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.dm.android.DMOfferWall;
import cn.waps.AppConnect;

import com.sean.igou.R;

/**
 * 服饰tab
 * @author sean
 */
public class MeTabPanel extends TabPanel implements OnClickListener
{
	// 我的
	private TextView me_profile;
	private TextView me_earn, me_duomeng;
	private TextView me_setting;

	public MeTabPanel(FragmentActivity main, int panel)
	{
		super(main, panel);
	}

	@Override
	public void init()
	{
		tab = (LinearLayout) main.findViewById(R.id.tab_me_btn);
		txt = (TextView) main.findViewById(R.id.tab_me_txt);
		img = (ImageView) main.findViewById(R.id.tab_me_img);

//		me_profile = (TextView) main.findViewById(R.id.me_profile);
		me_earn = (TextView) main.findViewById(R.id.me_earn);
		me_duomeng = (TextView) main.findViewById(R.id.me_duomeng);
//		me_setting = (TextView) main.findViewById(R.id.me_setting);

//		me_profile.setOnClickListener(this);
		me_earn.setOnClickListener(this);
		me_duomeng.setOnClickListener(this);
//		me_setting.setOnClickListener(this);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onHighlight(boolean highlight)
	{
		if (highlight)
		{
			this.img.setImageResource(R.drawable.guide_account_on);
		}
		else
		{
			this.img.setImageResource(R.drawable.guide_account_nm);
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v == me_profile)
		{
			
		}
		else if (v == me_earn)
		{	
//			Intent intent = new Intent();
//			intent.setClass(main, OfferwallActivity.class);
//			main.startActivity(intent);
			
			AppConnect.getInstance("b04222c03b0afea639a9ff345d73ee27", "waps", main);
			AppConnect.getInstance(main).showAppOffers(main);
		}
		else if (v == me_duomeng)
		{
			DMOfferWall.init(main, "96ZJ1/5AzeGQbwTCmq");
			DMOfferWall.getInstance(main).showOfferWall(main);
		}
		else if (v == me_setting)
		{
			
		}
	}
}
