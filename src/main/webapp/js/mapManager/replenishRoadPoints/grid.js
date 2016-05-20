$(function() {
	//$("#tooltip").hide();
	$("#modal-backdrop").hide();
	$(document).click(function(e) {
		var e = e || window.event; //浏览器兼容性
		var elem = e.target || e.srcElement;
		while (elem) { //循环判断至跟节点，防止点击的是div子元素
			if (elem.id && elem.id == 'reportdiv') {
				return;
			}
			elem = elem.parentNode;
		}
		$("#reportdiv").hide();
	});
	// 初始化地图
	map = new BMap.Map("container"); // 创建地图实例
	var len = document.body.scrollWidth * 0.45;
	$("#container").css("height", len);
	map.setMapStyle({
		styleJson : styleRoadJson
	});// 设置地图样式
	map.setMaxZoom(18);
	map.centerAndZoom(oneregionname, 8);
	map.addControl(new BMap.NavigationControl({
		anchor : BMAP_ANCHOR_TOP_LEFT
	})); // 添加默认缩放平移控件
    map.enableScrollWheelZoom();	
	map.addEventListener('zoomend', function(re) {
		// 设置地图样式
		if (map.getZoom() > thZoom) {
			map.setMapStyle({
				styleJson : styleJson
			});
		} else {
			map.setMapStyle({
				styleJson : styleRoadJson
			});
		}
	});
	
	//查询数据
	$("#btn_query").die().live("click", function(e) {
         //清空地图上和集合图层
         map.clearOverlays() ;
         //清除未保存的标记点集合
		 overlays.length = 0;
 		$("#modal-backdrop").show();
         queyDate(getVoBean());
	});

	//地图浮动选择框事件
	$(".map_float_left li").bind("click", function(e) {
		var id=$(this).attr("id");
		draw(id);
	});
	
	//先隐藏手动添加道路点工具条
	$("#mark_tool").hide();
	
});

/**
 * 获取查询条件
 * @returns 
 */
function getVoBean(){
	var bean=new Object();//参数
	bean.province=$("#province").val();
	bean.district=$("#district").val();
	bean.area=$("#area").val();
	bean.roadType=$("#roadType").val();
	bean.roadLevel=$("#roadLevel").val();
	bean.roadId=$("#roadId").val();
     return bean;
}
