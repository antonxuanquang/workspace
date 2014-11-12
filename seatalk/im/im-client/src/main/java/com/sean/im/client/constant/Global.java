package com.sean.im.client.constant;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 全局变量
 * @author sean
 */
public class Global
{
	// 项目根目录
	public static String Root = System.getProperty("user.dir") + "/";
	// 深蓝色
	public static Color DarkBlue = new Color(153, 175, 197);
	// 浅蓝色
	public static Color LightBlue = new Color(201, 221, 245);
	// 深绿
	public static Color DarkGreen = new Color(8, 132, 70);
	public static Color BgColor = new Color(248, 248, 248);
	// 浅黄色
	public static Color LightYellow = new Color(247, 251, 183);
	// 深黄色
	public static Color DarkYellow = new Color(224, 228, 158);
	// 边框颜色
	public static Color BorderColor = new Color(35, 3, 120);
	public static Color BorderColor2 = new Color(25, 174, 231);
	// 改变头像时候的临时头像
	public static int tmpHead;
	// 翻译密钥
	public static String Translator = "http://translate.google.cn/translate_a/t?client=t&text={text}&hl=zh-CN&sl={source}&tl={target}&ie=UTF-8&oe=UTF-8&multires=1&prev=conf&psl=zh-CN&ptl=zh-CN&otf=1&it=sel.6041&ssel=0&tsel=0&sc=1";
	// 语言
	public static Map<String, String> Lan;

	public static final boolean Optmized = true;

	/***********************************系统静态全局对象***************************************/
	// icon图标
	public static final ImageIcon ICON = new ImageIcon(Global.Root + "resource/image/icon.png");
	// 鼠标样式
	public static final Cursor CURSOR_DEF = new Cursor(Cursor.DEFAULT_CURSOR);
	public static final Cursor CURSOR_HAND = new Cursor(Cursor.HAND_CURSOR);
	public static final Cursor CURSOR_MOVE = new Cursor(Cursor.MOVE_CURSOR);
	// 字体
	public static final Font FONT = new Font("宋体", Font.PLAIN, 12);
	// 气泡
	public static BufferedImage Bubble_Left, Bubble_Right;
	static
	{
		try
		{
			Bubble_Left = ImageIO.read(new File(Global.Root + "resource/image/bubble/bubble_left.png"));
			Bubble_Right = ImageIO.read(new File(Global.Root + "resource/image/bubble/bubble_right.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
