var commUtils = {};
/* string empty */
commUtils.isNotEmptyText = function(text) {
	return (text != undefined) && (text != null) && (text !== "");
};
/* string empty undefined */
commUtils.isNotEmptyTextAndNotUndefined = function(text) {
	return commUtils.isNotEmptyText(text) && (text != "undefined");
};

/* escape HTML */
commUtils.escapeHTML = function(html) {
	var trans = {
		'&' : '&amp;',
		'<' : '&lt;',
		'>' : '&gt;',
		'"' : '&quot;',
		"'" : '&#x27;'
	};
	return (html + '').replace(/[&<>\"\']/g, function(c) {
		return trans[c];
	});
};
/* escape js */
commUtils.escapeJavaScript = function(js) {
	var trans = {
		'\'' : '\\\'',
		'"' : '\\\"',
		'\\' : '\\\\',
		'\n' : '\\n',
		"\r" : '\\r',
		"\t" : '\\t',
		"\f" : '\\f'
	};
	return (js + '').replace(/[\'\"\\\n\r\t\f]/g, function(c) {
		return trans[c];
	});
};
/* allNumber */
commUtils.isAllNumber = function(str) {
	if (/^\d+$/.test(str)) {
		return true;
	}
	return false;
};
/* toNumber */
commUtils.toNumber = function(str) {
	if (commUtils.isAllNumber(str)) {
		str = new Number(str).valueOf();
		return str;
	}
};

/**
 * ajax 异步请求
 */
commUtils.ajax = function(settings) {
	/*
	var $ = jQuery;
	if (!$)
		throw Error("This function must rely on the jquery lib.");
		*/

};

commUtils.ieVersion = function() {
    var bro = $.browser;		
	if(!bro.msie) return nulll;
	if (bro.version == "6.0") {
		return 6;
	} else if (bro.version == "7.0") {
		return 7;
	} else if (bro.version == "8.0") {
		return 8;
	} else if (bro.version == "9.0") {
		return 9;
	}
	return 10;
};

commUtils.browser = function(){
	var bro = $.browser;	
	
	if(bro.msie) return "ie";

    if(bro.mozilla) return "mozilla";

    if(bro.safari) return "safari";

    if(bro.opera) return "opera";
};
/**
 * my97 快速选择默认时间
 * @param _this
 * @returns {String}
 */
commUtils.defaultDate = function(_this){
	var _val = $("#" + _this).val();
	var year,month;
	if(_val != null && _val !=""){
		year = parseInt(_val.split("-")[0]);
		month = parseInt(_val.split("-")[1]);
	}else{ return;}
	if(year && month){
		var arr = [];
		var y="",m="";
		for(var i = 5; i >= 1; i --){
			if(i >= 4){
				if(month + i - 3 > 12){
					y = year + 1;
					m = month + i - 3 - 12;
				}else{
					y = year;
					m = month + i - 3;
				}
			}else if(i >= 1 && i <= 2){
				if(month - i < 0){
					y = year - 1;
					m = month - i + 12;
				}else{
					y = year;
					m = month - (3 - i);
				}
			}else{
				y = year;
				m = month;
			}
			
			arr[5-i] = y + "-" + addZero(m);
		}
		var str = "";
		for(var i = 0; i < arr.length; i ++){
			str += "'" + arr[i] + "',";
		}
		if(str != null && str != ""){
			str = str.substring(0,str.length - 1);
		}
		
		$("#" + _this).attr('onFocus','WdatePicker({Mchanged:commUtils.defaultDate("'+_this+'"),dateFmt:"yyyy-MM",qsEnabled:true,quickSel:['+str+']});');
	}
};

/**
*比较时间大小
*/
function compareTime(s_time,e_time){
	var s = s_time.split("-");
	var e = e_time.split("-");
	if(parseInt(e[0])==parseInt(s[0])){
		if(parseInt(e[1])==parseInt(s[1])){
			if(parseInt(e[2])>parseInt(s[2])){//年份月份相同天数大于结束时间
				return true;
			}else{
				return false;
			}
		}else if(parseInt(e[1])>parseInt(s[1])){//年份相同结束时间月份大于起始时间
			return true;
		}else{
			return false;
		}
	}else if(parseInt(e[0])>parseInt(s[0])){//结束时间年份大于起始时间
		return true;
	}else{
		return false;
	}
}

/**
 * 报表提交时间验证
 */
function validateTime(t_type,s_time,e_time,m_time){
	if(parseInt(t_type)==1&&s_time!=''&&e_time!=''&&typeof(s_time)!='undefined'&&typeof(e_time)!='undefined'){
		if(!compareTime(s_time,e_time)){
			$.messager.alert("提示", "起始时间应该小于结束时间！", "warning");
			return false;
		}else{
			return true;
		}
	}else if(parseInt(t_type)==2&&m_time!=''&&typeof(m_time)!='undefined'){
		return true;
	}else{
		$.messager.alert("提示", "时间不能为空！", "warning");
		return false;
	}
}