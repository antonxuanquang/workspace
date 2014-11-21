package com.sean.ipfilter.main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JButton selectFile, start, clear;
	public static JTextArea valid, invalid;
	private JScrollPane jsp1, jsp2;
	private JLabel label1, label2;
	private JTextField thread, timeout;

	private File file;

	public Main()
	{
		this.setTitle("无效代理IP过滤器");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setResizable(false);

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		toolbar.setBounds(0, 0, 800, 50);
		this.add(toolbar);

		selectFile = new JButton("选择提取文件");
		toolbar.add(selectFile);
		selectFile.addActionListener(this);

		label2 = new JLabel("并发线程数");
		toolbar.add(label2);

		thread = new JTextField("1024");
		toolbar.add(thread);
		
		toolbar.add(new JLabel("超时时间"));
		timeout = new JTextField("2000");
		toolbar.add(timeout);

		start = new JButton("开始过滤");
		toolbar.add(start);
		start.addActionListener(this);

		clear = new JButton("清除");
		toolbar.add(clear);
		clear.addActionListener(this);

		label1 = new JLabel("左边为有效代理, 右边为无效代理");
		label1.setBounds(10, 50, 1000, 20);
		this.add(label1);

		valid = new JTextArea();
		jsp1 = new JScrollPane(valid);
		jsp1.setBounds(10, 80, 350, 500);
		this.add(jsp1);

		invalid = new JTextArea();
		jsp2 = new JScrollPane(invalid);
		jsp2.setBounds(400, 80, 350, 500);
		this.add(jsp2);
	}

	public static void main(String[] args)
	{
		new Main().setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == selectFile)
		{
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("选择文件", "txt");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				this.file = chooser.getSelectedFile();
			}
		}
		else if (e.getSource() == start)
		{
			if (file != null)
			{
				int threadCount = Integer.parseInt(thread.getText().trim());
				int timeoutCount = Integer.parseInt(timeout.getText().trim());
				IpChecker checker = new IpChecker(file);
				checker.start(threadCount, timeoutCount);
			}
		}
		else if (e.getSource() == clear)
		{
			valid.setText("");
			invalid.setText("");
		}
	}
}
