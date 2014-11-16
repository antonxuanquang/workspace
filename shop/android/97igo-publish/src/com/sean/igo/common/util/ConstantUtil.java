package com.sean.igo.common.util;

import android.os.Environment;

public class ConstantUtil
{
	// 新浪认证
	public static final String SINA_APP_KEY = "3683525777";
	public static final String SINA_SECERT = "a4cb3aff1caeb34e2eb5a26c0eb4bd4c";
	public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";// 应用的回调页
	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

	// 微信appkey
	public static final String WX_APP_KEY = "wxfd376d6e4955fb6c";
	// QQ appkey
	public static final String QQ_APP_KEY = "1101775959";
	
	public static final String RootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bigcode/";
	public static final String ImageDir = RootDir + "images/";
	public static final String CacheDir = "/bigcode/cache/";
}
