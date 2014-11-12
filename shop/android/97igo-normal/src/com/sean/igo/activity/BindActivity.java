package com.sean.igo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sean.igo.R;
import com.sean.igo.common.util.PropertyUtil;

/**
 * 反馈意见
 * @author sean
 */
public class BindActivity extends Activity implements OnClickListener
{
	private ImageView btnBack;
	private TextView btnBind;
	private EditText etUsername, etName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);
		this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

		btnBack = (ImageView) findViewById(R.id.back);
		btnBind = (TextView) findViewById(R.id.btn_bind);
		etUsername = (EditText) findViewById(R.id.username);
		etName = (EditText) findViewById(R.id.name);

		btnBack.setOnClickListener(this);
		btnBind.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnBack)
		{
			BindActivity.this.finish();
		}
		else if (v == btnBind)
		{
			String username = etUsername.getText().toString();
			String name = etName.getText().toString();
			Log.d("debug", username + name);

			if (username.length() > 0 && name.length() > 0)
			{
				PropertyUtil.setUserProp(this, PropertyUtil.User_ZfbUser, username);
				PropertyUtil.setUserProp(this, PropertyUtil.User_ZfbName, name);
				Toast.makeText(this, "成功绑定", Toast.LENGTH_SHORT).show();
				BindActivity.this.finish();
			}
			else
			{
				Toast.makeText(this, "请绑定正确支付宝信息,以免影响提现", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
