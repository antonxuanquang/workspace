package com.sean.im.client.comp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.UUID;

import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.util.IMIoUtil;
import com.sean.im.client.util.RecordUtil;

/**
 * 录音控件
 * @author sean
 */
public class RecordComp extends CommonButton
{
	private static final long serialVersionUID = 1L;
	private long interval;
	private RecordListener recordListener;
	private RecordUtil recordUtil;
	private File targetFile;

	public RecordComp(RecordListener listener)
	{
		super("hold on");
		this.recordListener = listener;
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				IMIoUtil.checkUserAudioDir(ApplicationContext.User.getUsername());
				targetFile = new File(Global.Root + "users/" + ApplicationContext.User.getUsername() + "/recv/audio/" + UUID.randomUUID().toString()
						+ ".wav");
				recordUtil = new RecordUtil(targetFile);
				interval = System.currentTimeMillis();
				if (recordListener != null)
				{
					recordListener.beforeRecord();
				}
				recordUtil.startRecord();
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				long curr = System.currentTimeMillis();
				if ((curr - interval) > 500)
				{
					recordListener.afterRecord(targetFile);
				}
				else
				{
					recordListener.afterRecord(null);
				}
				recordUtil.stopRecording();
				targetFile = null;
				recordUtil = null;
				System.gc();
			}
		});
	}

	public interface RecordListener
	{
		public void beforeRecord();

		public void afterRecord(File audio);
	}
}
