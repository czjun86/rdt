var operatorId= ["1","2","3"];
var operatorName= ["联通","移动","电信"];
function initSelect(){
	//运营商
	var operator = $("#operator_initSelect").val();
	var datas = [];
	for(var i=0; i<operatorId.length; i++){
		var dt = new Object();
		dt.id = operatorId[i];
		dt.text = operatorName[i];
		if(operator == operatorId[i]){
			dt.selected = true;
		}else{
			if(operator == null || operator == ""){
				dt.selected = true;
			}
		}
		datas[i] = dt;
	}
	$("#operator").select2({data:datas});
	
	$.ajax({
		type:"post",
		url:contextPath +"/operator/selectData",
		data:{
			userid:($("#userid").val()==null||$("#userid").val()==""?null:$("#userid").val()),
		},
		success:function(data){
			createSelect2("roleid",data.groups,data.user.roleid);
			createSelect2("province",data.provinces,data.user.province);
			createSelect2("district",data.districts,data.user.district);
			createSelect2("county",data.countys,data.user.county);
		}
	});
}

function createSelect2(id,datas,myId){
	for(var i=0; i<datas.length; i++){
		var dt = new Object();
		dt.id = datas[i].id;
		dt.text = datas[i].text;
		if(myId == datas[i]){
			dt.selected = true;
		}else{
			if(myId == null || myId == ""){
				dt.selected = true;
			}
		}
		datas[i] = dt;
	}
	$("#"+id).select2({data:datas});
}