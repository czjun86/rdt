package com.wireless.web.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import com.wireless.uitl.Constant;

public class StringUtils {
	/**
	 * 创建文件路径
	 */
	public static void makeFile(String path){
		File filePath = new File(path);
		if(!filePath.exists()&&!filePath.isDirectory())  {
			filePath.mkdirs(); 
		}
	}
	
	/**
	 * 对应网络类型
	 */
	public static String netType(Integer i){
		String str = "";
		if(i==-1){
			str = "UNKNOWN";
		}else if(i==0){
			str = "UNKNOWN";
		}else if(i==1){
			str = "GPRS";
		}else if(i==2){
			str = "EDGE";
		}else if(i==3){
			str = "UMTS";
		}else if(i==4){
			str = "CDMA";
		}else if(i==5){
			str = "EVDO_0";
		}else if(i==6){
			str = "EVDO_A";
		}else if(i==7){
			str = "1xRTT";
		}else if(i==8){
			str = "HSPA";
		}else if(i==9){
			str = "HSPA";
		}else if(i==10){
			str = "HSPA";
		}else if(i==11){
			str = "IDEN";
		}else if(i==12){
			str = "EVDO_B";
		}else if(i==13){
			str = "LTE";
		}else if(i==14){
			str = "EHRPD";
		}else if(i==15){
			str = "HSPAP";
		}else{
			str = "UNKNOWN";
		}
		return str;
	}
	
	/**
	 * 处理模糊查询时的特殊字符
	 */
	
	public static String fuzzyQueryStr(String str){
		if(str!=null&&!(str.equals(""))){
			str=str.replaceAll("\\%", "\\\\%");
			str=str.replaceAll("\\_", "\\\\_");
		}
		return str;
	}
	
	/**
	 * 计算中英文混合的字节长度
	 * @throws Exception 
	 */
	public static Integer strByte(String str) throws Exception{
		if(str!=null&&!("".equals(str))){
			str=new String(str.getBytes("gb2312"),"iso-8859-1");
			return str.length();
		}else{
			return 0;
		}
	}
	
	public static Map<String, Object> subTime(Map<String, Object> map ,String queryTime ,Integer pageIndex ,Integer pageSize,String flag){
		if(queryTime!=null&&!queryTime.equals("")){
			//加工查询时间
			String st="";
			String et="";
			st=queryTime.split("-")[0];
			et=queryTime.split("-")[1];
			if(flag!=null && !"".equals(flag) && !"-1".equals(flag) ){
				map.put("ticketStartTime", new StringBuffer().append(st.substring(0,st.length()-1)).toString());
				map.put("ticketEndTime",  new StringBuffer().append(et.substring(1,et.length())).toString());
			}else if("-1".equals(flag)){
				map.put("ticketStartTime", new StringBuffer().append(st.substring(0,st.length()-1)).toString()+":00");
				map.put("ticketEndTime",  new StringBuffer().append(et.substring(1,et.length())).toString()+":59");
			}else{
				map.put("ticketStartTime", new StringBuffer().append(st.replaceAll(" ", "")).append(" 00:00:00").toString());
				map.put("ticketEndTime",  new StringBuffer().append(et.replaceAll(" ", "")).append(" 23:59:59").toString());
			}
			map.put("queryTime", map.get("ticketStartTime").toString().substring(0,10)+" - "+ map.get("ticketEndTime").toString().substring(0,10));
		}else{
			map.put("queryTime",null);
		}
		map.put("pageIndex", pageIndex==null?1:pageIndex);
		map.put("pageSize", pageSize==null?Constant.PAGESIZE:pageSize);
		
		return map;
	}
	
	/**
	 * 
	* 函数功能说明    获取一个自定义的时间段
	* tc  2015年8月20日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param st 开始时间前移天数
	* @参数： @param et 结束时间前移天数
	* @参数： @return   
	* @throws
	 */
	public static String getTime(int st,int et){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		
		Calendar cal =Calendar.getInstance();//的到日历
		cal.setTime(date);//让入时间
		cal.add(Calendar.DAY_OF_MONTH, -et);//设置偏移量
		Date ydate = cal.getTime();
		String yesterday = sdf.format(ydate);  //设置偏移量
		
		Calendar cal2 =Calendar.getInstance();//的到日历
		cal2.setTime(date);//让入时间
		cal2.add(Calendar.DAY_OF_MONTH, -st);//设置偏移量
		Date ldate = cal2.getTime();
		String longdate = sdf.format(ldate);  //设置偏移量
		
		return longdate + " - " +yesterday;
	
	}
	
