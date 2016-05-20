<div class="page-sidebar navbar-collapse collapse">
			<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
			<div class="sidebar-toggler hidden-phone"></div>
			<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
			<!-- BEGIN SIDEBAR MENU -->        
			<ul class="page-sidebar-menu">
				<li>
					<!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->
					<!--
					<form class="sidebar-search" action="extra_search.html" method="POST">
						<div class="form-container">
							<div class="input-box">
								<a href="javascript:;" class="remove"></a>
								<input type="text" placeholder="Search..."/>
								<input type="button" class="submit" value=" "/>
							</div>
						</div>
					</form>
					-->
					<!-- END RESPONSIVE QUICK SEARCH FORM -->
				</li>
					<#if Session.user_op_rights?? && Session.user_op_rights?size &gt; 0>
					<#list Session.user_op_rights as resource>
						<#if resource.parentid == 0>		
							<#if resource.subRight?? && resource.subRight?size &gt; 0>	
								<li class="<#if resource_index==0>start </#if><#if Session.user_op_rights?size == (resource_index+1)>last </#if><#if Session.user_op_active==resource.rightid || Session.user_op_parent_active==resource.rightid>active</#if>
								<#list resource.subRight as sub><#if Session.user_op_active==sub.rightid || Session.user_op_parent_active==sub.rightid>active</#if></#list>
								">
									<a href="javascript:;">
										<i class="fa <#if resource.skins??>${resource.skins}</#if>"></i> 
										<span class="title">${resource.rightname!}</span>
										<#if Session.user_op_active==resource.rightid || Session.user_op_parent_active==resource.rightid>
											<span class="selected"></span>
										</#if>
										<span class="arrow <#list resource.subRight as sub><#if Session.user_op_active==sub.rightid || Session.user_op_parent_active==sub.rightid>open</#if></#list>"></span>
									</a>
									<ul class="sub-menu" style="background-color:#464e78;">
										<#list resource.subRight as sub>
											<li class="<#if Session.user_op_active==sub.rightid || Session.user_op_parent_active==sub.rightid>active</#if>">
												<a href="${application.getContextPath()}${sub.url}?op_menu=${sub.rightid}">
												${sub.rightname!}</a>
											</li>
										</#list>
									</ul>
								</li>
							<#else>
								<li class="<#if resource_index==0>start </#if><#if Session.user_op_rights?size == resource_index>last </#if><#if Session.user_op_active==resource.rightid || Session.user_op_parent_active==resource.rightid>active</#if>" >							
									<a href="${application.getContextPath()}${resource.url}?op_menu=${resource.rightid}">
										<i class="fa <#if resource.skins??>${resource.skins}</#if>"></i> 
										<span class="title">${resource.rightname!}</span>
										<#if Session.user_op_active==resource.rightid || Session.user_op_parent_active==resource.rightid>
											<span class="selected"></span>
										</#if>
									</a>							
								</li>
							</#if>									
						</#if>						
					</#list>
				</#if>
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>