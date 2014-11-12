/**
 * im模块
 */
define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	// 依赖模块
	T.common = require('./func/common').common;

	var im =
	{
		// 初始化
		init : function()
		{
			$('#index_userlist').bind('click', im.showUserList);
			$('#index_ad_chatform').bind('click', im.showChatformAd);
			$('#index_icon').bind('click', im.showIcon);
			T.index.ready();
		},

		// 属性查询
		showUserList : function()
		{
			var tpl = T.common.ui.getTemplate("im", 'tpl_im_userlist');
			T.common.ui.open(tpl);

			var heightOffset = T.common.util.browser.isIE8()? 165: 160;
			$('#im_userlist_scroll').css('height', $(document).height() - heightOffset);
			T.common.ui.scroll.init('im_userlist_scroll');

			T.common.util.page.init(function(pageNo)
			{
				im.getUserListData(pageNo, 20);
			});
			im.getUserListData(1, 20);
		},

		getUserListData : function(pageNo)
		{
			var params =
			{
				pageNo : pageNo
			};

			T.common.ajax.requestBlock('v1/InquireServerInfo', params, false, function(jsonstr, data, code, msg)
			{
				var userList = new Array();
				for (var i = 0; i < data.userlist.length; i++)
				{
					var it = data.userlist[i];
					userList[i] =
					{
						userId : it.id,
						username : it.username,
						nickname : it.nickname,
						age : it.age,
						tel : it.tel,
						mail : it.mail,
						status : it.status == 1? '在线': '离线',
					};
				}

				var tplData =
				{
					userList : userList
				};
				var tpl = T.common.ui.getTemplate("im", 'tpl_im_userlist_listitem');
				var html = juicer(tpl, tplData);
				$('#im_userlist_list').html(html);

				$('#im_userlist_list span[class=list_btn]').bind('click', function()
				{
					var span = $(this);
					if (span.attr('type') == 'warn')
					{
						var userId = span.attr('userId');
						var tpl = T.common.ui.getTemplate('im', 'tpl_index_warn');
						T.common.ui.pop.show(tpl);

						$('#index_warn_submit').bind('click', function()
						{
							var content = $('#index_warn_content').val();
							var params =
							{
								receiverId : userId,
								content : content,
							};
							T.common.ajax.requestBlock("v1/SendWarningMsg", params, false, function(jsonstr, data, code, msg)
							{
								T.common.ui.toast("发送成功");
								T.common.ui.pop.close();
							});
						});
					}
					else if (span.attr('type') == 'kick')
					{
						var userId = span.attr('userId');
						var params =
						{
							userId : userId,
						};
						T.common.ajax.requestBlock("v1/ExitClient", params, false, function(jsonstr, data, code, msg)
						{
							T.common.ui.toast("踢出成功");
							im.getUserListData(1);
						});
					}
					else if (span.attr('type') == 'delete')
					{
						if (T.common.ui.confirm("确定要删除吗?", function()
							{
								var userId = span.attr('userId');
								var params =
								{
									userId : userId,
								};
								T.common.ajax.requestBlock("v1/DeleteUser", params, false, function(jsonstr, data, code, msg)
								{
									T.common.ui.pop.close();
									T.common.ui.toast("删除成功");
									im.getUserListData(1);
								});
							}))
							;
					}
				});

				T.common.util.page.show('im_userlist_page', pageNo, 20, data.totalrecords);
				$('#onlinecount').html("在线人数:" + data.onlineCount);
			});
		},

		showChatformAd : function()
		{
			var tpl = T.common.ui.getTemplate("im", 'tpl_ad_chatform');
			T.common.ui.open(tpl);
			$('#save_chatform_ad').bind('click', function()
			{
				var params =
				{
					adChatformLink : $('#adChatformLink').val(),
					adChatformImgUrl : $('#adChatformImgUrl').val(),
				};

				T.common.ajax.requestBlock('v1/UpdateAdChatform', params, false, function()
				{
					T.common.ui.toast("修改成功");
				});
			});
		},

		showIcon : function()
		{
			var tplData =
			{
				icons : new Array()
			};
			tplData.icons[0] =
			{
				iconId : 1,
				imgUrl : '邮件图标地址',
				imgUrlId : 'mail_imgUrlId',
				url : '邮件点击跳转',
				urlId : 'mail_urlId',
				visibleId : 'mail_visibleId',
			};
			tplData.icons[1] =
			{
				iconId : 2,
				imgUrl : '空间图标地址',
				imgUrlId : 'zone_imgUrlId',
				url : '空间点击跳转',
				urlId : 'zone_urlId',
				visibleId : 'zone_visibleId',
			};

			var tpl = T.common.ui.getTemplate("im", 'tpl_icon');
			var html = juicer(tpl, tplData);
			T.common.ui.open(html);

			$('#icon_table span[class=button]').bind('click', function()
			{
				var params =
				{
					iconId : $(this).attr('iconId'),
					iconImgUrl : $('#' + $(this).attr('imgUrlId')).val(),
					iconUrl : $('#' + $(this).attr('urlId')).val(),
					iconVisible : $('#' + $(this).attr('visibleId')).attr('checked') == 'checked'? '1': '0' ,
				};

				T.common.ajax.requestBlock("v1/UpdateIcon", params, false, function(jsonstr, data, code, msg)
				{
					T.common.ui.toast("修改成功");
				});
			});
		},

		// 外部接口
		api :
		{
		},
	};

	var inited = false;
	var api =
	{
		init : function()
		{
			if (!inited)
			{
				inited = true;
				im.init();
			}
		},

		im : im.api,
	};

	exports.im = api;
	T.im = api;
});
