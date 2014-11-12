package com.sean.im.client.util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.apache.log4j.Logger;

import com.sean.im.commom.constant.Loggers;
import com.sean.log.core.LogFactory;

/**
 * 录音工具类
 * @author sean
 */
public class RecordUtil implements Runnable
{
	private TargetDataLine targetDataLine;
	private AudioFileFormat.Type targetType;
	private AudioInputStream m_audioInputStream;
	private File targetFile;

	private static final Logger logger = LogFactory.getLogger(Loggers.IM);

	public RecordUtil(File targetFile)
	{
		try
		{
			AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 8, 1, 1, 44100F, false);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(audioFormat);

			targetType = AudioFileFormat.Type.AU;

			m_audioInputStream = new AudioInputStream(targetDataLine);
			this.targetFile = targetFile;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	public void startRecord()
	{
		targetDataLine.start();
		new Thread(this).start();
	}

	public void stopRecording()
	{
		targetDataLine.stop();
		targetDataLine.close();
		logger.debug("record stop");
	}

	@Override
	public void run()
	{
		try
		{
			AudioSystem.write(m_audioInputStream, targetType, targetFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}
}
