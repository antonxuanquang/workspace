package com.sean.im.client.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;

/**
 * UI工具类
 * @author sean
 */
public class UIUtil
{
	public static Map<Integer, String> country = new TreeMap<>();
	public static Map<Integer, String> lan = new TreeMap<>();
	public static Map<Integer, String> gender = new TreeMap<>();
	public static Map<Integer, String> age = new TreeMap<>();

	public static void initDic(JSONObject data)
	{
		JSONObject c = data.getJSONObject("country");
		for (String it : c.keySet())
		{
			country.put(Integer.parseInt(it), c.getString(it));
		}

		JSONObject l = data.getJSONObject("lan");
		for (String it : l.keySet())
		{
			lan.put(Integer.parseInt(it), l.getString(it));
		}

		JSONObject g = data.getJSONObject("gender");
		for (String it : g.keySet())
		{
			gender.put(Integer.parseInt(it), g.getString(it));
		}

		JSONObject a = data.getJSONObject("age");
		for (String it : a.keySet())
		{
			age.put(Integer.parseInt(it), a.getString(it));
		}
	}

	public static JMenuItem getMenuItem(String txt, ActionListener listener)
	{
		JMenuItem item = new JMenuItem(txt);
		item.setOpaque(false);
		item.setFont(Global.FONT);
		item.addActionListener(listener);
		return item;
	}

	public static JMenuItem getMenuItem(String txt, ImageIcon icon, ActionListener listener)
	{
		JMenuItem item = new JMenuItem(txt, icon);
		item.setOpaque(false);
		item.setFont(Global.FONT);
		item.addActionListener(listener);
		return item;
	}

	/**
	 * 获取国家下拉框
	 * @return
	 */
	public static JComboBox<ComboBoxItem> getCountryComboBox()
	{
		JComboBox<ComboBoxItem> jcb = new JComboBox<ComboBoxItem>();
		for (int id : country.keySet())
		{
			jcb.addItem(new ComboBoxItem(id, Global.Lan.get(country.get(id))));
		}
		return jcb;
	}

	/**
	 * 获取国家下拉框
	 * @return
	 */
	public static JComboBox<ComboBoxItem> getCountryComboBoxForSearch()
	{
		JComboBox<ComboBoxItem> jcb = new JComboBox<ComboBoxItem>();
		jcb.addItem(new ComboBoxItem(0, Global.Lan.get("不限")));
		for (int id : country.keySet())
		{
			jcb.addItem(new ComboBoxItem(id, Global.Lan.get(country.get(id))));
		}
		return jcb;
	}

	/**
	 * 获取语言下拉框
	 * @return
	 */
	public static JComboBox<ComboBoxItem> getLanguageComboBox()
	{
		JComboBox<ComboBoxItem> jcb = new JComboBox<ComboBoxItem>();
		for (int id : lan.keySet())
		{
			jcb.addItem(new ComboBoxItem(id, Global.Lan.get(lan.get(id))));
		}
		return jcb;
	}

	/**
	 * 获取性别下拉框
	 * @return
	 */
	public static JComboBox<ComboBoxItem> getSexComboBox()
	{
		JComboBox<ComboBoxItem> jcb = new JComboBox<ComboBoxItem>();
		for (int id : gender.keySet())
		{
			jcb.addItem(new ComboBoxItem(id, Global.Lan.get(gender.get(id))));
		}
		return jcb;
	}

	/**
	 * 获取性别下拉框
	 * @return
	 */
	public static JComboBox<ComboBoxItem> getSexComboBoxForSearch()
	{
		JComboBox<ComboBoxItem> jcb = new JComboBox<ComboBoxItem>();
		jcb.addItem(new ComboBoxItem(0, Global.Lan.get("不限")));
		for (int id : gender.keySet())
		{
			jcb.addItem(new ComboBoxItem(id, Global.Lan.get(gender.get(id))));
		}
		return jcb;
	}

