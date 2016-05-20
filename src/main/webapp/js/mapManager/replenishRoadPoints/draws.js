 var overlays = [];//覆盖物数组

/**
 * 清除所有已标记点
 */
function clearAll() {
	for(var i = 0; i < overlays.length; i++){
        map.removeOverlay(overlays[i]);
    }
    overlays.length = 0;
}

/**
* 鼠标点击的事件
*/
//var myIcon = new BMap.Icon("markers.png", new BMap.Size(23, 25), {    
		// 指定定位位置。   
		// 当标注显示在地图上时，其所指向的地理位置距离图标左上    
		// 角各偏移10像素和25像素。您可以看到在本例中该位置即是   
		   // 图标中央下端的尖角位置。    
		   //offset: new BMap.Size(10, 25),    
		   // 设置图片偏移。   
		   // 当您需要从一幅较大的图片中截取某部分作为标注图标时，您   
		   // 需要指定大图的偏移位置，此做法与css sprites技术类似。    
		   //imageOffset: new BMap.Size(0, 0)   // 设置图片偏移    
		// });

    var clickAction = function (e) {
    // 创建标注对象并添加到地图   
	// var marker = new BMap.Marker(e.point, {icon: myIcon});    
	 var marker = new BMap.Marker(e.point);
	 //调用逆地址纠偏接口获取道路信息
	 qryApi(e.point.lng,e.point.lat);
	 marker.enableDragging();//允许拖拽
	 marker.addEventListener("dragend", function(e){    
 		this.setPosition(e.point);
 		//调用逆地址纠偏接口获取道路信息
	    qryApi(e.point.lng,e.point.lat);
	});
	 
	 //右键清除此标记
	 marker.addEventListener("rightclick", function(){
		map.removeOverlay(this);
		//同时删除保存数组
		var index = -1;
		for(var i=0;i<overlays.length;i++){
			if(overlays[i]==this){
				index = i;
				break;
			}
		}
		if(index !=-1){
			overlays.splice(index,1);//删除这个点
		}
	});
	 
	 map.addOverlay(marker);
	 overlays.push(marker);
    }

/**
 * 画图工具按钮事件
 * @param id
 */
 var fat=0;
function draw(id){
    
	if (id == "h_1"&&fat==0) {
		 map.setDefaultCursor("crosshair");//改变鼠标指针为十字型
		 map.addEventListener("click", clickAction);//绑定点击地图画点事件
		 fat=1;
	}
	
	if (id == "h_2") {
		//清空所有标记点
		if(confirm("是否确定清除所有已经画好的标记点？")){
			clearAll();
		}
	}
	
	if (id == "h_3") {
		//保存所有标记点
		if(confirm("是否保存所有标记的点？")){
		
		   if(overlays.length>0){
			   //提交到保存的方法
			   var lnglat = [];//经纬度数组
			   for(var i=0;i<overlays.length;i++){
			   		var lng = overlays[i].getPosition().lng;
			   		var lat = overlays[i].getPosition().lat;
			   		lnglat.push(lng+","+lat);
			   }
			   saveMarks(lnglat);
		   }else{
		   	   alert("并没有标记任何点！");
		   }
		   
		}
	}
	
}

/**
 * 手动补点提示信息
 * @param lng
 * @param lat
 */
function qryApi(lng,lat){
	$.ajax({
	  url:contextPath + "/replenishrps/qryApi",
	  type:"post",
	  data: {"lng":lng,"lat":lat},  
	  success:function(data){
	  	  //保存查询的道路编号和名称到隐藏域
		  $("#boxRoadId").text(data.roadId);
		  $("#boxRoadName").text(data.roadName);
	  },error:function(data){
			$("#boxRoadId").text("查询失败");
		    $("#boxRoadName").text("查询失败");
	  }
	});
}