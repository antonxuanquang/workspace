package com.sean.im.client.listitem.execution;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;

/**
 * 项目任务列表项
 * @author sean
 */
public class ProjectTaskListItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel taskName, taskType, executer, createTime ,finishTime;

	public ProjectTaskListItem()
	{
		this.setLayout(null);
		this.setOpaque(false);
		this.setBackground(Global.DarkYellow);

		taskName = new CommonLabel("开发测试EM");
		taskName.setBounds(10, 3, 100, 25);
		this.add(taskName);

		taskType = new CommonLabel("即时任务");
		taskType.setBounds(100, 3, 100, 25);
		this.add(taskType);

		executer = new CommonLabel("张三");
		executer.setBounds(190, 3, 100, 25);
		this.add(executer);

		createTime = new CommonLabel("2013-1-1");
		createTime.setBounds(280, 3, 100, 25);
		this.add(createTime);
		
		finishTime = new CommonLabel("2013-1-4");
		finishTime.setBounds(370, 3, 100, 25);
		this.add(finishTime);

		this.setPreferredSize(new Dimension(1000, 30));
	}
}
