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
	<meta content="" name="caoj" />
	<meta name="MobileOptimized" content="320">
	<!-- BEGIN GLOBAL MANDATORY STYLES -->          
	<link href="${application.getContextPath()}/scripts/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="${application.getContextPath()}/scripts/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/easyui/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/easyui/themes/icon.css">
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
	<!--select2 选择器 开始-->
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/scripts/plugins/select2/select2_metro.css" />
	<!--easyui-->
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/easyui/themes/gray/easyuirevise.css">
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/easyui/themes/icon.css">
	<style>
		#container{height:500px;width:100%}  
		#titlediv {margin-bottom:1px;border: 1px solid transparent;min-height: 50px;position: relative;}
#tooltip{  
    float:left;  
    position:absolute;  
    border:1px solid #333;  
    background:#f7f5d1;  
    padding:1px;  
    color:#333;  
    display:none;
    z-index:1;
} 
.gis-tk{width:420px;margin:0px; padding:0px;border-collapse:collapse; font-family:Arial; color:#428bca;border-top:1px  solid #428bca}
.gis-tk ul{float:left; height:auto; line-heigh:auto;background:none;padding-left:0px;}
.gis-tk ul li{background:none;float:none;text-align:left;height:24px;line-height:24px;list-style-type:none;}
.gis-tk ul li span{float:left; width:90px; display:block; color:#000}
.no_service_box{position:absolute; top:50%; left:50%;}
.no_service {width:180px; height:26px; margin:-13px 0 0 -40px;}
.no_service span{float:leftl;}
.no_service img{float:left; width:23px; margin-right:5px;}
div.messager-body.panel-body.panel-body-noborder.window-body{
		width:100% !important;
	}
	
	</style>
	<script>
		var contextPath = '${application.getContextPath()}';
	</script>
	<script src="${application.getContextPath()}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
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
		<div class="page-content">
			<div class="row">
				<div class="col-md-12">
					<div class="portlet-body">
					<span class="gis_search" onClick="showOrhide()"><div class="map_contraction"></div></span>
						<div class="navbar navbar-default" id="titlediv" role="navigation">
							<input type="hidden" id="teleAuth" value="${(Session.user.teleAuth)!}">
						<!--颜色说明-->
							<div class="legend" id="legend"><ul>
							<li class="text_name">指标:</li>
							          <li class="heat_7"><input type="checkbox" name="kpiInterv" value="7" <#if vo.kpiInterv?index_of("7")!=-1>checked ='checked'</#if>><font  style="color:#000000;font-size:18px;">${(kpis.shape)!}</font>
							              <span style="font-size:11px;"> 无服务</span>
							               </li>
							               <li class="heat_1"><input type="checkbox" name="kpiInterv" value="1" <#if vo.kpiInterv?index_of("1")!=-1>checked ='checked'</#if>><font  style="color:${(kpis.range_one_color)!};font-size:18px;">${(kpis.shape)!}</font>
							              <span style="font-size:11px;"> 
							              <#if kpis.range_one_state ?? && kpis.range_one_state==1>(${(kpis.range_one_start)!},${(kpis.range_one_end)!})</#if>
							               <#if kpis.range_one_state ?? && kpis.range_one_state==2>[${(kpis.range_one_start)!},${(kpis.range_one_end)!})]</#if>
							               <#if kpis.range_one_state ?? && kpis.range_one_state==3>(${(kpis.range_one_start)!},${(kpis.range_one_end)!}]</#if>
							               <#if kpis.range_one_state ?? && kpis.range_one_state==4>[${(kpis.range_one_start)!},${(kpis.range_one_end)!})</#if></span>
							               </li>
											<li class="heat_2"> <input type="checkbox" name="kpiInterv" value="2" <#if vo.kpiInterv?index_of("2")!=-1>checked ='checked'</#if>><font style="color:${(kpis.range_two_color)!};font-size:18px;">${(kpis.shape)!}</font>
										  <span style="font-size:11px;"> 
										  <#if kpis.range_two_state ?? && kpis.range_two_state==1>(${(kpis.range_two_start)!},${(kpis.range_two_end)!})</#if>
							               <#if kpis.range_two_state ?? && kpis.range_two_state==2>[${(kpis.range_two_start)!},${(kpis.range_two_end)!})]</#if>
							               <#if kpis.range_two_state ?? && kpis.range_two_state==3>(${(kpis.range_two_start)!},${(kpis.range_two_end)!}]</#if>
							               <#if kpis.range_two_state ?? && kpis.range_two_state==4>[${(kpis.range_two_start)!},${(kpis.range_two_end)!})</#if></span>
							               </li>
											<li class="heat_3"> <input type="checkbox" name="kpiInterv" value="3" <#if vo.kpiInterv?index_of("3")!=-1>checked ='checked'</#if>><font  style="color:${(kpis.range_three_color)!};font-size:18px;">${(kpis.shape)!}</font>
										 <span style="font-size:11px;"> 
										  <#if kpis.range_three_state ?? && kpis.range_three_state==1>(${(kpis.range_three_start)!},${(kpis.range_three_end)!})</#if>
							               <#if kpis.range_three_state ?? && kpis.range_three_state==2>[${(kpis.range_three_start)!},${(kpis.range_three_end)!})]</#if>
							               <#if kpis.range_three_state ?? && kpis.range_three_state==3>(${(kpis.range_three_start)!},${(kpis.range_three_end)!}]</#if>
							               <#if kpis.range_three_state ?? && kpis.range_three_state==4>[${(kpis.range_three_start)!},${(kpis.range_three_end)!})</#if></span>
							               </li>
											<li class="heat_4"><input type="checkbox" name="kpiInterv" value="4" <#if vo.kpiInterv?index_of("4")!=-1>checked ='checked'</#if>><font  style="color:${(kpis.range_four_color)!};font-size:18px;">${(kpis.shape)!}</font>
                                          <span style="font-size:11px;">
                                           <#if kpis.range_four_state ?? && kpis.range_four_state==1>(${(kpis.range_four_start)!},${(kpis.range_four_end)!})</#if>
							               <#if kpis.range_four_state ?? && kpis.range_four_state==2>[${(kpis.range_four_start)!},${(kpis.range_four_end)!})]</#if>
							               <#if kpis.range_four_state ?? && kpis.range_four_state==3>(${(kpis.range_four_start)!},${(kpis.range_four_end)!}]</#if>
							               <#if kpis.range_four_state ?? && kpis.range_four_state==4>[${(kpis.range_four_start)!},${(kpis.range_four_end)!})</#if></span>
							               </li>
											<li class="heat_5"><input type="checkbox" name="kpiInterv" value="5" <#if vo.kpiInterv?index_of("5")!=-1>checked ='checked'</#if>><font  style="color:${(kpis.range_five_color)!};font-size:18px;">${(kpis.shape)!}</font>
											<span style="font-size:11px;">
											<#if kpis.range_five_state ?? && kpis.range_five_state==1>(${(kpis.range_five_start)!},${(kpis.range_five_end)!})</#if>
							               <#if kpis.range_five_state ?? && kpis.range_five_state==2>[${(kpis.range_five_start)!},${(kpis.range_five_end)!})]</#if>
							               <#if kpis.range_five_state ?? && kpis.range_five_state==3>(${(kpis.range_five_start)!},${(kpis.range_five_end)!}]</#if>
							               <#if kpis.range_five_state ?? && kpis.range_five_state==4>[${(kpis.range_five_start)!},${(kpis.range_five_end)!})</#if></span>
							               </li>		
											<li class="heat_6"><input type="checkbox" name="kpiInterv" value="6" <#if vo.kpiInterv?index_of("6")!=-1>checked ='checked'</#if>><font  style="color:${(kpis.range_six_color)!};font-size:18px;">${(kpis.shape)!}</font>
	                                      <span style="font-size:11px;"> 
	                                      <#if kpis.range_six_state ?? && kpis.range_six_state==1>(${(kpis.range_six_start)!},${(kpis.range_six_end)!})</#if>
							               <#if kpis.range_six_state ?? && kpis.range_six_state==2>[${(kpis.range_six_start)!},${(kpis.range_six_end)!})]</#if>
							               <#if kpis.range_six_state ?? && kpis.range_six_state==3>(${(kpis.range_six_start)!},${(kpis.range_six_end)!}]</#if>
							               <#if kpis.range_six_state ?? && kpis.range_six_state==4>[${(kpis.range_six_start)!},${(kpis.range_six_end)!})</#if></span>
							               </li>
											</ul>
						</div>
								<!--颜色说明-->
							<div class="legend warnclass" id="legend2" style="<#if vo.warn ?? && (vo.warn=='-1'||vo.warn=='11')>display:none</#if>"><ul>
							<li class="text_name">事件:</li>
							                    <li class="heat_1"><font  style="color:${(kpisWarn.range_one_color)!};font-size:15px;">★</font>
							              <span style="font-size:11px;"> 
							              <#if kpisWarn ?? && kpisWarn.range_one_state==1>(${(kpisWarn.range_one_start)!},${(kpisWarn.range_one_end)!})</#if>
							               <#if kpisWarn ?? && kpisWarn.range_one_state==2>[${(kpisWarn.range_one_start)!},${(kpisWarn.range_one_end)!})]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_one_state==3>(${(kpisWarn.range_one_start)!},${(kpisWarn.range_one_end)!}]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_one_state==4>[${(kpisWarn.range_one_start)!},${(kpisWarn.range_one_end)!})</#if></span>
							               </li>
											<li class="heat_2"> <font style="color:${(kpisWarn.range_two_color)!};font-size:15px;">★</font>
										  <span style="font-size:11px;"> 
										  <#if kpisWarn ?? && kpisWarn.range_two_state==1>(${(kpisWarn.range_two_start)!},${(kpisWarn.range_two_end)!})</#if>
							               <#if kpisWarn ?? && kpisWarn.range_two_state==2>[${(kpisWarn.range_two_start)!},${(kpisWarn.range_two_end)!})]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_two_state==3>(${(kpisWarn.range_two_start)!},${(kpisWarn.range_two_end)!}]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_two_state==4>[${(kpisWarn.range_two_start)!},${(kpisWarn.range_two_end)!})</#if></span>
							               </li>
											<li class="heat_3"><font  style="color:${(kpisWarn.range_three_color)!};font-size:15px;">★</font>
										 <span style="font-size:11px;"> 
										  <#if kpisWarn ?? && kpisWarn.range_three_state==1>(${(kpisWarn.range_three_start)!},${(kpisWarn.range_three_end)!})</#if>
							               <#if kpisWarn ?? && kpisWarn.range_three_state==2>[${(kpisWarn.range_three_start)!},${(kpisWarn.range_three_end)!})]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_three_state==3>(${(kpisWarn.range_three_start)!},${(kpisWarn.range_three_end)!}]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_three_state==4>[${(kpisWarn.range_three_start)!},${(kpisWarn.range_three_end)!})</#if></span>
							               </li>
											<li class="heat_4"><font  style="color:${(kpisWarn.range_four_color)!};font-size:15px;">★</font>
                                          <span style="font-size:11px;">
                                           <#if kpisWarn ?? && kpisWarn.range_four_state==1>(${(kpisWarn.range_four_start)!},${(kpisWarn.range_four_end)!})</#if>
							               <#if kpisWarn ?? && kpisWarn.range_four_state==2>[${(kpisWarn.range_four_start)!},${(kpisWarn.range_four_end)!})]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_four_state==3>(${(kpisWarn.range_four_start)!},${(kpisWarn.range_four_end)!}]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_four_state==4>[${(kpisWarn.range_four_start)!},${(kpisWarn.range_four_end)!})</#if></span>
							               </li>
											<li class="heat_5"><font  style="color:${(kpisWarn.range_five_color)!};font-size:15px;">★</font>
											<span style="font-size:11px;">
											<#if kpisWarn ?? && kpisWarn.range_five_state==1>(${(kpisWarn.range_five_start)!},${(kpisWarn.range_five_end)!})</#if>
							               <#if kpisWarn ?? && kpisWarn.range_five_state==2>[${(kpisWarn.range_five_start)!},${(kpisWarn.range_five_end)!})]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_five_state==3>(${(kpisWarn.range_five_start)!},${(kpisWarn.range_five_end)!}]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_five_state==4>[${(kpisWarn.range_five_start)!},${(kpisWarn.range_five_end)!})</#if></span>
							               </li>		
											<li class="heat_6"><font  style="color:${(kpisWarn.range_six_color)!};font-size:15px;">★</font>
	                                      <span style="font-size:11px;"> 
	                                      <#if kpisWarn ?? && kpisWarn.range_six_state==1>(${(kpisWarn.range_six_start)!},${(kpisWarn.range_six_end)!})</#if>
							               <#if kpisWarn ?? && kpisWarn.range_six_state==2>[${(kpisWarn.range_six_start)!},${(kpisWarn.range_six_end)!})]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_six_state==3>(${(kpisWarn.range_six_start)!},${(kpisWarn.range_six_end)!}]</#if>
							               <#if kpisWarn ?? && kpisWarn.range_six_state==4>[${(kpisWarn.range_six_start)!},${(kpisWarn.range_six_end)!})</#if></span>
							               </li>
									</ul>
						</div>
							<div  class="navbar-form navbar-left breadcrumb" >
								<!--时间范围控件       开始-->
								<div class="form-group" >
									<label class="control-label">时间:</label>
									<div class="form-group" id="reportrange" style="margin-right;8px;">
										<input type="text" name="queryTime" value="${(queryTime)!}" class="form-control" readOnly>	
																	
									</div>
								</div>
								<div class="form-group" style="margin-left:8px;">
									<label class="control-label" >运营商:</label>
									<select class="form-control input-small select2me kpi_v" style="width: 100px !important;" name="operator" id="operator">
										<#if operators ?? && operators?size &gt; 0>
											<#list operators as p>
												<option value="${(p.id)!}" <#if vo.operator?? && vo.operator+'' == p.id>selected = "true" </#if>>${(p.text)!}</option>
											</#list>
										</#if>
									</select>	
								</div>
								<div class="form-group" style="margin-left:8px;">
									<label class="control-label">指标类型:</label>
									<select class="form-control input-small select2me kpi_v" style="width: 100px !important;" name="kpiType" id="kpiType">
										<option value="1" <#if vo.kpiType?? && vo.kpiType == 1>selected = "true" </#if>>RSRP</option>
										<option value="2" <#if vo.kpiType?? && vo.kpiType == 2>selected = "true" </#if>>SNR</option>
										<option value="3" <#if vo.kpiType?? && vo.kpiType == 3>selected = "true" </#if>>RSRQ</option>
										<option value="4" <#if vo.kpiType?? && vo.kpiType == 4>selected = "true" </#if>>下行链路带宽</option>
										<option value="5" <#if vo.kpiType?? && vo.kpiType == 5>selected = "true" </#if>>时延</option>
										<option value="6" <#if vo.kpiType?? && vo.kpiType == 6>selected = "true" </#if>>丢包</option>
									</select>
								</div>
								<div class="form-group" style="margin-left:10px;">
									<label class="control-label">事件:</label>
									<select class="form-control input-small select2me kpi_v" style="width: 120px !important;" name="warn" id="warn">
										<option value="-1" <#if vo.warn ?? && vo.warn=='-1'>selected</#if>>无事件</option>	
										<option value="9" <#if vo.warn ?? && vo.warn=='9'>selected</#if>>网络掉线(%)</option>
										<option value="10" <#if vo.warn ?? && vo.warn=='10'>selected</#if>>业务掉线(%)</option>
										<option value="11" <#if vo.warn ?? && vo.warn=='11'>selected</#if>>4G切换3G(次)</option>
									</select>
									&nbsp;
									<select class="form-control input-small select2me" style="width: 50px !important;margin-left:-5px;" name="warnSymbol" id="warnSymbol">
										<option value="1" <#if vo.warnSymbol?? && vo.warnSymbol == 1>selected = "true" </#if>>></option>
										<option value="2" <#if vo.warnSymbol?? && vo.warnSymbol == 2>selected = "true" </#if>>>=</option>
										<option value="3" <#if vo.warnSymbol?? && vo.warnSymbol == 3>selected = "true" </#if>>=</option>
										<option value="4" <#if vo.warnSymbol?? && vo.warnSymbol == 4>selected = "true" </#if>><</option>
										<option value="5" <#if vo.warnSymbol?? && vo.warnSymbol == 5>selected = "true" </#if>><=</option>
									</select>   
									&nbsp;                                   
									<input name="warnValue" type="text" class="form-control " style="width:60px;margin-left:-5px;" id="warnValue" maxlength="3" value="${(vo.warnValue)!}" onkeyup="value=value.replace(/[^0-9]/g,'')" placeholder="请输入过滤值">
						     	</div>
								</br>
							<!--区域  开始-->
								<div class="form-group">
									<label class="control-label">区域:</label>
									<select class="form-control input-small select2me" style="width:110px !important;" name="province" id="province">
										<#if countrys ?? && countrys?size &gt; 0>
											<#list countrys as p>
												<option value="${(p.id)!}" <#if vo?? && p.id == vo.province>selected</#if>>${(p.text)!}</option>
											</#list>
										</#if>
									</select>
									&nbsp;
									<select class="fform-control input-small select2me" <#if user?? && user.district?? && user.district=='-1'>hasAreaAll="1"</#if> style="width:80px !important;margin-left:-5px;" name="district" id="district">
										<#if user?? && user.district?? && user.district=='-1'><option value="-1"  <#if vo?? && '-1' == vo.district>selected</#if>>全部</option></#if>
										<#if citys ?? && citys?size &gt; 0>
											<#list citys as p>
												<option value="${(p.id)!}" <#if vo?? && p.id == vo.district>selected</#if>>${(p.text)!}</option>
											</#list>
										</#if>
									</select>
									&nbsp;
									<select class="form-control input-small select2me" <#if user?? && user.district?? && user.district=='-1'>hasAreaAll="1"</#if> style="width:110px;margin-left:-5px;" name="area" id="area">
										<#if user?? && user.county?? && user.county=='-1'><option value="-1" <#if vo?? && '-1' == vo.area>selected</#if>>全部</option></#if>
										<#if areas ?? && areas?size &gt; 0>
											<#list areas as p>
												<option value="${(p.id)!}" <#if vo?? && p.id == vo.area>selected</#if>>${(p.text)!}</option>
											</#list>
										</#if>
									</select>
								</div>
								<!-- 区域 结束 -->	
								
								<!--道路  开始-->
								<div class="form-group" style="margin-left:8px;">
									<label class="control-label">道路类型:</label>
									<select class="form-control input-small select2me" style="width: 100px !important;" name="roadType" id="roadType">
										<option value="-1" <#if vo.roadType ?? && vo.roadType==-1>selected</#if>>全部</>
										<option value="1" <#if vo.roadType ?? && vo.roadType==1>selected</#if>>道路</>
										<option value="2" <#if vo.roadType ?? && vo.roadType==2>selected</#if>>桥梁</>
										<option value="3" <#if vo.roadType ?? && vo.roadType==3>selected</#if>>隧道</>
									</select>
								</div>
								<div class="form-group" style="margin-left:8px;">
									<label class="control-label">等级:</label>
									<select class="fform-control input-small select2me" style="width:80px !important;" name="roadLevel" id="roadLevel">
										       <option value="-1" <#if vo.roadLevel ?? && vo.roadLevel==-1>selected</#if>>全部</option>
												<option value="1" <#if vo.roadLevel ?? && vo.roadLevel==1>selected</#if>>主干路</option>
												<option value="2" <#if vo.roadLevel ?? && vo.roadLevel==2>selected</#if>>次干路</option>
												<option value="3" <#if vo.roadLevel ?? && vo.roadLevel==3>selected</#if>>高速公路</option>
												<option value="4" <#if vo.roadLevel ?? && vo.roadLevel==4>selected</#if>>国道</option>
												<option value="5" <#if vo.roadLevel ?? && vo.roadLevel==5>selected</#if>>省道</option>
												<option value="6" <#if vo.roadLevel ?? && vo.roadLevel==6>selected</#if>>县道</option>
												<option value="7" <#if vo.roadLevel ?? && vo.roadLevel==7>selected</#if>>乡村道路</option>
									</select>
								</div>
								<div class="form-group" style="margin-left:12px;">
									<label class="control-label" >名称:</label>
									<!--<select class="form-control input-small select2me" style="width:140px;" hasAll="1" name="roadId" id="roadId">
									<option value="-1" <#if vo.roadLevel ?? && vo.roadLevel==-1>selected</#if>>全部</option>
										<#if roads ?? && roads?size &gt; 0>
											<#list roads as p>
												<option value="${(p.id)!}" <#if vo??&&vo.roadId?? && p.id == vo.roadId>selected</#if>>${(p.text)!}</option>
											</#list>
										</#if>
									</select>-->
									<input class="form-control autocomplete_road" name="roadName" nonull="0" value="${(vo.roadName)!''}" id="roadName" placeholder="全部" style="width:160px !important;" onclick="$('#roadName').autocomplete('search', document.getElementById('roadName') );"/>
									<input type="hidden" name="roadName_v" id="roadName_v" value="${(vo.roadName)!''}"/>
									<input type="hidden" name="roadId" id="roadId" value="${(vo.roadId)!'-1'}"/>
								</div>
								<!--道路 结束-->	
								<button class="btn blue" id="btn_query" style="margin-top:-6px;margin-left:8px;padding:7px 14px;">查询</button>
								<button class="btn green" onclick="excelClick('')" id="btn_export" style="margin-top:-6px;margin-left:5px;padding:7px 14px;">导出</button>
							</div>
							
						</div>	
						<div id="tooltip"></div>
						<input id="uuid" style="display:none" value=""/>
						<input id="duibiuuid" style="display:none" value=""/>
						<!--
                        <div class="gis-mapgj">
							  <div id="h_4" class="gis-mapgj-diejia"  title="轨迹叠加"><img src="${application.getContextPath()}/images/drawing1.png"></div>
							  <div id="m_4" class="gis-mapgj-diejia-hover"  style="display:none;" title="删除叠加轨迹"><img src="${application.getContextPath()}/images/drawing1.png"></div>			
							  <div id="m_1" class="gis-mapgj-marquee" title="框选"><img src="${application.getContextPath()}/images/drawing1.png"></div>
							  <div id="h_1" class="gis-mapgj-marquee-hover"  style="display:none;" title="框选"><img src="${application.getContextPath()}/images/drawing1.png"></div>
		  		              <div id="m_2" class="gis-mapgj-circle" title="不规则选择"><img src="${application.getContextPath()}/images/drawing1.png"></div>
							  <div id="h_2" class="gis-mapgj-circle-hover"  style="display:none;" title="不规则选择"><img src="${application.getContextPath()}/images/drawing1.png"></div>
							  <div id="m_3" class="gis-mapgj-hand" style="display:none;" title="拖拽"><img src="${application.getContextPath()}/images/drawing1.png"></div>
							  <div id="h_3" class="gis-mapgj-hand-hover" title="拖拽"><img src="${application.getContextPath()}/images/drawing1.png"></div>	
		                </div>
		                -->
		                <div class="map_float">
									<div class="map_float_left">
									 <ul>
									<li id="h_3" class="left_img1" title="框选"><span>|</span></li>
									<li id="h_2" class="left_img2" title="不规则选择"><span>|</span></li>
									<li id="h_1" class="left_img3" title="拖拽"><span>|</span></li>
									<li id="h_4" class="left_img4" choose="false" title="轨迹叠加"><span>&nbsp;</span></li>
									 </ul>
									</div>
					</div>		
						<!--地图-->
						<div id="container"></div> 
					</div>	
				</div>	
			</div>		
		</div>
	</div>
	
<!-- BEGIN FOOTER -->
	<!-- END FOOTER -->
		<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- 开始    核心插件 -->   
	<!--[if lt IE 9]>
	<script src="${application.getContextPath()}/scripts/plugins/respond.min.js"></script>
	<script src="${application.getContextPath()}/scripts/plugins/excanvas.min.js"></script> 
	<![endif]-->   
	<!-- 结束    核心插件 -->
	<!-- 开始    日期范围控件脚本-->
	
    
	<script src="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/moment.min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/daterangepicker_amend.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/scripts/form-dateRanges_amend.js" type="text/javascript"></script>  
	<script type="text/javascript" src="${application.getContextPath()}/js/ejs_production.js"></script>
	<!--页面工具脚本-->
	   <script src="${application.getContextPath()}/js/report/areaRoad.js" type="text/javascript"></script>
	   <script src="${application.getContextPath()}/js/mapManager/map.js" type="text/javascript"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=byh1tKOFFXcgzqTYGkj5mWWp"></script>
<script type="text/javascript" src="${application.getContextPath()}/js/mapManager/DrawingManager_min.js"></script>
<script type="text/javascript" src="${application.getContextPath()}/js/mapManager/AreaRestriction_min.js"></script>
<script src="${application.getContextPath()}/js/mapManager/MarkerClusterer.min.js" type="text/javascript"></script>
<script src="${application.getContextPath()}/js/mapManager/TextIconOverlay_min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/js/mapManager/mapStyle.js" type="text/javascript"></script>
		<script src="${application.getContextPath()}/js/mapManager/queryShow.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/js/mapManager/roadMap/draws.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/js/mapManager/roadMap/grid.js" type="text/javascript"></script>
    <script src="${application.getContextPath()}/js/mapManager/roadMap/data.js" type="text/javascript"></script>
    <!-- 地图info分页 -->
    <script src="${application.getContextPath()}/js/mapManager/roadMap/point_info_page.js" type="text/javascript"></script>
	<!--<script type="text/javascript" src="${application.getContextPath()}/js/utils/netTypeUtils.js"></script>
	自定义js 开始
	<script src="${application.getContextPath()}/js/echartsTools.js" type="text/javascript"></script>

	<script src="${application.getContextPath()}/js/mapManager/pline.js" type="text/javascript"></script>
	
	<script src="${application.getContextPath()}/js/mapManager/mapReport.js" type="text/javascript"></script>-->
	
	<!--道路名称下拉提示 开始 -->
	<link rel="stylesheet" type="text/css" href="${application.getContextPath()}/js/report/area/jquery-ui.min.css">
	<script src="${application.getContextPath()}/js/report/area/jquery-ui-min.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/js/report/area/ajaxRoad.js" type="text/javascript"></script>
	<!--道路名称下拉提示 开始 -->
	<script>
	<!--顶级区域-->
	 var oneregionname = <#if oneregionname?? && oneregionname!= ''>'${oneregionname!}'<#else>''</#if>;
	 
	 var tiaozhuan = <#if tiaozhuan?? && tiaozhuan!= ''>'${tiaozhuan!}'<#else>''</#if>;
	 
	jQuery(document).ready(function() {
			//设置菜单样式
		//	setMenuCss(["mapManage"]);
			DateRanges.init('reportrange');	 
			//$(".page-content").css('min-height',document.body.clientHeight-5);  
		});
		$(function(){
			$("#warn").change(function(){
				var value = $("#warn").val();
				if(value!='-1'){
					$("#warnValue").val(0);
				}
			});
		});
		
		var teleAuth = document.getElementById("teleAuth").value;
	</script>
 
	</body>
<!-- END BODY -->
</html>