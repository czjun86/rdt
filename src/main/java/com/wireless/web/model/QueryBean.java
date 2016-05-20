package com.wireless.web.model;

/**
 * 判断是否选中指标
 * @author pengdd
 *
 */
public class QueryBean {
	
	private String uuid;//识别号
	private String timegransel;//时间粒度
	private String queryTime;//时间
	private String operator;//运营商
	private String province;//一级区域
	private String district;//二级区域
	private String area;//三级区域
	private Integer roadType;//道路类型
	private Integer roadLevel;//道路等级
	private String roadId;//道路id
	private String roadName;//道路名称
	private Integer rsrp;
	private Integer rsrq;
	private Integer snr;
	private Integer broadband;//宽带
	private Integer delay;//延时
	private Integer lose;//丢包
	private Integer showTime;//时间维度
	private Integer showArea;//区域维度
	private Integer showRoad;//道路维度
	private Integer showOperator;//运营商维度
	private Integer showInterval;//区间维度
	private Integer pageIndex;//当前页数
	private Integer pageSize;//当前页数
	private Integer totalPage;//当前页数
	private Integer kpiType;//指标类型
	
	private Double threshold;//差距门限值
	private Integer compare;//对比方式
	
	private String imei;
	
	private String taskName;//综合查询任务名称
	private String provinceName;//一级区域名称
	private String districtName;//二级区域名称
	private String areaName;//三级区域名称
	private String operatorName;//选中的运营商条件
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTimegransel() {
		return timegransel;
	}

	public void setTimegransel(String timegransel) {
		this.timegransel = timegransel;
	}

	public String getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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

	public String getRoadId() {
		return roadId;
	}

	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}

	public Integer getRsrp() {
		return rsrp;
	}

	public void setRsrp(Integer rsrp) {
		this.rsrp = rsrp;
	}

	public Integer getRsrq() {
		return rsrq;
	}

	public void setRsrq(Integer rsrq) {
		this.rsrq = rsrq;
	}

	public Integer getSnr() {
		return snr;
	}

	public void setSnr(Integer snr) {
		this.snr = snr;
	}

	public Integer getBroadband() {
		return broadband;
	}

	public void setBroadband(Integer broadband) {
		this.broadband = broadband;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Integer getLose() {
		return lose;
	}

	public void setLose(Integer lose) {
		this.lose = lose;
	}

	public Integer getShowTime() {
		return showTime;
	}

	public void setShowTime(Integer showTime) {
		this.showTime = showTime;
	}

	public Integer getShowArea() {
		return showArea;
	}

	public void setShowArea(Integer showArea) {
		this.showArea = showArea;
	}

	public Integer getShowRoad() {
		return showRoad;
	}

	public void setShowRoad(Integer showRoad) {
		this.showRoad = showRoad;
	}

	public Integer getShowOperator() {
		return showOperator;
	}

	public void setShowOperator(Integer showOperator) {
		this.showOperator = showOperator;
	}

	public Integer getShowInterval() {
		return showInterval;
	}

	public void setShowInterval(Integer showInterval) {
		this.showInterval = showInterval;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getKpiType() {
		return kpiType;
	}

	public void setKpiType(Integer kpiType) {
		this.kpiType = kpiType;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public Integer getCompare() {
		return compare;
	}

	public void setCompare(Integer compare) {
		this.compare = compare;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
}
