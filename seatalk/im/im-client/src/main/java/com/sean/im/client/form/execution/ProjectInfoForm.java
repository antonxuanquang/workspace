package com.sean.im.client.form.execution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.comp.MainTabComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.custom.CustomList;
import com.sean.im.client.custom.CustomScrollPane;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.listitem.execution.ProjectDocListItem;
import com.sean.im.client.listitem.execution.ProjectFinanceListItem;
import com.sean.im.client.listitem.execution.ProjectTaskListItem;
import com.sean.im.client.listitem.execution.ProjectTeamListItem;

/**
 * 项目详细
 * @author sean
 */
public class ProjectInfoForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;

	private CommonLabel lbl_username;
	private JPanel south, north;
	private MainTabComp tab;
	private CommonButton btn_close;
	private HeadComp head;

	private ProjectInfoPanel pip;
	private ProjectTeamPanel ptp;
	private ProjectTaskPanel ptaskp;
	private ProjectFinancePanel pfp;
	private ProjectDocPanel pdp;

	public ProjectInfoForm()
	{
		super(450, (int) MainForm.FORM.getSize().getHeight());
		this.setCustomTitle("项目详细", null);
		this.setCustomMaxiable(false);
		this.setCustomLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Point point = MainForm.FORM.getLocationOnScreen();
		this.setLocation(point.x - this.getWidth(), point.y);

		north = new JPanel();
		north.setOpaque(false);
		north.setLayout(null);
		north.setPreferredSize(new Dimension(300, 80));
		this.addCustomComponent(north, BorderLayout.NORTH);

		south = new JPanel();
		south.setOpaque(false);
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));
		south.setPreferredSize(new Dimension(300, 35));
		this.addCustomComponent(south, BorderLayout.SOUTH);

		head = new HeadComp(1, 70, 70);
		head.setHeightLight();
		head.setBounds(10, 5, 70, 70);
		north.add(head);

		// 添加用户名label
		lbl_username = new CommonLabel(ApplicationContext.User.getUsername());
		lbl_username.setForeground(Color.WHITE);
		lbl_username.setBounds(90, 10, 200, 25);
		north.add(lbl_username);

		tab = new MainTabComp();
		pip = new ProjectInfoPanel();
		tab.addTabPanel(pip, "项目概况");
		ptp = new ProjectTeamPanel();
		tab.addTabPanel(ptp, "项目团队");
		ptaskp = new ProjectTaskPanel();
		tab.addTabPanel(ptaskp, "项目任务");
		pfp = new ProjectFinancePanel();
		tab.addTabPanel(pfp, "项目财务");
		pdp = new ProjectDocPanel();
		tab.addTabPanel(pdp, "项目文档");

		tab.setSelectedTab(pip);
		this.addCustomComponent(tab, BorderLayout.CENTER);

		btn_close = new CommonButton(Global.Lan.get("关闭"));
		btn_close.setPreferredSize(new Dimension(60, 25));
		south.add(btn_close);
		btn_close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ProjectInfoForm.this.setVisible(false);
			}
		});
	}

	/**
	 * 项目概况
	 * @author sean
	 */
	private class ProjectInfoPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		private ProjectInfoPanel()
		{
			this.setLayout(new BorderLayout());
			this.setBackground(Color.WHITE);

			JPanel content = new JPanel();
			content.setOpaque(false);
			content.setLayout(new FlowLayout(FlowLayout.LEFT));
			CustomScrollPane sp = new CustomScrollPane(content);
			sp.setBackground(Color.WHITE);

			CommonLabel cl = new CommonLabel("我斯大林快速解放路斯柯达就类似肯德");
			content.add(cl);

			this.add(sp);
		}
	}

	/**
	 * 项目团队
	 * @author sean
	 */
	private class ProjectTeamPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonLabel name, username, department, role, joinTime;
		private CustomList list;
		private JPanel north;

		private ProjectTeamPanel()
		{
			this.setBackground(Color.WHITE);
			this.setLayout(new BorderLayout());

			this.north = new JPanel();
			this.north.setLayout(null);
			this.north.setBackground(Global.LightBlue);
			this.north.setPreferredSize(new Dimension(1000, 30));
			this.add(north, BorderLayout.NORTH);

			name = new CommonLabel("姓名");
			name.setBounds(10, 3, 100, 25);
			north.add(name);

			username = new CommonLabel("工号");
			username.setBounds(100, 3, 100, 25);
			north.add(username);

			department = new CommonLabel("部门");
			department.setBounds(190, 3, 100, 25);
			north.add(department);

			role = new CommonLabel("角色");
			role.setBounds(280, 3, 100, 25);
			north.add(role);

			joinTime = new CommonLabel("加入时间");
			joinTime.setBounds(370, 3, 100, 25);
			north.add(joinTime);

			list = new CustomList();
			this.add(list, BorderLayout.CENTER);

			// 添加数据
			for (int i = 0; i < 10; i++)
			{
				list.addElement(new ProjectTeamListItem());
			}
		}
	}

	/**
	 * 项目任务
	 * @author sean
	 */
	private class ProjectTaskPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonLabel taskName, taskType, executer, createTime, finishTime;
		private CustomList list;
		private JPanel north;

		private ProjectTaskPanel()
		{
			this.setBackground(Color.WHITE);
			this.setLayout(new BorderLayout());

			this.north = new JPanel();
			this.north.setLayout(null);
			this.north.setBackground(Global.LightBlue);
			this.north.setPreferredSize(new Dimension(1000, 30));
			this.add(north, BorderLayout.NORTH);

			taskName = new CommonLabel("任务名称");
			taskName.setBounds(10, 3, 100, 25);
			north.add(taskName);

			taskType = new CommonLabel("任务类型");
			taskType.setBounds(100, 3, 100, 25);
			north.add(taskType);

			executer = new CommonLabel("执行人");
			executer.setBounds(190, 3, 100, 25);
			north.add(executer);

			createTime = new CommonLabel("发起时间");
			createTime.setBounds(280, 3, 100, 25);
			north.add(createTime);

			finishTime = new CommonLabel("完成时间");
			finishTime.setBounds(370, 3, 100, 25);
			north.add(finishTime);

			list = new CustomList();
			this.add(list, BorderLayout.CENTER);

			// 添加数据
			for (int i = 0; i < 10; i++)
			{
				list.addElement(new ProjectTaskListItem());
			}
		}
	}

	/**
	 * 项目财务
	 * @author sean
	 */
	private class ProjectFinancePanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonLabel financeBrief, money, type, createTime, recorder;
		private CustomList list;
		private JPanel north;

		private ProjectFinancePanel()
		{
			this.setBackground(Color.WHITE);
			this.setLayout(new BorderLayout());

			this.north = new JPanel();
			this.north.setLayout(null);
			this.north.setBackground(Global.LightBlue);
			this.north.setPreferredSize(new Dimension(1000, 30));
			this.add(north, BorderLayout.NORTH);

			financeBrief = new CommonLabel("财务说明");
			financeBrief.setBounds(10, 3, 100, 25);
			north.add(financeBrief);

			money = new CommonLabel("金额");
			money.setBounds(100, 3, 100, 25);
			north.add(money);

			type = new CommonLabel("类型");
			type.setBounds(190, 3, 100, 25);
			north.add(type);

			createTime = new CommonLabel("发生时间");
			createTime.setBounds(280, 3, 100, 25);
			north.add(createTime);

			recorder = new CommonLabel("记录人");
			recorder.setBounds(370, 3, 100, 25);
			north.add(recorder);

			list = new CustomList();
			this.add(list, BorderLayout.CENTER);

			// 添加数据
			for (int i = 0; i < 10; i++)
			{
				list.addElement(new ProjectFinanceListItem());
			}
		}
	}

	/**
	 * 项目文档
	 * @author sean
	 */
	private class ProjectDocPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonLabel filename, size, type, creater, createTime;
		private CustomList list;
		private JPanel north;

		private ProjectDocPanel()
		{
			this.setBackground(Color.WHITE);
			this.setLayout(new BorderLayout());

			this.north = new JPanel();
			this.north.setLayout(null);
			this.north.setBackground(Global.LightBlue);
			this.north.setPreferredSize(new Dimension(1000, 30));
			this.add(north, BorderLayout.NORTH);

			filename = new CommonLabel("文件名");
			filename.setBounds(10, 3, 100, 25);
			north.add(filename);

			size = new CommonLabel("文件大小");
			size.setBounds(100, 3, 100, 25);
			north.add(size);

			type = new CommonLabel("类型");
			type.setBounds(190, 3, 100, 25);
			north.add(type);

			creater = new CommonLabel("创建人");
			creater.setBounds(280, 3, 100, 25);
			north.add(creater);

			createTime = new CommonLabel("创建时间");
			createTime.setBounds(370, 3, 100, 25);
			north.add(createTime);

			list = new CustomList();
			this.add(list, BorderLayout.CENTER);

			// 添加数据
			for (int i = 0; i < 10; i++)
			{
				list.addElement(new ProjectDocListItem());
			}
		}
	}
}
