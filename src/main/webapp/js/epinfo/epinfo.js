unique = 0;
$(function(){
	$("#add").click(function(){
		$("#modal-backdrop").show();
		$("#editChild").val(null);
		$("#editPage").submit();
	});
	$(".edit").live('click',function(){
		$("#modal-backdrop").show();
		$("#editChild").val($(this).attr('nid'));
		$("#editPage").submit();
	});
	/**
	 * 复选框连动
	 */
	$("input[name='checkAll']").change(function(){ 
		yfpzBtn();
		if($("input[name='checkAll']:checkbox").is(":checked")){
			$("input[name='checkOne']:checkbox").each(function(){
				$(this).attr("checked",true);
				$(this).parent().addClass('checked');
			});
		}
	});
	$("input[name='checkOne']:checkbox").change(function(){ 
		yfpzBtn();
		var flag = false;
		$("input[name='checkOne']:checkbox").each(function(){
			if(!$(this).is(":checked")){
				flag = true;
			}
		});
		if(flag){
			$("input[name='checkAll']:checkbox").attr("checked",false);
			$("input[name='checkAll']:checkbox").parent().removeClass('checked');
		}
	});
	
	/**
	 * 查询
	 */
	$("#querybtn").click(function(){
		$("#modal-backdrop").show();
		$("#queryTime_query").val($("#queryTime").val());
		$("#province_query").val($("#province").val());
		$("#district_query").val($("#district").val());
		$("#area_query").val($("#area").val());
		$("#operator_query").val($("#operator").val());
		$("#imei_query").val($("#imei").val());
		$("#model_query").val($("#model").val());
		$("#colletion_query").val($("#colletion").val());
		$("#vision_query").val($("#vision").val());
		$("#pageForm").submit();
	});
	
	/**
	 * 批量保存方案
	 */
	$("#colletion_nums_save").click(function(){
		var colletionVal = $("#colletion_nums").select2("data").text;
		var url = contextPath + "/epinfo/updateCollection"
		pluralUpdate(colletionVal,url);
	});
	
	/**
	 * 批量保存版本
	 */
	$("#vivsion_nums_save").click(function(){
		var visionVal = $("#vision_nums").select2("data").text;
		var url = contextPath + "/epinfo/updateVivsion"
		pluralUpdate(visionVal,url);
	});
	
	/**
	 * 导出
	 */
	$("#export").click(function(){
		if(unique==0){
			$("#modal-backdrop").show();
			unique++;
			$.ajax({
				type : "post",
				url : contextPath+"/epinfo/createExport",
				data:{
					queryTime : $("#queryTime_query").val(),
					province : $("#province_query").val(),
					district : $("#district_query").val(),
					area : $("#area_query").val(),
					operator : $("#operator_query").val(),
					imei : $("#imei_query").val(),
					model : $("#model_query").val(),
					colletion : $("#colletion_query").val(),
					vision : $("#vision_query").val()
				},
				success : function(data) {
					$("#modal-backdrop").hide();
					unique = 0;
					if (data != "-1") {
						window.location.href=contextPath+"/export/export?fileName="+data;
					} else {
						$.messager.alert('提示', "导出失败！", "error");
					}
				},
				error : function() {
					$("#modal-backdrop").hide();
					unique = 0;
					$.messager.alert('提示', "连接服务器失败！", "error");
				}
			});
		}
	});
	
	/**
	 * 导出模板
	 */
	$("#demo").click(function(){
		if(unique==0){
			$("#modal-backdrop").show();
			unique++;
			window.location.href=contextPath+"/export/template?fileName="+$(this).attr("fileName");
			setTimeout(function(){
				$("#modal-backdrop").hide();
				unique = 0;
			},500);
		}
	});
	
	/**
	 * 导入
	 */
	$("#import").click(function(){
		var $modal = $('#ajax-modal');
	    $modal.load(contextPath + '/epinfo/importFile',{"Content-type":"application/x-www-form-urlencoded"},function(){
	    	$(".importFile").live('click',function(){
	    		importExcel();
	    	});
	    	$modal.modal();
	    });
	});
});
function importExcel(){
	if(unique == 0){
		unique++;
		$('#importExcel').form({url:contextPath + '/epinfo/saveExcel', 
			onSubmit:function(){
				var name = $("#file").val();
				if(name!=null && name!=""){
					if(name.indexOf(".xlsx")>0){
						unique = 0;
						$("#modal-backdrop").show();
						return true;
					}else{
						unique = 0;
						$.messager.alert('提示', "请选择正确类型的文件!", "error");
						return false;
					}
				}else{
					unique = 0;
					$.messager.alert('提示', "请选择文件!", "error");
					return false;
				}
				
			}, success: function (data) {
				var info = eval("(" + data + ")")
				$('.default').click(); 
				if(info.msg == 0){
					$("#modal-backdrop").hide();
					unique = 0;
					$.messager.alert('提示', "导入成功!", "success",function(){
						$("#pageForm").submit();
					});
				}else{
					document.getElementById("error").innerHTML = openErrorInfo( info.error);
					unique = 0;
					$("#modal-backdrop").hide();
					$("#error").modal('show');
				}
			}
		});
		$('#importExcel').submit(); 
	}
}

