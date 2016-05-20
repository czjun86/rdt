$(function(){
	$("#add").click(function(){
		openModal(-1);
	});
	$(".deleteCollection").live('click',function(){
		var id = $(this).attr('nid');
		var name = $(this).attr('nname');
		deleteCollection(id,name);
	});
	$(".editCollection").live('click',function(){
		var id = $(this).attr('nid');
		var name = $(this).attr('nname');
		openModal(id);
	});
});
//防并发导致开关按钮多次初始化
var openTime = 0;
var concurrency = 0;
function openModal(id){
	var thisTime = new Date().getTime();
	if(thisTime-openTime>1000 && concurrency==0){
		concurrency++;
		var $modal = $('#ajax-modal');
		$modal.empty();
	    $modal.load(contextPath + '/collection/openModal',{"Content-type":"application/x-www-form-urlencoded",id:id},function(){
	    	//初始化验证
	    	formValidate.init();
	    	$(".saveChild").live('click',function(){
	    		saveChangeCollection();
	    	});
	    	$modal.modal();
	    	concurrency = 0;
	    	openTime = new Date().getTime();
	    });
	}
}
var unique= 0;
function saveChangeCollection(){
	if(unique==0){//防止快速多次提交
		unique++;
		if(isCompleteCollection()){
			if(!$("#saveChild").valid()){
				unique = 0;
				return false; //如果不通过则不提交
			}
			var name=$("#name").val();
			var mark=$("#mark").val();
			var isCollect = 0;
			if($("#isCollect").is(':checked')){
				isCollect=1;
			}else{
				isCollect=0;
			}
			var fdl = 0;
			if($("#fdl").is(':checked')){
				fdl=1;
			}else{
				fdl=0;
			}
			var url = $("#url").val();
			try {
				$.ajax({
					type : "post",
					url : contextPath + "/collection/save",
					data : {
						id : $("#id").val(),
						name : name,
						mark : mark,
						isCollect : isCollect,
						fdl : fdl,
						url : url,
						tcpport : $("#tcpport").val(),
						udpport : $("#udpport").val(),
						packnum : $("#packnum").val(),
						packsize : $("#packsize").val(),
	
						start1 : $("input[name='start1']").val(),
						end1 : $("input[name='end1']").val(),
						wireless1 : $("input[name='wireless1']").val(),
						app1 : $("input[name='app1']").val(),
	
						start2 : $("input[name='start2']").val(),
						end2 : $("input[name='end2']").val(),
						wireless2 : $("input[name='wireless2']").val(),
						app2 : $("input[name='app2']").val(),
	
						start3 : $("input[name='start3']").val(),
						end3 : $("input[name='end3']").val(),
						wireless3 : $("input[name='wireless3']").val(),
						app3 : $("input[name='app3']").val(),
	
						start4 : $("input[name='start4']").val(),
						end4 : $("input[name='end4']").val(),
						wireless4 : $("input[name='wireless4']").val(),
						app4 : $("input[name='app4']").val(),
	
						start5 : $("input[name='start5']").val(),
						end5 : $("input[name='end5']").val(),
						wireless5 : $("input[name='wireless5']").val(),
						app5 : $("input[name='app5']").val()
					},
					success : function(data) {
						unique = 0;
						if (data.msg == 1) {
							$.messager.alert('提示', "保存成功", "success", function() {
								$(".default").click();
								$("#modal-backdrop").show();
								timeout1 = setTimeout(function() {
									$("#pageForm").submit();
								}, 100);
							});
						} else {
							$.messager.alert('提示', "操作失败！", "error");
						}
					},
					error : function() {
						unique = 0;
						$.messager.alert('提示', "连接服务器失败！", "error");
					}
				});
			} catch (e) {
				unique = 0;
			}
		}
	}
}

/**
 * 删除
 * @param id
 * @param name
 */
function deleteCollection(id,name){
	$.messager.confirm('提示',"是否删除"+name+"方案?",function(data){
		if(data){
			$.ajax({
				type:"post",
				url:contextPath +"/collection/delete",
				data:{
					id:id
				},
				success:function(data){
					if(data==1){
						unique = 0;
						$.messager.alert('提示',"删除成功","success",function(){
							$("#modal-backdrop").show();
							timeout1=setTimeout(function(){
								pageForm.submit();
							},100);
						});
					}else if(data==2){
						unique = 0;
						$.messager.alert('提示',"该方案正在使用无法删除！","error");
					}else{
						unique = 0;
						$.messager.alert('提示',"操作失败！","error");
					}
				},
				error:function(){
					unique = 0;
					$.messager.alert('提示',"连接服务器失败！","error");
				}
			});
		}
	});
}

/**
 * 修改方案
 * @param id
 */
function updateCollection(id){
	openModal(id);
}

function isCompleteCollection(){
	var name = ['#start','#end','#wireless','#app'];
	var str = "";
	for(var i=1; i<=5 ;i++){
		var val1 = $(name[0]+i).val();
		var val2 = $(name[1]+i).val();
		var val3 = $(name[2]+i).val();
		var val4 = $(name[3]+i).val();
		if(//每一行是否有填写数字
			(val1!=null && val1!="")||
			(val2!=null && val2!="")||
			(val3!=null && val3!="")||
			(val4!=null && val4!="")
		){
			if(//只要配置了一个数字，其他数字也需要配置，并且周期要大于0
				!(
					(val1!=null && val1!="")&&
					(val2!=null && val2!="")&&
					(val3!=null && val3!="" && parseInt(val3)>0)&&
					(val4!=null && val4!="" && parseInt(val4)>0)
				)
			){
				str = "采集信息未填写完整!";
				break;
			}
		}
		//判断时间小于24小时
		if((val1!=null && val1!="")&&(val2!=null && val2!="")){
			if(parseInt(val1)>24 || parseInt(val2)>24){
				str = "时间时间段大于了24小时!";
				break;
			}else{
				//判断周期在时间段内
				var time = (parseInt(val2)-parseInt(val1))*60*60
				if(parseInt(val4)>time || parseInt(val3)>time){
					str = "测试周期大于了测试时间段!";
					break;
				}
			}
		}
	}
	if(str != ""){
		unique = 0;
		$.messager.alert('提示', str, "warning");
		return false;
	}else{
		return true;
	}
}