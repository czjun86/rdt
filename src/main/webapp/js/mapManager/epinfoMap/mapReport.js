/**
 * 单击行政区划
 * isarea 1-区域，2－小区
 * 区域或小区名称
 * @param regionData 小区或区域ID
 */
/**
 * echarts图像JS引入
 */
require.config({
    paths: {
        echarts: contextPath+'/scripts/plugins/echarts/www/js'
    }
});
function clickRegion(vo, name) {
					$.ajax({
						  url:contextPath + "/epinfoMap/loadReport",
						  type:"post",
						  data: {"vo":JSON.stringify(vo)},  
						  success:function(data){

					var trendName = data.trendName;
					var trendData = data.trendData;
					var trendX = data.trendX;
					var trendSeries = [];
					for (var i = 0; i < trendName.length; i++) {
						var t = {
							name : trendName[i],
							type : 'line',
							data : trendData[i]
						};
						trendSeries[i] = t;
					}
					//走势线形图
					optionTrend = createData(name + ':平均物理带宽走势图', 'vertical',
							trendName, trendX, '带宽(Mbps)', trendSeries, true,
							'shadow',-45);

					prelist = data.pros;
					timeX = trendX;
					//占比饼图
					optionPre = PieChart('宽带用户数量',trendX[trendX.length-1], 'vertical',
							prelist[prelist.length-1].proName, prelist[prelist.length-1].proData,null,null);

					require([ 'echarts', 'echarts/chart/bar',
							'echarts/chart/line', 'echarts/chart/pie' ],
							function(ec) {
								var ecConfig = require('echarts/config');

								var trendChart = ec.init(document
										.getElementById('trend'), 'macarons');
								trendChart.setOption(optionTrend);
								trendChart.on(ecConfig.EVENT.HOVER, switchPie);
								window.onresize = trendChart.resize;
								var preChart = ec.init(document
										.getElementById('pro'), 'macarons');
								preChart.setOption(optionPre);
								window.onresize = preChart.resize;
							});
					$("#reportdiv").show();
				}
			});
}

/**
 * 根据线形图联动展示饼图
 * @param param
 */
function switchPie(param) {
	var nowTime= Date.parse(new Date());
	if(timeInterval+500<nowTime){
		timeInterval = nowTime;
		require([ 'echarts', 'echarts/chart/bar', 'echarts/chart/line',
				'echarts/chart/pie' ], function(ec) {
			var proChart = ec.init(document.getElementById('pro'), 'macarons');
			var optionPro = PieChart('宽带设备数量', timeX[param.dataIndex], 'vertical',
					prelist[param.dataIndex].proName,
					prelist[param.dataIndex].proData);
			proChart.setOption(optionPro);
			window.onresize = proChart.resize;
		});
	}
}

/**
 * 判断IE浏览器版本
 * 
 * **/

function checkIEVersion() {
	var browser = navigator.appName;
	if (browser == "Microsoft Internet Explorer") {
		var b_version = navigator.appVersion;
		var version = b_version.split(";");
		var trim_Version = version[1].replace(/[ ]/g, "");
		if (trim_Version == "MSIE8.0") {
			//移除提示文本
			$("input[type='text']").attr("placeholder", "");
		} else if (trim_Version == "MSIE9.0") {
			//移除提示文本
			$("input[type='text']").attr("placeholder", "");
		}
	}
}