package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.listitem.SearchResultFriendPanel;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.UserInfo;

/**
 * 搜索好友结果
 * @author sean
 */
public class SearchResultForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel center, south;
	private CommonLabel lbl_pageNo;
	private CommonButton btn_previous, btn_next, btn_close;
	private int pageNo = 1;
	private Request request;
	private boolean isLast = false;

	public SearchResultForm(Request request)
	{
		super(600, 460);
		this.request = request;
		this.setCustomTitle(Global.Lan.get("搜索结果"), null);
		this.setLocationRelativeTo(null);
		this.setCustomMaxiable(false);
		this.setCustomLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		center = new JPanel();
		center.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		center.setBackground(Color.WHITE);
		this.addCustomComponent(center);

		south = new JPanel();
		south.setOpaque(false);
		south.setPreferredSize(new Dimension(1000, 35));
		south.setLayout(null);
		this.addCustomComponent(south, BorderLayout.SOUTH);

		// 添加底部控件
		lbl_pageNo = new CommonLabel(Global.Lan.get("当前页码") + "1");
		lbl_pageNo.setForeground(Color.WHITE);
		lbl_pageNo.setBounds(5, 5, 100, 25);
		south.add(lbl_pageNo);
		btn_previous = new CommonButton(Global.Lan.get("上一页"));
		btn_previous.setBounds(250, 5, 65, 25);
		south.add(btn_previous);
		btn_next = new CommonButton(Global.Lan.get("下一页"));
		btn_next.setBounds(330, 5, 65, 25);
		south.add(btn_next);
		btn_close = new CommonButton(Global.Lan.get("关闭"));
		btn_close.setBounds(410, 5, 65, 25);
		south.add(btn_close);

		// 添加事件
		Btn_Listener bl = new Btn_Listener();
		btn_close.addActionListener(bl);
		btn_previous.addActionListener(bl);
		btn_next.addActionListener(bl);

		this.getData();
	}

	private void getData()
	{
		center.removeAll();
		request.removeParameter("pageNo");
		request.setParameter("pageNo", pageNo);

		HttpUtil.request(request, new RequestHandler()
		{
			@Override
			public void callback(JSONObject data)
			{
				List<UserInfo> users = JSON.parseArray(data.getString("searchUsers"), UserInfo.class);
				for (int i = 0; i < users.size(); i++)
				{
					center.add(new SearchResultFriendPanel(i, users.get(i)));
				}
				lbl_pageNo.setText(Global.Lan.get("当前页码") + pageNo);
				if (users.size() < 10)
				{
					isLast = true;
				}
				else
				{
					isLast = false;
				}
				center.setVisible(false);
				center.setVisible(true);
			}
		});
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_close)
			{
				SearchResultForm.this.setVisible(false);
				SearchResultForm.this.dispose();
			}
			else if (e.getSource() == btn_previous)
			{
				if (pageNo > 1)
				{
					pageNo--;
					getData();
				}
			}
			else if (e.getSource() == btn_next)
			{
				if (!isLast)
				{
					pageNo++;
					getData();	
				}
			}
		}
	}
}
