package com.sean.im.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.sean.im.client.constant.Global;
import com.sean.im.commom.entity.UserInfo;

/**
 * 用户工具
 * @author sean
 */
public class IMIoUtil
{
	/**
	 * 写用户信息
	 * @param user
	 */
	public static void writeUser(UserInfo user)
	{
		File dir = new File(Global.Root + "users/" + user.getUsername());
		checkDir(dir);
		ObjectOutputStream output = null;
		try
		{
			File info = new File(Global.Root + "users/" + user.getUsername() + "/info");
			output = new ObjectOutputStream(new FileOutputStream(info));
			output.writeObject(user);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				output.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取用户信息
	 * @param username
	 * @return
	 */
	public static UserInfo readUser(String username)
	{
		File dir = new File(Global.Root + "users/" + username);
		checkDir(dir);
		ObjectInputStream input = null;
		try
		{
			File info = new File(Global.Root + "users/" + username + "/info");
			if (!info.exists())
			{
				return null;
			}
			input = new ObjectInputStream(new FileInputStream(info));
			return (UserInfo) input.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (input != null)
				{
					input.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 读取全部用户信息
	 * @return
	 */
	public static List<String> readAllUser()
	{
		File usersDir = new File(Global.Root + "users");
		checkDir(usersDir);
		File[] dirs = usersDir.listFiles();
		List<String> users = new ArrayList<String>(dirs.length);
		for (File dir : dirs)
		{
			if (!new File(dir.getAbsolutePath() + "/info").exists())
			{
				dir.delete();
				continue;
			}
			users.add(dir.getName());
		}
		return users;
	}
	
	public static void checkUserFileDir(String username)
	{
		File dir = new File(Global.Root + "users");
		checkDir(dir);
		dir = new File(Global.Root + "users/" + username);
		checkDir(dir);
		dir = new File(Global.Root + "users/" + username + "/recv");
		checkDir(dir);
		dir = new File(Global.Root + "users/" + username + "/recv/file");
		checkDir(dir);
	}
	
	public static void checkUserImageDir(String username)
	{
		File dir = new File(Global.Root + "users");
		checkDir(dir);
		dir = new File(Global.Root + "users/" + username);
		checkDir(dir);
		dir = new File(Global.Root + "users/" + username + "/recv");
		checkDir(dir);
		dir = new File(Global.Root + "users/" + username + "/recv/image");
		checkDir(dir);
	}
	
	public static void checkUserAudioDir(String username)
	{
		File dir = new File(Global.Root + "users");
		checkDir(dir);
		dir = new File(Global.Root + "users/" + username);
		checkDir(dir);
		dir = new File(Global.Root + "users/" + username + "/recv");
		checkDir(dir);
		dir = new File(Global.Root + "users/" + username + "/recv/audio");
		checkDir(dir);
	}
	
	public static void checkTmpDir()
	{
		File dir = new File(Global.Root + "tmp");
		checkDir(dir);
	}

	private static void checkDir(File dir)
	{
		if (dir.exists() && dir.isFile())
		{
			dir.delete();
		}
		if (!dir.exists())
		{
			dir.mkdir();
		}
	}
}
