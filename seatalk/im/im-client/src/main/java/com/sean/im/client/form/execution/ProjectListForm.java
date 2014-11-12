package com.sean.im.client.form.execution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import com.sean.im.client.form.MainForm;
import com.sean.im.client.listitem.execution.ProjectListItem;

/**
 * 项目列表
 * @author sean
 */
public class ProjectListForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;

	private CommonLabel lbl_username;
	private JPanel south, north;
	private MainTabComp tab;
	private CommonButton btn_close;
	private HeadComp head;

	private ProjectListPanel plp;
	private CreateProjectPanel cpp;

	public ProjectListForm()
	{
		super(450, (int) MainForm.FORM.getSize().getHeight());
		this.setCustomTitle("项目管理", null);
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
		plp = new ProjectListPanel();
		tab.addTabPanel(plp, "项目列表");
		cpp = new CreateProjectPanel();
		tab.addTabPanel(cpp, "创立项目");
		tab.setSelectedTab(plp);
		this.addCustomComponent(tab, BorderLayout.CENTER);

		btn_close = new CommonButton(Global.Lan.get("关闭"));
		btn_close.setPreferredSize(new Dimension(60, 25));
		south.add(btn_close);
		btn_close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ProjectListForm.this.setVisible(false);
			}
		});
	}

	/**
	 * 项目列表
	 * @author sean
	 */
	private class ProjectListPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonLabel lb_prjname, lb_creater, lb_createdate;
		private CustomList list;
		private JPanel north;

		private ProjectListPanel()
		{
			this.setBackground(Color.WHITE);
			this.setLayout(new BorderLayout());

			this.north = new JPanel();
			this.north.setLayout(null);
			this.north.setBackground(Global.LightBlue);
			this.north.setPreferredSize(new Dimension(1000, 30));
			this.add(north, BorderLayout.NORTH);

			lb_prjname = new CommonLabel("项目名称");
			lb_prjname.setBounds(20, 5, 100, 20);
			north.add(lb_prjname);

			lb_creater = new CommonLabel("立项人");
			lb_creater.setBounds(140, 5, 60, 20);
			north.add(lb_creater);

			lb_createdate = new CommonLabel("立项时间");
			lb_createdate.setBounds(250, 5, 60, 20);
			north.add(lb_createdate);

			list = new CustomList();
			this.add(list, BorderLayout.CENTER);

			// 添加数据
			for (int i = 0; i < 10; i++)
			{
				list.addElement(new ProjectListItem());
			}
			
			list.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() == 2)
					{
						new ProjectInfoForm().setVisible(true);
					}
				}	
			});
		}
	}

	/**
	 * 创建项目
	 * @author sean
	 */
	private class CreateProjectPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonButton btn_save;

		private CreateProjectPanel()
		{
			this.setLayout(null);
			this.setBackground(Color.WHITE);

			btn_save = new CommonButton(Global.Lan.get("保存"));
			btn_save.setBounds(330, 400, 60, 25);
			this.add(btn_save);
		}
	}
}
