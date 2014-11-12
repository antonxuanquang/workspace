define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('../../script/common');
	require('./util');

	var SearchManager =
	{
		currPageNo : 1,

		init : function()
		{
			SearchManager.getBuildList();
		},

		getBuildList : function()
		{
			var pageNo = SearchManager.currPageNo;
			$('#buildList').html("正在读取数据...");
			var params =
			{
				pageNo : pageNo,
			};

			T.common.ajax.requestBlock("InquireBuildLogAction", params, false, function(jsonstr, data, code, msg)
			{
				var buildList = data.buildList;

				var tplData =
				{
					buildList : buildList
				};
				var tpl = $('#tpl_buildList').html();
				var html = juicer(tpl, tplData);
				$('#buildList').html(html);

				// 计算分页
				T.util.initPage(SearchManager.currPageNo, data.totalrecord, 24, function(pageNo)
				{
					SearchManager.currPageNo = pageNo;
					SearchManager.getBuildList();
				});

				// 添加按钮事件
				$('#buildList button').bind('click', function()
				{
					var btn = $(this);
					if (btn.html() == "删除")
					{
						var r = confirm("确定要删除该日志吗?")
						if (r == true)
						{
							var params =
							{
								buildId : btn.attr('buildId')
							};
							T.common.ajax.requestBlock('DeleteBuildLogAction', params, false, function()
							{
								alert("删除成功");
								SearchManager.getBuildList();
							});
						}
					}
				});
			});
		},
	};

	SearchManager.init();
});
