    <div class="portlet box blue" style="width:35%;margin-left:35%;margin-top:200px;">
	<div class="portlet-title">
		<div class="caption">
			添加用户组
		</div>
		<div class="tools">
			<button class="close" aria-hidden="true" data-dismiss="modal" type="button"></button>
		</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal form-bordered form-row-seperated" id="addGroupForm" method="post">
			<div class="form-body">
				<div class="form-group" style="padding:10px 0px 0px 0;height:80px;">
					<label  class="col-md-3" style="margin-left:10px;width:22%;" for="userName" class="">
						<span style="color:red;">*&nbsp;</span>
						用&nbsp;户&nbsp;组&nbsp;名
					</label>
					<div class="input-icon right col-md-7">
						<input name="groupName" type="text" class="form-control" id="groupName" maxlength="20" placeholder="请输入用户组名">
						<span class="help-block" for="groupName"></span>
					</div>
					<div class="input-icon right col-md-2">
						<button type="button" class="btn blue saveGroup">确定</button>
					</div>
				</div>
			</div>
		</form>
	</div>	
</div>
<!--开关按钮的JS(是否启用)-->
<script src="${application.getContextPath()}/scripts/plugins/bootstrap-switch/static/js/bootstrap-switch.min.js" type="text/javascript" ></script>