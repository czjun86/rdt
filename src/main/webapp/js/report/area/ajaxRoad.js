$(function(){
	$( "#roadName" ).focus().autocomplete({
	      source: function( request, response ) {
	    	  //如果重新查询列表信息，先比对是否有修改
	    	  if($("#roadName").val()!=$("#roadName_v").val()){
	    		  $("#roadId").val("-1");
	    	  }else if($("#roadName").val()==""){
	    		  $("#roadId").val("-1");
	    	  }
	        $.ajax({
	          url: contextPath+'/area/searchRoad',
	          dataType: "json",
	          data: {
      			pid:$("#province").val(),
      			did:$("#district").val(),
      			aid:$("#area").val(),
      			roadType:$("#roadType").val(),
      			roadLevel:$("#roadLevel").val(),
      			roadArea:$("#roadName").val()
	          },
	          success: function( data ) {
                  response( $.map( data.roads, function( item ) {
                    return {
                      label: item.text,
                      value: item.text,
                      value2: item.id
                    }
                  }));
	          }
	        });
	      },
	      minLength: 0,
	      select: function( event, ui ) {
	    	//$("#roadName").val(ui.item.value);
	    	$("#roadId").val(ui.item.value2);
	    	$("#roadName_v").val(ui.item.value);
	      },
	      open: function() {
	        $( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
	      },
	      close: function() {
	        $( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
	      }
	    });
	$('#container').live('mousedown',function(){
		$('#roadName').autocomplete('close', document.getElementById('roadName') );
	});
});

//提交前验证道路名称是否是选中的
function validataRoadName(){
	if($("#roadName").val()!=$("#roadName_v").val() && $("#roadName").val()!=""){
		  $.messager.alert('提示',"为保证数据准确性,\"道路名称\"请务必在下拉提示中选择一项,谢谢合作","error");
		  return false;
	 }else if($("#roadName").attr("nonull")=="1" && $("#roadName").val()==""){//nonull : 1不能查全部 ，0能查全部
		 $.messager.alert('提示',"该页面查询需要选择一条道路","error");
		  return false;
	 }else if($("#roadName").val()==""){
		  $("#roadId").val("-1");
		  $("#roadName_v").val($("#roadName").val());
		  return true;
	  }
	 return true;
}