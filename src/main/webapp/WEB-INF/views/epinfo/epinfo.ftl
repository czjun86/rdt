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
					<!-- title-->
						<div class="portlet ">
							<div class="portlet-body">
								<!--报表工具-->
								<div class="navbar navbar-default" role="navigation" method="post" action="${application.getContextPath()}/epinfo/epinfo" style="background:#fff !important;">
									<form class="navbar-form form-inline navbar-left breadcrumb"  id="epinfoForm" onsubmit="return false;" >
										<!--时间范围控件       开始-->
										<div class="form-group">
											<label class="control-label">时间范围:</label>
											<div class="form-group" id="reportrange" dateFormat="YYYY.MM.DD" beforNdays="1" timeP="false" timeM="false" showDays="true"  style="width:165px;">
												<div class="input-icon right" data-date-start-date="-30d"  style="width:165px;">
													<input type="text" class="form-control report-input" id="queryTime" name="queryTime" value="${(bean.queryTime)!}" readOnly style=" padding-right: 15px !important;width:165px !important;" />
												</div>
											</div>
											<a href="#" onclick="document.getElementById('queryTime').value=''"><i class="fa fa-rotate-left"></i></a>
										</div>
										<!--时间范围控件       结束-->
										<!--区域  开始-->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">区域:</label>
											<select class="form-control input-small select2me" style="width:165px;" name="province" id="province">
												<#if countrys ?? && countrys?size &gt; 0>
													<#list countrys as p>
														<option value="${(p.id)!}" <#if bean?? && p.id == bean.province>selected</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
											&nbsp;
											<select class="fform-control input-small select2me" style="width:165px;margin-left:-5px;" name="district" id="district">
												<option value="-1" >全部</option>
												<#if citys ?? && citys?size &gt; 0>
													<#list citys as p>
														<option value="${(p.id)!}" <#if bean?? && p.id == bean.district>selected</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
											&nbsp;
											<select class="form-control input-small select2me" style="width:165px;margin-left:-5px;" name="area" id="area">
												<option value="-1" >全部</option>
												<#if areas ?? && areas?size &gt; 0>
													<#list areas as p>
														<option value="${(p.id)!}" <#if bean?? && p.id == bean.area>selected</#if>>${(p.text)!}</option>
													</#list>
												</#if>
											</select>
										</div>
										<!-- 区域 结束 -->
										<!-- 运营商 开始 -->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">运营商:</label>
											<select class="form-control input-small select2me" style="width:165px;" name="operator" id="operator">
												<option value="-1">全部</option>
												<option value="1" <#if bean?? && bean.operator?? && bean.operator == 1>selected</#if>>联通</option>
												<option value="2" <#if bean?? && bean.operator?? && bean.operator == 2>selected</#if>>移动</option>
												<option value="3" <#if bean?? && bean.operator?? && bean.operator == 3>selected</#if>>电信</option>
											</select>
										</div>
										<!-- 运营商 结束-->
										<!-- IMEI 开始 -->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">IMEI:</label>
											<input class="form-control" type="text" id="imei" name="imei" style="width:160px !important;margin-left:15px;" value="${(bean.imei)!}" placeholder="请输入IMEI"/>
										</div>
										<!-- IMEI 结束 -->
										<!-- 终端型号 开始 -->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">终端型号:</label>
											<select class="form-control input-small select2me" style="width:165px;" name="model" id="model">
												<option value="-1">全部</option>
												<#if models?? && models?size &gt; 0>
													<#list models as model>
														<option value="${(model.id)!}" <#if bean?? && bean.model?? && model.id == bean.model>selected</#if> >${(model.value)!}</option>
													</#list>
												</#if>
											</select>
										</div>
										<!-- 终端型号 结束 -->
										<!-- 方案名称 开始 -->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">方案名称:</label>
											<select class="form-control input-small select2me" style="width:165px;" name="colletion" id="colletion">
												<option value="-1" <#if bean?? && bean.colletion?? && -1 == bean.colletion>selected</#if>>全部</option>
												<option value="-2" <#if bean?? && bean.colletion?? && -2 == bean.colletion>selected</#if>>-</option>
												<#if colletions?? && colletions?size &gt; 0>
													<#list colletions as colletion>
														<option value="${(colletion.id)!}" <#if bean?? && bean.colletion?? && colletion.id == bean.colletion>selected</#if>>${(colletion.value)!}</option>
													</#list>
												</#if>
											</select>
										</div>
										<!-- 方案名称 结束 -->
										<!-- 版本号 开始 -->
										<div class="form-group" style="margin-left:8px;">
											<label class="control-label">版本号:</label>
											<select class="form-control input-small select2me" style="width:165px;" name="vision" id="vision">
												<option value="-1" <#if bean?? && bean.vision?? && -1 == bean.vision>selected</#if>>全部</option>
												<option value="-2" <#if bean?? && bean.vision?? && -2 == bean.vision>selected</#if>>-</option>
												<#if visions?? && visions?size &gt; 0>
													<#list visions as vision>
														<option value="${(vision.id)!}" <#if bean?? && bean.vision?? && vision.id == bean.vision>selected</#if>>${(vision.value)!}</option>
													</#list>
												</#if>
											</select>
										</div>
										<!-- 版本号 结束 -->
										<button class="btn blue" style="height:31px;width:62px;margin-top:-6px;margin-left:10px;" id="querybtn">查询</button>
									</form>
								</div>
								<div class="table-toolbar">
									<div class="btn-group">
										<button class="btn green" style="height:31px;width:82px;margin-top:8px;margin-left:0px;" id="add">创建&nbsp;<i class="fa fa-plus"></i></button>
										<button class="btn green" style="height:31px;width:82px;margin-top:8px;margin-left:10px;" id="export">导出&nbsp;<i class="fa fa-download"></i></button>
										<button class="btn green" style="height:31px;width:82px;margin-top:8px;margin-left:10px;" id="import">导入&nbsp;<i class="fa fa-upload"></i></button>
										<button class="btn green" style="height:31px;width:82px;margin-top:8px;margin-left:10px;" fileName="ImportTemplate" id="demo">导入模板&nbsp;<i class="fa fa-file-text-o"></i></button>
									</div>
									<div class="btn-group pull-right">
									</div>
								</div>
								<!-- 查询条件 -->
								<form  action="${application.getContextPath()}/epinfo/epinfo" id="pageForm" role="search" method="post" >
									<!--时间-->
									<input type="hidden" id="queryTime_query" name="queryTime" value="${(bean.queryTime)!}"/>
									<!--方案名称-->
									<input type="hidden" id="province_query" name="province" value="${(bean.province)!}"/>
									<input type="hidden" id="district_query" name="district" value="${(bean.district)!}"/>
									<input type="hidden" id="area_query" name="area" value="${(bean.area)!}"/>
									<!--时间-->
									<input type="hidden" id="operator_query" name="operator" value="${(bean.operator)!}"/>
									<!--方案名称-->
									<input type="hidden" id="imei_query" name="imei" value="${(bean.imei)!}"/>
									<!--时间-->
									<input type="hidden" id="model_query" name="model" value="${(bean.model)!}"/>
									<!--方案名称-->
									<input type="hidden" id="colletion_query" name="colletion" value="${(bean.colletion)!}"/>
									<!--方案名称-->
									<input type="hidden" id="vision_query" name="vision" value="${(bean.vision)!}"/>
								</form>
								<form  action="${application.getContextPath()}/epinfo/child" id="editPage" role="search" method="post" >
									<!--修改id-->
									<input type="hidden" id="editChild" name="id" value=""/>
									<!--分页-->
									<input type="hidden" id="editChild" name="pageIndex" value="${(page.pageIndex)!1}"/>
									<input type="hidden" id="editChild" name="pageSize" value="${(page.pageSize)!10}"/>
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
								<!--表单开始   开始-->
								<div class="table-responsive table-scrollable">
									<table class="table table-striped table-hover table-bordered" id="sample_editable_1">
										<!--表单title 开始-->
										<thead>
											<tr style="background-color:#EAEAEA;">
													<th style="text-align:center;" >
														<label>
															<input id="all" type="checkbox" name="checkAll" value="-1">
														</label>
													</th>
													<th style="text-align:center;" >区域</th>
													<th style="text-align:center;" >运营商</th>
													<th style="text-align:center;" >终端型号</th>
													<th style="text-align:center;" >IMEI</th>
													<th style="text-align:center;" >采集方案</th>
													<th style="text-align:center;" >版本</th>
													<th style="text-align:center;" >更新时间</th>
													<th style="text-align:center;" >是否上报</th>
													<th style="text-align:center;" >备注</th>
													<th style="text-align:center;" >操作</th>
											</tr>
										</thead>
										<!--表单title 结束-->
										<!-- 表单内容 开始 -->
										<tbody>
											<#if page?? && page.list?? &&  page.list?size &gt; 0>
												<#list page.list as ls>
													<tr style="height:37px;<#if ls_index==page.list?size-1>border-bottom:1px #dddddd  solid;</#if>">
														<td style="text-align:center;" >
															<label>
																<input  type="checkbox" name="checkOne" value="${(ls.id)!'-'}">
															</label>
														</td>
														<td style="text-align:center;" >${(ls.province)!'-'}&nbsp;|&nbsp;${(ls.district)!'-'}&nbsp;|&nbsp;${(ls.area)!'-'}</td>
														<td style="text-align:center;" >
														<#if bean?? && ls.telecoms?? && ls.telecoms == "1">联通</#if>
														<#if bean?? && ls.telecoms?? && ls.telecoms == "2">移动</#if>
														<#if bean?? && ls.telecoms?? && ls.telecoms == "3">电信</#if>
														</td>
														<td style="text-align:center;" >${(ls.term_model)!'-'}</td>
														<td style="text-align:center;" >${(ls.imei)!'-'}</td>
														<td style="text-align:center;" >${(ls.rule_name)!'-'}</td>
														<td style="text-align:center;" >${(ls.soft_ver)!'-'}</td>
														<td style="text-align:center;" >${(ls.update_time)!'-'}</td>
														<td style="text-align:center;" ><#if ls.is_collect?? && ls.is_collect == 1>是<#else>否</#if></td>
														<td style="text-align:center;" title="${(ls.memo)!}"><#if ls.memo?? && (ls.memo)?length &gt;10>${ls.memo?substring(0,10)}...<#else>${(ls.memo)!}</#if></td>
														<td style="text-align:center;" name="${(ls.id)!'-'}">
														<a href="#" class="edit" nid = "${(ls.id)!}" nname = "${(ls.name)!'-'}">编辑</a>
														</td>
													</tr>
												</#list>
											<#else>
												<tr>
													<td colspan="10"  align='center' style="height:37px;border-bottom:1px #dddddd  solid;">无符合的终端信息</td>
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
	<!--dialog请求层-->
	<div id="ajax-modal" class="modal fade" tabindex="-1"></div>
	<div class="modal fade" id="ajax" tabindex="-1" role="basic" aria-hidden="true">
	</div>
	<div class="bottom_btn yfpz_btn" style="position:fixed;float:right;bottom:40px;right:40px;width:225px;height:73px;display:none;">
		<button class="btn blue" id="colletion_nums_save" style="float:left;height:31px;width:75px;margin:5px 5px;">方案保存</button>
		<select class="form-control input-small select2me" style="width:165px;margin:5px 5px;" name="colletion_nums" id="colletion_nums">
			<#if colletions?? && colletions?size &gt; 0>
				<#list colletions as colletion>
					<option value="${(colletion.id)!}">${(colletion.value)!}</option>
				</#list>
			</#if>
		</select>
		<button class="btn blue" id="vivsion_nums_save" style="float:left;height:31px;width:75px;margin:5px 5px;">版本保存</button>
		<select class="form-control input-small select2me" style="width:165px;margin:5px 5px;" name="vision_nums" id="vision_nums">
			<#if visions?? && visions?size &gt; 0>
				<#list visions as vision>
					<option value="${(vision.id)!}">${(vision.value)!}</option>
				</#list>
			</#if>
		</select>
	</div>
	<div id="error-modal" class="modal fade" tabindex="-1"></div>
	<div class="modal fade" id="error" tabindex="-1" role="basic" aria-hidden="true">
	</div>
	<!-- 开始    日期范围控件脚本  -->
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/moment.min.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/plugins/bootstrap-daterangepicker/daterangepicker_amend.js"></script>
	<script type="text/javascript" src="${application.getContextPath()}/scripts/scripts/form-dateRanges_amend.js"></script>
	<!--区域连动-->
	<script type="text/javascript" src="${application.getContextPath()}/js/report/areaNoAuthority.js"></script>
	<!--分页-->
	<script src="${application.getContextPath()}/js/ejs_production.js" type="text/javascript"></script>
	<script src="${application.getContextPath()}/scripts/scripts/table-pages.js" type="text/javascript"></script>
	<!--复选框 -->
	<script src="${application.getContextPath()}/scripts/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
	
	<script src="${application.getContextPath()}/js/epinfo/epinfo.js" type="text/javascript"></script>
	
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
		   yfpzBtn();
		   $("#modal-backdrop").hide();
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>