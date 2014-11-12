define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./common');

	var execute_1 =
	{
		report : null,

		init : function()
		{
			var reportId = T.common.util.getParameter("reportId");
			execute_1.getReport(reportId);
		},

		getReport : function(reportId)
		{
			// 读取报表元数据
			var params =
			{
				reportId : reportId,
			};
			T.common.ajax.requestBlock("InquireReportAction", params, false, function(jsonstr, data, code, msg)
			{
				execute_1.report = data.reportDetail;
				execute_1.showTimeSelect();
			});
		},

		showTimeSelect : function()
		{
			// 计算日期
			var years = new Array();
			years[0] = new Date().getYear() + 1900;
			for (var i = 1; i < 5; i++)
			{
				years[i] = years[0] - i;
			}

			var tplData =
			{
				months : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
				years : years,
				thisYear : years[0],
				thisMonth : new Date().getMonth() + 1,
			};
			var tpl = $('#tpl_timeSelect').html();
			var html = juicer(tpl, tplData);
			$('#right').html(html);

			$('.btn-group li').bind('click', function()
			{
				var li = $(this);
				var button = li.parent().parent().find('button');
				button.html($(li.children().get(0)).html() + ' <span class="caret"></span>');
				button.attr('time', li.attr('value'));

				execute_1.getExecuteList();
			});

			// 月统计
			if (execute_1.report.countType == 2)
			{
				$('#selectMonth').remove();
			}

			execute_1.getExecuteList();
		},

		getExecuteList : function()
		{
			$('#tbody').html('<div align="center"><img src="http://seanzwx.github.io/97igo/image/loading.gif"/></div>');
			var yearOrMonth = "";
			if (execute_1.report.countType == 1)
			{
				yearOrMonth = $('#selectYear').attr('time') + "" + $('#selectMonth').attr('time');
			}
			else if (execute_1.report.countType == 2)
			{
				yearOrMonth = $('#selectYear').attr('time');
			}

			// 读取报表列表
			var params =
			{
				reportId : execute_1.report.reportId,
				yearOrMonth : yearOrMonth,
			};
			T.common.ajax.requestBlock("InquireExecuteListAction", params, false, function(jsonstr, data, code, msg)
			{
				var list = new Array();
				for (var i = 0; i < data.executeList.length; i++)
				{
					var it = data.executeList[i];
					list[list.length] =
					{
						executeTime : T.common.util.time.getYYYYMMDD(it.executeTime),
						success : 1,
						result : it.result,
					};
				}
				var tplData =
				{
					executeList : list,
				};
				var tpl = $('#tpl_executeList').html();
				var html = juicer(tpl, tplData);
				$('#tbody').html(html);

				$('#tbody tr').bind('click', function()
				{
					var tr = $(this);
					tr.find('input').prop('checked', !(tr.find('input').prop('checked')));

					execute_1.showReport();
				});

				execute_1.showReport();
			});
		},

		showReport : function()
		{
			var x = new Array();
			var y = new Array();
			var trs = $('#tbody tr');
			for (var i = 0; i < trs.length; i++)
			{
				var it = $(trs[i]);
				if (it.find('input').prop('checked'))
				{
					x[x.length] = $(it.children().get(1)).html();
					y[y.length] = parseFloat(it.attr('result'));
				}
			}

			$('#report').highcharts(
			{
				chart :
				{
					type : 'line'
				},
				title :
				{
					text : execute_1.report.reportName,
					x : -20 //center
				},
				subtitle :
				{
					text : 'Source: WorldClimate.com',
					x : -20
				},
				xAxis :
				{
					categories : x,
					title :
					{
						text : execute_1.report.xAxis
					},
				},
				yAxis :
				{
					title :
					{
						text : execute_1.report.yAxis
					},
					plotLines : [
					{
						value : 0,
						width : 1,
						color : '#808080'
					}]
				},
				legend :
				{
					layout : 'vertical',
					align : 'right',
					verticalAlign : 'middle',
					borderWidth : 0
				},
				plotOptions :
				{
					line :
					{
						dataLabels :
						{
							enabled : true
						},
					}
				},
				series : [
				{
					name : '日期曲线',
					data : y
				}]
			});
		},
	};

	var api =
	{
		init : execute_1.init,
	};

	exports.execute_1 = api;
	T.execute_1 = api;
});
