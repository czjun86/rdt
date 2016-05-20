/**
 * @param titleName 图表名称
 * @param legendType
 * @param rolename 统计运营商名称
 * @param Xname x轴刻度名称
 * @param Yname	y轴名称
 * @param Sers 每个运营商数据
 * @param flag 显示数据节点是否在X刻度对应纵轴上
 * @param axisType 鼠标移上去后X轴以线形或是阴影展示
 * @returns {___anonymous100_910}
 */
function createData(titleName,legendType,rolename,Xname,Yname,Sers,flag,axisType,xrotate){
	var option = {
			    title: {
			        text: titleName,
			        x: 'center',
			        textStyle:{
			        	fontSize: 14,
			            //fontWeight: 'bolder',
			            fontFamily:'微软雅黑',
			            color: '#444'
			        }
			    },
			    tooltip: {
			        trigger: 'axis',
		        	axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	                    type : axisType        // 默认为直线，可选为：'line' | 'shadow'
	                }
			    },
			    dataZoom: {
			        show: Xname.length>30?true:false,
					backgroundColor:'rgba(0,0,0,0)',
					showDetail:true
			    },
			    grid: {
			        y2: 90
			    },
			    legend: {
			    	show:(legendType!=null?true:false),
			    	orient:legendType,
			    	x: 'right',
			        y: 'center',
			        data: rolename
			    },
			    toolbox: {
			        show: false/*,
			        feature: {
			            saveAsImage: {show: true}
			        }*/
			    },
			    backgroundColor:'rgba(255,255,255,1)',
			    calculable: false,//禁止拖拽
			    xAxis: [
			        {
			            type: 'category',
			            boundaryGap : true,//数据出现在点上
			            nameTextStyle:{
			            	fontSize: 12,
				            color: '#444'
			            },
			            data: Xname,
			            axisLabel: { 
			            	textStyle:{
	                            fontSize: 10,
	                            color: '#444'
			            	},
			            	rotate:xrotate
                        },
			            areaStyle:{
			            	color: [
			            	        'rgba(250,250,250,1)'
			            	    ]
			            }
			        }
			    ],
			    yAxis: [
			        {
			            type: 'value',
			            nameTextStyle:{
			            	fontSize: 12,
				            color: '#939393'
			            },
			            'name': Yname
			        }
			    ],
			    series: Sers
			};
	return option;
}

/**
 * @param titleName 图表名称
 * @param legendType
 * @param rolename 统计运营商名称
 * @param Xname x轴刻度名称
 * @param Yname	y轴名称
 * @param Sers 每个运营商数据
 * @param flag 显示数据节点是否在X刻度对应纵轴上
 * @param axisType 鼠标移上去后X轴以线形或是阴影展示
 * @returns {___anonymous100_910}
 */
function createDataColorful(titleName,Xname,Xrate,Yname,Sers,flag,axisType,xrotate){
	var option = {
			    title: {
			        text: titleName,
			        x: 'center',
			        textStyle:{
			        	fontSize: 14,
			            //fontWeight: 'bolder',
			            fontFamily:'微软雅黑',
			            color: '#444'
			        }
			    },
			    tooltip: {
			        trigger: 'axis',
		        	axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	                    type : axisType        // 默认为直线，可选为：'line' | 'shadow'
	                },
	                formatter: function(params) {//鼠标移到柱状图上得提示信息
	                    var res = '<div>';
	                    for (var i = 0, l = params.length; i < l; i++) {
	                        /*res += '<strong>'+params[i].seriesName + ' : ' + params[i].value +'</strong>';
		                    res += '<br/><strong>占比' + ' : ' +Xrate[params[0].dataIndex]+'</strong>';*/
	                    	res += '<strong>'+params[i].value +'</strong>';
		                    //res += '<br/>'+Xrate[params[0].dataIndex]+'</strong>';
	                    }
	                    res += '</div>';
	                    return res;
	                }
			    },
			    dataZoom: {
			        show: Xname.length>30?true:false,
					backgroundColor:'rgba(0,0,0,0)',
					showDetail:true
			    },
			    grid: {
			        y2: 90
			    },
			    toolbox: {
			        show: false/*,
			        feature: {
			            saveAsImage: {show: true}
			        }*/
			    },
			    backgroundColor:'rgba(255,255,255,1)',
			    calculable: false,//禁止拖拽
			    xAxis: [
			        {
			            type: 'category',
			            boundaryGap : true,//数据出现在点上
			            nameTextStyle:{
			            	fontSize: 12,
				            color: '#444'
			            },
			            data: Xname,
			            axisLabel: { 
			            	textStyle:{
	                            fontSize: 10,
	                            color: '#444'
			            	},
			            	rotate:xrotate
                        },
			            areaStyle:{
			            	color: [
			            	        'rgba(250,250,250,1)'
			            	    ]
			            }
			        }
			    ],
			    yAxis: [
			        {
			            type: 'value',
			            nameTextStyle:{
			            	fontSize: 12,
				            color: '#939393'
			            },
			            'name': Yname
			        }
			    ],
			    series: Sers
			};
	return option;
}

/**
 * 生成饼图对象
 * @param titleName//标题
 * @param subtext//副标题
 * @param legendType//类型
 * @param proName//参与目标
 * @param proData//目标数据
 * @param tipName//数据名称
 * @param unitName//单位名称
 * @returns {___anonymous_optionPro1}
 */
function PieChart(titleName ,subtext,legendType,proName,proData,tipName,unitName){
	var sreData= [];
	for(var i=0;i<proName.length;i++){
		var t = {value: proData[i], name: proName[i]};
		sreData[i] = t;
	}
	var proSeries  = [
				        {
				            name: tipName,
				            type: 'pie',
				            radius: '55%',
				            center: ['50%', '60%'],
				            data: sreData
				        }
				    ];
	var option = {
		    title: {
		        text: titleName,
		        subtext: subtext,
		        x: 'center',
		        textStyle:{
		        	fontSize: 14,
		            //fontWeight: 'bolder',
		            fontFamily:'微软雅黑',
		            color: '#444'
		        }
		    },
		    backgroundColor:'rgba(255,255,255,1)',
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ("+unitName+")({d}%)"
		    },
		    legend: {
		        orient: legendType,
		        x:"right",
		        y: 'center',
		        data: proName
		    },
		    toolbox: {
		        show: true/*,
		        feature: {
		            saveAsImage: {show: true}
		        }*/
		    },
		    calculable: false,//禁止拖拽
		    series: proSeries
	};
	return option;
}