require.config({
	    paths: {
	        echarts: contextPath+'/scripts/plugins/echarts/www/js'
	    }
	});

/**
 * 首页图标回调
 */
function loadChart(){
	$("#modal-backdrop").show();
	$.ajax({
		type:"post",
		url:contextPath + "/home/getChartsInfo",
		async: false,
		success:function(data){
			//无线宽带覆盖率
			var wirelessCover = data.wirelessCover;
			var wirelessName = wirelessCover.name;
			var wirelessData = wirelessCover.data;
			var wirelessX = wirelessCover.x;
			var wirelessAX = wirelessCover.ax;
			var wirelessSeries = [];
			for(var i=0;i<wirelessName.length;i++){
				var t = {
				name : wirelessName[i],
				type : 'line',
	            data : wirelessData[i]
				};
				wirelessSeries[i] = t;
			}
			wirelessQuota = createData('覆盖率' ,'vertical',wirelessName,wirelessAX,'覆盖率(%)',wirelessSeries ,false,'line',-45);
			
			//下行链路宽带覆盖率
			var broadbandCover = data.broadbandCover;
			var broadbandName = broadbandCover.name;
			var broadbandData = broadbandCover.data;
			var broadbandX = broadbandCover.x;
			var broadbandAX = broadbandCover.ax;
			var broadbandSeries = [];
			for(var i=0;i<broadbandName.length;i++){
				var t = {
				name : broadbandName[i],
				type : 'line',
	            data : broadbandData[i]
				};
				broadbandSeries[i] = t;
			}
			broadbandQuota = createData('下行链路带宽' ,'vertical',broadbandName,broadbandAX,'速率(Mbps)',broadbandSeries ,false,'line',-45);
			
			//网络掉线率
			var networkRate = data.networkRate;
			var networkRateName = networkRate.name;
			var networkRateData = networkRate.data;
			var networkRateX = networkRate.x;
			var networkRateAX = networkRate.ax;
			var networkRateSeries = [];
			for(var i=0;i<networkRateName.length;i++){
				var t = {
				name : networkRateName[i],
				type : 'line',
	            data : networkRateData[i]
				};
				networkRateSeries[i] = t;
			}
			networkRateQuota = createData('网络掉线率' ,'vertical',networkRateName,networkRateAX,'%',networkRateSeries ,false,'line',-45);
			
			
			//业务掉线率
			var pingRate = data.pingRate;
			var pingRateName = pingRate.name;
			var pingRateData = pingRate.data;
			var pingRateX = pingRate.x;
			var pingRateAX = pingRate.ax;
			var pingRateSeries = [];
			for(var i=0;i<pingRateName.length;i++){
				var t = {
				name : pingRateName[i],
				type : 'line',
	            data : pingRateData[i]
				};
				pingRateSeries[i] = t;
			}
			pingRateQuota = createData('业务掉线率' ,'vertical',pingRateName,pingRateAX,'%',pingRateSeries ,false,'line',-45);
			
			
			//RSRP指标区间占比
			var rsrpTotal = data.rsrpTotal;
			var rsrpName = rsrpTotal.name;
			var rsrpData = rsrpTotal.data;
			var rsrpColor = rsrpTotal.color;
			var rsrpX = rsrpTotal.x;
			var rsrpRate = rsrpTotal.rate;
			var rsrpSeries = [];
			for(var i=0;i<rsrpName.length;i++){
				var t = {
				name : rsrpName[i],
				type : 'bar',
				itemStyle: {
	                normal: {
	                	label : {
							show: true, 
							position: 'top',
							formatter: function(params) {//鼠标移到柱状图上得提示信息
			                    var res = "";
			                    var datas = params.series.data;
			                    for (var j = 0; j < rsrpX.length; j++) {
			                    	if(params.name == rsrpX[j]){
			                    		//res = params.value+"<br/>"+snrRate[j];
			                    		res = rsrpRate[j];
			                    	}
			                    }
			                    return res;
			                },
			                textStyle: {
	                            /*color: function(params) {
	    	    					var colorList = rsrpColor;
	    	    					return colorList[params.dataIndex]
	    	    				}*/
			                	 color: '#555'
	                        }
						},
	                	color: function(params) {
	    					var colorList = rsrpColor;
	    					return colorList[params.dataIndex]
	    				}
	                }
	            },
	            data : rsrpData[i]
				};
				rsrpSeries[i] = t;
			}
			rsrpQuota = createDataColorful('RSRP指标区间占比' ,rsrpX,rsrpRate,'里程(km)',rsrpSeries ,false,'shadow',-45);
			
			//SNR指标区间占比
			var snrTotal = data.snrTotal;
			var snrName = snrTotal.name;
			var snrData = snrTotal.data;
			var snrColor = snrTotal.color;
			var snrX = snrTotal.x;
			var snrRate = snrTotal.rate;
			var snrSeries = [];
			for(var i=0;i<snrName.length;i++){
				var t = {
				name : snrName[i],
				type : 'bar',
				itemStyle: {
					normal: {
						label : {
							show: true, 
							position: 'top',
							formatter: function(params) {//鼠标移到柱状图上得提示信息
			                    var res = "";
			                    var datas = params.series.data;
			                    for (var j = 0; j < snrX.length; j++) {
			                    	if(params.name == snrX[j]){
			                    		//res = params.value+"<br/>"+snrRate[j];
			                    		res = snrRate[j];
			                    	}
			                    }
			                    return res;
			                },
			                textStyle: {
	                            /*color: function(params) {
	    	    					var colorList = rsrpColor;
	    	    					return colorList[params.dataIndex]
	    	    				}*/
			                	 color: '#555'
	                        }
						},
						color: function(params) {
	    					var colorList = rsrpColor;
	    					return colorList[params.dataIndex]
	    				}
					}
				},
	            data : snrData[i]
				};
				snrSeries[i] = t;
			}
			snrQuota = createDataColorful('SNR指标区间占比' ,snrX,snrRate,'里程(km)',snrSeries ,false,'shadow',-45);
			
			require(
			        [
			            'echarts',
			            'echarts/chart/bar',
			            'echarts/chart/line',
			            'echarts/chart/pie'
			        ],
			        function (ec) {
			        	var ecConfig = require('echarts/config');
			        	//无线宽带覆盖率
			        	var wirelessQuotaChart = ec.init(document.getElementById('wirelessCover'), 'macarons');
			        	wirelessQuotaChart.setOption(wirelessQuota);
			            window.onresize = wirelessQuotaChart.resize;
			            //下行链路宽带覆盖率
			        	var broadbandQuotaChart = ec.init(document.getElementById('broadbandCover'), 'macarons');
			        	broadbandQuotaChart.setOption(broadbandQuota);
			            window.onresize = broadbandQuotaChart.resize;
			            //网络掉线率
			        	var networkRateQuotaChart = ec.init(document.getElementById('networkRate'), 'macarons');
			        	networkRateQuotaChart.setOption(networkRateQuota);
			            window.onresize = networkRateQuotaChart.resize;
			            //业务掉线率
			        	var pingRateQuotaChart = ec.init(document.getElementById('pingRate'), 'macarons');
			        	pingRateQuotaChart.setOption(pingRateQuota);
			            window.onresize = pingRateQuotaChart.resize;
			            //RSRP指标区间占比
			        	var rsrpQuotaChart = ec.init(document.getElementById('rsrpRate'), 'macarons');
			        	rsrpQuotaChart.setOption(rsrpQuota);
			            window.onresize = rsrpQuotaChart.resize;
			            //SNR指标区间占比
			        	var snrQuotaChart = ec.init(document.getElementById('snrRate'), 'macarons');
			        	snrQuotaChart.setOption(snrQuota);
			            window.onresize = snrQuotaChart.resize;
			        }
			);
			pageSevenCharts(data.wirelessCover.prev,data.wirelessCover.next,wirelessX[0],wirelessX[wirelessX.length],'wirelessCoverCheck','wirelessCoverButton');
			pageSevenCharts(data.broadbandCover.prev,data.broadbandCover.next,broadbandX[0],broadbandX[broadbandX.length],'broadbandCoverCheck','broadbandCoverButton');
			pageSevenCharts(data.networkRate.prev ,data.networkRate.next ,networkRateX[0] ,networkRateX[networkRateX.length] ,'networkRateCheck' ,'networkRateButton');
			pageSevenCharts(data.pingRate.prev ,data.pingRate.next ,pingRateX[0] ,pingRateX[pingRateX.length] ,'pingRateCheck' ,'pingRateButton');
			$("#modal-backdrop").hide();
		}	
	});
}

