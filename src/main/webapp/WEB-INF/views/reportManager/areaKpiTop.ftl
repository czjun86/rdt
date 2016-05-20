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
									<form class="navbar-form form-inline navbar-left breadcrumb"  id="chooseForm" onsubmit="return false;">
										<!--时间范围控件       开始-->
										<div class="form-group">
											<label class="control-label">时间范围:</label>
											<div class="form-group" id="reportrange" dateFormat="YYYY.MM.DD" beforNdays="1" timeP="false" timeM="false" showDays="true"  style="width:165px;">
												<div class="input-icon right" data-date-start-date="-30d"  style="width:165px;">
													<input type="text" class="form-control report-input" id="queryTime" name="queryTime" value="${(queryBean.queryTime)!}" readOnly style=" padding-right: 15px !important;width:165px !important;" />
												</div>
											</div>
										</div>
										<!--时间范围控件       结束-->
										<!--区域  开始-->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">区域:</label>
											<select class="form-control input-small select2me" <#if user?? && user.province?? && user.province=='-1'>hasAreaAll="1"</#if> style="width:165px;" name="province" id="province">
												<#if countrys ?? && countrys?size &gt; 0>
													<#list countrys as p>
														<option value="${(p.id)!}" <#if queryBean?? && p.id == queryBean.province>selected</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
											&nbsp;
											<select class="fform-control input-small select2me" <#if user?? && user.district?? && user.district=='-1'>hasAreaAll="1"</#if> style="width:165px;margin-left:-5px;" name="district" id="district">
												<#if user?? && user.district?? && user.district=='-1'><option value="-1"  <#if queryBean?? && '-1' == queryBean.district>selected="true"</#if>>全部</option></#if>
												<#if citys ?? && citys?size &gt; 0>
													<#list citys as p>
														<option value="${(p.id)!}" <#if queryBean?? && p.id == queryBean.district>selected="true"</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
											&nbsp;
											<select class="form-control input-small select2me" <#if user?? && user.county?? && user.county=='-1'>hasAreaAll="1"</#if> style="width:165px;margin-left:-5px;" name="area" id="area">
												<#if user?? && user.county?? && user.county=='-1'><option value="-1" <#if queryBean?? && '-1' == queryBean.area>selected="true"</#if>>全部</option></#if>
												<#if areas ?? && areas?size &gt; 0>
													<#list areas as p>
														<option value="${(p.id)!}" <#if queryBean?? && p.id == queryBean.area>selected="true"</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
										</div>
										<!-- 区域 结束 -->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">指标:</label>
											<select class="form-control input-small select2me" style="width:165px;" name="kpiType" id="kpiType">
												<option value="8" <#if queryBean.kpiType?? && queryBean.kpiType == 8>selected</#if>>覆盖率</option>
												<option value="1" <#if queryBean.kpiType?? && queryBean.kpiType == 1>selected</#if>>RSRP</option>
												<option value="2" <#if queryBean.kpiType?? && queryBean.kpiType == 2>selected</#if>>SNR</option>
												<option value="4" <#if queryBean.kpiType?? && queryBean.kpiType == 4>selected</#if>>下行链路带宽</option>
												<option value="9" <#if queryBean.kpiType?? && queryBean.kpiType == 9>selected</#if>>网络掉线率</option>
												<option value="10" <#if queryBean.kpiType?? && queryBean.kpiType == 10>selected</#if>>业务掉线率</option>
											</select>
										</div>
										<!-- 维度选择 结束 -->
										<button class="btn blue" style="height:31px;width:62px;margin-top:-6px;margin-left:10px;" id="querybtn">查询</button>
										<button class="btn green" style="margin-left:5px;height:31px;width:62px;margin-top:-6px;" id="leadExcel">导出</button>
									</form>
								</div>
								<!-- 查询条件 -->
								<form action="${application.getContextPath()}/areaKpiTop/query" id="kpiForm" role="search" method="post" >
									<!--时间-->
									<input type="hidden" id="queryTime_query" name="queryTime" value="${(queryBean.queryTime)!}"/>
									<!--道路-->
									<input type="hidden" id="province_query" name="province" value="${(queryBean.province)!}"/>
									<input type="hidden" id="district_query" name="district" value="${(queryBean.district)!}"/>
									<input type="hidden" id="area_query" name="area" value="${(queryBean.area)!}"/>
									<!--指标-->
									<input type="hidden" id="kpi_query" name="kpiType" value="${(queryBean.kpiType)!}"/>
									<!-- 是否查询过 -->
									<input type="hidden" id="flag_query" name="flag" value="${(flag)!}"/>
								</form>
								<!-- 查询条件 -->
								<form id="pageForm" name="pageForm" action="${application.getContextPath()}/areaKpiTop/query" method="post" >
									
									<input type="hidden" name="totalPage" id="totalPage" value="${(page.totalPage)!1}" />
									<input type="hidden" name="pageSize" id="pageSize" value="${(page.pageSize)!10}" />
									<input type="hidden" name="pageIndex" id="pageIndex" value="${(page.pageIndex)!1}" />
									<!--UUID-->
									<input type="hidden" id="uuid_page" name="uuid" value="${(queryBean.uuid)!}"/>
									<!--时间-->
									<input type="hidden" id="queryTime_page" name="queryTime" value="${(queryBean.queryTime)!}"/>
									<!--道路-->
									<input type="hidden" id="province_page" name="province" value="${(queryBean.province)!}"/>
									<input type="hidden" id="district_page" name="district" value="${(queryBean.district)!}"/>
									<input type="hidden" id="area_page" name="area" value="${(queryBean.area)!}"/>
									<!--指标-->
									<input type="hidden" id="kpi_page" name="kpiType" value="${(queryBean.kpiType)!}"/>
								</form>
								<!--表单开始   开始-->
								<div class="table-responsive table-scrollable">
									<table class="table table-striped table-hover table-bordered" id="sample_editable_1">
										<!--表单title 开始-->
										<thead>
											<tr style="background-color:#EAEAEA;">
													<th style="text-align:center;" >市名</th>
													<th style="text-align:center;" >区县</th>
													<th style="text-align:center;" >第一名</th>
													<th style="text-align:center;" >${(kpiName)!'-'}</th>
													<th style="text-align:center;" >第二名</th>
													<th style="text-align:center;" >${(kpiName)!'-'}</th>
													<th style="text-align:center;" >第三名</th>
													<th style="text-align:center;" >${(kpiName)!'-'}</th>
											</tr>
										</thead>
										<!--表单title 结束-->
										<!-- 表单内容 开始 -->
										<tbody>
											<#if page?? && page.list?? &&  page.list?size &gt; 0>
												<#list page.list as ls>
													<tr style="height:37px;<#if ls_index==page.list?size-1>border-bottom:1px #dddddd  solid;</#if>">
														<td style="text-align:center;" >${(ls.cityName)!'-'}</td>
														<td style="text-align:center;" >${(ls.countryName)!'-'}</td>
														<td style="text-align:center;" >${(ls.telecoms1)!'-'}</td>
														<td style="text-align:center;" >${(ls.kpiValue1)!'-'}</td>
														<td style="text-align:center;" >${(ls.telecoms2)!'-'}</td>
														<td style="text-align:center;" >${(ls.kpiValue2)!'-'}</td>
														<td style="text-align:center;" >${(ls.telecoms3)!'-'}</td>
														<td style="text-align:center;" >${(ls.kpiValue3)!'-'}</td>
													</tr>
												</#list>
											<#else>
												<tr>
													<td colspan="8"  align='center' style="height:37px;border-bottom:1px #dddddd  solid;">无采集数据</td>
												</tr>
											</#if>
										</tbody>
										<!-- 表单内容 结束 -->
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
	<!-- 开始    日期范围控件脚本  -->
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/moment.min.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/daterangepicker_amend.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/scripts/form-dateRanges_amend.js"></script>
	<!--easyui-->
	<script type="text/javascript" src="${application.getContextPath()}/js/report/area.js"></script>
	
	<!--页面工具脚本-->
	<script src="${application.getContextPath()}/js/report/rptHelper.js" type="text/javascript"></script>
	<!--分页-->
	<script src="${application.getContextPath()}/js/ejs_production.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/scripts/table-pages.js" type="text/javascript"></script>
	
	<script src="${application.getContextPath()}/js/report/areaKpiTop.js" type="text/javascript"></script>
	<script type="text/javascript">
	jQuery(document).ready(function() {
			//设置日期控件
			DateRanges.init('reportrange');
		    var winHeight = window.screen.height;
			var scHeight = document.body.scrollTop;
			var winHeight = window.screen.height;
			$('.page-content').css('min-height',winHeight);
		 });
		$(function(){
			PageUtils.init({
				"sync" : true, //异步或者form提交,默认form提交
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
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>