/*******************************************************************************
 * 
 * 栅格JS文件 CZJ
 * 
 */

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
		//显示不同比例下的栅格及数据
	});
	chooseMap(map);	
	/**
	 * 初始化绘制工具
	 */
	
	
	//查询数据
	$("#btn_query").die().live("click", function(e) {
         //清空地图上和集合图层
 		$("input[name='kpiInterv']").attr({checked:"checked"});
         map.clearOverlays() ;
         pointColles.clear();
         var vo=getVoBean();
         if(vo!=null){
        		if(vo.roadId==null||vo.roadId==""){
        			$.messager.alert('提示','请选择道路！',"error");
        			return;
        		}
        		if(vo.imei==null || vo.imei.replace(/(^\s*)|(\s*$)/g, "") =='' ){
        			$.messager.alert('提示','请输入imei！',"error");
        			return;
        		}
 		$("#modal-backdrop").show();
         }
         queyDate(getVoBean());
	});
	//其他页面跳转自动触发事件
	if(tiaozhuan!=null&&tiaozhuan=="1"){
		$("#btn_query").trigger("click");
	}
	//区间联动事件
	$(".kpi_v").die().live("change", function(e) {
		var bean=new Object();//参数
		bean.operator=$("#operator").val();
		bean.kpiType=$("#kpiType").val();
		getKpiInterval(bean,0);
	});
	//事件联动事件
	$("#warn").die().live("change", function(e) {
		var bean=new Object();//参数
		bean.operator=$("#operator").val();
		bean.kpiType=$("#warn").val();
		getKpiInterval(bean,1);
	});
	//地图浮动选择框事件
	$(".map_float_left li").bind("click", function(e) {
		var id=$(this).attr("id");
		draw(id);
	});
	//区间图层点击事件
	$("input[name='kpiInterv']:checkbox").die().live("click", function(e) {
		var kpiInterv=$(this).val();
		var flag=0;
		if($(this).attr("checked")=="checked"){
			flag=1;
		}
		showColorsPoint(kpiInterv,flag);
	});
});
/**
 * points点集合
 */
function excelClick(points){
	 var vo=getVoBean();
	 vo.points=points;
	 if(vo.uuid!=""){
     	$("#modal-backdrop").show();
 		$.ajax({
 			type:"post",
 			url:contextPath + "/epinfoMap/excel",
 			data:{
 				"vo":JSON.stringify(vo)
 			},
 			success:function(data){
 				$("#modal-backdrop").hide();
 				var fileName = data.fileName;
 				if(fileName!="-1"){
 					window.location.href=contextPath+"/export/export?fileName="+fileName;
 				}else{
        			$.messager.alert('提示','生成excel失败！',"error");
 				}
 			},
 			error:function(data){
 				$("#modal-backdrop").hide();
    			$.messager.alert('提示','生成excel失败！',"error");
 			}
 		});
	 }else{
		 $.messager.alert('提示','请先查询数据！',"error");
	 }
}
/**
 * 获取查询条件
 * @returns {___anonymous3583_3586}
 */
function getVoBean(){
	var bean=new Object();//参数
	bean.uuid=$("#uuid").val();
	bean.starttime= $("input[name=queryTime]").val().split("-")[0].trim();
	bean.endtime=$("input[name=queryTime]").val().split("-")[1].trim();
	var days = DateDiff(bean.endtime, bean.starttime);
	if(days>30){
		$.messager.alert('提示','选择时间不能超过30天！',"error");
		return;
	}
	bean.province=$("#province").val();
	bean.district=$("#district").val();
	bean.area=$("#area").val();
	bean.operator=$("#operator").val();
	bean.kpiType=$("#kpiType").val();
	bean.roadType=$("#roadType").val();
	bean.roadLevel=$("#roadLevel").val();
	bean.roadId=$("#roadId").val();
	bean.kpiSymbol=$("#kpiSymbol").val();
	bean.kpiVaule=$("#kpiVaule").val();
	bean.kpiInterv="";
     $("input[name='kpiInterv']:checkbox").each(function(){ 
         if($(this).attr("checked")){
        	 bean.kpiInterv += $(this).val()+",";
         }
     });
     bean.imei=$("#imei").val();
     bean.warn = $("#warn").val();
     return bean;
}



/**
 * 日期控件限制
 * @param sDate1
 * @param sDate2
 */
function DateDiff(sDate1, sDate2)
{
    var aDate, oDate1, oDate2, iDays;
    oDate1 = new Date();
    oDate2 = new Date();
    aDate = sDate1.split(".");
    oDate1.setFullYear(aDate[0], aDate[1], aDate[2]);
    aDate = sDate2.split(".");
    oDate2.setFullYear(aDate[0], aDate[1], aDate[2]);
    iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 /24); //把相差的毫秒數轉抽象為天數
    return iDays;
}