/**
	 * 画栅格方法分别是500*500和200*200的网格 boundaries区域行政边界数组 area区域 gridlevel
	 * 格子大小5－500*500,2－200*200
	 */

	function getgrids(area, boundaries, gridlevel) {
		var jdis = 1.117 * 0.0048 * 0.2 * gridlevel;// 经度距离
		var wdis = 1.163 * 0.0039 * 0.2 * gridlevel;// 纬度度距离
		// 取数组经纬度
		if(boundaries.length>0){
	    var lngarr=[],latarr=[];
		   /* */  var ploy = new BMap.Polygon(boundaries, {
		   			strokeWeight : 2,
		   			fillOpacity : 1,
		   			strokeColor : "#000000",
		   			fillColor :"none"
		   		}); //建立多边形覆盖物
		 map.addOverlay(ploy); //添加覆盖物
		var points = boundaries;
		for (var i = 0; i < points.length; i++) {
			lngarr.push(points[i].lng);
			latarr.push(points[i].lat);
		}
		// 排序
		lngarr.sort(function(a, b) {
			return a > b ? 1 : -1;
		});
		latarr.sort(function(a, b) {
			return a > b ? 1 : -1;
		});
		var polyline;
		// 画线形成网格
		for (var i = latarr[0]; i <= parseFloat(latarr[latarr.length - 1])
				+ wdis; i = parseFloat(i) + wdis) {
			polyline = new BMap.Polyline([
					new BMap.Point(
							parseFloat(lngarr[lngarr.length - 1]) + jdis, i),
					new BMap.Point(lngarr[0], i) ], options);
			map.addOverlay(polyline);
			// lats.push(i);
		}

		for (var h = lngarr[0]; h <= parseFloat(lngarr[lngarr.length - 1])
				+ jdis; h = parseFloat(h) + jdis) {

			polyline = new BMap.Polyline([
					new BMap.Point(h, latarr[0]),
					new BMap.Point(h, parseFloat(latarr[latarr.length - 1])
							+ wdis) ], options);
			map.addOverlay(polyline);
		}

		
		}
	}