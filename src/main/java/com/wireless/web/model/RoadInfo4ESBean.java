/**     
 * @文件名称: RoadInfo4ESBean.java  
 * @类路径: com.wireless.web.model  
 * @描述: TODO  
 * @作者：dsk  
 * @时间：2015年12月9日 上午10:53:04  
 * @版本：V1.0     
 */
package com.wireless.web.model;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：
 * @作者：dsk
 * @创建时间：2015年12月9日 上午10:53:04
 * @版本：V1.0
 */
public class RoadInfo4ESBean {
	private String cityName; // 市区名(直辖市为一级区域名称)
	private String countryName;// 区县名(三级区域名称)
	private String roadName;// 道路名称
	private String regionCode;// 三级区域代码
	private String parentId;// 二级区域代码
	private String roadId;// 道路ID
	private Integer roadType;// 道路类型
	private Integer roadLevel;// 道路等级
	private String superId;// 一级区域代码
	private String superName;// 一级区域名称

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName
	 *            the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * @param countryName
	 *            the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * @return the roadName
	 */
	public String getRoadName() {
		return roadName;
	}

	/**
	 * @param roadName
	 *            the roadName to set
	 */
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	/**
	 * @return the regionCode
	 */
	public String getRegionCode() {
		return regionCode;
	}

	/**
	 * @param regionCode
	 *            the regionCode to set
	 */
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the roadId
	 */
	public String getRoadId() {
		return roadId;
	}

	/**
	 * @param roadId
	 *            the roadId to set
	 */
	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}

	/**
	 * @return the roadType
	 */
	public Integer getRoadType() {
		return roadType;
	}

	/**
	 * @param roadType
	 *            the roadType to set
	 */
	public void setRoadType(Integer roadType) {
		this.roadType = roadType;
	}

	/**
	 * @return the roadLevel
	 */
	public Integer getRoadLevel() {
		return roadLevel;
	}

	/**
	 * @param roadLevel
	 *            the roadLevel to set
	 */
	public void setRoadLevel(Integer roadLevel) {
		this.roadLevel = roadLevel;
	}

	/**
	 * @return the superId
	 */
	public String getSuperId() {
		return superId;
	}

	/**
	 * @param superId
	 *            the superId to set
	 */
	public void setSuperId(String superId) {
		this.superId = superId;
	}

	/**
	 * @return the superName
	 */
	public String getSuperName() {
		return superName;
	}

	/**
	 * @param superName
	 *            the superName to set
	 */
	public void setSuperName(String superName) {
		this.superName = superName;
	}
}
