/**     
* @文件名称: ReplenishRoadPoionts.java  
* @类路径: com.wireless.web.model  
* @描述: TODO  
* @作者：tc  
* @时间：2016年1月15日 上午10:40:31  
* @版本：V1.0     
*/
package com.wireless.web.model;
/**  
 * @类功能说明：   补充道路点实体
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者：tc  
 * @创建时间：2016年1月15日 上午10:40:31  
 * @版本：V1.0  
 */
public class ReplenishRoadPoionts {
	private String uuid;//道路点编号
	private String roadId;//道路编号
	private String roadName;//道路名称
	private String gdLongitude;//高德经度
	private String gdLatitude;//高德纬度
	private String bdLongitude;//百度经度
	private String bdLatitude;//百度纬度
	private String isReplenish;//是否为手动补充
	
	//以下是拽取的属性
	private String type;//道路类型
	private String feature;//道路性质
	private String level;  //道路等级
	private String regionCode; //区域编码
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getRoadId() {
		return roadId;
	}
	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	public String getGdLongitude() {
		return gdLongitude;
	}
	public void setGdLongitude(String gdLongitude) {
		this.gdLongitude = gdLongitude;
	}
	public String getGdLatitude() {
		return gdLatitude;
	}
	public void setGdLatitude(String gdLatitude) {
		this.gdLatitude = gdLatitude;
	}
	public String getBdLongitude() {
		return bdLongitude;
	}
	public void setBdLongitude(String bdLongitude) {
		this.bdLongitude = bdLongitude;
	}
	public String getBdLatitude() {
		return bdLatitude;
	}
	public void setBdLatitude(String bdLatitude) {
		this.bdLatitude = bdLatitude;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getIsReplenish() {
		return isReplenish;
	}
	public void setIsReplenish(String isReplenish) {
		this.isReplenish = isReplenish;
	}
}
