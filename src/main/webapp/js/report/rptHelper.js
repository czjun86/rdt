//延迟一秒后刷新页面
var timeout1;
function reloadReport(url){
	clearTimeout(timeout1);
	timeout1=setTimeout(function(){
		window.location.href=url;
		//window.location.reload();
		//history.go(0); 
		//location.reload(); 
		//location=location; 
		//location.assign(location); 
		//document.execCommand('Refresh'); 
		//window.navigate(location); 
		//location.replace(location); 
		//document.URL=location.href;
	},1000);
}


/**
 * 判断IE浏览器版本
 * 
 * **/

function checkIEVersion(){
	var browser=navigator.appName; 
	if(browser=="Microsoft Internet Explorer"){
		var b_version=navigator.appVersion;
		var version=b_version.split(";"); 
		var trim_Version=version[1].replace(/[ ]/g,""); 
		if(trim_Version=="MSIE8.0") 
		{ 
		   //移除提示文本
			$("input[type='text']").attr("placeholder","");
		} 
		else if(trim_Version=="MSIE9.0") 
		{ 
		   //移除提示文本
			$("input[type='text']").attr("placeholder","");
		}
	}
}


//清空时间控件
$(".picasa").click(function(){
	$("input[name='queryTime']").val('');
});