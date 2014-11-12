define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./index');

	var market =
	{
		query : '', // 当前查询语句
		channel : -1, // 当前渠道
		pageNo : 1, // 当前页码
		categoryId : -1, // 当前分类
		type : 1, // 表示来源: 1-商品分类, 2-渠道, 3-搜索
		ranking : 1, // 排名

		// 初始化
		init : function()
		{
			T.index.initUI();

			// 来自渠道
			if (T.common.util.getParameter('channel') != null)
			{
				market.type = 2;
				market.channel = T.common.util.getParameter('channel');
				market.pageNo = 1;
				$('#categoryMap').hide();
			}
			// 来自搜索
			else if (T.common.util.getParameter('q') != null)
			{
				market.type = 3;
				market.query = T.common.util.getParameter('q');
				market.channel = T.common.util.getParameter('c');
				market.pageNo = 1;
				$('#categoryMap').hide();

				$('#query').val(market.query);
			}

			// 搜索排名
			$('#ranking>button').bind('click', function()
			{
				$('#ranking>button').attr('class', 'btn btn-default');
				$(this).attr('class', 'btn btn-default active');
				market.ranking = $(this).attr('ranking');
				market.pageNo = 1;
				market.getGoodList();
			});

			market.getGoodList();
		},

		// 读取商品列表
		getGoodList : function()
		{
			var pageNo = market.pageNo;
			$('#goodListWrap').html('<div align="center"><img src="http://seanzwx.github.io/97igo/image/loading.gif"/></div>');

			// 读取商品列表
			var params =
			{
				pageNo : market.pageNo,
				ranking : market.ranking,
			};
			// 分类
			if (market.type == 1)
			{
				params.categoryId = market.categoryId;
			}
			// 渠道
			else if (market.type == 2)
			{
				params.channel = market.channel;
			}
			// 搜索
			else if (market.type == 3)
			{
				params.keyword = market.query;
				if (market.channel != null && market.channel != -1)
				{
					params.channel = market.channel;
				}
			}

			T.common.ajax.requestBlock("SearchGoodAction", params, false, function(jsonstr, data, code, msg)
			{
				for (var i = 0; i < data.goodList.length; i++)
				{
					data.goodList[i].shortName = data.goodList[i].goodName.substring(0, 20);
				}

				var tplData =
				{
					goodList : data.goodList
				};
				var tpl = $('#tpl_goodList').html();
				var html = juicer(tpl, tplData);
				$('#goodListWrap').html(html);
				// 加载图片
				$("img.lazy").lazyload(
				{
					effect : "fadeIn"
				});

				$('#goodList>div').bind('mouseover', function()
				{
					$(this).css('border-color', '#FF4400');
					$(this).find('div[class=exception]').show();
				}).bind('mouseout', function()
				{
					$(this).css('border-color', '#dcdcdc');
					$(this).find('div[class=exception]').hide();
				});
				$('#goodList div[class=exception]').bind('click', function()
				{
					var params =
					{
						goodId : $(this).attr('goodId')
					}
					T.common.ajax.requestBlock('FeedbackGoodAction', params, false, function()
					{
						alert("非常感谢您的反馈, 我们会做的更好^-^");
					});
				});

				// 计算分页
				var totalrecords = data.totalrecord;
				var totalpages = totalrecords % 24 == 0? parseInt(totalrecords / 24): parseInt(totalrecords / 24) + 1;

				var pages = new Array();
				for (var i = pageNo; i <= totalpages && pages.length < 10; i++)
				{
					pages[pages.length] =
					{
						pageNo : i
					};
				}

				tplData.pages = pages;
				tplData.last = totalpages;
				tplData.next = pageNo >= totalpages? totalpages: parseInt(pageNo) + 1;
				tplData.previous = pageNo == 1? 1: parseInt(pageNo) - 1;

				tpl = $('#tpl_page').html();
				html = juicer(tpl, tplData);
				$('#pages').html(html);

				$('#pages li').bind('click', function()
				{
					var pageNo = $(this).attr("pageNo");
					market.pageNo = pageNo;
					market.getGoodList();
				});
			});
			
			$('html,body').animate(
			{
				scrollTop : 0
			}, 300);
		},
	};

	var api =
	{
		init : market.init,
	};

	exports.market = api;
	T.market = api;
});
