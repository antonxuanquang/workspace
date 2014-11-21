define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./common');

	var usermgr =
	{
		// 初始化
		init : function()
		{
			T.common.user.checkAdmin();
			usermgr.getUserList();
		},

		// 读取报表列表
		getUserList : function()
		{
			$('#listwrap').html('<div align="center"><img src="http://seanzwx.github.io/97igo/image/loading.gif"/></div>');

			// 读取报表列表
			T.common.ajax.requestBlock("InquireUserListAction", null, false, function(jsonstr, data, code, msg)
			{
				var role = ["", "管理员", "普通用户"];
				for (var i = 0; i < data.userList.length; i++)
				{
					var it = data.userList[i];
					it.roleStr = role[it.role];
				}
				var tplData =
				{
					userList : data.userList
				};
				var tpl = $('#tpl_userList').html();
				var html = juicer(tpl, tplData);
				$('#listwrap').html(html);
			});
		},
	};

	var api =
	{
		init : usermgr.init,
	};

	exports.usermgr = api;
	T.usermgr = api;
});
