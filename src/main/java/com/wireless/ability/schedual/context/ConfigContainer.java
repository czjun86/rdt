package com.wireless.ability.schedual.context;
import javax.servlet.ServletContext;

import com.wireless.ability.schedual.config.TasksConfig;



/**
 * 配置容器接口
 * @author tian.bo
 */
public interface ConfigContainer {
	
	//任务配置文件
	public static final String TASK_CONFIG_PATH = "app.task.xml";
    
    /**
     * 获取任务配置
     * 
     * @return 任务配置
     */
    TasksConfig getTasksConfig();
    
    /**
     * 获取ServletContext上下文
     * 
     * @return ServletContext上下文
     */
    ServletContext getServletContext();

}
