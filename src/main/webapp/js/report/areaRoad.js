$(function() {
	/**
	 * 一级区域联动
	 */
	$("#province").live('change',function(){
		$("#roadName").val("");
		$("#roadName_v").val("");
		$("#roadId").val("-1");
		/*var hasAll = $('#roadId').attr("hasAll");*/
		var districtAll = $("#district").attr("hasAreaAll");
		var areaAll = $("#area").attr("hasAreaAll");
		var city = document.getElementById ("district");
		var area = document.getElementById ("area");
		/*var road = document.getElementById ("roadId");*/
		$('#district').find("option").remove();
		$('#area').find("option").remove();
		/*$('#roadId').find("option").remove();*/
		if(districtAll!=null){
			city.options.add(new Option('全部','-1'));
		}
		if(areaAll!=null){
			area.options.add(new Option('全部','-1')); 
		}
		/*if(hasAll!=null){
			road.options.add(new Option('全部','-1')); 
		}*/
		var roadArea = $(this).val().substring(0,2)+'%';
		$.ajax({
			type:"post",
			url:contextPath +"/area/searchNextRoad",
			data:{
				id:$(this).val(),
				type:2,
				roadType:$("#roadType").val(),
				roadLevel:$("#roadLevel").val(),
				roadArea:roadArea,
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
				/*var roads = data.roads;
				if(roads.length>0){ 
					for(var i=0;i<roads.length;i++){
						road.options.add(new Option(roads[i].text,roads[i].id)); 
					}
				}*/
				
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
				/*$("#roadLevel").select2("val", "-1");
				$("#roadType").select2("val", "-1");*/
				/*if(hasAll!=null){
					$("#roadId").select2("val", '-1');
				}else{
					if(roads.length>0){
						$("#roadId").select2("val", roads[0].id);
					}else{
						$('#roadId').select2(null);
					}
				}*/
			}
		});
	});
	
	/**
	 * 二级区域联动
	 */
	$("#district").change(function(){
		$("#roadName").val("");
		$("#roadName_v").val("");
		$("#roadId").val("-1");
		var id = $(this).val();
		/*var hasAll = $('#roadId').attr("hasAll");*/
		var areaAll = $("#area").attr("hasAreaAll");
		if(id != '-1'){
			var area = document.getElementById ("area");
			/*var road = document.getElementById ("roadId");*/
			$('#area').find("option").remove();
			/*$('#roadId').find("option").remove(); */
			if(areaAll!=null){
				area.options.add(new Option('全部','-1')); 
			}
			/*if(hasAll!=null){
				road.options.add(new Option('全部','-1')); 
			}*/
			var roadArea = $(this).val().substring(0,4)+'%';
			$.ajax({
				type:"post",
				url:contextPath +"/area/searchNextRoad",
				data:{
					id:$(this).val(),
					type:3,
					roadType:$("#roadType").val(),
					roadLevel:$("#roadLevel").val(),
					roadArea:roadArea,
				},
				success:function(data){
					var areas = data.areas;
					if(areas.length>0){
						for(var i=0;i<areas.length;i++){
							area.options.add(new Option(areas[i].text,areas[i].id)); 
						}
					}
					/*var roads = data.roads;
					if(roads.length>0){ 
						for(var i=0;i<roads.length;i++){
							road.options.add(new Option(roads[i].text,roads[i].id)); 
						}
					}*/
					if(areaAll!=null){
						$("#area").select2("val", '-1');
					}else{
						if(areas.length>0){
							$("#area").select2("val", areas[0].id);
						}
					}
					/*$("#roadLevel").select2("val", "-1");
					$("#roadType").select2("val", "-1");*/
					/*if(hasAll!=null){
						$("#roadId").select2("val", '-1');
					}else{
						if(roads.length>0){
							$("#roadId").select2("val", roads[0].id);
						}else{
							$('#roadId').select2(null);
						}
					}*/
				}
			});
		}else{
			var area = document.getElementById ("area");
			$('#area').find("option").remove();
			if(areaAll!=null){
				area.options.add(new Option('全部','-1')); 
				$("#area").select2("val", '-1');
			}
			/*$("#roadLevel").select2("val", "-1");
			$("#roadType").select2("val", "-1");*/
			
			/*var road = document.getElementById ("roadId");
			$('#roadId').find("option").remove(); 
			if(hasAll!=null){
				road.options.add(new Option('全部','-1')); 
			}*/
			
			/*var roadArea = $("#province").val().substring(0,2)+'%';
			$.ajax({
				type:"post",
				url:contextPath +"/area/searchNextRoad",
				data:{
					id:$("#province").val(),
					type:4,
					roadType:$("#roadType").val(),
					roadLevel:$("#roadLevel").val(),
					roadArea:roadArea,
				},
				success:function(data){
					var roads = data.roads;
					if(roads.length>0){ 
						for(var i=0;i<roads.length;i++){
							road.options.add(new Option(roads[i].text,roads[i].id)); 
						}
					}
					if(hasAll!=null){
						$("#roadId").select2("val", '-1');
					}else{
						if(roads.length>0){
							$("#roadId").select2("val", roads[0].id);
						}else{
							$('#roadId').select2(null);
						}
					}
				}
			});*/
		}
	});
	
	/**
	 * 三级区域联动
	 */
	$("#area").change(function(){
		$("#roadName").val("");
		$("#roadName_v").val("");
		$("#roadId").val("-1");
		/*var id = $(this).val();
		var hasAll = $('#roadId').attr("hasAll");
		if(id != '-1'){
			var road = document.getElementById ("roadId");
			$('#roadId').find("option").remove(); 
			if(hasAll!=null){
				road.options.add(new Option('全部','-1')); 
			}
			var roadArea = $(this).val();
			$.ajax({
				type:"post",
				url:contextPath +"/area/searchNextRoad",
				data:{
					id:$(this).val(),
					type:4,
					roadType:$("#roadType").val(),
					roadLevel:$("#roadLevel").val(),
					roadArea:roadArea,
				},
				success:function(data){
					var roads = data.roads;
					if(roads.length>0){ 
						for(var i=0;i<roads.length;i++){
							road.options.add(new Option(roads[i].text,roads[i].id)); 
						}
					}
					$("#roadLevel").select2("val", "-1");
					$("#roadType").select2("val", "-1");
					if(hasAll!=null){
						$("#roadId").select2("val", '-1');
					}else{
						if(roads.length>0){
							$("#roadId").select2("val", roads[0].id);
						}else{
							$('#roadId').select2(null);
						}
					}
				}
			});
		}else{
			var road = document.getElementById ("roadId");
			$('#roadId').find("option").remove(); 
			if(hasAll!=null){
				road.options.add(new Option('全部','-1')); 
			}
			var roadArea = $("#district").val().substring(0,4)+'%';
			$.ajax({
				type:"post",
				url:contextPath +"/area/searchNextRoad",
				data:{
					id:roadArea,
					type:4,
					roadType:-1,
					roadLevel:-1,
					roadArea:roadArea,
				},
				success:function(data){
					var roads = data.roads;
					if(roads.length>0){ 
						for(var i=0;i<roads.length;i++){
							road.options.add(new Option(roads[i].text,roads[i].id)); 
						}
					}
					$("#roadId").select2("val", '-1');
					$("#roadLevel").select2("val", "-1");
					if(hasAll!=null){
						$("#roadType").select2("val", "-1");
					}else{
						if(roads.length>0){
							$("#roadId").select2("val", roads[0].id);
						}else{
							$('#roadId').select2(null);
						}
					}
				}
			});
		}*/
	});
	
	/**
	 * 道路等级联动
	 */
	$("#roadLevel").live('change',function(){
		$("#roadName").val("");
		$("#roadName_v").val("");
		$("#roadId").val("-1");
		//roadLinkage();
	});
	/**
	 * 道路类型联动
	 */
	$("#roadType").live('change',function(){
		$("#roadName").val("");
		$("#roadName_v").val("");
		$("#roadId").val("-1");
		//roadLinkage();
	});
});

