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
	<meta content="" name="tc" />
	<meta name="MobileOptimized" content="320">
	<!-- BEGIN GLOBAL MANDATORY STYLES -->          
	<link href="${application.getContextPath()}/scripts/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN PAGE LEVEL PLUGIN STYLES --> 
	<link href="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
	<!-- END PAGE LEVEL PLUGIN STYLES -->
	<!-- BEGIN THEME STYLES -->
	<link href="${application.getContextPath()}/scripts/css/style-metronic.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/style.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/style-responsive.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/plugins.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/css/themes/light.css" rel="stylesheet" type="text/css" id="style_color"/>
	<!-- END THEME STYLES -->
	<!--分页标签-->
	<link href="${application.getContextPath()}/skins/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<!--分页标记-->
	<link rel="stylesheet" href="${application.getContextPath()}/scripts/plugins/data-tables/DT_bootstrap.css" />
	
	<!--easyui-->
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/easyui/themes/gray/easyuirevise.css">
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/easyui/themes/icon.css">
	
	<!-- echart 图表 开始 -->
	<link href="${application.getContextPath()}/scripts/plugins/echarts/www/css/echartsHome.css" rel="stylesheet">
	<!--select2 选择器 开始-->
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/select2/select2_metro.css" />
	<!--select2 选择器 结束-->
	
	<!-- 复选框样式 -->
	<link href="${application.getContextPath()}/scripts/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
	<!--echart 图表 结束 -->
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/jquery-1.10.2.min.js"></script>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed">
	<!-- BEGIN HEADER -->   
	<#include "../index/indexTop.ftl" />
	<!-- END HEADER -->
	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<#include "../index/indexMenu.ftl" />
		<!-- END SIDEBAR -->
		<!-- BEGIN PAGE -->
		<div class="page-content" style="min-height:550px;">
			<div class="row">
				<div id="pageTable">
					<div class="col-md-12">
					<!-- title-->
						<div class="portlet ">
							<div class="portlet-body">
								<!--报表工具-->
								<div class="navbar navbar-default" role="navigation" style="background:#fff !important;">
									<form class="navbar-form form-inline navbar-left breadcrumb" id="validateHealthDegree" role="search" method="post" onsubmit="return false;">
										
										<input type="hidden" id="qryflg" value="0"/>
										
										&nbsp;
										<!--时间范围控件       开始-->
										<div class="form-group">
											<label class="control-label">时间范围:</label>
											<div class="form-group" id="reportrange" dateFormat="YYYY.MM.DD" beforNdays="1" timeP="false" timeM="false" showDays="true"  style="width:165px;">
												<div class="input-icon right"  style="width:165px;">
													<input type="text" class="form-control report-input" id="queryTime" name="queryTime" value="${(queryBean.queryTime)!}" readOnly style="padding-right: 15px !important;width:165px !important;" />
												</div>
											</div>
										</div>
										<!--时间范围控件       结束-->
										<div class="form-group" style="margin-top:-5px;">
											<label class="control-label" style="margin-left:58px;">任务名称:</label>
											<input class="form-control" name="taskName" value="${(queryBean.taskName)!''}" id="taskName" placeholder="全部" style="width:160px !important;"/>
										</div>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<button class="btn blue" id="querybtn" style="float:right;width:62px;">查询</button>
									</form>
								</div>
								<!-- 查询条件 -->
								<form id="pageForm" name="pageForm" action="${application.getContextPath()}/taskReport/query" method="post">
									<input type="hidden" name="totalPage" id="totalPage" value="${(page.totalPage)!1}" />
									<input type="hidden" name="pageSize" id="pageSize" value="${(page.pageSize)!10}" />
									<input type="hidden" name="pageIndex" id="pageIndex" value="${(page.pageIndex)!1}" />
									<!--任务名称-->
									<input type="hidden" id="taskName_page" name="taskName" value="${(queryBean.taskName)!}"/>
									<!--时间-->
									<input type="hidden" id="queryTime_page" name="queryTime" value="${(queryBean.queryTime)!}"/>
								</form>
								
								<!--表单开始   开始-->
								<div class="table-responsive table-scrollable">
									<table class="table table-striped table-hover table-bordered" id="sample_editable_1">
										<!--表单title 开始-->
										<thead>
											<tr style="background-color:#EAEAEA;">
												<th style="text-align:center;" >任务名称</th>
												<th style="text-align:center;" >任务提交时间</th>
												<th style="text-align:center;" >完成状态</th>
												<th style="text-align:center;" >查询条件</th>
												<th style="text-align:center;" >操作</th>
											</tr>
										</thead>
										<!--表单title 结束-->
										<!-- 表单内容 开始 -->
										<tbody>
											<#if page?? && page.list?? &&  page.list?size &gt; 0>
												<#list page.list as ls>
													<tr style="height:37px;<#if ls_index==page.list?size-1>border-bottom:1px #dddddd  solid;</#if>">
													<td style="text-align:center;" >${(ls.taskName)!'-'}</td>
													<td style="text-align:center;" >${(ls.taskSubtime)!'-'}</td>
									                <td style="text-align:center;" >
														<#if ls.taskState?? && ls.taskState==0>
															未完成
														</#if>
														<#if ls.taskState?? && ls.taskState==1>
															已完成
														</#if>
														<#if ls.taskState?? && ls.taskState==2>
															已失败
														</#if>
													</td>
													<td style="text-align:center;cursor:pointer;" 
														onclick="showQuery(${(ls.rsrp)!0},${(ls.rsrq)!0},
																		   ${(ls.snr)!0},${(ls.broadband)!0},${(ls.delay)!0},
																		   ${(ls.lose)!0},${(ls.timegransel)!''},'${(ls.queryTime)!''}',
																		   '${(ls.operatorName)!''}','${(ls.provinceName)!''}','${(ls.districtName)!''}',
																		   '${(ls.areaName)!''}',${(ls.roadType)!-2},${(ls.roadLevel)!-2},
																		   '${(ls.roadId)!''}','${(ls.roadName)!''}',${(ls.showTime)!0},
																		   ${(ls.showArea)!0},${(ls.showRoad)!0},${(ls.showOperator)!0},
																		   ${(ls.showInterval)!0})"
													><a href="#">条件详情</a></td>
													<#if ls.taskState?? && ls.taskState==1>
													<td style="text-align:center;cursor:pointer;"
														onclick="exportData(${(ls.rsrp)!0},${(ls.rsrq)!0},
																		   ${(ls.snr)!0},${(ls.broadband)!0},${(ls.delay)!0},
																		   ${(ls.lose)!0},${(ls.timegransel)!''},'${(ls.queryTime)!''}',
																		   ${(ls.operator)!-2},${(ls.province)!-2},${(ls.district)!-2},
																		   ${(ls.area)!-2},${(ls.roadType)!-2},${(ls.roadLevel)!-2},
																		   '${(ls.roadId)!''}','${(ls.roadName)!''}',${(ls.showTime)!0},
																		   ${(ls.showArea)!0},${(ls.showRoad)!0},${(ls.showOperator)!0},
																		   ${(ls.showInterval)!0},'${(ls.taskId)!''}')"
													><a href="#">导出</a></td>
													<#else>
														<td style="text-align:center;"></td>
													</#if>
													</tr>
												</#list>
											<#else>
												<tr>
													<td colspan="5"  align='center' style="height:37px;border-bottom:1px #dddddd  solid;">无数据</td>
												</tr>
											</#if>
											<!-- 表单内容 结束 -->
										</tbody>
									</table>
								</div>
								<!--表单开始   结束-->
							</div>
						</div>
						<div id="resoucesPage"></div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!--条件弹出层 -->
	
	<div id="dialog_div"></div>
	
	<!-- 开始    日期范围控件脚本  -->
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/moment.min.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/daterangepicker_amend.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/scripts/form-dateRanges_amend.js"></script>
	<!--easyui-->
	<script type="text/javascript" src="${application.getContextPath()}/js/report/areaRoad.js"></script>
	
	<script type="text/javascript" src="${application.getContextPath()}/js/utils/timeUtils.js"></script>
	
	<!--页面工具脚本-->
	<script src="${application.getContextPath()}/js/report/rptHelper.js" type="text/javascript"></script>
	<!--分页-->
	<script src="${application.getContextPath()}/js/ejs_production.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/scripts/table-pages.js" type="text/javascript"></script>
	<!--复选框 -->
	<script src="${application.getContextPath()}/scripts/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
	<!--查询条件-->
	<script type="text/javascript" src="${application.getContextPath()}/js/report/taskReport.js"></script>
	
	
	<script type="text/javascript">
	jQuery(document).ready(function() {
		    var winHeight = window.screen.height;
			var scHeight = document.body.scrollTop;
			var winHeight = window.screen.height;
			$('.page-content').css('min-height',winHeight);
		 });
		$(function(){
			PageUtils.init({
				"sync" : true, //异步或者form提交,默认form提交
				"method" : dataPage,
				"lengthMenu": [10, 15, 20, 50],
		        "defaultLength": ${(page.pageSize)!10},      
		        "pageIndex":${(page.pageIndex)!1},
		        "total": ${(page.totalPage)!1},     
		        "form":"pageForm",
		        "displayContainer":"resoucesPage",
		        "language": "zh"         //语言  zh or en
		   });
		   $("#modal-backdrop").hide();
		});
		function dataPage(){
			$("#modal-backdrop").show();
			$("#pageForm").submit();
		}
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
<style type= text/css>
	.rightMenu{
		position:absolute;
		color:#555;
		cursor:pointer;
		z-index:900;
		top:150px;
		left:500px;
		width:140px;
		height:32px;
		background:#ddd;
		text-align:center;
		line-height:19px;
		padding:5px;
		border: 2px solid #aaa;
		-moz-border-radius: 15px; 
		-webkit-border-radius: 15px; 
		border-radius:10px !important; 
	}
	.rightMenu:hover {
	  color:#444;
	  background:#ccc;
	}
</style>
</html>