package ICTCLAS.I3S.AC;



public class Main
{
	public static void main(String[] args)
	{
		try
		{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			// 分词所需库的路径
			String argu = "/home/sean/Desktop/github/machine_learn/trunk/ictclas/API";
			// 初始化
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("utf-8")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}
			else
			{
				System.out.println("Init Succeed!");
			}

			String sInput = "TCL罗格朗开关插座面板五孔 二三插 仕界墙壁电源插旗舰店正品";

			byte[] nativeBytes = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("utf-8"), 0, 1);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "utf-8");
			String[] tmp = nativeStr.split(" ");
			for (String it : tmp)
			{
				System.out.print("[" + it + "]");
			}
			System.out.println();

			// 导入用户字典
			String usrdir = "/home/sean/share/ICTCLAS50_Linux_RHAS_32_JNI/API/userdic.txt"; // 用户字典路径
			byte[] usrdirb = usrdir.getBytes();
			// 第一个参数为用户字典路径，第二个参数为用户字典的编码类型(0:type unknown;1:ASCII码;2:GB2312,GBK,GB10380;3:UTF-8;4:BIG5)
			int nCount = testICTCLAS50.ICTCLAS_ImportUserDictFile(usrdirb, 3);
			System.out.println("导入用户词个数" + nCount);

			nativeBytes = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("utf-8"), 3, 1);
			nativeStr = new String(nativeBytes, 0, nativeBytes.length, "utf-8");
			tmp = nativeStr.split(" ");
			for (String it : tmp)
			{
				System.out.print("[" + it + "]");
			}
			System.out.println();

			testICTCLAS50.ICTCLAS_Exit();
		}
		catch (Exception ex)
		{
		}
	}
}
