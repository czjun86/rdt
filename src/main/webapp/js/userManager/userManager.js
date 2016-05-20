(function($){
	$(".editUser").click(function(){
		var userid = this.id;
		editUser(userid);
	});
	
	$(".changeUserState").click(function(){
		var userid = this.id;
		var userstate;
		if($(this).attr("_islock")==0){
			userstate=1;
		}else if($(this).attr("_islock")==1){
			userstate=0;
		}
		changeUserState(userid,userstate);
	});
})(jQuery);


/**
 * 打开编辑页面
 **/
function editUser(userid){
	var $modal = $('#ajax-modal');
	// create the backdrop and wait for next modal to be triggered
    //$('body').modalmanager('loading');
	$.fn.modal.Constructor.prototype.enforceFocus = function () { };
    $modal.load(contextPath + '/operator/editUser',{"Content-type":"application/x-www-form-urlencoded",userId:userid},function(){
    	//初始化验证
    	formValidate.init();
	$(".saveUser").click(function(){
			saveUser();
		});
	$("#province").change(function(){
		areaChange($(this).val() , 1);
	});
	$("#district").change(function(){
		areaChange($(this).val() , 2);
	});
	$("#operator").change(function(){
		var i = $(this).val();
		linkChange(i);
	});
	//initSelect();//初始化select2
      $modal.modal();
    });
}

/**
 * 保存新添用户或者修改用户
 */
var unique= 0;
function saveUser(){
	if(unique==0){//防止快速多次提交
		unique++;
		//判断是否通过验证
		if(!$("#editUser").valid()){
			return false; //如果不通过则不提交
		}
		//获取用户信息
		var _userId = $("#userid").val();
		var _userName = $("#userName").val();
		var _name =$("#name").val();
		var _email = $("#email").val();
		var _phone = $("#phone").val();
		var _islock = -1;
		var _operator = $("#operator").val();
		var _roleid = $("#roleid").val();
		var _province =$("#province").val();
		var _district = $("#district").val();
		var _county = $("#county").val();
		//运营商权限
		var uni = "0";
		var mob = "0";
		var tele = "0";
		if($("#checkuni").attr("checked")){
			uni="1";
		}
		if($("#checkmob").attr("checked")){
			mob="1";
		}
		if($("#checktele").attr("checked")){
			tele="1";
		}
		var _teleAuth = uni+mob+tele;
		
		if($("#islock").is(':checked')){
			_islock=0;
		}else{
			_islock=1;
		}
		var sucMsg="";
		if(_userId!=null&&_userId!=""){
			sucMsg="修改成功！";
		}else{
			sucMsg="添加成功！";
		}
		$.ajax({
			type:"post",
			url:contextPath +"/operator/saveUser",
			data:{
				userid:_userId,
				userName:_userName,
				name:_name,
				email:_email,
				phone:_phone,
				islock:_islock,
				operator:_operator,
				roleid:_roleid,
				province:_province,
				district:_district,
				county:_county,
				teleAuth:_teleAuth
			},
			success:function(data){
				unique = 0;
				if(data.msg==1){
					$.messager.alert('提示',sucMsg,"success",function(){reloadPage(contextPath + '/operator/opManager?op_menu=11',100);});
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

/**
 * 更改用户状态
 */
function changeUserState(userid,islock){
	$.ajax({
		type:"post",
		url:contextPath +"/operator/changeUserState",
		data:{
			userid:userid,
			islock:islock
		},
		success:function(data){
			if(data.msg==1){
				$.messager.alert('提示',"修改成功！","success",function(){reloadPage(contextPath + '/operator/opManager?op_menu=11',100);});
			}else{
				$.messager.alert('提示',"操作失败！","error");
			}
		},
		error:function(){
			$.messager.alert('提示',"连接服务器失败！","error");
		}
	});
}

//延迟设置时间后刷新页面
var timeout1;
function reloadPage(url,ms){
	clearTimeout(timeout1);
	timeout1=setTimeout(function(){
		//window.location.href=url;
		$("#pageForm").submit();
		//window.location.reload();
		//history.go(0); 
		//location.reload(); 
		//location=location; 
		//location.assign(location); 
		//document.execCommand('Refresh'); 
		//window.navigate(location); 
		//location.replace(location); 
		//document.URL=location.href;
	},ms);
}

/**
*修改省/市联动修改区县
**/

function areaChange(id,flag){
	if(flag == 1){
		$.ajax({
			type:"post",
			url:contextPath +"/operator/provinceChange",
			data:{
				id:id,
				flag:1
			},
			success:function(data){
				var district = document.getElementById ("district");
				var county = document.getElementById ("county");
				//清空已有选项
				$('#district').find("option").remove();
				$('#county').find("option").remove();
				//加入新选项
				var districts = data.districts;
				var countys = data.countys;
				district.options.add(new Option('全部','-1')); 
				if(districts.length>0){
					for(var i=0;i<districts.length;i++){
					    district.options.add(new Option(districts[i].text,districts[i].id)); 
					}
				}
				/*if(countys.length>0){
					for(var i=0;i<countys.length;i++){
						county.options.add(new Option(countys[i].text,countys[i].id)); 
					}
				}else{
				    county.options.add(new Option('无','')); 
				}*/
				county.options.add(new Option('全部','-1')); 
			}
		});
	}else if(flag == 2){
		if(id!='-1'){
			$.ajax({
				type:"post",
				url:contextPath +"/operator/provinceChange",
				data:{
					id:id,
					flag:2
				},
				success:function(data){
					var county = document.getElementById ("county");
					//清空已有选项
					$('#county').find("option").remove();
					//加入新选项
					var countys = data.countys;
					county.options.add(new Option('全部','-1')); 
					if(countys.length>0){
						for(var i=0;i<countys.length;i++){
							county.options.add(new Option(countys[i].text,countys[i].id)); 
						}
					}
				}
			});
		}else{
			var county = document.getElementById ("county");
			//清空已有选项
			$('#county').find("option").remove();
			county.options.add(new Option('全部','-1')); 
		}
	}
}

$(function(){
	$(".deleteUser").live('click',function(){
		var id=$(this).attr("userid");
		$.messager.confirm('提示',"是否确定删除\""+$(this).attr('un')+"\"用户？",function(data){
			if(data){
				$.ajax({
					type:"post",
					url:contextPath +"/operator/deleteUser",
					data:{
						userid:id
					},
					success:function(data){
						if(data.msg==1){
							$.messager.alert('提示',"删除成功！","success",function(){reloadPage(contextPath + '/operator/opManager?op_menu=11',100);});
						}else{
							$.messager.alert('提示',"操作失败！","error");
						}
					},
					error:function(){
						$.messager.alert('提示',"连接服务器失败！","error");
					}
				});
			}
		});
	});
});

/**
 * 运营商选择联动
 * @param i
 */
function linkChange(i){
	if(i == "1"){
		$("input[name='check']").removeAttr('checked');
		$("input[name='check']").removeAttr('disabled');
		$("#checkuni").attr('disabled','disabled');
		$("#checkuni").attr('checked','true');
	}else if(i=="2"){
		$("input[name='check']").removeAttr('checked');
		$("input[name='check']").removeAttr('disabled');
		$("#checkmob").attr('disabled','disabled');
		$("#checkmob").attr('checked','true');
	}else if(i=="3"){
		$("input[name='check']").removeAttr('checked');
		$("input[name='check']").removeAttr('disabled');
		$("#checktele").attr('disabled','disabled');
		$("#checktele").attr('checked','true');
}
}