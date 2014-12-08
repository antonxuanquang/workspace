define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./index');

	var profile =
	{
		// 初始化
		init : function()
		{
			T.index.initUI();

			profile.getProfile();
		},

		getProfile : function()
		{
			T.common.ajax.requestBlock("InquireProfileAction", null, false, function(jsonstr, data, code, msg)
			{
				data.userinfo.registTime = T.common.util.time.getYYYYMMDDHHMMSS(data.userinfo.registTime);
				for(var i = 0; i < data.orderList.length; i++)
				{
					var it = data.orderList[i];
					it.createTime = T.common.util.time.getYYYYMMDDHHMMSS(it.createTime);
					if(it.goodName.length > 20)
					{
						it.goodName = it.goodName.substring(0, 20);
					}
				}
				var tplData =
				{
					user : data.userinfo,
					orderList : data.orderList,
				};
				var tpl = $('#tpl_userinfo').html();
				var html = juicer(tpl, tplData);
				$('#userinfo').html(html);
				
				$('#updatePwd').bind('click', function()
				{
					var param = 
					{
						oldpassword : T.common.util.md5($('#oldpassword').val()),
						password : T.common.util.md5($('#newpassword').val()),
					};
					T.common.ajax.requestBlock("UpdatePasswordAction", param, false, function(jsonstr, data, code, msg)
					{
						alert("修改成功");
					});
				});
			});
		},
	};

	var api =
	{
		init : profile.init,
	};

	exports.profile = api;
	T.profile = api;
});
