package com.sean.im.client.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.LightButton;
import com.sean.im.client.custom.RoundPopMenu;

/**
 * 表情控件
 * @author sean
 */
public class EmotionComp extends LightButton implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private EmotionPanel panel;

	public EmotionComp(InputComp input)
	{
		super(new ImageIcon(Global.Root + "resource/image/chatform/emotion.png"));
		this.setToolTipText(Global.Lan.get("表情"));
		this.panel = new EmotionPanel(input);
		this.addActionListener(this);
	}

	private class EmotionPanel extends RoundPopMenu implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		private CommonButton btn_next, btn_previous;
		private int pageNo = 0;
		private List<JPanel> emotionPanels;
		private JPanel emotion, bg;
		private InputComp input;

		private EmotionPanel(InputComp input)
		{
			super(true);
			this.input = input;

			emotionPanels = new ArrayList<JPanel>(3);
			for (int i = 0; i < 3; i++)
			{
				JPanel bg = new JPanel();
				bg.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
				bg.setBackground(Color.WHITE);
				int start = i * 50;
				for (int j = start; j < start + 50; j++)
				{
					if (j > 134)
					{
						break;
					}
					LightButton lbl = new LightButton(new ImageIcon(Global.Root + "resource/image/emotion/static/" + j + ".png"));
					lbl.setName(String.valueOf(j));
					bg.add(lbl);
					lbl.addActionListener(this);
				}
				emotionPanels.add(bg);
			}

			JPanel nvg = new JPanel();
			nvg.setBackground(Global.LightBlue);
			nvg.setLayout(new FlowLayout(FlowLayout.LEFT));
			btn_next = new CommonButton(Global.Lan.get("下一页"));
			btn_next.setPreferredSize(new Dimension(70, 25));
			btn_previous = new CommonButton(Global.Lan.get("上一页"));
			btn_previous.setPreferredSize(new Dimension(70, 25));
			nvg.add(btn_previous);
			nvg.add(btn_next);

			emotion = new JPanel();
			emotion.setPreferredSize(new Dimension(280, 150));
			emotion.setLayout(new BorderLayout());
			emotion.add(this.emotionPanels.get(pageNo));

			bg = new JPanel();
			bg.setLayout(new BorderLayout());
			bg.add(emotion, BorderLayout.CENTER);
			bg.add(nvg, BorderLayout.SOUTH);

			this.add(bg);

			// 添加事件
			BtnListener btnl = new BtnListener();
			btn_previous.addActionListener(btnl);
			btn_next.addActionListener(btnl);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.setVisible(false);
			String emotionPath = Global.Root + "resource/image/emotion/" + ((LightButton) e.getSource()).getName() + ".gif";
			input.getInput().insertIcon(new ImageIcon((emotionPath)));
			input.getInput().requestFocus();
		}

		/**
		 * 显示表情
		 * @param pageNo
		 */
		private void showEmotion(int pageNo)
		{
			emotion.removeAll();
			emotion.add(this.emotionPanels.get(pageNo));
			emotion.setVisible(false);
			emotion.setVisible(true);
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
						showEmotion(pageNo);
					}
				}
				else if (e.getSource() == btn_next)
				{
					if (pageNo != 2)
					{
						pageNo++;
						showEmotion(pageNo);
					}
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
