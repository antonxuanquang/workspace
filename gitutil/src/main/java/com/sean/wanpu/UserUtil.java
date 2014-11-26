package com.sean.wanpu;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

public class UserUtil
{	
	public static void genImsi() throws Exception
	{
		Random r = new Random();

		List<String> users = FileUtils.readLines(new File("/home/sean/Desktop/user"));
		File target = new File("/home/sean/Desktop/target");
		int count = 0;
		
		for (String it : users)
		{
			String[] record = it.split("\t");
			StringBuilder imsi = new StringBuilder(record[1]);
			if (record.length == 8)
			{
				// 移动:[0-4], 电信[5-7], 联通[8-9]
				int frequency = r.nextInt(10);
				// 移动00、02、07
				if (frequency >= 0 && frequency <= 4)
				{
					int tmp = r.nextInt(3) + 1;
					if (tmp == 1)
					{
						imsi.setCharAt(4, '0');
					}
					else if (tmp == 2)
					{
						imsi.setCharAt(4, '2');
					}
					else if (tmp == 3)
					{
						imsi.setCharAt(4, '7');
					}
				}
				// 电信03、05
				if (frequency >= 5 && frequency <= 7)
				{
					int tmp = r.nextInt(2) + 1;
					if (tmp == 1)
					{
						imsi.setCharAt(4, '3');
					}
					else if (tmp == 2)
					{
						imsi.setCharAt(4, '5');
					}
				}
				// 联通01、06
				if (frequency >= 8 && frequency <= 9)
				{
					int tmp = r.nextInt(2) + 1;
					if (tmp == 1)
					{
						imsi.setCharAt(4, '1');
					}
					else if (tmp == 2)
					{
						imsi.setCharAt(4, '6');
					}
				}
			}
			
			// 写出
			StringBuilder rs = new StringBuilder();
			rs.append(record[0]).append('\t');
			rs.append(imsi.toString()).append('\t');
			rs.append(record[2]).append('\t');
			rs.append(record[3]).append('\t');
			rs.append(record[4]).append('\t');
			rs.append(record[5]).append('\t');
			rs.append(record[6]).append('\t');
			rs.append(record[7]).append('\n');
			
			FileUtils.writeStringToFile(target, rs.toString(), true);
			
			count++;
			System.out.println(count);
		}
		System.out.println("over");
	}
}
