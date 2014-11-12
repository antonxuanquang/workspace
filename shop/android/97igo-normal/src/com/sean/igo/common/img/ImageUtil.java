package com.sean.igo.common.img;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sean.igo.R;
import com.sean.igo.common.util.ConstantUtil;

public class ImageUtil
{
	private static final ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions options;

	public static void saveFile(InputStream input, String filepath)
	{
		File f = new File(filepath);
		try
		{
			FileOutputStream out = new FileOutputStream(f);
			byte[] buf = new byte[1024 * 10];
			int length = 0;
			while ((length = input.read(buf)) > 0)
			{
				out.write(buf, 0, length);
			}
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 初始化图片加载
	 * @param context
	 */
	public static void initImageLoader(Context context)
	{
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, ConstantUtil.CacheDir);
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_img).cacheOnDisc().cacheInMemory()
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();

		imageLoader.init(config);
	}

	/**
	 * 读取图片加载配置
	 * @return
	 */
	public static DisplayImageOptions getImageLoaderOption()
	{
		return options;
	}
}
