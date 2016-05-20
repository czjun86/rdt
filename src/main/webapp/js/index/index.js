$(function($){
		$(".pwd").live('click',function(){
			var $modal = $('#pwd_');
			$modal.load(contextPath + '/operator/toUpdPwd',{"Content-type":"application/x-www-form-urlencoded"},function(){
				$modal.modal();
			});
		});
});