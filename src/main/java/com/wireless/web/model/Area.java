package com.wireless.web.model;

import java.util.List;

public class Area {
	private String id;//区域id
	private String parentid;//父级区域id
	private String text;//区域名称
	private String state; // open or closed
	private boolean checked;//该区域是否被选中
	private String attributes;//绑定信息
	private List<Area> children;//子区域
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public List<Area> getChildren() {
		return children;
	}
	public void setChildren(List<Area> area) {
		this.children = area;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	
}