function roadLinkage(){
	var hasAll = $('#roadId').attr("hasAll");
	var road = document.getElementById ("roadId");
	$('#roadId').find("option").remove();
	if(hasAll!=null){
		road.options.add(new Option('全部','-1')); 
	}
	//区域
	var country = $("#province").val();
	var city = $("#district").val();
	var area = $("#area").val();
	//道路
	var roadType = $("#roadType").val();
	var roadLevel = $("#roadLevel").val();
	var roadArea;
	if(area == "-1"){
		if(city == "-1"){
			roadArea = country.substring(0,2)+'%';
		}else{
			roadArea = city.substring(0,4)+'%';
		}
	}else{
		roadArea = area;
	}
	var id = country;
	$.ajax({
		type:"post",
		url:contextPath +"/area/searchNextRoad",
		data:{
			id:id,
			type:4,
			roadType:roadType,
			roadLevel:roadLevel,
			roadArea:roadArea,
		},
		success:function(data){
			var roads = data.roads;
			if(roads.length>0){ 
				for(var i=0;i<roads.length;i++){
					road.options.add(new Option(roads[i].text,roads[i].id)); 
				}
			}
			if(hasAll!=null){
				$("#roadId").select2("val", '-1');
			}else{
				if(roads.length>0){
					$("#roadId").select2("val", roads[0].id);
				}else{
					$('#roadId').select2(null);
				}
			}
		}
	});
}