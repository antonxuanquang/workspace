package com.sean.im.client.form.execution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomField;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Flock;

/**
 * 添加群
 * @author sean
 */
public class SimpleForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel bg, south;
	private CommonLabel lbl_name, lbl_sig, lbl_descr;
	private CommonButton btn_create, btn_cancel;
	private CustomField jf_name, jf_sig, jf_descr;

	public SimpleForm()
	{
		super(400, 300);
		this.setCustomTitle(Global.Lan.get("添加群"), null);
		this.setLocationRelativeTo(null);
		this.setCustomMaxiable(false);
		this.setCustomLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		bg = new JPanel();
		bg.setBackground(Color.WHITE);
		bg.setLayout(null);
		this.addCustomComponent(bg, BorderLayout.CENTER);

		south = new JPanel();
		south.setOpaque(false);
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.addCustomComponent(south, BorderLayout.SOUTH);

		lbl_name = new CommonLabel(Global.Lan.get("群名称") + " : ");
		lbl_name.setBounds(20, 10, 100, 25);
		bg.add(lbl_name);
		jf_name = new CustomField();
		jf_name.setBounds(100, 10, 250, 25);
		bg.add(jf_name);

		lbl_sig = new CommonLabel(Global.Lan.get("群签名") + " : ");
		lbl_sig.setBounds(20, 40, 100, 25);
		bg.add(lbl_sig);
		jf_sig = new CustomField(CustomField.JTEXTAREA);
		jf_sig.setBounds(100, 40, 250, 75);
		bg.add(jf_sig);

		lbl_descr = new CommonLabel(Global.Lan.get("群简介") + " : ");
		lbl_descr.setBounds(20, 120, 100, 25);
		bg.add(lbl_descr);
		jf_descr = new CustomField(CustomField.JTEXTAREA);
		jf_descr.setBounds(100, 125, 250, 100);
		bg.add(jf_descr);

		btn_create = new CommonButton(Global.Lan.get("创建"));
		btn_create.setPreferredSize(new Dimension(65, 25));
		south.add(btn_create);
		btn_cancel = new CommonButton(Global.Lan.get("取消"));
		btn_cancel.setPreferredSize(new Dimension(65, 25));
		south.add(btn_cancel);

		// 添加事件
		Btn_Listener bl = new Btn_Listener();
		btn_cancel.addActionListener(bl);
		btn_create.addActionListener(bl);
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_cancel)
			{
				SimpleForm.this.setVisible(false);
			}
			else if (e.getSource() == btn_create)
			{
				final String name = jf_name.getText();
				final String sig = jf_sig.getText();
				final String descr = jf_descr.getText();

				if (name.isEmpty())
				{
					UIUtil.alert(null, Global.Lan.get("请输入完整信息"));
					return;
				}

				Request request = new Request(Actions.CreateFlockAction);
				request.setParameter("flockName", name);
				request.setParameter("flockSignature", sig);
				request.setParameter("flockDescr", descr);
				HttpUtil.request(request, new RequestHandler()
				{
					@Override
					public void callback(JSONObject data)
					{
						long flockId = data.getIntValue("flockId");
						
						Flock flock = new Flock();
						flock.setId(flockId);
						flock.setName(name);
						flock.setSignature(sig);
						flock.setCreater(ApplicationContext.User.getId());

						MainForm.FORM.getFlockList().addFlock(flock);

						UIUtil.alert(null, Global.Lan.get("创建成功"));
						SimpleForm.this.setVisible(false);
					}
				});
			}
		}
	}
}