function openErrorInfo(html){
	var word="";
	word+="<div class='portlet box blue' style='width:545px;margin-left:30%;margin-top:50px;'>";
		word+="<div class='portlet-title'>";
			word+="<div class='caption'>错误信息</div>";
		word+="</div>";
		word+="<div class='portlet-body form'>";
			word+="<div class='form-body'>";
				word+= html;
			word+="</div>";
			word+="<div class='form-actions modal-footer'>";
				word+="<button type='button' class='btn default' data-dismiss='modal'>关闭</button>";
			word+="</div>";
		word+="</div>";
	word+="</div>";
	return word;
}
function isEmpty(str)     
{     
    if(str != null && str.length > 0)     
    {     
        return true;     
    }     
    return false;     
}    
/**
 * 复选框勾选展示批处理按钮
 */
function yfpzBtn(){
	var flag = false;
	//只要有一个复选框选中就展示
	$("input[name='checkOne']:checkbox").each(function(){
		if($(this).is(":checked")){
			flag = true;
		}
	});
	if($("input[name='checkAll']").is(":checked")){
		flag = true;
	}
	if(flag){
		$(".yfpz_btn").show();
	}else{
		$(".yfpz_btn").hide();
	}
}

/**
 * 批量更新
 * @param value
 * @param url
 */
function pluralUpdate(value,url){
	var ids ="";
	if($("input[name='checkAll']:checkbox").is(":checked")){
		ids += $("input[name='checkAll']:checkbox").val()+",";
	}else{
		$("input[name='checkOne']:checkbox").each(function(){
			if($(this).is(":checked")){
				ids += $(this).val()+",";
			}
		});
	}
	if(ids.length>1){
		ids = ids.substring(0,ids.length-1);
	}
	if(unique==0){
		$("#modal-backdrop").show();
		unique++;
		$.ajax({
			type : "post",
			url : url,
			data:{
				value : value,
				ids : ids,
				queryTime : $("#queryTime_query").val(),
				province : $("#province_query").val(),
				district : $("#district_query").val(),
				area : $("#area_query").val(),
				operator : $("#operator_query").val(),
				imei : $("#imei_query").val(),
				model : $("#model_query").val(),
				colletion : $("#colletion_query").val(),
				vision : $("#vision_query").val()
			},
			success : function(data) {
				$("#modal-backdrop").hide();
				unique = 0;
				if (data == 1) {
					$.messager.alert('提示', "修改成功", "success", function() {
						$("#modal-backdrop").show();
						$("#pageForm").submit();
					});
				} else {
					$.messager.alert('提示', "操作失败！", "error");
				}
			},
			error : function() {
				$("#modal-backdrop").hide();
				unique = 0;
				$.messager.alert('提示', "连接服务器失败！", "error");
			}
		});
	}
}

function importMsg(error ,msg){
	if(msg == 0){
		$.messager.alert('提示', "修改成功", "success", function() {
			$("#modal-backdrop").show();
			$("#pageForm").submit();
		});
	}else{
		var html ="";
		for(var i = 0;i<error.size;i++){
			html += error[i].name+":"+error[i].info+"<br/>";
		}
		alert(html);
	}
}

function importMsg(){
	alert("aa");
}