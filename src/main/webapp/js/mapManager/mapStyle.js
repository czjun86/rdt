
var map = null;//当前百度底图
var thZoom=11;
var firstZoom=8;//变化的ZOOM
var date;
var colorArray = new Array('#CDCDCD',"#ff0000", "#ff00ff", "#ffff00", "#00ffff", "#0000ff", "#00ff00");
//地图样式
var styleJson = [ 

{
    "featureType": "green",
    "elementType": "all",
    "stylers": {"visibility" : "off"}
},
 {
    "featureType": "building",
    "elementType": "all",
    "stylers": {}
},
{
    "featureType": "label",
    "elementType": "labels.text.stroke",
    "stylers": {"visibility" : "off"}
},
{
    "featureType": "road",
    "elementType": "geometry.fill",
    "stylers": {
              "color": "#ffffff"
    }
},
{
    "featureType": "road",
    "elementType": "geometry.stroke",
    "stylers": {
              "color": "#bababa"
    }
},
{
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": {
              "color": "#767676"
    }
}, {
	"featureType" : "point",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
},  {
	"featureType" : "local",
	"elementType" : "labels",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "water",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "poi",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
} ];



var styleRoadJson = [  {
    "featureType": "background",
    "elementType": "geometry.fill",
    "stylers": {
              "color": "#F8F8F8"
    }
},
{
    "featureType": "label",
    "elementType": "labels.text.stroke",
    "stylers": {"visibility" : "off"}
},
{
    "featureType": "road",
    "elementType": "geometry.fill",
    "stylers": {
              "color": "#ffffff"
    }
},
{
    "featureType": "road",
    "elementType": "geometry.stroke",
    "stylers": {
              "color": "#bababa"
    }
},
{
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": {
              "color": "#767676"
    }
}, {
	"featureType" : "point",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "highway",
	"elementType" : "geometry",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "railway",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "local",
	"elementType" : "labels",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "water",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "poi",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
} ]
;
var opts = {
		  width : 20,     // 信息窗口宽度
		  height: 10,     // 信息窗口高度
		};

/**
 * 绘制工具样式
 */
var styleOptions = {
        strokeColor:"#CDCDCD",    //边线颜色。
        fillColor:"#CDCDCD",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 1,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
    };


/**
 *区间数据
 * @param vo
 */
function getKpiInterval(vo,iswarn){
	if(iswarn==1){
		$("#legend2").show();
		if(vo.kpiType=='9'||vo.kpiType=='10'){
			$(".legend2>ul>li").show();
		}else{
			$("#legend2").hide();
			return;
		}
	}
		$.ajax({
			  url:contextPath + "/map/mapkpi",
			  type:"post",
			  data: {"vo":JSON.stringify(vo)},  
			  success:function(data){
				  if(data){
					 var kpis=data.kpis;
					 var shape=kpis.shape;
						var one="";
						var oneColor=kpis.range_one_color;
						switch (kpis.range_one_state) {
						case 1:
							one="("+kpis.range_one_start+","+kpis.range_one_end+")";
							break;
						case 2:
							one="["+kpis.range_one_start+","+kpis.range_one_end+"]";
							break;
						case 3:one="("+kpis.range_one_start+","+kpis.range_one_end+"]";
							break;
						case 4:
							one="["+kpis.range_one_start+","+kpis.range_one_end+")";
							break;
						default:
							break;
						}
						var two="";
						var twoColor=kpis.range_two_color;
						switch (kpis.range_two_state) {
						case 1:
							two="("+kpis.range_two_start+","+kpis.range_two_end+")";
							break;
						case 2:
							two="["+kpis.range_two_start+","+kpis.range_two_end+"]";
							break;
						case 3:two="("+kpis.range_two_start+","+kpis.range_two_end+"]";
							break;
						case 4:
							two="["+kpis.range_two_start+","+kpis.range_two_end+")";
							break;
						default:
							break;
						}
						var three="";
						var threeColor=kpis.range_three_color;
						switch (kpis.range_three_state) {
						case 1:
							three="("+kpis.range_three_start+","+kpis.range_three_end+")";
							break;
						case 2:
							three="["+kpis.range_three_start+","+kpis.range_three_end+"]";
							break;
						case 3:three="("+kpis.range_three_start+","+kpis.range_three_end+"]";
							break;
						case 4:
							three="["+kpis.range_three_start+","+kpis.range_three_end+")";
							break;
						default:
							break;
						}
						var four="";
						var fourColor=kpis.range_four_color;
						switch (kpis.range_four_state) {
						case 1:
							four="("+kpis.range_four_start+","+kpis.range_four_end+")";
							break;
						case 2:
							four="["+kpis.range_four_start+","+kpis.range_four_end+"]";
							break;
						case 3:four="("+kpis.range_four_start+","+kpis.range_four_end+"]";
							break;
						case 4:
							four="["+kpis.range_four_start+","+kpis.range_four_end+")";
							break;
						default:
							break;
						}
						var five="";
						var fiveColor=kpis.range_five_color;
						switch (kpis.range_five_state) {
						case 1:
							five="("+kpis.range_five_start+","+kpis.range_five_end+")";
							break;
						case 2:
							five="["+kpis.range_five_start+","+kpis.range_five_end+"]";
							break;
						case 3:five="("+kpis.range_five_start+","+kpis.range_five_end+"]";
							break;
						case 4:
							five="["+kpis.range_five_start+","+kpis.range_five_end+")";
							break;
						default:
							break;
						}
						var six="";
						var sixColor=kpis.range_six_color;
						switch (kpis.range_six_state) {
						case 1:
							six="("+kpis.range_six_start+","+kpis.range_six_end+")";
							break;
						case 2:
							six="["+kpis.range_six_start+","+kpis.range_six_end+"]";
							break;
						case 3:six="("+kpis.range_six_start+","+kpis.range_six_end+"]";
							break;
						case 4:
							six="["+kpis.range_six_start+","+kpis.range_six_end+")";
							break;
						default:
							break;
						}
						var htl_id="#legend";
						if(iswarn==0){
							 $("#legend").find(".heat_7").hide();
							 if(vo.kpiType==1||vo.kpiType==2){
								  $("#legend").find(".heat_7").show();
							}
							 $(htl_id).find("font").html(shape);
						}else{
							htl_id="#legend2";
							$(htl_id).find("font").html("★");
						}
		               //设置值
						   
						    $(htl_id).find(".heat_1>span").html(one);
							$(htl_id).find(".heat_2>span").html(two);
							$(htl_id).find(".heat_3>span").html(three);
							$(htl_id).find(".heat_4>span").html(four);
							$(htl_id).find(".heat_5>span").html(five);			
							$(htl_id).find(".heat_6>span").html(six);
							//设置颜色
							    $(htl_id).find(".heat_1>font").css("color",oneColor);
								$(htl_id).find(".heat_2>font").css("color",twoColor);
								$(htl_id).find(".heat_3>font").css("color",threeColor);
								$(htl_id).find(".heat_4>font").css("color",fourColor);
								$(htl_id).find(".heat_5>font").css("color",fiveColor);			
								$(htl_id).find(".heat_6>font").css("color",sixColor);
								
					 
				  }
			   }
			});
	}