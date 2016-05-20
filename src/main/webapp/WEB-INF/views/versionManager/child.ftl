    <style>
    	.input-icon.right input{
    		padding-right :12px !important
    	}
    </style>
    <div class="portlet box blue" style="width:545px;margin-left:30%;margin-top:50px;">
	<div class="portlet-title">
		<div class="caption">
			新建版本
		</div>
		<div class="tools">
			<button class="close" aria-hidden="true" data-dismiss="modal" type="button"></button>
		</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal form-bordered form-row-seperated" id="saveChild" enctype="multipart/form-data" name="saveChild" method="post">
			<input name="id" type="hidden" id="id" value="${(bean.id)!-1}"/>
			<div class="form-body">
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;"><span style="color:red;" >*&nbsp;</span>版本文件:</label>
					<div class="input-icon right col-md-7" style="top:-5px;">
						<input name="file" type="file" class="form-control" id="file" style="padding-bottom:38px;">
						<input name="fileName" type="hidden" class="form-control" id="fileName" style="padding-bottom:38px;">
						<span class="help-block" for="groupName"></span>
					</div>
				</div>
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;"><span style="color:red;" >*&nbsp;</span>版本号&nbsp;:</label>
					<div class="col-md-7 validateDiv">
						<input class="form-control ban" id="soft_ver" name="soft_ver" <#if bean?? && bean.id??>disabled = "true"</#if> type="text" style="width:160px;float:left;" placeholder="请输入版本号" value="${(bean.soft_ver)!}"/>
						<br/><br/>
						<span class="help-block" for="soft_ver"></span>
					</div>
				</div>
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;">版本描述:</label>
					<div class="col-md-7 validateDiv">
						<textarea class="form-control" rows="3" name="desc" style="resize: none;" id="desc" maxlength="50" onkeyup="value=value.replace(/[^\u4e00-\u9fa5_a-zA-Z0-9，。,.]/g,'')">${(bean.desc)!}</textarea>
					</div>
				</div>
			</div>
			<div class="form-actions modal-footer">
				<button type="button" class="btn blue saveChild">确定</button>
				<button type="button" class="btn default" data-dismiss="modal">取消</button>
			</div>
		</form>
	</div>	
</div>
<!--开关按钮的JS(是否启用)-->
<script src="${application.getContextPath()}/scripts/plugins/bootstrap-switch/static/js/bootstrap-switch.min.js" type="text/javascript" ></script>	