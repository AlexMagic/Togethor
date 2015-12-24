package com.android.entity;

import java.io.Serializable;

public class User extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String phone;
	private String mail;
	
	private String head_url;
	
	private String name;
	private String oftenPos;
	private String sex;
	
	private String car_type;
	private String car_license;
	private int num;
	
	public String getCar_type() {
		return car_type;
	}
	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}
	public String getCar_license() {
		return car_license;
	}
	public void setCar_license(String car_license) {
		this.car_license = car_license;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getOftenPos() {
		return oftenPos;
	}
	public void setOftenPos(String oftenPos) {
		this.oftenPos = oftenPos;
	}
	public String getHead_url() {
		return head_url;
	}
	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}
	
	
}
