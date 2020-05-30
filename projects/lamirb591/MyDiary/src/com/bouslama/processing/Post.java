package com.bouslama.processing;

import java.sql.Blob;

public class Post {

	private int post_id;
	private String post_date;
	private String user_login;
	private String post_title;
	private String post_content;
	private String post_rate;
	private String post_visibility;
	private Blob post_pic;
	public Post(int post_id, String user_login, String post_date, String post_title, String post_content, String post_rate, String post_visibility, Blob post_pic) {
		//User user = new User();
		this.post_id = post_id;
		this.user_login = user_login;
		this.post_date = post_date;
		this.post_title = post_title;
		this.post_content = post_content;
		this.post_rate = post_rate;
		this.post_visibility = post_visibility;
		this.setPost_pic(post_pic);
	}
	public Post() {
		
	}

	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public String getPost_date() {
		return post_date;
	}
	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}
	public String getuser_login() {
		return user_login;
	}
	public void setuser_login(String user_login) {
		this.user_login = user_login;
	}
	public String getPost_title() {
		return post_title;
	}
	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}
	public String getPost_content() {
		return post_content;
	}
	public void setPost_content(String post_content) {
		this.post_content = post_content;
	}
	public String getPost_rate() {
		return post_rate;
	}
	public void setPost_rate(String post_rate) {
		this.post_rate = post_rate;
	}
	public void setPost_visibility(String post_visibility) {
		this.post_visibility = post_visibility;
	}
	public String getPost_visibility() {
		return post_visibility;
	}
	public Blob getPost_pic() {
		return post_pic;
	}
	public void setPost_pic(Blob post_pic) {
		this.post_pic = post_pic;
	}
	
	
}
