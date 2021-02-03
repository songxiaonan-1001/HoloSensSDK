package com.huawei.holosens.bean;

import com.alibaba.fastjson.JSON;

public class User {
	private String username;
	private String password;
	private long date;
	private boolean remember;
	public User(){}
	public User(String username, String password){
		this.username = username;
		this.password = password;
	}
//
//	public User(String username, String password, long date) {
//		this.username = username;
//		this.password = password;
//		this.date = date;
//	}

	public User(String username, String password, long date, boolean remember) {
		this.username = username;
		this.password = password;
		this.date = date;
		this.remember = remember;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@SuppressWarnings("NullableProblems")
	public String toString(){
		return JSON.toJSONString(this);
	}
}
