package com.sean.dm.kmeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * weka k-means工具
 * @author sean
 */
public class Kmeans
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

		// 导入kmeans训练模型
		SimpleKMeans kmeans = (SimpleKMeans) SerializationHelper.read(new FileInputStream(Model));

		// 开始预测
		double[] vector = new double[dataset.numAttributes() - 1];
		for (int i = 0; i < vector.length; i++)
		{
			vector[i] = -1;
		}

		vector[0] = 1;
		vector[1] = 1;
		vector[12] = 1;
		vector[22] = 1;
		vector[13] = 1;
		vector[50] = 1;

		Instance inst = new Instance(1, vector);
		inst.setDataset(dataset);

		// 分类
		double classId = kmeans.clusterInstance(inst);
		String[] title = new String[] { "男", "女" };
		System.out.println("预测概率: " + classId);
		System.out.println("目标分类: " + title[(int) classId]);

		// 查看整体分布
		double[] distribution = kmeans.distributionForInstance(inst);
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
		SimpleKMeans kmeans = null;

		ArffLoader loader = new ArffLoader();
		loader.setFile(new File(Train));
		ins = loader.getDataSet();

		kmeans = new SimpleKMeans();
		String options = "-N 2 -A weka.core.EuclideanDistance -R first-last -I 500 -S 10 -B";
		String[] optKmeans = weka.core.Utils.splitOptions(options);
		kmeans.setOptions(optKmeans);

		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(kmeans);

		kmeans.buildClusterer(ins);

		eval.evaluateClusterer(ins);
		System.out.println(eval.clusterResultsToString());

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Model));
		oos.writeObject(kmeans);
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

		File file = new File(Kmeans.class.getResource("/feature").getFile());
		String str = FileUtils.readFileToString(file);

		FileUtils.writeStringToFile(train, "@relation gender_predict\n", true);

		for (String it : str.split(" "))
		{
			FileUtils.writeStringToFile(train, "@attribute " + it + " integer\n", true);
		}
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
			FileUtils.writeStringToFile(train, data + "\n", true);
		}

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
