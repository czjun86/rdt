    <style>
    	.input-icon.right input{
    		padding-right :12px !important
    	}
    </style>
    <div class="portlet box blue" style="width:545px;margin-left:37%;margin-top:50px;">
	<div class="portlet-title">
		<div class="caption">
			采集方案编辑
		</div>
		<div class="tools">
			<button class="close" aria-hidden="true" data-dismiss="modal" type="button"></button>
		</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal form-bordered form-row-seperated" id="saveChild" name="saveChild" method="post">
			<input name="id" type="hidden" id="id" value="${(bean.id)!-1}"/>
			<div class="form-body">
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;"><span style="color:red;" >*&nbsp;</span>方案名称:</label>
					<div class="col-md-7">
						<input class="form-control ban" id="name" name="name" <#if bean?? && bean.id??>disabled = "true"</#if> type="text" style="width:160px;float:left;" placeholder="请输入方案名称" value="${(bean.name)!}" onkeyup="value=value.replace(/[^_a-zA-Z0-9.]/g,'')"/>
						<br/><br/>
						<span class="help-block" for="name"></span>
					</div>
				</div>
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;">方案描述:</label>
					<div class="col-md-7">
						<textarea class="form-control" rows="3" name="mark" style="resize: none;" id="mark" maxlength="50" onkeyup="value=value.replace(/[^\u4e00-\u9fa5_a-zA-Z0-9，。,.]/g,'')">${(bean.mark)!}</textarea>
					</div>
				</div>
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;"><span style="color:red;">*&nbsp;</span>测试服务器地址:</label>
					<div class="col-md-7">
						<input class="form-control ban" name="url" id="url" type="text" value="${(bean.url)!}" onkeyup="value=value.replace(/[^\u4e00-\u9fa5_a-zA-Z0-9,.:\/]/g,'')"  style="width:160px;float:left;" placeholder="请输入   服务器:端口号">
					</div>
				</div>
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;"><span style="color:red;">*&nbsp;</span>tcp端口号:</label>
					<div class="col-md-7">
						<input class="form-control ban" name="tcpport" id="tcpport"  value="${(bean.tcpport)!}" type="text" onkeyup="value=value.replace(/[^0-9\/]/g,'')"  style="width:160px;float:left;" placeholder="请输入  tcp端口号" maxlength = "6">
					</div>
				</div>
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;"><span style="color:red;">*&nbsp;</span>udp端口号:</label>
					<div class="col-md-7">
						<input class="form-control ban" name="udpport" id="udpport" value="${(bean.udpport)!}" type="text" onkeyup="value=value.replace(/[^0-9\/]/g,'')"  style="width:160px;float:left;" placeholder="请输入  udp端口号" maxlength = "6">
					</div>
				</div>
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;"><span style="color:red;">*&nbsp;</span>包数量:</label>
					<div class="col-md-7">
						<input class="form-control ban" name="packnum" id="packnum" value="${(bean.packnum)!}" type="text" onkeyup="value=value.replace(/[^0-9.\/]/g,'')"  style="width:160px;float:left;" placeholder="请输入  包数量">
					</div>
				</div>
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;"><span style="color:red;">*&nbsp;</span>包大小:</label>
					<div class="col-md-7">
						<input class="form-control ban" name="packsize" id="packsize" value="${(bean.packsize)!}" type="text" onkeyup="value=value.replace(/[^0-9.\/]/g,'')"  style="width:160px;float:left;" placeholder="请输入   包大小"><span style="position:relative;top:7px;left:5px;">(Byte)</span>
					</div>
				</div>
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:5px;"><span style="color:red;">*&nbsp;</span>非充电测试:</label>
					<div class="col-md-7" style="margin-top:-7px;">
						<div class="make-switch" data-on="success" data-off="danger" data-on-label="测试" data-off-label="不测">
							<input id="fdl" type="checkbox" <#if bean?? && bean.fdl?? && bean.fdl==1>checked="checked"</#if> class="toggle"/>
						</div>
					</div>
				</div>
				<div class="form-group validateDiv" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:5px;"><span style="color:red;">*&nbsp;</span>是否采集:</label>
					<div class="col-md-7" style="margin-top:-7px;">
						<div class="make-switch" data-on="success" data-off="danger" data-on-label="开启" data-off-label="禁用">
							<input id="isCollect" type="checkbox" <#if bean?? && bean.isCollect?? && bean.isCollect==0><#else>checked="checked"</#if> class="toggle"/>
						</div>
					</div>
				</div>
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label  class="col-md-4" style="text-align:center;margin-top:15px;margin-left:-7px;" for="userName">
						采集周期：
					</label>
					<div class="input-icon right col-md-11">
						<span style="float:left;margin-top:7px;margin-left:60px;">从</span><div class="validateDiv"><input name="start1" id="start1" value="${(bean.start1)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">到</span><div class="validateDiv"><input name="end1" id="end1" value="${(bean.end1)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">无线周期</span><div class="validateDiv"><input name="wireless1" value="${(bean.wireless1)!}" id="wireless1" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
						<span style="float:left;margin-top:7px;margin-left:15px;">应用周期</span><div class="validateDiv"><input name="app1" value="${(bean.app1)!}" id="app1" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
					</div>
					<div class="input-icon right col-md-11">
						<span style="float:left;margin-top:7px;margin-left:60px;">从</span><div class="validateDiv"><input name="start2" id="start2" value="${(bean.start2)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">到</span><div class="validateDiv"><input name="end2" id="end2" value="${(bean.end2)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">无线周期</span><div class="validateDiv"><input name="wireless2" value="${(bean.wireless2)!}" id="wireless2" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
						<span style="float:left;margin-top:7px;margin-left:15px;">应用周期</span><div class="validateDiv"><input name="app2" id="app2" value="${(bean.app2)!}" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
					</div>
					<div class="input-icon right col-md-11">
						<span style="float:left;margin-top:7px;margin-left:60px;">从</span><div class="validateDiv"><input name="start3" id="start3" value="${(bean.start3)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">到</span><div class="validateDiv"><input name="end3" id="end3" value="${(bean.end3)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">无线周期</span><div class="validateDiv"><input name="wireless3" value="${(bean.wireless3)!}" id="wireless3" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
						<span style="float:left;margin-top:7px;margin-left:15px;">应用周期</span><div class="validateDiv"><input name="app3" id="app3" value="${(bean.app3)!}" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
					</div>
					<div class="input-icon right col-md-11">
						<span style="float:left;margin-top:7px;margin-left:60px;">从</span><div class="validateDiv"><input name="start4" id="start4" value="${(bean.start4)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">到</span><div class="validateDiv"><input name="end4" id="end4" value="${(bean.end4)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">无线周期</span><div class="validateDiv"><input name="wireless4" value="${(bean.wireless4)!}" id="wireless4" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
						<span style="float:left;margin-top:7px;margin-left:15px;">应用周期</span><div class="validateDiv"><input name="app4" id="app4" value="${(bean.app4)!}" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
					</div>
					<div class="input-icon right col-md-11" style="padding-bottom:30px;">
						<span style="float:left;margin-top:7px;margin-left:60px;">从</span><div class="validateDiv"><input name="start5" id="start5" value="${(bean.start5)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">到</span><div class="validateDiv"><input name="end5" id="end5" value="${(bean.end5)!}" class="form-control ban" type="text" style="width:40px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="2"></div>
						<span style="float:left;margin-top:7px;margin-left:15px;">无线周期</span><div class="validateDiv"><input name="wireless5" value="${(bean.wireless5)!}" id="wireless5" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
						<span style="float:left;margin-top:7px;margin-left:15px;">应用周期</span><div class="validateDiv"><input name="app5" id="app5" value="${(bean.app5)!}" class="form-control ban" type="text" style="width:60px;float:left;margin-left:5px;" onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="5"></div><span style="float:left;margin-top:7px;margin-left:5px;">(s)</span>
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