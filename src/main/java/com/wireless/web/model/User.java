package com.wireless.web.model;

public class User implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer userid;
	private String userName;
	private String password;
	private String name;
	private String email;
	private String phone;
	private Integer islock;
	private String operator;
	private String province;
	private String district;
	private String county;
	private Integer roleid;
	private String rolename;
	private String teleAuth;
	
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
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public User() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getIslock() {
		return islock;
	}
	public void setIslock(Integer islock) {
		this.islock = islock;
	}
	
	public Integer getRoleid() {
		return roleid;
	}
	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getTeleAuth() {
		return teleAuth;
	}
	public void setTeleAuth(String teleAuth) {
		this.teleAuth = teleAuth;
	}
	public User(Integer userid, String userName, String password, String name,
			String email, String phone, Integer islock,Integer roleid,String rolename,String teleAuth) {
		this.userid = userid;
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.islock = islock;
		this.roleid = roleid;
		this.rolename = rolename;
		this.teleAuth = teleAuth;
	}
}
