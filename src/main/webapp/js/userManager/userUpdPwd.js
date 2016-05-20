/**
 * 保存新添用户或者修改用户
 */
var uniquePassWord = 0;
function updPwd(){
	if(uniquePassWord == 0){
		uniquePassWord++;
		//获取用户信息
		var _userId = $("#userid").val();
		var _password = $("#newPwd1").val();
		//判断是否通过验证
		if(!$("#modfiyPassword").valid()){
			return false; //如果不通过则不提交
		} 
		$.ajax({
			type:"post",
			url:contextPath +"/operator/modfiyPassword",
			data:{
				userid:_userId,
				password:_password
			},
			success:function(data){
				uniquePassWord = 0;
				if(data.re>0){
					$.messager.alert('提示',"修改密码成功,重新登陆时生效!","success",function(){
						$('.default').click();
					});
				}else{
					$.messager.alert('提示',"操作失败！","error");
				}
			},
			error:function(){
				$.messager.alert('提示',"连接服务器失败！","error");
			}
		});
	}
}
