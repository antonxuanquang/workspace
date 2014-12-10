package com.sean.dm.svm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * weka svm工具
 * @author sean
 */
public class Wlsvm
{
	private static final String Train = "/home/sean/Desktop/train.arff";
	private static final String Model = "/home/sean/Desktop/model";

	/**
	 * 预测
	 * @throws Exception
	 */
	@Test
	public void predict() throws Exception
	{
		// 读取训练数据
		DataSource dataSource = new DataSource(new FileInputStream(Train));
		Instances dataset = dataSource.getDataSet();
		dataset.setClassIndex(dataset.numAttributes() - 1);

		// 导入svm训练模型
		LibSVM libsvm = (LibSVM) SerializationHelper.read(new FileInputStream(Model));
		String options = "-S 0 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -seed 1 -B";
		String[] optSVM = weka.core.Utils.splitOptions(options);
		libsvm.setOptions(optSVM);

		// 开始预测
		double[] vector = new double[dataset.numAttributes() - 1];
		for (int i = 0; i < vector.length; i++)
		{
			vector[i] = -1;
		}
		
		vector[0] = 1;
		vector[2] = 1;
		vector[3] = 1;
		vector[4] = 1;
		vector[15] = 1;
		vector[12] = 1;
		vector[13] = 1;
		vector[14] = 1;
		vector[15] = 1;
		vector[30] = 1;
		vector[31] = 1;
		vector[32] = 1;
		vector[33] = 1;
		vector[34] = 1;
		vector[43] = 1;
		vector[44] = 1;

		Instance inst = new Instance(1, vector);
		inst.setDataset(dataset);

		// 分类
		double classId = libsvm.classifyInstance(inst);
		String[] title = new String[] { "女", "男" };
		System.out.println("预测概率: " + classId);
		System.out.println("目标分类: " + title[(int) classId]);

		// 查看整体分布
		double[] distribution = libsvm.distributionForInstance(inst);
		System.out.println("概率分布: " + Arrays.toString(distribution));
	}

	/**
	 * 训练模型
	 */
	@Test
	public void train() throws Exception
	{
		long curr = System.currentTimeMillis();

		Instances ins = null;
		LibSVM libsvm = null;

		ArffLoader loader = new ArffLoader();
		loader.setFile(new File(Train));
		ins = loader.getDataSet();
		ins.setClassIndex(ins.numAttributes() - 1);

		libsvm = new LibSVM();
		String options = "-S 0 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -seed 1 -B";
		String[] optSVM = weka.core.Utils.splitOptions(options);
		libsvm.setOptions(optSVM);
		
		libsvm.buildClassifier(ins);

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Model));
		oos.writeObject(libsvm);
		oos.flush();
		oos.close();

		System.out.println(System.currentTimeMillis() - curr);
	}

	/**
	 * 生成训练数据
	 * @throws Exception
	 */
	@Test
	public void genTrain() throws Exception
	{
		File train = new File(Train);
		if (train.exists())
		{
			train.delete();
		}

		File file = new File(Wlsvm.class.getResource("/feature").getFile());
		String str = FileUtils.readFileToString(file);

		FileUtils.writeStringToFile(train, "@relation gender_predict\n", true);

		for (String it : str.split(" "))
		{
			FileUtils.writeStringToFile(train, "@attribute " + it + " integer\n", true);
		}

		FileUtils.writeStringToFile(train, "@attribute gender {0,1}\n", true);
		FileUtils.writeStringToFile(train, "@data\n", true);

		Random random = new Random();

		// 生成男性训练数据
		for (int i = 0; i < 500; i++)
		{
			// 男性特征5-27个
			String[] vector = getArray(54);
			int count = random.nextInt(22) + 5;
			for (int j = 0; j < count; j++)
			{
				int index = random.nextInt(27);
				vector[index] = "1";
			}

			// 女性特征0-3个
			count = random.nextInt(3);
			for (int j = 0; j < count; j++)
			{
				int index = random.nextInt(27) + 27;
				vector[index] = "1";
			}

			String data = showVector(vector);
			data += ",1";
			FileUtils.writeStringToFile(train, data + "\n", true);
		}

		System.out.println("");

		// 生成女性训练数据
		for (int i = 0; i < 500; i++)
		{
			// 男性特征0-3
			String[] vector = getArray(54);
			int count = random.nextInt(3);
			for (int j = 0; j < count; j++)
			{
				int index = random.nextInt(27);
				vector[index] = "1";
			}

			// 女性特征5-27个
			count = random.nextInt(22) + 5;
			for (int j = 0; j < count; j++)
			{
				int index = random.nextInt(27) + 27;
				vector[index] = "1";
			}

			String data = showVector(vector);
			data += ",0";
			FileUtils.writeStringToFile(train, data + "\n", true);
		}
	}

	private String[] getArray(int length)
	{
		String[] vector = new String[length];
		for (int i = 0; i < vector.length; i++)
		{
			vector[i] = "-1";
		}
		return vector;
	}

	private String showVector(String[] arr)
	{
		String tmp = Arrays.toString(arr);
		tmp = tmp.replace(" ", "");
		tmp = tmp.replace("[", "");
		tmp = tmp.replace("]", "");
		return tmp;
	}
}
