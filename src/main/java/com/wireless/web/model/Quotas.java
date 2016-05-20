package com.wireless.web.model;

public class Quotas {
	public Integer operator;//1 联通，2 移动，3 电信，4 铁通，-1 未知
	public Integer netWork;//无网络(-1),没取到(0),wifi(1),2G(2),3G(3),4G(4)
	public Integer kpiClass;//1 无线层, 2 网络层, 3 应用层
	public Integer kpiType;//kpi指标对应编号
	public String kpiName;//kpi指标名称
	public Double refMinValuel;//最小参考值
	public Double refMaxValuel;//最大参考值
	public Double interValuelOne;//区间值1
	public Double interValuelTwo;//区间值2
	public Double interValuelThree;//区间值3
	public Double interValuelFour;//区间值4
	public Double interValuelFive;//区间值5
	public String kpiUnit;//指标单位
	public String kpiComment;//备注
	public Integer getOperator() {
		return operator;
	}
	public void setOperator(Integer operator) {
		this.operator = operator;
	}
	public Integer getNetWork() {
		return netWork;
	}
	public void setNetWork(Integer netWork) {
		this.netWork = netWork;
	}
	public Integer getKpiClass() {
		return kpiClass;
	}
	public void setKpiClass(Integer kpiClass) {
		this.kpiClass = kpiClass;
	}
	public Integer getKpiType() {
		return kpiType;
	}
	public void setKpiType(Integer kpiType) {
		this.kpiType = kpiType;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public Double getRefMinValuel() {
		return refMinValuel;
	}
	public void setRefMinValuel(Double refMinValuel) {
		this.refMinValuel = refMinValuel;
	}
	public Double getRefMaxValuel() {
		return refMaxValuel;
	}
	public void setRefMaxValuel(Double refMaxValuel) {
		this.refMaxValuel = refMaxValuel;
	}
	public String getKpiUnit() {
		return kpiUnit;
	}
	public void setKpiUnit(String kpiUnit) {
		this.kpiUnit = kpiUnit;
	}
	public String getKpiComment() {
		return kpiComment;
	}
	public void setKpiComment(String kpiComment) {
		this.kpiComment = kpiComment;
	}
	public Double getInterValuelOne() {
		return interValuelOne;
	}
	public void setInterValuelOne(Double interValuelOne) {
		this.interValuelOne = interValuelOne;
	}
	public Double getInterValuelTwo() {
		return interValuelTwo;
	}
	public void setInterValuelTwo(Double interValuelTwo) {
		this.interValuelTwo = interValuelTwo;
	}
	public Double getInterValuelThree() {
		return interValuelThree;
	}
	public void setInterValuelThree(Double interValuelThree) {
		this.interValuelThree = interValuelThree;
	}
	public Double getInterValuelFour() {
		return interValuelFour;
	}
	public void setInterValuelFour(Double interValuelFour) {
		this.interValuelFour = interValuelFour;
	}
	public Double getInterValuelFive() {
		return interValuelFive;
	}
	public void setInterValuelFive(Double interValuelFive) {
		this.interValuelFive = interValuelFive;
	}
	
}
