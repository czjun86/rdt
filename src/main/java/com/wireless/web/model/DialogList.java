package com.wireless.web.model;

import java.util.List;

public class DialogList {
	private Integer id;
	private Integer parentid;
	private String text;//内容
	private String state; // open or closed
	private boolean checked;//是否被选中
	private String attributes;//绑定信息
	private List<DialogList> children;//子区域
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
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
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public List<DialogList> getChildren() {
		return children;
	}
	public void setChildren(List<DialogList> children) {
		this.children = children;
	}
	
	
}
