
var map = null;//当前百度底图
var thZoom=11;
var firstZoom=8;//变化的ZOOM
var date;
var colorArray = new Array('#CDCDCD',"#ff0000", "#ff00ff", "#ffff00", "#00ffff", "#0000ff", "#00ff00");
//地图样式
var styleJson = [  {
    "featureType": "background",
    "elementType": "geometry.fill",
    "stylers": {
              "color": "#F8F8F8"
    }
},
{
    "featureType": "label",
    "elementType": "labels.text.stroke",
    "stylers": {"visibility" : "off"}
},
{
    "featureType": "road",
    "elementType": "geometry.fill",
    "stylers": {
              "color": "#ffffff"
    }
},
{
    "featureType": "road",
    "elementType": "geometry.stroke",
    "stylers": {
              "color": "#bababa"
    }
},
{
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": {
              "color": "#767676"
    }
}, {
	"featureType" : "point",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "local",
	"elementType" : "labels",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "water",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "poi",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
} ];



var styleRoadJson = [  {
    "featureType": "background",
    "elementType": "geometry.fill",
    "stylers": {
              "color": "#F8F8F8"
    }
},
{
    "featureType": "label",
    "elementType": "labels.text.stroke",
    "stylers": {"visibility" : "off"}
},
{
    "featureType": "road",
    "elementType": "geometry.fill",
    "stylers": {
              "color": "#ffffff"
    }
},
{
    "featureType": "road",
    "elementType": "geometry.stroke",
    "stylers": {
              "color": "#bababa"
    }
},
{
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": {
              "color": "#767676"
    }
}, {
	"featureType" : "point",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "highway",
	"elementType" : "geometry",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "railway",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "local",
	"elementType" : "labels",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "water",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
}, {
	"featureType" : "poi",
	"elementType" : "all",
	"stylers" : {
		"visibility" : "off"
	}
} ]
;
var opts = {
		  width : 20,     // 信息窗口宽度
		  height: 10,     // 信息窗口高度
		};

/**
 * 绘制工具样式
 */
var styleOptions = {
        strokeColor:"#CDCDCD",    //边线颜色。
        fillColor:"#CDCDCD",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 1,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
    };