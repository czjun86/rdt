$(function(){
	$("#querybtn").click(function(){
		if(getCondition()&&validataRoadName()){
			$("#modal-backdrop").show();
			$("#pageForm").submit();
		}
	});
	$("#leadExcel").click(function(){
		var uuid = $("#uuid_page").val();
		if(uuid!=null && uuid!=""){
			$("#modal-backdrop").show();
			$.ajax({
				type:"post",
				url:contextPath + "/epinfoAnalysis/export",
				data:{
					uuid:uuid,
					threshold:$("#threshold_page").val(),
					
					rsrp:$("#rsrp_page").val(),
					rsrq:$("#rsrq_page").val(),
					snr:$("#snr_page").val(),
					broadband:$("#broadband_page").val(),
					delay:$("#delay_page").val(),
					lose:$("#lose_page").val(),
					
					//取查询时间值
					timegransel:$("#timegransel_page").val(),
					queryTime:$("#queryTime_page").val(),
					//获取运营商
					operator:$("#operator_page").val(),
					//区域
					province:$("#province_page").val(),
					district:$("#district_page").val(),
					area:$("#area_page").val(),
					//道路
					roadType:$("#roadType_page").val(),
					roadLevel:$("#roadLevel_page").val(),
					roadId:$("#roadId_page").val(),
					roadName:$("#roadName_page").val(),
					
					showTime:$("#showTime_page").val(),
					showArea:$("#showArea_page").val(),
					showRoad:$("#showRoad_page").val(),
					showOperator:$("#showOperator_page").val(),
					showInterval:$("#showInterval_page").val()
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
	$("#showInterval").change(function(){
		if($("#showInterval").is(":checked")){
			$("input[name='kpiInvl']:checkbox").each(function(){
				$(this).attr("checked",true);
				$(this).parents('.checkbox').find('span').addClass('checked');
			});
		}else{
			$("input[name='kpiInvl']:checkbox").each(function(){
				$(this).attr("checked",false);
				$(this).parents('.checkbox').find('span').removeClass('checked');
			});
		}
	});
	$("input[name='kpiInvl']").change(function(){
		var flag = false;
		$("input[name='kpiInvl']:checkbox").each(function(){
			if( $(this).is(":checked")){
				flag = true;
			}
		});
		if(flag){
			$("#showInterval").attr('checked', true);
			$("#showInterval").parents('span').addClass('checked');
		}else{
			$("#showInterval").attr('checked', false);
			$("#showInterval").parents('span').removeClass('checked');
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
	var oper = $(id).attr("oper");//当前行内容返回运营商
	var paramOper = $("#operator_page").val();//选择的运营商
	var showArea = $("#showArea_page").val();
	var showRoad = $("#showRoad_page").val();
	if(showArea!=1){
		$("#district_map").val($("#district_page").val());
		$("#area_map").val($("#area_page").val());
	}else{
		$("#district_map").val(dis!=null&&dis!=""?dis:-1);
		$("#area_map").val(area!=null&&area!=""?area:-1);
	}
	if(showRoad!=1){
		$("#roadType_map").val($("#roadType_page").val());
		$("#roadLevel_map").val($("#roadLevel_page").val());
		$("#roadId_map").val($("#roadId_page").val());
	}else{
		$("#roadType_map").val(rt!=null&&rt!=""?rt:-1);
		$("#roadLevel_map").val(rl!=null&&rl!=""?rl:-1);
		$("#roadId_map").val(ri!=null&&ri!=""?ri:-1);
	}
	var flagOper = 0;
	if(oper != '-1'  && oper != 'N/A'){
		if(oper == '联通'){
			oper = 1;
			flagOper = 1;
		}else if(oper == '移动'){
			oper = 2;
			flagOper = 1;
		}else if(oper == '电信'){
			oper = 3;
			flagOper = 1;
		}else{
			oper = -1;
			flagOper = 0;
		}
	}else if(paramOper!="" && paramOper!=null && paramOper!='111'){
		oper = paramOper;
		flagOper = 1
	}
	
	$("#operator_map").val(oper);
	var imei = $("#imei_page").val();
	$("#imei_map").val(imei);
	
	if($("#showTime_map").val()!=1){
		$.messager.alert('提示',"没勾选\"时间维度\"查询无法跳转!","warning");
	}else if(ri==-1){
		$.messager.alert('提示',"没勾选\"道路维度\"查询无法跳转!","warning");
	}else if(flagOper == 0){
		$.messager.alert('提示',"该数据为多个运营商综合统计结果无法跳转!","warning");
	}else if(imei==""||imei==null||imei=="-1"){
		$.messager.alert('提示',"没有对应IMEI无法跳转!","warning");
	}else{
		var st = $(id).attr("st");
		var ed = $(id).attr("ed");
		$("#starttime_map").val(st!=null&&rl!=""?st.replace(/-/g,"."):-1);
		$("#endtime_map").val(ed!=null&&ri!=""?ed.replace(/-/g,"."):-1);
		$("#linkMap").submit();
	}
}

var inval=['rsrp','rsrq','snr','broadband','delay','lose'];
var dim=['showTime','showArea','showRoad','showOperator','showInterval'];

//点击按钮，重新查询，修改查询条件
function getCondition(){
	//页码回复默认
	$("#pageSize").val(null);
	$("#pageIndex").val(null);
	//uuid清空
	$("#uuid_page").val(null);
	//取查询时间值
	$("#timegransel_page").val($("#timegransel").val());
	$("#queryTime_page").val($("#queryTime").val());
	//获取运营商
	$("#operator_page").val($("#operator").val());
	//区域
	$("#province_page").val($("#province").val());
	$("#district_page").val($("#district").val());
	$("#area_page").val($("#area").val());
	//道路
	$("#roadType_page").val($("#roadType").val());
	$("#roadLevel_page").val($("#roadLevel").val());
	$("#roadId_page").val($("#roadId").val());
	$("#roadName_page").val($("#roadName").val());
	//imei
	$("#imei_page").val($("#imei").val());
	$("#threshold_page").val(parseInt($("#threshold").val()!=null&&$("#threshold").val()!=""?$("#threshold").val():0))
	//指标
	var str="";
    $("input[name='kpiInvl']:checkbox").each(function(){ 
        if($(this).attr("checked")){
            str += $(this).val()+","
        }
    })
    if(str.length>0){
    	var kpi = (str.substring(0,str.length-1)).split(",");
    	for(var i=0;i<inval.length;i++){
    		var flag = false;
    		
    		for(var j=0;j<kpi.length;j++){
    			if(inval[i] == kpi[j]){
    				flag = true;
    				break;
    			}
    		}
    		
    		if(flag){
    			$("#"+inval[i]+"_page").val(1);
    		}else{
    			$("#"+inval[i]+"_page").val(0);
    		}
    	}
    }else{
    	for(var i=0;i<inval.length;i++){
    		$("#"+inval[i]+"_page").val(0);
    	}
    }
    
    //维度
    var dimension="";
    $("input[name='dimension']:checkbox").each(function(){ 
        if($(this).attr("checked")){
        	dimension += $(this).val()+","
        }
    })
    if(dimension.length>0){
    	var ds = (dimension.substring(0,dimension.length-1)).split(",");
    	for(var i=0;i<dim.length;i++){
    		var flag = false;
    		
    		for(var j=0;j<ds.length;j++){
    			if(dim[i] == ds[j]){
    				flag = true;
    				break;
    			}
    		}
    		
    		if(flag){
    			$("#"+dim[i]+"_page").val(1);
    		}else{
    			$("#"+dim[i]+"_page").val(0);
    		}
    	}
    }else{
    	for(var i=0;i<dim.length;i++){
    		$("#"+dim[i]+"_page").val(0);
    	}
    }
    return true;
}