package com.sean.im.client.util;

import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class MusicUtil
{
	@SuppressWarnings("deprecation")
	public synchronized static void play(final String filepath)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					AudioClip audioclip = java.applet.Applet.newAudioClip(new File(filepath).toURL());
					audioclip.play();
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	public synchronized static void play(File file)
	{
		try
		{
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			AudioFormat aif = ais.getFormat();
			SourceDataLine sdl = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
			sdl = (SourceDataLine) AudioSystem.getLine(info);
			sdl.open(aif);
			sdl.start();
			int nByte = 0;
			byte[] buffer = new byte[128];
			while (nByte != -1)
			{
				nByte = ais.read(buffer, 0, 128);
				if (nByte >= 0)
				{
					sdl.write(buffer, 0, nByte);
				}
			}
			sdl.stop();
			ais.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
