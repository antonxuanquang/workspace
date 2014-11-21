define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./common');

	var reportmgr =
	{
		pageNo : 1, // 当前页码

		// 初始化
		init : function()
		{
			T.common.user.checkAdmin();
			reportmgr.getReportList();
		},

		// 读取报表列表
		getReportList : function()
		{
			var pageNo = reportmgr.pageNo;
			$('#listwrap').html('<div align="center"><img src="http://seanzwx.github.io/97igo/image/loading.gif"/></div>');

			// 读取报表列表
			var params =
			{
				pageNo : reportmgr.pageNo,
			};
			T.common.ajax.requestBlock("InquireReportListAction", params, false, function(jsonstr, data, code, msg)
			{
				var type = ["", "单值报表", "数值报表", "列表报表", "多值报表"];
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
					else if (btn == '删除')
					{
						if (confirm("确认要删除?"))
						{
							var p =
							{
								reportId : reportId,
							};
							T.common.ajax.requestBlock('DeleteReportAction', p, false, function()
							{
								location = location;
							});
						}
					}
					else if (btn == "授权")
					{
						// 读取用户列表
						T.common.ajax.requestBlock("InquireUserListAction", null, false, function(jsonstr, data, code, msg)
						{
							var tplData =
							{
								userList : data.userList
							};
							var tpl = $('#tpl_userList').html();
							var html = juicer(tpl, tplData);
							$('#userList_table').html(html);
							$('#userlist_modal').modal();

							// 读取访问权限列表
							T.common.ajax.requestBlock("InquireAclListAction",
							{
								reportId : reportId
							}, false, function(jsonstr, data, code, msg)
							{
								for (var i = 0; i < data.aclList.length; i++)
								{
									var it = data.aclList[i];
									$('#userList_table input[userId=' + it.userId + ']').prop('checked', true);
								}

								$('#auth_select_all').bind('click', function()
								{
									$('#auth_user_list input[type=checkbox]').prop('checked', $(this).prop('checked'));
								});

								$('#btn_save_auth').bind('click', function()
								{
									var input = $('#auth_user_list input[type=checkbox]');
									var userList = new Array();
									for (var i = 0; i < input.length; i++)
									{
										var ck = $(input[i]);
										if (ck.prop('checked'))
										{
											userList[userList.length] = $(input[i]).attr('userId');
										}
									}

									var p =
									{
										reportId : reportId,
										userList : userList,
									};
									T.common.ajax.requestBlock('UpdateAclListAction', p, false, function()
									{
										$('#userlist_modal').modal('hide');
									});
								});
							});
						});
					}
				});

				// 计算分页
				var totalrecords = data.totalrecord;
				var totalpages = totalrecords % 25 == 0? parseInt(totalrecords / 24): parseInt(totalrecords / 25) + 1;

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
					reportmgr.pageNo = pageNo;
					reportmgr.getReportList();
				});
			});
		},
	};

	var api =
	{
		init : reportmgr.init,
	};

	exports.reportmgr = api;
	T.reportmgr = api;
});
