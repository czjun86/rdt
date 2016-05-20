 
var pr;//已经选择区域
var	 drawingManager;
var menu = new BMap.ContextMenu();
//右键菜单
var txtMenuItem = [ {
		text : '框选详情',
		callback : function(p) {
			getinformation(pr);
		}

	}, {
		text : '导出数据',
		callback : function(p) {
			var list=pr.getPath();
			var points="";
			for(var i=0;i<list.length;i++){
				points+=list[i].lng+","+list[i].lat+";";
			}
			excelClick(points);
		}
	}, {
		text : '删除框选',
		callback : function(p) {
			pr.removeContextMenu(menu);
			map.removeOverlay(pr);
			map.closeInfoWindow() ;
			 pr=null;
		}
	} ];
for(var i=0; i < txtMenuItem.length; i++){
		menu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));
	}
/**
 * 加载百度画图工具
 */
var styleOptions = {
        strokeColor:"blue",    //边线颜色。
        strokeWeight: 1,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
    };
function chooseMap(map){
	 drawingManager =new BMapLib.DrawingManager(map, {isOpen: false, 
		    drawingType: BMAP_DRAWING_MARKER, enableDrawingTool: true,
		    enableCalculate: false,
		    drawingToolOptions: {
		        anchor: BMAP_ANCHOR_TOP_LEFT,
		        offset: new BMap.Size(5, 5),
		        drawingTypes : [
		            BMAP_DRAWING_RECTANGLE 
		         ]
		    },
		    polygonOptions: styleOptions, //多边形的样式
	        rectangleOptions: styleOptions //矩形的样式
	        });
	     drawingManager.addEventListener('overlaycomplete', function(re) {//监控矩形完成事件
	    	
	    	 drawingManager.close();
	        $("#m_3").show();
	  		$("#m_1").show();
	  		$("#m_2").show();
	  		$("#h_3").hide();
	  		$("#h_2").hide();
	  		$("#h_1").hide();
	  		$(".gis-mapgj div").bind("click", function(e) {
	  			var id=$(this).attr("id");
	  			draw(id);
	  		});
			 if(!(typeof(pr)=="undefined")&&pr!=null){
				 map.removeOverlay(pr);
				 map.closeInfoWindow() ;
				 pr=null;
			 }
	   
		   pr = re.overlay;
		 
			pr.addContextMenu(menu);
		// getinformation(pr);
		});
}
/**
 * 取画图路径传到后台取得平均值及采样点数并弹出消息
 * @param paths
 */
function getinformation(overlay){
	$("#modal-backdrop").show();
	var points="";
	var list=overlay.getPath();
	for(var i=0;i<list.length;i++){
		points+=list[i].lng+","+list[i].lat+";";
	}
	 var vo=getVoBean();
	 vo.points=points;
	$.ajax({
		  url:contextPath + "/map/pointInfo",
		  type:"post",
		  data: {"vo":JSON.stringify(vo)},  
		  success:function(data){
			  $("#modal-backdrop").hide();
			  if(data){
				  data.points=points;
				  if(data.signalCount>0){
					//请求出平均值
						var opts = {enableMessage:false};
						var html  = new EJS({url: contextPath+'/js/mapManager/avg_point_info.ejs'}).render(data);
					    var infoWindow = new BMap.InfoWindow(html,opts);  // 创建信息窗口对象  
					    setTimeout(function(){map.openInfoWindow(infoWindow, overlay.getBounds().getCenter()); }, 1000);        // 打开信息窗口
				  }else{
		 				$.messager.alert('提示','没有数据！',"error");
				  }
				
			  }
		  },
			error:function(data){
				$("#modal-backdrop").hide();
				$.messager.alert('提示','数据查询失败！',"error");
			}
		  });
	
}
/**
 * 画图工具按钮事件
 * @param id
 */
function draw(id){
	 if(!(typeof(pr)=="undefined")&&pr!=null){
		 pr.removeContextMenu(menu);
		 map.removeOverlay(pr);
		 map.closeInfoWindow() ;
		 pr=null;
	 }
	   drawingManager.close();
	    if (id == "m_1") {
		$("#" + id).hide();
		drawingManager.open();
		drawingManager.setDrawingMode(BMAP_DRAWING_RECTANGLE);
		$("#h_1").show();
		$("#m_2").show();
		$("#h_3").show();
		$("#h_2").hide();
		$("#m_3").hide();
		 $(".gis-mapgj>div").unbind("click");
	}
	if (id == "m_2") {
		$("#" + id).hide();
		drawingManager.open();
		drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
		$("#h_2").show();
		$("#m_1").show();
		$("#h_3").show();
		$("#h_1").hide();
		$("#m_3").hide();
		 $(".gis-mapgj>div").unbind("click");
	}
	if (id == "h_3") {
		$("#" + id).hide();
		$("#m_3").show();
		$("#m_1").show();
		$("#m_2").show();
		$("#h_2").hide();
		$("#h_1").hide();
	}
	
}