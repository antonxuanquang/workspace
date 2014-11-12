package com.sean.shop.search.core;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import com.sean.config.core.Config;

import ICTCLAS.I3S.AC.ICTCLAS50;

public class ICTCLASAnalyzer extends Analyzer
{
	private ICTCLAS50 icta;

	public ICTCLASAnalyzer() throws UnsupportedEncodingException
	{
		icta = new ICTCLAS50();
		String path = Config.getProperty("ictalas.path");
		// 初始化
		if (icta.ICTCLAS_Init(path.getBytes("utf-8")) == false)
		{
			System.out.println("Init Fail!");
			return;
		}

		// 设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
		icta.ICTCLAS_SetPOSmap(2);

		// 导入用户字典
		int nCount = 0;
		String usrdir = "/home/sean/Desktop/github/machine_learn/trunk/dic/shop.txt"; // 用户字典路径
		byte[] usrdirb = usrdir.getBytes();// 将string转化为byte类型
		// 导入用户字典,返回导入用户词语个数第一个参数为用户字典路径，第二个参数为用户字典的编码类型
		nCount = icta.ICTCLAS_ImportUserDictFile(usrdirb, 3);
		System.out.println("导入用户词个数" + nCount);
	}

	public List<String> token(String text)
	{
		List<String> result = new ArrayList<String>(1000);
		try
		{
			byte nativeBytes[] = icta.ICTCLAS_ParagraphProcess(text.getBytes("utf-8"), 3, 1);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "utf-8");

			// 进行词用词过滤
			String[] terms = nativeStr.split("\\s+");
			int pos;
			String term, type;
			for (String string : terms)
			{
				pos = string.lastIndexOf('/');
				if (pos == -1)
					continue;
				term = string.substring(0, pos);
				type = string.substring(pos + 1, string.length());
				if (accept(term, type))
				{
					result.add(term);
				}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		return result;
	}

	private boolean accept(String term, String type)
	{
		boolean accept = false;
		// 对词性的要求
		if (type.startsWith("n") // 名词
				|| type.startsWith("t") // 时间词
				|| type.startsWith("s") // 处所词
				|| type.startsWith("f") // 方位词
				|| type.startsWith("a") // 形容词
				|| type.startsWith("v") // 动词
				|| type.startsWith("b") // 区别词
				|| type.startsWith("z") // 状态词
				|| type.startsWith("r") // 代词
				|| type.startsWith("m") // 数词
				|| type.startsWith("q") // 量词
				|| type.startsWith("d") // 副词
				|| type.startsWith("p") // 介词
				|| type.startsWith("c") // 连词
				|| type.startsWith("u") // 助词
				|| type.startsWith("e") // 叹词
				|| type.startsWith("y") // 语气词
				|| type.startsWith("o") // 拟声词
				|| type.startsWith("h") // 前缀
				|| type.startsWith("k") // 后缀
				|| type.startsWith("x") // 网址URL
		// ||type.startsWith("w") //标点符号
		)
		{
			return true;
		}

		return accept;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader)
	{
		final Tokenizer source = new ICTCLASTokenizer(icta, reader);
		return new TokenStreamComponents(source);
	}

	@Override
	public void close()
	{
		icta.ICTCLAS_SaveTheUsrDic();
		icta.ICTCLAS_Exit();
		super.close();
	}

	public static void main(String[] args) throws Exception
	{
		final ICTCLASAnalyzer analyzer = new ICTCLASAnalyzer();
		for (int i = 0; i < 4; i++)
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						for (int j = 0; j < 5; j++)
						{
							List<String> ts = analyzer.token("TCL罗格朗开关插座面板五孔 二三插 仕界墙壁电源插旗舰店正品");
							StringBuilder sb = new StringBuilder();
							for (String it : ts)
							{
								sb.append("[" + it + "]");
							}
							System.out.println(sb.toString());
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		}
		System.in.read();
		analyzer.close();
	}
}