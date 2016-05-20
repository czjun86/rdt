package com.wireless.ability.schedual.context.impl;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.digester3.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import com.wireless.ability.schedual.config.Task;
import com.wireless.ability.schedual.config.TasksConfig;
import com.wireless.ability.schedual.context.ConfigContainer;


/**
 * 配置容器实现
 * @author tian.bo
 */
public class ConfigContainerImpl implements ConfigContainer, ServletContextAware,
        ApplicationContextAware {
	
    private static Logger log = LoggerFactory.getLogger(ConfigContainerImpl.class);

    private ApplicationContext applicationContext;
    private ServletContext servletContext;
    private Digester tasksDigester = new Digester();

    /**
     * init
     */
    public void init() throws Exception {
        initTasksConfigs();
    }
    
    /**
     * 初始化任务配置
     */
    private void initTasksConfigs() {
        tasksDigester.addObjectCreate("tasks", TasksConfig.class);
        tasksDigester.addSetProperties("tasks");
        tasksDigester.addObjectCreate("tasks/task", Task.class);
        tasksDigester.addSetProperties("tasks/task");
        tasksDigester.addCallMethod("tasks/task", "setInParam", 1);
        tasksDigester.addCallParam("tasks/task", 0);
        tasksDigester.addSetNext("tasks/task", "addTask", Task.class.getName());
    }

    /**
     * 初始化任务配置
     * @param    path
     *               配置文件所在路径
     * @param    fileName
     *               文件名称
     * @return   file
     *               需要的配置文件           
     */
    public File getConfigFile(String path, String fileName) {
        File file =
                new File(servletContext.getRealPath(String.format("/%s/%s", path,
                		fileName)));
        if (!file.exists()) {
           log.error("File does not exist.");
           return null;
        }
        return file;
    }

    public TasksConfig getTasksConfig() {
        TasksConfig config = null;
        if (config == null || config.isModified()) {
            boolean fileFound = config != null;
            File tasksFile =
                    fileFound ? config.getConfigFile() : getConfigFile("/WEB-INF/classes", ConfigContainer.TASK_CONFIG_PATH);
            if (!tasksFile.exists()) {
                return null;
            }

            log.info("parse tasksConfig");
            try {
            	config = (TasksConfig) tasksDigester.parse(tasksFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            config.setConfigFile(tasksFile);
            config.setTimestamp(tasksFile.lastModified());
        }
        return config;
    }
    
	public void setApplicationContext(ApplicationContext applicationcontext)
			throws BeansException {
		this.applicationContext = applicationcontext;
	}


	public void setServletContext(ServletContext servletcontext) {
		this.servletContext = servletcontext;
	}

	public ServletContext getServletContext() {
		return this.servletContext;
	}
	
}
