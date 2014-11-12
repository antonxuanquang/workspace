package com.sean.shop.search.core;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import ICTCLAS.I3S.AC.ICTCLAS50;

public class ICTCLASTokenizer extends Tokenizer
{
	private ICTCLAS50 icta;

	private Iterator<String> tokenIter;
	private List<String> tokens;

	private final CharTermAttribute termAtt;
	private final TypeAttribute typeAtt;

	private boolean isFirst = true;
	
	protected ICTCLASTokenizer(ICTCLAS50 icta, Reader reader)
	{
		super(reader);
		termAtt = addAttribute(CharTermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);

		this.icta = icta;

		tokens = this.tokenizeReader(reader);
		this.tokenIter = tokens.iterator();
	}

	@Override
	public boolean incrementToken() throws IOException
	{
		clearAttributes();

		if (tokenIter.hasNext())
		{
			String tokenstring = tokenIter.next();
			int pos = tokenstring.lastIndexOf('/');
			termAtt.append(tokenstring.substring(0, pos));
			termAtt.setLength(pos);
			typeAtt.setType(tokenstring.substring(pos + 1, tokenstring.length()));
			return true;
		}
		return false;
	}

	@Override
	public void reset() throws IOException
	{
		super.reset();

		if (isFirst)
		{
			isFirst = false;
			return;
		}
		tokens = this.tokenizeReader(input);
		this.tokenIter = tokens.iterator();
	}

	public List<String> tokenizeReader(Reader reader)
	{
		List<String> result = new ArrayList<String>(1000);
		try
		{
			StringBuilder contentbuffer = new StringBuilder();
			char[] temp = new char[1024];
			int size = 0;
			while ((size = reader.read(temp, 0, 1024)) != -1)
			{
				String tempstr = new String(temp, 0, size);
				contentbuffer.append(tempstr);
			}
			byte nativeBytes[] = icta.ICTCLAS_ParagraphProcess(contentbuffer.toString().getBytes("utf-8"), 3, 1);
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
					result.add(string);
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
				// ||type.startsWith("u") //助词
				// ||type.startsWith("e") //叹词
				// ||type.startsWith("y") //语气词
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
}