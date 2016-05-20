var DateRanges = function () {
    var handleDateRangePickers = function (idName) {
    	
        if (!jQuery().daterangepicker) {
            return;
        }

        $('#defaultrange').daterangepicker({
                opens: (App.isRTL() ? 'left' : 'right'),
                format: 'YYYY.MM.DD',
                separator: ' to ',
                startDate: moment().subtract('days', 29),
                endDate: moment(),
                minDate: '2012.01.01',
                maxDate: '2018.12.31',
            },
            function (start, end) {
                console.log("Callback has been called!");
                $('#defaultrange input').val(start.format('YYYY.MM.DD') + ' - ' + end.format('YYYY.MM.DD'));
            }
        );        
        
        //自定义变量
        //时间格式
        //var dateFormat = "YYYY.MM.DD HH:00";
        var dateFormat = $('#'+idName).attr("dateFormat")!=null?$('#'+idName).attr("dateFormat"):"YYYY.MM.DD";
        //初始化开始时间(当前时间前beforNdays天)
        var beforNdays = $('#'+idName).attr("beforNdays")!=null?parseInt($('#'+idName).attr("beforNdays")):1;
        //是否启用小时
        var timeP = $('#'+idName).attr("timeP")!=null?parseBool($('#'+idName).attr("timeP")):false;
        //是否启用分钟
        var timeM = $('#'+idName).attr("timeM")!=null?parseBool($('#'+idName).attr("timeM")):false;
        //是否启用日期
        var showDays = $('#'+idName).attr("showDays")!=null?parseBool($('#'+idName).attr("showDays")):true;
        //minDate
        var mindate = $('#'+idName).attr("mindate")!=null?$('#'+idName).attr("mindate"):"2010.01.01";
        //maxDate
        var maxdate = $('#'+idName).attr("maxdate")!=null?$('#'+idName).attr("maxdate"):moment();
        
        //创建时间控件
        $('#'+idName).daterangepicker(idName,{
                opens: (App.isRTL() ? 'left' : 'right'),
                //startDate: moment().subtract('days', 29),
                startDate: moment().subtract('days', beforNdays),
                endDate: moment(),
                minDate: mindate,
                maxDate: maxdate,
//                dateLimit: {
//                    days: 0,
//                	month:0
//                },
                //月份和年份是否可以下拉选择
                showDropdowns: true,
                //日期格子的显示
                showDaysNumbers:showDays,
                //分钟数的显示
                showMinute:timeM,
                showWeekNumbers: false,
                timePicker12Hour: false,
                timePicker: timeP,
                timePickerIncrement: 1,
                
//                ranges: {
//                    'Today': [moment(), moment()],
//                    'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
//                    'Last 7 Days': [moment().subtract('days', 6), moment()],
//                    'Last 30 Days': [moment().subtract('days', 29), moment()],
//                    'This Month': [moment().startOf('month'), moment().endOf('month')],
//                    'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
//                },
                buttonClasses: ['btn'],
                applyClass: 'green',
                cancelClass: 'default',
                format: dateFormat,
                separator: ' to ',
                locale: {
                    applyLabel: '确定',
                    cancelLabel: '取消',
                    fromLabel: '从',
                    toLabel: '到',
                    weekLabel: '周',
                    //customRangeLabel: 'Custom Range',
                    daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                    monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                    firstDay: 1
                }
            },
            function (start, end) {
                console.log("Callback has been called!");
                $('#'+idName+' input').val(start.format(dateFormat) + ' - ' + end.format(dateFormat));
            }
        );
        //Set the initial state of the picker label
//        $('#reportrange input').val(moment().subtract('days', 29).format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));
        
    }

    return {
        //main function to initiate the module
        init: function (idName) {
            return handleDateRangePickers(idName);
        }
    };

}();

//转换字符串为布尔值
function parseBool(str){
	if(str=="true"){
		return true;
	}else{
		return false;
	}
}