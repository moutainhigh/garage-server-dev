package com.yixin.garage.auth_local;

import java.io.Serializable;

public class LocalUser implements Serializable{
	private static final long serialVersionUID = -4832348817743290851L;
	private String userName;
	private String password;
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
	@Override
	public String toString() {
		return "LocalUser [userName=" + userName + ", password=" + password + "]";
	}
}
