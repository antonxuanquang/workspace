package com.sean.shop.search.core;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.Floats;
import org.apache.lucene.search.Query;

/**
 * 加权评分查询
 * @author sean
 */
public class BoostQuery extends CustomScoreQuery
{
	public BoostQuery(Query subQuery)
	{
		super(subQuery);
	}

	@Override
	protected CustomScoreProvider getCustomScoreProvider(AtomicReaderContext context) throws IOException
	{
		return new BoostScorerProvider(context);
	}

	public class BoostScorerProvider extends CustomScoreProvider
	{
		private Floats boosts;

		public BoostScorerProvider(AtomicReaderContext context)
		{
			super(context);

			try
			{
				// note: 该操作lucene内部已经实现了缓存, 无须在做缓存
				boosts = FieldCache.DEFAULT.getFloats(context.reader(), "boost", false);
			}
			catch (IOException e)
			{
				throw new RuntimeException("读取文档boost列表异常:" + e.getMessage(), e);
			}
		}

		@Override
		public float customScore(int docID, float subQueryScore, float valSrcScore) throws IOException
		{
			// 评分算法 = 默认余弦相似评分 * 加权因子boost
			float oriScore = super.customScore(docID, subQueryScore, valSrcScore);
			float boost = boosts.get(docID);
			return oriScore * boost;
		}
	}

}
