package com.sean.im.client.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CustomScrollPane;

/**
 * 输入面板
 * @author sean
 */
public class InputComp extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextPane input;

	public InputComp()
	{
		this.setLayout(new BorderLayout());
		this.setOpaque(false);

		JPanel tmp = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g)
			{
				Graphics2D g2 = (Graphics2D) g;
				g.setColor(Global.LightBlue);
				GeneralPath path = new GeneralPath();
				path = new GeneralPath();
				path.moveTo(0, 13);
				path.lineTo(10, 3);
				path.lineTo(10, 13);
				path.lineTo(0, 13);
				path.closePath();
				g.setClip(path);
				g2.fill(path);
			}
		};
		tmp.setOpaque(false);
		tmp.setPreferredSize(new Dimension(10, 1000));
		this.add(tmp, BorderLayout.WEST);

		input = new JTextPane();
		input.setBackground(Global.LightBlue);

		CustomScrollPane tmp2 = new CustomScrollPane(input);
		tmp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		this.add(tmp2, BorderLayout.CENTER);
		this.setBorder(new EmptyBorder(0, 0, 0, 5));
	}

	public void requestFocus()
	{
		input.requestFocus();
	}
	
	/**
	 * 获取输入框文本信息
	 */
	public String getInputMsg()
	{
		List<ImageIcon> icons = new ArrayList<ImageIcon>(20);
		int lines = input.getStyledDocument().getRootElements()[0].getElementCount();
		for (int j = 0; j < lines; j++)
		{
			for (int i = 0; i < input.getStyledDocument().getRootElements()[0].getElement(j).getElementCount(); i++)
			{
				ImageIcon icon = (ImageIcon) StyleConstants.getIcon(input.getStyledDocument().getRootElements()[0].getElement(j).getElement(i)
						.getAttributes());
				if (icon != null)
				{
					icons.add(icon);
				}
			}
		}

		StringBuilder msg = new StringBuilder();
		int k = 0;
		for (int i = 0; i < input.getText().length(); i++)
		{
			if (input.getStyledDocument().getCharacterElement(i).getName().equals("icon"))
			{
				String[] tmp = icons.get(k).toString().replace('\\', '/').split("/");
				String path = tmp[tmp.length - 1];
				while (path.length() < 7)
				{
					path = " " + path;
				}
				msg.append("/").append(path);
				k++;
			}
			else
			{
				try
				{
					msg.append(input.getStyledDocument().getText(i, 1).replace("/", ""));
				}
				catch (BadLocationException e1)
				{
					e1.printStackTrace();
				}
			}
		}
		return msg.toString();
	}

	public JTextPane getInput()
	{
		return this.input;
	}
}
