<!DOCTYPE html>
<html lang="en" class="no-js">
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
	<!-- echart 图表 开始 -->
	<link href="${application.getContextPath()}/scripts/plugins/echarts/www/css/echartsHome.css" rel="stylesheet">
	<!-- 分页样式 -->
	<link href="${application.getContextPath()}/skins/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<!-- 信号采样点外框样式 -->
	<style type= "text/css" >
		.boxcontent_1 {
			background: #27a9e3 none repeat scroll 0 0;
		}
		.boxcontent_2 {
			background: #28b779 none repeat scroll 0 0;
		}
		.boxcontent_3 {
			background: #c64eae none repeat scroll 0 0;
		}
		.boxcontent_4 {
			background: #ffb848 none repeat scroll 0 0;
		}
		.boxcontent_5 {
			background: #5369cb none repeat scroll 0 0;
		}
		.boxcontent_6 {
			background: #cd7f32 none repeat scroll 0 0;
		}
		.boxcontent_7 {
			background: #ff1cae none repeat scroll 0 0;
		}
		.boxcontent_8 {
			background: #cc3299 none repeat scroll 0 0;
		}
		.boxcontent {
			border-radius: 5px;
			float: left;
			height: 80px;
			margin-left: 1%;
			margin-top: 20px;
			margin-bottom: 20px;
			text-align: center;
			vertical-align: middle;
			width: 11%;
			border-radius:25!important;
		}
		
		.box_table {
			display:flex;
			justify-content:center;
			align-items:center;
			margin-top:20px;
			font-size:13px;
			color:#fff;
		}
	</style>
	<!-- echart 图表 结束 -->
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/jquery-1.10.2.min.js"></script>
</head>
<body class="page-header-fixed">
	<!-- 上框 开始 -->   
	<#include "../index/indexTop.ftl" />
	<!-- 上框 结束 -->
	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- 左边菜单 开始 -->
		<#include "../index/indexMenu.ftl" />
		<!-- 左边菜单 结束 -->
		<div class="page-content">
			<div class="row">
				<div class="col-md-12">
					<!--无线宽带覆盖率-->
					<div style="width: 48%;float: left;margin-top:20px;">
						<div id="wirelessCover" class="main" style="width: 100%;float: left"></div>
						<div id="wirelessCoverButton" style="width: 97%;float: left">
							<div class="clearfix"></div>
						</div>
					</div>
					<!--下行链路宽带覆盖率网络掉线率-->
					<div style="margin-left: 20px; width: 48%;float: left;margin-top:20px;">
						<div id="broadbandCover" class="main" style="width: 100%;float: left"></div>
						<div id="broadbandCoverButton" style="width: 97%;float: left">
							<div class="clearfix"></div>
						</div>
					</div>
					<!--网络掉线率-->
					<div style="width: 48%;float: left;margin-top:20px;">
						<div id="networkRate" class="main" style="width: 100%;float: left"></div>
						<div id="networkRateButton" style="width: 97%;float: left">
							<div class="clearfix"></div>
						</div>
					</div>
					<!--业务掉线率-->
					<div style="margin-left: 20px; width: 48%;float: left;margin-top:20px;">
						<div id="pingRate" class="main" style="width: 100%;float: left"></div>
						<div id="pingRateButton" style="width: 97%;float: left">
							<div class="clearfix"></div>
						</div>
					</div>
					<!-- 信号采样点信息 开始 -->
					<div style="background-color: #f7f7f7; height: 50px;width:100%">
						<#if pointList?? && pointList?size &gt; 0>
							<#list pointList as p>
								<div class="boxcontent boxcontent_${p_index+1}" style="width:11%">
									<table class="box_table">
										<tbody>
											<tr>
												<td> ${(p.name)!} </td>
											<tr>
											<tr>
												<td> ${(p.value)!} </td>
											</tr>
										</tbody>
									</table>
								</div>
							</#list>
						</#if>
					</div>
					<!-- 信号采样点信息 结束 -->
					<!--RSRP占比-->
					<div id="rsrpRate" class="main" style="width: 48%;float: left"></div>
					<!--SNR占比 -->
					<div id="snrRate" class="main" style="margin-left: 20px; width: 48%;float: left"></div>
					<!-- 分隔线  -->
					<div style="width: 97%;float: left;margin-top:20px;margin-left:10px;argin-bottom:10px;vertical-align:middle;">
						<div style="border-top:3px solid #ddd;height:1px;width: 41%;float: left;margin-top:20px;">
						
						</div>
						<div style="width: 14%;float: left;margin:10px;color:#aaa;font-size:17px;font-family:SimSun;text-align:center;">
							道路覆盖质差TOPN
						</div>
						<div style="border-top:3px solid #ddd;height:1px;width: 41%;float: left;margin-top:20px;">
						
						</div>
					</div>
					<!--道路覆盖topn-->
					<div style="width: 97%;float: left">
						<table class="table table-striped table-hover table-bordered dataTable">
							<thead>
								<tr style="background-color:#EAEAEA;">
									<th style="text-align:center;width:10%;">序号</th>
									<th style="text-align:center;width:20%;">市</th>
									<th style="text-align:center;width:20%;">区/县</th>
									<th style="text-align:center;width:20%;">道路(km)</th>
									<th style="text-align:center;width:10%;">覆盖率(%)</th>
									<th style="text-align:center;width:10%;">RSRP(dBm)</th>
									<th style="text-align:center;width:10%;">SNR(dB)</th>
								</tr>
							</thead>
							<tbody>
								<#if coverList?? && coverList?size &gt; 0>
									<#list coverList as p>
									<tr style="height:37px;<#if p_index==coverList?size-1>border-bottom:1px #dddddd  solid;</#if>">
										<td style="text-align:center;">${(p_index+1)!}</td>
										<td style="text-align:center;">${(p.cityName)!'-'}</td>
										<td style="text-align:center;">${(p.contryName)!'-'}</td>
										<td style="text-align:center;">${(p.roadName)!'-'}</td>
										<td style="text-align:center;">${(p.coverRate)!'-'}</td>
										<td style="text-align:center;">${(p.rsrp)!'-'}</td>
										<td style="text-align:center;">${(p.snr)!'-'}</td>
									</tr>
									</#list>
								<#else>
									<tr style="height:37px;">
										<td colspan="7" style="text-align:center;">无数据</td>
									</tr>
								</#if>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- echart 图表 开始 -->
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/echarts/www/js/echarts.js"></script>
	<!-- echart 图表 结束 -->
	<!--自定义js 开始-->
	<script type="text/javascript" src="${application.getContextPath()}/js/echartsTools.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/js/page/pageCharts.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/js/homePage/homePage.js"></script>
	<!--自定义js 结束-->
	<script type="text/javascript">
		$(document).ready(function(){
			loadChart();
			var winHeight = window.screen.height;
			var scHeight = document.body.scrollTop;
			var winHeight = window.screen.height;
			$('.page-content').css('min-height',winHeight);
		});
	</script>
</body>
</html>