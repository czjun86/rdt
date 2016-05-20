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
