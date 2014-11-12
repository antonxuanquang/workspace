package com.sean.bigdata.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sean.bigdata.entity.ExecuteEntity;
import com.sean.bigdata.entity.ReportEntity;
import com.sean.config.core.Config;
import com.sean.log.core.LogFactory;
import com.sean.persist.core.Dao;
import com.sean.persist.core.PersistLaucher;

public class HDFSImportor
{
	private static final Logger logger = LogFactory.getLogger(HDFSImportor.class);

	public static void main(String[] args) throws Exception
	{
		Options options = new Options();
		options.addOption("hdfs", true, "hdfs地址: hfds://master:8020");
		options.addOption("file", true, "导入报表系统数据文件或者目录: /tmp/text");
		options.addOption("split", true, "记录的分割字符");
		options.addOption("reportId", true, "导入报表系统的报表ID");
		options.addOption("userKey", true, "报表归属用户的key");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		String hdfs = cmd.getOptionValue("hdfs");
		String file = cmd.getOptionValue("file");
		String split = cmd.getOptionValue("split");
		long reportId = Long.parseLong(cmd.getOptionValue("reportId"));
		String userKey = cmd.getOptionValue("userKey");

		// 启动持久层框架
		Config.readConfiguration();
		PersistLaucher.getInstance().launch(new String[] { "com.sean.bigdata" });

		ReportEntity report1 = checkReport(1);
		List<String> data1 = getHdfsData("hdfs://master:8020", "/tmp/viewlog/count/value_1");
		String json1 = checkData(report1, data1, "\t");
		System.out.println(json1);
		insert(report1, json1, 20141101);

		ReportEntity report2 = checkReport(2);
		List<String> data2 = getHdfsData("hdfs://master:8020", "/tmp/viewlog/count/value_2");
		String json2 = checkData(report2, data2, "\t");
		System.out.println(json2);
		insert(report2, json2, 20141101);

		ReportEntity report3 = checkReport(3);
		List<String> data3 = getHdfsData("hdfs://master:8020", "/tmp/viewlog/count/value_3");
		String json3 = checkData(report3, data3, "\t");
		System.out.println(json3);
		insert(report3, json3, 20141101);
	}

	private static ReportEntity checkReport(long reportId)
	{
		ReportEntity report = Dao.loadById(ReportEntity.class, reportId);
		if (report != null)
		{
			// TODO 检查报表所属人权限
			return report;
		}
		else
		{
			throw new RuntimeException("报表不存在:ID = " + reportId);
		}
	}

	private static String checkData(ReportEntity report, List<String> data, String split)
	{
		// 单值: 单行单列单数字
		if (report.type == 1)
		{
			if (data.size() == 1)
			{
				String[] columns = data.get(0).split(split, Integer.MAX_VALUE);
				if (columns.length == 1)
				{
					try
					{
						Double.parseDouble(columns[0]);
						return columns[0];
					}
					catch (Exception e)
					{
					}
				}
			}
			throw new RuntimeException("单值报表数据格式错误, 请参考格式: (输出文件格式: 单行单列单数字)");
		}
		// 数值: 多行, 每行两列, 第一列为key, 第二列为value
		else if (report.type == 2)
		{
			if (data.size() > 0)
			{
				JSONArray ja = new JSONArray();
				for (String it : data)
				{
					JSONObject obj = new JSONObject();
					String[] columns = it.split(split, Integer.MAX_VALUE);

					if (columns.length == 2)
					{
						try
						{
							Double.parseDouble(columns[1]);

							obj.put("k", columns[0]);
							obj.put("v", columns[1]);
							ja.add(obj);
						}
						catch (Exception e)
						{
							throw new RuntimeException("数值报表数据格式错误, 请参考格式: (输出文件格式: 多行, 每行两列, 第一列为key, 第二列为value, value必须为数值)");
						}
					}
					else
					{
						throw new RuntimeException("数值报表数据格式错误, 请参考格式: (输出文件格式: 多行, 每行两列, 第一列为key, 第二列为value, value必须为数值)");
					}
				}
				return ja.toJSONString();
			}
			throw new RuntimeException("数值报表数据格式错误, 请参考格式: (输出文件格式: 多行, 每行两列, 第一列为key, 第二列为value, value必须为数值)");
		}
		// 列表
		else if (report.type == 3)
		{
			int columnSize = report.conditions.split(";").length;
			if (data.size() > 0)
			{
				JSONArray ja = new JSONArray();
				for (String it : data)
				{
					JSONObject obj = new JSONObject();
					String[] columns = it.split(split, Integer.MAX_VALUE);

					// [{"v":"10", "c":[{"c":"1800"}, {"c":"1"}]}
					if (columns.length == (columnSize + 1))
					{
						try
						{
							Double.parseDouble(columns[columns.length - 1]);

							obj.put("v", columns[columns.length - 1]);

							JSONArray ja2 = new JSONArray();
							for (int i = 0; i < columns.length - 1; i++)
							{
								JSONObject obj1 = new JSONObject();
								obj1.put("c", columns[i]);
								ja2.add(obj1);
							}
							obj.put("c", ja2);

							ja.add(obj);
						}
						catch (Exception e)
						{
							throw new RuntimeException("列表报表数据格式错误, 请参考格式: (输出文件格式: 多行, 每行多列, 具体根据报表定义限定列数, 前几列为分组条件, 最后一列为value, value必须为数值)");
						}
					}
					else
					{
						throw new RuntimeException(
								"列表报表数据格式错误, 数据列数与报表定义不一致, 请参考格式: (输出文件格式: 多行, 每行多列, 具体根据报表定义限定列数, 前几列为分组条件, 最后一列为value, value必须为数值)");
					}
				}
				return ja.toJSONString();
			}
			throw new RuntimeException("列表报表数据格式错误, 请参考格式: (输出文件格式: 多行, 每行多列, 具体根据报表定义限定列数, 前几列为分组条件, 最后一列为value, value必须为数值)");
		}
		return null;
	}

	private static List<String> getHdfsData(String hdfs, String file) throws Exception
	{
		LinkedList<String> lines = new LinkedList<>();
		Configuration cfg = new Configuration();
		FileSystem fs = FileSystem.get(new URI(hdfs), cfg);
		Path path = new Path(file);
		if (fs.exists(path))
		{
			if (fs.isFile(path))
			{
				getFile(fs, path, lines);
			}
			else
			{
				FileStatus[] list = fs.listStatus(path);
				for (FileStatus it : list)
				{
					if (it.isFile())
					{
						getFile(fs, it.getPath(), lines);
					}
				}
			}

			if (lines.isEmpty())
			{
				throw new RuntimeException("hdfs数据文件为空:" + hdfs + "/" + file);
			}
			return lines;
		}
		else
		{
			throw new RuntimeException("hdfs数据文件不存在:" + hdfs + "/" + file);
		}
	}

	private static void getFile(FileSystem fs, Path file, LinkedList<String> lines) throws Exception
	{
		FSDataInputStream input = fs.open(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
		reader.close();
	}

	private static void insert(ReportEntity report, String json, long time)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf.parse(String.valueOf(time));

			ExecuteEntity ee = new ExecuteEntity();
			ee.executeTime = time;
			ee.reportId = report.reportId;
			ee.result = json;
			Dao.persist(ExecuteEntity.class, ee);
		}
		catch (Exception e)
		{
			throw new RuntimeException("日期格式错误, 格式为yyyyMMddHHmmss");
		}
	}
}
