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
								<div class="portlet-body">
									<!-- 指标参数选择 开始 -->
									<div role="navigation" >
										<div class="form-group">
											<label class="control-label" style="width:80px !important;margin-left:20px;float: left;font-size:15px;margin-top:5px;color:#444;">对比指标:</label>
											<select class="form-control input-small select2me" style="width:160px !important;" name="kpiType" id="kpiType">
												<option value="1" <#if kpiBean.kpiType?? && kpiBean.kpiType == 1>selected</#if>>RSRP</option>
												<option value="2" <#if kpiBean.kpiType?? && kpiBean.kpiType == 2>selected</#if>>SNR</option>
												<option value="3" <#if kpiBean.kpiType?? && kpiBean.kpiType == 3>selected</#if>>RSRQ</option>
												<option value="4" <#if kpiBean.kpiType?? && kpiBean.kpiType == 4>selected</#if>>下行链路带宽</option>
												<option value="5" <#if kpiBean.kpiType?? && kpiBean.kpiType == 5>selected</#if>>延时</option>
												<option value="6" <#if kpiBean.kpiType?? && kpiBean.kpiType == 6>selected</#if>>丢包</option>
												<option value="9" <#if kpiBean.kpiType?? && kpiBean.kpiType == 9>selected</#if>>网络掉线率</option>
												<option value="10" <#if kpiBean.kpiType?? && kpiBean.kpiType == 10>selected</#if>>业务掉线率</option>
											</select>
										</div>
										<form action="${application.getContextPath()}/kpiSet/query" id="kpiQuery" role="search" method="post" >
											<input type="hidden" name="kpiType" id="kpiType_query" value="${(kpiBean.kpiType)!}" />
										</form>
									</div>
									<!-- 指标参数选择 结束 -->
									<!-- 分隔线 开始 -->
									<div style="width: 100%;float: left;margin-left:10px;argin-bottom:10px;vertical-align:middle;">
										<div style="border-top:3px solid #ddd;height:1px;width: 100%;float: left;">
										</div>
									</div>
									<!-- 分隔线 结束 -->
									<!-- 指标参数设置 开始 -->
									<div style="margin-top:10px;margin-left:20px;float: left;width:100%">
										<div class="table-responsive">
											<form action="${application.getContextPath()}/kpiSet/update" id="kpiForm" role="search" method="post" >
												<input type="hidden" name="kpiType" id="kpiType_page" value="${(kpiBean.kpiType)!}" />
												<table class="table table-hover">
												<thead>
													<tr>
														<th style="text-align:center; vertical-align:middle;">区间</th>
														<th style="text-align:center; vertical-align:middle;">范围</th>
														<th style="text-align:center; vertical-align:middle;">颜色</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td style="text-align:center; vertical-align:middle;">区间一</td>
														<td style="text-align:center; vertical-align:middle;">
																<div class="form-group" style="margin-top:14px;margin-left:105px;">
																<input class="form-control" type="text" id="kpiRangVal11" name="kpiRangVal11" value="${(kpiBean.kpiRangVal11)!}" style="width:60px !important;margin-left:6px;float:left;" onkeyup="value=value.replace(/[^0-9\-]/g,'')" maxlength="5"/>
																</div>
																<!--<select class="form-control input-small select2me" style="width:60px !important;float:left;margin-left:6px;" name="kpiRangOpt11" id="kpiRangOpt11">
																	<option value="1" <#if kpiBean.kpiRangOpt11?? && kpiBean.kpiRangOpt11 == 1>selected</#if>>&lt;</option>
																	<option value="2" <#if kpiBean.kpiRangOpt11?? && kpiBean.kpiRangOpt11 == 2>selected</#if>>&lt;=</option>
																</select>-->
																<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
																<input class="form-control" type="text" readonly="readonly" value="&lt;=" style="width:60px !important;margin-left:6px;float:left;"/>
																<input class="form-control" type="hidden" name="kpiRangOpt11" id="kpiRangOpt11" value="2" style="width:60px !important;margin-left:6px;float:left;"/>
																<span style="float:left;margin-left:6px;margin-top:7px;">X</span>
																<!--<select class="form-control input-small select2me" style="width:60px !important;float:left;margin-left:6px;" name="kpiRangOpt12" id="kpiRangOpt12">
																	<option value="1" <#if kpiBean.kpiRangOpt12?? && kpiBean.kpiRangOpt12 == 1>selected</#if>>&lt;</option>
																	<option value="2" <#if kpiBean.kpiRangOpt12?? && kpiBean.kpiRangOpt12 == 2>selected</#if>>&lt;=</option>
																</select>-->
																<input class="form-control" type="text" readonly="readonly" value="&lt;=" style="width:60px !important;margin-left:6px;float:left;"/>
																<input class="form-control" type="hidden" name="kpiRangOpt12" id="kpiRangOpt12" value="2" style="width:60px !important;margin-left:6px;float:left;"/>
																<input class="form-control" type="text" readonly="readonly" id="kpiRangVal12" name="kpiRangVal12" value="${(kpiBean.kpiRangVal12)!}" style="width:60px !important;margin-left:6px;float:left;" onkeyup="value=value.replace(/[^0-9\-]/g,'')" maxlength="5"/>
																<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
														</td>
														<td style="text-align:center; vertical-align:middle;">
															<input class="color {valueElement:'kpiRangColor1'}" readonly="readonly" id="bc1" style="border:0px;background:#${(kpiBean.kpiRangColor1)!};width:160px;height:35px;line-height:26px;">
															<input type="hidden" value="${(kpiBean.kpiRangColor1)!}" name="kpiRangColor1" id="kpiRangColor1">
														</td>
													</tr>
													<tr>
														<td style="text-align:center; vertical-align:middle;">区间二</td>
														<td style="text-align:center; vertical-align:middle;">
															<div class="form-group" style="margin-top:14px;margin-left:105px;"><input class="form-control" type="text" id="kpiRangVal21" name="kpiRangVal21" value="${(kpiBean.kpiRangVal21)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;"
															 onkeyup="value=value.replace(/[^0-9\-]/g,'');document.getElementById('kpiRangVal12').value=this.value;" onblur="document.getElementById('kpiRangVal12').value=this.value" maxlength="5"/></div>
															<!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt21" id="kpiRangOpt21">
																<option value="1" <#if kpiBean.kpiRangOpt21?? && kpiBean.kpiRangOpt21 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt21?? && kpiBean.kpiRangOpt21 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
															<input class="form-control" type="text"  readonly="readonly" value="&lt;" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt21" id="kpiRangOpt21" value="1" style="width:60px !important;margin-left:6px;float:left;"/>
															<span style="float:left;margin-left:6px;margin-top:7px;">X</span>
															<!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt22" id="kpiRangOpt22">
																<option value="1" <#if kpiBean.kpiRangOpt22?? && kpiBean.kpiRangOpt22 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt22?? && kpiBean.kpiRangOpt22 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<input class="form-control" type="text" readonly="readonly" value="&lt;=" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt22" id="kpiRangOpt22" value="2" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="text" readonly="readonly" id="kpiRangVal22" name="kpiRangVal22" value="${(kpiBean.kpiRangVal22)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;" onkeyup="value=value.replace(/[^0-9\-]/g,'')" maxlength="5"/>
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
														</td>
														<td style="text-align:center; vertical-align:middle;">
															<input class="color {valueElement:'kpiRangColor2'}" readonly="readonly" id="bc2" style="border:0px;background:#${(kpiBean.kpiRangColor2)!};width:160px;height:35px;line-height:26px;">
															<input type="hidden" value="${(kpiBean.kpiRangColor2)!}" name="kpiRangColor2" id="kpiRangColor2">
														</td>
													</tr>
													<tr>
														<td style="text-align:center; vertical-align:middle;">区间三</td>
														<td style="text-align:center; vertical-align:middle;">
															<div class="form-group" style="margin-top:14px;margin-left:105px;"><input class="form-control" type="text" id="kpiRangVal31" name="kpiRangVal31" value="${(kpiBean.kpiRangVal31)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;" 
															 onkeyup="value=value.replace(/[^0-9\-]/g,'');document.getElementById('kpiRangVal22').value=this.value;" onblur="document.getElementById('kpiRangVal22').value=this.value" maxlength="5"/></div>
															<!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt31" id="kpiRangOpt31">
																<option value="1" <#if kpiBean.kpiRangOpt31?? && kpiBean.kpiRangOpt31 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt31?? && kpiBean.kpiRangOpt31 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
															<input class="form-control" type="text" readonly="readonly" value="&lt;" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt31" id="kpiRangOpt31" value="1" style="width:60px !important;margin-left:6px;float:left;"/>
															<span style="float:left;margin-left:6px;margin-top:7px;">X</span>
															<!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt32" id="kpiRangOpt32">
																<option value="1" <#if kpiBean.kpiRangOpt32?? && kpiBean.kpiRangOpt32 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt32?? && kpiBean.kpiRangOpt32 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<input class="form-control" type="text" readonly="readonly" value="&lt;=" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt32" id="kpiRangOpt32" value="2" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="text" readonly="readonly" id="kpiRangVal32" name="kpiRangVal32" value="${(kpiBean.kpiRangVal32)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;" onkeyup="value=value.replace(/[^0-9\-]/g,'')" maxlength="5"/>
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
														</td>
														<td style="text-align:center; vertical-align:middle;">
															<input class="color {valueElement:'kpiRangColor3'}" readonly="readonly" id="bc3" style="border:0px;background:#${(kpiBean.kpiRangColor3)!};width:160px;height:35px;line-height:26px;">
															<input type="hidden" value="${(kpiBean.kpiRangColor3)!}" name="kpiRangColor3" id="kpiRangColor3">
														</td>
													</tr>
													<tr>
														<td style="text-align:center; vertical-align:middle;">区间四</td>
														<td style="text-align:center; vertical-align:middle;">
															<div class="form-group" style="margin-top:14px;margin-left:105px;"><input class="form-control" type="text" id="kpiRangVal41" name="kpiRangVal41" value="${(kpiBean.kpiRangVal41)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;" 
															 onkeyup="value=value.replace(/[^0-9\-]/g,'');document.getElementById('kpiRangVal32').value=this.value;" onblur="document.getElementById('kpiRangVal32').value=this.value" maxlength="5"/></div>
															 <!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt41" id="kpiRangOpt41">
																<option value="1" <#if kpiBean.kpiRangOpt41?? && kpiBean.kpiRangOpt41 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt41?? && kpiBean.kpiRangOpt41 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
															<input class="form-control" type="text" readonly="readonly" value="&lt;" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt41" id="kpiRangOpt41" value="1" style="width:60px !important;margin-left:6px;float:left;"/>
															<span style="float:left;margin-left:6px;margin-top:7px;">X</span>
															<!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt42" id="kpiRangOpt42">
																<option value="1" <#if kpiBean.kpiRangOpt42?? && kpiBean.kpiRangOpt42 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt42?? && kpiBean.kpiRangOpt42 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<input class="form-control" type="text" readonly="readonly" value="&lt;=" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt42" id="kpiRangOpt42" value="2" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="text" readonly="readonly" id="kpiRangVal42" name="kpiRangVal42" value="${(kpiBean.kpiRangVal42)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;" onkeyup="value=value.replace(/[^0-9\-]/g,'')" maxlength="5"/>
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
														</td>
														<td style="text-align:center; vertical-align:middle;">
															<input class="color {valueElement:'kpiRangColor4'}" readonly="readonly" id="bc4" style="border:0px;background:#${(kpiBean.kpiRangColor4)!};width:160px;height:35px;line-height:26px;">
															<input type="hidden" value="${(kpiBean.kpiRangColor4)!}" name="kpiRangColor4" id="kpiRangColor4">
														</td>
													</tr>
													<tr>
														<td style="text-align:center; vertical-align:middle;">区间五</td>
														<td style="text-align:center; vertical-align:middle;">
															<div class="form-group" style="margin-top:14px;margin-left:105px;"><input class="form-control" type="text" id="kpiRangVal51" name="kpiRangVal51" value="${(kpiBean.kpiRangVal51)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;" 
															 onkeyup="value=value.replace(/[^0-9\-]/g,'');document.getElementById('kpiRangVal42').value=this.value;" onblur="document.getElementById('kpiRangVal42').value=this.value" maxlength="5"/></div>
															 <!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt51" id="kpiRangOpt51">
																<option value="1" <#if kpiBean.kpiRangOpt51?? && kpiBean.kpiRangOpt51 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt51?? && kpiBean.kpiRangOpt51 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
															<input class="form-control" type="text" readonly="readonly" value="&lt;" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt51" id="kpiRangOpt51" value="1" style="width:60px !important;margin-left:6px;float:left;"/>
															<span style="float:left;margin-left:6px;margin-top:7px;">X</span>
															<!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt52" id="kpiRangOpt52">
																<option value="1" <#if kpiBean.kpiRangOpt52?? && kpiBean.kpiRangOpt52 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt52?? && kpiBean.kpiRangOpt52 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<input class="form-control" type="text" readonly="readonly" value="&lt;=" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt52" id="kpiRangOpt52" value="2" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="text" id="kpiRangVal52" readonly="readonly" name="kpiRangVal52" value="${(kpiBean.kpiRangVal52)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;" onkeyup="value=value.replace(/[^0-9\-]/g,'')" maxlength="5"/>
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
														</td>
														<td style="text-align:center; vertical-align:middle;">
															<input class="color {valueElement:'kpiRangColor5'}" readonly="readonly" id="bc5" style="border:0px;background:#${(kpiBean.kpiRangColor5)!};width:160px;height:35px;line-height:26px;">
															<input type="hidden" value="${(kpiBean.kpiRangColor5)!}" name="kpiRangColor5" id="kpiRangColor5">
														</td>
													</tr>
													<tr>
														<td style="text-align:center; vertical-align:middle;">区间六</td>
														<td style="text-align:center; vertical-align:middle;">
															<div class="form-group" style="margin-top:14px;margin-left:105px;"><input class="form-control" type="text" id="kpiRangVal61" name="kpiRangVal61" value="${(kpiBean.kpiRangVal61)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;" 
															 onkeyup="value=value.replace(/[^0-9\-]/g,'');document.getElementById('kpiRangVal52').value=this.value;" onblur="document.getElementById('kpiRangVal52').value=this.value" maxlength="5"/></div>
															<!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt61" id="kpiRangOpt61">
																<option value="1" <#if kpiBean.kpiRangOpt61?? && kpiBean.kpiRangOpt61 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt61?? && kpiBean.kpiRangOpt61 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
															<input class="form-control" type="text" readonly="readonly" value="&lt;" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt61" id="kpiRangOpt61" value="1" style="width:60px !important;margin-left:6px;float:left;"/>
															<span style="float:left;margin-left:6px;margin-top:7px;">X</span>
															<!--<select class="form-control input-small select2me" style="width:60px !important;margin-left:6px;float:left;" name="kpiRangOpt62" id="kpiRangOpt62">
																<option value="1" <#if kpiBean.kpiRangOpt62?? && kpiBean.kpiRangOpt62 == 1>selected</#if>>&lt;</option>
																<option value="2" <#if kpiBean.kpiRangOpt62?? && kpiBean.kpiRangOpt62 == 2>selected</#if>>&lt;=</option>
															</select>-->
															<input class="form-control" type="text" value="&lt;=" readonly="readonly" style="width:60px !important;margin-left:6px;float:left;"/>
															<input class="form-control" type="hidden" name="kpiRangOpt62" id="kpiRangOpt62" value="2" style="width:60px !important;margin-left:6px;float:left;"/>
															<div class="form-group" style="margin-top:14px;"><input class="form-control" type="text" id="kpiRangVal62" name="kpiRangVal62" value="${(kpiBean.kpiRangVal62)!}" style="margin-left:6px;float:left;width:60px !important;margin-left:6px;" onkeyup="value=value.replace(/[^0-9\-]/g,'')" maxlength="5"/></div>
															<span style="float:left;margin-right:6px;margin-left:2px;margin-top:7px;">${(unit)!}</span>
														</td>
														<td style="text-align:center; vertical-align:middle;">
															<input class="color {valueElement:'kpiRangColor6'}" readonly="readonly" id="bc6" style="border:0px;background:#${(kpiBean.kpiRangColor6)!};width:160px;height:35px;line-height:26px;">
															<input type="hidden" value="${(kpiBean.kpiRangColor6)!}" name="kpiRangColor6" id="kpiRangColor6">
														</td>
													</tr>
													<tr style="display:none;">
														<td style="text-align:center; vertical-align:middle;">指标图形</td>
														<td style="text-align:left; vertical-align:middle;" colspan="2">
															<select class="form-control input-small select2me" style="width:120px !important;float:left;margin-left:111px;" name="kpiGrapId" id="kpiGrapId">
																<option value="BMAP_POINT_SHAPE_CIRCLE" <#if kpiBean.kpiGrapId?? && kpiBean.kpiGrapId == 'BMAP_POINT_SHAPE_CIRCLE'>selected</#if>>圆形</option>
																<option value="BMAP_POINT_SHAPE_STAR" <#if kpiBean.kpiGrapId?? && kpiBean.kpiGrapId == 'BMAP_POINT_SHAPE_STAR'>selected</#if>>星形</option>
																<option value="BMAP_POINT_SHAPE_SQUARE" <#if kpiBean.kpiGrapId?? && kpiBean.kpiGrapId == 'BMAP_POINT_SHAPE_SQUARE'>selected</#if>>方形</option>
																<option value="BMAP_POINT_SHAPE_RHOMBUS" <#if kpiBean.kpiGrapId?? && kpiBean.kpiGrapId == 'BMAP_POINT_SHAPE_RHOMBUS'>selected</#if>>菱形</option>
																<!--<option value="BMAP_POINT_SHAPE_WATERDROP" <#if kpiBean.kpiGrapId?? && kpiBean.kpiGrapId == 'BMAP_POINT_SHAPE_WATERDROP'>selected</#if>>水滴形</option>-->
															</select>
														</td>
													</tr>
												</table>
											</form>
											<button class="btn blue" style="float:right;margin-right:90px;width:62px;margin-top:50px;" id="updateBtn">保存</button>
										</div>
									</div>
									<!-- 指标参数设置 结束 -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="${application.getContextPath()}/js/jscolor/jscolor.js" type="text/javascript"></script>
	<script type="text/javascript" src="${application.getContextPath()}/js/kpiSet/kpiSet.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/js/kpiSet/kpiValidater.js"></script>
	<script type="text/javascript">
	jQuery(document).ready(function() {
		    var winHeight = window.screen.height;
			var scHeight = document.body.scrollTop;
			var winHeight = window.screen.height;
			$('.page-content').css('min-height',winHeight);
			kpiValidate.init();
			$("#modal-backdrop").hide();
		 });
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>