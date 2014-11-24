package com.sean.bigdata.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		options.addOption("time", true, "执行时间, 格式: yyyyMMddHHmmss");
		options.addOption("check", true, "是否检查数据, 不写入数据库, true/false");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		String hdfs = cmd.getOptionValue("hdfs");
		String file = cmd.getOptionValue("file");
		String split = cmd.getOptionValue("split");
		long reportId = Long.parseLong(cmd.getOptionValue("reportId"));
		long time = Long.parseLong(cmd.getOptionValue("time"));
		String userKey = cmd.getOptionValue("userKey");
		boolean check = Boolean.parseBoolean(cmd.getOptionValue("check"));

		Map<String, String> params = new HashMap<>();
		params.put("hdfs", hdfs);
		params.put("file", file);
		params.put("split", split);
		params.put("reportId", reportId + "");
		params.put("time", time + "");
		params.put("userKey", userKey);
		params.put("check", String.valueOf(check));
		logger.info("参数列表: " + params);

		// 启动持久层框架
		Config.readConfiguration();
		PersistLaucher.getInstance().launch(new String[] { "com.sean.bigdata" });

		ReportEntity report = checkReport(reportId);
		List<String> data = getHdfsData(hdfs, file);
		String json = checkData(report, data, split);
		logger.info("通过验证, 数据合法, 生成json: " + json);
		if (!check)
		{
			insert(report, json, time);
		}

		// ReportEntity report1 = checkReport(1);
		// List<String> data1 = getHdfsData("hdfs://master:8020",
		// "/tmp/viewlog/count/value_1");
		// String json1 = checkData(report1, data1, "\t");
		// System.out.println(json1);
		// insert(report1, json1, 20141101000000L);

		// ReportEntity report2 = checkReport(2);
		// List<String> data2 = getHdfsData("hdfs://master:8020",
		// "/tmp/viewlog/count/value_2");
		// String json2 = checkData(report2, data2, "\t");
		// System.out.println(json2);
		// insert(report2, json2, 20141101000000L);
		//
		// ReportEntity report3 = checkReport(3);
		// List<String> data3 = getHdfsData("hdfs://master:8020",
		// "/tmp/viewlog/count/value_3");
		// String json3 = checkData(report3, data3, "\t");
		// System.out.println(json3);
		// insert(report3, json3, 20141101000000L);
		//
		// ReportEntity report4 = checkReport(7);
		// List<String> data4 = getHdfsData("hdfs://master:8020",
		// "/tmp/viewlog/count/value_4");
		// String json4 = checkData(report4, data4, "\t");
		// System.out.println(json4);
		// insert(report4, json4, 20141101000000L);
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
		// 多值
		else if (report.type == 4)
		{
			int columnSize = report.columnTags.split(";").length;
			if (data.size() == 1)
			{
				String[] columns = data.get(0).split(split, Integer.MAX_VALUE);
				// [123,123,123]
				if (columns.length == columnSize)
				{
					try
					{
						StringBuilder json = new StringBuilder("[");
						for (String it : columns)
						{
							json.append(Double.parseDouble(it)).append(", ");
						}
						json.setLength(json.length() - 1);
						json.setCharAt(json.length() - 1, ']');
						return json.toString();
					}
					catch (Exception e)
					{
						throw new RuntimeException("多值报表数据格式错误, 请参考格式: (输出文件格式: 单行多列, 所有列为数值, 列数预报表定义一致)");
					}
				}
				else
				{
					throw new RuntimeException("多值报表数据格式错误, 请参考格式: (输出文件格式: 单行多列, 所有列为数值, 列数预报表定义一致)");
				}
			}
			throw new RuntimeException("多值报表数据格式错误, 请参考格式: (输出文件格式: 单行多列, 所有列为数值, 列数预报表定义一致)");
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
			logger.info("成功导入执行结果:" + ee.getValues());
		}
		catch (Exception e)
		{
			throw new RuntimeException("日期格式错误, 格式为yyyyMMddHHmmss");
		}
	}
}
