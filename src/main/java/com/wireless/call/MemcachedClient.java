package com.wireless.call;

import java.util.Date;

import com.wireless.call.impl.MemcachedClientImpl.SyncObj;




/**
 * memcache客户端接口
* @类修改者：  
* @修改日期：  
* @修改说明：  
* @公司名称：  
* @作者： liuxianjin@poweropt.cn
* @创建时间：2015年1月12日 下午6:01:58  
* @版本：V1.0
 */
public interface MemcachedClient {

	public boolean set(String key, Object value) ;
	public boolean set(String key, Object value, Integer hashCode);
	public boolean set(String key, Object value, Date expiry);
	
	public Object get(String key);
	public Object get(String key, Integer hashCode);
	
	public boolean del(String key);

	public boolean flushAll();
	public boolean keyExists(String key);
	/**
	 * memcached 悲观锁实现版本，调用gets函数，同时返回value版本信息
	 * 函数功能说明 
	 * 创建者：liuxianjin@poweropt.cn 
	 * 创建时间： 2015年11月12日 
	 * 修改者名字修改日期：
	 * 修改内容：
	 * @参数： @param key
	 * @参数： @return   SyncObj
	 * @throws
	 */
	public abstract SyncObj gets(String key);

	/**
	 * 
	 * 函数功能说明 分布式环境下保证数据不产生脏读等情况
	 * 创建者：liuxianjin@poweropt.cn 
	 * 创建时间： 2015年11月12日 
	 * 修改者名字修改日期：
	 * 修改内容：
	 * @参数： @param so 调用方保证so各属性不能为null
	 * @参数： @return   false：写失败，客户端可以考虑重试或放弃本次修改
	 * @throws
	 */
	public abstract boolean cas(SyncObj so);	
}
