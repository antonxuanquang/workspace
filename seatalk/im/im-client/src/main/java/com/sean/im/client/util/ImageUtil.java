package com.sean.im.client.util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageUtil
{
	/**
	 * 获取灰度图片
	 * @param filename
	 * @return
	 */
	public static BufferedImage getImage(File file)
	{
		try
		{
			BufferedImage image = ImageIO.read(file);
			return image;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取灰度图片
	 * @param filename
	 * @return
	 */
	public static BufferedImage grayImage(String filename)
	{
		return grayImage(new File(filename));
	}

	/**
	 * 获取灰度图片
	 * @param filename
	 * @return
	 */
	public static BufferedImage grayImage(File file)
	{
		try
		{
			BufferedImage image = ImageIO.read(file);
			return grayImage(image);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取灰度图片
	 * @param filename
	 * @return
	 */
	public static BufferedImage grayImage(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				int rgb = image.getRGB(i, j);
				grayImage.setRGB(i, j, rgb + 100);
			}
		}
		return grayImage;
	}
}
