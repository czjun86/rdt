package com.wireless.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;




public class CustomServletContextListener implements ServletContextListener {
	/**
	 * 用户监听servlet启动时
	 */
	protected ServletContext servletContext;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//启动加入监听
	}
	
	protected ServletContext getServletContext(){
		return servletContext;
	}

}
