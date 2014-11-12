package com.sean.wanpu;

import java.util.Random;

public class UserUtil
{
	public static void main(String[] args)
	{
		for (int i = 0; i < 100; i++)
		{
			String userinfo = genImei() + " " + genImsi() + " " + genMac();
			System.out.println("users.add(createUser(\"" + userinfo + "\"));");	
		}
	}

	public static String genImei()
	{
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 15; i++)
		{
			sb.append(r.nextInt(9));
		}
		return sb.toString();
	}

	public static String genImsi()
	{
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 15; i++)
		{
			sb.append(r.nextInt(9));
		}
		return sb.toString();
	}

	private static String chars = "1234567890ABCDEF";

	public static String genMac()
	{
		Random r = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 6; i++)
		{
			sb.append(chars.charAt(r.nextInt(chars.length())));
			sb.append(chars.charAt(r.nextInt(chars.length())));
			sb.append(":");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
