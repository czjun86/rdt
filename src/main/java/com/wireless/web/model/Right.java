package com.wireless.web.model;

import java.io.Serializable;
import java.util.Set;

public class Right implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer rightid;

	private String rightname;
	
	private String url;

	private Integer parentid;

	private Short type;

	private Integer orderby;
	
	private Integer showtype;
	
	private String skins;
	
	private String operate;
	
	private String modules;

	private String memo;
	
	private Set<Right> subRight;

	public Integer getRightid() {
		return rightid;
	}

	public void setRightid(Integer rightid) {
		this.rightid = rightid;
	}

	public String getRightname() {
		return rightname;
	}

	public void setRightname(String rightname) {
		this.rightname = rightname == null ? null : rightname.trim();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}

	public Set<Right> getSubRight() {
		return subRight;
	}

	public void setSubRight(Set<Right> subRight) {
		this.subRight = subRight;
	}

	public Integer getShowtype() {
		return showtype;
	}

	public void setShowtype(Integer showtype) {
		this.showtype = showtype;
	}

	public String getSkins() {
		return skins;
	}

	public void setSkins(String skins) {
		this.skins = skins;
	}	

	public String getModules() {
		return modules;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public void setModules(String modules) {
		this.modules = modules;
	}	
	
}