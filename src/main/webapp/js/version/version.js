var unique= 0;
$(function(){
	$(".deleteVersion").live('click',function(){
		var id = $(this).attr("nid");
		$.messager.confirm('提示',"是否删除"+$(this).attr("nname")+"?",function(data){
			if(unique==0 && data){
				unique++;
				$.ajax({
					type:"post",
					url:contextPath +"/version/delete",
					data:{
						id:id
					},
					success:function(data){
						if(data==1){
							unique = 0;
							$.messager.alert('提示',"删除成功","success",function(){
								$("#modal-backdrop").show();
								timeout1=setTimeout(function(){
									$("#pageForm").submit();
								},100);
							});
						}else if(data==2){
							unique = 0;
							$.messager.alert('提示',"该版本正在使用无法删除！","error");
						}else{
							unique = 0;
							$.messager.alert('提示',"操作失败！","error");
						}
					},
					error:function(){
						unique = 0;
						$.messager.alert('提示',"连接服务器失败！","error");
					}
				})
			}
		});
	});
	
	$("#add").live('click',function(){
		openModal();
	});
});
function openModal(){
	var $modal = $('#ajax-modal');
    $modal.load(contextPath + '/version/openModal',{"Content-type":"application/x-www-form-urlencoded"},function(){
    	//初始化验证
    	formValidate.init();
    	$(".saveChild").live('click',function(){
    		saveVersionFile();
    	});
    	$("#file").change(function(){
    		var name = $(this).val();
    		if(name.lastIndexOf(".")>0){
    			name = name.substring(0,name.lastIndexOf("."));
    		}
    		$("#soft_ver").val(name);
    		$("#saveChild").valid();
    	});
    	$modal.modal();
    });
}
function saveVersionFile(){
	if(unique==0){//防止快速多次提交
		unique++;
		$('#saveChild').form({url:contextPath + '/version/save', 
			onSubmit:function(){
				if(!$("#saveChild").valid()){
					unique = 0;
					return false; //如果不通过则不提交
				}else{
					var file = $("#file").val();
					if(file!=null && file!=""){
						$("#fileName").val(file);
					}else{
						unique = 0;
						$.messager.alert('提示', "请选择文件!", "error");
						return false;
					}
				}
				$("#modal-backdrop").show();
				return true;
			}, success: function (data) {
				unique = 0;
				var info = eval("(" + data + ")");
				$("#modal-backdrop").hide();
				if(info.msg==0){
					$('.default').click();
					$.messager.alert('提示', "保存成功!", "success",function(){
						$("#modal-backdrop").show();
						$("#pageForm").submit();
					});
				}else if(info.msg==1){
					$.messager.alert('提示', "服务器保存路径不正确!", "error");
				}else if(info.msg==2){
					$.messager.alert('提示', "版本文件大于30M!", "error");
				}else{
					$.messager.alert('提示', "保存失败!", "error");
				}
			} ,error: function (){
				$("#modal-backdrop").hide();
				unique = 0;
				$.messager.alert('提示', "服务器错误!", "error");
			}
		});
		$('#saveChild').submit(); 
	}
}