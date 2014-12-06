define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('../../script/common');
	require('./util');

	var GoodManager =
	{
		currChannel : -1, // 当前渠道
		currPageNo : 1, // 当前页码
		currQuery : "", // 当前搜索条件
		currStatus : -1, // 当前商品状态

		categoryMap : new Object(),

		init : function()
		{
			var href = location.href;
			// 商品列表
			if (href.indexOf("goodlist.html") != -1)
			{
				$('#search').bind('click', GoodManager.search);
			}
			// 添加商品
			else if (href.indexOf("addgood.html") != -1)
			{
				$('#addgood').bind('click', GoodManager.addGood);
			}
			// 商品反馈
			else if (href.indexOf("goodfeedback.html") != -1)
			{
				GoodManager.getFeedbackList();
			}
		},

		// 搜索
		search : function()
		{
			GoodManager.currPageNo = 1;
			GoodManager.currChannel = $('#channel').val();
			GoodManager.currQuery = $('#keyword').val();
			GoodManager.currStatus = $('#status').val();

			GoodManager.getGoodList();
			return false;
		},

		// 读取商品列表
		getGoodList : function()
		{
			$('#goodList').html("正在读取数据...");

			// 读取商品列表
			var params =
			{
				pageNo : GoodManager.currPageNo
			};
			if (GoodManager.currQuery != "")
			{
				params.keyword = GoodManager.currQuery;
			}
			if (GoodManager.currChannel != -1)
			{
				params.channel = GoodManager.currChannel;
			}
			if (GoodManager.currStatus != -1)
			{
				params.status = GoodManager.currStatus;
			}

			T.common.ajax.requestBlock("SearchGood4ConsoleAction", params, false, function(jsonstr, data, code, msg)
			{
				var goodList = data.goodList4Console;
				for (var i = 0; i < goodList.length; i++)
				{
					var it = goodList[i];
					it.name = it.goodName.substring(0, 16);
				}

				var tplData =
				{
					goodList : goodList
				};
				var tpl = $('#tpl_goodList').html();
				var html = juicer(tpl, tplData);
				$('#goodList').html(html);

				// 计算分页
				T.util.initPage(GoodManager.currPageNo, data.totalrecord, 24, function(pageNo)
				{
					GoodManager.currPageNo = pageNo;
					GoodManager.getGoodList();
				});

				// 添加按钮事件
				$('#goodList button').bind('click', function()
				{
					var btn = $(this);
					if (btn.html() == "查看")
					{
						open(btn.attr('url'));
					}
					if (btn.html() == "下架" || btn.html() == "上架")
					{
						var r = confirm("确定要修改该商品状态吗?")
						if (r == true)
						{
							var params =
							{
								goodId : btn.attr('goodId')
							};
							T.common.ajax.requestBlock('UpdateGoodStatusAction', params, false, function()
							{
								alert("修改成功");
								GoodManager.getGoodList();
							});
						}
					}
					if (btn.html() == "删除")
					{
						var r = confirm("确定要删除该商品吗?")
						if (r == true)
						{
							var params =
							{
								goodId : btn.attr('goodId')
							};
							T.common.ajax.requestBlock('DeleteGoodAction', params, false, function()
							{
								alert("删除成功");
								GoodManager.getGoodList();
							});
						}
					}
				});
			});
		},

		// 读取商品反馈列表
		getFeedbackList : function()
		{
			var pageNo = GoodManager.currPageNo;
			$('#feedbackList').html("正在读取数据...");

			// 读取商品列表
			var params =
			{
				pageNo : pageNo
			};

			T.common.ajax.requestBlock("InquireFeedbackAction", params, false, function(jsonstr, data, code, msg)
			{
				var feedbackList = data.feedbackList;
				for (var i = 0; i < feedbackList.length; i++)
				{
					var it = feedbackList[i];
					it.name = it.goodName.substring(0, 10);
				}
				var tplData =
				{
					feedbackList : feedbackList
				};
				var tpl = $('#tpl_feedbackList').html();
				var html = juicer(tpl, tplData);
				$('#feedbackList').html(html);

				// 计算分页
				T.util.initPage(GoodManager.currPageNo, data.totalrecord, 24, function(pageNo)
				{
					GoodManager.currPageNo = pageNo;
					GoodManager.getFeedbackList();
				});

				// 添加按钮事件
				$('#feedbackList button').bind('click', function()
				{
					var btn = $(this);
					if (btn.html() == "查看")
					{
						open(btn.attr('url'));
					}
					if (btn.html() == "下架")
					{
						var r = confirm("确定要下架该商品状态吗?")
						if (r == true)
						{
							var params =
							{
								goodId : btn.attr('goodId')
							};
							T.common.ajax.requestBlock('UpdateGoodStatusAction', params, false, function()
							{
								alert("修改成功");
							});
						}
					}
					if (btn.html() == "删除商品")
					{
						var r = confirm("确定要删除该商品吗?")
						if (r == true)
						{
							var params =
							{
								goodId : btn.attr('goodId')
							};
							T.common.ajax.requestBlock('DeleteGoodAction', params, false, function()
							{
								alert("删除成功");
							});
						}
					}
					if (btn.html() == "删除反馈")
					{
						var r = confirm("确定要删除该反馈吗?")
						if (r == true)
						{
							var params =
							{
								feedbackId : btn.attr('feedbackId')
							};
							T.common.ajax.requestBlock('DeleteFeedbackAction', params, false, function()
							{
								alert("删除成功");
								GoodManager.getFeedbackList();
							});
						}
					}
				});
			});
		},

		// 添加商品
		addGood : function()
		{
			var params =
			{
				goodName : $('#goodName').val(),
				price : $('#price').val(),
				imageUrl : $("#imageUrl").val(),
				goodUrl : $('#goodUrl').val(),
				channel : $('#channel').val(),
				keyword : $('#keyword').val(),
				boost : $('#boost').val(),
				saleCount : $('#saleCount').val(),
			};
			T.common.ajax.requestBlock('AddGoodAction', params, false, function()
			{
				alert("添加成功, 大概要5秒后才能搜索到哦");
			});
		},
	};

	GoodManager.init();
});
