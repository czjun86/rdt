function infoTable(id){
	if(!$('li[name='+id+']').hasClass('active_tab')){
		var hn = $('.active_tab').attr('name');//获取已展现页id
		var sn = id;//获取要展现页id
		$('.active_tab').removeClass('active_tab');
		$('li[name='+id+']').addClass('active_tab');
		$('#'+sn).show(500);//隐藏已经展现的页
		$('#'+hn).hide(500);//隐藏已经展现的页
	}
}