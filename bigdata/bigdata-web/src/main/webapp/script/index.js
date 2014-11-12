define(function(require, exports, module)
{
	if (window.T == null)
	{
		window.T = new Object();
	}

	require('./common');

	var index =
	{
		// 初始化
		init : function()
		{	
			index.initUI();
			
			// 轮播
			var tplData = 
			{
				carouselList : [
				{
					image : 'http://gtms03.alicdn.com/tps/i3/TB1da.ZGXXXXXXzXVXXvKyzTVXX-520-280.jpg',
					url : '#'
				},
				{
					image : 'http://gtms04.alicdn.com/tps/i4/TB1gqjtGpXXXXaLXpXXvKyzTVXX-520-280.jpg',
					url : '#'
				},
				{
					image : 'http://i.mmcdn.cn/simba/img/TB1_PHCGXXXXXcUXFXXSutbFXXX.jpg',
					url : '#'
				}]
			};
			var tpl = $('#tpl_carousel').html();
			var html = juicer(tpl, tplData);
			$('#carousel').append(html);
			$('#myCarousel').carousel(
			{
				interval: 3000
			});

			// 商品分类
			var category = [{"child":[{"child":[{"id":"1101010000","name":"羽绒服"},{"id":"1101020000","name":"毛呢外套"},{"id":"1101030000","name":"针织衫"},{"id":"1101040000","name":"短外套"}],"id":"1101000000","keyword":"女士","name":"女式上装"},{"child":[{"id":"1102010000","name":"牛仔裤"},{"id":"1102020000","name":"打底裤"},{"id":"1102030000","name":"休闲裤"},{"id":"1102040000","name":"短裤"}],"id":"1102000000","keyword":"女士","name":"女式裤子"},{"child":[{"id":"1103010000","name":"连衣裙"},{"id":"1103020000","name":"中长款连衣裙"},{"id":"1103030000","name":"长袖连衣裙"},{"id":"1103040000","name":"半身裙"}],"id":"1103000000","keyword":"女士","name":"女式裙子"},{"child":[{"id":"1105010000","name":"长袖衬衫"},{"id":"1105020000","name":"长袖T恤"},{"id":"1105030000","name":"薄夹克"},{"id":"1105040000","name":"卫衣"}],"id":"1105000000","keyword":"男士","name":"当季男装"},{"child":[{"id":"1106010000","name":"牛仔裤"},{"id":"1106020000","name":"休闲裤"},{"id":"1106030000","name":"哈伦裤"},{"id":"1106040000","name":"西裤"}],"id":"1106000000","keyword":"男士","name":"男式裤子"},{"child":[{"id":"1107010000","name":"T恤"},{"id":"1107020000","name":"夹克"},{"id":"1107030000","name":"衬衫"},{"id":"1107040000","name":"毛衣"}],"id":"1107000000","keyword":"男士","name":"男式上装"}],"color":"#EF7B91","id":"1100000000","name":"女装男装","span":"A","style":"margin-right: 8px;height: 218px;"},{"child":[{"child":[{"id":"1201010000","name":"单鞋"},{"id":"1201020000","name":"凉鞋"},{"id":"1201030000","name":"帆布鞋"},{"id":"1201040000","name":"鱼嘴鞋"}],"id":"1201000000","keyword":"女士","name":"春秋女鞋"},{"child":[{"id":"1202010000","name":"凉鞋"},{"id":"1202020000","name":"鱼嘴鞋"},{"id":"1202030000","name":"凉拖"},{"id":"1202040000","name":"防水台凉鞋"}],"id":"1202000000","keyword":"女士","name":"夏季女鞋"},{"child":[{"id":"1203010000","name":"日常休闲鞋"},{"id":"1203020000","name":"运动休闲鞋"},{"id":"1203030000","name":"商务休闲鞋"},{"id":"1203040000","name":"休闲皮鞋"}],"id":"1203000000","keyword":"男士","name":"春秋男鞋"},{"child":[{"id":"1204010000","name":"凉鞋"},{"id":"1204020000","name":"凉拖"},{"id":"1204030000","name":"真皮凉鞋"},{"id":"1204040000","name":"运动休闲凉鞋"}],"id":"1204000000","keyword":"男士","name":"夏季男鞋"},{"child":[{"id":"1205010000","name":"单肩包"},{"id":"1205020000","name":"手提包"},{"id":"1205030000","name":"斜挎包"},{"id":"1205040000","name":"双肩包"}],"id":"1205000000","keyword":"女士","name":"精品女包"},{"child":[{"id":"1206010000","name":"钱包"},{"id":"1206020000","name":"商务男包"},{"id":"1206030000","name":"单肩包"},{"id":"1206040000","name":"手提包"}],"id":"1206000000","keyword":"男士","name":"精品男包"}],"color":"#66AAE9","id":"1200000000","name":"鞋类箱包","span":"B","style":"height: 218px;"},{"child":[{"child":[{"id":"1301010000","name":"巧克力"},{"id":"1301020000","name":"肉干"},{"id":"1301030000","name":"肉脯"},{"id":"1301040000","name":"豆干"}],"id":"1301000000","name":"休闲零食"},{"child":[{"id":"1302010000","name":"冬虫夏草"},{"id":"1302020000","name":"蜂蜜"},{"id":"1302030000","name":"石斛"},{"id":"1302040000","name":"枫斗"}],"id":"1302000000","name":"营养品"},{"child":[{"id":"1303010000","name":"零食"},{"id":"1303020000","name":"坚果"},{"id":"1303030000","name":"有机茶"},{"id":"1303040000","name":"滋补品"}],"id":"1303000000","name":"有机食品"},{"child":[{"id":"1306010000","name":"车厘子"},{"id":"1306020000","name":"樱桃"},{"id":"1306030000","name":"芒果"},{"id":"1306040000","name":"石榴"}],"id":"1306000000","name":"水果蔬菜"}],"color":"#B9D329","id":"1300000000","name":"美食特产","span":"K","style":"margin-right: 8px;height: 170px;"},{"child":[{"child":[{"id":"1401010000","name":"面膜"},{"id":"1401020000","name":"乳液"},{"id":"1401030000","name":"面霜"},{"id":"1401040000","name":"洁面"}],"id":"1401000000","name":"美容护肤"},{"child":[{"id":"1402010000","name":"唇膏"},{"id":"1402020000","name":"口红"},{"id":"1402030000","name":"香水"},{"id":"1402040000","name":"BB霜"}],"id":"1402000000","name":"彩妆香水"},{"child":[{"id":"1403010000","name":"洗发水"},{"id":"1403020000","name":"护发素"},{"id":"1403030000","name":"发膜"},{"id":"1403040000","name":"倒膜"}],"id":"1403000000","name":"美发护发"},{"child":[{"id":"1404010000","name":"Olay"},{"id":"1404020000","name":"玉兰油"},{"id":"1404030000","name":"美肤宝"},{"id":"1404040000","name":"PECHOIN"}],"id":"1404000000","name":"热门品牌"}],"color":"#FA5F94","id":"1400000000","name":"护肤彩妆","span":"H","style":"height: 170px;"},{"child":[{"child":[{"id":"1501010000","name":"Apple"},{"id":"1501020000","name":"苹果"},{"id":"1501030000","name":"Samsung"},{"id":"1501040000","name":"三星"}],"id":"1501000000","name":"手机"},{"child":[{"id":"1502010000","name":"DV数码相机"},{"id":"1502020000","name":"单反相机"},{"id":"1502030000","name":"单电"},{"id":"1502040000","name":"微单"}],"id":"1502000000","name":"相机"},{"child":[{"id":"1503010000","name":"BLUCHIN"},{"id":"1503020000","name":"蓝琴"},{"id":"1503030000","name":"苹果笔记本"},{"id":"1503040000","name":"超级本"}],"id":"1503000000","name":"笔记本"},{"child":[{"id":"1504010000","name":"BLUEING"},{"id":"1504020000","name":"蓝影"},{"id":"1504030000","name":"BYONE"},{"id":"1504040000","name":"宝扬"}],"id":"1504000000","name":"平板电脑"},{"child":[{"id":"1505010000","name":"液晶显示器"},{"id":"1505020000","name":"主板"},{"id":"1505030000","name":"显卡"},{"id":"1505040000","name":"无线鼠标"}],"id":"1505000000","name":"电脑周边"},{"child":[{"id":"1506010000","name":"路由器"},{"id":"1506020000","name":"无线网卡"},{"id":"1506030000","name":"无线路由器"},{"id":"1506040000","name":"网络设备"}],"id":"1506000000","name":"网络存储"}],"color":"#79D9F3","id":"1500000000","name":"数码科技","span":"F","style":"margin-right: 8px;height: 218px;"},{"child":[{"child":[{"id":"1601010000","name":"鞋板鞋"},{"id":"1601020000","name":"休闲鞋"},{"id":"1601030000","name":"滑板鞋"},{"id":"1601040000","name":"跑步鞋"}],"id":"1601000000","name":"运动鞋"},{"child":[{"id":"1602010000","name":"运动套装"},{"id":"1602020000","name":"运动长裤"},{"id":"1602030000","name":"运动茄克"},{"id":"1602040000","name":"运动卫衣"}],"id":"1602000000","name":"运动服"},{"child":[{"id":"1603010000","name":"户外登山背包"},{"id":"1603020000","name":"双肩背包"},{"id":"1603030000","name":"单肩背包"},{"id":"1603040000","name":"腰包"}],"id":"1603000000","name":"运动包"},{"child":[{"id":"1604010000","name":"冲锋衣裤"},{"id":"1604020000","name":"鱼竿"},{"id":"1604030000","name":"徒步鞋"},{"id":"1604040000","name":"钓鱼用品"}],"id":"1604000000","name":"运动用品"},{"child":[{"id":"1605010000","name":"瑜伽"},{"id":"1605020000","name":"健身"},{"id":"1605030000","name":"球迷用品舞蹈"},{"id":"1605040000","name":"健美操"}],"id":"1605000000","name":"运动器材"},{"child":[{"id":"1606010000","name":"足球"},{"id":"1606020000","name":"高尔夫"},{"id":"1606030000","name":"羽毛球"},{"id":"1606040000","name":"篮球"}],"id":"1606000000","name":"球迷用品"}],"color":"#91B566","id":"1600000000","name":"运动户外","span":"D","style":"height: 218px;"},{"child":[{"child":[{"id":"1701010000","name":"女士睡衣"},{"id":"1701020000","name":"男平角裤"},{"id":"1701030000","name":"睡衣套装"},{"id":"1701040000","name":"连裤袜"}],"id":"1701000000","name":"内衣分类"},{"child":[{"id":"1702010000","name":"Hodo"},{"id":"1702020000","name":"红豆"},{"id":"1702030000","name":"Triumph"},{"id":"1702040000","name":"黛安芬"}],"id":"1702000000","name":"内衣品牌"},{"child":[{"id":"1703010000","name":"牛皮皮带"},{"id":"1703020000","name":"围巾"},{"id":"1703030000","name":"腰带"},{"id":"1703040000","name":"自动扣皮带"}],"id":"1703000000","name":"服装配饰"}],"color":"#8F8CAC","id":"1700000000","name":"内衣配饰","span":"C","style":"margin-right: 8px;height: 144px;"},{"child":[{"child":[{"id":"1801010000","name":"黄金"},{"id":"1801020000","name":"翡翠"},{"id":"1801030000","name":"钻石"},{"id":"1801040000","name":"红蓝宝石"}],"id":"1801000000","name":"珠宝钻石"},{"child":[{"id":"1802010000","name":"Longines"},{"id":"1802020000","name":"浪琴"},{"id":"1802030000","name":"Casio"},{"id":"1802040000","name":"卡西欧"}],"id":"1802000000","name":"品牌手表"},{"child":[{"id":"1803010000","name":"项链"},{"id":"1803020000","name":"手链"},{"id":"1803030000","name":"发饰"},{"id":"1803040000","name":"戒指"}],"id":"1803000000","name":"流行饰品"}],"color":"#8F8CAC","id":"1800000000","name":"珠宝手表","span":"R","style":"height: 144px;"},{"child":[{"child":[{"id":"1901010000","name":"儿童羽绒服"},{"id":"1901020000","name":"儿童牛仔裤"},{"id":"1901030000","name":"儿童套装"},{"id":"1901040000","name":"儿童裤子"}],"id":"1901000000","name":"童装"},{"child":[{"id":"1902010000","name":"孕妇裤"},{"id":"1902020000","name":"托腹裤"},{"id":"1902030000","name":"孕妇外套"},{"id":"1902040000","name":"孕妇风衣"}],"id":"1902000000","name":"孕妇用品"},{"child":[{"id":"1903010000","name":"连体衣"},{"id":"1903020000","name":"哈衣"},{"id":"1903030000","name":"新生儿内衣"},{"id":"1903040000","name":"S纸尿裤"}],"id":"1903000000","name":"新生儿"},{"child":[{"id":"1904010000","name":"奶粉"},{"id":"1904020000","name":"辅食"},{"id":"1904030000","name":"营养品"},{"id":"1904040000","name":"零食"}],"id":"1904000000","name":"宝宝食品"},{"child":[{"id":"1905010000","name":"纸尿裤"},{"id":"1905020000","name":"拉拉裤"},{"id":"1905030000","name":"奶瓶"},{"id":"1905040000","name":"婴幼儿推车"}],"id":"1905000000","name":"宝宝用品"},{"child":[{"id":"1906010000","name":"毛绒玩具"},{"id":"1906020000","name":"积木类玩具"},{"id":"1906030000","name":"早教"},{"id":"1906040000","name":"智能玩具"}],"id":"1906000000","name":"儿童玩具"}],"color":"#F78499","id":"1900000000","name":"母婴用品","span":"I","style":"margin-right: 8px;height: 218px;"},{"child":[{"child":[{"id":"2001010000","name":"平板电视"},{"id":"2001020000","name":"冰箱"},{"id":"2001030000","name":"洗衣机"},{"id":"2001040000","name":"空调"}],"id":"2001000000","name":"大家电"},{"child":[{"id":"2002010000","name":"电饭煲"},{"id":"2002020000","name":"净水器"},{"id":"2002030000","name":"电水壶"},{"id":"2002040000","name":"豆浆机"}],"id":"2002000000","name":"厨房电器"},{"child":[{"id":"2003010000","name":"空气净化"},{"id":"2003020000","name":"氧吧"},{"id":"2003030000","name":"吸尘器"},{"id":"2003040000","name":"蒸汽挂烫机"}],"id":"2003000000","name":"生活电器"},{"child":[{"id":"2004010000","name":"打印机"},{"id":"2004020000","name":"多功能一体机"},{"id":"2004030000","name":"投影机"},{"id":"2004040000","name":"投影机配件"}],"id":"2004000000","name":"办公设备"},{"child":[{"id":"2005010000","name":"耳机"},{"id":"2005020000","name":"耳麦"},{"id":"2005030000","name":"移动"},{"id":"2005040000","name":"便携DVD"}],"id":"2005000000","name":"影音电器"},{"child":[{"id":"2006010000","name":"剃须刀"},{"id":"2006020000","name":"电吹风"},{"id":"2006030000","name":"美发造型器"},{"id":"2006040000","name":"体重秤"}],"id":"2006000000","name":"护理按摩"}],"color":"#79D9F3","id":"2000000000","name":"家电办公","span":"G","style":"height: 218px;"},{"child":[{"child":[{"id":"2101010000","name":"旋转拖把"},{"id":"2101020000","name":"平板拖把"},{"id":"2101030000","name":"升降晾晒衣架"},{"id":"2101040000","name":"收纳盒"}],"id":"2101000000","name":"收纳整理"},{"child":[{"id":"2102010000","name":"冬季棉拖"},{"id":"2102020000","name":"棉鞋"},{"id":"2102030000","name":"棉靴"},{"id":"2102040000","name":"居家鞋"}],"id":"2102000000","name":"居家日用"},{"child":[{"id":"2103010000","name":"保温杯"},{"id":"2103020000","name":"玻璃杯"},{"id":"2103030000","name":"炒锅"},{"id":"2103040000","name":"整套茶具"}],"id":"2103000000","name":"餐饮用具"},{"child":[{"id":"2104010000","name":"卷筒纸"},{"id":"2104020000","name":"抽纸"},{"id":"2104030000","name":"洗发水"},{"id":"2104040000","name":"沐浴露"}],"id":"2104000000","name":"洗护清洁"},{"child":[{"id":"2105010000","name":"男用器具"},{"id":"2105020000","name":"安全套"},{"id":"2105030000","name":"女用器具"},{"id":"2105040000","name":"情侣情趣"}],"id":"2105000000","name":"成人用品"},{"child":[{"id":"2107010000","name":"餐桌"},{"id":"2107020000","name":"沙发"},{"id":"2107030000","name":"衣柜"},{"id":"2107040000","name":"简易衣柜"}],"id":"2107000000","name":"住宅家具"},{"child":[{"id":"2108010000","name":"装饰摆件"},{"id":"2108020000","name":"无框画"},{"id":"2108030000","name":"油画"},{"id":"2108040000","name":"墙贴"}],"id":"2108000000","name":"家居饰品"},{"child":[{"id":"2109010000","name":"十字绣"},{"id":"2109020000","name":"地垫"},{"id":"2109030000","name":"布艺窗帘"},{"id":"2109040000","name":"地毯"}],"id":"2109000000","name":"家纺布艺"}],"color":"#ED9E5B","id":"2100000000","name":"日用百货","span":"L","style":"margin-right: 8px;height: 270px;"},{"child":[{"child":[{"id":"2201010000","name":"漫画书籍"},{"id":"2201020000","name":"小说"},{"id":"2201030000","name":"期刊杂志"},{"id":"2201040000","name":"考试"}],"id":"2201000000","name":"书籍杂志"},{"child":[{"id":"2202010000","name":"音乐CD"},{"id":"2202020000","name":"DVD"},{"id":"2202030000","name":"电影"},{"id":"2202040000","name":"电视剧"}],"id":"2202000000","name":"音像影视"},{"child":[{"id":"2203010000","name":"贝司"},{"id":"2203020000","name":"口琴"},{"id":"2203030000","name":"吉他配件"},{"id":"2203040000","name":"钢琴"}],"id":"2203000000","name":"乐器吉他"},{"child":[{"id":"2204010000","name":"中国邮票"},{"id":"2204020000","name":"外国钱币"},{"id":"2204030000","name":"外国邮票"},{"id":"2204040000","name":"港澳台钱币"}],"id":"2204000000","name":"古董收藏"},{"child":[{"id":"2205010000","name":"艺鲜花"},{"id":"2205020000","name":"花卉"},{"id":"2205030000","name":"绿植盆栽"},{"id":"2205040000","name":"花卉"}],"id":"2205000000","name":"鲜花园"},{"child":[{"id":"2206010000","name":"狗狗"},{"id":"2206020000","name":"猫咪"},{"id":"2206030000","name":"猫主粮"},{"id":"2206040000","name":"犬主粮"}],"id":"2206000000","name":"宠物水族"},{"child":[{"id":"2207010000","name":"照片冲印"},{"id":"2207020000","name":"设计素材"},{"id":"2207030000","name":"源文件"},{"id":"2207040000","name":"服装定制"}],"id":"2207000000","name":"个性定制"},{"child":[{"id":"2208010000","name":"男用器具"},{"id":"2208020000","name":"安全套"},{"id":"2208030000","name":"女用器具"},{"id":"2208040000","name":"情侣情趣"}],"id":"2208000000","name":"成人用品"}],"color":"#5DDCE3","id":"2200000000","name":"文化玩乐","span":"N","style":"height: 270px;"}];
			var tplData = 
			{
				categoryList : category
			};
			var tpl = $('#tpl_root_category').html();
			var html = juicer(tpl, tplData);
			$('#root_category').append(html);
			tpl = $('#tpl_category').html();
			html = juicer(tpl, tplData);
			$('#categoryPanels').append(html);
			
			// banner广告
			tplData = 
			{
				url : '#',
				image : 'http://i.mmcdn.cn/simba/img/TB1ODO2FVXXXXcjXVXXSutbFXXX.jpg',
			};
			var tpl = $('#tpl_ad').html();
			var html = juicer(tpl, tplData);
			$($('#categoryPanels').children()[3]).after(html);
			// banner广告
			tplData = 
			{
				url : '#',
				image : 'http://gtms02.alicdn.com/tps/i2/TB1rIfZGpXXXXXIXFXXnLSnFXXX-880-70.png',
			};
			var tpl = $('#tpl_ad').html();
			var html = juicer(tpl, tplData);
			$($('#categoryPanels').children()[8]).after(html);
		},

		// 初始化相关控件
		initUI : function()
		{
			// 读取搜索热词
			T.common.ajax.requestBlock("InquireHotwordAction", null, false, function(jsonstr, data, code, msg)
			{
				var color = ["label-default", "label-primary", "label-success", "label-info", "label-warning", "label-danger"];
				for (var i = 0; i < data.hotwordList.length; i++)
				{
					data.hotwordList[i].color = color[i % color.length];
				}
				var tplData =
				{
					hotwordList : data.hotwordList
				};
				var tpl = $('#tpl_hotword').html();
				var html = juicer(tpl, tplData);
				$('#hotword').append(html);

				$('#hotword span').bind('click', function()
				{
					var query = $(this).html();
					$('#query').val(query);
					index.search();
				});
			});
			
			// 搜索事件
			$('#channel_dropbox li').bind('click', function()
			{
				var channel = $(this).attr('channel');
				$('#channel_dropbox_btn').html($(this).children('a').html() + '<span class="caret"></span>').attr('channel', channel);
			});
			$('#search').bind('click', index.search);
			$('#query').keydown(function(e)
			{
				if (e.keyCode == 13)
				{
					index.search();
					return false;
				}
			});

			// 火箭
			$('#rocket').click(function()
			{
				$('html,body').animate(
				{
					scrollTop : 0
				}, 300);
				return false;
			});
			
			// 自动补全
			$('#query').typeahead(
			{
				source : [
				{
					id : 1,
					name : 'Toronto'
				},
				{
					id : 2,
					name : 'Montreal'
				}]
			});
		},

		// 搜索
		search : function()
		{
			var query = $('#query').val();
			if (query != "")
			{
				var channel = $('#channel_dropbox_btn').attr('channel');
				location.href = "market.html?q=" + query + '&c=' + channel;
			}
		},

		// 读取活动列表
		getActiveList : function()
		{
			$('#tabContent').html('<div align="center"><img src="http://seanzwx.github.io/97igo/image/loading.gif"/></div>');

			// 读取商品列表
			var params =
			{
				pageNo : 1,
				activeChannel : 1,
				activing : 1,
			};
			T.common.ajax.requestBlock("InquireActiveListAction", params, false, function(jsonstr, data, code, msg)
			{
				for (var i = 0; i < data.activeList.length; i++)
				{
					data.activeList[i].name = data.activeList[i].activeName.substring(0, 20);
				}

				var tplData =
				{
					activeList : data.activeList
				};
				var tpl = $('#tpl_activeList').html();
				var html = juicer(tpl, tplData);
				$('#tabContent').html(html);
				$("img.lazy").lazyload(
				{
					effect : "fadeIn"
				});

				$('#activeList>div').bind('mouseover', function()
				{
					$(this).css('border-color', '#FF4400');
					$(this).find('div[class=exception]').show();
				}).bind('mouseout', function()
				{
					$(this).css('border-color', '#dcdcdc');
					$(this).find('div[class=exception]').hide();
				});
			});
		},
	};
	
	var api = 
	{
		init : index.init,
		initUI : function()
		{
			index.initUI();
		},
	};

	exports.index = api;
	T.index = api;
});
