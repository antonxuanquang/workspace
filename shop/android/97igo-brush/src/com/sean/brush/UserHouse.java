package com.sean.brush;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Environment;

import com.alibaba.fastjson.JSON;

public class UserHouse
{
	/**
	 * part1.降低成果率用户(该部分用户不用来下载, 只是用来降低成果率)
	 * part2.已经下载过的用户(该部分用户可以用来下载, 并用来提高活跃度)
	 * part3.剩余用户库
	 */
	public static List<User> part1, part2, part3;
	public static List<String> ipList = new LinkedList<String>();
	public static final Random random = new Random();

	public static final String RootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/97igo/";
	public static final File file_part1 = new File(RootDir + "part1");
	public static final File file_part2 = new File(RootDir + "part2");
	public static final File file_part3 = new File(RootDir + "part3");

	public static String currIP;

	/**
	 * 从序列化文件中读取用户信息
	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	public static void initHouse(Context context) throws Exception
	{
		// sd卡没有用户数据, 从软件包中写入sd卡
		if (!check())
		{
			InputStream part1 = context.getAssets().open("part1");
			writeToFile(part1, file_part1);
			part1.close();

			InputStream part3 = context.getAssets().open("part3");
			writeToFile(part3, file_part3);
			part3.close();
		}

		// 读取用户信息
		String part1Json = FileUtils.readFileToString(file_part1);
		part1 = JSON.parseArray(part1Json, User.class);

		String part3Json = FileUtils.readFileToString(file_part3);
		part3 = JSON.parseArray(part3Json, User.class);

		part2 = new ArrayList<User>();
		if (file_part2.exists())
		{
			String part2Json = FileUtils.readFileToString(file_part2);
			if (!part2Json.isEmpty())
			{
				part2 = JSON.parseArray(part2Json, User.class);
			}
		}

		initIpDB(context);
	}

	public static void initIpDB(Context context) throws IOException
	{
		// 读取IP列表
		ipList = FileUtils.readLines(new File(RootDir + "ip.txt"), "utf-8");
	}

	public static User getTestUser() throws Exception
	{
		// 861466010216571	460030938312273	0a:cd:54:d8:c3:5f	2.3.6	G12	480	800	htc
		User user = new User();
		user.imei = "861466010216571";
		user.imsi = "460030938312273";
		user.mac = "0a:cd:54:d8:c3:5f";
		user.brand = "htc";
		user.model = "G12";
		user.version = "2.3.6";
		user.screenHeight = "800";
		user.screenWidth = "480";
		return user;
	}

	/**
	 * 从part2 / part3 随机读取用户
	 * @return
	 * @throws Exception 
	 */
	public static User getUser4Brush() throws Exception
	{
		//		int part = random.nextInt(2);
		//		// 随机返回part2用户
		//		if (part == 0)
		//		{
		//			return part2.get(random.nextInt(part2.size()));
		//		}
		//		// 随机返回part3用户, 并将part3用户移到part2用户群中
		//		else
		//		{
		//			User user = part3.remove(0);
		//			part2.add(user);
		//
		//			// 写入磁盘序列化文件
		//			write();
		//			return user;
		//		}

		User user = part3.remove(random.nextInt(part3.size()));
		part2.add(user);

		// 写入磁盘序列化文件
		write();
		return user;
	}

	/**
	 * 从part1 / part2 随机读取用户
	 * @return
	 */
	public static User getUser4Increment()
	{
		//		int part = random.nextInt(2);
		//		// 随机返回part1用户
		//		if (part == 0)
		//		{
		//			return part1.get(random.nextInt(part1.size()));
		//		}
		//		// 随机返回part2用户
		//		else
		//		{
		//			return part2.get(random.nextInt(part2.size()));
		//		}

		return part1.get(random.nextInt(part1.size()));
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static String nextIp(final Context context)
	{
		if (!ipList.isEmpty())
		{
			String ip = ipList.remove(0);
			StringBuilder buf = new StringBuilder();
			// 覆盖ip列表
			for (String it : ipList)
			{
				buf.append(it).append("\n");
			}
			try
			{
				FileUtils.writeStringToFile(new File(RootDir + "ip.txt"), buf.toString(), false);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			currIP = ip;

			if (ip != null)
			{
				ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				String[] tmp = ip.split(":");
				clipboard.setText(tmp[0]);
			}
			return ip;
		}
		return null;
	}

	public static void resetPart2() throws Exception
	{
		part3.addAll(part2);
		part2.clear();
		write();
	}

	private static boolean check() throws Exception
	{
		File root = new File(RootDir);
		if (!root.exists())
		{
			root.mkdirs();
		}

		if (!file_part1.exists())
		{
			return false;
		}

		if (!file_part3.exists())
		{
			return false;
		}

		return true;
	}

	private static void write() throws Exception
	{
		File root = new File(RootDir);
		if (!root.exists())
		{
			root.mkdirs();
		}

		// 覆盖part1
		String str1 = JSON.toJSONString(part1);
		FileUtils.writeStringToFile(file_part1, str1, false);

		// 覆盖part2
		String str2 = JSON.toJSONString(part2);
		FileUtils.writeStringToFile(file_part2, str2, false);

		// 覆盖part1
		String str3 = JSON.toJSONString(part3);
		FileUtils.writeStringToFile(file_part3, str3, false);
	}

	private static void writeToFile(InputStream input, File target) throws Exception
	{
		if (target.exists())
		{
			target.delete();
		}

		List<User> list = new ArrayList<User>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			User user = new User();
			user.parse(line);

			list.add(user);
		}
		reader.close();

		FileUtils.writeStringToFile(target, JSON.toJSONString(list), false);
	}
}
