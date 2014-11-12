package com.sean.shop.search.core;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 索引工具
 * @author sean
 */
public class IndexUtil
{
	/**
	 * 打开目录
	 * @param path
	 * @return
	 */
	public static Directory openDir(String path) throws IOException
	{
		// 创建索引目录
		File dir = new File(path);
		if (!dir.exists())
		{
			dir.mkdir();
		}
		return FSDirectory.open(dir);
	}

	/**
	 * 打开内存目录
	 * @param path
	 * @return
	 */
	public static RAMDirectory openRAMDir() throws IOException
	{
		RAMDirectory dir = new RAMDirectory();
		return dir;
	}

	/**
	 * 创建IndexWriter
	 * @param path
	 * @return
	 * @throws Exception 
	 */
	public static IndexWriter createIndexWriter(String path) throws IOException
	{
		Directory dir = openDir(path);
		return createIndexWriter(dir);
	}

	/**
	 * 创建IndexWriter
	 * @param path
	 * @return
	 */
	public static IndexWriter createIndexWriter(Directory dir) throws IOException
	{
		// 立即解锁，防止索引被锁
		if (IndexWriter.isLocked(dir))
		{
			IndexWriter.unlock(dir);
		}
		// 初始化writer配置
//		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_4_10_0, new ICTCLASAnalyzer());
		
		// 采用IK 最细分词
		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_4_10_0, new IKAnalyzer(true));
		return new IndexWriter(dir, cfg);
	}

	/**
	 * 创建IndexReader
	 * @param path
	 * @return
	 */
	public static IndexReader createIndexReader(String path) throws IOException
	{
		Directory dir = openDir(path);
		return createIndexReader(dir);
	}

	/**
	 * 创建IndexReader
	 * @param path
	 * @return
	 */
	public static IndexReader createIndexReader(Directory dir) throws IOException
	{
		try
		{
			IndexReader reader = DirectoryReader.open(dir);
			return reader;
		}
		catch (Exception e)
		{
			IndexWriter iw = createIndexWriter(dir);
			iw.commit();
			iw.close();
			IndexReader reader = DirectoryReader.open(dir);
			return reader;
		}
	}

	/**
	 * 创建IndexReader
	 * @param path
	 * @return
	 */
	public static IndexReader createIndexReader(IndexWriter writer) throws IOException
	{
		IndexReader reader = DirectoryReader.open(writer, true);
		return reader;
	}
}
