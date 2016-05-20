/**     
 * @文件名称: StringUtils.java  
 * @类路径: com.wireless.web.daoElastic.utils  
 * @描述: TODO  
 * @作者：dsk  
 * @时间：2015年11月30日 下午2:27:07  
 * @版本：V1.0     
 */
package com.wireless.web.daoElastic.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.wireless.web.utils.TimeUtils;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：
 * @作者：dsk
 * @创建时间：2015年11月30日 下午2:27:07
 * @版本：V1.0
 */
public class StringUtils {
	/**
	 * elasticsearch:指标区间设置用根据id获取kpi指标列名称
	 * 
	 * @param id
	 * @return 存储字段列名
	 */
	public static String getKpiName(Integer id) {
		String str = null;
		if (id == 1) {
			str = "lte_rsrp";
		} else if (id == 2) {
			str = "lte_snr";
		} else if (id == 3) {
			str = "lte_rsrq";
		} else if (id == 4) {
			str = "bw_link";
		} else if (id == 5) {
			str = "ping_avg_delay";
		} else if (id == 6) {
			str = "ping_lose_rate";
		}
		return str;
	}

	
	/**
	 * 
	* 函数功能说明   针对es的时间格式转化
	* tc  2015年12月11日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param str  时间字符串
	* @参数： @return   
	* @throws
	 */
	public static String findDate(String str) {
		String re = "";
		int idx = str.lastIndexOf(":");
		String dastr = str.substring(0, idx) + str.substring(idx + 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			re = formatter.format(sdf.parse(dastr));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}
	
	
	/**
	 * 
	* 函数功能说明 	处理返回查询ES时索引
	* tc  2015年12月11日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param queryTime   查询时间段
	* @参数： @param province	        一级区域编码
	* @参数： @return   
	* @throws
	 */
	public static List<String> findIndex4ES(String queryTime,String province){
		List<String> re = new ArrayList<String>();
		try {
			//分解一级区域
			String pro = province.substring(0,2);
			//分解开始和结束时间
			Map<String ,String> times = TimeUtils.getSETime(queryTime);
			String st = times.get("starttime").substring(0,7);
			String et = times.get("endtime").substring(0,7);
			int n = getIntervalMonth(st,et);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
			for(int i=0;i<n;i++){
				Calendar cl = Calendar.getInstance();
				cl.setTime(formatter.parse (st));
				cl.add(Calendar.MONTH, i);
				String t = formatter.format(cl.getTime()).replace("-", "");
				re.add(pro+"_"+t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}
	
	/**
	 * 
	* 函数功能说明 	处理返回查询ES时道路点中间抽取索引
	* tc  2015年12月11日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param queryTime   查询时间段
	* @参数： @param province	        一级区域编码
	* @参数： @return   
	* @throws
	 */
	public static List<String> findMidIndex4ES(String queryTime,String province){
		List<String> re = new ArrayList<String>();
		try {
			//分解一级区域
			String pro = province.substring(0,2);
			//分解开始和结束时间
			Map<String ,String> times = TimeUtils.getSETime(queryTime);
			String st = times.get("starttime").substring(0,7);
			String et = times.get("endtime").substring(0,7);
			int n = getIntervalMonth(st,et);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
			for(int i=0;i<n;i++){
				Calendar cl = Calendar.getInstance();
				cl.setTime(formatter.parse (st));
				cl.add(Calendar.MONTH, i);
				String t = formatter.format(cl.getTime()).replace("-", "");
				re.add("mid_"+pro+"_"+t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}
	
	
	/** 
	 * 起始年月yyyy-MM与终止月yyyy-MM之间跨度的月数。 
	 *  
	 * @param beginMonth 
	 *            格式为yyyy-MM 
	 * @param endMonth 
	 *            格式为yyyy-MM 
	 * @return 整数。 
	 */  
	public static int getIntervalMonth(String beginMonth, String endMonth) {  
	 int intBeginYear = Integer.parseInt(beginMonth.substring(0, 4));  
	 int intBeginMonth = Integer.parseInt(beginMonth.substring(beginMonth  
	   .indexOf("-") + 1));  
	 int intEndYear = Integer.parseInt(endMonth.substring(0, 4));  
	 int intEndMonth = Integer.parseInt(endMonth.substring(endMonth  
	   .indexOf("-") + 1));  
	  
	 return ((intEndYear - intBeginYear) * 12)  
	   + (intEndMonth - intBeginMonth) + 1;  
	}  
	
	/**
	 * 
	* 功能说明: 根据查询条件,获取ES type
	* 修改者名字: dsk
	* 修改日期 2015年12月14日
	* 修改内容 
	* @参数： @param telecoms
	* @参数： @return   
	* @throws
	 */
	public static List<String> findType4ES(String telecoms) {
		List<String> re = new ArrayList<String>();
		try {
			// 不为空
			if (null != telecoms && telecoms != "") {
				// 长度为1
				if (telecoms.length() == 1) {
					// 全部
					if (telecoms.equals("-1")) {
						re.add("1");
						re.add("2");
						re.add("3");
					} else {
						re.add(telecoms);
					}
					// 权限范围内,全部运营商(形如:010,第一位：移动；第二位：联通；第三位：电信)1,有0,无
				} else if (telecoms.length() == 3) {
					if (telecoms.substring(0, 1).equals("1")) {
						re.add("1");
					}
					if (telecoms.substring(1, 2).equals("1")) {
						re.add("2");
					}
					if (telecoms.substring(2, 3).equals("1")) {
						re.add("3");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}
}
