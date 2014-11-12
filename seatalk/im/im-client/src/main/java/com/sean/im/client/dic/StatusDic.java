package com.sean.im.client.dic;

import com.sean.im.commom.constant.StatusEnum;

public class StatusDic
{
	private static String[] statusDic = new String[6];
	static
	{
		statusDic[0] = "";
		statusDic[StatusEnum.OffLine] = "离线";
		statusDic[StatusEnum.Online] = "在线";
		statusDic[StatusEnum.Leave] = "离开";
		statusDic[StatusEnum.Hide] = "隐身";
		statusDic[StatusEnum.Disable] = "冻结";
	}

	public static String getStatus(int statusId)
	{
		return statusDic[statusId];
	}
}
