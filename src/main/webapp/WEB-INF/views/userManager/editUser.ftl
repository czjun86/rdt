    <div class="portlet box blue" style="width:30%;margin-left:35%;margin-top:50px;">
	<div class="portlet-title">
		<div class="caption">
			<#if (user.userid)??>编辑用户</#if><#if !(user.userid)??>添加用户</#if>
		</div>
		<div class="tools">
			<button class="close" aria-hidden="true" data-dismiss="modal" type="button"></button>
		</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal form-bordered form-row-seperated" id="editUser" method="post">
			<input name="userid" type="hidden" id="userid" value="${(user.userid)!}"/>
			<div class="form-body">
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label  class="col-md-4" style="text-align:center;margin-top:15px;margin-left:-7px;" for="userName" class="">
						<span style="color:red;">*&nbsp;</span>
						用&nbsp;&nbsp;户&nbsp;&nbsp;名
					</label>
					<div class="input-icon right col-md-7">
						<input name="userName" type="text" class="form-control" id="userName" value="${(user.userName)!}" <#if user.userid??>readonly="readonly"</#if> maxlength="20" placeholder="请输入用户名">
						<span class="help-block" for="userName"></span>
					</div>
				</div>
				
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label  class="col-md-4" style="text-align:center;margin-top:15px;margin-left:-7px;" for="name">
						<span style="color:red;">*&nbsp;</span>
						真实姓名
					</label>
					<div class="input-icon right col-md-7">
						<input name="name" type="text" class="form-control" id="name" value="${(user.name)!}" maxlength="10" placeholder="请输入真实姓名">
						<span class="help-block" for="name"></span>
					</div>
				</div>
				
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label  class="col-md-4" style="text-align:center;margin-top:15px;" for="port">联系邮箱</label>
					<div class="input-icon right col-md-7">                                       
						<input name="email" type="text" class="form-control" id="email" value="${(user.email)!}" placeholder="请输入联系邮箱">
						<span class="help-block" for="email"></span>
					</div>
				</div>
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label  class="col-md-4" style="text-align:center;margin-top:15px;"  for="name" >联系电话</label>
					<div class="input-icon right col-md-7">                                       
						<input name="phone" type="text" class="form-control " id="phone" maxlength="11" value="${(user.phone)!}" placeholder="请输入联系电话">
						<span class="help-block" for="phone"></span>
					</div>
				</div>
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;">运&nbsp;&nbsp;营&nbsp;&nbsp;商</label>
					<div class="input-icon right col-md-7">
						<select class="form-control select2me" name="operator" id="operator">
							<option value="1" <#if user.operator?? && user.operator =='1'>selected="true"</#if>>联通</option>
							<option value="2" <#if user.operator?? && user.operator =='2'>selected="true"</#if>>移动</option>
							<option value="3" <#if user.operator?? && user.operator =='3'>selected="true"</#if>>电信</option>
						</select>
					</div>
				</div>
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:7px;">运营商权限</label>
					<div class="input-icon right col-md-7">
						<label style="margin-right:10px;">
							<input  type="checkbox" name="check" <#if user.operator?? && user.operator =='1'>disabled</#if> <#if checkuni?? && checkuni =='1'>checked="true"</#if> id="checkuni" value="${(ls.id)!'-'}"><span style="margin-left:5px;">联通</span>
						</label>
						<label style="margin-right:10px;">
							<input  type="checkbox" name="check" <#if user.operator?? && user.operator =='2'>disabled</#if> <#if checkmob?? && checkmob =='1'>checked="true"</#if> id="checkmob" value="${(ls.id)!'-'}"><span style="margin-left:5px;">移动</span>
						</label>
						<label style="margin-right:10px;">
							<input  type="checkbox" name="check" <#if user.operator?? && user.operator =='3'>disabled</#if> <#if checktele?? && checktele =='1'>checked="true"</#if> id="checktele" value="${(ls.id)!'-'}"><span style="margin-left:5px;">电信</span>
						</label>
					</div>
				</div>
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;">用&nbsp;&nbsp;户&nbsp;&nbsp;组</label>
					<div class="input-icon right col-md-7">
						<select class="form-control select2me" name="roleid" id="roleid">
							<#if groups ?? && groups?size &gt; 0>
								<#list groups as p>
									<option value="${(p.id)!}" <#if user.roleid?? && p.id == user.roleid>selected="true"</#if>>${(p.text)!}</option>
								</#list>
							<#else>
								<option value="">无</option>
							</#if>
						</select>
					</div>
				</div>
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<div class="input-icon right col-md-4">
					<label class="">省</label>
						<select class="form-control select2me" name="province" id="province">
							<#if provinces ?? && provinces?size &gt; 0>
								<#list provinces as p>
									<option value="${(p.id)!}" <#if user.province?? && p.id == user.province>selected="true"</#if>>${(p.text)!}</option>
								</#list>
							<#else>
								<option value="">无</option>
							</#if>
						</select>
					</div>
					<div class="input-icon right col-md-4">
						<label class="">市</label>
						<select class="form-control select2me" name="district" id="district">
							<option value="-1" <#if user.district?? && '-1' == user.district>selected="true"</#if>>全部</option>
							<#if districts ?? && districts?size &gt; 0>
								<#list districts as p>
									<option value="${(p.id)!}" <#if user.district?? && p.id == user.district>selected="true"</#if>>${(p.text)!}</option>
								</#list>
							</#if>
						</select>
					</div>
					<div class="input-icon right col-md-4">
						<label class="">区/县</label>
						<select class="form-control select2me" name="county" id="county">
							<option value="-1" <#if user.county?? && '-1' == user.county>selected="true"</#if>>全部</option>
							<#if user.district?? && '-1' != user.district>
								<#if countys ?? && countys?size &gt; 0>
									<#list countys as p>
										<option value="${(p.id)!}" <#if user.county?? && p.id == user.county>selected="true"</#if>>${(p.text)!}</option>
									</#list>
								</#if>
							</#if>
						</select>
					</div>
				</div>
				<div class="form-group" style="padding:10px 0px 0px 0;">
					<label class="col-md-4" style="text-align:center;margin-top:15px;">是否启用</label>
					<div class="col-md-7">
						<div class="make-switch" data-on="success" data-off="danger" data-on-label="启用" data-off-label="禁用">
							<input id="islock" type="checkbox" <#if !((user.islock)??) || user.islock == 0>checked</#if> class="toggle"/>
						</div>
					</div>
				</div>
			</div>
			<div class="form-actions modal-footer">
				<button type="button" class="btn blue saveUser">确定</button>
				<button type="button" class="btn default" data-dismiss="modal">取消</button>
			</div>
		</form>
	</div>	
</div>
<!--开关按钮的JS(是否启用)-->
<script src="${application.getContextPath()}/scripts/plugins/bootstrap-switch/static/js/bootstrap-switch.min.js" type="text/javascript" ></script>