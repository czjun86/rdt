<style type="text/css" >
/*loading*/
.load{ position:fixed; width:100%; height:100%; text-align:center; left:0; top:0;background:#111;opacity:0.8;filter:alpha(opacity=80); z-index:100}
.load_pic{ width:25%; position:relative; margin:auto; margin-top:18%;}
.load_txt{ position:absolute; left:0; top:0; width:100%;}
.animationRotate{ animation:rotateRun infinite 1s linear; -webkit-animation:rotateRun infinite 1s linear; -moz-animation:rotateRun infinite 1s linear;}
@keyframes rotateRun{
	0%{ transform:rotate(0deg);}	
	100%{ transform:rotate(360deg)}
}
@-webkit-keyframes rotateRun{
	0%{ -webkit-transform:rotate(0deg);}	
	100%{ -webkit-transform:rotate(360deg)}
}
@-moz-keyframes rotateRun{
	0%{ -moz-transform:rotate(0deg);}	
	100%{ -moz-transform:rotate(360deg)}
}
body>div.window{
	z-index:10100 !important;
}
body>div.messager-window{
	z-index:10101 !important;
}
body>div.window-mask{
	z-index:10099 !important;
}
</style>
<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/easyui/themes/icon.css">
<!--loading-->
<!--<div class="modal-backdrop fade in"  style="display:block;z-index: 10050;text-align:center;">
	<img src="${application.getContextPath()}/images/load-pic.png" style="margin-top:200px;"/>
</div>-->
<div id="modal-backdrop" class="load" style="display:block;z-index: 11050;">
    <div class="load_pic">
    	<img src="${application.getContextPath()}/images/load-pic.png" width="20%" class="animationRotate">
        <span class="load_txt"><img src="${application.getContextPath()}/images/wo.png" width="20%"></span>
    </div>    
</div>
		<!-- BEGIN HEADER -->   
	<div class="header navbar navbar-inverse navbar-fixed-top">
		<!-- BEGIN TOP NAVIGATION BAR -->
		<div class="header-inner">
			<!-- BEGIN LOGO -->  
			<a class="navbar-brand" id="logo_left_top">
				<img src="${application.getContextPath()}/scripts/img/logo1.png"  style="margin-top:12px;" id="top_logo" alt="logo" class="img-responsive" />
				<img src="${application.getContextPath()}/scripts/img/logo2.png"  style="margin-top:12px;display:none;margin-left:3px;" id="top_logo2" alt="logo" class="img-responsive" />
			</a>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER --> 
			<a href="javascript:;" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<img src="${application.getContextPath()}/scripts/img/menu-toggler.png" alt="" />
			</a> 
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<ul class="nav navbar-nav pull-right" style="margin-top:5px;">
				<!-- BEGIN USER LOGIN DROPDOWN -->
				<li class="dropdown user">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
						<!--<img alt="" src="${application.getContextPath()}/scripts/img/avatar1_small.jpg"/>-->
						<i class="fa  fa-user" ></i>
						<span class="username" style="font-size:12px;">${(Session.user.name)!}</span>
						<i class="fa fa-angle-down"></i>
					</a>
					<ul class="dropdown-menu">
						<!--<li><a href="extra_profile.html"><i class="fa fa-user"></i>个人资料</a></li>
						<li class="divider"></li>-->
						<li><a class="pwd" href="#"><i class="fa fa-edit"></i>&nbsp;&nbsp;修改密码</a></li>
						<li><a href="javascript:;" id="trigger_fullscreen"><i class="fa fa-move" ></i>&nbsp;&nbsp;全屏</a></li>
						<!--<li><a href="extra_lock.html"><i class="fa fa-lock"></i>锁屏</a></li>-->
						<li><a href="${application.getContextPath()}/login/logout"><i class="fa fa-key" ></i>&nbsp;&nbsp;登出</a></li>
					</ul>
				</li>
				<!-- END USER LOGIN DROPDOWN -->
			</ul>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END TOP NAVIGATION BAR -->
	</div>
	<!--dialog请求层-->
	<div id="pwd_" class="modal fade" tabindex="-1"></div>
	<div class="modal fade" id="ajax" tabindex="-1" role="basic" aria-hidden="true">
	</div>
<!--设置全局变量 开始-->
<script>
	var contextPath = '${application.getContextPath()}';
</script>
<!--设置全局变量 结束-->
<!-- 开始    核心插件 -->   
<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/jquery-migrate-1.2.1.min.js"></script> 
<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/jquery.cookie.min.js"></script>
<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/jquery.blockui.min.js"></script>
<!-- 开始     页面级脚本 -->
<!--[if lt IE 9]>
<script src="${application.getContextPath()}/scripts/plugins/respond.min.js"></script>
<script src="${application.getContextPath()}/scripts/plugins/excanvas.min.js"></script> 
<![endif]-->   
<script type="text/javascript" src="${application.getContextPath()}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${application.getContextPath()}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<!--alter等提示框-->
<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/bootbox/bootbox.min.js"></script>
<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/data-tables/jquery.dataTables.js"></script>
<script type="text/javascript" src="${application.getContextPath()}/scripts/scripts/table-editable.js"></script> 
<!-- 开始     页面级脚本 -->
<script type="text/javascript" src="${application.getContextPath()}/scripts/scripts/app.js"></script>
<!-- 结束    页面级脚本 -->  
<!--select2 选择器 开始-->
<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/select2/select2.min.js"></script>
<!--select2 选择器 结束-->
<!--输入验证 开始-->
<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/jquery-validation/dist/jquery.validate.min_amend.js"></script>
<!--输入验证 结束-->
<!--echart 图表 开始 -->
<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/echarts/www/js/echarts.js"></script>
<!--echart 图表 结束 -->
<!--用户菜单-->
<script type="text/javascript" src="${application.getContextPath()}/js/index/index.js"></script>
<script type="text/javascript" src="${application.getContextPath()}/js/userManager/userUpdPwd.js"></script>	
<script type="text/javascript" src="${application.getContextPath()}/js/userManager/validater.js"></script>	
<script type="text/javascript">
	jQuery(document).ready(function() { 
			App.init();
		});
</script>