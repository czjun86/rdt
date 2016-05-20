    <div class="portlet box blue" style="width:37%;margin-left:35%;margin-top:200px;">
	<div class="portlet-title">
		<div class="caption">
			导入Excel
		</div>
	<div class="tools">
			<button class="close" aria-hidden="true" data-dismiss="modal" type="button"></button>
		</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal form-bordered form-row-seperated" target="leadwindow" enctype="multipart/form-data" id="importExcel"  action="${application.getContextPath()}/epinfo/saveExcel" method="post">
			<div class="form-body">
				<div class="form-group" style="padding:10px 0px 0px 0;height:80px;">
					<label  class="col-md-3" style="margin-left:10px;width:22%;" for="userName" class="">
						<span style="color:red;">*&nbsp;</span>
						导入把文件:
					</label>
					<div class="input-icon right col-md-7" style="top:-5px;">
						<input name="file" type="file" class="form-control" id="file" style="padding-bottom:38px;">
						<span class="help-block" for="groupName"></span>
					</div>
					<div class="input-icon right col-md-2">
						<button type="button" class="btn blue importFile">确定</button>
						<button class="btn default" data-dismiss="modal" style="display:none;" type="button">取消</button>
					</div>
				</div>
			</div>
		</form>
	</div>	
</div>
<!--开关按钮的JS(是否启用)-->
<script src="${application.getContextPath()}/scripts/plugins/bootstrap-switch/static/js/bootstrap-switch.min.js" type="text/javascript" ></script>