	/**
	 * 获取年龄下拉框
	 * @return
	 */
	public static JComboBox<Integer> getAgeComboBox()
	{
		JComboBox<Integer> jcb = new JComboBox<Integer>();
		for (int id : age.keySet())
		{
			jcb.addItem(id);
		}
		return jcb;
	}

	/**
	 * 获取搜索年龄下拉框
	 * @return
	 */
	public static JComboBox<ComboBoxItem> getAgeForSearchComboBox()
	{
		JComboBox<ComboBoxItem> jcb = new JComboBox<ComboBoxItem>();
		jcb.addItem(new ComboBoxItem(0, Global.Lan.get("不限")));
		jcb.addItem(new ComboBoxItem(1, "8-18"));
		jcb.addItem(new ComboBoxItem(2, "19-30"));
		jcb.addItem(new ComboBoxItem(3, "31-50"));
		jcb.addItem(new ComboBoxItem(4, "51-80"));
		return jcb;
	}

	/**
	 * 提示框
	 * @param parent
	 * @param message
	 */
	public static void alert(Component parent, String message)
	{
		JOptionPane.showMessageDialog(parent, message, Global.Lan.get("提示消息"), JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * 提示框
	 * @param parent
	 * @param message
	 */
	public static void warn(Component parent, String message)
	{
		JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * 输入狂
	 * @return
	 */
	public static String input()
	{
		String remark = JOptionPane.showInputDialog(null, "请输入信息", Global.Lan.get("提示消息"), JOptionPane.INFORMATION_MESSAGE);
		return remark;
	}

	public static boolean confirm(Component parent, String message)
	{
		Object[] options = { "确定", "取消" };
		int response = JOptionPane.showOptionDialog(parent, message, Global.Lan.get("提示消息"), JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
		return response == 0;
	}

	public static void drawBackground(BufferedImage img, Graphics g, Component component, int cornerWidth)
	{
		// 左上角
		g.drawImage(img.getSubimage(0, 0, cornerWidth, cornerWidth), 0, 0, cornerWidth, cornerWidth, null);
		// 上方
		g.drawImage(img.getSubimage(cornerWidth, 0, cornerWidth, cornerWidth), cornerWidth, 0, component.getWidth() - cornerWidth * 2, cornerWidth,
				null);
		// 右上角
		g.drawImage(img.getSubimage(img.getWidth() - cornerWidth, 0, cornerWidth, cornerWidth), component.getWidth() - cornerWidth, 0, cornerWidth,
				cornerWidth, null);
		// 右边
		g.drawImage(img.getSubimage(img.getWidth() - cornerWidth, cornerWidth, cornerWidth, cornerWidth), component.getWidth() - cornerWidth,
				cornerWidth, cornerWidth, component.getHeight() - cornerWidth * 2, null);
		// 右下角
		g.drawImage(img.getSubimage(img.getWidth() - cornerWidth, img.getHeight() - cornerWidth, cornerWidth, cornerWidth), component.getWidth()
				- cornerWidth, component.getHeight() - cornerWidth, cornerWidth, cornerWidth, null);
		// 下方
		g.drawImage(img.getSubimage(cornerWidth, img.getHeight() - cornerWidth, cornerWidth, cornerWidth), cornerWidth, component.getHeight()
				- cornerWidth, component.getWidth() - cornerWidth * 2, cornerWidth, null);
		// 左下角
		g.drawImage(img.getSubimage(0, img.getHeight() - cornerWidth, cornerWidth, cornerWidth), 0, component.getHeight() - cornerWidth, cornerWidth,
				cornerWidth, null);
		// 左边
		g.drawImage(img.getSubimage(0, cornerWidth, cornerWidth, cornerWidth), 0, cornerWidth, cornerWidth, component.getHeight() - cornerWidth * 2,
				null);
		// 中间
		g.drawImage(img.getSubimage(cornerWidth, cornerWidth, 10, 10), cornerWidth, cornerWidth, component.getWidth() - cornerWidth * 2,
				component.getHeight() - cornerWidth * 2, null);
	}

	public static void drawBackground(BufferedImage img, Graphics g, Component component, int cornerWidth, int blank)
	{
		// 左上角
		g.drawImage(img.getSubimage(0, 0, cornerWidth, cornerWidth), 0, 0, cornerWidth, cornerWidth, null);
		// 上方
		g.drawImage(img.getSubimage(cornerWidth, 0, cornerWidth, cornerWidth), cornerWidth, 0, component.getWidth() - cornerWidth * 2, cornerWidth,
				null);
		// 右上角
		g.drawImage(img.getSubimage(img.getWidth() - cornerWidth, 0, cornerWidth, cornerWidth), component.getWidth() - cornerWidth, 0, cornerWidth,
				cornerWidth, null);
		// 右边
		g.drawImage(img.getSubimage(img.getWidth() - cornerWidth, cornerWidth, cornerWidth, cornerWidth), component.getWidth() - cornerWidth,
				cornerWidth, cornerWidth, component.getHeight() - cornerWidth * 2, null);
		// 右下角
		g.drawImage(img.getSubimage(img.getWidth() - cornerWidth, img.getHeight() - cornerWidth, cornerWidth, cornerWidth), component.getWidth()
				- cornerWidth, component.getHeight() - cornerWidth, cornerWidth, cornerWidth, null);
		// 下方
		g.drawImage(img.getSubimage(cornerWidth, img.getHeight() - cornerWidth, cornerWidth, cornerWidth), cornerWidth, component.getHeight()
				- cornerWidth, component.getWidth() - cornerWidth * 2, cornerWidth, null);
		// 左下角
		g.drawImage(img.getSubimage(0, img.getHeight() - cornerWidth, cornerWidth, cornerWidth), 0, component.getHeight() - cornerWidth, cornerWidth,
				cornerWidth, null);
		// 左边
		g.drawImage(img.getSubimage(0, cornerWidth, cornerWidth, cornerWidth), 0, cornerWidth, cornerWidth, component.getHeight() - cornerWidth * 2,
				null);
		// 中间
		g.drawImage(img.getSubimage(cornerWidth, cornerWidth, blank, blank), cornerWidth, cornerWidth, component.getWidth() - cornerWidth * 2,
				component.getHeight() - cornerWidth * 2, null);
	}

	public static void drawBubble(BufferedImage img, Graphics g, Component component)
	{
		// header
		g.drawImage(img.getSubimage(0, 0, 31, 31), 0, 0, 14, 14, null);
		g.drawImage(img.getSubimage(31, 0, 10, 31), 14, 0, component.getWidth() - 14 - 14, 14, null);
		g.drawImage(img.getSubimage(img.getWidth() - 31, 0, 31, 31), component.getWidth() - 14, 0, 14, 14, null);
		// body
		g.drawImage(img.getSubimage(0, 31, 31, img.getHeight() - 31 - 25), 0, 14, 14, component.getHeight() - 14 - 14, null);
		g.drawImage(img.getSubimage(31, 31, 5, img.getHeight() - 31 - 25), 14, 14, component.getWidth() - 14 - 14, component.getHeight() - 14 - 14,
				null);
		g.drawImage(img.getSubimage(img.getWidth() - 25, 31, 25, img.getHeight() - 31 - 25), component.getWidth() - 14, 14, 14,
				component.getHeight() - 14 - 14, null);
		// footer
		g.drawImage(img.getSubimage(0, img.getHeight() - 25, 33, 25), 0, component.getHeight() - 14, 14, 14, null);
		g.drawImage(img.getSubimage(40, img.getHeight() - 25, 5, 25), 14, component.getHeight() - 14, component.getWidth() - 14 - 14, 14, null);
		g.drawImage(img.getSubimage(img.getWidth() - 28, img.getHeight() - 28, 28, 28), component.getWidth() - 14, component.getHeight() - 14, 14,
				14, null);
	}
}
