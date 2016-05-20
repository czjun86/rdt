package com.wireless.web.model;

import java.util.List;

/**
 * 地图参数属性
 * 
 * @author CZJ
 * 
 */
public class VoBean {
	private String id;
	public String province;//一级区域
	public String district;//二级区域
	public String area;//三级区域
	
	private String roadId;// 道路
	
	private String roadName;// 道路
	
	private Integer roadType;// 道路类型
	
	private Integer roadLevel;// 道路级别
	
	private Integer level;// 区域等级,是否对比
	
	private String points; // 经纬度集合

	private String starttime;// 时间
	
	private String endtime;// 时间
	
	private Integer operator;// 运营商
	
	private Integer userId;// 
	private String uuid;// 

	

	private Integer kpiType;// 指标
	
	private Integer kpiSymbol;// 指标符号1->,2-<,3->=,4-<=,5-=

	private Double kpiVaule;// 指标值
	
	private String kpiInterv;//指标区间
	
	private String  dubiOperator;// 对比运营商
	private String imei;
	private String warn;//-1,无事件,9网络掉线,10业务掉线,114G切换3G
	private String warnValue;//事件值
	private String warnSymbol;//事件符号
	
	public String getDubiOperator() {
		return dubiOperator;
	}

	public void setDubiOperator(String dubiOperator) {
		this.dubiOperator = dubiOperator;
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

	public String getRoadId() {
		return roadId;
	}

	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}


	public void setLevel(Integer level) {
		this.level = level;
	}



	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}



	public String getKpiInterv() {
		return kpiInterv;
	}

	public void setKpiInterv(String kpiInterv) {
		this.kpiInterv = kpiInterv;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoadType() {
		return roadType;
	}

	public void setRoadType(Integer roadType) {
		this.roadType = roadType;
	}

	public Integer getRoadLevel() {
		return roadLevel;
	}

	public void setRoadLevel(Integer roadLevel) {
		this.roadLevel = roadLevel;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public Integer getKpiType() {
		return kpiType;
	}

	public void setKpiType(Integer kpiType) {
		this.kpiType = kpiType;
	}

	public Integer getKpiSymbol() {
		return kpiSymbol;
	}

	public void setKpiSymbol(Integer kpiSymbol) {
		this.kpiSymbol = kpiSymbol;
	}

	public Double getKpiVaule() {
		return kpiVaule;
	}

	public void setKpiVaule(Double kpiVaule) {
		this.kpiVaule = kpiVaule;
	}

	public Integer getLevel() {
		return level;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	

	public String getWarn() {
		return warn;
	}

	public void setWarn(String warn) {
		this.warn = warn;
	}

	public String getWarnValue() {
		return warnValue;
	}

	public void setWarnValue(String warnValue) {
		this.warnValue = warnValue;
	}

	public String getWarnSymbol() {
		return warnSymbol;
	}

	public void setWarnSymbol(String warnSymbol) {
		this.warnSymbol = warnSymbol;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	
}
