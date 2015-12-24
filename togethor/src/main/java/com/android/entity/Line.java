package com.android.entity;

import java.io.Serializable;
import java.util.List;

public class Line extends Entity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2498756181239667077L;
	
	private int id;
	private String startAddr;
	private String endAddr;
	private String dateTime;
	private int num;
	private int left_num;
	private int type;
	private String reason;
	private String url;
	private int status;
	private String phone;
	private User driver;
	private List<User> passenger_List;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStartAddr() {
		return startAddr;
	}
	public void setStartAddr(String startAddr) {
		this.startAddr = startAddr;
	}
	public String getEndAddr() {
		return endAddr;
	}
	public void setEndAddr(String endAddr) {
		this.endAddr = endAddr;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getLeft_num() {
		return left_num;
	}
	public void setLeft_num(int left_num) {
		this.left_num = left_num;
	}
	public User getDriver() {
		return driver;
	}
	public void setDriver(User driver) {
		this.driver = driver;
	}
	public List<User> getPassenger_List() {
		return passenger_List;
	}
	public void setPassenger_List(List<User> passenger_List) {
		this.passenger_List = passenger_List;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
	
	
}
