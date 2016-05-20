function ChildPoint(object){
	  this.method = BMap.Point; 
	  this.method(object.lng,object.lat);//最关键的一行 
	  delete this.method; 
	  this.object = object; 
}

var pointColles = new Map();//图层键值对,键－采样点单一敬意状态，值－单一的图层
var shapes=[BMAP_POINT_SHAPE_CIRCLE,BMAP_POINT_SHAPE_STAR,BMAP_POINT_SHAPE_SQUARE,BMAP_POINT_SHAPE_RHOMBUS];
/**
 * 查询数据
 * @param vo
 */
function queyDate(vo){
	if(validataRoadName()){
		$.ajax({
		  url:contextPath + "/map/mapPoint",
		  type:"post",
		  data: {"vo":JSON.stringify(vo)},  
		  success:function(data){
			  $("#modal-backdrop").hide();
			  if(data.list.length>0){
				  addMarker(data.list,vo);
				  addEventMarker(data.warnList,vo);
				  $("#uuid").val(data.uuid);
				  setTimeout(function(){ $("svg").insertAfter($("canvas").last()); }, 300); 
			  }else{
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
 * @param vo
 */
	
	function addMarker(list,vo){
		
		var xxMap={};
		//根据颜色分组
		for(var i=0;i<list.length;i++){
			 if(xxMap[list[i].state] == undefined){  
				 var pots=[]; 
				 pots.push(list[i]);  
		            xxMap[list[i].state] = pots;  
		        }else{  
		        	xxMap[list[i].state].push(list[i]);  
		        }  
		}
		var centerPots=[];//装所有点位置数组
		for (ss in xxMap) {
		var pots = [];
		var st = xxMap[ss];
		for (var t = 0; t < st.length; t++) {
			pots.push(new ChildPoint(st[t]));
		}
		centerPots=centerPots.concat(pots);
		var options = {
			shape : shapes[0].shape,
			size : BMAP_POINT_SIZE_SMALL,
			color : ss
		};
		var pointCollection = new BMap.PointCollection(pots, options);
		map.addOverlay(pointCollection);
		//如果包含些颜色区间则显示
		if(vo.kpiInterv.indexOf(st[0].inter)==-1){
			pointCollection.hide();
		}
		pointCollection.addEventListener('click', function(e) {
			var kpiInterv_ss="";
			$("input[name='kpiInterv']:checkbox").each(function(){ 
		         if($(this).attr("checked")){
		        	 kpiInterv_ss += $(this).val()+",";
		         }
		     });
			if(kpiInterv_ss.indexOf(e.point.object.inter)!=-1){
			pointinfo(map,0,e.point);}
		});
		pointColles.put(st[0].inter,pointCollection);
	}
		//在设置视野
        	 if(centerPots.length>1){
               	setTimeout(function(){map.setViewport(centerPots);},200);
            	}
               	if(pots.length==1){
               		setTimeout(function(){ map.centerAndZoom(new BMap.Point(pots[0].lng,pots[0].lat),17);},200);
               	}
		
	}
	/***
	 * 增加轨迹点事件
	 * @param vo
	 */	
function addEventMarker(list,vo){
		var xxMap={};
		//根据颜色分组
		for(var i=0;i<list.length;i++){
			 if(xxMap[list[i].state] == undefined && list[i].warn != undefined){  
				 var pots=[]; 
				 pots.push(list[i]);  
		            xxMap[list[i].state] = pots;  
		        }else{  
		        	xxMap[list[i].state].push(list[i]);  
		        }  
		}
		var i_s=0;
		for (ss in xxMap) {
			var eventPots = [];//事件点
			var st = xxMap[ss];
			var objectPoint;
			for (var t = 0; t < st.length; t++) {
				if(true){//判断是否添加事件点
					objectPoint = $.extend(true,{},st[t]);//真正赋值，而不是赋内存地址
					objectPoint.lng = parseFloat(objectPoint.lng) + 0.00007;
					objectPoint.lat = parseFloat(objectPoint.lat);
					eventPots.push(new ChildPoint(objectPoint));//加入事件点集合
				}
			}
			var eventoptions = {//事件点样式
				shape : BMAP_POINT_SHAPE_STAR,
				size : BMAP_POINT_SIZE_SMALL,
				color : ss
			};
			var eventPointCollection = new BMap.PointCollection(eventPots, eventoptions);//画事件点
			map.addOverlay(eventPointCollection);//地图展现事件点
		}
		
	}
		
/**
 * 
 * @param kpiInterv区间值,flag 1-选中，2－取消
 */	
function showColorsPoint(kpiInterv,flag){
	map.closeInfoWindow() ;
	var polc=pointColles.get(parseInt(kpiInterv));
	if(!pointColles.isEmpty()&&polc!=null){
		//判断选中
		if(flag==1){
			polc.show();
		}else{
			polc.hide();
		}
		
	}
}
/**
 * 点事件上的弹出信息
 * @param map
 * flag是否叠加1－叠加，0－不叠加
 * point
 */	
function pointinfo(map,flag,point){
	var html ="";
	if(point.object.inter!='7')
	html  = new EJS({url: contextPath+'/js/mapManager/point_info.ejs'}).render(point.object);
	if(point.object.inter=='7')html="<div class='no_service_box'><div class='no_service'><img src='"+contextPath+"/images/question.png' width='23' height='23' /><span>无服务</span></div></div>";
	var opts = {enableMessage:false};
    var infoWindow = new BMap.InfoWindow(html,opts);  // 创建信息窗口对象 
    setTimeout(function(){map.openInfoWindow(infoWindow,new  BMap.Point(point.lng,point.lat)); }, 1000); 
	
}	
/**
 * 删除所有轨迹图层
 * @param vo
 */
function deleteOverlays(){
	map.clearOverlays();
}
