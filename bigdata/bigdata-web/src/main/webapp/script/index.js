define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./common');

	var index =
	{
		pageNo : 1, // 当前页码

		// 初始化
		init : function()
		{
			T.common.user.checkLogin();
			index.getReportList();
		},

		// 读取报表列表
		getReportList : function()
		{
			var pageNo = index.pageNo;
			$('#listwrap').html('<div align="center"><img src="http://seanzwx.github.io/97igo/image/loading.gif"/></div>');

			// 读取报表列表
			var params =
			{
				pageNo : index.pageNo,
			};
			T.common.ajax.requestBlock("InquireMyReportListAction", params, false, function(jsonstr, data, code, msg)
			{
				var type = ["", "单值报表", "数值报表", "列表报表"];
				var countType = ["", "日统计", "月统计"];
				for (var i = 0; i < data.reportList.length; i++)
				{
					var it = data.reportList[i];
					it.typeStr = type[it.type];
					it.countType = countType[it.countType];
					it.createTime = T.common.util.time.getYYYYMMDDHHMMSS(it.createTime);
				}
				var tplData =
				{
					reportList : data.reportList
				};
				var tpl = $('#tpl_reportList').html();
				var html = juicer(tpl, tplData);
				$('#listwrap').html(html);

				$('#listwrap button').bind('click', function()
				{
					var reportId = $(this).attr("reportId");
					var btn = $(this).html();
					if (btn == "查看")
					{
						var type = $(this).attr('reportType');
						window.open("./execute_" + type + ".html?reportId=" + reportId);
					}
				});

				// 计算分页
				var totalrecords = data.totalrecord;
				var totalpages = totalrecords % 25 == 0? parseInt(totalrecords / 25): parseInt(totalrecords / 25) + 1;

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
					index.pageNo = pageNo;
					index.getReportList();
				});
			});
		},
	};

	var api =
	{
		init : index.init,
	};

	exports.index = api;
	T.index = api;
});
