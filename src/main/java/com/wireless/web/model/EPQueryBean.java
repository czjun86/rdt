package com.wireless.web.model;

public class EPQueryBean {

	private String queryTime;
	
	private String province;
	private String district;
	private String area;
	
	private Integer operator;
	private String imei;
	private String model;
	private Integer colletion;
	private Integer vision;
	
	public String getQueryTime() {
		return queryTime;
	}
	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getOperator() {
		return operator;
	}
	public void setOperator(Integer operator) {
		this.operator = operator;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getColletion() {
		return colletion;
	}
	public void setColletion(Integer colletion) {
		this.colletion = colletion;
	}
	public Integer getVision() {
		return vision;
	}
	public void setVision(Integer vision) {
		this.vision = vision;
	}
}
