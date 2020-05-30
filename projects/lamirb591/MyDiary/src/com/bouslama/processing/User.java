package com.bouslama.processing;

import java.sql.Blob;

import javax.swing.ImageIcon;

public class User {
	
	private String user_login;
	private String user_name;
	private String user_last_name;
	private String user_password;
	private Blob user_pic;
	public User(String user_login, String user_name, String user_last_name,
			String user_password, Blob user_pic) {
		
		this.user_login = user_login;
		this.user_name = user_name;
		this.user_last_name = user_last_name;
		this.user_password = user_password;
		this.user_pic = user_pic;
	}

	public User() {
		
	}

	
	public String getuser_login() {
		return user_login;
	}

	public void setuser_login(String user_login) {
		this.user_login = user_login;
	}

	public String getuser_name() {
		return user_name;
	}

	public void setuser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getuser_last_name() {
		return user_last_name;
	}

	public void setuser_last_name(String user_last_name) {
		this.user_last_name = user_last_name;
	}

	public String getuser_password() {
		return user_password;
	}

	public void setuser_password(String user_password) {
		this.user_password = user_password;
	}

	public Blob getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(Blob user_pic) {
		this.user_pic = user_pic;
	}
	
	

}
