<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
	<meta charset="utf-8" />
	<title>移动网络自动路测系统</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="tc" />
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
		.row {
		    margin-left: 0;
		    margin-right: 0;
		}
	</style>
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
						<div class="portlet ">
							<div class="portlet-body">
								<!--<div class="navbar navbar-default" role="navigation" method="post" action="${application.getContextPath()}/collection/collection" style="background:#fff !important;">
									
								</div>-->
								<div class="table-toolbar">
									<div class="btn-group">
										<button class="btn yellow" style="height:31px;width:82px;margin-top:8px;margin-left:0px;" id="back"  onClick="goBack();">返回<i class="fa fa-mail-reply"></i></button>
									</div>
									<div class="btn-group pull-right">
									</div>
								</div>
								<form  action="${application.getContextPath()}/epinfo/epinfo" id="pageForm" role="search" method="post" >
									<!--分页-->
									<input type="hidden" id="editChild" name="pageIndex" value="${(pageIndex)!1}"/>
									<input type="hidden" id="editChild" name="pageSize" value="${(pageSize)!10}"/>
									<!--时间-->
									<input type="hidden" id="queryTime_child" name="queryTime" value="${(bean.queryTime)!}"/>
									<!--方案名称-->
									<input type="hidden" id="province_child" name="province" value="${(bean.province)!}"/>
									<input type="hidden" id="district_child" name="district" value="${(bean.district)!}"/>
									<input type="hidden" id="area_child" name="area" value="${(bean.area)!}"/>
									<!--时间-->
									<input type="hidden" id="operator_child" name="operator" value="${(bean.operator)!}"/>
									<!--方案名称-->
									<input type="hidden" id="imei_child" name="imei" value="${(bean.imei)!}"/>
									<!--时间-->
									<input type="hidden" id="model_child" name="model" value="${(bean.model)!}"/>
									<!--方案名称-->
									<input type="hidden" id="colletion_child" name="colletion" value="${(bean.colletion)!}"/>
									<!--方案名称-->
									<input type="hidden" id="vision_child" name="vision" value="${(bean.vision)!}"/>
								</form>
								<form  id="saveChild" name="saveChild" onsubmit="return false;">
									<!--表单开始   开始-->
									<div class="table-responsive table-scrollable">
											<input class="form-control" type="hidden" id="id" value="${(epinfo.id)!'-1'}" style="width:160px !important;" />
										<!--区域  开始-->
											<div class="form-group col-md-10" style="margin-left:65px;margin-top:20px;">
												<label class="control-label" style="margin-right:38px;">区域:</label>
												<select class="form-control input-small select2me" style="width:165px;" name="province" id="province">
													<#if provinces ?? && provinces?size &gt; 0>
														<#list provinces as p>
															<option value="${(p.id)!}" <#if province?? && p.id == province>selected</#if>>${(p.text)!}</option>
														</#list>
													</#if>
												</select>
												&nbsp;
												<select class="form-control input-small select2me" style="width:165px;margin-left:20px;" name="district" id="district">
													<option value="-1" >全部</option>
													<#if districts ?? && districts?size &gt; 0>
														<#list districts as p>
															<option value="${(p.id)!}" <#if district?? && p.id == district>selected</#if>>${(p.text)!}</option>
														</#list>
													</#if>
												</select>
												&nbsp;
												<select class="form-control input-small select2me" style="width:165px;margin-left:20px;" name="area" id="area">
													<option value="-1" >全部</option>
													<#if areas ?? && areas?size &gt; 0>
														<#list areas as p>
															<option value="${(p.id)!}" <#if area?? && p.id == area>selected</#if>>${(p.text)!}</option>
														</#list>
													</#if>
												</select>
											</div>
											<!-- 区域 结束 -->
											<!-- 运营商 开始 -->
											<div class="form-group col-md-4" style="margin-left:65px; margin-top:20px;">
												<label class="control-label" style="margin-right:25px;">运营商:</label>
												<select class="form-control input-small select2me" style="width:160px !important;" name="operator" id="operator">
													<option <#if epinfo?? && epinfo.telecoms?? && epinfo.telecoms == "1">selected</#if> value="1">联通</option>
													<option <#if epinfo?? && epinfo.telecoms?? && epinfo.telecoms == "2">selected</#if> value="2">移动</option>
													<option <#if epinfo?? && epinfo.telecoms?? && epinfo.telecoms == "3">selected</#if> value="3">电信</option>
												</select>
											</div>
											<!-- 运营商 结束-->
											<!-- IMEI 开始 -->
											<div class="form-group col-md-6" style="margin-top:20px;">
												<label class="control-label" style="float:left;margin-right:27px;margin-top:7px;height:50px;">IMEI:</label>
												<div class="form-group">
													<input class="form-control" type="text" id="imei" name="imei" style="width:160px !important;" value="${(epinfo.imei)!}" placeholder="请输入IMEI"/>
													<span class="help-block" for="imei"></span>
												</div>
											</div>
											<!-- IMEI 结束 -->
											<!-- 终端型号 开始 -->
											<div class="form-group col-md-4" style="margin-left:65px; ">
												<label class="control-label" style="margin-right:17px;float:left;">终端型号:</label>
												<div style="width:160px;float:left;"><input class="form-control epName" type="text" id="model" name="model" value="${(epinfo.term_model)!}" style="width:160px !important;float:left;" placeholder="请输入终端型号"/></div>
											</div>
											<!-- 终端型号 结束 -->
											<!-- 方案名称 开始 -->
											<div class="form-group col-md-6" style="">
												<label class="control-label">方案名称:</label>
												<select class="form-control input-small select2me" style="width:160px !important;" name="colletion" id="colletion">
													<#if colletions?? && colletions?size &gt; 0>
														<#list colletions as colletion>
															<option <#if epinfo?? && epinfo.rule_name?? && epinfo.rule_name == colletion.value>selected</#if> value="${(colletion.id)!}" >${(colletion.value)!}</option>
														</#list>
													</#if>
												</select>
											</div>
											<!-- 方案名称 结束 -->
											<!-- 版本号 开始 -->
											<div class="form-group col-md-8" style="margin-left:65px; margin-top:20px;">
												<label class="control-label" style="margin-right:25px;">版本号:</label>
												<select class="form-control input-small select2me" style="width:160px !important;" name="vision" id="vision">
													<#if visions?? && visions?size &gt; 0>
														<#list visions as vision>
															<option <#if epinfo?? && epinfo.soft_ver?? && epinfo.soft_ver == vision.value>selected</#if> value="${(vision.id)!}" >${(vision.value)!}</option>
														</#list>
													</#if>
												</select>
											</div>
											<!-- 版本号 结束 -->
											<div class="form-group col-md-8" style="margin-left:65px; margin-top:20px;">
												<label class="control-label" style="margin-right:18px; margin-right:40px;float:left;">备注:</label>
												<div style="width:320px;">
													<textarea class="form-control" rows="3" name="mark" style="resize: none;" id="mark" maxlength="50" onkeyup="value=value.replace(/[^\u4e00-\u9fa5_a-zA-Z0-9，。,.]/g,'')">${(epinfo.memo)!}</textarea>
												</div>
											</div>
											<div class="form-group col-md-8" style="margin-left:65px; margin-top:20px;">
													<button class="btn blue" style="float:right;margin:-10px 10px 0 0;width:62px;" id="save">保存</button>
											</div>
									</div>
									<!--表单开始   结束-->
								</form>
							</div>
						</div>
						<div id="resoucesPage"></div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="${application.getContextPath()}/js/epinfo/autocomplete.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/js/epinfo/child.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/js/epinfo/validater.js" type="text/javascript"></script>
	<!--区域连动-->
	<script type="text/javascript" src="${application.getContextPath()}/js/report/areaNoAuthority.js"></script>
	<script type="text/javascript">
	jQuery(document).ready(function() {
		    var winHeight = window.screen.height;
			var scHeight = document.body.scrollTop;
			var winHeight = window.screen.height;
			$('.page-content').css('min-height',winHeight);
			$("#modal-backdrop").hide();
			formValidate.init();
		 });
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>