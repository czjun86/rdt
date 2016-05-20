$(document).ready(function () {
    //屏蔽页面右键菜单
    unContextmenu();
	//时间控件
    changeDateRange();
});
//屏蔽页面右键菜单
function unContextmenu(){
	//整个页面的右键文本目录事件
    $(document).bind("contextmenu", function (event) {
        if (document.all)
            window.event.returnValue = false; // for IE  
        else
            event.preventDefault();
    });
}

function unRegChartRightClickEvents(){
	$("#hdline_div").unbind("mouseup");
}

//时间粒度变更
function changeDateRange(){
	//选中的时间粒度
	var chd = $("#timegransel").val();
	//设置时间粒度条件值
	$("#timeGranule").val(chd);
	
	var dateRange = $("#reportrange");
	if(chd=='1'){
		dateRange.attr("dateFormat","YYYY.MM.DD");
		dateRange.attr("beforNdays","1");
		dateRange.attr("timeP","false");
		dateRange.attr("showDays","true");
		if($("#qryflg").val()!=1){//判断是否带时间跳转入页面
			//初始化事件选用传入时间
		    var str = $("#queryTime_back").val();
		    $('#queryTime').val(str);
		}else{
			var nday = new Date(new Date()-1*24*60*60*1000);
			var yday = new Date(new Date()-1*24*60*60*1000);
			$('#queryTime').val(yday.format("yyyy.MM.dd")+' - '+nday.format("yyyy.MM.dd"));
		}
	}else if(chd=='2'){
		dateRange.attr("dateFormat","YYYY.MM.DD");
		dateRange.attr("beforNdays","1");
		dateRange.attr("timeP","false");
		dateRange.attr("showDays","true");
		if($("#qryflg").val()!=1){//判断是否带时间跳转入页面
			//初始化事件选用传入时间
		    var str = $("#queryTime_back").val();
		    $('#queryTime').val(str);
		}else{
			var nday = new Date(new Date()-1*24*60*60*1000);
			var yday = new Date(new Date()-1*24*60*60*1000);
			$('#queryTime').val(yday.format("yyyy.MM.dd")+' - '+nday.format("yyyy.MM.dd"));
		}
	}else if(chd=='3'){
		dateRange.attr("dateFormat","YYYY.MM");
		dateRange.attr("beforNdays","30");
		dateRange.attr("timeP","false");
		dateRange.attr("showDays","false");
		if($("#qryflg").val()!=1){//判断是否带时间跳转入页面
			//初始化事件选用传入时间
		    var str = $("#queryTime_back").val();
		    $('#queryTime').val(str);
		}else{
			var now = new Date().format("yyyy.MM");
			var year = now.split('.')[0];
			var month = now.split('.')[1];
			var nowmonth = 0;
			var nowmonth2 = "0";
			var nowyear = 0;
			var ymonth = 0;
			var ymonth2 = "0";
			var yyear = 0;
			if(month!=1){
				nowmonth = month-1;
				if(nowmonth<10){
					nowmonth2 = "0"+nowmonth;
				}else{
					nowmonth2 = nowmonth;
				}
				nowyear = year;
			}else{
				nowmonth = 12;
				if(nowmonth<10){
					nowmonth2 = "0"+nowmonth;
				}else{
					nowmonth2 = nowmonth;
				}
				nowyear = year-1;
			}
			if(month>= 4){
				ymonth = month-3;
				if(ymonth<10){
					ymonth2 = "0"+ymonth;
				}else{
					ymonth2 = ymonth;
				}
				yyear = year;
			}else{
				ymonth = 12-(3-month);
				if(ymonth<10){
					ymonth2 = "0"+ymonth;
				}else{
					ymonth2 = ymonth;
				}
				yyear = year-1;
			}
			$('#queryTime').val(yyear+'.'+ymonth2+' - '+nowyear+'.'+nowmonth2);
		}
	}
	//设置日期控件
	DateRanges.init('reportrange');
	//更改标记
	$("#qryflg").val("1");
}


Date.prototype.format = function(format){
	var o = {
	"M+" : this.getMonth()+1, //month
	"d+" : this.getDate(), //day
	"h+" : this.getHours(), //hour
	"m+" : this.getMinutes(), //minute
	"s+" : this.getSeconds(), //second
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter
	"S" : this.getMilliseconds() //millisecond
	};

	if(/(y+)/.test(format)) {
	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}

	for(var k in o) {
	if(new RegExp("("+ k +")").test(format)) {
	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
	}
	}
	return format;
};
