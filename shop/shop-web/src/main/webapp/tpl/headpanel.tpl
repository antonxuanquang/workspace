<style>
    .nav_li {
        color: white;
        background-color: #D9534F;
        font-size: 16px;
        font-weight: bold;
        height: 50px;
        width: 100px;
    	text-decoration: none;
    }
    .nav_li:hover {
    	text-decoration: none;
    	color: white;
    }
    .nav_li:after {
    	text-decoration: none;
    	color: white;
    }
</style>
            
<div style="position: fixed; left: 0px; top: 0px; width:100%;background-color: #D9534F;z-index: 10;height:50px;" align="center">
	<div style="width: 1210px;">
		<ul id="items" class="nav navbar-nav" style="height: 50px;">
			<li>
				<a href="index.html" class="nav_li" style="background-color: #D9534F">首页</a>
			</li>
			<li>
				<a href="market.html?channel=1" class="nav_li" style="background-color: #D9534F">9.9包邮</a>
			</li>
			<li>
				<a href="market.html?channel=2" class="nav_li" style="background-color: #D9534F">精品服饰</a>
			</li>
			<li>
				<a href="tutorial.html" class="nav_li" style="background-color: #D9534F">免费购物</a>
			</li>
			<li>
				<a href="download.html" class="nav_li" style="background-color: #D9534F">手机爱购</a>
			</li>
			<li class="logined" style="display:none">
				<a href="profile.html" class="nav_li" style="background-color: #D9534F">我的资料</a>
			</li>
			<li class="logined" style="display:none">
				<a id="signout" href="javascript:void(0)" class="nav_li" style="background-color: #D9534F">注销</a>
			</li>
			<li class="unlogin">
				<a href="javascript:void(0)" class="nav_li" style="background-color: #D9534F" data-toggle="modal" data-target="#signinModal">登录|注册</a>
			</li>
		</ul>
	</div>
</div>

<div align="center">
	<div id="search_panel" align="left" style="width: 1210px; height: 78px;margin-top:56px;">
		<!-- logo -->
		<div style="width: 200px; float: left;">
			<div style="font-family: sans-serif; font-size: 36px;color: red;text-align: center">97爱购网</div>
			<div style="text-align: center; color:red">www.97igo.com</div>
		</div>		
		<!-- 搜索框 -->		
		<div style="width: 800px; float: left;">
			<!-- 搜索框 -->
			<div class="row">
				<form class="bs-example bs-example-form" role="form">
					<div class="col-lg-6" style="padding: 0px;">
						<div class="input-group">
							<div class="input-group-btn">
								<button id="channel_dropbox_btn" channel="-1" type="button" class="btn btn-default dropdown-toggle" style="height: 44px;" data-toggle="dropdown">全部 <span class="caret"></span></button>
								<ul id="channel_dropbox" class="dropdown-menu">
									<li channel="-1">
										<a href="javascript:void(0)">全部 </a>
									</li>
									<li channel="1">
										<a href="javascript:void(0)">9.9包邮 </a>
									</li>
									<li channel="2">
										<a href="javascript:void(0)">精品服饰 </a>
									</li>
								</ul>
							</div>
							<input id="query" type="text" class="form-control typeahead" style="width: 650px;height: 44px;" placeholder="输入商品关键字,50万商品任你搜">
							<span class="input-group-btn">
								<button id="search" class="btn btn-default" type="button" style="height: 44px;">搜索商品</button> 
							</span>
						</div>
					</div>
				</form>						
			</div>
			<!-- 热门搜索词 -->
			<div class="row">
				<h4 id="hotword" style="width: 800px;">
					{@each hotwordList as it,index}
						<span class="label ${it.color}" style="cursor: pointer">${it.hotword}</span>
					{@/each}
				</h4>
			</div>
		</div>
		<!-- 二维码 -->
		<div style="float: right">
			<a href="download.html">
				<img src="http://seanzwx.github.io/97igo/image/app.png" width="60" height="60" />
				<div style="text-align: center; color:#D9534F; font-size: 12px;">手机爱购</div>
			</a>
		</div>
	</div>
</div>

<!-- 火箭 -->
<div style="z-index: 1000; position: fixed; right: 10px; bottom: 10px;" title="回顶部">
	<a id="rocket" href="javascript:void(0)">
		<img src="http://seanzwx.github.io/97igo/image/rocket.png" />
	</a>
</div>

<!-- 登录（Modal） -->
<div class="modal fade" id="signinModal" tabindex="-1" role="dialog" aria-labelledby="signinModalLabel" aria-hidden="true">
   <div class="modal-dialog" style="width:300px;">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title" id="signinModalLabel">登录 | 快速注册</h4>
         </div>
         <div class="modal-body">
			<form role="form">
			   <div class="form-group">
			      <label for="name">帐号:</label>
			      <input type="text" class="form-control" id="username" placeholder="请输入帐号" value="sean_zwx">
			   </div>
			   <div class="form-group">
			      <label for="name">密码:</label>
			      <input type="text" class="form-control" id="password" placeholder="请输入密码" value="xuxu">
			   </div>
			</form>
         </div>
         <div class="modal-footer">
            <button id="signin" type="button" class="btn btn-primary btn-block">登 录</button>
            <button id="signup" type="button" class="btn btn-danger btn-block">快速注册</button>
         </div>
      </div>
</div>