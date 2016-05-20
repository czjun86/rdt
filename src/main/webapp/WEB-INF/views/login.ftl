<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>移动网络自动路测系统 | 登陆</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<meta name="MobileOptimized" content="320"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->          
<link href="${application.getContextPath()}/scripts/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="${application.getContextPath()}/scripts/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="${application.getContextPath()}/scripts/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES --> 
<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/select2/select2_metro.css" />
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME STYLES --> 
<link href="${application.getContextPath()}/scripts/css/style-metronic.css" rel="stylesheet" type="text/css"/>
<link href="${application.getContextPath()}/scripts/css/style.css" rel="stylesheet" type="text/css"/>
<link href="${application.getContextPath()}/scripts/css/style-responsive.css" rel="stylesheet" type="text/css"/>
<link href="${application.getContextPath()}/scripts/css/plugins.css" rel="stylesheet" type="text/css"/>
<link href="${application.getContextPath()}/scripts/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color"/>
<link href="${application.getContextPath()}/scripts/css/pages/login-soft.css" rel="stylesheet" type="text/css"/>
<link href="${application.getContextPath()}/scripts/css/custom.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="login">
	<!-- BEGIN LOGO -->
	<div class="logo">
		<img src="${application.getContextPath()}/scripts/img/login-logo.png" alt="" /> 
	</div>
	<!-- END LOGO -->
	<!-- BEGIN LOGIN -->
	<div class="content">
		<!-- BEGIN LOGIN FORM -->
		<form  class="login-form" action="${application.getContextPath()}/index/indexMain" method="post">
			<h3 class="form-title">登录到您的帐户</h3>
			<div class="alert alert-danger display-hide">
				<button class="close" data-close="alert"></button>
				<span>用户名或密码错误！</span>
			</div>
			<div class="form-group">
				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
				<label class="control-label visible-ie8 visible-ie9">用户名</label>
				<div class="input-icon">
					<i class="fa fa-user"></i>
					<input id="username" class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="用户名" name="username"/>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">密码</label>
				<div class="input-icon">
					<i class="fa fa-lock"></i>
					<input id="password" class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="密码" name="password" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">验证码</label>
				<div class="input-icon">
					<i class="fa fa-ellipsis-horizontal"></i>
					<input id="verifyCode" class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="验证码" name="verifyCode" style="width:72%;float:left;margin-bottom:15px;"/>
					<div style="width:25%;float:left;cursor:pointer;margin-left:8px;margin-bottom:15px;"><img  id="imgCode" border=0 src="${application.getContextPath()}/validateCode/code" style="float:right;"/></div>
				</div>
			</div>
			<div class="form-actions">
				<button type="submit" class="btn blue pull-right">
				登陆 <i class="m-icon-swapright m-icon-white"></i>
				</button>            
			</div>
		</form>
		<!-- END LOGIN FORM -->        
	</div>
	<!-- END LOGIN -->
	<!-- BEGIN COPYRIGHT -->
	<div class="copyright">
		2015 &copy; 移动网络自动路测系统- 成都网优力软件有限公司.
	</div>
	<!-- END COPYRIGHT -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->   
	<!--[if lt IE 9]>
	<script src="${application.getContextPath()}/scripts/plugins/respond.min.js"></script>
	<script src="${application.getContextPath()}/scripts/plugins/excanvas.min.js"></script> 
	<![endif]-->   
	<script src="${application.getContextPath()}/scripts/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
	<script src="${application.getContextPath()}/scripts/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/plugins/jquery.blockui.min.js" type="text/javascript"></script>  
	<script src="${application.getContextPath()}/scripts/plugins/jquery.cookie.min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script src="${application.getContextPath()}/scripts/plugins/jquery-validation/dist/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/select2/select2.min.js"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script src="${application.getContextPath()}/dwr/engine.js"></script>
	<script src="${application.getContextPath()}/dwr/interface/loginDwr.js"></script>
	<script src="${application.getContextPath()}/scripts/scripts/app_amend.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/scripts/login-soft.js" type="text/javascript"></script>      
	<!-- END PAGE LEVEL SCRIPTS --> 
	<script>
		var basePath = '${application.getContextPath()}';
		jQuery(document).ready(function() {     
		  App.init();
		  Login.init();
		  $("#imgCode").live('click',function(){
		  	var verify=document.getElementById('imgCode');
		  	verify.setAttribute('src',basePath+"/validateCode/code?"+Math.random());
		  });
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>