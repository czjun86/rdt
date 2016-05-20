package com.wireless.web.model;

public class Points {
   private String state;//颜色
   private String inter;//区间1,2,3,4,5,6
   private String imei;
   private String lat;
   private String lng;
   private String ci;
   private String pci;
   private String bid;
   private String tac;
   private String rsrp;
   private String rsrq;
   private String snr;
   private String link;
   private String lose;
   private String delay;
   private String time;
   private String warn;//-1,无事件,9网络掉线,10业务掉线,114G切换3G,采样点时为空，事件点有值
   private String netWarn;//网络事件描述（采样点有值，详情需要）
   private String dicWarn;//业务事件描述（采样点有值，详情需要）

public String getInter() {
	return inter;
}
public void setInter(String inter) {
	this.inter = inter;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}

public String getImei() {
	return imei;
}
public void setImei(String imei) {
	this.imei = imei;
}
public String getLat() {
	return lat;
}
public void setLat(String lat) {
	this.lat = lat;
}
public String getLng() {
	return lng;
}
public void setLng(String lng) {
	this.lng = lng;
}
public String getCi() {
	return ci;
}
public void setCi(String ci) {
	this.ci = ci;
}
public String getPci() {
	return pci;
}
public void setPci(String pci) {
	this.pci = pci;
}
public String getBid() {
	return bid;
}
public void setBid(String bid) {
	this.bid = bid;
}
public String getTac() {
	return tac;
}
public void setTac(String tac) {
	this.tac = tac;
}
public String getRsrp() {
	return rsrp;
}
public void setRsrp(String rsrp) {
	this.rsrp = rsrp;
}
public String getRsrq() {
	return rsrq;
}
public void setRsrq(String rsrq) {
	this.rsrq = rsrq;
}
public String getSnr() {
	return snr;
}
public void setSnr(String snr) {
	this.snr = snr;
}
public String getLink() {
	return link;
}
public void setLink(String link) {
	this.link = link;
}
public String getLose() {
	return lose;
}
public void setLose(String lose) {
	this.lose = lose;
}
public String getDelay() {
	return delay;
}
public void setDelay(String delay) {
	this.delay = delay;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}

public String getDicWarn() {
	return dicWarn;
}
public void setDicWarn(String dicWarn) {
	this.dicWarn = dicWarn;
}
public String getWarn() {
	return warn;
}
public void setWarn(String warn) {
	this.warn = warn;
}
public String getNetWarn() {
	return netWarn;
}
public void setNetWarn(String netWarn) {
	this.netWarn = netWarn;
}
   
   
}
