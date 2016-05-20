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
	<link href="${application.getContextPath()}/scripts/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN PAGE LEVEL PLUGIN STYLES --> 
	<link href="${application.getContextPath()}/scripts/plugins/gritter/css/jquery.gritter.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
	<link href="${application.getContextPath()}/scripts/plugins/fullcalendar/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/plugins/jqvmap/jqvmap/jqvmap.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/plugins/jquery-easy-pie-chart/jquery.easy-pie-chart.css" rel="stylesheet" type="text/css"/>
	
	
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/bootstrap-fileupload/bootstrap-fileupload.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/select2/select2_metro.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/clockface/css/clockface.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/bootstrap-datepicker/css/datepicker.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/bootstrap-timepicker/compiled/timepicker.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/bootstrap-colorpicker/css/colorpicker.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/bootstrap-datetimepicker/css/datetimepicker.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/jquery-multi-select/css/multi-select.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/bootstrap-switch/static/stylesheets/bootstrap-switch-metro.css"/>
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/jquery-tags-input/jquery.tagsinput.css" />
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/bootstrap-markdown/css/bootstrap-markdown.min.css">
	<!-- END PAGE LEVEL PLUGIN STYLES -->
	<!-- BEGIN THEME STYLES --> 
	<link href="${application.getContextPath()}/scripts/css/style-metronic.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/style.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/style-responsive.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/plugins.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/pages/tasks.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/themes/light.css" rel="stylesheet" type="text/css" id="style_color"/>
	<link href="${application.getContextPath()}/scripts/css/custom.css" rel="stylesheet" type="text/css"/>
	<!-- END THEME STYLES -->
	<link href="${application.getContextPath()}/skins/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/jquery-1.10.2.min.js"></script>
	<style type="text/css">
		.row{
		margin-left: 0px !important;
		margin-right: 0px !important;
		}
	</style>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/jquery-1.10.2.min.js"></script>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed">
    <#include "../index/indexTop.ftl" />
	<!-- END HEADER -->
	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
			<#include "../index/indexMenu.ftl" />
		<!-- END SIDEBAR -->
		<!-- BEGIN PAGE -->
		<div class="page-content" >
			<div class="row" >
	<div class="col-md-12">
		<!-- 报表title-->
		<div class="portlet">
			<!--
			<ul class="page-breadcrumb breadcrumb">
				<li>
					<i class="fa fa-home"></i>
					<span>系统管理</span> 
                	<i class="fa fa-angle-right"></i>
				</li>
			<li>
					<span>用户管理</span> 
				</li>
			</ul>
			-->
			<!--报表正文-->
			<div class="portlet-body">
			<!--报表工具-->
				<div class="table-toolbar">
					<div class="btn-group">
						<button class="btn blue editUser">
						 <i class="fa fa-plus"></i> &nbsp;新增
						</button>
						<!--<button class="btn red deleteresouces" type="button">
						删除 <i class="fa fa-trash-o"></i>
						</button>-->
					</div>
				</div>
				<form id="pageForm" action="${application.getContextPath()}/operator/opManager" method="post">
					<input type="hidden" name="pageIndex" value="${(page.pageIndex)!1}" />
					<input type="hidden" name="pageSize" value="${(page.pageSize)!10}" />
				</form>
				<!--报表列属性名-->
				<form id="submitform" action="${application.getContextPath()}/operator/opManager" method="post">
					<div class="table-responsive table-scrollable" style="height:<#if !(page.list??) ||  page.list?size == 0>111<#else>${((page.list?size+2)*37)!}</#if>px;">
						<table class="table table-striped table-hover table-bordered dataTable" id="sample_editable_1">
							<thead>
								<tr style="background-color:#EAEAEA;">
									<!--<th class="table-checkbox">
										<span>
											<input class="group-checkable" data-set="#childs .checkboxes" type="checkbox" >
										</span>
									</th>-->
									<th style="text-align:center;width:10%;">用户名</th>
									<th style="text-align:center;width:10%;">操作员名称</th>
									<th style="text-align:center;width:10%;">电话</th>
									<th style="text-align:center;width:30%;">邮箱</th>
									<th style="text-align:center;width:10%;">运营商</th>
									<th style="text-align:center;width:10%;">用户组</th>
									<th style="text-align:center;width:10%;">状态</th>
									<th colspan="3" style="text-align:center;width:10%;">操作</th>
								</tr>
							</thead>
							<tbody>
								 <#if page.list?? &&  page.list?size &gt; 0>
									<#list page.list as p>
										 <tr style="height:37px;<#if p_index==page.list?size-1>border-bottom:1px #dddddd  solid;</#if>">
											<!--<td><input name="resck" type="checkbox" class="checkboxes" value="${(resources.id)!}" id="childs" /></td>-->
											<td align='center'>${(p.userName)!}</td>
											<td align='center'>${(p.name)!}</td>
											<td align='center'>
												${(p.phone)!}
											</td>
											<td align='center'>
												${(p.email)!}
											</td>
											<td align='center'>
												${(p.operator)!}
											</td>
											<td align='center'>
												${(p.rolename)!}
											</td>
											<td align='center'>
												<#if (p.islock)?? && p.islock==1>禁用</#if>
												<#if (p.islock)?? && p.islock==0>启用</#if>
											</td>
											<td align='center' colspan="3">
												<#if (p.userName)?? && p.userName!="admin"><a class="editUser" href="#" id="${(p.userid)!}">修改</a>|</#if>
												<!--<td align='center'><#if (p.userName)?? && p.userName!="admin"><a class="del" href="#"  id="${(p.userid)!}">删除</a></#if></td>-->
												<#if (p.userName)?? && p.userName!="admin">
													<a class="resSta changeUserState" align='center' href="#" id="${(p.userid)!}" _islock="${(p.islock)!}" >
														<#if (p.islock)?? && p.islock==1>启用</#if>
														<#if (p.islock)?? && p.islock==0>禁用</#if>
													</a>
													|
												</#if>
													<!--<div class="form-group">
														<div class="col-md-9">
															<div id = "${(resources.id)!}" class="make-switch resSta" data-on="success" data-off="danger" name="${(resources.status)!'0'}">
																<input id = "${(resources.id)!}" class="staUse resSta" type="checkbox" <#if (resources.status)?? && resources.status == 1>checked</#if> class="toggle"/>
															</div>
														</div>
													</div>-->
												<#if (p.userName)?? && p.userName!="admin">
													<a class="deleteUser" href="#" id="${(p.userid)!}" un="${(p.userName)!}" userid="${(p.userid)!}">删除</a>
												</#if>
											</td>
										</tr>
									</#list>
								<#else>
									<tr>
										<td colspan="7" style="text-align:'center';">还没有数据</td>
									</tr>
								</#if>
							</tbody>
						</table>
					</div>
				</form>
				<div id="containerPage"></div>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>
