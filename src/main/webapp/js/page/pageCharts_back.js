function pageCharts(totlePage,indexPage,classStr,did){
	var dociv = document.getElementById(did);
	//先清空该节点下所有子节点
	while(dociv.hasChildNodes()) 
    {
		dociv.removeChild(dociv.firstChild);
    }
	var div = $("#"+did);
	div.css('width','300px');
	if(indexPage>0){
		div.append('<button class="btn btn-sm '+classStr+'" style="float:left;width:18%;margin-right:2px;" index='+(indexPage-1)+'>'+'上一页'+'</button>');
	}else{
		div.append('<button class="btn btn-sm" display="true" style="float:left;width:18%;margin-right:2px;color:#999" index='+(indexPage-1)+'>'+'上一页'+'</button>');
	}
	if(indexPage>=3){
		div.append('<p style="float:left;width:4%;margin-right:2px;line-height:40px;color:#666">...</p>');
	}
	for(var i = 2;i >0;i--){
		if(indexPage+1-i>0){
			div.append('<button class="btn btn-sm gapCheck blue" style="float:left;width:10%;margin-right:2px;" index='+(indexPage-i)+'>'+(indexPage+1-i)+'</button>');
		}else{
			continue;
		}
	}
	div.append('<button class="btn btn-sm " style="float:left;width:10%;margin-right:2px;" index='+indexPage+'>'+(indexPage+1)+'</button>');
	for(var i = 1 ;i < 3 ;i++){
		if(indexPage+1+i<=totlePage){
			div.append('<button class="btn btn-sm gapCheck blue" style="float:left;width:10%;margin-right:2px;" index='+(indexPage+i)+'>'+(indexPage+i+1)+'</button>');
		}else{
			continue;
		}
	}
	if(indexPage<totlePage-3){
		div.append('<p style="float:left;width:4%;margin-right:2px;line-height:40px;color:#666">...</p>');
	}
	if(indexPage<totlePage-1){
		div.append('<button class="btn btn-sm '+classStr+'" style="float:left;width:18%;margin-right:2px;" index='+(indexPage+1)+'>'+'下一页'+'</button>');
	}else{
		div.append('<button class="btn btn-sm" display="true" style="float:left;width:18%;margin-right:2px;color:#999" index='+(indexPage+1)+'>'+'下一页'+'</button>');
	}
}