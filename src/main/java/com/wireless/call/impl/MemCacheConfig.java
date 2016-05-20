/**     
* @文件名称: MemeCacheConfig.java  
* @类路径: cn.poweropt.cms.call.impl  
* @描述: TODO  
* @作者：liuxianjin@poweropt.cn
* @时间：2015年1月13日 下午1:40:03  
* @版本：V1.0     
*/
package com.wireless.call.impl;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wireless.uitl.HelpUtil;

/**  
 * MemCache配置
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者： liuxianjin@poweropt.cn
 * @创建时间：2015年1月13日 下午1:40:03  
 * @版本：V1.0  
 */
public class MemCacheConfig {

	private String initConn;
	private String minConn;
	private String maxConn;
	private String maxIdle;
	private String mainSleep;
	private String isNagle;
	private String socketReadTimeout;
	private String socketConnTimeout;
	private String bufferSize;
	private String serverAddr;
	private String serverWeight;
	
	/**
	 * 
	* 初始化配置
	* 创建者：liuxianjin@poweropt.cn 
	* 创建时间： 2015年1月13日 
	* 修改者名字修改日期：
	* 修改内容：
	* @参数：    
	* @throws
	 */
	public void initcfg()
	{
		//读取默认配置文件 mem-default-config.properties  cn.poweropt.cms.call.impl
		//读取配置
		String path = "config/mem-default-config.properties";
		Properties poper = HelpUtil.readProperties(path);
		setInitConn(poper.getProperty("init-conn"));
		setMinConn(poper.getProperty("min-conn"));
		setMaxConn(poper.getProperty("max-conn"));
		setMaxIdle(poper.getProperty("max-idle"));
		setMainSleep(poper.getProperty("main-sleep"));
		setIsNagle(poper.getProperty("is-nagle"));
		setSocketReadTimeout(poper.getProperty("socket-read-timeout"));
		setSocketConnTimeout(poper.getProperty("socket-conn-timeout"));
		setBufferSize(poper.getProperty("buffer-size"));
		setServerAddr(poper.getProperty("server-addr"));
		setServerWeight(poper.getProperty("server-weight"));
	}
	
	/**
	 * @return the initConn
	 */
	public String getInitConn() {
		return initConn;
	}
	/**
	 * @param initConn the initConn to set
	 */
	public void setInitConn(String initConn) {
		this.initConn = initConn;
	}
	/**
	 * @return the minConn
	 */
	public String getMinConn() {
		return minConn;
	}
	/**
	 * @param minConn the minConn to set
	 */
	public void setMinConn(String minConn) {
		this.minConn = minConn;
	}
	/**
	 * @return the maxConn
	 */
	public String getMaxConn() {
		return maxConn;
	}
	/**
	 * @param maxConn the maxConn to set
	 */
	public void setMaxConn(String maxConn) {
		this.maxConn = maxConn;
	}
	/**
	 * @return the maxIdle
	 */
	public String getMaxIdle() {
		return maxIdle;
	}
	/**
	 * @param maxIdle the maxIdle to set
	 */
	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}
	/**
	 * @return the mainSleep
	 */
	public String getMainSleep() {
		return mainSleep;
	}
	/**
	 * @param mainSleep the mainSleep to set
	 */
	public void setMainSleep(String mainSleep) {
		this.mainSleep = mainSleep;
	}
	/**
	 * @return the isNagle
	 */
	public String getIsNagle() {
		return isNagle;
	}
	/**
	 * @param isNagle the isNagle to set
	 */
	public void setIsNagle(String isNagle) {
		this.isNagle = isNagle;
	}
	/**
	 * @return the socketReadTimeout
	 */
	public String getSocketReadTimeout() {
		return socketReadTimeout;
	}
	/**
	 * @param socketReadTimeout the socketReadTimeout to set
	 */
	public void setSocketReadTimeout(String socketReadTimeout) {
		this.socketReadTimeout = socketReadTimeout;
	}
	/**
	 * @return the socketConnTimeout
	 */
	public String getSocketConnTimeout() {
		return socketConnTimeout;
	}
	/**
	 * @param socketConnTimeout the socketConnTimeout to set
	 */
	public void setSocketConnTimeout(String socketConnTimeout) {
		this.socketConnTimeout = socketConnTimeout;
	}
	/**
	 * @return the bufferSize
	 */
	public String getBufferSize() {
		return bufferSize;
	}
	/**
	 * @param bufferSize the bufferSize to set
	 */
	public void setBufferSize(String bufferSize) {
		this.bufferSize = bufferSize;
	}
	/**
	 * @return the serverAddr
	 */
	public String getServerAddr() {
		return serverAddr;
	}
	/**
	 * @param serverAddr the serverAddr to set
	 */
	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	/**
	 * @return the serverWeight
	 */
	public String getServerWeight() {
		return serverWeight;
	}
	/**
	 * @param serverWeight the serverWeight to set
	 */
	public void setServerWeight(String serverWeight) {
		this.serverWeight = serverWeight;
	}


}
