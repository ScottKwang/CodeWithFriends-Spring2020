package com.bouslama.forms;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.AbstractBorder;

import com.bouslama.connect.MySQLConnection;
import com.bouslama.maintenance.UsersManagement;
import com.bouslama.processing.User;

public class frmLogin extends JFrame implements ActionListener{
	
	//Timer timer = new Timer(20, this);
	static JLabel error_handler = new JLabel("");
	JLabel label = new JLabel("your best quarantine diary");
	JLabel name = new JLabel("name:");
	JLabel password = new JLabel("password:");
	JPanel panel = new JPanel();
	JTextField user_name = new JTextField();
	JButton login_button = new JButton("login"), sign_up_button = new JButton("signup");
	JPasswordField user_password = new JPasswordField();
	ImageIcon interface_logo = new ImageIcon("files/logo.png");
	ImageIcon icon = new ImageIcon("files/icon.png");
	JLabel logo_dis = new JLabel();
	String user_login, login_pass;
	
	
	public int width = 400, height = 550;
	public static void main(String[] args) {
		System.out.println("running");
		
		frmLogin frame = new frmLogin();
		
			
	}
	
	public frmLogin() {
		
		
		setSize(700, 480);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		add(panel);
		//timer.start();
		
		panel.setLayout(null);
		logo_dis.setText("");
		logo_dis.setIcon(interface_logo);
		
		logo_dis.setBounds(width / 2 - 20, 30, 300, 150);
		panel.setBackground(new Color(248, 255, 221));
		login_button.setBounds(width / 2 + 220, 380, 120, 35);
		sign_up_button.setBounds(width / 2 - 20, 380, 120, 35);
		label.setBounds(190, 160, 500, 100);
		name.setBounds(120, 240, 100, 30);
		password.setBounds(120, 300, 100, 30);
		sign_up_button.addActionListener(this);
		error_handler.setBounds(width / 2 + 30, 330, 300, 35);
		error_handler.setForeground(Color.red);
		error_handler.setFont(new  Font("SansSerif", Font.PLAIN, 14));
		panel.add(error_handler);
		user_name.setBounds(width / 2 +30, 240, 280, 35);
		user_password.setBounds(width / 2 +30, 300, 280, 35);
		user_name.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 201, 225)));
		user_name.setBackground(new Color(248, 255, 221));
		user_name.setForeground(Color.black);
		user_name.setFont(new Font("SansSerif", Font.PLAIN, 18));
		user_password.setBackground(new Color(248, 255, 221));
		user_password.setForeground(Color.black);
		user_password.setFont(new Font("SansSerif", Font.PLAIN, 18));
		user_password.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 201, 225)));
		login_button.setForeground(new Color(248, 255, 221));
		login_button.setFont(new Font("SansSerif", Font.PLAIN, 18));
		login_button.setBackground(new Color(65, 221, 255));
		login_button.setBorder(null);
		
		login_button.addActionListener(this);
		
		
		name.setFont(new Font("SansSerif", Font.ITALIC, 20));
		password.setFont(new Font("SansSerif", Font.ITALIC, 20));
		label.setFont(new Font("SansSerif", Font.PLAIN, 25));
		label.setForeground(new Color(8, 59, 102));
		panel.add(error_handler);
		panel.add(logo_dis); 
		panel.add(label);
		panel.add(user_name);
		panel.add(name);
		panel.add(user_password);
		panel.add(password);
		panel.add(login_button);
		panel.add(sign_up_button);
		setTitle("this pannel is the app");
		//setLocation(0, 0);
		//pack();
		setVisible(true);
		
	}
	
	protected void login() {
		 
		try {
		Connection con = MySQLConnection.getConnection();
		String sql_login = "select * from users where user_login = ? and user_password =  ?;";
		PreparedStatement pst = con.prepareStatement(sql_login);
		pst.setString(1, user_name.getText());
		pst.setString(2, String.valueOf(user_password.getPassword()));
		
		ResultSet rs = pst.executeQuery();
		if(rs.next() == false) {
			error_handler.setText("*.user or password not found");
			user_password.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.red));
			user_password.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.red));
			
		}
		
		else {
				error_handler.setText("congrats login ");
				error_handler.setForeground(Color.green);
				user_password.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.green));
				setVisible(false);
				frmManagement.user_login = user_name.getText();
				frmManagement.user_pass = String.valueOf(user_password.getPassword());
				
				frmManagement mangementFrame = new frmManagement();
				
				
				
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand() == "login") {
			
			if(user_name.getText().isEmpty() || String.valueOf(user_password.getPassword()).isEmpty()) {
				
				error_handler.setText("*.please fill all fields");
			}
			else {
				
				login();
			}
		
			}
		if(e.getActionCommand() == "signup") {
			
			System.out.println("test 124");
			frmSignup signupFrame = new frmSignup();
			
		}
		
	
	}
	}
	
	

	
