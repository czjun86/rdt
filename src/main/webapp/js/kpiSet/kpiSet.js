var minVal=[-130,-30,-30,0,0,0,0,0];
var maxVal=[0,30,0,150,1000,100,100,100];
var kpiName=['RSRP','SNR','RSRQ','下行链路带宽','延时','丢包','网络掉线率','业务掉线率'];
var kpiUnit=['dBm','dB','dB','Mbps','ms','%','%','%'];
$(function(){
	$("#kpiType").change(function(){
		$("#kpiType_query").val($(this).val());
		$("#kpiQuery").submit();
	});
	$("#updateBtn").live('click',function(){
		$("#modal-backdrop").show();
		var flag=false;
		var dt = {};
		dt["kpiType"] = $("#kpiType").val();
		dt["kpiGrapId"] = $("#kpiGrapId").val();
		for(var i=1;i<=6;i++){
			var val1 = $("#kpiRangVal"+i+"1").val();
			var val2 = $("#kpiRangVal"+i+"2").val();
			if(val1!=null && val1!="" && val2!=null && val2!=""){
				if(/^-?\d+$/.test(val1) && /^-?\d+?$/.test(val2)){
					if(parseFloat(val2)>=parseFloat(val1)){
						flag = true;
					}else{
						flag = false;
						break;
					}
				}else{
					flag = false;
					break;
				}
			}else{
				flag = false;
				break;
			}
			dt["kpiRangVal"+i+"1"] = val1;
			dt["kpiRangVal"+i+"2"] = val2;
			dt["kpiRangOpt"+i+"1"] = $("#kpiRangOpt"+i+"1").val();
			dt["kpiRangOpt"+i+"2"] = $("#kpiRangOpt"+i+"2").val();
			dt["kpiRangColor"+i] = $("#kpiRangColor"+i).val();
		}
		var id ;
		if($("#kpiType_page").val()==9){
			id = 6;
		}else if($("#kpiType_page").val() ==10){
			id = 7;
		}else{
			id = $("#kpiType_page").val()-1;
		}
		var min = minVal[id];
		var max = maxVal[id];
		var minKpi = $("#kpiRangVal11").val();
		var maxKpi = $("#kpiRangVal62").val();
		if(minKpi<min){
			$("#modal-backdrop").hide();
			$("#kpiRangVal11").parent('div').addClass("has-error");
			$.messager.alert('提示',kpiName[id]+"的最小值至少为"+min+kpiUnit[id],"error");
		}else if(maxKpi>max){
			$("#modal-backdrop").hide();
			$("#kpiRangVal62").parent('div').addClass("has-error");
			$.messager.alert('提示',kpiName[id]+"的最大值不超过"+max+kpiUnit[id],"error");
		}else if(flag){
			saveKpi(dt);
		}else{
			$("#modal-backdrop").hide();
			$.messager.alert('提示',"输入数据有误，请修改！","error");
		}
	});
});
function saveKpi(dt){
	$.ajax({
		type:"post",
		url:contextPath +"/kpiSet/update",
		data:dt,
		success:function(data){
			$("#modal-backdrop").hide();
			if(data.result==1){
				$.messager.alert('提示',"保存成功","success");
			}else{
				$.messager.alert('提示',"保存失败","error");
			}
		},
		error:function(data){
			$("#modal-backdrop").hide();
			$.messager.alert('提示',"保存失败","error");
		}
	});
}