package com.sean.im.client.listitem.execution;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;

/**
 * 项目列表项
 * @author sean
 */
public class ProjectListItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel prjName, prjCreater, prjCreateTime;

	public ProjectListItem()
	{
		this.setLayout(null);
		this.setOpaque(false);
		this.setBackground(Global.DarkYellow);

		prjName = new CommonLabel("项目1");
		prjName.setBounds(10, 3, 100, 25);
		this.add(prjName);

		prjCreater = new CommonLabel("sean_zwx");
		prjCreater.setBounds(130, 3, 100, 25);
		this.add(prjCreater);
		
		prjCreateTime = new CommonLabel("2013-1-1");
		prjCreateTime.setBounds(250, 3, 100, 25);
		this.add(prjCreateTime);

		this.setPreferredSize(new Dimension(1000, 30));
	}
}
