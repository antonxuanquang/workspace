define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	// 依赖模块
	require('../base/uploadify/jquery.uploadify');

	var common =
	{
		/**
		 * 全局配置
		 */
		config :
		{
			// 请求地址
			// requestUrl : ["http://localhost:8080/im-web/action/"],
			requestUrl : ["action/"],
			// 文件上传地址
			uploadUrl : "http://localhost:8080/filecenter/UploadFileServlet",
			// 下载文件地址
			downloadUrl : "http://localhost:8081/",

			// 用户开放接口
			api :
			{
				getRequestUrl : function(index)
				{
					return common.config.requestUrl[index];
				},

				getUploadUrl : function()
				{
					return common.config.uploadUrl;
				},

				getDownloadUrl : function()
				{
					return common.config.downloadUrl;
				},
			},
		},

		/**
		 * 全局用户
		 */
		user :
		{
			_sid : null,
			_encryptKey : null,
			_isAdmin : false,
			_userId : 0,

			// 用户开放接口
			api :
			{
				// 设置用户登录信息
				setUserInfo : function(sid, encryptKey, isAdmin, userId)
				{
					if (common.user._sid == null)
					{
						common.user._sid = sid;
						common.user._encryptKey = encryptKey;
						common.user._isAdmin = isAdmin;
						common.user._userId = userId;
					}
				},

				// 读取sid
				getSid : function()
				{
					return common.user._sid;
				},

				// 读取私钥
				getEncryptKey : function()
				{
					return common.user._encryptKey;
				},

				// 登录用户是否为管理员
				isAdmin : function()
				{
					return common.user._isAdmin;
				},

				// 读取用户ID
				getUserId : function()
				{
					return common.user._userId;
				},
			},
		},

		/**
		 * 全局UI
		 */
		ui :
		{
			// 获取模板
			getTemplate : function(module, template)
			{
				var tpl = $('#frame_' + module).contents().find("#" + template).html();
				return tpl;
			},

			// 打开中间内容
			open : function(html)
			{
				// 清空垃圾桶
				$('#garbage').html('');
				var content = $('#center_content');
				content.html(html);
			},

			// 前进
			forward : function(module, template)
			{
				// 清空垃圾桶
				$('#garbage').html('');
				var tpl = common.ui.getTemplate(module, template);
				// 隐藏最后一个子页面
				var content = $('#center_content');
				content.children('table:last-child').hide();
				content.append(tpl);
			},

			// 前进
			forwardHtml : function(html)
			{
				// 清空垃圾桶
				$('#garbage').html('');
				// 隐藏最后一个子页面
				var content = $('#center_content');
				content.children('table:last-child').hide();
				content.append(html);
			},

			// 返回
			goback : function()
			{
				var content = $('#center_content');
				// 删除最后一个子页面
				content.children('table:last-child').remove();
				content.children('table:last-child').show();
			},

			// 单选按钮组
			radioGroup :
			{
				// 初始化
				init : function(groupId)
				{
					$('#' + groupId + ">div").bind('click', function()
					{
						$('#' + groupId + ">div").attr('class', 'radio_item');
						$(this).prop('class', 'radio_item_select');
					});
				},

				// 获取单选按钮值
				getValue : function(groupId)
				{
					var val = $('#' + groupId + ">div[class=radio_item_select]").attr('val');
					return val;
				},
			},

			// 下拉框
			select :
			{
				// 初始化
				init : function(selectId, array)
				{
					var tpl = common.ui.getTemplate("index", "tpl_index_select");
					var data =
					{
						id : selectId,
						items : array,
						def : array[0],
					};
					var html = juicer(tpl, data);
					$('#' + selectId).append(html);

					$('#' + selectId + " table[class=select]").bind('click', function()
					{
						var select = $(this);
						var body = $('#' + selectId + '_select_body');

						var mask = $('<div id="mask" class="mask">&nbsp;</div>');
						mask.css('opacity', 0).css('filter', "alpha(opacity=0)");
						$('#garbage').append(mask);
						mask.show();
						mask.bind('click', function()
						{
							$(this).remove();
							body.fadeOut(200);
						});

						if (body.css('z-index') != 100)
						{
							body.css('left', select.offset().left);
							body.css('top', select.offset().top + 31);
							body.css('width', select.css('width'));
							body.css('z-index', 100);
							body.css('position', 'absolute');
							$('#garbage').append(body);
						}
						body.fadeIn(200, function()
						{
							body.find("div[class=selectitem]").bind('click', function()
							{
								var item = $(this);
								var body = $('#' + selectId + '_select_body');
								var select = $('#' + selectId + "_select");

								select.attr('value', item.attr('val')).html(item.html());
								$('#mask').remove();
								body.fadeOut(200);
							});
						});
						common.ui.scroll.init(selectId + "_select_scroll");
					});
				},

				// 读取值
				getValue : function(selectId)
				{
					var select = $('#' + selectId + "_select");
					return select.attr('value');
				},

				// 设置值
				setValue : function(selectId, val)
				{
					var select = $('#' + selectId + "_select");
					var items = $('#' + selectId + '_select_scroll>div');
					var it;
					for (var i = 0; i < items.length; i++)
					{
						it = $(items[i]);
						if (it.attr('val') == val)
						{
							select.attr('value', it.attr('val')).html(it.html());
							break;
						}
					}
				}
			},

			// 滚动条
			scroll :
			{
				init : function(target)
				{
					if ($('#' + target + '_scroll_body').length == 0)
					{
						var scroll = $('#' + target);
						scroll.css('overflow', 'hidden');

						var tpl = common.ui.getTemplate('index', 'tpl_index_scroll');
						var data =
						{
							target : target
						};
						var html = juicer(tpl, data);
						var content = scroll.html();
						scroll.html(html);
						$('#' + target + '_scroll_body').html(content);

						scroll.bind('mousewheel', function(event, delta)
						{
							var scroll = $(this);
							var scrollHeight = parseInt(scroll.css('height').replace("px", ""));
							var body = $('#' + scroll.attr('id') + '_scroll_body');
							var bodyHeight = parseInt(body.css('height').replace("px", ""));
							var scrollbar = $('#' + scroll.attr('id') + '_scrollbar');

							if (bodyHeight > scrollHeight)
							{
								var margin = parseInt(body.css('margin-top').replace("px", ""));
								// 向上
								if (delta > 0)
								{
									margin += 50;
									if (margin > 0)
									{
										margin = 0;
									}
								}
								// 向下
								else
								{
									margin += -50;
									var height = parseInt(body.css('height').replace("px", ""));
									if ((height + margin) <= scrollHeight)
									{
										margin = scrollHeight - bodyHeight;
									}
								}
								$('#' + target + '_scrollbar').parent().css('padding-top', margin * -1 / 10);
								body.css('margin-top', margin);
							}
						}).bind('mouseenter', function()
						{
							var scroll = $(this);
							var scrollHeight = parseInt(scroll.css('height').replace("px", ""));
							var body = $('#' + scroll.attr('id') + '_scroll_body');
							var bodyHeight = parseInt(body.css('height').replace("px", ""));
							var scrollbar = $('#' + scroll.attr('id') + '_scrollbar');

							if (bodyHeight > scrollHeight)
							{
								var offset = bodyHeight - scrollHeight;
								scrollbar.css('height', scrollHeight - offset / 10);
								$('#' + target + '_scrollbar_wrap').fadeIn(200);
							}
							else
							{
								body.css('margin-top', 0);
							}
						}).bind('mouseleave', function()
						{
							$('#' + target + '_scrollbar_wrap').fadeOut(200);
						});
					}
				},
			},

			// 弹窗
			pop :
			{
				// 显示
				show : function(html)
				{
					var pop = $('#pop');
					pop.html(html);
					$.fancybox.open(
					{
						href : '#pop',
						padding : 1,
						openEffect : 'none',
						closeEffect : 'none'
					});
				},

				// 关闭
				close : function()
				{
					$.fancybox.close();
				},
			},

			// 提示信息
			toast : function(msg)
			{
				var body = $('body');

				var data =
				{
					msg : msg
				};
				var tpl = common.ui.getTemplate("index", "tpl_index_toast");
				var html = $(juicer(tpl, data));
				html.css('top', ($(document).height() - 80) / 2);
				html.css('left', ($(document).width() - 400) / 2);

				body.append(html);
				html.fadeIn(300);

				setTimeout(function()
				{
					html.fadeOut(300, function()
					{
						html.remove();
					});
				}, 1500);
			},

			// 遮罩
			mask :
			{
				show : function()
				{
					var mask = $('<div id="mask" class="mask">&nbsp;</div>');
					$('body').append(mask);
					if (common.util.browser.isIE8())
					{
						mask.show();
					}
					else
					{
						mask.fadeIn(300);
					}

					var load = $(common.ui.getTemplate("index", "tpl_index_wait"));
					load.css('top', ($(document).height() - 100) / 2);
					load.css('left', ($(document).width() - 300) / 2);
					$('body').append(load);
					load.fadeIn(300);
				},

				hide : function()
				{
					var mask = $('#mask');
					mask.attr('id', common.util.time.getTime() + "mask");
					mask.fadeOut(300, function()
					{
						mask.remove();
					});
					var load = $('#load');
					load.attr('id', common.util.time.getTime() + "load");
					load.fadeOut(300, function()
					{
						load.remove();
					});
				},
			},

			// 确认信息
			confirm : function(msg, callback)
			{
				var tpl = common.ui.getTemplate("index", "tpl_index_confirm");
				var data =
				{
					msg : msg
				};
				var html = juicer(tpl, data);
				T.common.ui.pop.show(html);
				$('#index_confirm_ok').bind('click', callback);
				$('#index_confirm_cancel').bind('click', function()
				{
					T.common.ui.pop.close();
				});
			},
		},

		/**
		 * 网络请求模块
		 */
		ajax :
		{
			ajaxobj : null,

			// 获取完整参数
			_getFullParameter : function(params)
			{
				// var sid = common.user.api.getSid();
				// if (sid == null || sid == "")
				// {
				// return params;
				// }
				//
				// var seed = Math.random() * 10000 + 1;
				// params.sid = sid;
				// params.seed = seed;
				// params.sbf = common.util.md5(sid + seed + common.user.api.getEncryptKey());
				return params;
			},

			// ajax请求
			_request : function(block, serverIndex, action, params, care, callback)
			{
				if (params != null)
				{
					params = common.ajax._getFullParameter(params);
				}
				else
				{
					params = common.ajax._getFullParameter(
					{
					});
				}
				var parameter = "";
				for (var p in params)
				{
					if ( typeof (params[p]) != "function" && typeof (params[p]) != "Object")
					{
						if (params[p] instanceof Array)
						{
							for (var i = 0; i < params[p].length; i++)
							{
								parameter += p + "=" + params[p][i] + "&";
							}
						}
						else
						{
							if (params[p] != '')
							{
								parameter += p + "=" + params[p] + "&";
							}
						}
					}
				}

				if (parameter.charAt(parameter.length - 1) == '&')
				{
					parameter.substr(0, parameter.length - 1);
				}

				if (block)
				{
					common.ui.mask.show();
				}

				ajaxobj = $.ajax(
				{
					url : common.config.api.getRequestUrl(serverIndex) + action + "Action",

					// type : 'post',
					// dataType : 'text',

					type : 'get',
					dataType : 'jsonp',
					jsonp : "callback",

					cache : 'false',
					data : parameter,
					success : function(jsonstr)
					{
						if (block)
						{
							common.ui.mask.hide();
						}
						if (callback != null)
						{
							try
							{
								var jsonobj = JSON.parse(jsonstr);
								var state = jsonobj.state;
								var code = jsonobj.code;
								var msg = jsonobj.msg
								var data = jsonobj.data;

								// 成功
								if (state == "Success")
								{
									callback(jsonstr, data, 0, '操作成功');
								}
								// 业务异常
								else if (state == "BusinessException")
								{
									// 如果关心业务异常
									if (care)
									{
										callback(jsonstr, data, code, msg);
									}
									else
									{
										common.ui.toast(msg);
									}
								}
								// 其他异常
								else
								{
									common.ui.toast(msg);
								}
							}
							catch (error)
							{
								common.ui.toast(action + "-" + error.message);
							}
						}
					},
					error : function(jqXHR, textStatus, errorThrown)
					{
						alert(textStatus);
						if (block)
						{
							common.ui.mask.hide();
						}
						common.ui.toast("网络请求失败，请检查网络:" + errorThrown + "--" + textStatus);
					},
				});
			},

			// 网络请求公开api
			api :
			{
				request : function(action, params, care, callback)
				{
					common.ajax._request(false, 0, action, params, care, callback);
				},
				requestBlock : function(action, params, care, callback)
				{
					common.ajax._request(true, 0, action, params, care, callback);
				},
				requestIndex : function(serverIndex, action, params, care, callback)
				{
					common.ajax._request(false, serverIndex, action, params, care, callback);
				},
				requestIndexBlock : function(serverIndex, action, params, care, callback)
				{
					common.ajax._request(true, serverIndex, action, params, care, callback);
				},
			},
		},

		/**
		 * 文件上传模块
		 */
		uploader :
		{
			_target : null,
			init : function(upload, target)
			{
				common.uploader._target = target;

				var data =
				{
					target : target
				};
				var tpl = common.ui.getTemplate("index", "tpl_index_upload");
				var html = juicer(tpl, data);
				$('#' + target).append(html);

				$('#' + upload).uploadify(
				{
					swf : 'css/uploadify/uploadify.swf',
					uploader : common.config.api.getUploadUrl(),
					queueID : target + "_queue",
					multi : false,
					buttonText : '选择文件',
					fileSizeLimit : '1024KB',
					fileTypeExts : '*.doc; *.docx; *.xls; *.txt; *.jpg; *.png; *.jpeg; *.pdf; *.ppt',
					fileObjName : 'file',
					formData :
					{
						sid : common.user.api.getSid()
					},
					onSelect : function(file)
					{
						this.addPostParam("filename", file.name);
					},
					onUploadSuccess : function(file, data, response)
					{
						data = eval('(' + data + ')');
						var tpl = common.ui.getTemplate("index", "tpl_index_attach");
						var tplData =
						{
							filename : file.name,
							url : data.data.url,
						};
						var html = juicer(tpl, tplData);
						$('#' + common.uploader._target + "_files").append(html);
					},
					onUploadError : function(file, errorCode, errorMsg, errorString)
					{
						alert('文件[' + file.name + ']上传失败: ' + errorString);
					}
				});
			},

			// 读取文件
			getFiles : function()
			{
				if (common.uploader._target != null)
				{
					var files = $('#' + common.uploader._target + "_files>div[class=attach]");
					var attach = "";
					for (var i = 0; i < files.length; i++)
					{
						attach += $(files[i]).attr('url') + ",";
					}
					if (attach != "")
					{
						attach = attach.substring(0, attach.length - 1);
					}
					return attach;
				}
				return "";
			},
		},

		/**
		 * 工具类
		 */
		util :
		{
			// 判断是否为空
			isEmpty : function(val)
			{
				return val == null || val == "";
			},

			// 浏览器类型
			browser :
			{
				// 如果是IE8一下
				isIE8Low : function()
				{
				},

				isIE8 : function()
				{
					if ($.browser.msie && $.browser.version == "8.0")
					{
						return true;
					}
					return false;
				},
			},

			// md5加密
			md5 : function(txt)
			{
				return hex_md5(txt);
			},

			// 获取url参数
			getParameter : function(name)
			{
				// 如果链接没有参数，或者链接中不存在我们要获取的参数，直接返回空
				if (location.href.indexOf("?") == -1 || location.href.indexOf(name + '=') == -1)
				{
					return null;
				}

				// 获取链接中参数部分
				var queryString = decodeURI(location.href.substring(location.href.indexOf("?") + 1));

				// 分离参数对 ?key=value&key2=value2
				var parameters = queryString.split("&");

				var pos, paraName, paraValue;
				for (var i = 0; i < parameters.length; i++)
				{
					// 获取等号位置
					pos = parameters[i].indexOf('=');
					if (pos == -1)
					{
						continue;
					}

					// 获取name 和 value
					paraName = parameters[i].substring(0, pos);
					paraValue = parameters[i].substring(pos + 1);

					// 如果查询的name等于当前name，就返回当前值，同时，将链接中的+号还原成空格
					if (paraName == name)
					{
						return unescape(paraValue.replace(/\+/g, " "));
					}
				}
				return null;
			},

			// 时间工具
			time :
			{
				// 获取时间戳
				getTime : function()
				{
					var date = new Date();
					var time = date.getTime();
					date = null;
					return time;
				},

				// 获取日期
				getYYYYMMDD : function(date)
				{
					var tmp = date + "";
					return tmp.substr(0, 4) + "-" + tmp.substr(4, 2) + "-" + tmp.substr(6, 2);
				},

				/**
				 * 获取时间
				 * yyyyMMddHHmmss转yyyy-MM-dd HH:mm:ss
				 */
				getYYYYMMDDHHMMSS : function(date)
				{
					var tmp = date + "";
					return tmp.substr(0, 4) + "-" + tmp.substr(4, 2) + "-" + tmp.substr(6, 2) + " " + tmp.substr(8, 2) + ":" + tmp.substr(10, 2) + ":" + tmp.substr(12, 2);
				},

				/**
				 * 获取时间
				 * yyyy-MM-dd HH:mm:ss转yyyyMMddHHmmss
				 */
				fromYYYYMMDDHHMMSS : function(date)
				{
					var str = date.substr(0, 4);
					str += date.substr(5, 2);
					str += date.substr(8, 2);
					str += date.substr(11, 2);
					str += date.substr(14, 2);
					str += date.substr(17, 2);
					return str;
				}
			},

			// 数组工具
			array :
			{
				// 删除指定下表的数组元素
				removeAt : function(arr, index)
				{
					if (isNaN(index) && index < arr.length)
					{
						delete arr[index];
						for (var i = index; i < arr.length - 1; i++)
						{
							arr[i] = arr[i + 1];
						}
						arr.length -= 1;
					}
				},

				// 删除指定引用的数组元素
				removeItem : function(arr, item)
				{
					for (var i = 0; i < arr.length; i++)
					{
						if (item == arr[i])
						{
							delete arr[i];
							for (var j = i; j < arr.length - 1; j++)
							{
								arr[j] = arr[j + 1];
							}
							arr.length -= 1;
							break;
						}
					}
				}
			},

			// 分页工具
			page :
			{
				_callback : null,
				_totalPages : 0,

				// 初始化
				init : function(callback)
				{
					_callback = callback;
				},

				// 显示
				show : function(target, pageNo, pageSize, totalrecord)
				{
					var pageInfo =
					{
						pageNo : parseInt(pageNo),
						totalPages : totalrecord % pageSize == 0? totalrecord / pageSize: parseInt(totalrecord / pageSize) + 1,
						pages : new Array(),
						previous : pageNo - 1,
						next : parseInt(pageNo) + 1,
					};
					_totalPages = pageInfo.totalPages;

					if (pageNo <= 3)
					{
						for (var i = 1; i <= 5; i++)
						{
							if (i > pageInfo.totalPages)
							{
								break;
							}
							pageInfo.pages[i - 1] =
							{
								pageNo : i
							};
						}
					}
					else
					{
						var tmp = pageNo - 2;
						for (var i = 1; i <= 5; i++)
						{
							if (tmp > pageInfo.totalPages)
							{
								break;
							}
							pageInfo.pages[i - 1] =
							{
								pageNo : tmp
							};
							tmp++;
						}
					}

					var tpl = common.ui.getTemplate("index", "tpl_index_page");
					var html = juicer(tpl, pageInfo);
					$('#' + target).html(html);

					// 绑定事件
					$('#' + target + ">span[class=page_btn]").bind('click', function()
					{
						var pageNo = $(this).attr('page');
						_callback(pageNo);
					});

					$('#' + target + ">span[page=" + pageNo + "]").unbind('click');
					if (pageNo == 1)
					{
						$('#' + target + ">span[page=0]").unbind('click');
					}
					if (pageNo == pageInfo.totalPages)
					{
						$('#' + target + ">span[page=" + (parseInt(pageNo) + 1) + "]").unbind('click');
					}

					$('#index_page_go').unbind('click');
					$('#index_page_totalPages').unbind('click');

					$('#index_page_go').bind('click', function()
					{
						var pageNo = $('#index_page_pageNo').val();
						if (!isNaN(pageNo))
						{
							pageNo = parseInt(pageNo);
							if (pageNo > 0 && pageNo <= _totalPages)
							{
								_callback(pageNo);
							}
						}
					});
				}
			},
		},
	};

	var api = new Object();
	api.config = common.config.api;
	api.user = common.user.api;
	api.ajax = common.ajax.api;
	api.util = common.util;
	api.ui = common.ui;
	api.dic = common.dic;
	api.uploader = common.uploader;

	exports.common = api;
	T.common = api;

	/**
	 * ===========================MD5加密=======================
	 */
	var hexcase = 0;
	/* hex output format. 0 - lowercase; 1 - uppercase        */
	var b64pad = "";
	/* base-64 pad character. "=" for strict RFC compliance   */
	var chrsz = 8;
	/* bits per input character. 8 - ASCII; 16 - Unicode      */

	/*
	 * These are the functions you'll usually want to call
	 * They take string arguments and return either hex or base-64 encoded strings
	 */
	function hex_md5(s)
	{
		return binl2hex(core_md5(str2binl(s), s.length * chrsz));
	}

	function b64_md5(s)
	{
		return binl2b64(core_md5(str2binl(s), s.length * chrsz));
	}

	function str_md5(s)
	{
		return binl2str(core_md5(str2binl(s), s.length * chrsz));
	}

	function hex_hmac_md5(key, data)
	{
		return binl2hex(core_hmac_md5(key, data));
	}

	function b64_hmac_md5(key, data)
	{
		return binl2b64(core_hmac_md5(key, data));
	}

	function str_hmac_md5(key, data)
	{
		return binl2str(core_hmac_md5(key, data));
	}

	/*
	 * Perform a simple self-test to see if the VM is working
	 */
	function md5_vm_test()
	{
		return hex_md5("abc") == "900150983cd24fb0d6963f7d28e17f72";
	}

	/*
	 * Calculate the MD5 of an array of little-endian words, and a bit length
	 */
	function core_md5(x, len)
	{
		/* append padding */
		x[len >> 5] |= 0x80 << ((len) % 32);
		x[(((len + 64) >>> 9) << 4) + 14] = len;

		var a = 1732584193;
		var b = -271733879;
		var c = -1732584194;
		var d = 271733878;

		for (var i = 0; i < x.length; i += 16)
		{
			var olda = a;
			var oldb = b;
			var oldc = c;
			var oldd = d;

			a = md5_ff(a, b, c, d, x[i + 0], 7, -680876936);
			d = md5_ff(d, a, b, c, x[i + 1], 12, -389564586);
			c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819);
			b = md5_ff(b, c, d, a, x[i + 3], 22, -1044525330);
			a = md5_ff(a, b, c, d, x[i + 4], 7, -176418897);
			d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426);
			c = md5_ff(c, d, a, b, x[i + 6], 17, -1473231341);
			b = md5_ff(b, c, d, a, x[i + 7], 22, -45705983);
			a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416);
			d = md5_ff(d, a, b, c, x[i + 9], 12, -1958414417);
			c = md5_ff(c, d, a, b, x[i + 10], 17, -42063);
			b = md5_ff(b, c, d, a, x[i + 11], 22, -1990404162);
			a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682);
			d = md5_ff(d, a, b, c, x[i + 13], 12, -40341101);
			c = md5_ff(c, d, a, b, x[i + 14], 17, -1502002290);
			b = md5_ff(b, c, d, a, x[i + 15], 22, 1236535329);

			a = md5_gg(a, b, c, d, x[i + 1], 5, -165796510);
			d = md5_gg(d, a, b, c, x[i + 6], 9, -1069501632);
			c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713);
			b = md5_gg(b, c, d, a, x[i + 0], 20, -373897302);
			a = md5_gg(a, b, c, d, x[i + 5], 5, -701558691);
			d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083);
			c = md5_gg(c, d, a, b, x[i + 15], 14, -660478335);
			b = md5_gg(b, c, d, a, x[i + 4], 20, -405537848);
			a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438);
			d = md5_gg(d, a, b, c, x[i + 14], 9, -1019803690);
			c = md5_gg(c, d, a, b, x[i + 3], 14, -187363961);
			b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501);
			a = md5_gg(a, b, c, d, x[i + 13], 5, -1444681467);
			d = md5_gg(d, a, b, c, x[i + 2], 9, -51403784);
			c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473);
			b = md5_gg(b, c, d, a, x[i + 12], 20, -1926607734);

			a = md5_hh(a, b, c, d, x[i + 5], 4, -378558);
			d = md5_hh(d, a, b, c, x[i + 8], 11, -2022574463);
			c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562);
			b = md5_hh(b, c, d, a, x[i + 14], 23, -35309556);
			a = md5_hh(a, b, c, d, x[i + 1], 4, -1530992060);
			d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353);
			c = md5_hh(c, d, a, b, x[i + 7], 16, -155497632);
			b = md5_hh(b, c, d, a, x[i + 10], 23, -1094730640);
			a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174);
			d = md5_hh(d, a, b, c, x[i + 0], 11, -358537222);
			c = md5_hh(c, d, a, b, x[i + 3], 16, -722521979);
			b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189);
			a = md5_hh(a, b, c, d, x[i + 9], 4, -640364487);
			d = md5_hh(d, a, b, c, x[i + 12], 11, -421815835);
			c = md5_hh(c, d, a, b, x[i + 15], 16, 530742520);
			b = md5_hh(b, c, d, a, x[i + 2], 23, -995338651);

			a = md5_ii(a, b, c, d, x[i + 0], 6, -198630844);
			d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415);
			c = md5_ii(c, d, a, b, x[i + 14], 15, -1416354905);
			b = md5_ii(b, c, d, a, x[i + 5], 21, -57434055);
			a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571);
			d = md5_ii(d, a, b, c, x[i + 3], 10, -1894986606);
			c = md5_ii(c, d, a, b, x[i + 10], 15, -1051523);
			b = md5_ii(b, c, d, a, x[i + 1], 21, -2054922799);
			a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359);
			d = md5_ii(d, a, b, c, x[i + 15], 10, -30611744);
			c = md5_ii(c, d, a, b, x[i + 6], 15, -1560198380);
			b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649);
			a = md5_ii(a, b, c, d, x[i + 4], 6, -145523070);
			d = md5_ii(d, a, b, c, x[i + 11], 10, -1120210379);
			c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259);
			b = md5_ii(b, c, d, a, x[i + 9], 21, -343485551);

			a = safe_add(a, olda);
			b = safe_add(b, oldb);
			c = safe_add(c, oldc);
			d = safe_add(d, oldd);
		}
		return Array(a, b, c, d);

	}

	/*
	 * These functions implement the four basic operations the algorithm uses.
	 */
	function md5_cmn(q, a, b, x, s, t)
	{
		return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b);
	}

	function md5_ff(a, b, c, d, x, s, t)
	{
		return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);
	}

	function md5_gg(a, b, c, d, x, s, t)
	{
		return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);
	}

	function md5_hh(a, b, c, d, x, s, t)
	{
		return md5_cmn(b ^ c ^ d, a, b, x, s, t);
	}

	function md5_ii(a, b, c, d, x, s, t)
	{
		return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);
	}

	/*
	 * Calculate the HMAC-MD5, of a key and some data
	 */
	function core_hmac_md5(key, data)
	{
		var bkey = str2binl(key);
		if (bkey.length > 16)
			bkey = core_md5(bkey, key.length * chrsz);

		var ipad = Array(16), opad = Array(16);
		for (var i = 0; i < 16; i++)
		{
			ipad[i] = bkey[i] ^ 0x36363636;
			opad[i] = bkey[i] ^ 0x5C5C5C5C;
		}

		var hash = core_md5(ipad.concat(str2binl(data)), 512 + data.length * chrsz);
		return core_md5(opad.concat(hash), 512 + 128);
	}

	/*
	 * Add integers, wrapping at 2^32. This uses 16-bit operations internally
	 * to work around bugs in some JS interpreters.
	 */
	function safe_add(x, y)
	{
		var lsw = (x & 0xFFFF) + (y & 0xFFFF);
		var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
		return (msw << 16) | (lsw & 0xFFFF);
	}

	/*
	 * Bitwise rotate a 32-bit number to the left.
	 */
	function bit_rol(num, cnt)
	{
		return (num << cnt) | (num >>> (32 - cnt));
	}

	/*
	 * Convert a string to an array of little-endian words
	 * If chrsz is ASCII, characters >255 have their hi-byte silently ignored.
	 */
	function str2binl(str)
	{
		var bin = Array();
		var mask = (1 << chrsz) - 1;
		for (var i = 0; i < str.length * chrsz; i += chrsz)
			bin[i >> 5] |= (str.charCodeAt(i / chrsz) & mask) << (i % 32);
		return bin;
	}

	/*
	 * Convert an array of little-endian words to a string
	 */
	function binl2str(bin)
	{
		var str = "";
		var mask = (1 << chrsz) - 1;
		for (var i = 0; i < bin.length * 32; i += chrsz)
			str += String.fromCharCode((bin[i >> 5] >>> (i % 32)) & mask);
		return str;
	}

	/*
	 * Convert an array of little-endian words to a hex string.
	 */
	function binl2hex(binarray)
	{
		var hex_tab = hexcase? "0123456789ABCDEF": "0123456789abcdef";
		var str = "";
		for (var i = 0; i < binarray.length * 4; i++)
		{
			str += hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8 + 4)) & 0xF) + hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8  )) & 0xF);
		}
		return str;
	}

	/*
	 * Convert an array of little-endian words to a base-64 string
	 */
	function binl2b64(binarray)
	{
		var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		var str = "";
		for (var i = 0; i < binarray.length * 4; i += 3)
		{
			var triplet = (((binarray[i >> 2] >> 8 * (i % 4)) & 0xFF) << 16) | (((binarray[i + 1 >> 2] >> 8 * ((i + 1) % 4)) & 0xFF) << 8 ) | ((binarray[i + 2 >> 2] >> 8 * ((i + 2) % 4)) & 0xFF);
			for (var j = 0; j < 4; j++)
			{
				if (i * 8 + j * 6 > binarray.length * 32)
					str += b64pad;
				else
					str += tab.charAt((triplet >> 6 * (3 - j)) & 0x3F);
			}
		}
		return str;
	}

});
