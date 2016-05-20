<div class="portlet box blue" style="width:30%;margin-left:35%;margin-top:70px;">
<div class="portlet-title">
	<div class="caption">
		修改密码
	</div>
	<div class="tools">
		<button class="close" aria-hidden="true" data-dismiss="modal" type="button"></button>
	</div>
</div>
<div class="portlet-body form">
	<form class="form-horizontal form-bordered form-row-seperated" id="modfiyPassword" method="post">
		<input name="userid" type="hidden" id="userid" value="${(user.userid)!}"/>
		<div class="form-body">
			<div class="form-group" style="padding:10px 0px 0px 0;">
				<label class="col-md-3" style="margin-left:10px;width:25%;" for="userName">
					<span style="color:red;">*&nbsp;</span>
					用&nbsp;&nbsp;户&nbsp;&nbsp;名
				</label>
				<div class="input-icon right col-md-8">
					<input name="userName" type="text" class="form-control" id="userName" value="${(user.userName)!}" readonly>
				</div>
			</div>
			<div class="form-group" style="padding:10px 0px 0px 0;">
				<label class="col-md-3" style="margin-left:10px;width:25%;" for="oldPwd">
					<span style="color:red;">*&nbsp;</span>
					旧的密码
				</label>
				<div class="input-icon right col-md-8">
					<input name="oldPwd" type="text" class="form-control" id="oldPwd" maxlength="15"  onkeyup="value=value.replace(/[\W]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" />
					<span class="help-block" for="oldPwd"></span>
				</div>
			</div>
			<div class="form-group" style="padding:10px 0px 0px 0;">
				<label class="col-md-3" style="margin-left:10px;width:25%;" for="newPwd1">
					<span style="color:red;">*&nbsp;</span>
					新的密码
				</label>
				<div class="input-icon right col-md-8">
					<input name="newPwd1" type="text" class="form-control" id="newPwd1" maxlength="15"  onkeyup="value=value.replace(/[\W]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" />
					<span class="help-block" for="newPwd1"></span>
				</div>
			</div>
			<div class="form-group" style="padding:10px 0px 0px 0;">
				<label class="col-md-3" style="margin-left:10px;width:25%;" for="userName">
					<span style="color:red;">*&nbsp;</span>
					确认密码
				</label>
				<div class="input-icon right col-md-8">
					<input name="newPwd2" type="text" class="form-control" id="newPwd2" maxlength="15" onkeyup="value=value.replace(/[\W]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" />
					<span class="help-block" for="newPwd2"></span>
				</div>
			</div>
		</div>
		<div class="form-actions modal-footer">
			<button type="button" class="btn blue" onclick="updPwd()">确定</button>
			<button type="button" class="btn default" data-dismiss="modal">取消</button>
		</div>
	</form>
</div>	
</div>
<script>
jQuery(document).ready(function() { 
			formValidate.initPwd();
		});
</script>