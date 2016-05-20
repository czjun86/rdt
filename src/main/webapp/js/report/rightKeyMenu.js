/**
 * 右键事件
 */
$(function(){
	$("tr[name='linkToMap']").mousedown(function(e){
		if(e.which==3){
			var event=e||event;
			var left=event.pageX+'px';
			var top=event.pageY+'px';
			$("#linkRow").attr("linkGisId",$(this).attr('id'));
			$("#rightMenu").css('top',top);
			$("#rightMenu").css('left',left);
			$("#rightMenu").show();
			$('.page-header-fixed').bind("click",function(){
				$("#rightMenu").hide();
				$('.page-header-fixed').unbind("click");
			});
		}
	});
	$("#linkRow").live('click',function(){
		linkToGis("#"+$("#linkRow").attr("linkGisId"));
	});
});