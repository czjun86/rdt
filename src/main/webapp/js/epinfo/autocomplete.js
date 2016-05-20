//定义全局变量用于储存提示层
//提示菜单栏的节点
var tips;
//禁用chrome和firefox浏览器自带的自动提示
$('.epName').attr("autocomplete","off");
$('.epName').bind("propertychange input focus",function(event){
	$('.epName').addClass('spinner');//增加等待查询图标
    $this=$(this);
    if(event.type!='focus'){
        //如果有改变，则状态定为必须重新选择，因为纯人手输入会导致value无法插入
        $this.data('ok',false);
    }
    
    //重建储存提示层并让其在适当位置显示
    $(tips).remove();
    tips=document.createElement('div');
    $tips=$(tips);
    //提示菜单展示位置
    $tips.addClass("tips");
    $tips.css({top:'34px',left:'0px',width:'100%',position:'absolute','z-index':'9994',background:'#FFFAFA'
    	,'border':'1px','border-style':'solid','border-color':'#e3e3e3','cursor':'pointer'});
    $tips.appendTo($this.parent());
    //定义ajax获取菜单内容
    if($this.val()!=null && $this.val().replace(/^\s+|\s+$/g, '')!=""){
    	$.ajax({
    		type:"post",
    		url:contextPath + "/epinfo/getEpName",
    		data:({name:$this.val()}),//区分天，月
    		success:function(data){
    			$('.epName').removeClass('spinner');
    			var lists = data;
    	        $tips.empty();
    	        var htmlcode="<table cellspacing='0' cellpadding='2' style='width:100%'><tbody>";
    	        for(var i=0;i<lists.length;i++){
    	            //这里我需要用到value和title两项，所以用data-value传递多一个参数，在回车或鼠标点击后赋值到相应的地方，以此完美地替代select
    	            htmlcode+='<tr data-value="'+lists[i]+'"><td>'+lists[i]+'</td></tr>';
    	        }
    	        htmlcode+="</tbody></table>";
    	        //把loading动态图替换成内容
    	        if(lists.length>0){
    	        	$tips.html(htmlcode);
    	        	$tips.show();
    	        }else{
    	        	$tips.hide();
    	        }
    		}
    	});
    }else{
    	$('.epName').removeClass('spinner');
    	$tips.hide();
    }
});

//监听鼠标动作，mouseover改变选中项
$(document).delegate('.tips td','mouseover',function(){
    var tr=$(this).parent();
    tr.siblings('tr').removeClass();
    tr.addClass('selectedtr');
});

//监听鼠标动作，click选定
$(document).delegate('.tips td','click',function(){
    var tr=$(this).parent();
    if(tr.length==1){
        //向上找到对应input，以防出现相同class的input影响
        $inputfield=tr.parent().parent().parent().siblings('input.epName');

        //设置value值到input框通过参数data-valueto配置的value值存储项中去，一般是hidden项
        $valuefield=$('input[name='+$inputfield.data('valueto')+']');
        $valuefield.val(tr.data('value'));
        $inputfield.val(tr.text());
        //设置状态是从选项中选择，允许blur
        $inputfield.data('ok',true);
    }
    $(tips).fadeOut('fast');
});

//失去焦点时，移除菜单
$(".epName").blur(function(){
	$(tips).fadeOut('fast');
});

