
package com.bouslama.connect;
import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class MySQLConnection {
	static String sql_user;
	static String sql_pass;
	private static Scanner x;
	public static void read_data() {
	try {
		
		x = new Scanner(new File("files/connect_data"));
		while(x.hasNext()) {
			sql_user = x.next();
			sql_pass = x.next();
			
		}
	}
	catch(Exception e) {
		System.out.println("cant find file");
	}
		
	}
	public static Connection getConnection() {
		read_data();
		
		Connection con = null;
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/codewithfriends?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
			String user = sql_user;
			String password = sql_pass;
			
			Class.forName(driver);
			con = DriverManager.getConnection(url , user , password);
			
			
			
		}
		catch(ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}
