/**     
* @文件名称: ReplenishRoadPointsUtils.java  
* @类路径: com.wireless.web.utils  
* @描述: TODO  
* @作者：tc  
* @时间：2016年3月3日 下午4:13:41  
* @版本：V1.0     
*/
package com.wireless.web.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**  
 * @类功能说明：  
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者：tc  
 * @创建时间：2016年3月3日 下午4:13:41  
 * @版本：V1.0  
 */
public class ReplenishRoadPointsUtils {
	
	/**
	 * 
	* 函数功能说明   	百度坐标系转化为火星坐标系(高德、google)
	* tc  2015年9月22日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param bdLng
	* @参数： @param bdLat
	* @参数： @return   
	* @throws
	 */
	public static Map<String,Double> bdTogcj(double bdLng,double bdLat)
	{
		Map<String,Double> re = new HashMap<String, Double>();
		Double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
		Double x = bdLng - 0.0065, y = bdLat - 0.006;
		Double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		Double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		Double gcjLng = z * Math.cos(theta);
		Double gcjLat = z * Math.sin(theta);
		re.put("lng", gcjLng);
	    re.put("lat", gcjLat);
	    
	    return re;
	}
	
	
	/**
	 * 生成流水号（无后缀）UUID 方式
	 */
	public static String findFlowUUID(){
		String flowUuid = UUID.randomUUID().toString();
		flowUuid+=(int)(Math.random()*9999);
		return flowUuid;
	}
	
	
	/** */
	/**
	 * 从json数组中解析出java字符串数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String[] getStringArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);
		}

		return stringArray;
	}
	
	
	/**
	 * 高德地图逆地址编码API  这个是支持单一经纬度查询的，支持批量查询的coordsys属性无用
	 * @param lng
	 * @param lat
	 * @param coordsys   坐标类型    gps、baidu
	 * @return
	 */
	public static Map<String,String> regeo(String lng,String lat,String coordsys){
		Map<String,String> map = new HashMap<String, String>();
		//获取AK
		String ak = "70d685eea7879da7180ea5a348b7d033";
		String str = "http://restapi.amap.com/v3/geocode/regeo?key="+ak+"&extensions=all&radius=0&coordsys="+coordsys+"&location="+lng+","+lat;
		try {
			URL url = new URL(str.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
			connection.setRequestMethod("GET");
            connection.connect();// 连接会话
            // 获取输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sl = new StringBuilder();
            while ((line = br.readLine()) != null) {// 循环读取流
            	sl.append(line);
            }
            br.close();// 关闭流
            connection.disconnect();// 断开连接
            System.out.println(sl.toString());
            //处理API返回结果
            JSONObject json = JSONObject.fromObject(sl.toString());
            String status = json.get("status")!=null?json.getString("status"):"";
            if(status.equals("1")){//API接口调用成功
            	//获取结果
            	JSONObject reg = json.getJSONObject("regeocode");
            	//获取地址信息
            	JSONObject addCom = reg.getJSONObject("addressComponent");
            	//获取区域信息
            	String province = addCom.get("province")!=null?addCom.getString("province"):"[]";//省(直辖市)
            	String city = addCom.get("city")!=null?addCom.getString("city"):"[]";//市、县
            	String district = addCom.get("district")!=null?addCom.getString("district"):"[]";//区
            	String adcode = addCom.get("adcode")!=null&&!addCom.getString("adcode").equals("[]")?addCom.getString("adcode"):"";//区域编码
            	String citycode = addCom.get("citycode")!=null&&!addCom.getString("citycode").equals("[]")?addCom.getString("citycode"):"";//城市编码
            	//获取住宅小区信息
            	JSONObject neighborhood = addCom.getJSONObject("neighborhood");//小区信息对象
            	String name = neighborhood.get("name")!=null&&!neighborhood.getString("name").equals("[]")?neighborhood.getString("name"):"";//小区名称
            	//获取道路编号和道路名称
            	String roadId = "";
            	String roadName = "";
            	JSONArray roads = reg.getJSONArray("roads");
            	if(roads.size()>0){
            		JSONObject road=roads.getJSONObject(0);
            		roadId = road.get("id")!=null?road.getString("id"):"";
            		roadName = road.get("name")!=null?road.getString("name"):"";
            	}
            	
            	//组合返回结果
            	//组合区域字符串和确定百度place接口区域查询字符串
            	StringBuffer locationName=new StringBuffer();
            	StringBuffer regionQry =new StringBuffer();
            	if(!province.equals("[]")){
            		locationName.append(province);
            		regionQry.append(province);
            	}else{
            		province="";
            	}
            	if(!city.equals("[]")){
            		locationName.append("|"+city);
            		regionQry.append(","+city);
            	}else{
            		city="";
            	}
            	if(!district.equals("[]")){
            		locationName.append("|"+district);
            		regionQry.append(","+district);
            	}else{
            		district="";
            	}
            	
            	if(!name.equals("")){
            		locationName.append("|"+name);
            	}
            	map.put("location_name", locationName.toString());//区域名称
            	map.put("province", province);//省
            	map.put("city", city);//市
            	map.put("district", district);//区
            	map.put("region_code", adcode);//区域编码
            	map.put("cityCode", citycode);
            	map.put("name", name);//小区名称
            	map.put("roadId", roadId);//道路编号
            	map.put("roadName", roadName);//道路名称
            	map.put("region_qry", regionQry.toString());
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
