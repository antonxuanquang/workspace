(function()
{
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
	var s;
	(s = ua.match(/rv:([\d.]+)\) like gecko/)) ? Sys.ie = s[1] :
	(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
	(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
	(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
	(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
	(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
	
	var version = parseFloat(Sys.ie);
	if (Sys.ie <= 8.0) 
	{
		var r = confirm("亲, 您怎么还在用石器时代的浏览器? 下载现代浏览器吗?")
		if (r == true)
		{
			location = "http://112.90.6.247/tech.down.sina.com.cn/20141008/7bc27741/40.0.2181.0_chrome_installer.exe?fn=&ssig=ecZM4ttCZj&Expires=1412938167&KID=sae,230kw3wk15&ip=1412858967,220.250.21.82&corp=1";
		}
	}
})();