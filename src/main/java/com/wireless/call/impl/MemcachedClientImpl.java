package com.wireless.call.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.schooner.MemCached.MemcachedItem;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import com.wireless.call.MemcachedClient;


public class MemcachedClientImpl implements MemcachedClient{
	private static Logger logger = LoggerFactory.getLogger(MemcachedClient.class);

	protected static MemCachedClient mcc = new MemCachedClient();

	static {
		try {
			MemCacheConfig config = new MemCacheConfig();
			// 需要初始化
			config.initcfg();

			String addr = config.getServerAddr();
			String[] servers = addr.split(",");

			String[] weights = config.getServerWeight().split(",");
			Integer[] wt = new Integer[weights.length];
			for (int i = 0; i < weights.length; i++) {
				wt[i] = new Integer(weights[i]);
			}
			// 获取连接处实例
			SockIOPool pool = SockIOPool.getInstance();

			// 设置服务器及权重
			pool.setServers(servers);
			pool.setWeights(wt);
			// 设置连接参数
			pool.setInitConn(new Integer(config.getInitConn()));
			pool.setMinConn(new Integer(config.getMinConn()));
			pool.setMaxConn(new Integer(config.getMaxConn()));
			pool.setMaxIdle(new Integer(config.getMaxIdle()));

			pool.setMaintSleep(new Long(config.getMainSleep()));

			pool.setNagle(new Boolean(config.getIsNagle()));
			pool.setSocketTO(new Integer(config.getSocketReadTimeout()));
			pool.setSocketConnectTO(new Integer(config.getSocketConnTimeout()));

			// 初始化连接池
			pool.initialize();
		} catch (Exception ex) {
			logger.error("MemCache 初始化配置失败，原因：{}", ex.getMessage());
		}
	}

	/**
	 * @return true: 成功 false:失败
	 */
	@Override
	public boolean set(String key, Object value) {
		return mcc.set(key, value);
	}

	@Override
	public boolean set(String key, Object value, Integer hashCode) {
		return mcc.set(key, value, hashCode);
	}

	@Override
	public boolean set(String key, Object value, Date expiry) {
		return mcc.set(key, value, expiry);
	}

	@Override
	public Object get(String key) {
		return mcc.get(key);
	}

	@Override
	public Object get(String key, Integer hashCode) {
		return mcc.get(key, hashCode);
	}

	@Override
	public boolean del(String key) {
		return mcc.delete(key);
	}

	@Override
	public boolean flushAll() {
		return mcc.flushAll();
	}

	@Override
	public boolean keyExists(String key) {
		return mcc.keyExists(key);
	}

   /* (non-Javadoc)
 * @see cn.poweropt.cms.call.impl.MemcachedClient#gets(java.lang.String)
 */
	@Override
	public SyncObj gets(String key) {
		MemcachedItem mi = mcc.gets(key);
		SyncObj so = new SyncObj();
	   if(null !=  mi)
	   {
		   so.setUniqueId(mi.casUnique);
		   so.setValue(mi.getValue());
		   so.setKey(key);
		   
	   }
		return so;
	}
	
	/* (non-Javadoc)
	 * @see cn.poweropt.cms.call.impl.MemcachedClient#cas(cn.poweropt.cms.call.impl.MemcachedClientImpl.SyncObj)
	 */
	@Override
	public boolean cas( SyncObj so)
	{
		return mcc.cas(so.getKey(), so.getValue(), so.getUniqueId());
	}
	
	
	public static class SyncObj
	{
		private String key;
		
		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}
		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}
		private long uniqueId;
		/**
		 * @return the uniqueId
		 */
		public long getUniqueId() {
			return uniqueId;
		}
		/**
		 * @param uniqueId the uniqueId to set
		 */
		public void setUniqueId(long uniqueId) {
			this.uniqueId = uniqueId;
		}
		/**
		 * @return the value
		 */
		public Object getValue() {
			return value;
		}
		/**
		 * @param value the value to set
		 */
		public void setValue(Object value) {
			this.value = value;
		}
		private Object value;
	}
}
