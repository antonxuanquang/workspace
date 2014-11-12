package com.sean.im.client.listitem.execution;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;

/**
 * 项目文档列表项
 * @author sean
 */
public class ProjectDocListItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel filename, size, type, creater, createTime;

	public ProjectDocListItem()
	{
		this.setLayout(null);
		this.setOpaque(false);
		this.setBackground(Global.DarkYellow);

		filename = new CommonLabel("需求文档.doc");
		filename.setBounds(10, 3, 100, 25);
		this.add(filename);

		size = new CommonLabel("2M");
		size.setBounds(100, 3, 100, 25);
		this.add(size);

		type = new CommonLabel("Word");
		type.setBounds(190, 3, 100, 25);
		this.add(type);

		creater = new CommonLabel("Sean");
		creater.setBounds(280, 3, 100, 25);
		this.add(creater);

		createTime = new CommonLabel("2013-1-1");
		createTime.setBounds(370, 3, 100, 25);
		this.add(createTime);

		this.setPreferredSize(new Dimension(1000, 30));
	}
}
