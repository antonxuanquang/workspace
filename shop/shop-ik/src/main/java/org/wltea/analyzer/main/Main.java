package org.wltea.analyzer.main;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		String txt = "2014秋装新款毛呢连衣裙韩版修身七分袖打底裙 七分款套装女包邮";
		StringReader reader = new StringReader(txt.toLowerCase());
		IKSegmenter ik = new IKSegmenter(reader, true);
		Lexeme lexeme = null;
		while ((lexeme = ik.next()) != null)
		{
			System.out.print("[" + lexeme.getLexemeText() + "]");
		}
	}
}