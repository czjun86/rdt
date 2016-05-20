

var x_num=10;//固定X轴个数
var now_point=0;//当前第几个点
/**
 * 
 * @param xrotate X轴数组
 * @param pci PCI数据
 * @param data 指标数据
 * @param name 指标名称
 * @returns {___anonymous120_2736}
 */
function creatDateKpi(xrotate,pci,pci_data,obj){
	var dis=[];
	for(var kk in obj.data){
		if(obj.data[kk]!="-")
			dis.push(parseFloat(obj.data[kk]));
	}
	
	var min_kpi=Math.min.apply(Math,dis);
	var max_kpi=Math.max.apply(Math,dis);
	if(min_kpi%10>0)
	min_kpi=min_kpi-(min_kpi%10);
	if(min_kpi%10<0)
	min_kpi=min_kpi-(10+min_kpi%10);
	if(max_kpi%10>0)
	max_kpi=max_kpi+(10-(max_kpi%10));
	if(max_kpi%10<0)
	max_kpi=max_kpi-(max_kpi%10);
	var option = {
		    title: {
		        text: obj.name,
		        x: 'center',
		        textStyle:{
		        	fontSize: 12,
		            //fontWeight: 'bolder',
		            fontFamily:'微软雅黑',
		            color: '#444'
		        }
		    },
		    tooltip: {
		        trigger: 'axis',
	        	axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : "line"        // 默认为直线，可选为：'line' | 'shadow'
                }
		    ,
                formatter:function(params,ticket,callback){
                	var res="";
                	var index=parseInt(ticket.split(":")[1])+now_point;
                	if(pci[index]==null)pci[index]="N/A";
                	if(params[1].value=="-")params[1].value="N/A";
                     res += params[1].seriesName + ' : ' + params[1].value+'<br/>PCI:' +pci[index];
                	  setTimeout(function (){
                          // 仅为了模拟异步回调
                          callback(ticket, res);
                      }, 100);
                      return 'loading';
                }
		    },
		    //设置网格及边距
		    grid:{
		    	x:40,
		    	y:30,
		    	x2:10,
		    	borderColor:"rgba(255,255,255,1)",
		    	borderWidth:0.1,
		    	y2:30},
		    dataZoom: {
		        show: false,
				realtime: true,
				start:0,
				end:(x_num/(xrotate.length-1))*100
		    },
		    backgroundColor:'rgba(255,255,255,1)',
		    calculable: false,//禁止拖拽
		    xAxis: [
		        {
		            type: 'category',
		            boundaryGap : true,//数据出现在点上
		            nameTextStyle:{
		            	fontSize: 10,
			            color: '#444'
		            },
		            data: xrotate
		            ,
		            splitLine:{show:false},
		            axisLabel: { 
		            	interval:1 ,
		            	textStyle:{
                            fontSize: 10,
                            color: '#444'
		            	},
		            	//rotate:-45
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
		            splitLine:{show:false},
		            nameTextStyle:{
		            	fontSize: 10,
			            color: '#939393'
		            },
		            'name': "",
		            //boundaryGap:[1,1],
		            precision:0,
		            min:min_kpi,
		            max:max_kpi
		        }
		    ],
		    series: [
		             //指标
		               {
            name:obj.name,
            type:'line',
            data:obj.data
            ,markPoint : {
            	 symbolSize:15,
                data : [
                    {name :obj.name, value:obj.data[0], xAxis:0, yAxis:obj.data[0]=="-"?0:obj.data[0]}
                ]
            }
        }
 ,
        //PCI
        {
            name:"PCI",
            type:'bar',
            barWidth: 0,
            stack: '1',
            position: 'inside',
            itemStyle : { normal: {label : {
                show: true,
                textStyle : {
                    fontSize : '10',
                    fontFamily : '微软雅黑',
                    fontWeight : 'bold'
                }
            }}},
            data:pci_data //显示变化的PCI值，没变的补空值
        }
		 ]
		};
return option;
}



/**
 * 点的图表连动事件
 * type:1-下一步，2－上一步，3-任意选择点(point_index不为空，其他为空)
 */
function pointSkip(points,report_list,type){
	switch (parseInt(type)) {
	case 1:
		if(now_point<points.length-1)
		now_point++;
		break;
    case 2:
    	if(now_point>0)
    	now_point--;
		break;
	default:
		break;
	}
	eacharts_dataZoomChage(report_list);
	markerFlicker(points);
}


/**mycharts图表对象数组
*options,图表属性数组
*now,－当前索引
*list-数据
 * 
 */
function eacharts_dataZoomChage(report_list){
	 var name="";
	for(var i=0;i<echarts_kpi.length;i++){
		 echarts_kpi[i].clear();
		 optionas[i].dataZoom.start =((now_point)/(report_list[i].data.length-1))*100;
		 optionas[i].dataZoom.end =((now_point+x_num)/(report_list[i].data.length-1))*100;
		 echarts_kpi[i].setOption(optionas[i]);
		 switch (i) {
			case 0:
				name="rsrp";
			break;
			case 1:
				name="snr";
				break;
			case 2:
				name="下行";
				break;
			case 3:
				name="延时";
				break;
			default:
				break;
			}
		 var xx_p=0;
		 var arr_zoom=echarts_kpi[i].component.xAxis.option.series[0].data;
		 xx_p=$.inArray(report_list[i].data[now_point], arr_zoom);
		 echarts_kpi[i].delMarkPoint(0,name);
		 echarts_kpi[i].addMarkPoint(0, {data:[{name : name, value : report_list[i].data[now_point], xAxis:xx_p, yAxis:report_list[i].data[now_point]=="-"?0:report_list[i].data[now_point]}]});

		
	}
	
}

/**
 * 当前点跳动
 * @param points
 */

function  markerFlicker(points){
	 //删除闪烁点
	if(special_point.length==3){
		map.removeOverlay(special_point[2]);
		special_point.pop();
	}
	 //生成闪烁点
	 var vectorStar = new BMap.Marker(new BMap.Point(points[now_point].lng,points[now_point].lat), {
		  // 初始化五角星symbol
		   icon: new BMap.Symbol(BMap_Symbol_SHAPE_CIRCLE, {
		    scale: 1,
		    strokeWeight: 2,
		    strokeColor:"red",
		    fillOpacity:1,
		    fillColor: points[now_point].state
		  })
		});	
	 map.addOverlay(vectorStar);
	 var scale=2;
	 setInterval(function() {
		 vectorStar.setIcon(new BMap.Symbol(BMap_Symbol_SHAPE_CIRCLE, {
			    scale:scale,
			    strokeWeight: 2,
			    strokeColor:"red",
			    fillOpacity:1,
			    fillColor: points[now_point].state
			  }));
		 if(scale<10)
		 scale+=1;
		 if(scale==10)
		 scale=2;
		}, 100);
	 special_point.push(vectorStar);
}