define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./common');

	var create =
	{
		// 初始化
		init : function()
		{
			T.common.user.checkAdmin();
			$('#submit').bind('click', function()
			{
				var params =
				{
					reportName : $('#reportName').val(),
					xAxis : $('#xAxis').val(),
					yAxis : $('#yAxis').val(),
					conditions : $('#conditions').val(),
					type : $('#type').val(),
					countType : $('#countType').val(),
				};
				T.common.ajax.requestBlock("CreateReportAction", params, false, function(jsonstr, data, code, msg)
				{
					alert("创建成功");
				});
			});
		},
	};

	var api =
	{
		init : create.init,
	};

	exports.create = api;
	T.create = api;
});
