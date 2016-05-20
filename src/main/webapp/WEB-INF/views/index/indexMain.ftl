<!DOCTYPE html>
<!-- 
Template Name: Metronic - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.0.2
Version: 1.5.4
Author: KeenThemes
Website: http://www.keenthemes.com/
Purchase: http://themeforest.net/item/metronic-responsive-admin-dashboard-template/4021469?ref=keenthemes
-->
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8" />
	<title>移动网络自动路测系统</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="yaon" />
	<meta name="MobileOptimized" content="320">       
  <!-- BEGIN GLOBAL MANDATORY STYLES -->          
	<link href="${application.getContextPath()}/scripts/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN THEME STYLES --> 
	<link href="${application.getContextPath()}/scripts/css/style-metronic.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/style.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/style-responsive.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/plugins.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<link href="${application.getContextPath()}/scripts/css/custom.css" rel="stylesheet" type="text/css"/>
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed">
	<#include "./indexTop.ftl" />
	<div class="clearfix"></div>
	<#include "./indexCenter.ftl" />
	<#include "./copyright.ftl" />
</body>
<!-- END BODY -->
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
	<script src="${application.getContextPath()}/scripts/scripts/app_amend.js"></script>
	<script>
	var contextPath= '${application.getContextPath()}';
		jQuery(document).ready(function() {    
		   App.init();
		});
	</script>

</html>