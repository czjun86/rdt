$(function(){
	$("#netType").change(function(){
		var netType = $(this).val();
		//获取运营商类型
		var operator = $("#operator").val();
		//删除当前所有指标
		$('#quota').find("option").remove();
		//获取指标select节点
		var quota = document.getElementById ("quota");
		$.ajax({
			type:"POST",
			url:contextPath+"/area/queryNetType",
			data:{netType:netType},
			dataType:"json",
			success:function(data){
				if(data.length>0){
				for(var i = 0;i<data.length;i++){
					quota.options.add(new Option(data[i].kpiName,data[i].kpiType));
				}
				//默认选中rxpower
				if(data.length>0){
					$("#quota").select2("val", data[0].kpiType);
					 var kpi=data[0];
					 $("#legend").find("span.heat_1").html(">"+kpi.interValuelOne+"");
						$("#legend").find("span.heat_2").html("["+kpi.interValuelOne+","+kpi.interValuelTwo+")");
						$("#legend").find("span.heat_3").html("["+kpi.interValuelTwo+","+kpi.interValuelThree+")");
						$("#legend").find("span.heat_4").html("["+kpi.interValuelThree+","+kpi.interValuelFour+")");
						$("#legend").find("span.heat_5").html("["+kpi.interValuelFour+","+kpi.interValuelFive+")");			
						$("#legend").find("span.heat_6").html("<="+kpi.interValuelFive+"");
						$("#legend").find("span.title").html("("+kpi.kpiUnit+"");
				}}
			},
			error:function(){
				$.messager.alert('提示',"查询发生未知错误！！！","error");
			}
		});
	});
});