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
										<!--时间粒度选择开始-->
										<div class="form-group">
											<label>时间粒度:</label>
											<select class="form-control input-small select2me" style="width: 80px !important;" id="timegransel" onchange="changeDateRange()">
												<!--<option value="1" <#if (timeGranule)??&&timeGranule=='1' >selected</#if>>时</option>-->
												<option value="1" <#if (queryBean.timegransel)??&&queryBean.timegransel=='1' >selected</#if>>天</option>
												<option value="2" <#if (queryBean.timegransel)??&&queryBean.timegransel=='2' >selected</#if>>周</option>
												<option value="3" <#if (queryBean.timegransel)??&&queryBean.timegransel=='3' >selected</#if>>月</option>
											</select>
										</div>
										<input type="hidden" id="qryflg" value="0"/>
										<!--保存变更后时间粒度-->
										<input type="hidden" id="queryTime_back" name="queryTime_back" value="${(queryBean.queryTime)!}" />
										<!--时间粒度选择结束-->
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
										
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">运营商:</label>
											<select class="form-control input-small select2me" style="width:165px;" name="operator" id="operator">
												<#if operators ?? && operators?size &gt; 0>
													<#list operators as p>
														<option value="${(p.id)!}" <#if queryBean.operator?? && p.id == queryBean.operator>selected</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
										</div>
										
										<!--区域  开始-->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">区域:</label>
											<select class="form-control input-small select2me" hasAreaAll="1" style="width:165px;" name="province" id="province">
												<#if countrys ?? && countrys?size &gt; 0>
													<#list countrys as p>
														<option value="${(p.id)!}" <#if queryBean?? && p.id == queryBean.province>selected</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
											&nbsp;
											<select class="fform-control input-small select2me" hasAreaAll="1" <#if user?? && user.district?? && user.district=='-1'>hasAreaAll="1"</#if> style="width:165px;margin-left:-5px;" name="district" id="district">
												<#if user?? && user.district?? && user.district=='-1'><option value="-1"  <#if queryBean?? && '-1' == queryBean.district>selected="true"</#if>>全部</option></#if>
												<#if citys ?? && citys?size &gt; 0>
													<#list citys as p>
														<option value="${(p.id)!}" <#if queryBean?? && p.id == queryBean.district>selected</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
											&nbsp;
											<select class="form-control input-small select2me" hasAreaAll="1" <#if user?? && user.county?? && user.county=='-1'>hasAreaAll="1"</#if> style="width:165px;margin-left:-5px;" name="area" id="area">
												<#if user?? && user.county?? && user.county=='-1'><option value="-1" <#if queryBean?? && '-1' == queryBean.area>selected="true"</#if>>全部</option></#if>
												<#if areas ?? && areas?size &gt; 0>
													<#list areas as p>
														<option value="${(p.id)!}" <#if queryBean?? && p.id == queryBean.area>selected</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
										</div>
										<!-- 区域 结束 -->
										
										<br/>
										
										<!--道路  开始-->
										<div class="form-group">
											<label class="control-label">道路类型:</label>
											<select class="form-control input-small select2me" style="width: 80px !important;" name="roadType" id="roadType">
												<option value="-1" <#if queryBean.roadType ?? && queryBean.roadType==-1>selected</#if>>全部</option>
												<option value="1" <#if queryBean.roadType ?? && queryBean.roadType==1>selected</#if>>道路</option>
												<option value="2" <#if queryBean.roadType ?? && queryBean.roadType==2>selected</#if>>桥梁</option>
												<option value="3" <#if queryBean.roadType ?? && queryBean.roadType==3>selected</#if>>隧道</option>
											</select>
											&nbsp;
											<label class="control-label" style="margin-left:25px;">等级:</label>
											<select class="fform-control input-small select2me" style="width:165px;" name="roadLevel" id="roadLevel">
												<option value="-1" <#if queryBean.roadLevel ?? && queryBean.roadLevel==-1>selected</#if>>全部</option>
												<option value="1" <#if queryBean.roadLevel ?? && queryBean.roadLevel==1>selected</#if>>主干路</option>
												<option value="2" <#if queryBean.roadLevel ?? && queryBean.roadLevel==2>selected</#if>>次干路</option>
												<option value="3" <#if queryBean.roadLevel ?? && queryBean.roadLevel==3>selected</#if>>高速公路</option>
												<option value="4" <#if queryBean.roadLevel ?? && queryBean.roadLevel==4>selected</#if>>国道</option>
												<option value="5" <#if queryBean.roadLevel ?? && queryBean.roadLevel==5>selected</#if>>省道</option>
												<option value="6" <#if queryBean.roadLevel ?? && queryBean.roadLevel==6>selected</#if>>县道</option>
												<option value="7" <#if queryBean.roadLevel ?? && queryBean.roadLevel==7>selected</#if>>乡村道路</option>
											</select>
											&nbsp;
											<label class="control-label" style="margin-left:58px;">名称:</label>
											<!--<select class="form-control input-small select2me" style="width:165px;" hasAll="1" name="roadId" id="roadId">
												<option value="-1" >全部</option>
												<#if roads ?? && roads?size &gt; 0>
													<#list roads as p>
														<option value="${(p.id)!}" <#if queryBean?? && queryBean.roadId?? && p.id == queryBean.roadId>selected</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>-->
											<input class="form-control autocomplete_road" name="roadName" nonull="0" value="${(queryBean.roadName)!''}" id="roadName" placeholder="全部" style="width:160px !important;" onclick="$('#roadName').autocomplete('search', document.getElementById('roadName') );"/>
											<input type="hidden" name="roadName_v" id="roadName_v" value="${(queryBean.roadName)!''}"/>
											<input type="hidden" name="roadId" id="roadId" value="${(queryBean.roadId)!'-1'}"/>
										</div>
										<!--道路 结束-->
										<!--<div class="form-group"  style="margin-left:8px;">
											<label class="control-label">带宽占比阀值:</label>
											<input class="form-control" type="text" id="threshold" name="threshold" style="width:70px !important;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="4" value="${(queryBean.threshold)!'0'}"/>
										</div>-->
										<input class="form-control" type="hidden" id="threshold" name="threshold" style="width:70px !important;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="4" value="${(queryBean.threshold)!'12'}"/>
										<!-- 区间指标 开始 -->
										<br/>
										<div class="form-group checkbox" style="width:450px;margin-top:20px;" >
											<label class="control-label" style="float:left;">指标选择:&nbsp;&nbsp;</label>
											<label>
												<input id="rsrp" name="kpiInvl" type="checkbox" value="rsrp" <#if queryBean.rsrp?? && queryBean.rsrp==1>checked</#if> /> RSRP
											</label>
											<label>
												<input id="rsrq" name="kpiInvl"  type="checkbox" value="rsrq" <#if queryBean.rsrq?? && queryBean.rsrq==1>checked</#if> /> RSRQ
											</label>
											<label>
												<input id="snr" name="kpiInvl" type="checkbox" value="snr" <#if queryBean.snr?? && queryBean.snr==1>checked</#if>  /> SNR
											</label>
											<label>
												<input id="broadband" name="kpiInvl" type="checkbox" value="broadband" <#if queryBean.broadband?? && queryBean.broadband==1>checked</#if>  /> 下行链路带宽
											</label>
											<label>
												<input id="delay" name="kpiInvl" type="checkbox" value="delay" <#if queryBean.delay?? && queryBean.delay ==1>checked</#if> /> 延时
											</label>
											<label>
												<input id="lose" name="kpiInvl" type="checkbox" value="lose" <#if queryBean.lose?? && queryBean.lose ==1>checked</#if> /> 丢包
											</label>
										</div>
										<!-- 区间指标 结束 -->
										
										<br/>
										
										<!-- 维度选择 开始 -->
										<div class="form-group" style="width:600px;">
											<label class="control-label" style="float:left;">指标维度:&nbsp;&nbsp;</label>
											<label>
												<input id="showTime" name="dimension" type="checkbox" value="showTime" <#if queryBean.showTime?? && queryBean.showTime ==1>checked</#if> /> 时间
											</label>
											<label>
												<input id="showArea" name="dimension" type="checkbox" value="showArea" <#if queryBean.showArea?? && queryBean.showArea ==1>checked</#if> /> 区域
											</label>
											<label>
												<input id="showRoad" name="dimension" type="checkbox" value="showRoad" <#if queryBean.showRoad?? && queryBean.showRoad ==1>checked</#if> /> 道路
											</label>
											<label>
												<input id="showOperator" name="dimension" type="checkbox" value="showOperator" <#if queryBean.showOperator?? && queryBean.showOperator ==1>checked</#if> /> 运营商
											</label>
											<label>
												<input id="showInterval" name="dimension" type="checkbox" value="showInterval" <#if queryBean.showInterval?? && queryBean.showInterval ==1>checked</#if> /> 指标区间
											</label>
										</div>
										<!-- 维度选择 结束 -->
										<button class="btn green" id="leadExcel" style="float:right;margin-top:-10px;width:62px;">导出</button>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<button class="btn blue" id="querybtn" style="float:right;margin:-10px 10px 0 0;width:62px;">查询</button>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<button class="btn yellow" id="taskbtn" style="float:right;margin:-10px 10px 0 0;width:75px;">查询任务</button>
									</form>
								</div>
								<!-- 查询条件 -->
								<form id="pageForm" name="pageForm" action="${application.getContextPath()}/compreQuota/query" method="post">
									
									<input type="hidden" name="totalPage" id="totalPage" value="${(page.totalPage)!1}" />
									<input type="hidden" name="pageSize" id="pageSize" value="${(page.pageSize)!10}" />
									<input type="hidden" name="pageIndex" id="pageIndex" value="${(page.pageIndex)!1}" />
									<!--UUID-->
									<input type="hidden" id="uuid_page" name="uuid" value="${(queryBean.uuid)!}"/>
									<!--时间-->
									<input type="hidden" id="timegransel_page" name="timegransel" value="${(queryBean.timegransel)!}"/>
									<input type="hidden" id="queryTime_page" name="queryTime" value="${(queryBean.queryTime)!}"/>
									<!--运营商-->
									<input type="hidden" id="operator_page" name="operator" value="${(queryBean.operator)!}"/>
									<!--区域-->
									<input type="hidden" id="province_page" name="province" value="${(queryBean.province)!}"/>
									<input type="hidden" id="district_page" name="district" value="${(queryBean.district)!'-1'}"/>
									<input type="hidden" id="area_page" name="area" value="${(queryBean.area)!'-1'}"/>
									<!--道路-->
									<input type="hidden" id="roadType_page" name="roadType" value="${(queryBean.roadType)!}"/>
									<input type="hidden" id="roadLevel_page" name="roadLevel" value="${(queryBean.roadLevel)!}"/>
									<input type="hidden" id="roadId_page" name="roadId" value="${(queryBean.roadId)!}"/>
									<input type="hidden" id="roadName_page" name="roadName" value="${(queryBean.roadName)!}"/>
									<!-- 下行宽带占比阀值 -->
									<input type="hidden" id="threshold_page" name="threshold" value="${(queryBean.threshold)!'12'}"/>
									<!--指标-->
									<input type="hidden" id="rsrp_page" name="rsrp" value="${(queryBean.rsrp)!}"/>
									<input type="hidden" id="rsrq_page" name="rsrq" value="${(queryBean.rsrq)!}"/>
									<input type="hidden" id="snr_page" name="snr" value="${(queryBean.snr)!}"/>
									<input type="hidden" id="broadband_page" name="broadband" value="${(queryBean.broadband)!}"/>
									<input type="hidden" id="delay_page" name="delay" value="${(queryBean.delay)!}"/>
									<input type="hidden" id="lose_page" name="lose" value="${(queryBean.lose)!}"/>
									<!--维度-->
									<input type="hidden" id="showTime_page" name="showTime" value="${(queryBean.showTime)!}"/>
									<input type="hidden" id="showArea_page" name="showArea" value="${(queryBean.showArea)!}"/>
									<input type="hidden" id="showRoad_page" name="showRoad" value="${(queryBean.showRoad)!}"/>
									<input type="hidden" id="showOperator_page" name="showOperator" value="${(queryBean.showOperator)!}"/>
									<input type="hidden" id="showInterval_page" name="showInterval" value="${(queryBean.showInterval)!}"/>
								</form>
								<!-- 跳转地图查询条件 开始 -->
								<form id="linkMap" name="linkMap" action="${application.getContextPath()}/roadMap/roadMap?op_menu=5" method="post" >
									<input type="hidden" id="hasTime" value="${(hasTime)!}"/>
									<input type="hidden" id ="showTime_map" value="${(queryBean.showTime)!}" />
									<!--区域-->
									<input type="hidden" id="province_map" name="province" value="${(queryBean.province)!}"/>
									<input type="hidden" id="district_map" name="district"/>
									<input type="hidden" id="area_map" name="area"/>
									<!--道路-->
									<input type="hidden" id="roadType_map" name="roadType"/>
									<input type="hidden" id="roadLevel_map" name="roadLevel"/>
									<input type="hidden" id="roadId_map" name="roadId"/>
									<!--时间-->
									<input type="hidden" id="starttime_map" name="starttime"/>
									<input type="hidden" id="endtime_map" name="endtime"/>
									<!--运营商-->
									<input type="hidden" id="userId_map" name="userId" value="${(Session.user.userid)!}"/>
									<!--运营商-->
									<input type="hidden" id="operator_map" name="operator" value="${(queryBean.operator)!}"/>
									<!--指标类型  默认选择RSRP-->
									<input type="hidden" id="kpiType_map" name="kpiType" value="1"/>
									<!--指标类型  事件  默认无事件-->
									<input type="hidden" id="event_map" name="event" value="-1"/>
									<!-- 指标区间 -->
									<input type="hidden" id="kpiInterv_map" name="kpiInterv" value="1,2,3,4,5,6"/>
									<!-- 事件默认-1 -->
									<input type="hidden" id="warn_map" name="warn" value="-1"/>
								</form>
								<!-- 跳转地图查询条件 结束 -->
								<!--表单开始   开始-->
								<div class="table-responsive table-scrollable">
									<table class="table table-striped table-hover table-bordered" id="sample_editable_1">
										<!--表单title 开始-->
										<thead>
											<tr style="background-color:#EAEAEA;">
												<#if queryBean?? &&  queryBean.showTime?? &&  queryBean.showTime ==1>
													<th style="text-align:center;" >开始时间</th>
													<th style="text-align:center;" >结束时间</th>
												</#if>
												<#if queryBean?? &&  queryBean.showArea?? &&  queryBean.showArea ==1>
													<th style="text-align:center;" >市级</th>
													<th style="text-align:center;" >区县</th>
												</#if>
												<#if queryBean?? &&  queryBean.showRoad?? &&  queryBean.showRoad ==1>
													<!--<th style="text-align:center;" >道路类型</th>-->
													<!--<th style="text-align:center;" >道路等级</th>-->
													<th style="text-align:center;" >道路名称</th>
												</#if>
												<#if queryBean?? &&  queryBean.showOperator?? &&  queryBean.showOperator ==1>
													<th style="text-align:center;" >运营商</th>
												</#if>
												
													<th style="text-align:center;" >信号采样点数量</th>
													<th style="text-align:center;" >业务总采样点数</th>
													<!--<th style="text-align:center;" >弱覆盖点数量</th>-->
													<!--<th style="text-align:center;" >无线覆盖率(%)</th>-->
													<th style="text-align:center;" >测试里程(km)</th>
													<th style="text-align:center;" >覆盖里程(km)</th>
													<th style="text-align:center;" >弱覆盖里程(km)</th>
													<th style="text-align:center;" >道路点覆盖率(%)</th>
													
													<th style="text-align:center;" >网络掉线次数</th>
													<th style="text-align:center;" >网络掉线率(%)</th>
													<th style="text-align:center;" >业务掉线次数</th>
													<th style="text-align:center;" >业务掉线率(%)</th>
													<th style="text-align:center;" >4G下切3G次数</th>
													
													<th style="text-align:center;" >RSRP(dBm)</th>
												<#if queryBean?? &&  queryBean.showInterval?? &&  queryBean.showInterval ==1>
													<#if queryBean.rsrp?? &&  queryBean.rsrp ==1>
														<!-- RSCP区间 -->
														<!--<th style="text-align:center;" >${(titleName.rsrpRange1)!'RSRP区间一'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange1)!'RSRP区间一'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange2)!'RSRP区间二'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange2)!'RSRP区间二'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange3)!'RSRP区间三'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange3)!'RSRP区间三'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange4)!'RSRP区间四'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange4)!'RSRP区间四'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange5)!'RSRP区间五'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange5)!'RSRP区间五'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange6)!'RSRP区间六'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrpRange6)!'RSRP区间六'} (采样点)</th>-->
														<th style="text-align:center;" >RSRP${(titleName.rsrpRange1)!'区间一'}</th>
														<th style="text-align:center;" >RSRP${(titleName.rsrpRange2)!'区间二'}</th>
														<th style="text-align:center;" >RSRP${(titleName.rsrpRange3)!'区间三'}</th>
														<th style="text-align:center;" >RSRP${(titleName.rsrpRange4)!'区间四'}</th>
														<th style="text-align:center;" >RSRP${(titleName.rsrpRange5)!'区间五'}</th>
														<th style="text-align:center;" >RSRP${(titleName.rsrpRange6)!'区间六'}</th>
													</#if>
												</#if>
												
													<th style="text-align:center;" >RSRQ(dB)</th>
												<#if queryBean?? &&  queryBean.showInterval?? &&  queryBean.showInterval ==1>
													<#if queryBean.rsrq?? &&  queryBean.rsrq ==1>
														<!-- RSRQ区间 -->
														<!--<th style="text-align:center;" >${(titleName.rsrqRange1)!'RSRQ区间一'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange1)!'RSRQ区间一'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange2)!'RSRQ区间二'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange2)!'RSRQ区间二'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange3)!'RSRQ区间三'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange3)!'RSRQ区间三'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange4)!'RSRQ区间四'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange4)!'RSRQ区间四'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange5)!'RSRQ区间五'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange5)!'RSRQ区间五'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange6)!'RSRQ区间六'} (占比)</th>
														<th style="text-align:center;" >${(titleName.rsrqRange6)!'RSRQ区间六'} (采样点)</th>-->
														<th style="text-align:center;" >RSRQ${(titleName.rsrqRange1)!'区间一'}</th>
														<th style="text-align:center;" >RSRQ${(titleName.rsrqRange2)!'区间二'}</th>
														<th style="text-align:center;" >RSRQ${(titleName.rsrqRange3)!'区间三'}</th>
														<th style="text-align:center;" >RSRQ${(titleName.rsrqRange4)!'区间四'}</th>
														<th style="text-align:center;" >RSRQ${(titleName.rsrqRange5)!'区间五'}</th>
														<th style="text-align:center;" >RSRQ${(titleName.rsrqRange6)!'区间六'}</th>
													</#if>
												</#if>
												
													<th style="text-align:center;" >SNR(dB)</th>
												<#if queryBean?? &&  queryBean.showInterval?? &&  queryBean.showInterval ==1>
													<#if queryBean.snr?? &&  queryBean.snr ==1>
														<!-- SNR区间 -->
														<!--<th style="text-align:center;" >${(titleName.snrRange1)!'SNR区间一'} (占比)</th>
														<th style="text-align:center;" >${(titleName.snrRange1)!'SNR区间一'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.snrRange2)!'SNR区间二'} (占比)</th>
														<th style="text-align:center;" >${(titleName.snrRange2)!'SNR区间二'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.snrRange3)!'SNR区间三'} (占比)</th>
														<th style="text-align:center;" >${(titleName.snrRange3)!'SNR区间三'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.snrRange4)!'SNR区间四'} (占比)</th>
														<th style="text-align:center;" >${(titleName.snrRange4)!'SNR区间四'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.snrRange5)!'SNR区间五'} (占比)</th>
														<th style="text-align:center;" >${(titleName.snrRange5)!'SNR区间五'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.snrRange6)!'SNR区间六'} (占比)</th>
														<th style="text-align:center;" >${(titleName.snrRange6)!'SNR区间六'} (采样点)</th>-->
														<th style="text-align:center;" >SNR${(titleName.snrRange1)!'区间一'}</th>
														<th style="text-align:center;" >SNR${(titleName.snrRange2)!'区间二'}</th>
														<th style="text-align:center;" >SNR${(titleName.snrRange3)!'区间三'}</th>
														<th style="text-align:center;" >SNR${(titleName.snrRange4)!'区间四'}</th>
														<th style="text-align:center;" >SNR${(titleName.snrRange5)!'区间五'}</th>
														<th style="text-align:center;" >SNR${(titleName.snrRange6)!'区间六'}</th>
													</#if>
												</#if>
												
													<th style="text-align:center;" >下行链路带宽(Mbps)</th>
													<th style="text-align:center;" >&gt;${(queryBean.threshold)!'12'}Mbps比例</th>
												<#if queryBean?? &&  queryBean.showInterval?? &&  queryBean.showInterval ==1>
													<#if queryBean.broadband?? &&  queryBean.broadband ==1>
														<!-- 宽带区间 -->
														<!--<th style="text-align:center;" >${(titleName.bwRange1)!'宽带区间一'} (占比)</th>
														<th style="text-align:center;" >${(titleName.bwRange1)!'宽带区间一'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.bwRange2)!'宽带区间二'} (占比)</th>
														<th style="text-align:center;" >${(titleName.bwRange2)!'宽带区间二'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.bwRange3)!'宽带区间三'} (占比)</th>
														<th style="text-align:center;" >${(titleName.bwRange3)!'宽带区间三'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.bwRange4)!'宽带区间四'} (占比)</th>
														<th style="text-align:center;" >${(titleName.bwRange4)!'宽带区间四'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.bwRange5)!'宽带区间五'} (占比)</th>
														<th style="text-align:center;" >${(titleName.bwRange5)!'宽带区间五'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.bwRange6)!'宽带区间六'} (占比)</th>
														<th style="text-align:center;" >${(titleName.bwRange6)!'宽带区间六'} (采样点)</th>-->
														<th style="text-align:center;" >带宽${(titleName.bwRange1)!'区间一'}</th>
														<th style="text-align:center;" >带宽${(titleName.bwRange2)!'区间二'}</th>
														<th style="text-align:center;" >带宽${(titleName.bwRange3)!'区间三'}</th>
														<th style="text-align:center;" >带宽${(titleName.bwRange4)!'区间四'}</th>
														<th style="text-align:center;" >带宽${(titleName.bwRange5)!'区间五'}</th>
														<th style="text-align:center;" >带宽${(titleName.bwRange6)!'区间六'}</th>
													</#if>
												</#if>
												
													<th style="text-align:center;" >时延(ms)</th>
												<#if queryBean?? &&  queryBean.showInterval?? &&  queryBean.showInterval ==1>
													<#if queryBean.delay?? &&  queryBean.delay ==1>
														<!-- 延时区间 -->
														<!--<th style="text-align:center;" >${(titleName.delayRange1)!'延时区间一'} (占比)</th>
														<th style="text-align:center;" >${(titleName.delayRange1)!'延时区间一'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.delayRange2)!'延时区间二'} (占比)</th>
														<th style="text-align:center;" >${(titleName.delayRange2)!'延时区间二'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.delayRange3)!'延时区间三'} (占比)</th>
														<th style="text-align:center;" >${(titleName.delayRange3)!'延时区间三'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.delayRange4)!'延时区间四'} (占比)</th>
														<th style="text-align:center;" >${(titleName.delayRange4)!'延时区间四'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.delayRange5)!'延时区间五'} (占比)</th>
														<th style="text-align:center;" >${(titleName.delayRange5)!'延时区间五'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.delayRange6)!'延时区间六'} (占比)</th>
														<th style="text-align:center;" >${(titleName.delayRange6)!'延时区间六'} (采样点)</th>-->
														<th style="text-align:center;" >时延${(titleName.delayRange1)!'区间一'}</th>
														<th style="text-align:center;" >时延${(titleName.delayRange2)!'区间二'}</th>
														<th style="text-align:center;" >时延${(titleName.delayRange3)!'区间三'}</th>
														<th style="text-align:center;" >时延${(titleName.delayRange4)!'区间四'}</th>
														<th style="text-align:center;" >时延${(titleName.delayRange5)!'区间五'}</th>
														<th style="text-align:center;" >时延${(titleName.delayRange6)!'区间六'}</th>
													</#if>
												</#if>
												
													<th style="text-align:center;" >丢包率(%)</th>
												<#if queryBean?? &&  queryBean.showInterval?? &&  queryBean.showInterval ==1>
													<#if queryBean.lose?? &&  queryBean.lose ==1>
														<!-- 丢包率区间 -->
														<!--<th style="text-align:center;" >${(titleName.loseRange1)!'丢包率区间一'} (占比)</th>
														<th style="text-align:center;" >${(titleName.loseRange1)!'丢包率区间一'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.loseRange2)!'丢包率区间二'} (占比)</th>
														<th style="text-align:center;" >${(titleName.loseRange2)!'丢包率区间二'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.loseRange3)!'丢包率区间三'} (占比)</th>
														<th style="text-align:center;" >${(titleName.loseRange3)!'丢包率区间三'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.loseRange4)!'丢包率区间四'} (占比)</th>
														<th style="text-align:center;" >${(titleName.loseRange4)!'丢包率区间四'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.loseRange5)!'丢包率区间五'} (占比)</th>
														<th style="text-align:center;" >${(titleName.loseRange5)!'丢包率区间五'} (采样点)</th>
														<th style="text-align:center;" >${(titleName.loseRange6)!'丢包率区间六'} (占比)</th>
														<th style="text-align:center;" >${(titleName.loseRange6)!'丢包率区间六'} (采样点)</th>-->
														<th style="text-align:center;" >丢包率${(titleName.loseRange1)!'区间一'}</th>
														<th style="text-align:center;" >丢包率${(titleName.loseRange2)!'区间二'}</th>
														<th style="text-align:center;" >丢包率${(titleName.loseRange3)!'区间三'}</th>
														<th style="text-align:center;" >丢包率${(titleName.loseRange4)!'区间四'}</th>
														<th style="text-align:center;" >丢包率${(titleName.loseRange5)!'区间五'}</th>
														<th style="text-align:center;" >丢包率${(titleName.loseRange6)!'区间六'}</th>
													</#if>
												</#if>
											</tr>
										</thead>
										<!--表单title 结束-->
										<!-- 表单内容 开始 -->
										<tbody>
											<#if page?? && page.list?? &&  page.list?size &gt; 0>
												<#list page.list as ls>
													<tr name="linkToMap" style="height:37px;cursor:pointer;<#if ls_index==page.list?size-1>border-bottom:1px #dddddd  solid;</#if>"
													dis="${(ls.level2_code)!'-1'}" area="${(ls.region_code)!'-1'}" rt="${(ls.road_type)!'-1'}" rl="${(ls.road_level)!'-1'}" ri="${(ls.road_id)!'-1'}" 
													st="${(ls.start_time)!'-'}" ed="${(ls.end_time)!'-'}" title="双击跳转道路指标GIS展现"
													oper=${(ls.telecoms)!'-1'} id="linkCol_${(ls_index)}">
													<#if queryBean?? &&  queryBean.showTime?? &&  queryBean.showTime ==1>
														<td style="text-align:center;" >${(ls.start_time)!'-'}</td>
														<td style="text-align:center;" >${(ls.end_time)!'-'}</td>
													</#if>
													<#if queryBean?? &&  queryBean.showArea?? &&  queryBean.showArea ==1>
														<td style="text-align:center;" >${(ls.parent_region)!'-'}</td>
														<td style="text-align:center;" >${(ls.region_name)!'-'}</td>
													</#if>
													<#if queryBean?? &&  queryBean.showRoad?? &&  queryBean.showRoad ==1>
														<!--<td style="text-align:center;" >${(ls.road_type)!'-'}</td>-->
														<!--<td style="text-align:center;" >${(ls.road_level)!'-'}</td>-->
														<td style="text-align:center;" >${(ls.road_name)!'-'}</td>
													</#if>
													<#if queryBean?? &&  queryBean.showOperator?? &&  queryBean.showOperator ==1>
														<td style="text-align:center;" >${(ls.telecoms)!'-'}</td>
													</#if>
													
														<td style="text-align:center;" >${(ls.point_signal)!'-'}</td>
														<td style="text-align:center;" >${(ls.point_business)!'-'}</td>
														<!--弱覆盖点数量-->
														<!--<td style="text-align:center;" >${(ls.point_poor_coverage)!'-'}</td>-->
														<!-- 无线覆盖率(%) -->
														<!--<td style="text-align:center;" >${(ls.wireless_coverage)!'-'}</td>-->
														<!--道路测试里程(km)-->
														<td style="text-align:center;" >${(ls.road_test_mileage)!'-'}</td>
														<!--道路测试覆盖里程(km)-->
														<td style="text-align:center;" >${(ls.road_cover_mileage)!'-'}</td>
														<!--道路弱覆盖路段里程(km)-->
														<td style="text-align:center;" >${(ls.road_poor_mileage)!'-'}</td>
														<!--道路点覆盖率(%)-->
														<td style="text-align:center;" >${(ls.road_cover_rate)!'-'}</td>
														
														<!--网络掉线次数-->
														<td style="text-align:center;" >${(ls.network_count)!'-'}</td>
														<!--网络掉线率(%)-->
														<td style="text-align:center;" >${(ls.network_rate)!'-'}</td>
														<!--业务掉线次数-->
														<td style="text-align:center;" >${(ls.ping_count)!'-'}</td>
														<!--业务掉线率(%)-->
														<td style="text-align:center;" >${(ls.ping_rate)!'-'}</td>
														<!--4G下切3G次数-->
														<td style="text-align:center;" >${(ls.network_change)!'-'}</td>
														
														<td style="text-align:center;" >${(ls.lte_rsrp)!'-'}</td>
													<#if queryBean?? &&  queryBean.showOperator?? &&  queryBean.showInterval ==1>
														<#if queryBean.rsrp?? &&  queryBean.rsrp ==1>
															<!-- RSCP区间 -->
															<!--<td style="text-align:center;" >${(ls.rsrp_ratio1)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_point1)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio2)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_point2)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio3)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_point3)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio4)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_point4)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio5)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_point5)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio6)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrp_point6)!'-'}</td>-->
															<td style="text-align:center;" >${(ls.rsrp_ratio1)!'-'}%&nbsp;|&nbsp;${(ls.rsrp_point1)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio2)!'-'}%&nbsp;|&nbsp;${(ls.rsrp_point2)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio3)!'-'}%&nbsp;|&nbsp;${(ls.rsrp_point3)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio4)!'-'}%&nbsp;|&nbsp;${(ls.rsrp_point4)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio5)!'-'}%&nbsp;|&nbsp;${(ls.rsrp_point5)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrp_ratio6)!'-'}%&nbsp;|&nbsp;${(ls.rsrp_point6)!'-'}km</td>
														</#if>
													</#if>
													
														<td style="text-align:center;" >${(ls.lte_rsrq)!'-'}</td>
													<#if queryBean?? &&  queryBean.showOperator?? &&  queryBean.showInterval ==1>
														<#if queryBean.rsrq?? &&  queryBean.rsrq ==1>
															<!-- RSRQ区间 -->
															<!--<td style="text-align:center;" >${(ls.rsrq_ratio1)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_point1)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio2)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_point2)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio3)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_point3)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio4)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_point4)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio5)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_point5)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio6)!'-'}</td>
															<td style="text-align:center;" >${(ls.rsrq_point6)!'-'}</td>-->
															<td style="text-align:center;" >${(ls.rsrq_ratio1)!'-'}%&nbsp;|&nbsp;${(ls.rsrq_point1)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio2)!'-'}%&nbsp;|&nbsp;${(ls.rsrq_point2)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio3)!'-'}%&nbsp;|&nbsp;${(ls.rsrq_point3)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio4)!'-'}%&nbsp;|&nbsp;${(ls.rsrq_point4)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio5)!'-'}%&nbsp;|&nbsp;${(ls.rsrq_point5)!'-'}km</td>
															<td style="text-align:center;" >${(ls.rsrq_ratio6)!'-'}%&nbsp;|&nbsp;${(ls.rsrq_point6)!'-'}km</td>
														</#if>
													</#if>
														
														<td style="text-align:center;" >${(ls.lte_snr)!'-'}</td>
													<#if queryBean?? &&  queryBean.showOperator?? &&  queryBean.showInterval ==1>
														<#if queryBean.snr?? &&  queryBean.snr ==1>
															<!-- SNR区间 -->
															<!--<td style="text-align:center;" >${(ls.snr_ratio1)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_point1)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_ratio2)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_point2)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_ratio3)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_point3)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_ratio4)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_point4)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_ratio5)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_point5)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_ratio6)!'-'}</td>
															<td style="text-align:center;" >${(ls.snr_point6)!'-'}</td>-->
															<td style="text-align:center;" >${(ls.snr_ratio1)!'-'}%&nbsp;|&nbsp;${(ls.snr_point1)!'-'}km</td>
															<td style="text-align:center;" >${(ls.snr_ratio2)!'-'}%&nbsp;|&nbsp;${(ls.snr_point2)!'-'}km</td>
															<td style="text-align:center;" >${(ls.snr_ratio3)!'-'}%&nbsp;|&nbsp;${(ls.snr_point3)!'-'}km</td>
															<td style="text-align:center;" >${(ls.snr_ratio4)!'-'}%&nbsp;|&nbsp;${(ls.snr_point4)!'-'}km</td>
															<td style="text-align:center;" >${(ls.snr_ratio5)!'-'}%&nbsp;|&nbsp;${(ls.snr_point5)!'-'}km</td>
															<td style="text-align:center;" >${(ls.snr_ratio6)!'-'}%&nbsp;|&nbsp;${(ls.snr_point6)!'-'}km</td>
														</#if>
													</#if>
														
														<td style="text-align:center;" >${(ls.bw_link)!'-'}</td>
														<!--下行链路带宽大于12Mbps宽带比例-->
														<td style="text-align:center;" >${(ls.bw_link_rate)!'-'}</td>
													<#if queryBean?? &&  queryBean.showOperator?? &&  queryBean.showInterval ==1>
														<#if queryBean.broadband?? &&  queryBean.broadband ==1>
															<!-- 宽带区间 -->
															<!--<td style="text-align:center;" >${(ls.bw_link_ratio1)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_point1)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio2)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_point2)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio3)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_point3)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio4)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_point4)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio5)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_point5)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio6)!'-'}</td>
															<td style="text-align:center;" >${(ls.bw_link_point6)!'-'}</td>-->
															<td style="text-align:center;" >${(ls.bw_link_ratio1)!'-'}%&nbsp;|&nbsp;${(ls.bw_link_point1)!'-'}km</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio2)!'-'}%&nbsp;|&nbsp;${(ls.bw_link_point2)!'-'}km</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio3)!'-'}%&nbsp;|&nbsp;${(ls.bw_link_point3)!'-'}km</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio4)!'-'}%&nbsp;|&nbsp;${(ls.bw_link_point4)!'-'}km</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio5)!'-'}%&nbsp;|&nbsp;${(ls.bw_link_point5)!'-'}km</td>
															<td style="text-align:center;" >${(ls.bw_link_ratio6)!'-'}%&nbsp;|&nbsp;${(ls.bw_link_point6)!'-'}km</td>
														</#if>
													</#if>	
														
														<td style="text-align:center;" >${(ls.ping_avg_delay)!'-'}</td>
													<#if queryBean?? &&  queryBean.showOperator?? &&  queryBean.showInterval ==1>
														<#if queryBean.delay?? &&  queryBean.delay ==1>
															<!-- 延时区间 -->
															<!--<td style="text-align:center;" >${(ls.ping_delay_ratio1)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_point1)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio2)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_point2)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio3)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_point3)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio4)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_point4)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio5)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_point5)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio6)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_delay_point6)!'-'}</td>-->
															<td style="text-align:center;" >${(ls.ping_delay_ratio1)!'-'}%&nbsp;|&nbsp;${(ls.ping_delay_point1)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio2)!'-'}%&nbsp;|&nbsp;${(ls.ping_delay_point2)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio3)!'-'}%&nbsp;|&nbsp;${(ls.ping_delay_point3)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio4)!'-'}%&nbsp;|&nbsp;${(ls.ping_delay_point4)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio5)!'-'}%&nbsp;|&nbsp;${(ls.ping_delay_point5)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_delay_ratio6)!'-'}%&nbsp;|&nbsp;${(ls.ping_delay_point6)!'-'}km</td>
														</#if>
													</#if>
														
														<td style="text-align:center;" >${(ls.ping_lose_rate)!'-'}</td>
													<#if queryBean?? &&  queryBean.showOperator?? &&  queryBean.showInterval ==1>
														<#if queryBean.lose?? &&  queryBean.lose ==1>
															<!-- 丢包率区间 -->
															<!--<td style="text-align:center;" >${(ls.ping_lose_ratio1)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_point1)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio2)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_point2)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio3)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_point3)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio4)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_point4)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio5)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_point5)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio6)!'-'}</td>
															<td style="text-align:center;" >${(ls.ping_lose_point6)!'-'}</td>-->
															<td style="text-align:center;" >${(ls.ping_lose_ratio1)!'-'}%&nbsp;|&nbsp;${(ls.ping_lose_point1)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio2)!'-'}%&nbsp;|&nbsp;${(ls.ping_lose_point2)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio3)!'-'}%&nbsp;|&nbsp;${(ls.ping_lose_point3)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio4)!'-'}%&nbsp;|&nbsp;${(ls.ping_lose_point4)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio5)!'-'}%&nbsp;|&nbsp;${(ls.ping_lose_point5)!'-'}km</td>
															<td style="text-align:center;" >${(ls.ping_lose_ratio6)!'-'}%&nbsp;|&nbsp;${(ls.ping_lose_point6)!'-'}km</td>
														</#if>
													</#if>
													
													</tr>
												</#list>
											<#else>
												<tr>
													<td colspan="62"  align='center' style="height:37px;border-bottom:1px #dddddd  solid;">无采集数据</td>
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
	<script type="text/javascript" src="${application.getContextPath()}/js/report/comprehensive.js"></script>
	<!-- 右键菜单 -->
	<script type="text/javascript" src="${application.getContextPath()}/js/report/rightKeyMenu.js"></script>
	
	<!--道路名称下拉提示 开始 -->
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/report/area/jquery-ui.min.css">
	<script src="${application.getContextPath()}/js/report/area/jquery-ui-min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/js/report/area/ajaxRoad.js" type="text/javascript"></script>
	<!--道路名称下拉提示 开始 -->
	
	
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
<div id="rightMenu" class="rightMenu" style="display:none;">
	<div class="gis-kuangxuan-close"id="linkRow">跳转道路指标GIS展现</div>
</div>
</html>