<!--<div class="portlet box blue" style="width:25%;margin-left:35%;margin-top:100px;">
<div class="portlet-title">
	<div class="caption">
		选择菜单
	</div>
	<div class="tools">
		<button class="close" aria-hidden="true" data-dismiss="modal" type="button"></button>
	</div>
</div>
	<div class="portlet-body form">
		<div class="form-body">
			<div class="form-group" style="padding:10px 0px 0px 0;height:300px;">
				<ul id="tt" class="easyui-tree" data-options="url:'${application.getContextPath()}/group/getarea?groupid=${groupid!}',animate:true,checkbox:true,
					onlyLeafCheck:false">
				</ul>
				<button class="btn blue sel_group btn-sm">
				  &nbsp;确定
				</button>
				<button class="btn blue default btn-sm" onclick="javascript:$('#groupdlg').dialog('close')">
				  &nbsp;取消
				</button>
			</div>
		</div>
	</div>	
</div>-->

<ul id="tt" class="easyui-tree" data-options="url:'${application.getContextPath()}/group/getarea?groupid=${groupid!}',animate:true,checkbox:true,
					onlyLeafCheck:false">
				</ul>