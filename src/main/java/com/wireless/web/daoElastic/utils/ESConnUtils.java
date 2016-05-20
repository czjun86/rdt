/**     
 * @文件名称: ConnUtils.java  
 * @类路径: com.wireless.web.daoElastic.utils  
 * @描述: TODO  
 * @作者：dsk  
 * @时间：2015年11月30日 下午2:28:00  
 * @版本：V1.0     
 */
package com.wireless.web.daoElastic.utils;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wireless.uitl.HelpUtil;

/**
 * @类功能说明：
 * @类修改者：TC
 * @修改日期：2015.12.08
 * @修改说明：修改为读取配置文件
 * @公司名称：
 * @作者：dsk
 * @创建时间：2015年11月30日 下午2:28:00
 * @版本：V1.0
 */
public class ESConnUtils {
	// 滚屏时长(ms)
	public static long ES_SCNA_TIME = 60000;
	public static int ES_SCNA_SIZE = 1000;
	
	private static final Logger logger = LoggerFactory
			.getLogger(ESConnUtils.class);
	
	private static  TransportClient client;
	
	private static Object lock = new Object();

	
//	static {
//		synchronized(lock){
//			if(client==null){
//				load();
//			}
//		}
//	}
	
	private static void load(){
		try {
			//读取配置
			String path = "config/esParams.properties";
			Properties poper = HelpUtil.readProperties(path);
			//设置参数
			ES_SCNA_TIME = poper!=null?NumberUtils.toInt(poper.getProperty("es_scna_time")):60000;
			ES_SCNA_SIZE = poper!=null?NumberUtils.toInt(poper.getProperty("es_scna_size")):1000;
			//设置client参数
			Settings settings = Settings.settingsBuilder().put("cluster.name", poper.getProperty("cluster.name"))
								.put("client.transport.ping_timeout", poper.getProperty("client.transport.ping_timeout"))
								.put("client.transport.sniff", poper.getProperty("client.transport.sniff")).build();
			client = TransportClient.builder().settings(settings).build();
			//获取节点信息
			int flg = 1;//循环结束标记
			for(int i =1;flg==1;i++){
				String ip = poper.getProperty("transportAddress_"+i);//IP
				String port = poper.getProperty("transportPort_"+i);//端口
				if(ip!=null&&!ip.equals("")&&port!=null&&!port.equals("")){
					client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), NumberUtils.toInt(port)));
				}else{
					flg = -1;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载Elasticsearch client 失败！", e);
		}
	}
	

	// 取得实例
	public static TransportClient getTransportClient() {
		synchronized(lock){
			if(client==null){
				load();
			}
		}
		return client;
	}
	
	/**
	 * 
	* 函数功能说明 
	* tc  2015年12月21日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param indexs   索引集合
	* @参数： @param indexType  索引类型集合
	* @参数： @return   
	* @throws
	 */
	public static String querESByJson(List<String> indexs,String indexType,String queryJson){
		
		Runtime run = Runtime.getRuntime();

		long max = run.maxMemory()/1024/1024;

		long total = run.totalMemory()/1024/1024;

		long free = run.freeMemory()/1024/1024;

		long usable = max - total + free;

		System.out.println("最大内存 (M)= " + max);
		System.out.println("已分配内存 (M)= " + total);
		System.out.println("已分配内存中的剩余空间 (M)= " + free);
		System.out.println("最大可用内存 =(M) " + usable);
		
		System.out.println("查询ES语句:"+queryJson);
		String re ="";
		long stLong = new Date().getTime();
		try {
			//读取配置
			String path = "config/esParams.properties";
			Properties poper = HelpUtil.readProperties(path);
			
			//设置连接字符串
			/**设置IP和端口**/
			StringBuffer conn = new StringBuffer("http://");
			conn.append(poper.getProperty("headAddress"));
			conn.append(":");
			conn.append(poper.getProperty("headPort"));
			conn.append("/");
			/**设置索引**/
			StringBuffer indexString = new StringBuffer();
			for(int i=0;i<indexs.size();i++){
				indexString.append(indexs.get(i));
				indexString.append(",");
			}
			if(indexs.size()>0){
				//去掉最后一个逗号
				String tem = indexString.toString();
				conn.append(tem.substring(0, tem.length()-1));
				//添加间隔
				conn.append("/");
			}
			/**设置索引类型**/
			if(indexType!=null){
				conn.append(indexType);
				conn.append("/");
			}
			
			conn.append("_search");
			//开始查询
			URL url = new URL(conn.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
			connection.setRequestMethod("POST");
			// Post 请求不能使用缓存
	        connection.setUseCaches(false);
	        //发送Post强求，开启其读写的功能
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Connection", "keep-alive");
			//发送Post请求
			connection.connect();
	        //设置参数
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
	        //发送参数
	        writer.write(queryJson);
	        writer.flush();
	        writer.close();
           
            // 获取输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder sl = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null) {// 循环读取流
//            	sl.append(line);
//            }
//            br.close();// 关闭流
            
            long edLong = new Date().getTime();
    		System.out.println("查询时间:"+(edLong-stLong));
            
            String lines = IOUtils.toString(br);
            
            
            connection.disconnect();// 断开连接
            re = lines;
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("查询ES返回结果:"+re);
		
		return re;
	}
	
	
	/**
	 * 
	* 函数功能说明 
	* tc  2015年12月21日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param indexs   索引集合
	* @参数： @param indexType  索引类型集合
	* @参数： @return   
	* @throws
	 */
	public static InputStream querESByJson2(List<String> indexs,String indexType,String queryJson){
		InputStream re = null;
		try {
			//读取配置
			String path = "config/esParams.properties";
			Properties poper = HelpUtil.readProperties(path);
			
			//设置连接字符串
			/**设置IP和端口**/
			StringBuffer conn = new StringBuffer("http://");
			conn.append(poper.getProperty("headAddress"));
			conn.append(":");
			conn.append(poper.getProperty("headPort"));
			conn.append("/");
			/**设置索引**/
			StringBuffer indexString = new StringBuffer();
			for(int i=0;i<indexs.size();i++){
				indexString.append(indexs.get(i));
				indexString.append(",");
			}
			if(indexs.size()>0){
				//去掉最后一个逗号
				String tem = indexString.toString();
				conn.append(tem.substring(0, tem.length()-1));
				//添加间隔
				conn.append("/");
			}
			/**设置索引类型**/
			if(indexType!=null){
				conn.append(indexType);
				conn.append("/");
			}
			
			conn.append("_search");
			//开始查询
			URL url = new URL(conn.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
			connection.setRequestMethod("POST");
			// Post 请求不能使用缓存
	        connection.setUseCaches(false);
	        //发送Post强求，开启其读写的功能
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Connection", "keep-alive");
			//发送Post请求
			connection.connect();
	        //设置参数
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
	        //发送参数
	        writer.write(queryJson);
	        writer.flush();
	        writer.close();
           
            // 获取输入流
	        re = connection.getInputStream();
	        
	        connection.disconnect();// 断开连接
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return re;
	}

}