</div>
		</div>
	
		<!--dialog请求层-->
		<div id="ajax-modal" class="modal fade" tabindex="-1"></div>
		<div class="modal fade" id="ajax" tabindex="-1" role="basic" aria-hidden="true">
		</div>
	</div>
	<!--分页-->
	<script type="text/javascript" src="${application.getContextPath()}/js/ejs_production.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/scripts/table-pages.js"></script>	
	
	<script type="text/javascript" src="${application.getContextPath()}/js/userManager/userManager.js"></script>
	<!--<script type="text/javascript" src="${application.getContextPath()}/js/userManager/initSelect.js"></script>-->
	<!--复选框 -->
	<script src="${application.getContextPath()}/scripts/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
	<!--验证--> 	
	<script type="text/javascript" src="${application.getContextPath()}/js/userManager/validater.js"></script>	
	
	<script type="text/javascript">
	jQuery(document).ready(function() {    
		   PageUtils.init({
				"sync" : true, //异步或者form提交,默认form提交
				"method" : -1,
			   	"lengthMenu": [10, 15, 20, 50],
		        "defaultLength": ${(page.pageSize)!10},      
		        "pageIndex":${(page.pageIndex)!1},
		        "total": ${(page.totalPage)!1},     
		        "form":"submitform",
		        "displayContainer":"containerPage",
		        "language": "zh"         //语言  zh or en
		   });
		var winHeight = window.screen.height;
		var scHeight = document.body.scrollTop;
		var winHeight = window.screen.height;
		$('.page-content').css('min-height',winHeight);
		$("#modal-backdrop").hide();
    });	
	</script>
</body>
<!-- END BODY -->
</html>