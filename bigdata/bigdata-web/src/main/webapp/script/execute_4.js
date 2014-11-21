define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./common');

	var execute_4 =
	{
		report : null,

		init : function()
		{
			var reportId = T.common.util.getParameter("reportId");
			execute_4.getReport(reportId);
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
				execute_4.report = data.reportDetail;
				execute_4.showTimeSelect();
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

				execute_4.getExecuteList();
			});

			// 月统计
			if (execute_4.report.countType == 2)
			{
				$('#selectMonth').remove();
			}

			execute_4.getExecuteList();
		},

		getExecuteList : function()
		{
			$('#tbody').html('<div align="center"><img src="http://seanzwx.github.io/97igo/image/loading.gif"/></div>');
			var yearOrMonth = "";
			if (execute_4.report.countType == 1)
			{
				yearOrMonth = $('#selectYear').attr('time') + "" + $('#selectMonth').attr('time');
			}
			else if (execute_4.report.countType == 2)
			{
				yearOrMonth = $('#selectYear').attr('time');
			}

			// 读取报表列表
			var params2 =
			{
				reportId : execute_4.report.reportId,
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

					execute_4.showTable();
				});
				execute_4.showTable($($('#tbody tr').get(0)));
			});
		},

		showTable : function(tr)
		{
			$('#bottom').html('');

			// 条件标签
			var columns = execute_4.report.columnTags.split(";");
			var columnTags = new Array();
			for (var i = 0; i < columns.length; i++)
			{
				columnTags[columnTags.length] =
				{
					tag : columns[i],
				}
			}

			var results = new Array();
			var trs = $('#tbody tr');
			for (var i = 0; i < trs.length; i++)
			{
				var it = $(trs[i]);
				if (it.find('input').prop('checked'))
				{
					results[results.length] =
					{
						value : JSON.parse(it.attr('result')),
						executeTime : it.attr('executeTime')
					};
				}
			}

			if (results.length > 0)
			{
				var columnList = new Array();
				for (var j = 0; j < results.length; j++)
				{
					columnList[j] = results[j];
				}

				var tplData =
				{
					columnTags : columnTags,
					columnList : columnList,
				};
				var tpl = $('#tpl_columnList').html();
				var html = juicer(tpl, tplData);
				$('#bottom').html(html);

				$('#thread input[type=radio]').bind('click', function()
				{
					var radio = $(this);
					var index = parseInt(radio.attr('index'));
					execute_4.showReport(index);
				});
				execute_4.showReport(0);
			}
		},

		showReport : function(index)
		{
			var x = new Array();
			var series = new Array();
			var trs = $('#column_tbody tr');
			for (var i = 0; i < trs.length; i++)
			{
				var tds = $(trs[i]).children();
				x[i] = $(tds[0]).html();
				series[i] = parseFloat($(tds[index + 1]).html());
			}

			$('#report').highcharts(
			{
				chart :
				{
					type : 'line'
				},
				title :
				{
					text : execute_4.report.reportName,
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
						text : '日期'
					},
					categories : x
				},
				yAxis :
				{
					title :
					{
						text : ''
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
					name : $('#thread label[for=column_' + (index) + ']').html(),
					data : series
				}],
			});
		},
	};

	var api =
	{
		init : execute_4.init,
	};

	exports.execute_4 = api;
	T.execute_4 = api;
});
