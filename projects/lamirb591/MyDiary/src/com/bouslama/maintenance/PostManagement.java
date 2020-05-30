package com.bouslama.maintenance;

import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.bouslama.connect.MySQLConnection;
import com.bouslama.forms.frmManagement;
import com.bouslama.forms.pnlCharts;
import com.bouslama.processing.Post;
import com.bouslama.processing.User;

public class PostManagement {
	
	public Post getPrivatePost(Post usersPost) {
		
		Post post = null;
		Connection con = null;
		try {
		
		frmManagement.postsList.clear();
		con = MySQLConnection.getConnection();
		String sql_post = "select * from posts where user_login = ?;";
		PreparedStatement pst = con.prepareStatement(sql_post);
		pst.setString(1, usersPost.getuser_login());
		ResultSet rs = pst.executeQuery();
		while(rs.next()) {
			post = new Post(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getBlob(8));
			frmManagement.postsList.add(post);
			
			
		}
		
		
		}
		catch(Exception e) {
			System.out.println("error obtaining post");
		}
		
		return post;
	}
	public Post getPulicPost() {
		
		Post post = null;
		Connection con = null;
		try {
		frmManagement.postsList.clear();
		con = MySQLConnection.getConnection();
		String sql_post = "select * from posts where post_visibility = '1';";
		PreparedStatement pst = con.prepareStatement(sql_post);
		ResultSet rs = pst.executeQuery();
		while(rs.next()) {
			post = new Post(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getBlob(8));
			frmManagement.postsList.add(post);
			
			
			
		}
		
		
		}
		catch(Exception e) {
			System.out.println("error obtaining post");
		}
		
		return post;
	}
	public Post getCommunityPost() {
		pnlCharts.community_posts_list.clear();
		Post post = null;
		Connection con = null;
		try {
		
		con = MySQLConnection.getConnection();
		String sql_post = "select * from posts;";
		PreparedStatement pst = con.prepareStatement(sql_post);
		ResultSet rs = pst.executeQuery();
		while(rs.next()) {
			post = new Post(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getBlob(8));
			
			pnlCharts.community_posts_list.add(post);
			
			
		}
		
		
		}
		catch(Exception e) {
			System.out.println("error obtaining post");
		}
		
		return post;
	}
public Post getUserPosts(Post usersPost) {
		
		Post post = null;
		Connection con = null;
		try {
		
		pnlCharts.user_posts_list.clear();
		con = MySQLConnection.getConnection();
		String sql_post = "select * from posts where user_login = ?;";
		PreparedStatement pst = con.prepareStatement(sql_post);
		pst.setString(1, usersPost.getuser_login());
		ResultSet rs = pst.executeQuery();
		while(rs.next()) {
			post = new Post(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getBlob(8));
			pnlCharts.user_posts_list.add(post);
			
			
		}
		
		
		}
		catch(Exception e) {
			System.out.println("error obtaining post");
		}
		
		return post;
	}
	
}
