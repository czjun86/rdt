var unique = 0;
$(function(){
	$(".addNewGroup").live('click',function(){
		var $modal = $('#ajax-modal');
	    $modal.load(contextPath + '/group/addNewGroup',function(){
	    	//初始化验证
			groupValidate.init();
			$(".saveGroup").click(function(){
				saveGroup();
			});
	      $modal.modal();
	    });
	});
	/*$(".getGroup").live('click',function(){
		var roleid = $(this).attr('groupid');
		var $modal = $('#test-modal');
		$modal.load(contextPath + '/group/grouplist',{"Content-type":"application/x-www-form-urlencoded",groupid:roleid},function(){
			$('#tt').tree({      
		        url: contextPath+"/group/getarea?groupid=2",  
		        loadFilter: function(data){     
		            return data;        
		        }       
		    });  
			$modal.modal();
			$(".sel_group").unbind('click').click(function(){
				var nodes = $('#groupdlg').tree('getChecked');
				var strtext = "";
				var	strids = "";
				//所有被完全选中的id
				if(nodes.length > 0){
					for(var i = 0; i < nodes.length; i++){
						strids += nodes[i].id + ",";
						strtext += nodes[i].text + ",";
					}
				}
				//获取半选中父节点id
				var nodes2 = $('#groupdlg').tree('getChecked','indeterminate');
				if(nodes2.length > 0){
					for(var i = 0; i < nodes2.length; i++){
						strids += nodes2[i].id + ",";
						strtext += nodes2[i].text + ",";
					}
				}

				if(strids !=null && strids != ""){
					strids = strids.substring(0,strids.length-1);
				}
				if(strtext !=null && strtext != ""){
					strtext = strtext.substring(0,strtext.length-1);
				}
				saveGroupIds(roleid,strids);
				$('#groupdlg').dialog('close');
			});
	    });
	});*/
	
	$(".deleteGroup").live('click',function(){
		var groupid = $(this).attr('groupid');
		$.ajax({
			url:contextPath + '/group/hasUser',
			type: "post",
			data: {
				groupid : groupid
			},success:function(data){
				if(data==1){
					$.messager.confirm('提示',"确定要算出该用户组吗？",function(data){
						if(data){deleteGroup(groupid);}});
				}else if(data==0){
					$.messager.alert('提示',"请先删除用户组下的用户，或修改这些用户所在用户组！","warning");
				}else{
					$.messager.alert('提示',"连接服务器失败！","error");
				}
			},
			error:function(){
				$.messager.alert('提示',"连接服务器失败！","error");
			}
		});
	});
	
	$(".getGroup").live('click',function(){
		var roleid = $(this).attr('groupid');
		var src = contextPath + '/group/grouplist?groupid='
				+ roleid;
		$("#groupdlg").dialog({
			href : src,
			height : 400,
			width : 380,
			title : "选择权限菜单",
			modal : true,
			closed : false
		});
		$('#groupdlg').dialog('open');
		$.parser.parse('#groupdlg');
		
		$(".sel_group").unbind('click').click(function(){
			var nodes = $('#groupdlg').tree('getChecked');
			var strtext = "";
			var	strids = "";
			//所有被完全选中的id
			if(nodes.length > 0){
				for(var i = 0; i < nodes.length; i++){
					strids += nodes[i].id + ",";
					strtext += nodes[i].text + ",";
				}
			}
			//获取半选中父节点id
			var nodes2 = $('#groupdlg').tree('getChecked','indeterminate');
			if(nodes2.length > 0){
				for(var i = 0; i < nodes2.length; i++){
					strids += nodes2[i].id + ",";
					strtext += nodes2[i].text + ",";
				}
			}

			if(strids !=null && strids != ""){
				strids = strids.substring(0,strids.length-1);
			}
			if(strtext !=null && strtext != ""){
				strtext = strtext.substring(0,strtext.length-1);
			}
			saveGroupIds(roleid,strids);
			$('#groupdlg').dialog('close');
		});
	});
});

//延迟设置时间后刷新页面
function reloadPage(url,ms){
	timeout1=setTimeout(function(){
		window.location.href=url;
	},ms);
}

function saveGroup(){
	$("#modal-backdrop").show();
	if(unique==0){
		unique=1;
		//判断是否通过验证
		if(!$("#addGroupForm").valid()){
			return false; //如果不通过则不提交
		}
		$.ajax({
			type:"post",
			url:contextPath +"/group/saveGroup",
			data:{
				groupName:$("#groupName").val()
			},
			success:function(data){
				unique = 0;
				$("#modal-backdrop").hide();
				if(data==1){
					$.messager.alert('提示',"保存成功","success",function(){
						$("#modal-backdrop").show();
						reloadPage(contextPath + '/group/groupManager?op_menu=15',100);});
				}else{
					$.messager.alert('提示',"操作失败！","error");
				}
			},
			error:function(){
				unique = 0;
				$("#modal-backdrop").hide();
				$.messager.alert('提示',"连接服务器失败！","error");
			}
		});
	}
}

function saveGroupIds(roleid,groupids){
	if(unique==0){
		unique++;
		$("#modal-backdrop").show();
		$.ajax({
			type:"post",
			url:contextPath +"/group/saveGroupIds",
			data:{
				roleid:roleid,
				groupids:groupids
			},
			success:function(data){
				$("#modal-backdrop").hide();
				unique = 0;
				if(data==1){
					$.messager.alert('提示',"保存成功","success");
				}else{
					$.messager.alert('提示',"操作失败！","error");
				}
			},
			error:function(){
				$("#modal-backdrop").hide();
				unique = 0;
				$.messager.alert('提示',"连接服务器失败！","error");
			}
		});
	}
}

function deleteGroup(groupid){
	if(unique==0){
		unique++;
		$.ajax({
			url:contextPath + '/group/deleteGroup',
			type: "post",
			data: {
				groupid : groupid
			},
			success:function(data){
				unique = 0;
				if(data==1){
					$.messager.alert('提示',"删除成功!","success",function(){reloadPage(contextPath + '/group/groupManager?op_menu=15',100);});
				}else{
					$.messager.alert('提示',"操作失败！","error");
				}
			},
			error:function(){
				unique = 0;
				$.messager.alert('提示',"连接服务器失败！","error");
			}
		});
	}
}