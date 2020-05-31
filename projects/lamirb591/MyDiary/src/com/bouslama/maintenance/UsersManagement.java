package com.bouslama.maintenance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.bouslama.connect.MySQLConnection;
import com.bouslama.processing.User;;

public class UsersManagement {

	

	public User getUser(User us) {
		
		User user  = null;
		
		Connection con = null;
		PreparedStatement pst = null;
		
		ResultSet rs = null;
		
		try {
			con = MySQLConnection.getConnection();
			String sql = "select * from users where user_login = ? and user_password =  ?;";
			
			
			pst = con.prepareStatement(sql);
			
			pst.setString(1, us.getuser_login());
			pst.setString(2, us.getuser_password());
			
			
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBlob(5));
			}
			Statement stame = con.createStatement();
			rs = stame.executeQuery("select * from users;");
			
			
		} catch (Exception e) {
			
			System.out.println("error obtaining user");
		}
		
		
		return user;
	}
	
}
