package com.sean.im.client.listitem.execution;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;

/**
 * 项目财务列表项
 * @author sean
 */
public class ProjectFinanceListItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel financeBrief, money, type, createTime, recorder;

	public ProjectFinanceListItem()
	{
		this.setLayout(null);
		this.setOpaque(false);
		this.setBackground(Global.DarkYellow);

		financeBrief = new CommonLabel("办公用品");
		financeBrief.setBounds(10, 3, 100, 25);
		this.add(financeBrief);

		money = new CommonLabel("1000");
		money.setBounds(100, 3, 100, 25);
		this.add(money);

		type = new CommonLabel("支出");
		type.setBounds(190, 3, 100, 25);
		this.add(type);

		createTime = new CommonLabel("2013-1-1");
		createTime.setBounds(280, 3, 100, 25);
		this.add(createTime);

		recorder = new CommonLabel("张三");
		recorder.setBounds(370, 3, 100, 25);
		this.add(recorder);

		this.setPreferredSize(new Dimension(1000, 30));
	}
}
