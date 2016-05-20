require.config({
	    paths: {
	        echarts: contextPath+'/scripts/plugins/echarts/www/js'
	    }
	});
var echarts_kpi=[];//组装图表的对象
var optionas=[];//组装图表属性的对象


/**
 * 加载生成曲线指标图
 * 需要生成的多个指标kpi_ids
 * list数据
 */
function loadChartKpi(kpi_ids,list){
	 require(
		        [
		            'echarts',
		            'echarts/chart/bar',
		            'echarts/chart/line',
		            'echarts/chart/map'
		        ],
		        function (echarts) {
		        	//设置PCI值
		        	var pci_data=[],pci_last=0;
		        	var pp=list.pci;
		        	for(var k=0;k<pp.length;k++){
		        		if(null==pp[k]||pp[k]=="-999"||pp[k]=="-998"){
		        			pci_data.push("-")	;
		        		}else{
		        			if(k==0){
		        				pci_data.push(pp[k]);
		        			}else{
		        				if (pp[k]==pci_last) {
		        					pci_data.push("-");
		        				} else {
		        					pci_data.push(pp[k]);
		        				}
		        			}
		        		}
		        		pci_last=pp[k];
		        	}
		        	//循环图表
		        	for(var i=0;i<kpi_ids.length;i++){
		        		 var id_d="kpi_"+i;
		        		 var 	myChart ;
		        		 if(echarts_kpi.length==4){
		        			 myChart= echarts_kpi[i];
		        		 }else{
		        			 myChart= echarts.init(document.getElementById(id_d));
		        			 echarts_kpi.push(myChart);
		        		 }
		        		 var option=creatDateKpi(list.xrotate,list.pci,pci_data,list.kpi[i]);
		        		 myChart.setOption(option);
		        		 optionas.push(option);
		        		
		        	  }
		        	 if(echarts_kpi.length==4){
		        		 echarts_kpi[0].connect([echarts_kpi[1], echarts_kpi[2], echarts_kpi[3]]); 
		        		 echarts_kpi[1].connect([echarts_kpi[0], echarts_kpi[2], echarts_kpi[3]]); 
		        		 echarts_kpi[2].connect([echarts_kpi[0], echarts_kpi[1], echarts_kpi[3]]); 
		        		 echarts_kpi[3].connect([echarts_kpi[1], echarts_kpi[2], echarts_kpi[0]]); 
		        	 }
		        		 
		        	 
		        });
	
	
}

