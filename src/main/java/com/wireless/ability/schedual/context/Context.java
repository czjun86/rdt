package com.wireless.ability.schedual.context;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

/**
 * 执行上下文
 * 
 * @author tian.bo
 */
public class Context {

	private ApplicationContext applicationContext;
	private ServletContext servletContext;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
