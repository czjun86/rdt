$(document).ready(function() {
	
	setInterval(function(){$('#curTime').html(currentTime);},1000);
	$(".menuitem > li").find('li').each(function(){
		var _this = $(this).find("ul");
		//没有三级菜单隐藏图片
		if(_this != null && _this.size() == 0){
			$(this).find('.arrows').removeClass('arrows');
		}
	});
	//菜单导航效果
	$('.menuitem > li').hover(function() {
		$(this).find('.children').animate({
			opacity : 'show',
			height : 'show'
		}, 300);
		$(this).find('.xiala1').addClass('navhover');
		$('.three').hide();
	}, function() {
		$('.children').stop(true, true).hide();
		$('.xiala1').removeClass('navhover');
	}).slice(-3, -1).find('.children');
	//二级菜单
	$('.second').live('mouseover mouseout',function(e) {
		if(e.type == 'mouseover'){
			if($(this).find('.three').is(":hidden")){				
				$(this).find('.three').show();
			}
		}else{
			$(this).find('.three').hide();
		}
	});
	//三级菜单
	$('.three').live('mouseout',function(e) {
		if(e.type == 'mouseover'){
			$(this).show();
		}else{
			$(this).hide();
		}
	});
	
	//绑定二级三级菜单单击事件
	$(".ej").find('li').live("click",function(){
		var _this = $(this);
		var check = _this.find("ul");
		var url = _this.attr("url");
		//有三级菜单不跳转
		if( check != null && check.size() == 0 && url != null && url != ""){
			window.location.href =  url;
		}
    });
	//退出登录
	$('#logout').click(function(){
		top.location.href = $(this).attr('contextPath') ;
	});
	//个人信息
	$("#toInfo").click(function(){
		window.location.href =  $(this).attr("url");
	});
	/*
	//二级菜单加载
	$('#topMenu > li').each(function(){
		var resources = null;
		var _this = $(this);
		var parentid = parseInt(_this.attr("parentid"));
		var url = _this.attr('url');
		$.ajax({
			type:"get",
			url:url,
			success:function(data,textStatus,jqXHR){
				if(jqXHR.responseText.indexOf('top.location.href="/cas"') == -1){
					resources = data.resources;
					var ejHtml = "";
					if(resources != undefined && resources != null){
						var len = resources.length;
						ejHtml += "<ul class='children clearfixmenu ej' style='display: none;'>";
						var count = 0;
						for(var i = 0; i < len; i ++){
							var sjHtml = "";
							if(parseInt(resources[i].parentid) == parentid && resources[i].type == 0){
								
								for(var j = 0; j < len; j ++){
									if(parseInt(resources[i].resourceid) == parseInt(resources[j].parentid) && resources[j].type == 0){
										if(count == 0){
											sjHtml += '<ul class="children clearfixmenu three" style="position:absolute;top:-6px;left:123px;display: none;border-top:2px solid #c50000;padding:5px 0;">';
											count ++;
										}
										sjHtml += '<li class="threeli" url="' + contextPath + resources[j].url +'">'+resources[j].resourcename+'</li>';
									}
								}
								//是否有三级菜单
								if(count == 1){
									ejHtml += '<li class="noborder" url="">' +
									'<h3 class="two"><a class="spritemenu" href="javascript:void(0);"><div>'+resources[i].resourcename+'</div><div class="arrows"></div></a></h3>';
									sjHtml += '</ul>';
									count --;
								}else{
									ejHtml += '<li class="noborder"  url="' + contextPath + resources[i].url +'">' +
									'<h3 class="two"><a class="spritemenu" href="javascript:void(0);"><div>'+resources[i].resourcename+'</div></a></h3>';
								}
								ejHtml += sjHtml + '</li>';
							}
						}
						ejHtml += "</ul>";
						//判断是否有二级菜单
						if(ejHtml != ""){
//							_this.append($(ejHtml));
						}
					}
				}else{
					top.location.href="/cas";
				}
			}
		});
		
	});
	*/
});
function currentTime(){
	var d = new Date(),str = '';
	 str += d.getFullYear()+'年';
	 str  += d.getMonth() + 1+'月';
	 str  += d.getDate()+'日  ';
	 str += d.getHours()+'时';
	 str  += d.getMinutes()+'分';
	str+= d.getSeconds()+'秒';
	return str;
}
