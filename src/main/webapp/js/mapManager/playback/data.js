function ChildPoint(object){
	  this.method = BMap.Point; 
	  this.method(parseFloat(object.lng)+object.OffsetLat,parseFloat(object.lat)+object.OffsetLng);//最关键的一行 
	  delete this.method; 
	  this.object = object; 
}

var pointColles = new Map();//图层键值对,键－采样点单一敬意状态，值－单一的图层
var shapes=[BMAP_POINT_SHAPE_CIRCLE,BMAP_POINT_SHAPE_SQUARE,BMAP_POINT_SHAPE_RHOMBUS,BMAP_POINT_SHAPE_STAR];
var special_point=[];//3个点，起点，终点，当前点
var all_data={points:null,warnPoints:null,report_list:null};//封装采点数据，事件数据，图表数据
/**
 * 查询数据
 * @param vo
 */
function queyDate(vo){
	if(validataRoadName()){
		$.ajax({
		  url:contextPath + "/playBack/mapPoint",
		  type:"post",
		  data: {"vo":JSON.stringify(vo)},  
		  success:function(data){
			  $("#modal-backdrop").hide();
				
//				/地图点显示
			  if(data.points.length>0){
				 
				  /**
					 * 加载曲线图
					 */
				  var report_list={xrotate:data.xrotate,
						  pci:data.pci,kpi:[{name:'rsrp',unit:'',data:data.rsrp},{name:'snr',unit:'',data:data.snr},
						   			     {name:'下行',unit:'',data:data.link},{name:'延时',unit:'',data:data.ping}]};
						  var ids_kpi=[1,2,3,4];
						//指标曲线展示
							 $("input[name='kpi_contrast']:checkbox").each(function(){ 
						         if($(this).attr("checked")){
						        	 $("#kpi_"+$(this).val()).show();
						         }
						     });	
					
					 addMarker(data.points,vo,report_list.kpi,0);
					 addMarker(data.warnPoints,vo,report_list.kpi,1);
					 loadChartKpi(ids_kpi,report_list);
					 all_data.points=data.points;
					 all_data.warnPoints=data.warnPoints;
					 all_data.report_list=report_list.kpi;
				  setTimeout(function(){ $("svg").insertAfter($("canvas").last()); }, 300); 
			  }else{
				  $(".main").hide();
				  $.messager.alert('提示','没有数据！',"warning");
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
 * report_list 曲线数据
 * iswarn 是否事件数据
 * @param vo
 */
	
	function addMarker(list,vo,report_list,iswarn){
		
		var xxMap={};
		//根据颜色分组
		for(var i=0;i<list.length;i++){
			list[i].num=i;
			 if(xxMap[list[i].state] == undefined){  
				 var pots=[]; 
				 pots.push(list[i]);  
		            xxMap[list[i].state] = pots;  
		        }else{  
		        	xxMap[list[i].state].push(list[i]);  
		        }  
			 if(iswarn!=1){
			 if(i==list.length-1){
				 add_special_point(new BMap.Point(list[list.length-1].lng,list[list.length-1].lat),"终点！",0);
				 add_special_point(new BMap.Point(list[0].lng,list[0].lat),"起点！",0);
				 markerFlicker(list);
			 }
			 }
		}
		var shape=iswarn==1?shapes[3]:shapes[0];
		var centerPots=[];//装所有点位置数组
		for (ss in xxMap) {
		var pots = [];
		var st = xxMap[ss];
		for (var t = 0; t < st.length; t++) {
			st[t].OffsetLat=iswarn==1?0.00003:0;
			st[t].OffsetLng=iswarn==1?0.00001:0;
			pots.push(new ChildPoint(st[t]));
		}
		centerPots=centerPots.concat(pots);
		var options = {
			shape : shape,
			size : BMAP_POINT_SIZE_SMALL,
			color : ss
		};
		var pointCollection = new BMap.PointCollection(pots, options);
		map.addOverlay(pointCollection);
		pointCollection.addEventListener('click', function(e) {
			//点跳动，曲线连动
			if(iswarn!=1){
			now_point=e.point.object.num;
			pointSkip(list,report_list,3);
			}
		});
		if(iswarn!=1)
		pointColles.put(st[0].inter,pointCollection);
	}
		//在第一图层设置视野
        if(iswarn!==1){
        	//在设置视野
        	 if(centerPots.length>1){
               	setTimeout(function(){map.setViewport(centerPots);},200);
            	}
               	if(pots.length==1){
               		setTimeout(function(){ map.centerAndZoom(new BMap.Point(pots[0].lng,pots[0].lat),17);},200);
               	}
               	
		}
		
	}
	
/**
 * text 文本内容
 */
function add_special_point(point,text){
	 //终点
	//设置marker图标为水滴
	 var vectorMarker = new BMap.Marker(point, {
	   // 指定Marker的icon属性为Symbol
	   icon: new BMap.Symbol(BMap_Symbol_SHAPE_POINT, {
	     scale: 1,//图标缩放大小
	     fillColor: "orange",//填充颜色
	     fillOpacity: 0.8//填充透明度
	   })
	 });
	 map.addOverlay(vectorMarker);
	 if(text!=''){
		 var label = new BMap.Label(text,{offset:new BMap.Size(10,-10)});
		 vectorMarker.setLabel(label);
	 }
	 special_point.push(vectorMarker);
}
	

/**
 * 删除所有轨迹图层
 * @param vo
 */
function deleteOverlays(){
	map.clearOverlays();
}






