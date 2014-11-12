package com.sean.im.client.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sean.im.client.constant.Config;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.custom.LightButton;
import com.sean.im.client.util.ChatFormCache;

/**
 * 皮肤
 * @author sean
 */
public class SkinForm extends CustomFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private LightButton skin1, skin2, skin3, skin4, skin5;
	private JSlider js;
	private JLabel custom;

	public SkinForm()
	{
		super(450, 170);
		this.setCustomTitle(Global.Lan.get("换皮肤"), null);
		this.setLocationRelativeTo(null);
		this.setCustomMaxiable(false);
		this.setCustomLayout(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		skin1 = new LightButton(new ImageIcon(Global.Root + "resource/skin/ubuntu/ubuntu_icon.jpg"));
		skin1.setBounds(10, 10, 60, 60);
		skin1.setName("ubuntu/ubuntu.jpg");
		skin1.setCursor(Global.CURSOR_HAND);
		this.addCustomComponent(skin1);
		skin1.addActionListener(this);

		skin2 = new LightButton(new ImageIcon(Global.Root + "resource/skin/skin2/skin2_icon.jpg"));
		skin2.setBounds(80, 10, 60, 60);
		skin2.setCursor(Global.CURSOR_HAND);
		skin2.setName("skin2/skin2.jpg");
		this.addCustomComponent(skin2);
		skin2.addActionListener(this);

		skin3 = new LightButton(new ImageIcon(Global.Root + "resource/skin/skin3/skin3_icon.jpg"));
		skin3.setBounds(150, 10, 60, 60);
		skin3.setCursor(Global.CURSOR_HAND);
		skin3.setName("skin3/skin3.jpg");
		this.addCustomComponent(skin3);
		skin3.addActionListener(this);

		skin4 = new LightButton(new ImageIcon(Global.Root + "resource/skin/skin4/skin4_icon.jpg"));
		skin4.setBounds(220, 10, 60, 60);
		skin4.setCursor(Global.CURSOR_HAND);
		skin4.setName("skin4/skin4.jpg");
		this.addCustomComponent(skin4);
		skin4.addActionListener(this);

		skin5 = new LightButton(new ImageIcon(Global.Root + "resource/skin/skin1/skin1_icon.jpg"));
		skin5.setBounds(290, 10, 60, 60);
		skin5.setCursor(Global.CURSOR_HAND);
		skin5.setName("skin1/skin1.jpg");
		this.addCustomComponent(skin5);
		skin5.addActionListener(this);

		custom = new JLabel(new ImageIcon(Global.Root + "resource/image/add_skin.png"));
		custom.setBounds(360, 10, 60, 60);
		custom.setCursor(Global.CURSOR_HAND);
		this.addCustomComponent(custom);
		custom.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(MainForm.FORM);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File file = chooser.getSelectedFile();
					ImageIcon img = new ImageIcon(file.getAbsolutePath());
					MainForm.FORM.setBackgroundImage(img);

					for (ChatForm form : ChatFormCache.getChatForms())
					{
						form.setBackgroundImage(img);
					}

					Config.UserSetting.skin = file.getAbsolutePath();
				}
			}
		});

		js = new JSlider(0, 100);
		js.setOpaque(false);
		js.setValue(100 - (int) (100 * Config.UserSetting.opacity));
		js.setBounds(50, 80, 325, 50);
		this.addCustomComponent(js);
		js.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				Config.UserSetting.opacity = (float) (100 - js.getValue()) / 100f;
				MainForm.FORM.setOpacity(Config.UserSetting.opacity);

				for (ChatForm form : ChatFormCache.getChatForms())
				{
					form.setOpacity(Config.UserSetting.opacity);
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		LightButton btn = (LightButton) e.getSource();
		ImageIcon img = new ImageIcon(Global.Root + "resource/skin/" + btn.getName());
		MainForm.FORM.setBackgroundImage(img);

		SkinForm.this.repaint();
		for (ChatForm form : ChatFormCache.getChatForms())
		{
			form.repaint();
		}

		Config.UserSetting.skin = Global.Root + "resource/skin/" + btn.getName();
	}
}
