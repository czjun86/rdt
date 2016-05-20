/**
 * 图形分页
 * 页面需要引入${application.getContextPath()}/skins/default.css 样式
 * <div id="did"></div><div class="clearfix"></div>
 * @param totlePage  总页数
 * @param indexPage	当前页码
 * @param classStr	绑定点击时间的class节点
 * @param did		页面存放分页标签的div节点id
 * @author peng.dd
 */
function pageCharts(totlePage,indexPage,classStr,did){
	var dociv = document.getElementById(did);
	//先清空该节点下所有子节点
	while(dociv.hasChildNodes()) 
    {
		dociv.removeChild(dociv.firstChild);
    }
	var div = $("#"+did);
	var substr = "<div style=\"width: 20%; line-height: 33px; float: left;\">"+
	"<div id='' class=\"dataTables_info\">第"+(indexPage+1)+"/"+(totlePage==0?1:totlePage)+"页</div>"+
	"</div>";
	substr +="<div class=\"pageNumber\"><div style=\"float: right;\"><ul style=\"visibility: visible; margin: 10px 0; float: left;\"class=\"pageContainer\">";
	
	//上一页
	if(indexPage>0){
		substr +="<li class=\""+classStr+"\" index="+(indexPage-1)+"><a href=\"javascript:void(0);\" title=\"上一页\"><i class=\"fa fa-angle-left\"></i></a></li>";
	}else{
		substr +="<li class=\"classStr disabled\" index="+(indexPage-1)+"><a href=\"javascript:void(0);\" title=\"上一页\"><i class=\"fa fa-angle-left\"></i></a></li>";
	}
	
	//中间页数按钮
	//之前页码按钮
	for(var i = 2;i >0;i--){
		if(indexPage+1-i>0){
			substr +="<li class=\""+classStr+"\" index=\""+(indexPage-i)+"\"><a  href=\"javascript:void(0);\">"+(indexPage+1-i)+"</a></li>";
		}else{
			continue;
		}
	}
	//当前页
	substr +="<li index=\""+indexPage+"\"><a class=\"a-selected\" href=\"javascript:void(0);\">"+(indexPage+1)+"</a></li>";
	//之后页码按钮
	for(var i = 1 ;i < 3 ;i++){
		if(indexPage+1+i<=totlePage){
			substr +="<li class=\""+classStr+"\"  index=\""+(indexPage+i)+"\"><a  href=\"javascript:void(0);\">"+(indexPage+i+1)+"</a></li>";
		}else{
			continue;
		}
	}
	
	//下一页
	if(indexPage<totlePage-1){
		substr +="<li class=\""+classStr+"\" index="+(indexPage+1)+"><a href=\"javascript:void(0);\" title=\"上一页\"><i class=\"fa fa-angle-right\"></i></a></li>";
	}else{
		substr +="<li class=\"classStr disabled\" index="+(indexPage+1)+"><a href=\"javascript:void(0);\" title=\"上一页\"><i class=\"fa fa-angle-right\"></i></a></li>";
	}
	
	substr +="</ul></div>"
	div.append(substr);
}

/**
 * 7天1组图形分页
 * 页面需要引入${application.getContextPath()}/skins/default.css 样式
 * <div id="did"></div><div class="clearfix"></div>
 * @param prev	判断是否有上一页
 * @param next	判断是否有下一页
 * @param classStr	绑定点击时间的class节点
 * @param did		页面存放分页标签的div节点id
 * @author peng.dd
 */
function pageSevenCharts(prev,next,prevTime,nextTime,classStr,did){
	var dociv = document.getElementById(did);
	//先清空该节点下所有子节点
	while(dociv.hasChildNodes()) 
    {
		dociv.removeChild(dociv.firstChild);
    }
	var div = $("#"+did);
	var substr = "<div style=\"width: 20%; line-height: 33px; float: left;\">"+
	"<div id='' class=\"dataTables_info\"></div>"+
	"</div>";
	substr +="<div class=\"pageNumber\"><div style=\"float: right;\"><ul style=\"visibility: visible; margin: 10px 0; float: left;\"class=\"pageContainer\">";
	
	//上一页
	if(prev!=null && prevTime != null){
		substr +="<li class=\""+classStr+"\" pageType='prev' time="+prevTime+"><a href=\"javascript:void(0);\" title=\"上一页\"><i class=\"fa fa-angle-left\"></i></a></li>";
	}else{
		substr +="<li class=\"classStr disabled\" pageType='prev' time="+prevTime+"><a href=\"javascript:void(0);\" title=\"上一页\"><i class=\"fa fa-angle-left\"></i></a></li>";
	}
	//下一页
	if(next!=null && nextTime != null){
		substr +="<li class=\""+classStr+"\" pageType='next' time="+nextTime+"><a href=\"javascript:void(0);\" title=\"上一页\"><i class=\"fa fa-angle-right\"></i></a></li>";
	}else{
		substr +="<li class=\"classStr disabled\" pageType='next' time="+nextTime+"><a href=\"javascript:void(0);\" title=\"上一页\"><i class=\"fa fa-angle-right\"></i></a></li>";
	}
	
	substr +="</ul></div>"
	div.append(substr);
}