	public static String getTimeSS(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		String now = sdf.format(new Date());//今天
		
		Calendar cal =Calendar.getInstance();//的到日历
		cal.setTime(date);//让入时间
		cal.add(Calendar.DAY_OF_MONTH, -1);//设置为前一天
		Date ydate = cal.getTime();
		String yesterday = sdf.format(ydate);  //设置为前一天
		
		return yesterday + " 00:00 - " +now+" 23:59";
	
	}
	
	/**
	 * 获取前七天或前七个月的开始结束时间
	 * flag  判断返回     2天   3月
	 * time  传入时间  差前七天
	 * @param flag
	 * @return
	 */
	public static Map<String , Object> getSevenTime(String flag ,String time){
		Map<String , Object > map = new HashMap<String , Object>();
		if(time == null){
			if("month".equals(flag)){
				DateFormat  df = new SimpleDateFormat("yyyy-MM");
				Date date = new Date();
				Calendar c1 = Calendar.getInstance();
				c1.setTime(date);
				c1.add(Calendar.MONTH, -7);
				Calendar c2 = Calendar.getInstance();
				c2.setTime(date);
				c2.add(Calendar.MONTH, 0);
				map.put("starttime", df.format(c1.getTime())+"-01");
				map.put("endtime", df.format(c2.getTime())+"-01");
			}else{
				DateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				Calendar c1 = Calendar.getInstance();
				c1.setTime(date);
				c1.add(Calendar.DATE, -7);
				Calendar c2 = Calendar.getInstance();
				c2.setTime(date);
				c2.add(Calendar.DATE, -1);
				map.put("starttime", df.format(c1.getTime()));
				map.put("endtime", df.format(c2.getTime()));
			}
		}else{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			DateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
				date=sdf.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, -6);
			map.put("starttime", df.format(c.getTime()));
			map.put("endtime", time);
		}
		return map;
	}
	
	/**
	 * 根据传入月份获取下个月
	 * @param time
	 * @return
	 * @throws ParseException 
	 */
	public static String getNextMonth(String time) throws Exception{
		if(time.length()>7){
			time = time.substring(0,7);
		}
		DateFormat  df = new SimpleDateFormat("yyyy-MM");
		Date date= df.parse(time);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		return df.format(c.getTime());
	}
	
	/**
	 * 根据传入月份获取上个月
	 * @param time
	 * @return
	 * @throws ParseException 
	 */
	public static String getUpperMonth(String time) throws ParseException{
		if(time.length()>7){
			time = time.substring(0,7);
		}
		DateFormat  df = new SimpleDateFormat("yyyy-MM");
		Date date= df.parse(time);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		return df.format(c.getTime());
	}
	
	public static Map<String ,Object> stbTime(Map<String ,Object> map ,String querytime){
		if(querytime != null && !"".equals(querytime)){
			String[] str = querytime.split("-");
			map.put("starttime", str[0].trim().replaceAll("\\.", "-"));
			map.put("endtime", str[1].trim().replaceAll("\\.", "-"));
		}
		return map;
	}
	
	/**
	 * 根据传入时间获取包括时间内前6天时间
	 * @param time
	 * @return
	 */
	public static List<String> getSevenDate(String time){
		List<String> times = new ArrayList<String>();
		
		DateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		try {
			date=df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		for(int i = 6 ;i>=0 ;i--){
			c.setTime(date);
			c.add(Calendar.DATE, -i);
			times.add(df.format(c.getTime()));
		}
		return times;
		
	}
	
	/**
	 * 判断ID是否是直辖市
	 * boolean  false不是直辖市  true 是直辖市
	 * @param id
	 * @return
	 */
	public static boolean decideCity(String id){
		if(
			!"110000".equals(id)//北京市
			&&!"120000".equals(id)//天津市
			&&!"310000".equals(id)//上海市
			&&!"500000".equals(id)//重庆市
		){
			return false;
		}else{
			return true;
		}	
	}

	/**
	 * 四大运营商
	 * @return
	 */
	public static List<String> getOperator(){
		List<String> list = new ArrayList<String>();
		list.add("联通");
		list.add("移动");
		list.add("电信");
		//list.add("其他");
		return list;
	}
	
	/**
	 * 根据运营商权限获取运营商
	 */
	public static List<String> getOperator(String teleAuth){
		List<String> list = new ArrayList<String>();
		if(teleAuth!=null && !"".equals(teleAuth)){
			if("1".equals(teleAuth.substring(0,1))){
				list.add("联通");
			}
			if("1".equals(teleAuth.substring(1,2))){
				list.add("移动");
			}
			if("1".equals(teleAuth.substring(2,3))){
				list.add("电信");
			}
		}
		return list;
	}
	
	/**
	 * 转译运营商更具名字转id
	 * @param str
	 * @return
	 */
	public static Integer getOperatorId(String str){
		int i = -1;
		if("联通".equals(str)){
			i = 1;
		}else if("移动".equals(str)){
			i = 2;
		}else if("电信".equals(str)){
			i = 3;
		}else if("其他".equals(str)){
			i = 4;
		}
		return i;
	}
	
	/**
	 * 转译运营商更具名字转id
	 * @param str
	 * @return
	 */
	public static String getOperatorName(String id){
		String str = "未知";
		if("1".equals(id)){
			str = "联通";
		}else if("2".equals(id)){
			str = "移动";
		}else if("3".equals(id)){
			str = "电信";
		}else if("4".equals(id)){
			str = "其他";
		}
		return str;
	}
	
	/**
	 * 四大运营商 中文转译
	 * @return
	 */
	public static String getOperatorIdToString(String name){
		String str = null;
		if("联通".equals(name)){
			str = "1";
		}else if("移动".equals(name)){
			str = "2";
		}else if("电信".equals(name)){
			str = "3";
		}else if("其他".equals(name)){
			str = "4";
		}
		return str;
	}
	
	/**
	 * id对应kpi指标
	 * @param name
	 * @return
	 */
	public static String KpiToName(Integer kpi){
		String str=null;
		if(kpi == 1){
			str = "RSRP(dBm)";
		}else if(kpi == 2){
			str = "SNR(dB)";
		}else if(kpi == 3){
			str = "RSRQ(dB)";
		}else if(kpi == 4){
			str = "下行链路带宽(Mbps)";
		}else if(kpi == 5){
			str = "延时(ms)";
		}else if(kpi == 6){
			str = "丢包(%)";
		}else if(kpi == 7){
			str = "无覆盖率(%)";
		}else if(kpi == 8){
			str = "覆盖率(%)";
		}else if(kpi == 9){
			str = "网络掉线率(%)";
		}else if(kpi == 10){
			str = "业务掉线率(%)";
		}else if(kpi == 11){
			str = "4G切换3G(次数)";
		}
		return str;
	}
	public static String KpiU(Integer kpi){
		String str=null;
		if(kpi == 1){
			str = "(dBm)";
		}else if(kpi == 2){
			str = "(dB)";
		}else if(kpi == 3){
			str = "(dB)";
		}else if(kpi == 4){
			str = "(Mbps)";
		}else if(kpi == 5){
			str = "(ms)";
		}else if(kpi == 6){
			str = "(%)";
		}else if(kpi == 7){
			str = "(%)";
		}else if(kpi == 8){
			str = "(%)";
		}else if(kpi == 9){
			str = "(%)";
		}else if(kpi == 10){
			str = "(%)";
		}else if(kpi == 11){
			str = "次";
		}
		return str;
	}
	
	/**
	 * 生成uuid
	 * @return
	 */
	public static String getUuid(){
		String val = "";  
        Random random = new Random();  
        //参数length，表示生成几位随机数  
        for(int i = 0; i < 8; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return System.currentTimeMillis()+val;  
	}
	
	/**
	 * 指标区间设置用根据id获取kpi指标名称
	 * @param i
	 * @return
	 */
	public static String getKpi(Integer i){
		String str = null;
		if(i == 1){
			str = "RSRP";
		}else if(i == 2){
			str = "SNR";
		}else if(i == 3){
			str = "RSRQ";
		}else if(i == 4){
			str = "下行链路带宽";
		}else if(i == 5){
			str = "延时";
		}else if(i == 6){
			str = "丢包";
		}else if(i == 9){
			str = "网络掉线率";
		}else if(i == 10){
			str = "业务掉线率";
		}
		return str;
	}
	
	/**
	 * 根据图形id获取图形
	 * @param id
	 * @return
	 */
	public static String getShape(String id){
		String shape = null;
		if("BMAP_POINT_SHAPE_CIRCLE".equals(id)){
			shape = "●";
		}else if("BMAP_POINT_SHAPE_STAR".equals(id)){
			shape = "★";
		}else if("BMAP_POINT_SHAPE_SQUARE".equals(id)){
			shape = "■";
		}else if("BMAP_POINT_SHAPE_RHOMBUS".equals(id)){
			shape = "◆";
		}else if("BMAP_POINT_SHAPE_WATERDROP".equals(id)){
			shape = "۵";
		}
		return shape;
	}
	
	/**
	 * 发送json到前台
	 * @param json
	 * @param response
	 */
	public static void sendJson(String json, HttpServletResponse response){
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
