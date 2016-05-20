package com.wireless.web.model;

public class Pot {
	   private String id;
	   private String state;//颜色
	   private String inter;//区间1,2,3,4,5,6
	   private Double lat;
	   private Double lng;
	   private String telecoms;//运营商
	   private String warn;//-1,无事件,9网络掉线,10业务掉线,114G切换3G
	   private String dicWarn;//事件描述
	   private String isWarn;//是否是事件点 0否，1是
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getInter() {
		return inter;
	}
	public void setInter(String inter) {
		this.inter = inter;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public String getTelecoms() {
		return telecoms;
	}
	public void setTelecoms(String telecoms) {
		this.telecoms = telecoms;
	}

	public String getWarn() {
		return warn;
	}
	public void setWarn(String warn) {
		this.warn = warn;
	}
	public String getDicWarn() {
		return dicWarn;
	}
	public void setDicWarn(String dicWarn) {
		this.dicWarn = dicWarn;
	}
	public String getIsWarn() {
		return isWarn;
	}
	public void setIsWarn(String isWarn) {
		this.isWarn = isWarn;
	}
	
	   
}
