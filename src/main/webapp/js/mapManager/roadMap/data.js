function ChildPoint(object){
	  this.method = BMap.Point; 
	  this.method(object.lng+object.i/(object.zoom*50),object.lat-object.i/(object.zoom*100));//最关键的一行 
	  delete this.method; 
	  this.object = object; 
}

var pointColles = new Map();//图层键值对,键－采样点单一敬意状态，值－单一的图层
var pointCol_diejia = new Array();//叠加图层
var shapes=[BMAP_POINT_SHAPE_CIRCLE,BMAP_POINT_SHAPE_SQUARE,BMAP_POINT_SHAPE_RHOMBUS,BMAP_POINT_SHAPE_STAR];
var arrayMarker = [] ;
/**
 * 查询数据
 * diejia "1,2,3"
 * @param vo
 */
function queyDate(vo,diejia,chooseType){
	if(validataRoadName()){
		$.ajax({
		  url:contextPath + "/roadMap/mapPot",
		  type:"post",
		  data: {"vo":JSON.stringify(vo)},  
		  success:function(data){
			  $("#modal-backdrop").hide();
			  if(data.list.length>0){
				  addMarker(data.list,vo,diejia,chooseType);
				  addEventMarker(data.warnList,vo,diejia,chooseType);
				  //非叠加查询写入UUID
				  if(data.uuid)
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
 * diejia "1,2,3"
 * @param vo
 */
	
function addMarker(list,vo,diejia,chooseType){
	var diejia_arr=[];
	diejia_arr=diejia.split(",");
	var diejia_length=diejia_arr.length-1;
	var xxMap={};
	//根据颜色分组
	for(var i=0;i<list.length;i++){
		 if(xxMap[list[i].state+""+list[i].telecoms] == undefined){  
			 var pots=[]; 
			 pots.push(list[i]);  
	            xxMap[list[i].state+""+list[i].telecoms] = pots;  
	        }else{  
	        	xxMap[list[i].state+""+list[i].telecoms].push(list[i]);  
	        }  
	}
	var centerPots=[];//装所有点位置数组
	for (ss in xxMap) {
		var pots = [];
		var st = xxMap[ss];
		var zoom=map.getZoom();
		if(chooseType==2){
			diejia_length=$.inArray(st[0].telecoms, diejia_arr)+1;
		}
		for (var t = 0; t < st.length; t++) {
			st[t].i=diejia_length;
			st[t].zoom=zoom;
			var point_zi=new ChildPoint(st[t]);
			pots.push(point_zi);
		}
		centerPots=centerPots.concat(pots);
		var options = {
			shape : shapes[diejia_length],
			size : BMAP_POINT_SIZE_SMALL,
			color : st[0].state
		};
		var pointCollection = new BMap.PointCollection(pots, options);
		map.addOverlay(pointCollection);
		if(st[0].inter!=8){
			pointCollection.addEventListener('click', function(e) {
				//叠加事件先清除之前的叠加数据
				
				var kpiInterv_ss="";
				$("input[name='kpiInterv']:checkbox").each(function(){ 
			         if($(this).attr("checked")){
			        	 kpiInterv_ss += $(this).val()+",";
			         }
			     });
				if(kpiInterv_ss.indexOf(e.point.object.inter)!=-1){
					pointinfo(map,diejia,e.point,vo,chooseType);}
			});
		}
		//非叠加数据
		if(diejia==""){
		    pointColles.put(st[0].inter,pointCollection);
		}else{
			pointCol_diejia.push(pointCollection);
		}
	}
	 if(diejia==""){
	//在设置视野
        	 if(centerPots.length>1){
               	setTimeout(function(){map.setViewport(centerPots);},200);
            	}
               	if(pots.length==1){
               		setTimeout(function(){ map.centerAndZoom(new BMap.Point(pots[0].lng,pots[0].lat),17);},200);
               	}
       }
}
	
/***
 * 增加轨迹点
 * diejia "1,2,3"
 * @param vo
 */
	
	function addEventMarker(list,vo,diejia,chooseType){
		var xxMap={};
		//根据颜色分组
		for(var i=0;i<list.length;i++){
			 if(xxMap[list[i].state+""+list[i].telecoms] == undefined){  
				 var pots=[]; 
				 pots.push(list[i]);  
		            xxMap[list[i].state+""+list[i].telecoms] = pots;  
		        }else{  
		        	xxMap[list[i].state+""+list[i].telecoms].push(list[i]);  
		        }  
		}
		var i_s=0;
		for (ss in xxMap) {
		var pots = [];
		var st = xxMap[ss];
		var zoom=map.getZoom();
		for (var t = 0; t < st.length; t++) {
			st[t].i=0;
			st[t].zoom=zoom;
			st[t].lng +=  0.00007;
			var point_zi=new ChildPoint(st[t]);
			pots.push(point_zi);
		}
		//在第一图层设置视野
        /*if(i_s==0&&diejia==""){
        	 map.setViewport(pots);
		}*/
		var options = {
			shape : BMAP_POINT_SHAPE_STAR,
			size : BMAP_POINT_SIZE_SMALL,
			color : st[0].state
		};
		var pointCollection = new BMap.PointCollection(pots, options);
		map.addOverlay(pointCollection);
		//if(st[0].inter!=8){
			pointCollection.addEventListener('mouseover', function(e) {
				if(e.point.object.isWarn=="1"){
					var myLabel = new BMap.Label("出现次数:"+e.point.object.dicWarn+"次",     //为lable填写内容
						    {
								offset:new BMap.Size(-60,-60),                  //label的偏移量，为了让label的中心显示在点上
							    position:new BMap.Point(parseFloat(e.point.object.lng)+0.0006, parseFloat(e.point.object.lat)-0.00037)
						    }
					);
	
					//myLabel.setTitle("我是文本标注label");
					if(arrayMarker.length>0){//添加到label集合用于删除
						arrayMarker[arrayMarker.length] = myLabel;
					}else{
						arrayMarker[0]= myLabel;
					}
					map.addOverlay(myLabel); 
					/*var html = "<div>出现次数:"+e.point.object.dicWarn+"</div>";
					html += "<div>lat:"+e.point.object.lat+"</div>";
					html += "<div>lat:"+e.point.object.lng+"</div>";
					var opts = {enableMessage:false};
				    var infoWindow = new BMap.InfoWindow(html,opts);  // 创建信息窗口对象 
					setTimeout(function(){map.openInfoWindow(infoWindow,new  BMap.Point(e.point.lng,e.point.lat)); }, 1000);      // 打开信息窗口
					*/
				}
			});
			pointCollection.addEventListener('mouseout', function(e) {
				for(var i=0;i<arrayMarker.length;i++){
					map.removeOverlay(arrayMarker[i]);
				}
			});
		//}
		/*//非叠加数据
		if(diejia==""){
		    pointColles.put(st[0].inter,pointCollection);
		}else{
			pointCol_diejia.push(pointCollection);
		}
		
		i_s++;*/
	}
}
/**
 * 点事件上的弹出信息
 * @param map
 * flag是否叠加1－叠加，0－不叠加
 * point返回数据
 * vo参数
 */	
function pointinfo(map,diejia,point,vo,chooseType){
	vo.id=point.object.id;
	vo.uuid= $("#uuid").val();
	$.ajax({
		  url:contextPath + "/roadMap/mapPoint",
		  type:"post",
		  data: {"vo":JSON.stringify(vo)},  
		  success:function(data){
			  if(data.list.length>0){
				 var ppat=data.list[0];
				 ppat.starttime=vo.starttime;
				 ppat.endtime=vo.endtime;
				 ppat.diejia=diejia;
						if(chooseType!=null&&chooseType==2){
							ppat.telecoms=point.object.telecoms;
							switch(parseInt(ppat.telecoms))
							{
							case 1:
								ppat.diejia="联通,";
							  break;
							case 2:
								ppat.diejia="移动,";
							  break;
							case 3:
								ppat.diejia="电信,";
							  break;
							default:
								break;
							}
						}
					ppat.chooseOp=vo.operator;
					 $(".choseTime").hide();
					var html ="";
					//判断是看详情还是作对比分析功能
					if($("#h_4").attr("choose")=='true'&&ppat.diejia.length==0){
						ppat.operatoruni = 0;
						ppat.operatormob = 0;
						ppat.operatortele = 0;
						if(teleAuth.substring(0,1)=="1"){
							ppat.operatoruni = 1;
						}
						if(teleAuth.substring(1,2)=="1"){
							ppat.operatormob = 1;
						}
						if(teleAuth.substring(2,3)=="1"){
							ppat.operatortele = 1;
						}
						
						html=new EJS({url: contextPath+'/js/mapManager/roadMap/duibi_choose.ejs'}).render(ppat);
					}else{
						if(point.object.inter!='7')
						html= new EJS({url: contextPath+'/js/mapManager/roadMap/point_info.ejs'}).render(ppat);
						if(point.object.inter=='7')html="<div class='no_service_box'><div class='no_service'><img src='"+contextPath+"/images/question.png' width='23' height='23' /><span>无服务-"+ppat.road_name+"</span></div></div>";
					}
					
					var opts = {enableMessage:false};
				    var infoWindow = new BMap.InfoWindow(html,opts);  // 创建信息窗口对象 
				    infoWindow.addEventListener('close', function(e) {
				    	if($("#dtck_reportrange2").length>0){
				    		$("#dtck_reportrange2").remove();
				    	}
					});
				    setTimeout(function(){map.openInfoWindow(infoWindow,new  BMap.Point(point.lng,point.lat)); }, 1000);      // 打开信息窗口
				  
			  }
		  },
			error:function(data){
				$.messager.alert('提示','数据查询失败！',"error");
			}
		  });
	
	
	
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
 * 删除所有轨迹图层
 * @param vo
 */
function deleteOverlays(){
	map.clearOverlays();
}
/**
 * 删除对比路测数据
 */
function deleteDubi(){
	for(var k=0;k<pointCol_diejia.length;k++){
		map.removeOverlay(pointCol_diejia[k]);
	}
	pointCol_diejia=[];
}
/**
 * 轨迹叠加确定事件
 */
function dubiClick(road_id){
	var vo=getVoBean();
	//清除之前的叠加图层
	for(var k=0;k<pointCol_diejia.length;k++){
		map.removeOverlay(pointCol_diejia[k]);
	}

	pointCol_diejia=[];
	
	var chooseType=$("#chooseType").val();
	vo.chooseType=chooseType;
	vo.roadId=road_id;
	vo.level=1;//作对比用
	var diejia="";
	//设置对比时间
	if(vo.chooseType==1){
		vo.starttime= $("#choseTime").val().split("-")[0].trim();
		vo.endtime= $("#choseTime").val().split("-")[1].trim();
		var days = DateDiff(vo.starttime, vo.endtime);
		if(days>30){
            $.messager.alert('提示','选择时间不能超过30天！',"error");
			return;
		}
		diejia=$("#choseTime").val()+",";
	}
    //设置对比运营商
	if(vo.chooseType==2){
		vo.dubiOperator="";
		 $("input[name='dubiOperator']:checkbox").each(function(){ 
	         if($(this).attr("checked")){
	        	 vo.dubiOperator += $(this).val()+",";
	         }
	     });
		 diejia=vo.dubiOperator;
	}	
	if(diejia!='')
	{
	$("#modal-backdrop").show();
	queyDate(vo,diejia,chooseType);
	}
}
