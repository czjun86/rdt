package com.wireless.ability.schedual.config;
/**
 * 
 * 任务实体
 * @author tian.bo
 *
 */
public class Task {
	
	private String path;
	private String cron;
	private String inParam;
	private String async;
	private String locale;
	private String modul;
	private String execute;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getInParam() {
		return inParam;
	}

	public void setInParam(String inParam) {
		this.inParam = inParam;
	}

	public String getAsync() {
		return async;
	}

	public void setAsync(String async) {
		this.async = async;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getModul() {
		return modul;
	}

	public void setModul(String modul) {
		this.modul = modul;
	}

	public String getExecute() {
		return execute;
	}

	public void setExecute(String execute) {
		this.execute = execute;
	}	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{path=").append(path).append(",cron=").append(cron)
				.append(",async=").append(async).append(",inParam=")
				.append(inParam).append(",modul=").append(modul).append(",execute=")
				.append(execute).append('}');
		return sb.toString();
	}

}
