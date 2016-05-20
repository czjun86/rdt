$(document).ready(function() {
 // initAjaxForm();
  $('#submit').click(function(){
	  var _this = $('#username');
	  var _errorMsg = $('#errorMsg');
	  var _length = jQuery.trim(_this.val()).length;
	  if(_length == 0){
		  _errorMsg.html('用户名不能为空!');
		  _this.focus();
		  return;
	  }
	  if(_length > 30){
		  _errorMsg.html('用户名不能大于30位!');
		  _this.focus();
		  return;
	  }
	  _username = _this.val();
	  _this = $('#password');
	  _length = jQuery.trim(_this.val()).length;
	  if(_length == 0)
      {
		  _errorMsg.html('密码不能为空!');
		  _this.focus();
		  return;
      }
	  if(_length > 32){
		  _errorMsg.html('密码不能大于32位');
		  _this.focus();
		  return;
	  }
	  _errorMsg.html('正在登陆...');
	  $("#myform").submit();
  });
  
  $('#myform').keydown(function(e){
	  var curKey = e.which;
	  if(curKey == 13)
		  $(this).submit();
  });
});
function initAjaxForm(){
	$('#myform').ajaxForm(function(data) {
		 var _this = $('#password');
		 var _errorMsg = $('#errorMsg');
		 _errorMsg.html(data.msg);
		 if(data.status == 0)
		 {
			 _this.val('');
			 _this.focus();
		 }
		 else if(data.status == 1){
			 location.href = contextPath + data.url;
	     }
	  }); 
}