define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	var api =
	{
		/**
		 * 初始化分页控件
		 * @param {int} pageNo
		 * @param {int} totalrecords
		 * @param {int} pageSize
		 * @param {function} callback
		 */
		initPage : function(pageNo, totalrecords, pageSize, callback)
		{
			// 计算分页
			var totalpages = totalrecords % pageSize == 0? parseInt(totalrecords / pageSize): parseInt(totalrecords / pageSize) + 1;

			var pages = new Array();
			for (var i = pageNo; i <= totalpages && pages.length < 10; i++)
			{
				pages[pages.length] =
				{
					pageNo : i
				};
			}

			var tplData =
			{
				pages : pages,
				last : totalpages,
				next : pageNo >= totalpages? totalpages: parseInt(pageNo) + 1,
				previous : pageNo == 1? 1: parseInt(pageNo) - 1,
			};
			var tpl = $('#tpl_page').html();
			html = juicer(tpl, tplData);
			$('#pages').html(html);

			$('#pages li').bind('click', function()
			{
				var pageNo = $(this).attr("pageNo");
				callback(pageNo);
			});
		}
	};

	exports.util = api;
	T.util = api;
});
