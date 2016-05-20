/**     
* @文件名称: NumUtils.java  
* @类路径: com.wireless.web.utils  
* @描述: TODO  
* @作者：tc  
* @时间：2015年12月4日 下午5:34:39  
* @版本：V1.0     
*/
package com.wireless.web.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.math.NumberUtils;

/**  
 * @类功能说明：  数字工具类
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者：tc  
 * @创建时间：2015年12月4日 下午5:34:39  
 * @版本：V1.0  
 */
public class NumUtils {
	/**
	 * 
	* 函数功能说明   4 舍5入 保留n位小数
	* tc  2015年12月4日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param num  处理数值
	* @参数： @param n  保留小数位数
	* @参数： @return   
	* @throws
	 */
	public static double resDouble(double num,int n){
		BigDecimal b = new BigDecimal(num); 
		double f1 = b.setScale(n,BigDecimal.ROUND_HALF_UP).doubleValue(); 
		return f1;
	}
	
	/**
	 * 
	* 函数功能说明    判断是否为数值类型的对象
	* tc  2015年12月7日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param j
	* @参数： @return   
	* @throws
	 */
	public static boolean isNumber(Object j){
		
		return NumberUtils.isNumber(j.toString());
	}
	
	/**
	 * 
	* 功能说明: 格式化数字
	* 修改者名字: dsk
	* 修改日期 2015年12月28日
	* 修改内容 
	* @参数： @param num
	* @参数： @param pattern
	* @参数： @return   
	* @throws
	 */
	public static String resNumber(Number num,String pattern){
		DecimalFormat df = new DecimalFormat(pattern);
		String format = df.format(num);
		return format;
	}
	
}
