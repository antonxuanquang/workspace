define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./index');

	var detail =
	{
		goodId : 0,
		
		// 初始化
		init : function()
		{
			T.index.initUI();

			detail.getGood();
		},

		// 读取商品列表
		getGood : function()
		{
			detail.goodId = T.common.util.getParameter("id");
			// 读取商品列表
			var params =
			{
				goodId : detail.goodId
			};
			T.common.ajax.requestBlock("InquireGoodDetailAction", params, false, function(jsonstr, data, code, msg)
			{
				data.goodDetail.jifen = data.goodDetail.price * 100 * 2;
				var tpl = $('#tpl_goodDetail').html();
				var html = juicer(tpl, data.goodDetail);
				$('#goodDetail').html(html);

				$('#feedback').bind('click', function()
				{
					var params =
					{
						goodId : data.goodDetail.goodId
					}
					T.common.ajax.requestBlock('FeedbackGoodAction', params, false, function()
					{
						alert("非常感谢您的反馈, 我们会做的更好^-^");
					});
				});
				
				$('#summitOrder').bind('click', function()
				{
					var params =
					{
						goodId : detail.goodId,
						tel : $('#tel').val(),
						qq : $('#qq').val(),
						address : $('#address').val(),
						remark : $('#remark').val(),
					};
					T.common.ajax.requestBlock('CreateOrderAction', params, false, function()
					{
						alert("成功下订单, 请等待后台处理, 我们会和您联系, 祝您购物愉快^_^");
					});
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
		init : detail.init,
	};

	exports.detail = api;
	T.detail = api;
});
