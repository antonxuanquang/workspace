define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('../../script/common');
	require('./util');

	var ActiveManager =
	{
		currPageNo : 1,

		init : function()
		{
			var href = location.href;
			// 活动列表
			if (href.indexOf("activelist.html") != -1)
			{
				ActiveManager.getActiveList();
			}
			// 发布活动
			else if (href.indexOf("addactive.html") != -1)
			{
				$('#addactive').bind('click', ActiveManager.addActive);
			}
		},

		getActiveList : function()
		{
			$('#activeList').html("正在读取数据...");
			var params =
			{
				activing : "0",
				pageNo : ActiveManager.currPageNo,
			};

			T.common.ajax.requestBlock("InquireActiveListAction", params, false, function(jsonstr, data, code, msg)
			{
				var activeList = data.activeList;

				var tplData =
				{
					activeList : activeList
				};
				var tpl = $('#tpl_activeList').html();
				var html = juicer(tpl, tplData);
				$('#activeList').html(html);

				// 计算分页
				T.util.initPage(ActiveManager.currPageNo, data.totalrecord, 24, function(pageNo)
				{
					ActiveManager.currPageNo = pageNo;
					ActiveManager.getActiveList();
				});

				// 添加按钮事件
				$('#activeList button').bind('click', function()
				{
					var btn = $(this);
					if (btn.html() == "删除")
					{
						var r = confirm("确定要删除该活动吗?")
						if (r == true)
						{
							var params =
							{
								activeId : btn.attr('activeId')
							};
							T.common.ajax.requestBlock('DeleteActiveAction', params, false, function()
							{
								alert("删除成功");
								ActiveManager.getActiveList();
							});
						}
					}
					if (btn.html() == "查看")
					{
						open(btn.attr('url'));
					}
				});
			});
		},

		addActive : function()
		{
			var params =
			{
				activeName : $('#activeName').val(),
				imageUrl : $("#imageUrl").val(),
				activeUrl : $("#activeUrl").val(),
				startDate : $("#startDate").val(),
				endDate : $("#endDate").val(),
				activeChannel : $('#activeChannel').val(),
			};
			T.common.ajax.requestBlock('AddActiveAction', params, false, function()
			{
				alert("发布成功");
			});
		}
	};

	ActiveManager.init();
});
