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
<body class="page-header-fixed" oncontextmenu="return false" ><!-- 禁用自身右键菜单 -->
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
										<!--区域  开始-->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">区域:</label>
											<select class="form-control input-small select2me" <#if user?? && user.province?? && user.province=='-1'>hasAreaAll="1"</#if> style="width:80px !important;" name="province" id="province">
												<#if countrys ?? && countrys?size &gt; 0>
													<#list countrys as p>
														<option value="${(p.id)!}" <#if queryBean?? && p.id == queryBean.province>selected="true"</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
											&nbsp;
											<select class="fform-control input-small select2me" <#if user?? && user.district?? && user.district=='-1'>hasAreaAll="1"</#if> style="width:80px !important;margin-left:-5px;" name="district" id="district">
												<#if user?? && user.district?? && user.district=='-1'><option value="-1"  <#if queryBean?? && '-1' == queryBean.district>selected="true"</#if>>全部</option></#if>
												<#if citys ?? && citys?size &gt; 0>
													<#list citys as p>
														<option value="${(p.id)!}" <#if queryBean?? && p.id == queryBean.district>selected="true"</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
											&nbsp;
											<select class="form-control input-small select2me" <#if user?? && user.county?? && user.county=='-1'>hasAreaAll="1"</#if> style="width:100px !important;margin-left:-5px;" name="area" id="area">
												<#if user?? && user.county?? && user.county=='-1'><option value="-1" <#if queryBean?? && '-1' == queryBean.area>selected="true"</#if>>全部</option></#if>
												<#if areas ?? && areas?size &gt; 0>
													<#list areas as p>
														<option value="${(p.id)!}" <#if queryBean?? && p.id == queryBean.area>selected="true"</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
										</div>
										<!-- 区域 结束 -->
										<!-- 对比指标 开始 -->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">对比指标:</label>
											<select class="form-control input-small select2me" style="width:100px !important;" name="kpiType" id="kpiType">
												<option value="8" <#if queryBean.kpiType?? && queryBean.kpiType == 8>selected</#if>>覆盖率</option>
												<option value="1" <#if queryBean.kpiType?? && queryBean.kpiType == 1>selected</#if>>RSRP</option>
												<option value="2" <#if queryBean.kpiType?? && queryBean.kpiType == 2>selected</#if>>SNR</option>
												<option value="4" <#if queryBean.kpiType?? && queryBean.kpiType == 4>selected</#if>>下行链路带宽</option>
												<option value="9" <#if queryBean.kpiType?? && queryBean.kpiType == 9>selected</#if>>网络掉线率</option>
												<option value="10" <#if queryBean.kpiType?? && queryBean.kpiType == 10>selected</#if>>业务掉线率</option>
											</select>
										</div>
										<!-- 对比指标 结束 -->
									
										<!--道路  开始-->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">道路类型:</label>
											<select class="form-control input-small select2me" style="width: 80px !important;" name="roadType" id="roadType">
												<option value="-1" <#if queryBean.roadType ?? && queryBean.roadType==-1>selected="true"</#if>>全部</option>
												<option value="1" <#if queryBean.roadType ?? && queryBean.roadType==1>selected="true"</#if>>道路</option>
												<option value="2" <#if queryBean.roadType ?? && queryBean.roadType==2>selected="true"</#if>>桥梁</option>
												<option value="3" <#if queryBean.roadType ?? && queryBean.roadType==3>selected="true"</#if>>隧道</option>
											</select>
											<label class="control-label"style="margin-left:8px;">等级:</label>
											<select class="fform-control input-small select2me" style="width:80px !important;" name="roadLevel" id="roadLevel">
												<option value="-1" <#if queryBean.roadLevel ?? && queryBean.roadLevel==-1>selected</#if>>全部</option>
												<option value="1" <#if queryBean.roadLevel ?? && queryBean.roadLevel==1>selected</#if>>主干路</option>
												<option value="2" <#if queryBean.roadLevel ?? && queryBean.roadLevel==2>selected</#if>>次干路</option>
												<option value="3" <#if queryBean.roadLevel ?? && queryBean.roadLevel==3>selected</#if>>高速公路</option>
												<option value="4" <#if queryBean.roadLevel ?? && queryBean.roadLevel==4>selected</#if>>国道</option>
												<option value="5" <#if queryBean.roadLevel ?? && queryBean.roadLevel==5>selected</#if>>省道</option>
												<option value="6" <#if queryBean.roadLevel ?? && queryBean.roadLevel==6>selected</#if>>县道</option>
												<option value="7" <#if queryBean.roadLevel ?? && queryBean.roadLevel==7>selected</#if>>乡村道路</option>
											</select>
										</div>
										<!--道路 结束-->
										<br/>
										<!-- 运营商 开始 -->
										<div class="form-group">
											<label class="control-label">运&nbsp;营&nbsp;商:&nbsp;</label>
											<select class="form-control input-small select2me" style="width:80px !important;" name="operator" id="operator">
												<#if operators ?? && operators?size &gt; 0>
													<#list operators as p>
														<#if user.operator ?? && user.operator !=p.id><option value="${(p.id)!}" <#if queryBean.operator?? && p.id == queryBean.operator>selected</#if>>${(p.text)!}</option></#if>
													</#list>
												</#if>
											</select>
										</div>
										<!-- 运营商 结束 -->
										<!--差距门限值 开始-->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">差距门限值&gt;</label>
											<input class="form-control" type="text" id="threshold" name="threshold" style="width:80px !important;" onkeyup="value=value.replace(/[^0-9\-\.]/g,'')" value="${(queryBean.threshold)!}"/>
										</div>
										<!--差距门限值 结束-->
										<!-- 对比方式 开始 -->
										<div class="form-group"style="margin-left:8px;">
											<label class="control-label">对比方式:</label>
											<select class="form-control input-small select2me" style="width:80px !important;" name="compare" id="compare">
												<option value="2" <#if queryBean.compare ?? && queryBean.compare==2>selected</#if>>劣势对比</option>
												<option value="1" <#if queryBean.compare ?? && queryBean.compare==1>selected</#if>>优势对比</option>
											</select>
										</div>
										<!-- 对比方式 结束 -->
										<button class="btn green" id="leadExcel" style="float:right;width:62px;">导出</button>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<button class="btn blue" id="querybtn" style="float:right;margin:0px 10px 0 0;width:62px;">查询</button>
									</form>
								</div>
								<!-- 查询条件 -->
								<form id="pageForm" name="pageForm" action="${application.getContextPath()}/roadQuality/query" method="post" >
									
									<input type="hidden" name="totalPage" id="totalPage" value="${(page.totalPage)!1}" />
									<input type="hidden" name="pageSize" id="pageSize" value="${(page.pageSize)!10}" />
									<input type="hidden" name="pageIndex" id="pageIndex" value="${(page.pageIndex)!1}" />
									<!--UUID-->
									<input type="hidden" id="uuid_page" name="uuid" value="${(queryBean.uuid)!}"/>
									<!--时间-->
									<input type="hidden" id="queryTime_page" name="queryTime" value="${(queryBean.queryTime)!}"/>
									<!--运营商-->
									<input type="hidden" id="operator_page" name="operator" value="${(queryBean.operator)!}"/>
									<!--指标类型-->
									<input type="hidden" id="kpiType_page" name="kpiType" value="${(queryBean.kpiType)!}"/>
									<!--区域-->
									<input type="hidden" id="province_page" name="province" value="${(queryBean.province)!}"/>
									<input type="hidden" id="district_page" name="district" value="${(queryBean.district)!}"/>
									<input type="hidden" id="area_page" name="area" value="${(queryBean.area)!}"/>
									<!--道路-->
									<input type="hidden" id="roadType_page" name="roadType" value="${(queryBean.roadType)!}"/>
									<input type="hidden" id="roadLevel_page" name="roadLevel" value="${(queryBean.roadLevel)!}"/>
									<!-- 差距门限值 -->
									<input type="hidden" id="threshold_page" name="threshold" value="${(queryBean.threshold)!}"/>
									<!-- 对比方式 -->
									<input type="hidden" id="compare_page" name="compare" value="${(queryBean.compare)!}"/>
									<!-- 是否查询过 -->
									<input type="hidden" id="flag_query" name="flag" value="${(flag)!}"/>
								</form>
								<!--表单开始   开始-->
								<!-- 跳转地图查询条件 开始 -->
								<form id="linkMap" name="linkMap" action="${application.getContextPath()}/roadMap/roadMap?op_menu=5" method="post" >
									<input type="hidden" id="hasTime" value="${(hasTime)!}"/>
									<!--区域-->
									<input type="hidden" id="province_map" name="province" value="${(queryBean.province)!}"/>
									<input type="hidden" id="district_map" name="district"/>
									<input type="hidden" id="area_map" name="area"/>
									<!--道路-->
									<input type="hidden" id="roadType_map" name="roadType"/>
									<input type="hidden" id="roadLevel_map" name="roadLevel"/>
									<input type="hidden" id="roadId_map" name="roadId"/>
									<!--时间-->
									<input type="hidden" id="starttime_map" name="starttime" value="${(starttime)!}"/>
									<input type="hidden" id="endtime_map" name="endtime" value="${(endtime)!}"/>
									<!--运营商-->
									<input type="hidden" id="userId_map" name="userId" value="${(Session.user.userid)!}"/>
									<!--运营商-->
									<input type="hidden" id="operator_map" name="operator" value="${(Session.user.operator)!}"/>
									<!--指标类型-->
									<#if queryBean.kpiType?? && (queryBean.kpiType ==1 ||queryBean.kpiType ==2 ||queryBean.kpiType ==4 ||queryBean.kpiType ==8)>
										<input type="hidden" id="kpiType_map" name="kpiType" value="${(queryBean.kpiType)!}"/>
									<#else>
										<input type="hidden" id="kpiType_map" name="kpiType" value="1"/>
									</#if>
									<#if queryBean.kpiType?? && (queryBean.kpiType ==9 ||queryBean.kpiType ==10)>
										<input type="hidden" id="event_map" name="event" value="${(queryBean.kpiType)!}"/>
									<#else>
										<input type="hidden" id="event_map" name="event" value="-1"/>
									</#if>
									<!-- 指标区间 -->
									<input type="hidden" id="kpiInterv_map" name="kpiInterv" value="1,2,3,4,5,6"/>
									<!-- 事件默认-1 -->
									<input type="hidden" id="warn_map" name="warn" value="-1"/>
								</form>
								<!-- 跳转地图查询条件 结束 -->
								<div class="table-responsive table-scrollable">
									<table class="table table-striped table-hover table-bordered" id="sample_editable_1">
										<!--表单title 开始-->
										<thead>
											<tr style="background-color:#EAEAEA;">
													<th style="text-align:center;" >市名</th>
													<th style="text-align:center;" >区县</th>
													<th style="text-align:center;" >道路名称</th>
													<th style="text-align:center;" >${(myOperator)!'-'} - ${(kpiName)!'-'}</th>
													<th style="text-align:center;" >${(otherOperator)!'-'} - ${(kpiName)!'-'}</th>
													<th style="text-align:center;" >对比指标差值</th>
													<th style="text-align:center; <#if queryBean.kpiType ?? && queryBean.kpiType == 1>display:none;</#if>" >RSRP(dBm)</th>
													<th style="text-align:center; <#if queryBean.kpiType ?? && queryBean.kpiType == 8>display:none;</#if>" >覆盖率(%)</th>
													<th style="text-align:center; <#if queryBean.kpiType ?? && queryBean.kpiType == 2>display:none;</#if>" >SNR(dB)</th>
													<th style="text-align:center; <#if queryBean.kpiType ?? && queryBean.kpiType == 4>display:none;</#if>" >下行链路带宽(Mbps)</th>
													<th style="text-align:center; <#if queryBean.kpiType ?? && queryBean.kpiType == 9>display:none;</#if>" >网络掉线率(%)</th>
													<th style="text-align:center; <#if queryBean.kpiType ?? && queryBean.kpiType == 10>display:none;</#if>" >业务掉线率(%)</th>
													<th style="text-align:center;" >4G切3G(次数)</th>
											</tr>
										</thead>
										<!--表单title 结束-->
										<!-- 表单内容 开始 -->
										<tbody>
											<#if page?? && page.list?? &&  page.list?size &gt; 0>
												<#list page.list as ls>
													<tr name="linkToMap" style="height:37px;cursor:pointer;<#if ls_index==page.list?size-1>border-bottom:1px #dddddd  solid;</#if>"
													dis="${(ls.parentId)!'-1'}" area="${(ls.regionCode)!'-1'}" rt="${(ls.roadType)!'-1'}" rl="${(ls.roadLevel)!'-1'}" ri="${(ls.roadId)!'-1'}"
													title="双击跳转道路指标GIS展现" id="linkCol_${(ls_index)}">
															<td style="text-align:center;" >${(ls.cityName)!'-'}</td>
															<td style="text-align:center;" >${(ls.countryName)!'-'}</td>
															<td style="text-align:center;" >${(ls.roadName)!'-'}</td>
															<td style="text-align:center;" >${(ls.selfValue)!'-'}</td>
															<td style="text-align:center;" >${(ls.compValue)!'-'}</td>
															<td style="text-align:center;" >${(ls.diffValue)!'-'}</td>
															<td style="text-align:center;<#if queryBean.kpiType ?? && queryBean.kpiType == 1>display:none;</#if>" >${(ls.lteRsrp)!'-'}</td>
															<td style="text-align:center;<#if queryBean.kpiType ?? && queryBean.kpiType == 8>display:none;</#if>" >${(ls.wirelessCoverage)!'-'}</td>
															<td style="text-align:center;<#if queryBean.kpiType ?? && queryBean.kpiType == 2>display:none;</#if>" >${(ls.lteSnr)!'-'}</td>
															<td style="text-align:center;<#if queryBean.kpiType ?? && queryBean.kpiType == 4>display:none;</#if>" >${(ls.bwLink)!'-'}</td>
															<td style="text-align:center;<#if queryBean.kpiType ?? && queryBean.kpiType == 9>display:none;</#if>" >${(ls.networkRate)!'-'}</td>
															<td style="text-align:center;<#if queryBean.kpiType ?? && queryBean.kpiType == 10>display:none;</#if>" >${(ls.pingRate)!'-'}</td>
															<td style="text-align:center;" >${(ls.lteToWcdma)!'-'}</td>
													</tr>
												</#list>
											<#else>
												<tr>
													<td colspan="12"  align='center' style="height:37px;border-bottom:1px #dddddd  solid;">无采集数据</td>
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
	<script type="text/javascript" src="${application.getContextPath()}/js/report/area.js"></script>
	<!--页面工具脚本-->
	<script src="${application.getContextPath()}/js/report/rptHelper.js" type="text/javascript"></script>
	<!--分页-->
	<script src="${application.getContextPath()}/js/ejs_production.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/scripts/table-pages.js" type="text/javascript"></script>
	<!--复选框 -->
	<script src="${application.getContextPath()}/scripts/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
	<script type="text/javascript" src="${application.getContextPath()}/js/report/roadQuality.js"></script>
	<!--验证--> 	
	<script type="text/javascript" src="${application.getContextPath()}/js/report/areaValidater.js"></script>
	<!-- 右键菜单 -->
	<script type="text/javascript" src="${application.getContextPath()}/js/report/rightKeyMenu.js"></script>
	<script type="text/javascript">
	jQuery(document).ready(function() {
			//设置日期控件
			DateRanges.init('reportrange');
		    var winHeight = window.screen.height;
			var scHeight = document.body.scrollTop;
			var winHeight = window.screen.height;
			$('.page-content').css('min-height',winHeight);
			thisValidate.init();
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
<!-- END BODY -->
</html>