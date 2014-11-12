package com.sean.im.console.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.UserInfo;

public class ConsoleForm extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JLabel onlineCount, lbl_pageNo, totalpage;
	private JButton btn_next, btn_previous, btn_refresh, btn_exit, btn_warn;
	private JTable table;
	private DefaultTableModel tablemodel;
	private int pageNo = 1;
	private int totalrecords = 1;

	public ConsoleForm()
	{
		this.setTitle("IM Console");
		this.setSize(1000, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);

		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());
		this.add(south, BorderLayout.SOUTH);

		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		this.add(center, BorderLayout.CENTER);

		JPanel north = new JPanel();
		north.setLayout(new FlowLayout());
		this.add(north, BorderLayout.NORTH);

		onlineCount = new JLabel("当前在线用户：0");
		north.add(onlineCount);
		btn_refresh = new JButton("刷新");
		north.add(btn_refresh);
		btn_exit = new JButton("强制下线");
		north.add(btn_exit);

		btn_warn = new JButton("发送警告信息");
		north.add(btn_warn);

		final Object[] header = { "编号", "帐号", "昵称", "国家", "年龄", "联系电话", "邮箱", "状态" };
		tablemodel = new DefaultTableModel(header, 0)
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		table = new JTable();
		table.setModel(tablemodel);
		table.setDragEnabled(false);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane jsp = new JScrollPane(table);
		center.add(jsp);

		lbl_pageNo = new JLabel("第1页");
		btn_next = new JButton("下一页");
		btn_previous = new JButton("上一页");
		totalpage = new JLabel("共1页");
		south.add(lbl_pageNo);
		south.add(btn_previous);
		south.add(btn_next);
		south.add(totalpage);

		Btn_Listener bl = new Btn_Listener();
		btn_refresh.addActionListener(bl);
		btn_previous.addActionListener(bl);
		btn_next.addActionListener(bl);
		btn_exit.addActionListener(bl);
		btn_warn.addActionListener(bl);

		this.getData(1);

		int pages = totalrecords % 20 == 0 ? totalrecords / 20 : totalrecords / 20 + 1;
		totalpage.setText("共" + pages + "页");
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_refresh)
			{
				getData(pageNo);
			}
			else if (e.getSource() == btn_previous)
			{
				if (pageNo > 1)
				{
					pageNo--;
					getData(pageNo);
				}
			}
			else if (e.getSource() == btn_next)
			{
				int pages = totalrecords % 20 == 0 ? totalrecords / 20 : totalrecords / 20 + 1;
				if (pageNo < pages)
				{
					pageNo++;
					getData(pageNo);
				}
			}
			else if (e.getSource() == btn_exit)
			{
				int rows[] = table.getSelectedRows();
				if (rows.length > 0)
				{
					long[] clients = new long[rows.length];
					for (int i = 0; i < rows.length; i++)
					{
						clients[i] = Long.parseLong(tablemodel.getValueAt(rows[i], 0).toString());
					}
					Request request = new Request(Actions.ExitClientAction);
					request.setParameter("userId", clients[0]);
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							getData(pageNo);
						}
					});
				}
				else
				{
					JOptionPane.showMessageDialog(null, "请选择要强制下线的用户", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			}
			else if (e.getSource() == btn_warn)
			{
				int rows[] = table.getSelectedRows();
				if (rows.length > 0)
				{
					long[] clients = new long[rows.length];
					for (int i = 0; i < rows.length; i++)
					{
						clients[i] = Long.parseLong(tablemodel.getValueAt(rows[i], 0).toString());
					}
					new WriteForm(clients[0]).setVisible(true);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "请选择用户", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	public void getData(int page)
	{
		tablemodel.setRowCount(0);
		Request request = new Request(Actions.InquireServerInfoAction);
		request.setParameter("pageNo", pageNo);
		request.setParameter("totalrecords", totalrecords);
		HttpUtil.request(request, new RequestHandler()
		{
			@Override
			public void callback(JSONObject data)
			{
				int online = data.getIntValue("onlineCount");
				List<UserInfo> users = JSON.parseArray(data.getString("userlist"), UserInfo.class);
				onlineCount.setText("当前在线用户：" + online);
				int length = users.size();
				for (int i = 0; i < length; i++)
				{
					UserInfo user = users.get(i);
//					tablemodel.addRow(new Object[] { user.getId(), user.getUsername(), user.getNickname(), CountryDic.getCountry(user.getCountry()),
//							user.getAge(), user.getTel(), user.getMail(), StatusDic.getStatus(user.getStatus()) });
				}
				lbl_pageNo.setText("第" + pageNo + "页");

				totalrecords = data.getIntValue("totalrecords");
			}
		});
	}
}
