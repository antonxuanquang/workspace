package com.sean.im.client.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.LightButton;
import com.sean.im.client.custom.RoundPopMenu;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;

/**
 * 国家选择
 * @author sean
 */
public class CountryComp extends LightButton implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private CountryPanel panel;
	private String selectedLan = "";

	public CountryComp()
	{
		super(new ImageIcon(Global.Root + "resource/image/chatform/dic.png"));
		this.setToolTipText(Global.Lan.get("翻译"));
		this.panel = new CountryPanel();
		this.addActionListener(this);
	}

	/**
	 * 选择选择的语言
	 * @return
	 */
	public String getSelectedLan()
	{
		return selectedLan;
	}

	private class CountryPanel extends RoundPopMenu implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		private CommonButton btn_next, btn_previous, btn_none;
		private int pageNo = 0;
		private List<JPanel> countryPanels;
		private JPanel country, bg2;

		public CountryPanel()
		{
			super(true);
			countryPanels = new ArrayList<JPanel>(3);
			File[] imgs = new File(Global.Root + "resource/image/country/").listFiles();

			JPanel bg = new JPanel();
			bg.setLayout(new FlowLayout(FlowLayout.LEADING, 3, 3));
			bg.setBackground(Color.WHITE);

			for (int i = 0; i < imgs.length; i++)
			{
				LightButton lbl = new LightButton(new ImageIcon(imgs[i].getAbsolutePath()));
				lbl.setPreferredSize(new Dimension(40, 40));
				lbl.setCursor(Global.CURSOR_HAND);
				String[] tmp = imgs[i].getName().split("\\.");
				lbl.setName(tmp[0]);
				bg.add(lbl);
				lbl.addActionListener(this);

				if ((i + 1) % 18 == 0)
				{
					countryPanels.add(bg);
					bg = new JPanel();
					bg.setLayout(new FlowLayout(FlowLayout.LEADING, 3, 3));
					bg.setBackground(Color.WHITE);
				}
			}
			countryPanels.add(bg);

			JPanel nvg = new JPanel();
			nvg.setBackground(Global.LightBlue);
			nvg.setLayout(new FlowLayout(FlowLayout.LEFT));
			btn_next = new CommonButton(Global.Lan.get("下一页"));
			btn_next.setPreferredSize(new Dimension(70, 25));
			btn_previous = new CommonButton(Global.Lan.get("上一页"));
			btn_previous.setPreferredSize(new Dimension(70, 25));
			btn_none = new CommonButton(Global.Lan.get("不翻译"));
			btn_none.setPreferredSize(new Dimension(70, 25));
			nvg.add(btn_previous);
			nvg.add(btn_next);
			nvg.add(btn_none);

			country = new JPanel();
			country.setPreferredSize(new Dimension(265, 140));
			country.setLayout(new BorderLayout());
			country.add(countryPanels.get(0));

			bg2 = new JPanel();
			bg2.setLayout(new BorderLayout());
			bg2.add(country, BorderLayout.CENTER);
			bg2.add(nvg, BorderLayout.SOUTH);

			this.add(bg2);

			// 添加事件
			BtnListener btnl = new BtnListener();
			btn_previous.addActionListener(btnl);
			btn_next.addActionListener(btnl);
			btn_none.addActionListener(btnl);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			selectedLan = ((LightButton) e.getSource()).getName();
			this.setVisible(false);
			Request request = new Request(Actions.ChangeTranslatorAction);
			request.setParameter("translator", selectedLan);
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					ApplicationContext.User.setTranslator(selectedLan);
				}
			});
		}

		/**
		 * 显示国家
		 * @param pageNo
		 */
		private void showCountry(int pageNo)
		{
			country.removeAll();
			country.add(this.countryPanels.get(pageNo));
			country.setVisible(false);
			country.setVisible(true);
		}

		private class BtnListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == btn_previous)
				{
					if (pageNo != 0)
					{
						pageNo--;
						showCountry(pageNo);
					}
				}
				else if (e.getSource() == btn_next)
				{
					if (pageNo != 2)
					{
						pageNo++;
						showCountry(pageNo);
					}
				}
				else if (e.getSource() == btn_none)
				{
					selectedLan = "";
					Request request = new Request(Actions.ChangeTranslatorAction);
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							ApplicationContext.User.setTranslator(null);
						}
					});
					panel.setVisible(false);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		panel.show(this, 0, -195);
	}
}
