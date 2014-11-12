package com.sean.im.client.listitem.execution;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;

/**
 * 项目团队列表项
 * @author sean
 */
public class ProjectTeamListItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel name, username, department, role, joinTime;

	public ProjectTeamListItem()
	{
		this.setLayout(null);
		this.setOpaque(false);
		this.setBackground(Global.DarkYellow);

		name = new CommonLabel("张三");
		name.setBounds(10, 3, 100, 25);
		this.add(name);

		username = new CommonLabel("sean_zwx");
		username.setBounds(100, 3, 100, 25);
		this.add(username);

		department = new CommonLabel("开发部");
		department.setBounds(190, 3, 100, 25);
		this.add(department);

		role = new CommonLabel("工程师");
		role.setBounds(280, 3, 100, 25);
		this.add(role);

		joinTime = new CommonLabel("2013-1-1");
		joinTime.setBounds(370, 3, 100, 25);
		this.add(joinTime);

		this.setPreferredSize(new Dimension(1000, 30));
	}
}
