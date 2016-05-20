package com.wireless.ability.schedual.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 任务配置实体
 * @author tian.bo
 *
 */
public class TasksConfig {
	private boolean enable;
	private List<Task> tasks = new ArrayList<Task>();
	private long timestamp;
	private File configFile;
	private String appName;
	
	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public File getConfigFile() {
		return this.configFile;
	}

	public boolean isModified() {
		return this.configFile.lastModified() > timestamp;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
}
