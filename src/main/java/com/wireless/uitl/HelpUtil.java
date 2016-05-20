/**     
* @文件名称: HelpUtil.java  
* @类路径: com.wireless.uitl  
* @描述: TODO  
* @作者：tc  
* @时间：2015年12月8日 下午6:32:24  
* @版本：V1.0     
*/
package com.wireless.uitl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  
 * @类功能说明：  
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者：tc  
 * @创建时间：2015年12月8日 下午6:32:24  
 * @版本：V1.0  
 */
public class HelpUtil {
	private static Logger logger = LoggerFactory.getLogger(HelpUtil.class);
	
	public static Properties readProperties(String path){
		Properties prop = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("读取配置文件报错:", e);
		}
		return prop;
	}
}
