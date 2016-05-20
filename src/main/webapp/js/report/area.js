$(function() {
	$("#areaText").click(function() {
		var src = contextPath + '/area/arealist?areaids=' + $("#areaid").val();
		$("#areadlg").dialog({
			href : src,
			height : 400,
			width : 380,
			title : "选择区域",
			modal : true,
			closed : false
		});
		$('#areadlg').dialog('open');
		$.parser.parse('#areadlg');
	});

	/**
	 * 点击确定,将区域值赋值给父页面hidden
	 */
	$(".sel_area").unbind('click').click(function() {
		var nodes = $('#areadlg').tree('getChecked');
		var areaids = "";
		var areatext = "";
		if (nodes.length > 0) {
			for (var i = 0; i < nodes.length; i++) {
				if (nodes[i].id != -1) {
					areaids += nodes[i].id + ",";
					areatext += nodes[i].text + ",";
				} else {
					areaids = "-1" + ",";
					areatext = "全部" + ",";
					break;
				}
			}
			areaids = areaids.substring(0, areaids.length - 1);
			areatext = areatext.substring(0, areatext.length - 1);
		} else {
			areaids = "-1";
			areatext = "全部";
		}
		$("#areaText").val(areatext);
		$("#areaid").val(areaids);
		$('#areadlg').dialog('close');
	});
	
	/**
	 * 一级区域联动
	 */
	$("#province").live('change',function(){
		var districtAll = $("#district").attr("hasAreaAll");
		var areaAll = $("#area").attr("hasAreaAll");
		var city = document.getElementById ("district");
		var area = document.getElementById ("area");
		$('#district').find("option").remove();
		$('#area').find("option").remove();
		if(districtAll!=null){
			city.options.add(new Option('全部','-1'));
		}
		if(areaAll!=null){
			area.options.add(new Option('全部','-1')); 
		}
		$.ajax({
			type:"post",
			url:contextPath +"/area/searchNext",
			data:{
				id:$(this).val(),
				type:2
			},
			success:function(data){
				var citys = data.citys;
				if(citys.length>0){ 
					for(var i=0;i<citys.length;i++){
						city.options.add(new Option(citys[i].text,citys[i].id)); 
					}
				}
				
				var areas = data.areas;
				if(areas.length>0){
					for(var i=0;i<areas.length;i++){
						area.options.add(new Option(areas[i].text,areas[i].id)); 
					}
				}
				
				if(districtAll!=null){
					$("#district").select2("val", '-1');
				}else{
					if(citys.length>0){
						$("#district").select2("val", citys[0].id);
					}
				}
				if(areaAll!=null){
					$("#area").select2("val", '-1');
				}else{
					if(areas.length>0){
						$("#area").select2("val", areas[0].id);
					}
				}
			}
		});
	});
	
	/**
	 * 二级区域联动
	 */
	$("#district").change(function(){
		var areaAll = $("#area").attr("hasAreaAll");
		var id = $(this).val();
		if(id != '-1'){
			var area = document.getElementById ("area");
			$('#area').find("option").remove();
			if(areaAll!=null){
				area.options.add(new Option('全部','-1')); 
			}
			$.ajax({
				type:"post",
				url:contextPath +"/area/searchNext",
				data:{
					id:$(this).val(),
					type:3
				},
				success:function(data){
					var areas = data.areas;
					if(areas.length>0){
						for(var i=0;i<areas.length;i++){
							area.options.add(new Option(areas[i].text,areas[i].id)); 
						}
					}
					if(areaAll!=null){
						$("#area").select2("val", '-1');
					}else{
						if(areas.length>0){
							$("#area").select2("val", areas[0].id);
						}
					}
				}
			});
		}else{
			var area = document.getElementById ("area");
			$('#area').find("option").remove();
			if(areaAll!=null){
				area.options.add(new Option('全部','-1')); 
				$("#area").select2("val", '-1');
			}
		}
	});
});