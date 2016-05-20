$(function() {
	/**
	 * 一级区域联动
	 */
	$("#province").live('change',function(){
		var hasAll = $('#roadId').attr("hasAll");
		var city = document.getElementById ("district");
		var area = document.getElementById ("area");
		var road = document.getElementById ("roadId");
		$('#district').find("option").remove();
		$('#area').find("option").remove();
		$('#roadId').find("option").remove();
		city.options.add(new Option('全部','-1'));
		area.options.add(new Option('全部','-1')); 
		if(hasAll!=null){
			road.options.add(new Option('全部','-1')); 
		}
		var roadArea = $(this).val().substring(0,2)+'%';
		$.ajax({
			type:"post",
			url:contextPath +"/area/searchNextNoAuthorityRoad",
			data:{
				id:$(this).val(),
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
				var roads = data.roads;
				if(roads.length>0){ 
					for(var i=0;i<roads.length;i++){
						road.options.add(new Option(roads[i].text,roads[i].id)); 
					}
				}
				
				$("#district").select2("val", '-1');
				$("#area").select2("val", '-1');
				/*$("#roadLevel").select2("val", "-1");
				$("#roadType").select2("val", "-1");*/
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
	});
	
	/**
	 * 二级区域联动
	 */
	$("#district").change(function(){
		var id = $(this).val();
		var hasAll = $('#roadId').attr("hasAll");
		if(id != '-1'){
			var area = document.getElementById ("area");
			var road = document.getElementById ("roadId");
			$('#area').find("option").remove();
			$('#roadId').find("option").remove(); 
			area.options.add(new Option('全部','-1')); 
			if(hasAll!=null){
				road.options.add(new Option('全部','-1')); 
			}
			var roadArea = $(this).val().substring(0,4)+'%';
			$.ajax({
				type:"post",
				url:contextPath +"/area/searchNextNoAuthorityRoad",
				data:{
					id:$(this).val(),
					roadType:$("#roadType").val(),
					roadLevel:$("#roadLevel").val(),
					roadArea:roadArea,
				},
				success:function(data){
					var areas = data.citys;
					if(areas.length>0){
						for(var i=0;i<areas.length;i++){
							area.options.add(new Option(areas[i].text,areas[i].id)); 
						}
					}
					var roads = data.roads;
					if(roads.length>0){ 
						for(var i=0;i<roads.length;i++){
							road.options.add(new Option(roads[i].text,roads[i].id)); 
						}
					}
					$("#area").select2("val", '-1');
					/*$("#roadLevel").select2("val", "-1");
					$("#roadType").select2("val", "-1");*/
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
			var area = document.getElementById ("area");
			$('#area').find("option").remove();
			area.options.add(new Option('全部','-1')); 
			$("#area").select2("val", '-1');
			/*$("#roadLevel").select2("val", "-1");
			$("#roadType").select2("val", "-1");*/
			
			var road = document.getElementById ("roadId");
			$('#roadId').find("option").remove(); 
			if(hasAll!=null){
				road.options.add(new Option('全部','-1')); 
			}
			
			var roadArea = $("#province").val().substring(0,2)+'%';
			$.ajax({
				type:"post",
				url:contextPath +"/area/searchNextNoAuthorityRoad",
				data:{
					id:$(this).val(),
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
			});
		}
	});
	
	/**
	 * 三级区域联动
	 */
	$("#area").change(function(){
		var id = $(this).val();
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
				url:contextPath +"/area/searchNextNoAuthorityRoad",
				data:{
					id:$(this).val(),
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
					/*$("#roadLevel").select2("val", "-1");
					$("#roadType").select2("val", "-1");*/
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
				url:contextPath +"/area/searchNextNoAuthorityRoad",
				data:{
					id:roadArea,
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
		}
	});
	
	/**
	 * 道路等级联动
	 */
	$("#roadLevel").live('change',function(){
		roadLinkage();
	});
	/**
	 * 道路类型联动
	 */
	$("#roadType").live('change',function(){
		roadLinkage();
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
		url:contextPath +"/area/searchNextNoAuthorityRoad",
		data:{
			id:id,
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