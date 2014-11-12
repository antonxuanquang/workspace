package com.sean.im.client.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JWindow;

import com.sean.im.client.constant.Global;

/**
 * 截图工具
 * @author sean
 */
public class CatchScreen extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private Point point = new Point();
	private Rectangle rect = new Rectangle();
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private BufferedImage img;
	private boolean paintd = false, draged = false;
	private JWindow nvg;
	private JButton save, send, cancel;

	private ScreenCatchListener screenCatchListener;

	public CatchScreen(ScreenCatchListener listener)
	{
		this.screenCatchListener = listener;
		img = shotscreen(rect.x, rect.y, screenSize.width, screenSize.height);

		this.setTitle("screen shot");
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setIconImage(Global.ICON.getImage());
		this.getContentPane().setBackground(Color.BLACK);
		this.setBackground(Color.BLACK);
		this.setOpacity(0.5f);

		// 导航烂
		nvg = new JWindow();
		nvg.setSize(105, 35);
		nvg.setBackground(Global.LightBlue);
		nvg.setLayout(null);
		save = new JButton(new ImageIcon(Global.Root + "resource/image/save.png"));
		send = new JButton(new ImageIcon(Global.Root + "resource/image/send.png"));
		cancel = new JButton(new ImageIcon(Global.Root + "resource/image/cancel.png"));
		send.setBounds(2, 2, 32, 32);
		save.setBounds(36, 2, 32, 32);
		cancel.setBounds(70, 2, 32, 32);
		nvg.add(save);
		nvg.add(send);
		nvg.add(cancel);
		save.addActionListener(this);
		send.addActionListener(this);
		cancel.addActionListener(this);

		this.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				CatchScreen.this.setCursor(Global.CURSOR_HAND);
				Point p = e.getLocationOnScreen();
				Point canvas = CatchScreen.this.getLocationOnScreen();

				rect.x = (p.x > point.x ? point.x : p.x) - canvas.x;
				rect.y = (p.y > point.y ? point.y : p.y) - canvas.y;
				rect.width = Math.abs(point.x - p.x);
				rect.height = Math.abs(point.y - p.y);

				draged = true;
				CatchScreen.this.repaint();
			}
		});

		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				point.setLocation(e.getLocationOnScreen());
				nvg.setVisible(false);
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					notifyShot();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				CatchScreen.this.setCursor(Global.CURSOR_DEF);
				if (draged)
				{
					paintd = true;
				}
				CatchScreen.this.repaint();
			}
		});

		this.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == 27)
				{
					destory();
				}
			}
		});
	}

	private void notifyShot()
	{
		if (rect.width > 0 && rect.height > 0)
		{
			try
			{
				Point p = this.getLocationOnScreen();
				BufferedImage tmp = img.getSubimage(rect.x + p.x, rect.y + p.y, rect.width, rect.height);

				// 写到tmp目录下
				IMIoUtil.checkTmpDir();
				File file = new File(Global.Root + "tmp/" + UUID.randomUUID().toString() + ".jpg");
				ImageIO.write(tmp, "jpg", file);
				tmp = null;

				destory();

				// 回调函数
				if (screenCatchListener != null)
				{
					screenCatchListener.catched(file);
				}
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}

	public void destory()
	{
		this.setVisible(false);
		this.dispose();
		nvg.setVisible(false);
		nvg.dispose();
		img = null;

		System.gc();
	}

	@Override
	public void paint(Graphics g)
	{
		g.clearRect(0, 0, screenSize.width, screenSize.height);
		g.setColor(Color.BLUE);
		g.drawRect(rect.x, rect.y, rect.width, rect.height);

		if (paintd)
		{
			Point p = this.getLocationOnScreen();
			g.drawImage(img.getSubimage(rect.x + p.x, rect.y + p.y, rect.width, rect.height), rect.x, rect.y, rect.width, rect.height, this);
			paintd = false;
			nvg.setLocation(rect.x + rect.width - 110, rect.y + rect.height + 3);
			nvg.setVisible(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			// 取消
			if (e.getSource() == cancel)
			{
				destory();
			}
			// 另存为
			else if (e.getSource() == save)
			{
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File f = chooser.getSelectedFile();
					if (f.exists())
					{
						f.delete();
					}
					ImageIO.write(img.getSubimage(rect.x, rect.y, rect.width, rect.height), "jpg", f);
					destory();
				}
			}
			// 发送
			else if (e.getSource() == send)
			{
				notifyShot();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	protected BufferedImage shotscreen(int x, int y, int width, int height)
	{
		try
		{
			Rectangle rect = new Rectangle(x, y, width, height);
			GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice device = environment.getDefaultScreenDevice();
			Robot robot = new Robot(device);
			BufferedImage tmp = robot.createScreenCapture(rect);

			environment = null;
			device = null;
			robot = null;
			return tmp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public interface ScreenCatchListener
	{
		public void catched(File img);
	}
}
