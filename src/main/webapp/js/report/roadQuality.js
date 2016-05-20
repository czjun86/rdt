$(function(){
	$("#querybtn").click(function(){
		var str = $("#threshold").val();
		if($("#operator").val()!=null && $("#operator").val()!=""){
			if(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(str)||str==""){
				$("#modal-backdrop").show();
				//页码回复默认
				$("#pageSize").val(null);
				$("#pageIndex").val(null);
				//uuid清空
				$("#uuid_page").val(null);
				//时间
				$("#queryTime_page").val($("#queryTime").val());
				//运营商
				$("#operator_page").val($("#operator").val());
				//指标
				$("#kpiType_page").val($("#kpiType").val());
				//区域
				$("#province_page").val($("#province").val());
				$("#district_page").val($("#district").val());
				$("#area_page").val($("#area").val());
				//道路
				$("#roadType_page").val($("#roadType").val());
				$("#roadLevel_page").val($("#roadLevel").val());
				//阀值
				$("#threshold_page").val($("#threshold").val());
				//对比方式
				$("#compare_page").val($("#compare").val());
				$("#pageForm").submit();
			}
		}else{
			$.messager.alert('提示',"请选择对比运营商","error");
		}
	});
	$("#leadExcel").click(function(){
		var queryFlag = $("#flag_query").val();
		var uuid = $("#uuid_page").val();
		if(queryFlag=="1"){
			$("#modal-backdrop").show();
			$.ajax({
				type:"post",
				url:contextPath + "/roadQuality/export",
				data:{
					uuid:uuid,
					queryTime:$("#queryTime_page").val(),
					operator:$("#operator_page").val(),
					kpiType:$("#kpiType_page").val(),
					
					province:$("#province_page").val(),
					district:$("#district_page").val(),
					area:$("#area_page").val(),
					
					roadType:$("#roadType").val(),
					roadLevel:$("#roadLevel_page").val(),
					
					threshold:$("#threshold_page").val(),
					compare:$("#compare_page").val()
				},
				success:function(data){
					$("#modal-backdrop").hide();
					var fileName = data.fileName;
					if(fileName!="-1"){
						window.location.href=contextPath+"/export/export?fileName="+fileName;
					}else{
						$.messager.alert('提示',"生成excel失败","error");
					}
				},
				error:function(data){
					$("#modal-backdrop").hide();
					$.messager.alert('提示',"生成excel失败","error");
				}
			});
		}else{
			$.messager.alert('提示',"请先查询数据!","warning");
		}
	});
	/**
	 * 双击跳转
	 */
	$("tr[name='linkToMap']").live('dblclick',function(){
		linkToGis("#"+$(this).attr('id'));
	});
});
function linkToGis(id){
	var dis = $(id).attr("dis");
	var area = $(id).attr("area");
	var rt = $(id).attr("rt");
	var rl = $(id).attr("rl");
	var ri = $(id).attr("ri");
	$("#district_map").val(dis);
	$("#area_map").val(area);
	$("#roadType_map").val(rt);
	$("#roadLevel_map").val(rl);
	$("#roadId_map").val(ri);
	if($("#hasTime").val()!="-1"){
		if($("#kpiType_map").val()!=8){
			var querytime = $("#queryTime_page").val();
			var start = querytime.substring(0,10);
			var end = querytime.substring(13,23);
			start = (new Date(start.replace(/\./g,"/"))).getTime();
			end = (new Date(end.replace(/\./g,"/"))).getTime();
			if(end-start>=30*24*60*60*1000){
				$.messager.confirm('提示',"跳转地图查询时间超过30天,只会查询最后30天时间范围内的数据!",function(data){if(data){$("#linkMap").submit();}});
			}else{
				$("#linkMap").submit();
			}
		}else{
			$.messager.alert('提示',"覆盖率查询结果无法跳转!","error");
		}
	}else{
		$.messager.alert("跳转地图的时间不包含最近一个月内的时间!","warning");
	}
}