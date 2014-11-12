package com.sean.im.client.form.flock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.comp.MainTabComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomField;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.custom.CustomList;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.listitem.FlockInfoMemberItem;
import com.sean.im.client.util.TimeUtil;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.FlockCard;
import com.sean.im.commom.entity.FlockMember;
import com.sean.im.commom.entity.UserInfo;

/**
 * 群信息
 * @author sean
 */
public class FlockInfoForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel north, south;
	private CommonLabel name, id, signature;
	private MainTabComp tab;
	private Flock flock;
	private CommonButton btn_close;
	private FlockInfoPanel fip;
	private FlockMemberPanel fmp;
	private FlockCardPanel fcp;

	public FlockInfoForm(Flock flock)
	{
		super(450, (int) MainForm.FORM.getSize().getHeight());
		this.flock = flock;
		this.setCustomTitle("", null);
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

		HeadComp head = new HeadComp(Global.Root + "resource/image/flock.png", 70, 70);
		head.setHeightLight();
		head.setBounds(10, 5, 70, 70);
		north.add(head);

		name = new CommonLabel();
		name.setFont(new Font("", Font.BOLD, 20));
		name.setForeground(Color.WHITE);
		name.setBounds(100, 5, 400, 25);
		north.add(name);

		id = new CommonLabel(Global.Lan.get("群号") + ":" + flock.getId());
		id.setForeground(Color.WHITE);
		id.setBounds(100, 30, 100, 25);
		north.add(id);

		signature = new CommonLabel();
		signature.setForeground(Color.WHITE);
		signature.setBounds(100, 50, 200, 25);
		north.add(signature);

		tab = new MainTabComp();
		fip = new FlockInfoPanel();
		tab.addTabPanel(fip, Global.Lan.get("群资料"));
		fmp = new FlockMemberPanel();
		tab.addTabPanel(fmp, Global.Lan.get("群成员"));
		fcp = new FlockCardPanel();
		tab.addTabPanel(fcp, Global.Lan.get("群名片"));
		tab.setSelectedTab(fip);

		this.addCustomComponent(tab, BorderLayout.CENTER);

		south = new JPanel();
		south.setOpaque(false);
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));
		south.setPreferredSize(new Dimension(300, 35));
		this.addCustomComponent(south, BorderLayout.SOUTH);

		btn_close = new CommonButton(Global.Lan.get("关闭"));
		btn_close.setPreferredSize(new Dimension(60, 25));
		south.add(btn_close);
		btn_close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				FlockInfoForm.this.setVisible(false);
			}
		});
	}

	public void getData()
	{
		fip.getData();
		fmp.getData();
		fcp.getData();
	}

	public void setMemberSelected()
	{
		tab.setSelectedTab(fmp);
	}

	private class FlockInfoPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonLabel lb_name, lb_id, lb_sig, lb_descr, lb_createTime, lb_creater;
		private CustomField cf_name, cf_sig, cf_descr;
		private CommonButton btn_save;

		public FlockInfoPanel()
		{
			this.setBackground(Color.WHITE);
			this.setLayout(null);

			lb_name = new CommonLabel(Global.Lan.get("群名称") + ":");
			lb_name.setBounds(10, 10, 60, 25);
			this.add(lb_name);

			cf_name = new CustomField();
			cf_name.setBounds(70, 10, 150, 25);
			this.add(cf_name);

			lb_id = new CommonLabel(Global.Lan.get("群号") + ":");
			lb_id.setBounds(250, 10, 70, 25);
			this.add(lb_id);

			lb_creater = new CommonLabel(Global.Lan.get("创建人") + ":");
			lb_creater.setBounds(10, 40, 250, 25);
			this.add(lb_creater);

			lb_createTime = new CommonLabel(Global.Lan.get("创建时间") + ":");
			lb_createTime.setBounds(250, 40, 200, 25);
			this.add(lb_createTime);

			lb_sig = new CommonLabel(Global.Lan.get("群签名") + ":");
			lb_sig.setBounds(10, 70, 100, 25);
			this.add(lb_sig);

			cf_sig = new CustomField(CustomField.JTEXTAREA);
			cf_sig.setBounds(10, 90, 380, 60);
			this.add(cf_sig);

			lb_descr = new CommonLabel(Global.Lan.get("群签简介") + ":");
			lb_descr.setBounds(10, 170, 100, 25);
			this.add(lb_descr);

			cf_descr = new CustomField(CustomField.JTEXTAREA);
			cf_descr.setBounds(10, 190, 380, 100);
			this.add(cf_descr);

			btn_save = new CommonButton(Global.Lan.get("保存"));
			btn_save.setBounds(330, 300, 65, 25);

			btn_save.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					final String flockName = cf_name.getText().trim();
					if (!flockName.isEmpty())
					{
						Request request = new Request(Actions.UpdateFlockInfoAction);
						request.setParameter("flockId", flock.getId());
						request.setParameter("flockName", flockName);
						request.setParameter("signature", cf_sig.getText());
						request.setParameter("description", cf_descr.getText());
						HttpUtil.request(request, new RequestHandler()
						{
							@Override
							public void callback(JSONObject data)
							{
								flock.setName(flockName);
								flock.setSignature(cf_sig.getText());

								name.setText(flockName);
								signature.setText(cf_sig.getText());

								MainForm.FORM.getFlockList().notifyChange(flock.getId());
								UIUtil.alert(null, Global.Lan.get("保存成功"));
							}
						});
					}
					else
					{
						UIUtil.alert(null, Global.Lan.get("群名称不能为空"));
					}
				}
			});
		}

		public void getData()
		{
			Request request = new Request(Actions.InquireFlockInfoAction);
			request.setParameter("flockId", flock.getId());
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					Flock f = data.getObject("flockFull", Flock.class);

					name.setText(f.getName());
					signature.setText(f.getSignature());

					cf_name.setText(f.getName());
					lb_id.setText(lb_id.getText() + f.getId());
					lb_createTime.setText(lb_createTime.getText() + TimeUtil.parseYYYYMMDD(f.getCreateTime()));
					cf_sig.setText(f.getSignature());
					cf_descr.setText(f.getDescription());
					UserInfo creater = data.getObject("flockCreater", UserInfo.class);
					lb_creater.setText(lb_creater.getText() + creater.getNickname() + "(" + creater.getUsername() + ")");
					if (f.getCreater() == ApplicationContext.User.getId())
					{
						FlockInfoPanel.this.add(btn_save);
					}
				}
			});
		}
	}

	public class FlockMemberPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonLabel lb_username, lb_nickname, lb_joinTime;
		private CustomList list;
		private JPanel north;
		private CommonButton btn_mgr;

		public FlockMemberPanel()
		{
			this.setBackground(Color.WHITE);
			this.setLayout(new BorderLayout());

			north = new JPanel();
			north.setLayout(null);
			north.setBackground(Global.LightBlue);
			north.setPreferredSize(new Dimension(1000, 30));
			this.add(north, BorderLayout.NORTH);

			btn_mgr = new CommonButton(Global.Lan.get("添加成员"));
			btn_mgr.setPreferredSize(new Dimension(75, 25));
			btn_mgr.setBounds(310, 5, 75, 25);

			lb_username = new CommonLabel(Global.Lan.get("帐号"));
			lb_username.setBounds(70, 5, 60, 20);
			north.add(lb_username);

			lb_nickname = new CommonLabel(Global.Lan.get("昵称"));
			lb_nickname.setBounds(170, 5, 60, 20);
			north.add(lb_nickname);

			lb_joinTime = new CommonLabel(Global.Lan.get("加入时间"));
			lb_joinTime.setBounds(250, 5, 60, 20);
			north.add(lb_joinTime);

			list = new CustomList();
			this.add(list, BorderLayout.CENTER);
			list.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					FlockInfoMemberItem fmi = (FlockInfoMemberItem) list.getSelectedValue();
					if (fmi != null)
					{
						if (e.isMetaDown())
						{
							fmi.showMenu(list, e.getX(), e.getY());
						}
					}
				}
			});

			btn_mgr.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					new AddMemberForm(flock.getId(), FlockMemberPanel.this).setVisible(true);
				}
			});
		}

		public void addMember(List<FlockMember> members)
		{
			for (FlockMember fm : members)
			{
				list.addElement(new FlockInfoMemberItem(flock, fm));
			}
		}

		public CustomList getMemberList()
		{
			return list;
		}

		public void getData()
		{
			Request request = new Request(Actions.InquireMemberListAction);
			request.setParameter("flockId", flock.getId());
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					List<FlockMember> members = JSON.parseArray(data.getString("flockMemberList"), FlockMember.class);
					for (FlockMember fm : members)
					{
						list.addElement(new FlockInfoMemberItem(flock, fm));
						if (fm.getUserId() == ApplicationContext.User.getId())
						{
							// 如果当前用户是管理员或者创建人
							if (fm.getIsAdmin() == 1 || flock.getCreater() == ApplicationContext.User.getId())
							{
								north.add(btn_mgr);
							}
						}
					}
				}
			});
		}
	}

	private class FlockCardPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonLabel lb_name, lb_tel, lb_email, lb_descr;
		private CustomField cf_name, cf_tel, cf_email, cf_descr;
		private CommonButton btn_save;

		public FlockCardPanel()
		{
			this.setBackground(Color.WHITE);
			this.setLayout(null);

			lb_name = new CommonLabel(Global.Lan.get("姓名") + ":");
			lb_name.setBounds(10, 10, 60, 25);
			this.add(lb_name);

			cf_name = new CustomField();
			cf_name.setBounds(70, 10, 270, 25);
			this.add(cf_name);

			lb_tel = new CommonLabel(Global.Lan.get("电话") + ":");
			lb_tel.setBounds(10, 40, 60, 25);
			this.add(lb_tel);

			cf_tel = new CustomField();
			cf_tel.setBounds(70, 40, 270, 25);
			this.add(cf_tel);

			lb_email = new CommonLabel(Global.Lan.get("邮箱") + ":");
			lb_email.setBounds(10, 80, 60, 25);
			this.add(lb_email);

			cf_email = new CustomField();
			cf_email.setBounds(70, 80, 270, 25);
			this.add(cf_email);

			lb_descr = new CommonLabel(Global.Lan.get("个人说明") + ":");
			lb_descr.setBounds(10, 120, 60, 25);
			this.add(lb_descr);

			cf_descr = new CustomField(CustomField.JTEXTAREA);
			cf_descr.setBounds(70, 120, 270, 100);
			this.add(cf_descr);

			btn_save = new CommonButton(Global.Lan.get("保存"));
			btn_save.setBounds(280, 300, 65, 25);
			this.add(btn_save);

			btn_save.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Request request = new Request(Actions.UpdateFlockCardAction);
					request.setParameter("flockId", flock.getId());
					request.setParameter("name", cf_name.getText());
					request.setParameter("tel", cf_tel.getText());
					request.setParameter("mail", cf_email.getText());
					request.setParameter("description", cf_descr.getText());
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							UIUtil.alert(null, Global.Lan.get("保存名片成功"));
						}
					});
				}
			});
		}

		public void getData()
		{
			Request request = new Request(Actions.InquireFlockCardAction);
			request.setParameter("flockId", flock.getId());
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					FlockCard card = data.getObject("flockCard", FlockCard.class);
					if (card != null)
					{
						cf_name.setText(card.getName());
						cf_tel.setText(card.getTel());
						cf_email.setText(card.getEmail());
						cf_descr.setText(card.getDescription());
					}
				}
			});
		}
	}

}
