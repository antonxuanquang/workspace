package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomField;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.util.ComboBoxItem;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.Request;

/**
 * 搜索好友
 * @author sean
 */
public class SearchForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel center, south;
	private CommonLabel lbl_username, lbl_nickname, lbl_country, lbl_sex, lbl_age;
	private CommonButton btn_search, btn_cancel;
	private CustomField jf_username, jf_nickname;
	private JComboBox<ComboBoxItem> jcb_country, jcb_sex, jcb_age;

	public SearchForm()
	{
		super(400, 320);
		this.setCustomTitle(Global.Lan.get("搜索好友"), null);
		this.setLocationRelativeTo(null);
		this.setCustomMaxiable(false);
		this.setCustomLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		center = new JPanel();
		center.setLayout(null);
		center.setBackground(Color.WHITE);
		this.addCustomComponent(center, BorderLayout.CENTER);

		south = new JPanel();
		south.setOpaque(false);
		south.setPreferredSize(new Dimension(1000, 35));
		south.setLayout(null);
		this.addCustomComponent(south, BorderLayout.SOUTH);

		// 添加中部控件
		lbl_username = new CommonLabel(Global.Lan.get("帐号"));
		lbl_username.setBounds(10, 10, 100, 25);
		center.add(lbl_username);
		jf_username = new CustomField();
		jf_username.setBounds(10, 35, 200, 25);
		center.add(jf_username);
		lbl_nickname = new CommonLabel(Global.Lan.get("昵称"));
		lbl_nickname.setBounds(10, 65, 100, 25);
		center.add(lbl_nickname);
		jf_nickname = new CustomField();
		jf_nickname.setBounds(10, 90, 200, 25);
		center.add(jf_nickname);

		// 添加国家
		lbl_country = new CommonLabel(Global.Lan.get("国家"));
		lbl_country.setBounds(10, 125, 80, 25);
		jcb_country = UIUtil.getCountryComboBoxForSearch();
		jcb_country.setBounds(60, 125, 80, 25);
		center.add(lbl_country);
		center.add(jcb_country);

		// 添加性别
		lbl_sex = new CommonLabel(Global.Lan.get("性别"));
		lbl_sex.setBounds(150, 125, 80, 25);
		jcb_sex = UIUtil.getSexComboBoxForSearch();
		jcb_sex.setBounds(190, 125, 70, 25);
		center.add(lbl_sex);
		center.add(jcb_sex);

		// 添加年龄
		lbl_age = new CommonLabel(Global.Lan.get("年龄"));
		lbl_age.setBounds(270, 125, 80, 25);
		jcb_age = UIUtil.getAgeForSearchComboBox();
		jcb_age.setBounds(310, 125, 70, 25);
		center.add(lbl_age);
		center.add(jcb_age);

		// 添加底部控件
		btn_search = new CommonButton(Global.Lan.get("搜索"));
		btn_search.setBounds(240, 5, 65, 25);
		south.add(btn_search);
		btn_cancel = new CommonButton(Global.Lan.get("取消"));
		btn_cancel.setBounds(310, 5, 65, 25);
		south.add(btn_cancel);

		// 添加事件
		Btn_Listener bl = new Btn_Listener();
		btn_cancel.addActionListener(bl);
		btn_search.addActionListener(bl);
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_cancel)
			{
				SearchForm.this.setVisible(false);
				SearchForm.this.dispose();
			}
			else if (e.getSource() == btn_search)
			{
				final Request request = new Request(Actions.SearchUserAction);
				request.setParameter("pageNo", 1);
				String username = jf_username.getText();
				if (username != null && !username.isEmpty())
				{
					request.setParameter("username", username);
				}
				else
				{
					String nickname = jf_nickname.getText();
					if (nickname != null && !nickname.isEmpty())
					{
						request.setParameter("nickname", nickname);
					}
					ComboBoxItem country = (ComboBoxItem) jcb_country.getSelectedItem();
					request.setParameter("country", country.getId());
					ComboBoxItem sex = (ComboBoxItem) jcb_sex.getSelectedItem();
					request.setParameter("sex", sex.getId());
					ComboBoxItem age = (ComboBoxItem) jcb_age.getSelectedItem();
					request.setParameter("age", age.getId());
				}

				SearchForm.this.setVisible(false);
				SearchForm.this.dispose();
				new SearchResultForm(request).setVisible(true);
			}
		}
	}
}