$(function(){
	/**
	 * 无线覆盖率分页
	 */
	$('.wirelessCoverCheck').live('click',function(){
		var quota = 'wireless';
		var time = $(this).attr('time');
		var pageType = $(this).attr('pagetype');
		$("#modal-backdrop").show();
		$.ajax({
			type:"post",
			url:contextPath + "/home/queryChartsInfo",
			data:{time:time,pageType:pageType,quota:quota},
			async: false,
			success:function(data){
				//无线宽带覆盖率
				var cover = data.cover;
				var name = cover.name;
				var coverData = cover.data;
				var x = cover.x;
				var ax = cover.ax;
				var series = [];
				for(var i=0;i<name.length;i++){
					var t = {
					name : name[i],
					type : 'line',
		            data : coverData[i]
					};
					series[i] = t;
				}
				quota = createData('覆盖率' ,'vertical',name,ax,'覆盖率(%)',series ,false,'line',-45);
				require(
				        [
				            'echarts',
				            'echarts/chart/line',
				        ],
				        function (ec) {
				        	var ecConfig = require('echarts/config');
				        	//无线宽带覆盖率
				        	var quotaChart = ec.init(document.getElementById('wirelessCover'), 'macarons');
				        	quotaChart.setOption(quota);
				            window.onresize = quotaChart.resize;
				        }
				);
				pageSevenCharts(cover.prev,cover.next,x[0],x[x.length-1],'wirelessCoverCheck','wirelessCoverButton');
				$("#modal-backdrop").hide();
			}	
		});
	});
	
	/**
	 * 下行链路带宽分页
	 */
	$('.broadbandCoverCheck').live('click',function(){
		var quota = 'broadband';
		var time = $(this).attr('time');
		var pageType = $(this).attr('pagetype');
		$("#modal-backdrop").show();
		$.ajax({
			type:"post",
			url:contextPath + "/home/queryChartsInfo",
			data:{time:time,pageType:pageType,quota:quota},
			async: false,
			success:function(data){
				//无线宽带覆盖率
				var cover = data.cover;
				var name = cover.name;
				var coverData = cover.data;
				var x = cover.x;
				var ax = cover.ax;
				var series = [];
				for(var i=0;i<name.length;i++){
					var t = {
					name : name[i],
					type : 'line',
		            data : coverData[i]
					};
					series[i] = t;
				}
				quota = createData('下行链路带宽' ,'vertical',name,ax,'速率(Mbps)',series ,false,'line',-45);
				
				require(
				        [
				            'echarts',
				            'echarts/chart/line',
				        ],
				        function (ec) {
				        	var ecConfig = require('echarts/config');
				        	//无线宽带覆盖率
				        	var quotaChart = ec.init(document.getElementById('broadbandCover'), 'macarons');
				        	quotaChart.setOption(quota);
				            window.onresize = quotaChart.resize;
				        }
				);
				pageSevenCharts(cover.prev,cover.next,x[0],x[x.length-1],'broadbandCoverCheck','broadbandCoverButton');
				$("#modal-backdrop").hide();
			}	
		});
	});
	
	/**
	 * 网络掉线率分页
	 */
	$('.networkRateCheck').live('click',function(){
		var quota = 'networkRate';
		var time = $(this).attr('time');
		var pageType = $(this).attr('pagetype');
		$("#modal-backdrop").show();
		$.ajax({
			type:"post",
			url:contextPath + "/home/queryChartsInfo",
			data:{time:time,pageType:pageType,quota:quota},
			async: false,
			success:function(data){
				//无线宽带覆盖率
				var cover = data.cover;
				var name = cover.name;
				var coverData = cover.data;
				var x = cover.x;
				var ax = cover.ax;
				var series = [];
				for(var i=0;i<name.length;i++){
					var t = {
					name : name[i],
					type : 'line',
		            data : coverData[i]
					};
					series[i] = t;
				}
				quota = createData('网络掉线率' ,'vertical',name,ax,'%',series ,false,'line',-45);
				
				require(
				        [
				            'echarts',
				            'echarts/chart/line',
				        ],
				        function (ec) {
				        	var ecConfig = require('echarts/config');
				        	//无线宽带覆盖率
				        	var quotaChart = ec.init(document.getElementById('networkRate'), 'macarons');
				        	quotaChart.setOption(quota);
				            window.onresize = quotaChart.resize;
				        }
				);
				pageSevenCharts(cover.prev,cover.next,x[0],x[x.length-1],'networkRateCheck','networkRateButton');
				$("#modal-backdrop").hide();
			}	
		});
	});
	
	/**
	 * 业务掉线率分页
	 */
	$('.pingRateCheck').live('click',function(){
		var quota = 'pingRate';
		var time = $(this).attr('time');
		var pageType = $(this).attr('pagetype');
		$("#modal-backdrop").show();
		$.ajax({
			type:"post",
			url:contextPath + "/home/queryChartsInfo",
			data:{time:time,pageType:pageType,quota:quota},
			async: false,
			success:function(data){
				//无线宽带覆盖率
				var cover = data.cover;
				var name = cover.name;
				var coverData = cover.data;
				var x = cover.x;
				var ax = cover.ax;
				var series = [];
				for(var i=0;i<name.length;i++){
					var t = {
					name : name[i],
					type : 'line',
		            data : coverData[i]
					};
					series[i] = t;
				}
				quota = createData('业务掉线率' ,'vertical',name,ax,'%',series ,false,'line',-45);
				
				require(
				        [
				            'echarts',
				            'echarts/chart/line',
				        ],
				        function (ec) {
				        	var ecConfig = require('echarts/config');
				        	//无线宽带覆盖率
				        	var quotaChart = ec.init(document.getElementById('pingRate'), 'macarons');
				        	quotaChart.setOption(quota);
				            window.onresize = quotaChart.resize;
				        }
				);
				pageSevenCharts(cover.prev,cover.next,x[0],x[x.length-1],'pingRateCheck','pingRateButton');
				$("#modal-backdrop").hide();
			}	
		});
	});
	
	
});