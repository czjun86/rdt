unique = 0;
$(function(){
	$("#save").live('click',function(){
		var provinceid = $("#province").val();
		var districtid = $("#district").val();
		var areaid = $("#area").val();
		var province = $("#province").select2("data").text;
		var district = $("#district").select2("data").text;
		var area = $("#area").select2("data").text;
		
		var operator = $("#operator").val();
		var imei = $("#imei").val();
		var model = $("#model").val();
		var colletion = $("#colletion").select2("data").text;
		var vision = $("#vision").select2("data").text;
		
		var mark = $("#mark").val();
		if(unique==0){
			unique ++;
			if($("#saveChild").valid()){
				if(colletion!="" && vision!=""){
					var regionCode = 0;
					if(areaid=="-1"){
						if(districtid == "-1"){
							regionCode = provinceid
						}else{
							regionCode = districtid;
						}
					}else{
						regionCode = areaid;
					}
					$("#modal-backdrop").show();
					$.ajax({
						type : "post",
						url : contextPath + "/epinfo/save",
						data : {
							id : $("#id").val(),
							province:province,
							district:district,
							area:area,
							regionCode : regionCode,
							operator : operator,
							imei : imei,
							model : model,
							colletion : colletion,
							vision : vision,
							mark:mark
						},
						success : function(data) {
							$("#modal-backdrop").hide();
							unique = 0;
							if (data == 1) {
								$.messager.alert('提示', "保存成功", "success", function() {
									goBack();
								});
							} else {
								$.messager.alert('提示', "操作失败！", "error");
							}
						},
						error : function() {
							unique = 0;
							$("#modal-backdrop").hide();
							$.messager.alert('提示', "连接服务器失败！", "error");
						}
					})
				}else{
					unique = 0;
					$.messager.alert('提示', "方案与版本不能为空！", "warning");
				}
			}else{
				unique = 0;
			}
		}
		
	});
});

function goBack(){
	$("#modal-backdrop").show();
	pageForm.submit();
}