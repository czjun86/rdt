package com.wireless.core.dwr;

import java.util.List;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.filter.AuditLogAjaxFilter;
import org.directwebremoting.impl.DefaultAjaxFilterManager;
/**
 * <p>
 * 文件名称: CustomAjaxFilterManager.java
 * </p>
 * <p>
 * 文件描述: 本类描述
 * </p>
 * <p>
 * 版权所有: 版权所有(C)2014
 * </p>
 * <p>
 * 公 司: wyl Corporation
 * </p>
 * <p>
 * 内容摘要:
 * </p>
 * <p>
 * 其他说明:
 * </p>
 * <p>
 * 完成日期：2014-2-14
 * </p>
 * <p>
 * 修改记录0：无
 * </p>
 * 
 * @version 1.0
 * @author tian.bo
 */
public class CustomAjaxFilterManager implements AjaxFilterManager {

	
	private AjaxFilterManager ajaxFilterManager;
	
	public CustomAjaxFilterManager(){
		ajaxFilterManager = new DefaultAjaxFilterManager();
		ajaxFilterManager.addAjaxFilter(new AuditLogAjaxFilter());
	}
	
	public void addAjaxFilter(AjaxFilter filter) {
		this.ajaxFilterManager.addAjaxFilter(filter);
	}

	public void addAjaxFilter(AjaxFilter filter, String scriptName) {
		this.ajaxFilterManager.addAjaxFilter(filter, scriptName);
	}

	public List<AjaxFilter> getAjaxFilters(String scriptName) {
		return this.ajaxFilterManager.getAjaxFilters(scriptName);
	}

}
