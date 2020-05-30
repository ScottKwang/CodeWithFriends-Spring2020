package com.bouslama.forms;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import com.bouslama.connect.MySQLConnection;
import com.mysql.cj.jdbc.SuspendableXAConnection;
import com.mysql.cj.protocol.ResultStreamer;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class frmSignup extends JFrame implements ActionListener{
	
		//shiiiiiiiiiit loooooool
	
	JPanel profile_pic = new JPanel (new MigLayout("wrap, center"));
	JButton back = new JButton("back");
		JLabel signup_header = new JLabel("signup");
		JPanel signup_panel = new JPanel(new MigLayout("wrap 2"));
		JLabel choose_login = new JLabel("choose login:");
		JTextField read_login = new JTextField();
		JLabel set_name = new JLabel("name:");
		JTextField read_name = new JTextField();
		JLabel set_last = new JLabel("last name:");
		JLabel error_handler = new JLabel();
		JTextField read_last = new JTextField();
		JLabel set_password = new JLabel("enter password:");
		JPasswordField read_password = new JPasswordField();
		JLabel second_password = new JLabel("repeat password");
		JPasswordField repeat_password = new JPasswordField();
		JButton signup_button = new JButton("signup");
		String pic_path;
	public frmSignup() {
		setVisible(true);
		setSize(500, 500);
		
		signup_panel.add(signup_header, "wrap");
		
		signup_panel.add(choose_login);
		
		signup_panel.add(read_login, "w 250!");
		
		signup_panel.add(set_name);
		
		signup_panel.add(read_name, "w 250!");
		
		signup_panel.add(set_last);
		
		signup_panel.add(read_last, "w 250!");
		
		signup_panel.add(set_password);
		
		signup_panel.add(read_password, "w 250!");
		
		signup_panel.add(second_password);
		
		signup_panel.add(repeat_password, "w 250!");
		
		error_handler.setForeground(Color.red);
		signup_panel.add(error_handler);
		
		signup_button.addActionListener(this);
		signup_panel.add(signup_button, "right, w 150!");
		
		add(signup_panel);
		
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("signup")) {
			if(read_login.getText().isEmpty() || read_name.getText().isEmpty() || read_last.getText().isEmpty() || read_password.getText().isEmpty() || repeat_password.getText().isEmpty()) {
				error_handler.setText("please fill all fields");
				
			}
			
			
			else {
				if(read_login.getText().contains(" ")) {
					error_handler.setText("no spaces in user_name");
					
				}
				
				else {
				if(!String.valueOf(read_password.getPassword()).equals(String.valueOf(repeat_password.getPassword())))  {
					error_handler.setText("non matching passwords");
				}
				
				else {
					
					try {
					Connection con = MySQLConnection.getConnection();
					String sql_existing_user = "select * from users where user_login = ?;";
					PreparedStatement pst = con.prepareStatement(sql_existing_user);
					pst.setString(1, read_login.getText());
					ResultSet rs = pst.executeQuery();
					if( rs.next() == true) {
						System.out.println("existing user");
						error_handler.setText("existing user");
					}
					else {
						String sql_insert_user= "insert into users values (?, ?, ?, ?, NULL);";
						PreparedStatement pst_insert = con.prepareStatement( sql_insert_user);
						pst_insert.setString(1, read_login.getText());
						pst_insert.setString(2, read_name.getText());
						pst_insert.setString(3, read_last.getText());
						pst_insert.setString(4, String.valueOf(repeat_password.getPassword()));
						
						pst_insert.executeUpdate();
						
						
						
						
						signup_panel.setVisible(false);
						this.revalidate();
						
						
						JLabel pic_header = new JLabel("choose profile pic");
						
						BufferedImage default_img = null;
						try {
						    default_img = ImageIO.read(new File("files/user_default_pic.jpg"));
						} catch (Exception exception) {
						    exception.printStackTrace();
						}
						Image dimg = default_img.getScaledInstance(300, 300,
						        Image.SCALE_SMOOTH);
						
						
						JLabel user_pic = new JLabel("test");
						
						
						
						
						
						ImageIcon default_pic_source = new ImageIcon(dimg);
						
						
						
						
						
						
						user_pic.setIcon(default_pic_source);
						JButton file_pic = new JButton("choose pic");
						file_pic.addActionListener(new ActionListener() {
						
							@Override
							public void actionPerformed(ActionEvent arg0) {
								JFileChooser file = new JFileChooser();
								file.setCurrentDirectory(new File(System.getProperty("user.home")));
								file.addChoosableFileFilter(new FileNameExtensionFilter("*.Images", "jpg", "png"));
								int result = file.showSaveDialog(null);
								
								if(result == JFileChooser.APPROVE_OPTION) {
									System.out.println("working");
									File selectedFile = file.getSelectedFile();
									pic_path = selectedFile.getAbsolutePath();
									BufferedImage uploaded_img = null;
									try {
									    uploaded_img = ImageIO.read(new File(pic_path));
									} catch (Exception exception) {
									    exception.printStackTrace();
									}
									Image uimg = uploaded_img.getScaledInstance(300, 300,
									        Image.SCALE_SMOOTH);
									ImageIcon user_pic_source = new ImageIcon(uimg);
									user_pic.setIcon(user_pic_source);
									
								}
								else {
									
									return;
									
								}
								
								
							}
						});
						
						JButton confirm = new JButton("confirm");
						confirm.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								if(user_pic.getIcon().equals(default_pic_source)) {
									
									
									dispose();
									frmLogin.error_handler.setForeground(Color.green);
									frmLogin.error_handler.setText("successfully signed up, login to your account");
									
									
									
								}
								else {
									System.out.println(pic_path);
									try {
										InputStream user_image = new FileInputStream(pic_path);
										Connection con = MySQLConnection.getConnection();
										String set_pic = "update users set user_pic = ? where user_login = ?;";
										PreparedStatement ps = con.prepareStatement(set_pic);
										ps.setBlob(1, user_image);
										ps.setString(2, read_login.getText());
										ps.executeUpdate();
										System.out.println("successfull");
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									dispose();
									frmLogin.error_handler.setForeground(Color.green);
									frmLogin.error_handler.setText("successfully signed up, login to your account");
								}
								
							}
						});
						
						JPanel choice = new JPanel(new MigLayout("wrap 2"));
						
						
						back.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								profile_pic.setVisible(false);
								remove(profile_pic);
								revalidate();
								signup_button.setText("continue");
								add(signup_panel);
								signup_panel.setVisible(true);
								revalidate();
								
							}
						});
						choice.add(back);
						
						choice.add(confirm);
						
						
						profile_pic.add(pic_header, "center");
						profile_pic.add(user_pic, "w 300!, height 300");
						profile_pic.add(file_pic, "center");
						profile_pic.add(choice, "center");
						this.add(profile_pic);
						
						this.revalidate();
						
					}
					}
					catch(Exception  exception) {
						exception.printStackTrace();
					}
					
			
			
			
					
				}
				if(e.getActionCommand() == "continue") {
					System.out.println("hot baby");
					signup_panel.setVisible(false);
					profile_pic.setVisible(true);
					this.revalidate();
					this.add(profile_pic);
					
					this.revalidate();
					
				}
				
				}
		
			}
	
		}
	}

}
