define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./common');

	var execute_3 =
	{
		report : null,

		init : function()
		{
			var reportId = T.common.util.getParameter("reportId");
			execute_3.getReport(reportId);
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
				execute_3.report = data.reportDetail;
				execute_3.showTimeSelect();
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

				execute_3.getExecuteList();
			});

			// 月统计
			if (execute_3.report.countType == 2)
			{
				$('#selectMonth').remove();
			}

			execute_3.getExecuteList();
		},

		getExecuteList : function()
		{
			$('#tbody').html('<div align="center"><img src="http://seanzwx.github.io/97igo/image/loading.gif"/></div>');
			var yearOrMonth = "";
			if (execute_3.report.countType == 1)
			{
				yearOrMonth = $('#selectYear').attr('time') + "" + $('#selectMonth').attr('time');
			}
			else if (execute_3.report.countType == 2)
			{
				yearOrMonth = $('#selectYear').attr('time');
			}

			// 读取报表列表
			var params2 =
			{
				reportId : execute_3.report.reportId,
				yearOrMonth : yearOrMonth,
			};
			T.common.ajax.requestBlock("InquireExecuteListAction", params2, false, function(jsonstr, data, code, msg)
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
					executeList : list
				};
				var tpl = $('#tpl_executeList').html();
				var html = juicer(tpl, tplData);
				$('#tbody').html(html);

				$('#tbody tr').bind('click', function()
				{
					var tr = $(this);
					tr.find('input').prop('checked', !(tr.find('input').prop('checked')));

					execute_3.showCondition();
				});
				execute_3.showCondition($($('#tbody tr').get(0)));
			});
		},

		showCondition : function(tr)
		{
			// 条件标签
			var conds = execute_3.report.conditions.split(";");
			var condAxis = new Array();
			for (var i = 0; i < conds.length; i++)
			{
				condAxis[condAxis.length] =
				{
					axis : conds[i],
				}
			}

			var results = new Array();
			var trs = $('#tbody tr');
			for (var i = 0; i < trs.length; i++)
			{
				var it = $(trs[i]);
				if (it.find('input').prop('checked'))
				{
					results[results.length] = JSON.parse(it.attr('result'));
				}
			}

			if (results.length > 0)
			{
				var condList = results[0];
				var columns = condList.length;
				for (var j = 0; j < columns; j++)
				{
					var value = new Array();
					for (var i = 0; i < results.length; i++)
					{
						value[value.length] = results[i][j].v;
					}
					condList[j].v = value;
				}

				var tplData =
				{
					condAxis : condAxis,
					condList : condList,
				};
				var tpl = $('#tpl_condList').html();
				var html = juicer(tpl, tplData);
				$('#bottom').html(html);

				$('#cond_tbody tr').bind('click', function()
				{
					var tr = $(this);
					tr.find('input').prop('checked', !(tr.find('input').prop('checked')));
					execute_3.showReport();
				});

				execute_3.showReport();
			}
		},

		showReport : function()
		{
			var x = new Array();
			var series = new Array();
			var trs = $('#tbody tr');
			for (var i = 0; i < trs.length; i++)
			{
				var it = $(trs[i]);
				if (it.find('input').prop('checked'))
				{
					x[x.length] = $(it.children().get(1)).html();
				}
			}

			trs = $('#cond_tbody tr');
			var ths = $('#thread th');
			for (var i = 0; i < trs.length; i++)
			{
				var it = $(trs[i]);
				if (it.find('input').prop('checked'))
				{
					var tds = it.find('td');
					var name = "";
					for (var j = 1; j < tds.length; j++)
					{
						name += $(ths[j]).html() + "=" + $(tds[j]).html() + ", ";
					}
					series[series.length] =
					{
						name : name,
						data : eval('[' + it.attr('result') + ']')
					}
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
					text : execute_3.report.reportName,
					x : -20 //center
				},
				subtitle :
				{
					text : 'Source: WorldClimate.com',
					x : -20
				},
				xAxis :
				{
					title :
					{
						text : execute_3.report.xAxis
					},
					categories : x
				},
				yAxis :
				{
					title :
					{
						text : execute_3.report.yAxis
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
				series : series,
			});
		},
	};

	var api =
	{
		init : execute_3.init,
	};

	exports.execute_3 = api;
	T.execute_3 = api;
});
