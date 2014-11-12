/**
 * 首页对象
 */
define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	// 依赖模块
	T.common = require('./func/common').common;
	T.im = require('./im').im;

	// 是否已经初始化
	var inited = false;

	// 调整界面
	function adjust()
	{
		$('#menu_scroll').css('height', $(document).height() - 70);
	};

	// 登录
	function login()
	{
		var tpl = $('#tpl_login').html();
		T.common.ui.pop.show(tpl);

		$('#login_btn').bind('click', function()
		{
			$(this).html("登  录...");
			$('#login_info').html("");
			var params =
			{
				username : $('#login_username').val(),
				password : $('#login_password').val(),
			};
			if (params.username == '' || params.username.length > 16)
			{
				$('#login_info').html("用户名错误");
				$(this).html("登  录");
				return;
			}
			T.common.ajax.requestBlock("v1/AdminLogin", params, true, function(jsonstr, data, code, msg)
			{
				if (data.loginrs == 1)
				{
					localStorage.setItem("sid", "true");
					location = location;
				}
				else
				{
					$('#login_info').html("帐号或密码错误");
					$('#login_btn').html("登  录");
				}
			});
		});
	}

	// 对外开放api
	var api =
	{
		count : 0,
		// 初始化
		init : function()
		{
			if (!inited)
			{
				inited = true;
				T.common.ui.mask.show();

				// 读取登录参数
				var sid = localStorage.getItem("sid");
				localStorage.clear();

				//未登录
				if (sid == null || sid == '')
				{
					login();
					return;
				}

				// 绑定事件
				$(window).resize(adjust);

				// 调整界面
				adjust();

				// 初始化各个模块
				T.im.init();
			}
		},

		// 准备完毕
		ready : function()
		{
			api.count++;
			if (api.count >= 1)
			{
				T.common.ui.mask.hide();
				$('#menu_scroll').show();
			}
		},
	};
	T.index = api;

	// 启动程序
	api.init();
});
