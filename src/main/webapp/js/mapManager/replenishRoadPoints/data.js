function ChildPoint(object){
	  this.method = BMap.Point;
	  this.method(object.bdLongitude,object.bdLatitude);//最关键的一行 
	  delete this.method;
	  this.object = object;
}


var shapes=[BMAP_POINT_SHAPE_CIRCLE,BMAP_POINT_SHAPE_STAR,BMAP_POINT_SHAPE_SQUARE,BMAP_POINT_SHAPE_RHOMBUS];
/**
 * 查询数据
 * @param vo
 */
function queyDate(vo){
	if(validataRoadName()){
		$.ajax({
		  url:contextPath + "/replenishrps/mapPoint",
		  type:"post",
		  data: {"vo":JSON.stringify(vo)},  
		  success:function(data){
		  	  //保存查询的道路编号和名称到隐藏域
			  $("#qry_roadId").val($("#roadId").val());
			  $("#qry_roadName").val($("#roadName").val());
			  //关闭遮挡层
			  $("#modal-backdrop").hide();
			  if(data.list.length>0){
				  addMarker(data.list);
			  }else{
				  $.messager.alert('提示','没有数据！',"warn");
			  }
			  if($("#qry_roadId").val()!="-1"&&$("#qry_roadName").val()!=""){
			  	  $("#mark_tool").show();
			  }
		  },
			error:function(data){
 				$("#modal-backdrop").hide();
 				$.messager.alert('提示','数据查询失败！',"error");
 			}
		  });
	}else{
		$("#modal-backdrop").hide();
	}
}

/***
 * 增加轨迹点
 * @param vo
 */
	function addMarker(list){
		var i_s=0;
		var pots = [];
		for (var i=0;i<list.length;i++) {
			pots.push(new ChildPoint(list[i]));
		}
		var options = {
			shape : BMAP_POINT_SHAPE_CIRCLE,
			size : BMAP_POINT_SIZE_SMALL,
			color : 'green'
		};
		var pointCollection = new BMap.PointCollection(pots, options);
		map.addOverlay(pointCollection);
		//添加点击事件
		pointCollection.addEventListener('click', function(e) {
			alert("道路点编号:"+e.point.object.uuid+"\n道路编号:"+e.point.object.roadId+"\n道路名称:"+e.point.object.roadName);
		});
		//在第一图层设置视野
        if(i_s==0){
        	 map.setViewport(pots);
		}
		i_s++;
		
	}

		
/**
 * 删除所有轨迹图层
 * @param vo
 */
function deleteOverlays(){
	map.clearOverlays();
}

/**
 * 保存已经标记好了的点
 * 
 */
 function saveMarks(list){
 	$("#modal-backdrop").show();
 	var roadId = $("#qry_roadId").val();
 	var roadName = $("#qry_roadName").val();
 	$.ajax({
		  url:contextPath + "/replenishrps/saveMarks",
		  type:"post",
		  data: {
		  	"list":JSON.stringify(list),
		  	"roadId": roadId,
		    "roadName": roadName
		  },  
		  success:function(){
		  	//清除已保存的标记点集合
		  	overlays.length = 0;
			$("#modal-backdrop").hide();
			$.messager.alert('提示','保存成功！',"info");
		  },
			error:function(){
 				$("#modal-backdrop").hide();
 				$.messager.alert('提示','保存标记点失败！',"error");
 			}
		  });
 }
 
 