$(function(){
	//绑定查询按钮
	$("#querybtn").click(function(){
		$("#modal-backdrop").show();
		//页码回复默认
		$("#pageSize").val(null);
		$("#pageIndex").val(null);
		//取查询时间值
		$("#queryTime_page").val($("#queryTime").val());
		//获取任务名称
		$("#taskName_page").val($("#taskName").val());
		$("#pageForm").submit();
	});
	
	
});

function exportData(rsrp,rsrq,snr,broadband,delay,lose,
					timegransel,queryTime,operator,province,district,
					area,roadType,roadLevel,roadId,roadName,showTime,
					showArea,showRoad,showOperator,showInterval,uuid){
	$("#modal-backdrop").show();
	$.ajax({
		type:"post",
		url:contextPath + "/compreQuota/export",
		data:{
			uuid:uuid,
			threshold:12,//被写死了！
			rsrp:rsrp,
			rsrq:rsrq,
			snr:snr,
			broadband:broadband,
			delay:delay,
			lose:lose,
			//取查询时间值
			timegransel:timegransel,
			queryTime:queryTime,
			//获取运营商
			operator:operator,
			//区域
			province:province,
			district:district,
			area:area,
			//道路
			roadType:roadType,
			roadLevel:roadLevel,
			roadId:roadId,
			roadName:roadName,
			
			showTime:showTime,
			showArea:showArea,
			showRoad:showRoad,
			showOperator:showOperator,
			showInterval:showInterval
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
	
}
	
	function showQuery(rsrp,rsrq,snr,broadband,delay,lose,
					timegransel,queryTime,operatorName,provinceName,districtName,
					areaName,roadType,roadLevel,roadId,roadName,showTime,
					showArea,showRoad,showOperator,showInterval){
		//设置内容
		$("#dialog_div").text("");
		var str = "";
		if(timegransel!=null){
			str=str+"时间粒度:  <b>";
			timegransel==1?str=str+'天':timegransel==2?str=str+'周':timegransel==3?str=str+'月':str=str+'';
			str=str+"</b></br>";
		}
		if(queryTime!=null){
			str=str+"时间范围:  <b>"+queryTime+"</b><br>";
		}
		if(operatorName!=null){
			str=str+"运营商:  <b>"+operatorName+"</b><br>";
		}
		if(provinceName!=null){
			str=str+"一级区域:  <b>"+provinceName+"</b><br>";
		}
		if(districtName!=null){
			str=str+"二级区域:  <b>"+districtName+"</b><br>";
		}
		if(areaName!=null){
			str=str+"三级区域:  <b>"+areaName+"</b><br>";
		}
		if(roadType!=null){
			str=str+"道路类型:  <b>";
			roadType==1?str=str+'道路':roadType==2?str=str+'隧道':roadType==3?str=str+'桥梁':roadType==-1?str=str+'全部':str=str+'';
			str=str+"</b></br>";
		}
		if(roadLevel!=null){
			str=str+"道路等级:  <b>";
			roadLevel==1?str=str+'主干路':roadLevel==2?str=str+'次干路':roadLevel==3?str=str+'高速公路'
			:roadLevel==4?str=str+'国道':roadLevel==5?str=str+'省道':roadLevel==6?str=str+'县道'
			:roadLevel==7?str=str+'乡村道路':roadLevel==-1?str=str+'全部':str=str+'';
			str=str+"</b></br>";
		}
		if(roadId!=null){
			if(roadId!="-1"){
				str=str+"道路编号:  <b>"+roadId+"</b><br>";
			}else{
				str=str+"道路编号:<br>";
			}
		}
		if(roadName!=null){
			str=str+"道路名称:  <b>"+roadName+"</b><br>";
		}
		
		//指标选择
		str=str+"指标选择:  <b>";
		var tep="";
		if(rsrp==1){
			tep=tep+"RSCP、";
		}
		if(rsrq==1){
			tep=tep+"RSCQ、";
		}
		if(snr==1){
			tep=tep+"SNR、";
		}
		if(broadband==1){
			tep=tep+"下行链路带宽、";
		}
		if(delay==1){
			tep=tep+"延时、";
		}
		if(lose==1){
			tep=tep+"丢包、";
		}
		str=str+tep.substring(0,tep.length-1);
		str=str+"</b><br>";
		
		//指标纬度
		str=str+"指标纬度:  <b>";
		tep="";
		if(showTime==1){
			tep=tep+"时间、";
		}
		if(showArea==1){
			tep=tep+"区域、";
		}
		if(showRoad==1){
			tep=tep+"道路、";
		}
		if(showOperator==1){
			tep=tep+"运营商、";
		}
		if(showInterval==1){
			tep=tep+"指标区间、";
		}
		str=str+tep.substring(0,tep.length-1);
		str=str+"</b><br>";
		
		$("#dialog_div").append(str);
		$('#dialog_div').dialog({
                 title: '任务查询条件',   
		         width: 400,   
		         height: 260,   
		         closed: false,   
		         cache: false,   
		         modal: true
         });
				
	      $(".window-shadow").hide();//隐藏阴影
	}
