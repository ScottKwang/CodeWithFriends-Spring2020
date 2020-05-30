package com.bouslama.forms;

import com.bouslama.processing.Post;
import com.bouslama.processing.User;
import com.mysql.cj.jdbc.PreparedStatementWrapper;
import com.mysql.cj.protocol.ResultStreamer;
import com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;
import com.mysql.cj.x.protobuf.MysqlxNotice.Frame;
import com.bouslama.connect.MySQLConnection;
import net.miginfocom.layout.Grid;
import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.WebSocket.Listener;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import com.bouslama.maintenance.PostManagement;
import com.bouslama.maintenance.UsersManagement;

public class frmManagement extends JFrame implements ActionListener {
	public static ArrayList<Post> postsList = new ArrayList<>();
	pnlCharts pnl;
	UsersManagement userManagement = new UsersManagement();
	public static String user_login;
	public static String user_pass;
	public boolean logged_in = true;
	static User user = new User();
	Timer timer = new Timer(20, this);

	JButton create = new JButton("create new post");
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel data_display = new JPanel(new WrapLayout());
	JPanel preview = new JPanel(new MigLayout("wrap"));
	JPanel right_panel = new JPanel();
	JPanel charts_display = new JPanel(new MigLayout());
	JPanel charts = new JPanel();
	JPanel session_managemet = new JPanel(new MigLayout("wrap 2, center"));
	JPanel header = new JPanel(new MigLayout("wrap 2"));

	JLabel welcome = new JLabel();
	JLabel profile_pic = new JLabel();
	JButton private_posts = new JButton("my posts");
	JButton public_posts = new JButton("all posts");
	JButton logout = new JButton("logout");
	JPanel content = new JPanel(new MigLayout("wrap 3"));
	JPanel private_public = new JPanel(new MigLayout("wrap 2"));
	JScrollPane posts_scroll;
	boolean post_editing, changed_parameters;
	JTextArea preview_title = new JTextArea(1, 45);
	JTextArea preview_content = new JTextArea(6, 45);
	String day_option;
	JRadioButton good_day = new JRadioButton("good day", true);
	JRadioButton average_day = new JRadioButton("average day", true);
	JRadioButton bad_day = new JRadioButton("bad day", true);
	JRadioButton private_check = new JRadioButton("private", true);
	JRadioButton public_check = new JRadioButton("public", true);
	ButtonGroup visibility_buttons = new ButtonGroup();
	ButtonGroup day_rate = new ButtonGroup();
	JTextArea create_title = new JTextArea(1, 40);
	JTextArea create_Content = new JTextArea(6, 40);
	HashSet<Integer> users_posts = new HashSet<Integer>();
	JTextArea change_login = new JTextArea();
	JTextArea change_first_name = new JTextArea();
	JTextArea change_last_name = new JTextArea();
	JLabel session_pic = new JLabel();
	JLabel preview_pic = new JLabel();
	int previous_post, pic_changed = 0;
	String new_pic_path;
	JScrollPane pnl_scrol;
	Font preview_font = new Font("SansSerif", Font.BOLD, 14);
	MouseAdapter ma = new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {

			JFileChooser file = new JFileChooser();
			file.setCurrentDirectory(new File(System.getProperty("user.home")));
			file.addChoosableFileFilter(new FileNameExtensionFilter("*.Images", "jpg", "png"));
			int result = file.showSaveDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				post_editing = true;

				File selectedFile = file.getSelectedFile();
				new_pic_path = selectedFile.getAbsolutePath();
				BufferedImage uploaded_img = null;
				try {
					uploaded_img = ImageIO.read(new File(new_pic_path));
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				Image uimg = uploaded_img.getScaledInstance(preview_pic.getWidth(), preview_pic.getHeight(),
						Image.SCALE_SMOOTH);
				ImageIcon user_pic_source = new ImageIcon(uimg);
				preview_pic.setIcon(user_pic_source);
				right_panel.revalidate();
				preview.remove(right_panel);
				preview.add(right_panel, "w 500, height 600!");
				preview.revalidate();

			}

		}

	};

	public frmManagement() {
		// timer.start();
		setSize(1300, 700);

		users_posts.clear();
		user.setuser_login(user_login);
		user.setuser_password(user_pass);

		User us = userManagement.getUser(user);
		pnl = new pnlCharts();

		setVisible(true);
		welcome.setText(us.getuser_name() + " " + us.getuser_last_name());
		preview_title.setBackground(new Color(248, 255, 221));
		preview_title.setFont(preview_font);
		preview_title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(65, 221, 255)));
		preview_content.setBackground(new Color(248, 255, 221));
		preview_content.setFont(preview_font);
		preview_content.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(65, 221, 255)));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		data_display.setBackground(new Color(248, 255, 221));
		JScrollPane charts_101 = new JScrollPane(data_display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tabbedPane.addTab("posts", charts_101);
		pnl = new pnlCharts();
		pnl_scrol = new JScrollPane(pnl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tabbedPane.add("charts", pnl_scrol);

		tabbedPane.revalidate();
		tabbedPane.add("session", session_managemet);

		tabbedPane.revalidate();
		add(tabbedPane);
		logout.addActionListener(this);

		// sessions Management pannel
		good_day.setBackground(new Color(248, 255, 221));
		average_day.setBackground(new Color(248, 255, 221));
		bad_day.setBackground(new Color(248, 255, 221));
		private_check.setBackground(new Color(248, 255, 221));
		public_check.setBackground(new Color(248, 255, 221));
		session_pic.setSize(200, 200);
		get_user_pic(session_pic, us);

		JLabel session_user = new JLabel(us.getuser_name() + " " + us.getuser_last_name());
		JLabel session_login = new JLabel("login");

		JLabel session_first_name = new JLabel("first name");

		JLabel session_last_name = new JLabel("last name");

		change_login.setText(user_login);
		change_login.setEditable(false);
		change_first_name.setEditable(false);
		change_last_name.setEditable(false);
		change_first_name.setText(us.getuser_name());
		change_last_name.setText(us.getuser_last_name());

		JButton session_password = new JButton("change password");
		session_password.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFrame change_password = new JFrame();
				change_password.setResizable(false);
				change_password.setSize(400, 180);
				JPanel password_panel = new JPanel(new MigLayout("wrap 2"));
				JLabel old_password = new JLabel("old password");
				JPasswordField input_old_password = new JPasswordField();
				JLabel new_password = new JLabel("new password");
				JPasswordField input_new_password = new JPasswordField();
				JLabel confirm_password = new JLabel("confirm password");
				JPasswordField input_confirm_password = new JPasswordField();
				JLabel password_error_handler = new JLabel();
				JButton save_password = new JButton("save passowrd");

				input_old_password.setText("");
				input_new_password.setText("");
				input_confirm_password.setText("");
				password_panel.add(old_password);
				password_panel.add(input_old_password, "w 250!, height 25!");
				password_panel.add(new_password);
				password_panel.add(input_new_password, "w 250!, height 25!");
				password_panel.add(confirm_password);
				password_panel.add(input_confirm_password, "w 250!, height 25!");
				password_panel.add(password_error_handler);
				password_panel.add(save_password, "right");
				change_password.add(password_panel);
				change_password.setVisible(true);
				password_error_handler.setForeground(Color.red);
				save_password.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						if (String.valueOf(input_old_password.getPassword()).equals("")
								|| String.valueOf(input_new_password.getPassword()).equals("")
								|| String.valueOf(input_confirm_password.getPassword()).equals("")) {

							password_error_handler.setText("Please fill all fields");

						}

						else {

							try {
								Connection con = MySQLConnection.getConnection();
								String get_password_sql = "SELECT user_password from users where user_login = ?";
								PreparedStatement pst = con.prepareStatement(get_password_sql);
								pst.setString(1, us.getuser_login());
								ResultSet rst = pst.executeQuery();
								rst.next();

								if (!rst.getString(1).equals(String.valueOf(input_old_password.getPassword()))) {
									password_error_handler.setText("old password not matching");

								} else if (!String.valueOf(input_new_password.getPassword())
										.equals(String.valueOf(input_confirm_password.getPassword()))) {

									password_error_handler.setText("not matching password");
									input_confirm_password
											.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.red));

								} else {
									try {

										String update_password_sql = "Update users set user_password = ? where user_login = ?";
										PreparedStatement pst_pwd = con.prepareStatement(update_password_sql);
										pst_pwd.setString(1, String.valueOf(input_confirm_password.getPassword()));
										pst_pwd.setString(2, us.getuser_login());
										pst_pwd.executeUpdate();
										us.setuser_password(String.valueOf(input_confirm_password.getPassword()));
										user_pass = us.getuser_password();
									} catch (Exception exception) {
										exception.printStackTrace();
									}

									change_password.setVisible(false);

								}

							} catch (Exception exceptioin) {
								 exceptioin.printStackTrace();
							}

						}
					}
				});

			}
		});

		JButton modify_session = new JButton("modify Account information");
		JButton save_session = new JButton("save changes");
		session_pic.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.home")));
				file.addChoosableFileFilter(new FileNameExtensionFilter("*.Images", "jpg", "png"));
				int result = file.showSaveDialog(null);

				if (result == JFileChooser.APPROVE_OPTION) {
					pic_changed = 1;

					 
					File selectedFile = file.getSelectedFile();
					new_pic_path = selectedFile.getAbsolutePath();
					BufferedImage uploaded_img = null;
					try {
						uploaded_img = ImageIO.read(new File(new_pic_path));
					} catch (Exception exception) {
						exception.printStackTrace();
					}
					Image uimg = uploaded_img.getScaledInstance(session_pic.getWidth(), session_pic.getHeight(),
							Image.SCALE_SMOOTH);
					ImageIcon user_pic_source = new ImageIcon(uimg);
					session_pic.setIcon(user_pic_source);

				} else {

					return;

				}

			}

		});
		session_managemet.add(session_pic);
		session_managemet.add(session_user, "wrap");
		session_managemet.add(session_login);
		session_managemet.add(change_login, "w 250!, height 25!");
		session_managemet.add(session_first_name);
		session_managemet.add(change_first_name, "w 250!, height 25!");
		session_managemet.add(session_last_name);
		session_managemet.add(change_last_name, "w 250!, height 25!");
		session_managemet.add(session_password, "center, wrap");
		modify_session.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				change_login.setEditable(true);
				change_first_name.setEditable(true);
				change_last_name.setEditable(true);

			}
		});

		save_session.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (change_login.getText().equals(us.getuser_login())
						&& change_first_name.getText().equals(us.getuser_name())
						&& change_last_name.getText().equals(us.getuser_last_name()) && pic_changed == 0) {
					return;
				} else {

					security(us);

					charts_display.remove(posts_scroll);
					charts_display.revalidate();
					PostManagement postManagement = new PostManagement();

					Post in_post = postManagement.getPulicPost();
					content.removeAll();
					content.revalidate();
					display_posts();
					posts_scroll = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					charts_display.add(posts_scroll, "height 450!");

				}

			}
		});
		tabbedPane.setFocusable(true);
		session_managemet.setFocusable(true);
		// session_managemet.addFocusListener(this);
		session_managemet.add(modify_session, "center");
		session_managemet.add(save_session, "center");
		session_managemet.add(logout, "span 2, center");

		/// chartss display
		PostManagement postManagement = new PostManagement();
		Post in_post = postManagement.getPulicPost();
		public_posts.setFont(new Font("Arial", Font.BOLD, 14));
		public_posts.setBackground(Color.CYAN);
		public_posts.setForeground(Color.white);
		private_posts.setFont(new Font("Arial", Font.BOLD, 14));
		private_posts.setBackground(Color.white);
		private_posts.setForeground(Color.white);
		private_posts.setBorder(null);
		public_posts.setBorder(null);
		public_posts.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				public_posts.setBackground(Color.black);
				public_posts.setForeground(Color.white);
			}

			public void mouseExited(MouseEvent e) {
				public_posts.setBackground(Color.cyan);

			}

		});
		private_posts.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				private_posts.setBackground(Color.black);

			}

			public void mouseExited(MouseEvent e) {
				private_posts.setBackground(Color.white);

			}

		});

		try {
			Connection con = MySQLConnection.getConnection();
			String getting_user_pic = "select user_pic from users where user_login = ?;";
			PreparedStatement pst = con.prepareStatement(getting_user_pic);
			pst.setString(1, us.getuser_login());
			ResultSet rs = pst.executeQuery();
			rs.next();
			if (us.getUser_pic() == null) {
				// ImageIcon default_pic = new ImageIcon();

				BufferedImage default_img = ImageIO.read(new File("files/user_default_pic.jpg"));

				Image dimg = default_img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

				ImageIcon default_pic_source = new ImageIcon(dimg);
				profile_pic.setIcon(default_pic_source);

			} else {

				Blob blob = us.getUser_pic();
				InputStream in = blob.getBinaryStream();
				BufferedImage user_img = ImageIO.read(in);
				Image dimg = user_img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
				ImageIcon default_pic_source = new ImageIcon(dimg);
				profile_pic.setIcon(default_pic_source);
				//

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		charts_display.setPreferredSize(new Dimension(500, 650));
		data_display.add(charts_display);
		//

		data_display.add(preview);

		header.add(profile_pic, "w 100!, height 100!");
		welcome.setFont(new Font("HelvericaNeue-Medium", Font.PLAIN, 18));
		header.add(welcome);
		charts_display.add(header, "wrap");
		private_public.add(private_posts, "w 120!, height 30!");
		private_posts.addActionListener(this);
		private_public.add(public_posts, "w 120!, height 30!");
		public_posts.addActionListener(this);
		charts_display.add(private_public, "wrap");
		charts_display.setBackground(new Color(248, 255, 221));
		header.setBackground(new Color(248, 255, 221));
		private_public.setBackground(new Color(248, 255, 221));
		right_panel.setBackground(new Color(248, 255, 221));
		preview.setBackground(new Color(248, 255, 221));
		create.setFont(new Font("Helvetica", Font.BOLD, 16));
		create.setBackground(new Color(0, 201, 225));
		create.setBorder(BorderFactory.createLineBorder(new Color(0, 201, 225), 10));
		create.setForeground(Color.white);
		create.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				create.setBackground(Color.white);
				create.setForeground(Color.black);
				create.setBorder(BorderFactory.createLineBorder(Color.white, 10));
			}

			public void mouseExited(MouseEvent e) {
				create.setBorder(BorderFactory.createLineBorder(new Color(0, 201, 225), 10));
				create.setBackground(new Color(0, 201, 225));
				create.setForeground(Color.white);
			}

		});
		preview.add(create, "left");
		// preview.setBackground(Color.green);
		// right_panel.setBackground(Color.magenta);
		right_panel.setPreferredSize(new Dimension(600, 650));
		preview.add(right_panel, "w 500, height 600!");
		post_editing = false;
		bad_day.setActionCommand("1");
		average_day.setActionCommand("2");
		good_day.setActionCommand("3");
		private_check.setActionCommand("0");
		public_check.setActionCommand("1");
		preview_content.setEditable(false);
		preview_title.setEditable(false);
		previous_post = -1;
		display_posts();
		posts_scroll = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		charts_display.add(posts_scroll, "w 500!, height 450!");
		create.addActionListener(this);
		content.setBackground(new Color(248, 255, 221));

	}

	private void get_post_writer(Post writer_post, JLabel writer_pic, JLabel writer_name) {

		try {
			Connection con = MySQLConnection.getConnection();
			String getting_user_pic = "select user_pic, user_name, user_last_name from users where user_login = ?;";
			PreparedStatement pst = con.prepareStatement(getting_user_pic);
			pst.setString(1, writer_post.getuser_login());
			ResultSet rs = pst.executeQuery();
			rs.next();
			if (rs.getBlob(1) == null) {
				// ImageIcon default_pic = new ImageIcon();

				BufferedImage default_img = ImageIO.read(new File("files/user_default_pic.jpg"));

				Image dimg = default_img.getScaledInstance(writer_pic.getWidth(), writer_pic.getHeight(),
						Image.SCALE_SMOOTH);

				ImageIcon default_pic_source = new ImageIcon(dimg);
				writer_pic.setIcon(default_pic_source);

			} else {

				Blob blob = rs.getBlob(1);
				InputStream in = blob.getBinaryStream();
				BufferedImage user_img = ImageIO.read(in);
				Image dimg = user_img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				ImageIcon default_pic_source = new ImageIcon(dimg);
				writer_pic.setIcon(default_pic_source);

			}
			writer_name.setText(rs.getString(2) + " " + rs.getString(3));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void get_user_pic(JLabel IconLabel, User user_check) {

		try {

			if (user_check.getUser_pic() == null) {
				// ImageIcon default_pic = new ImageIcon();

				BufferedImage default_img = ImageIO.read(new File("files/user_default_pic.jpg"));

				Image dimg = default_img.getScaledInstance(IconLabel.getWidth(), IconLabel.getHeight(),
						Image.SCALE_SMOOTH);

				ImageIcon default_pic_source = new ImageIcon(dimg);
				IconLabel.setIcon(default_pic_source);

			} else {

				Blob blob = user_check.getUser_pic();
				InputStream in = blob.getBinaryStream();
				BufferedImage user_img = ImageIO.read(in);
				Image dimg = user_img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
				ImageIcon default_pic_source = new ImageIcon(dimg);
				IconLabel.setIcon(default_pic_source);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void security(User user_check) {
		JPasswordField password_check = new JPasswordField();
		int security_check = JOptionPane.showConfirmDialog(null, password_check, "enter password",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (security_check == JOptionPane.OK_OPTION) {

			if (!String.valueOf(password_check.getPassword()).equals(user_check.getuser_password())) {

				JOptionPane.showMessageDialog(this, "shit");
				security(user_check);

			} else {
				try {

					Connection con = MySQLConnection.getConnection();
					Statement stmt_disable = con.createStatement();
					

					stmt_disable.execute("SET FOREIGN_KEY_CHECKS=0");
					stmt_disable.close();

					String sql_session = "update users set user_login = ?, user_name = ?, user_last_name = ?, user_pic = ? where user_login = ?;";
					PreparedStatement pst_session = con.prepareStatement(sql_session);
					pst_session.setString(1, change_login.getText());
					pst_session.setString(2, change_first_name.getText());
					pst_session.setString(3, change_last_name.getText());
					if (new_pic_path != null) {
						InputStream new_image = new FileInputStream(new_pic_path);
						pst_session.setBlob(4, new_image);
					} else if (new_pic_path == null) {
						pst_session.setBlob(4, user_check.getUser_pic());
					}

					pst_session.setString(5, user_check.getuser_login());
					pst_session.executeUpdate();

					Statement stmt_enable = con.createStatement();
					stmt_enable.execute("SET FOREIGN_KEY_CHECKS=1");
					stmt_enable.close();
					String sql_session_post = "update posts set user_login = ? where user_login = ?;";
					PreparedStatement pst_post = con.prepareStatement(sql_session_post);
					pst_post.setString(1, change_login.getText());
					pst_post.setString(2, user_check.getuser_login());
					pst_post.executeUpdate();

					user_check.setuser_login(change_login.getText());
					user_check.setuser_name(change_first_name.getText());
					user_check.setuser_last_name(change_last_name.getText());
					String getting_user_pic = "select user_pic from users where user_login = ?;";
					PreparedStatement pspic = con.prepareStatement(getting_user_pic);
					pspic.setString(1, user_check.getuser_login());
					ResultSet rspic = pspic.executeQuery();
					rspic.next();
					user_check.setUser_pic(rspic.getBlob(1));
					new_pic_path = null;

					// user_check.setUser_pic(Ioutils.toByteArray());
					welcome.setText(user_check.getuser_name() + " " + user_check.getuser_last_name());

					user_login = change_login.getText();
					user.setuser_login(user_login);
					user.setuser_password(user_pass);

					change_first_name.setText(user_check.getuser_name());
					change_last_name.setText(user_check.getuser_last_name());
					charts_display.remove(posts_scroll);
					charts_display.revalidate();

					PostManagement postManagement = new PostManagement();
					Post in_post = postManagement.getPulicPost();
					content.removeAll();
					content.revalidate();
					display_posts();
					get_user_pic(profile_pic, user_check);

				}

				catch (Exception exception) {
					exception.printStackTrace();
				}

			}

		} else {
			user.setuser_login(user_login);
			user.setuser_password(user_pass);

			User us = userManagement.getUser(user);
			change_login.setText(us.getuser_login());
			change_first_name.setText(us.getuser_name());
			change_last_name.setText(us.getuser_last_name());
			get_user_pic(session_pic, user_check);
			pic_changed = 0;

		}

	}

	private void display_posts() {

		content.removeAll();
		content.revalidate();
		user.setuser_login(user_login);
		user.setuser_password(user_pass);

		User us = userManagement.getUser(user);

		try {
			Connection con = MySQLConnection.getConnection();
			String sql_my_posts = "select post_id from posts where user_login = ?;";
			PreparedStatement pst = con.prepareStatement(sql_my_posts);
			pst.setString(1, us.getuser_login());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				users_posts.add(rs.getInt(1));

			}

		} catch (Exception e) {
			
		}

		if (postsList.isEmpty()) {
			JLabel empty = new JLabel("there are no posts available");
			empty.setSize(20, 150);
			content.add(empty);

		} else {

			for (Post post : postsList) {

				JPanel post_panel = new JPanel(new MigLayout("wrap 2"));
				post_panel.setBorder(null);
				JPanel user_writer = new JPanel(new MigLayout("wrap 2"));
				JLabel writer_name_charts = new JLabel();
				JLabel writer_pic_charts = new JLabel();

				JLabel ppst_pic = new JLabel();

				JLabel title = new JLabel(post.getPost_title());
				JLabel date = new JLabel(String.valueOf(post.getPost_date()));
				title.setFont(new Font("SansSerif", Font.BOLD, 16));

				title.setForeground(Color.white);
				title.setVerticalAlignment(JLabel.TOP);

				JLabel post_content = new JLabel();
				post_content.setFont(new Font("SansSerif", Font.PLAIN, 15));
				post_content.setText("<html>" + post.getPost_content() + "</html>");

				post_content.setVerticalAlignment(JLabel.TOP);
				writer_pic_charts.setSize(50, 50);
				get_post_writer(post, writer_pic_charts, writer_name_charts);
				user_writer.add(writer_pic_charts);
				user_writer.add(writer_name_charts);

				post_panel.add(user_writer, "wrap");
				post_panel.add(title, "w 150!,height 20!");
				post_panel.add(date, "wrap, right");

				post_panel.add(post_content, "span 2,  wrap, w 450!, height 40!");
				post_panel.add(ppst_pic, "center, wrap");
				if (post.getPost_pic() != null) {

					Blob blob = post.getPost_pic();
					try {
						InputStream in = blob.getBinaryStream();
						BufferedImage user_img = ImageIO.read(in);

						Image dimg = user_img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
						ImageIcon default_pic_source = new ImageIcon(dimg);
						ppst_pic.setIcon(default_pic_source);

					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
				if (post.getPost_rate().equals("3")) {

					post_panel.setBackground(new Color(84, 251, 84));
					user_writer.setBackground(new Color(84, 251, 84));

				} else if (post.getPost_rate().equals("2")) {
					post_panel.setBackground(new Color(74, 74, 202));
					user_writer.setBackground(new Color(74, 74, 202));

				} else if (post.getPost_rate().equals("1")) {

					post_panel.setBackground(new Color(251, 84, 84));
					user_writer.setBackground(new Color(251, 84, 84));
				}
				post_panel.addMouseListener(new MouseAdapter() {

					int previous_pic = 0;

					@Override
					public void mouseClicked(MouseEvent e) {
						if(previous_post != -1 && ( preview_title.getText().isEmpty() || preview_content.getText().isEmpty())) {
							preview_title.setBackground(Color.white);
							preview_content.setBackground(Color.white);
						}
						else {
						if (!create_title.getText().equals("") || !create_Content.getText().equals("")) {

							int confirmation = JOptionPane.showInternalConfirmDialog(null, "all changes will be lost, return and save",
									"Ignore modification", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (confirmation == JOptionPane.YES_OPTION) {
								create_title.setText("");
								create_Content.setText("");
								post_editing = false;
								new_pic_path = null;

							} else {
								return;

							}

						}

						if (create_title.getText().equals("") && create_Content.getText().equals("")) {
							if (post.getPost_id() == previous_post) {
								return;
							} else {
								if (previous_post == -1 || !users_posts.contains(previous_post)) {
									post_editing = false;
								}

								if (users_posts.contains(previous_post)) {

									try {
										
										Connection con = MySQLConnection.getConnection();
										String rte_check = "select post_title, post_content, post_rate, post_visibility from posts where post_id = ?;";
										PreparedStatement pst = con.prepareStatement(rte_check);
										pst.setInt(1, previous_post);

										ResultSet rs = pst.executeQuery();
										rs.next();
										//  (rs.getString(1));
										 
										if (!rs.getString(1).equals(preview_title.getText())
												|| !rs.getString(2).equals(preview_content.getText())
												|| !rs.getString(3).equals(day_rate.getSelection().getActionCommand())
												|| !rs.getString(4)
														.equals(visibility_buttons.getSelection().getActionCommand())) {
											post_editing = true;
											 
										} else if (rs.getString(1).equals(preview_title.getText())
												&& !rs.getString(2).equals(preview_content.getText())
												&& rs.getString(3).equals(day_rate.getSelection().getActionCommand())
												&& rs.getString(4).equals(visibility_buttons.getSelection()
														.getActionCommand())/*
																			 * rs.getString(1).equals(preview_title.
																			 * getText()) &&
																			 * rs.getString(2).equals(preview_content.
																			 * getText()) &&
																			 * rs.getString(3).equals(day_rate.
																			 * getSelection().getActionCommand()) &&
																			 * rs.getString(4).equals(visibility_buttons
																			 * .getSelection().getActionCommand())
																			 */) {
											
											post_editing = false;

										}
									} catch (Exception exception) {
										exception.printStackTrace();

									}
								}

							}
							if (post_editing) {
								int confirmation = JOptionPane.showInternalConfirmDialog(null,
										"<html>all changes will be lost, return and save</html>",
										"drop post?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if (confirmation == JOptionPane.YES_OPTION) {
									post_editing = false;
									new_pic_path = null;

								} else {
									post_editing = true;

								}
							}
							if (!post_editing) {

								preview.remove(right_panel);

								right_panel.removeAll();
								right_panel.revalidate();
								preview.add(right_panel);
								preview.revalidate();
								preview_title.setBackground(new Color(248, 255, 221));
								preview_content.setBackground(new Color(248, 255, 221));
								right_panel.setLayout(new MigLayout("wrap 2"));
								if (!users_posts.contains(post.getPost_id())) {

									right_panel.remove(preview_pic);
									for (MouseListener lister : preview_pic.getMouseListeners()) {
										preview_pic.removeMouseListener(lister);
									}
									previous_post = post.getPost_id();
									JPanel preview_writer_info = new JPanel();
									JLabel preview_writer_pic = new JLabel();
									JLabel preview_writer = new JLabel();
									preview_writer_pic.setSize(50, 50);
									get_post_writer(post, preview_writer_pic, preview_writer);
									preview_writer_info.add(preview_writer_pic);
									preview_writer_info.add(preview_writer);
									JLabel preview_date = new JLabel(post.getPost_date());
									preview_title.setEditable(false);
									preview_title.setWrapStyleWord(true);
									preview_title.setLineWrap(true);
									preview_content.setFont(new Font("SansSerif", Font.ITALIC, 12));

									preview_content.setWrapStyleWord(true);
									preview_content.setLineWrap(true);
									preview_content.setFont(new Font("SansSerif", Font.ITALIC, 12));

									preview_content.setWrapStyleWord(true);
									preview_content.setLineWrap(true);

									right_panel.add(preview_writer_info);
									right_panel.add(preview_date, "span 2, right, wrap");
									preview_title.setColumns(50);
									preview_content.setColumns(50);
									right_panel.add(preview_title, "span 2, wrap");
									right_panel.add(preview_content, "span 2, wrap");
									if (post.getPost_pic() != null) {
										Blob blob = post.getPost_pic();
										try {
											InputStream in = blob.getBinaryStream();
											BufferedImage user_img = ImageIO.read(in);
											preview_pic.setSize(250, 250);
											Image dimg = user_img.getScaledInstance(preview_pic.getWidth(),
													preview_pic.getHeight(), Image.SCALE_SMOOTH);
											ImageIcon default_pic_source = new ImageIcon(dimg);
											right_panel.remove(preview_pic);
											right_panel.add(preview_pic, "center, wrap");
											preview_pic.setIcon(default_pic_source);

											right_panel.revalidate();

										} catch (Exception exception) {
											exception.printStackTrace();
										}
									}
									preview_title.setText(post.getPost_title());
									preview_content.setText(post.getPost_content());

									right_panel.revalidate();
								} else if (users_posts.contains(post.getPost_id())) {

									previous_post = post.getPost_id();

									JButton save_changes = new JButton("save");
									JPanel preview_writer_info = new JPanel();
									JLabel preview_writer_pic = new JLabel();
									JLabel preview_writer = new JLabel();
									preview_writer_pic.setSize(50, 50);
									get_post_writer(post, preview_writer_pic, preview_writer);
									preview_writer_info.add(preview_writer_pic);
									preview_writer_info.add(preview_writer);
									JLabel preview_date = new JLabel(post.getPost_date());
									JButton modify_title = new JButton("1");

									JButton modify_content = new JButton("2");

									preview_title.setEditable(false);
									preview_title.setWrapStyleWord(true);
									preview_title.setLineWrap(true);
									preview_content.setFont(new Font("SansSerif", Font.ITALIC, 12));

									preview_content.setWrapStyleWord(true);
									preview_content.setLineWrap(true);
									right_panel.add(preview_writer_info);
									right_panel.add(preview_date, "right");
									preview_title.setColumns(50);
									preview_content.setColumns(50);
									right_panel.add(preview_title, "growx, pushx");
									right_panel.add(modify_title, "right, wrap");
									right_panel.add(preview_content, "growx, pushx");
									right_panel.add(modify_content, "right, wrap");

									visibility_buttons.add(public_check);
									visibility_buttons.add(private_check);
									bad_day.setActionCommand("1");
									average_day.setActionCommand("2");
									good_day.setActionCommand("3");
									day_rate.add(good_day);
									day_rate.add(average_day);
									day_rate.add(bad_day);
									JPanel modify_day_rate = new JPanel(new MigLayout("wrap 3"));
									modify_day_rate.setBackground(new Color(248, 255, 221));
									good_day.setBackground(new Color(248, 255, 221));
									average_day.setBackground(new Color(248, 255, 221));
									bad_day.setBackground(new Color(248, 255, 221));
									private_check.setBackground(new Color(248, 255, 221));
									public_check.setBackground(new Color(248, 255, 221));
									modify_day_rate.add(good_day, "left");
									modify_day_rate.add(average_day, "right");
									modify_day_rate.add(bad_day, "left");
									right_panel.add(modify_day_rate, "wrap, center");
									// day_rate.clearSelection();
									preview_title.setText(post.getPost_title());
									preview_content.setText(post.getPost_content());
									if (post.getPost_rate().equals("1")) {
										day_rate.setSelected(bad_day.getModel(), true);

									}
									if (post.getPost_rate().equals("2")) {
										day_rate.setSelected(average_day.getModel(), true);

									}
									if (post.getPost_rate().equals("3")) {
										day_rate.setSelected(good_day.getModel(), true);

									}

									JPanel modify_visibility = new JPanel(new MigLayout("wrap 2"));
									modify_visibility.setBackground(new Color(248, 255, 221));
									modify_visibility.add(private_check);
									modify_visibility.add(public_check);
									right_panel.add(modify_visibility, "wrap, center");
									visibility_buttons.clearSelection();

									if (post.getPost_visibility().equals("0")) {
										visibility_buttons.setSelected(private_check.getModel(), true);
									}
									if (post.getPost_visibility().equals("1")) {
										visibility_buttons.setSelected(public_check.getModel(), true);
									}

									modify_title.addActionListener(new ActionListener() {

										@Override
										public void actionPerformed(ActionEvent arg0) {

											preview_title.setEditable(true);
											preview_title.setBorder(
													BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
											preview_content.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
													new Color(65, 221, 255)));
											preview_title.setFocusable(true);
											preview_title.requestFocus(true);

										}
									});
									
									
									modify_content.addActionListener(new ActionListener() {

										@Override
										public void actionPerformed(ActionEvent arg0) {
											preview_content.setEditable(true);
											
											preview_content.setBorder(
													BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
											preview_title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
													new Color(65, 221, 255)));
											preview_title.requestFocus(false);
											preview_content.setFocusable(true);
											preview_content.requestFocus(true);
											
										}
									});
									

									if (post.getPost_pic() != null) {
										Blob blob = post.getPost_pic();
										try {
											InputStream in = blob.getBinaryStream();
											BufferedImage user_img = ImageIO.read(in);
											preview_pic.setSize(250, 250);
											Image dimg = user_img.getScaledInstance(preview_pic.getWidth(),
													preview_pic.getHeight(), Image.SCALE_SMOOTH);
											ImageIcon default_pic_source = new ImageIcon(dimg);

											preview_pic.setIcon(default_pic_source);

											right_panel.remove(preview_pic);
											right_panel.add(preview_pic, "center, wrap");
											right_panel.add(save_changes);
											right_panel.revalidate();

										} catch (Exception exception) {
											exception.printStackTrace();
										}
										for (MouseListener lister : preview_pic.getMouseListeners()) {
											preview_pic.removeMouseListener(lister);
										}

										preview_pic.addMouseListener(ma);
									} else if (post.getPost_pic() == null) {
										JButton add_pic = new JButton("add pic");
										add_pic.addActionListener(new ActionListener() {

											@Override
											public void actionPerformed(ActionEvent arg0) {
												JFileChooser file = new JFileChooser();
												file.setCurrentDirectory(new File(System.getProperty("user.home")));
												file.addChoosableFileFilter(
														new FileNameExtensionFilter("*.Images", "jpg", "png"));
												int result = file.showSaveDialog(null);

												if (result == JFileChooser.APPROVE_OPTION) {
													post_editing = true;

													File selectedFile = file.getSelectedFile();
													new_pic_path = selectedFile.getAbsolutePath();
													BufferedImage uploaded_img = null;
													try {
														uploaded_img = ImageIO.read(new File(new_pic_path));
													} catch (Exception exception) {
														exception.printStackTrace();
													}
													preview_pic.setSize(250, 250);
													Image uimg = uploaded_img.getScaledInstance(preview_pic.getWidth(),
															preview_pic.getHeight(), Image.SCALE_SMOOTH);
													ImageIcon user_pic_source = new ImageIcon(uimg);
													preview_pic.setIcon(user_pic_source);
													right_panel.add(preview_pic);
													right_panel.add(save_changes);
													for (MouseListener lister : preview_pic.getMouseListeners()) {
														preview_writer_pic.removeMouseListener(lister);
														;

													}
													for (MouseListener lister : preview_pic.getMouseListeners()) {
														preview_writer_pic.removeMouseListener(lister);

													}
													for (MouseListener lister : preview_pic.getMouseListeners()) {
														preview_pic.removeMouseListener(lister);
													}
													preview_pic.addMouseListener(new MouseAdapter() {
														public void mouseClicked(MouseEvent e) {

															JFileChooser file = new JFileChooser();
															file.setCurrentDirectory(
																	new File(System.getProperty("user.home")));
															file.addChoosableFileFilter(new FileNameExtensionFilter(
																	"*.Images", "jpg", "png"));
															int result = file.showSaveDialog(null);

															if (result == JFileChooser.APPROVE_OPTION) {
																post_editing = true;

																File selectedFile = file.getSelectedFile();
																new_pic_path = selectedFile.getAbsolutePath();
																BufferedImage uploaded_img = null;
																try {
																	uploaded_img = ImageIO.read(new File(new_pic_path));
																} catch (Exception exception) {
																	exception.printStackTrace();
																}
																Image uimg = uploaded_img.getScaledInstance(
																		preview_pic.getWidth(), preview_pic.getHeight(),
																		Image.SCALE_SMOOTH);
																ImageIcon user_pic_source = new ImageIcon(uimg);
																preview_pic.setIcon(user_pic_source);
																right_panel.add(preview_pic);
																right_panel.add(save_changes);

															} else {

																return;

															}

														}

													});
													// right_panel.remove(preview_pic);
													// right_panel.add(preview_pic, "center, wrap");
													previous_pic = 0;

													right_panel.add(save_changes);
													right_panel.remove(add_pic);
													right_panel.revalidate();
													preview.add(right_panel, "w 500, height 600!");
												}

											}
										});
										right_panel.add(add_pic, "wrap");

									}
									right_panel.add(save_changes, "span 2, right, wrap");
									save_changes.addActionListener(new ActionListener() {

										@Override
										public void actionPerformed(ActionEvent arg0) {
											post_editing = false;
											try {
												Connection con = MySQLConnection.getConnection();
												String sql_modify = "update posts set post_title = ?, post_content = ?, post_rate = ?, post_visibility = ? where post_id = ?;";
												PreparedStatement pst = con.prepareStatement(sql_modify);

												pst.setString(1, preview_title.getText());
												pst.setString(2, preview_content.getText());
												pst.setString(3, day_rate.getSelection().getActionCommand());
												pst.setString(4, visibility_buttons.getSelection().getActionCommand());
												pst.setInt(5, post.getPost_id());
												pst.executeUpdate();

												post.setPost_title(preview_title.getText());
												post.setPost_content(preview_content.getText());
												post.setPost_rate(day_rate.getSelection().getActionCommand());
												post.setPost_visibility(
														visibility_buttons.getSelection().getActionCommand());
												if (new_pic_path != null) {
													
													sql_modify = "update posts set post_pic = ? where post_id = ?;";
													pst = con.prepareStatement(sql_modify);
													InputStream post_image = new FileInputStream(new_pic_path);
													pst.setBlob(1, post_image);
													pst.setInt(2, post.getPost_id());
													pst.executeUpdate();
													String update_image = "select post_pic from posts where post_id = ?;";
													PreparedStatement uisp = con.prepareStatement(update_image);
													uisp.setInt(1, post.getPost_id());
													ResultSet uirst = uisp.executeQuery();
													uirst.next();
													post.setPost_pic(uirst.getBlob(1));
													BufferedImage uploaded_img = null;
													try {
														uploaded_img = ImageIO.read(new File(new_pic_path));
													} catch (Exception exception) {
														exception.printStackTrace();
													}
													preview_pic.setSize(250, 250);
													Image uimg = uploaded_img.getScaledInstance(preview_pic.getWidth(),
															preview_pic.getHeight(), Image.SCALE_SMOOTH);
													ImageIcon user_pic_source = new ImageIcon(uimg);
													ppst_pic.setIcon(user_pic_source);
													new_pic_path = null;

												}

											} catch (Exception e) {
												e.printStackTrace();
											}

											new_pic_path = null;

											preview_title.setEditable(false);
											preview_content.setEditable(false);

											charts_display.remove(posts_scroll);
											charts_display.revalidate();
											PostManagement postManagement = new PostManagement();
											Post usersPost = new Post();
											usersPost.setuser_login(user.getuser_login());
											Post private_post = postManagement.getPrivatePost(usersPost);
											tabbedPane.remove(pnl_scrol);
											pnl = new pnlCharts();
											pnl_scrol = new JScrollPane(pnl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
													JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

											tabbedPane.add("charts", pnl_scrol);

											tabbedPane.add("session", session_managemet);
											tabbedPane.revalidate();
											content.removeAll();
											content.revalidate();
											display_posts();
											posts_scroll = new JScrollPane(content,
													JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
													JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
											charts_display.add(posts_scroll, "w 500!,height 450!");

										}

									});

								}

								right_panel.revalidate();
								preview.add(right_panel, "w 500, height 600!");
								
							}
						}

						right_panel.revalidate();
						preview.add(right_panel, "w 500, height 600!");
					}

					}

					public void mouseEntered(MouseEvent e) {
						post_panel.setBackground(new Color(167, 173, 187));
						user_writer.setBackground(new Color(167, 173, 187));
					}

					public void mouseExited(MouseEvent e) {
						if (post.getPost_rate().equals("3")) {

							post_panel.setBackground(new Color(84, 251, 84));
							user_writer.setBackground(new Color(84, 251, 84));

						} else if (post.getPost_rate().equals("2")) {
							post_panel.setBackground(new Color(74, 74, 202));
							user_writer.setBackground(new Color(74, 74, 202));

						} else if (post.getPost_rate().equals("1")) {

							post_panel.setBackground(new Color(251, 84, 84));
							user_writer.setBackground(new Color(251, 84, 84));
						}
					}

				});

				if (us.getuser_login().equals(post.getuser_login())) {

					JButton delete = new JButton("delete");
					delete.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {

							int delete_post = JOptionPane.showInternalConfirmDialog(null, "delete?", "delete?",
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

							if (delete_post == JOptionPane.OK_OPTION) {
								if (previous_post != -1 && post.getPost_id() == previous_post) {
									right_panel.removeAll();
									right_panel.revalidate();
									preview.add(right_panel, "w 500, height 600!");
									
									preview.revalidate();
									previous_post = -1;
								}
								try {
									Connection con = MySQLConnection.getConnection();
									String delete_sql = "delete from posts where post_id = ?;";
									PreparedStatement pst = con.prepareStatement(delete_sql);
									pst.setInt(1, post.getPost_id());
									pst.executeUpdate();

								} catch (Exception exception) {

								}
								charts_display.remove(posts_scroll);
								charts_display.revalidate();

								PostManagement postManagement = new PostManagement();
								Post usersPost = new Post();
								usersPost.setuser_login(user.getuser_login());
								Post private_post = postManagement.getPrivatePost(usersPost);
								content.removeAll();
								content.revalidate();
								display_posts();
								posts_scroll = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
								charts_display.add(posts_scroll, "w 500!,height 450!");

							} else {
								return;
							}
						}
					});

					post_panel.add(delete, "span 4, right");
				}

				content.add(post_panel, "wrap");

				// charts_display.setBackground(Color.yellow);

			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (tabbedPane.getSelectedIndex() == 2) {

			user.setuser_login(user_login);
			user.setuser_password(user_pass);

			User us = userManagement.getUser(user);
			change_login.setText(us.getuser_login());
			change_first_name.setText(us.getuser_name());
			change_last_name.setText(us.getuser_last_name());
			get_user_pic(session_pic, us);

		}
		if (e.getActionCommand() == "logout") {
			dispose();
			frmLogin frame = new frmLogin();

		}
		if (e.getActionCommand() == "create new post") {
			if (previous_post == -1 || !users_posts.contains(previous_post)) {
				previous_post = -1;
				new_pic_path = null;

				
				
					right_panel.removeAll();
					right_panel.revalidate();
					preview.remove(right_panel);
					preview.add(right_panel);
					preview.revalidate();
					preview.add(right_panel, "w 500, height 600!");
					preview.revalidate();
					create_title.setText("");
					create_Content.setText("");
					JLabel title_sub = new JLabel("title");
					JLabel content_sub = new JLabel("how was your day");
					JLabel day_sub = new JLabel("booy");

					create_title.setLineWrap(true);
					create_title.setWrapStyleWord(true);
					create_Content.setBorder(
							BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
					create_Content.setBackground(new Color(248, 255, 221));
					create_Content.setLineWrap(true);
					create_title.setWrapStyleWord(true);
					create_title.setWrapStyleWord(true);
					JScrollPane content_creation_scroll = new JScrollPane(create_Content,
							JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					JButton create_pic = new JButton("add pic");
					JLabel created_post_pic = new JLabel();
					JButton SubmitPost = new JButton("submit");
					UsersManagement userManagement = new UsersManagement();
					JRadioButton good_day = new JRadioButton("good day", true);
					good_day.setBackground(new Color(248, 255, 221));
					JRadioButton average_day = new JRadioButton("average day", true);
					average_day.setBackground(new Color(248, 255, 221));
					JRadioButton bad_day = new JRadioButton("bad day", true);
					bad_day.setBackground(new Color(248, 255, 221));
					JLabel visibility = new JLabel("who do you want to see this");
					
					JRadioButton private_check = new JRadioButton("private", true);
					private_check.setBackground(new Color(248, 255, 221));
					JRadioButton public_check = new JRadioButton("share with others", true);
					public_check.setBackground(new Color(248, 255, 221));
					ButtonGroup visibility_buttons = new ButtonGroup();
					visibility_buttons.add(public_check);
					visibility_buttons.add(private_check);
					right_panel.setLayout(new MigLayout("wrap 3"));
					right_panel.add(title_sub);
					create_title.setBorder(
							BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
					create_title.setBackground(new Color(248, 255, 221));
					right_panel.add(create_title, "span 3, wrap, growx");
					// right_panel.setBackground(Color.magenta);
					content_creation_scroll.setBackground(new Color(248, 255, 221));
					right_panel.add(content_sub);
					right_panel.add(content_creation_scroll, "span 3, wrap, growx");

					ButtonGroup day_rate = new ButtonGroup();
					create_pic.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							JFileChooser file = new JFileChooser();
							file.setCurrentDirectory(new File(System.getProperty("user.home")));
							file.addChoosableFileFilter(
									new FileNameExtensionFilter("*.Images", "jpg", "png"));
							int result = file.showSaveDialog(null);

							if (result == JFileChooser.APPROVE_OPTION) {
								post_editing = true;

								File selectedFile = file.getSelectedFile();
								new_pic_path = selectedFile.getAbsolutePath();
								BufferedImage uploaded_img = null;
								try {
									uploaded_img = ImageIO.read(new File(new_pic_path));
								} catch (Exception exception) {
									exception.printStackTrace();
								}
								created_post_pic.setSize(250, 250);
								Image uimg = uploaded_img.getScaledInstance(created_post_pic.getWidth(),
										created_post_pic.getHeight(), Image.SCALE_SMOOTH);
								ImageIcon user_pic_source = new ImageIcon(uimg);
								created_post_pic.setIcon(user_pic_source);
								created_post_pic.addMouseListener(new MouseAdapter() {
									public void mouseClicked(MouseEvent e) {

										JFileChooser file = new JFileChooser();
										file.setCurrentDirectory(new File(System.getProperty("user.home")));
										file.addChoosableFileFilter(
												new FileNameExtensionFilter("*.Images", "jpg", "png"));
										int result = file.showSaveDialog(null);

										if (result == JFileChooser.APPROVE_OPTION) {
											post_editing = true;

											File selectedFile = file.getSelectedFile();
											new_pic_path = selectedFile.getAbsolutePath();
											BufferedImage uploaded_img = null;
											try {
												uploaded_img = ImageIO.read(new File(new_pic_path));
											} catch (Exception exception) {
												exception.printStackTrace();
											}
											Image uimg = uploaded_img.getScaledInstance(
													created_post_pic.getWidth(),
													created_post_pic.getHeight(), Image.SCALE_SMOOTH);
											ImageIcon user_pic_source = new ImageIcon(uimg);
											created_post_pic.setIcon(user_pic_source);

										} else {

											return;

										}

									}

								});
								right_panel.add(created_post_pic, "center, wrap");
								right_panel.add(SubmitPost);
								right_panel.remove(create_pic);
								right_panel.revalidate();
								preview.remove(right_panel);
								preview.add(right_panel, "w 500, height 600!");
								preview.revalidate();

							}

						}

					});

					day_rate.add(good_day);
					day_rate.add(average_day);
					day_rate.add(bad_day);
					bad_day.setActionCommand("1");
					average_day.setActionCommand("2");
					good_day.setActionCommand("3");
					right_panel.add(day_sub, "wrap");
					right_panel.add(bad_day, "left");
					right_panel.add(average_day, "center");
					right_panel.add(good_day, "center, wrap");
					right_panel.add(visibility, "wrap");
					private_check.setActionCommand("0");
					public_check.setActionCommand("1");

					right_panel.add(public_check);
					right_panel.add(private_check, "wrap, span 3");
					right_panel.add(create_pic, "wrap, center");
					right_panel.add(SubmitPost, "span 3, wrap, right");
					right_panel.revalidate();
					preview.remove(right_panel);
					preview.add(right_panel, "w 500, height 600!");
					preview.revalidate();
					
					SubmitPost.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							if (create_title.getText().isEmpty() || create_Content.getText().isEmpty()) {
								create_title.setBackground(Color.darkGray);
								create_Content.setBackground(Color.darkGray);
							} else {
								post_editing = false;
								try {
									user.setuser_login(user_login);
									user.setuser_password(user_pass);

									User us = userManagement.getUser(user);
									Connection con = MySQLConnection.getConnection();
									 
									String sql_existing_user = "insert into posts(user_login, post_date, post_title, post_content, post_rate, post_visibility, post_pic) values(?, CURDATE(), ?, ?, ?, ?, ?);";
									;
									PreparedStatement pst;
									pst = con.prepareStatement(sql_existing_user);
									pst.setString(1, us.getuser_login());
									pst.setString(2, create_title.getText());
									pst.setString(3, create_Content.getText());
									pst.setString(4, day_rate.getSelection().getActionCommand());
									pst.setString(5, visibility_buttons.getSelection().getActionCommand());

									if (new_pic_path == null) {
										pst.setNull(6, java.sql.Types.BLOB);
									} else if (new_pic_path != null) {
										InputStream created_image = new FileInputStream(new_pic_path);

										pst.setBlob(6, created_image);
									}

									pst.executeUpdate();

								} catch (Exception exe) {
									JOptionPane.showMessageDialog(null, "image heavy");
								}
								PostManagement postManagement = new PostManagement();
								if (visibility_buttons.getSelection().getActionCommand() == "1") {
									Post in_post = postManagement.getPulicPost();
								} else if (visibility_buttons.getSelection().getActionCommand() == "0") {
									Post usersPost = new Post();
									usersPost.setuser_login(user.getuser_login());
									Post in_post = postManagement.getPrivatePost(usersPost);
								}
								create_title.setText("");
								create_Content.setText("");
								right_panel.remove(created_post_pic);
								bad_day.setSelected(true);
								public_check.setSelected(true);
								right_panel.add(create_pic);
								right_panel.add(SubmitPost);
								right_panel.revalidate();
								preview.remove(right_panel);
								preview.add(right_panel, "w 500, height 600!");
								preview.revalidate();
								content.removeAll();
								content.revalidate();
								charts_display.remove(posts_scroll);
								charts_display.revalidate();
								display_posts();
								posts_scroll = new JScrollPane(content,
										JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
								charts_display.add(posts_scroll, "w 550!, height 450!");
								tabbedPane.remove(pnl);
								pnl = new pnlCharts();
								pnl_scrol = new JScrollPane(pnl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

								tabbedPane.add("charts", pnl_scrol);
								tabbedPane.revalidate();
								tabbedPane.add("session", session_managemet);
								tabbedPane.revalidate();
							}

						}
					});
					//right_panel.removeAll();
					right_panel.revalidate();
					preview.remove(right_panel);
					preview.add(right_panel);
					preview.revalidate();
					preview.add(right_panel, "w 500, height 600!");
					preview.revalidate();
			} else {

				try {
					Connection con = MySQLConnection.getConnection();
					String rte_check = "select post_title, post_content, post_rate, post_visibility from posts where post_id = ?;";
					PreparedStatement pst = con.prepareStatement(rte_check);
					pst.setInt(1, previous_post);

					ResultSet rs = pst.executeQuery();
					rs.next();

					if (!rs.getString(1).equals(preview_title.getText() )|| !rs.getString(2).equals(preview_content.getText()) || !rs.getString(3).equals(day_rate.getSelection().getActionCommand()) || !rs.getString(4).equals(visibility_buttons.getSelection().getActionCommand())) {
						int confirmation = JOptionPane.showInternalConfirmDialog(null, "all changes will be lost, return and save",
								"Ignore changes?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (confirmation == JOptionPane.YES_OPTION) {
							
							previous_post = -1;
							new_pic_path = null;

							
							
								right_panel.removeAll();
								right_panel.revalidate();
								preview.remove(right_panel);
								preview.add(right_panel);
								preview.revalidate();
								preview.add(right_panel, "w 500, height 600!");
								preview.revalidate();
								create_title.setText("");
								create_Content.setText("");
								JLabel title_sub = new JLabel("title");
								JLabel content_sub = new JLabel("how was your day");
								JLabel day_sub = new JLabel("booy");

								create_title.setLineWrap(true);
								create_title.setWrapStyleWord(true);
								create_Content.setBorder(
										BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
								create_Content.setBackground(new Color(248, 255, 221));
								create_Content.setLineWrap(true);
								create_title.setWrapStyleWord(true);
								create_title.setWrapStyleWord(true);
								JScrollPane content_creation_scroll = new JScrollPane(create_Content,
										JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
								JButton create_pic = new JButton("add pic");
								JLabel created_post_pic = new JLabel();
								JButton SubmitPost = new JButton("submit");
								UsersManagement userManagement = new UsersManagement();
								JRadioButton good_day = new JRadioButton("good day", true);
								good_day.setBackground(new Color(248, 255, 221));
								JRadioButton average_day = new JRadioButton("average day", true);
								average_day.setBackground(new Color(248, 255, 221));
								JRadioButton bad_day = new JRadioButton("bad day", true);
								bad_day.setBackground(new Color(248, 255, 221));
								JLabel visibility = new JLabel("who do you want to see this");
								
								JRadioButton private_check = new JRadioButton("private", true);
								private_check.setBackground(new Color(248, 255, 221));
								JRadioButton public_check = new JRadioButton("share with others", true);
								public_check.setBackground(new Color(248, 255, 221));
								ButtonGroup visibility_buttons = new ButtonGroup();
								visibility_buttons.add(public_check);
								visibility_buttons.add(private_check);
								right_panel.setLayout(new MigLayout("wrap 3"));
								right_panel.add(title_sub);
								create_title.setBorder(
										BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
								create_title.setBackground(new Color(248, 255, 221));
								right_panel.add(create_title, "span 3, wrap, growx");
								// right_panel.setBackground(Color.magenta);
								content_creation_scroll.setBackground(new Color(248, 255, 221));
								right_panel.add(content_sub);
								right_panel.add(content_creation_scroll, "span 3, wrap, growx");

								ButtonGroup day_rate = new ButtonGroup();
								create_pic.addActionListener(new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent arg0) {
										JFileChooser file = new JFileChooser();
										file.setCurrentDirectory(new File(System.getProperty("user.home")));
										file.addChoosableFileFilter(
												new FileNameExtensionFilter("*.Images", "jpg", "png"));
										int result = file.showSaveDialog(null);

										if (result == JFileChooser.APPROVE_OPTION) {
											post_editing = true;

											File selectedFile = file.getSelectedFile();
											new_pic_path = selectedFile.getAbsolutePath();
											BufferedImage uploaded_img = null;
											try {
												uploaded_img = ImageIO.read(new File(new_pic_path));
											} catch (Exception exception) {
												exception.printStackTrace();
											}
											created_post_pic.setSize(250, 250);
											Image uimg = uploaded_img.getScaledInstance(created_post_pic.getWidth(),
													created_post_pic.getHeight(), Image.SCALE_SMOOTH);
											ImageIcon user_pic_source = new ImageIcon(uimg);
											created_post_pic.setIcon(user_pic_source);
											created_post_pic.addMouseListener(new MouseAdapter() {
												public void mouseClicked(MouseEvent e) {

													JFileChooser file = new JFileChooser();
													file.setCurrentDirectory(new File(System.getProperty("user.home")));
													file.addChoosableFileFilter(
															new FileNameExtensionFilter("*.Images", "jpg", "png"));
													int result = file.showSaveDialog(null);

													if (result == JFileChooser.APPROVE_OPTION) {
														post_editing = true;

														File selectedFile = file.getSelectedFile();
														new_pic_path = selectedFile.getAbsolutePath();
														BufferedImage uploaded_img = null;
														try {
															uploaded_img = ImageIO.read(new File(new_pic_path));
														} catch (Exception exception) {
															exception.printStackTrace();
														}
														Image uimg = uploaded_img.getScaledInstance(
																created_post_pic.getWidth(),
																created_post_pic.getHeight(), Image.SCALE_SMOOTH);
														ImageIcon user_pic_source = new ImageIcon(uimg);
														created_post_pic.setIcon(user_pic_source);

													} else {

														return;

													}

												}

											});
											right_panel.add(created_post_pic, "center, wrap");
											right_panel.add(SubmitPost);
											right_panel.remove(create_pic);
											right_panel.revalidate();
											preview.remove(right_panel);
											preview.add(right_panel, "w 500, height 600!");
											preview.revalidate();

										}

									}

								});

								day_rate.add(good_day);
								day_rate.add(average_day);
								day_rate.add(bad_day);
								bad_day.setActionCommand("1");
								average_day.setActionCommand("2");
								good_day.setActionCommand("3");
								right_panel.add(day_sub, "wrap");
								right_panel.add(bad_day, "left");
								right_panel.add(average_day, "center");
								right_panel.add(good_day, "center, wrap");
								right_panel.add(visibility, "wrap");
								private_check.setActionCommand("0");
								public_check.setActionCommand("1");

								right_panel.add(public_check);
								right_panel.add(private_check, "wrap, span 3");
								right_panel.add(create_pic, "wrap, center");
								right_panel.add(SubmitPost, "span 3, wrap, right");
								right_panel.revalidate();
								preview.remove(right_panel);
								preview.add(right_panel, "w 500, height 600!");
								preview.revalidate();
								
								SubmitPost.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {

										if (create_title.getText().isEmpty() || create_Content.getText().isEmpty()) {
											create_title.setBackground(Color.darkGray);
											create_Content.setBackground(Color.darkGray);
										} else {
											post_editing = false;
											try {
												user.setuser_login(user_login);
												user.setuser_password(user_pass);

												User us = userManagement.getUser(user);
												Connection con = MySQLConnection.getConnection();
												
												String sql_existing_user = "insert into posts(user_login, post_date, post_title, post_content, post_rate, post_visibility, post_pic) values(?, CURDATE(), ?, ?, ?, ?, ?);";
												;
												PreparedStatement pst;
												pst = con.prepareStatement(sql_existing_user);
												pst.setString(1, us.getuser_login());
												pst.setString(2, create_title.getText());
												pst.setString(3, create_Content.getText());
												pst.setString(4, day_rate.getSelection().getActionCommand());
												pst.setString(5, visibility_buttons.getSelection().getActionCommand());

												if (new_pic_path == null) {
													pst.setNull(6, java.sql.Types.BLOB);
												} else if (new_pic_path != null) {
													InputStream created_image = new FileInputStream(new_pic_path);

													pst.setBlob(6, created_image);
												}

												pst.executeUpdate();

											} catch (Exception exe) {
												JOptionPane.showMessageDialog(null, "image heavy");
											}
											PostManagement postManagement = new PostManagement();
											if (visibility_buttons.getSelection().getActionCommand() == "1") {
												Post in_post = postManagement.getPulicPost();
											} else if (visibility_buttons.getSelection().getActionCommand() == "0") {
												Post usersPost = new Post();
												usersPost.setuser_login(user.getuser_login());
												Post in_post = postManagement.getPrivatePost(usersPost);
											}
											create_title.setText("");
											create_Content.setText("");
											right_panel.remove(created_post_pic);
											bad_day.setSelected(true);
											public_check.setSelected(true);
											right_panel.add(create_pic);
											right_panel.add(SubmitPost);
											right_panel.revalidate();
											preview.remove(right_panel);
											preview.add(right_panel, "w 500, height 600!");
											preview.revalidate();
											content.removeAll();
											content.revalidate();
											charts_display.remove(posts_scroll);
											charts_display.revalidate();
											display_posts();
											posts_scroll = new JScrollPane(content,
													JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
													JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
											charts_display.add(posts_scroll, "w 550!, height 450!");
											tabbedPane.remove(pnl);
											pnl = new pnlCharts();
											pnl_scrol = new JScrollPane(pnl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
													JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

											tabbedPane.add("charts", pnl_scrol);
											tabbedPane.revalidate();
											tabbedPane.add("session", session_managemet);
											tabbedPane.revalidate();
										}

									}
								});
								//right_panel.removeAll();
								right_panel.revalidate();
								preview.remove(right_panel);
								preview.add(right_panel);
								preview.revalidate();
								preview.add(right_panel, "w 500, height 600!");
								preview.revalidate();
							
						}
						else {
							return;
						}
					} else if ((rs.getString(1).equals(preview_title.getText())
							&& rs.getString(2).equals(preview_content.getText())
							&& rs.getString(3).equals(day_rate.getSelection().getActionCommand())
							&& rs.getString(4).equals(visibility_buttons.getSelection().getActionCommand()))
							|| !users_posts.contains(previous_post) || previous_post == -1) {
						
						previous_post = -1;
						right_panel.removeAll();
						right_panel.revalidate();
						preview.add(right_panel, "w 500, height 600!");
						preview.revalidate();

						JLabel title_sub = new JLabel("title");
						JLabel content_sub = new JLabel("how was your day");
						JLabel day_sub = new JLabel("booy");

						create_title.setLineWrap(true);
						create_title.setWrapStyleWord(true);
						create_Content.setBorder(
								BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
						create_Content.setBackground(new Color(248, 255, 221));
						create_Content.setLineWrap(true);
						create_title.setWrapStyleWord(true);
						create_title.setWrapStyleWord(true);
						JScrollPane content_creation_scroll = new JScrollPane(create_Content,
								JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
						JButton create_pic = new JButton("add pic");
						JLabel created_post_pic = new JLabel();
						JButton SubmitPost = new JButton("submit");
						UsersManagement userManagement = new UsersManagement();
						JRadioButton good_day = new JRadioButton("good day", true);
						good_day.setBackground(new Color(248, 255, 221));
						JRadioButton average_day = new JRadioButton("average day", true);
						average_day.setBackground(new Color(248, 255, 221));
						JRadioButton bad_day = new JRadioButton("bad day", true);
						bad_day.setBackground(new Color(248, 255, 221));
						JLabel visibility = new JLabel("who do you want to see this");
						JRadioButton private_check = new JRadioButton("private", true);
						private_check.setBackground(new Color(248, 255, 221));
						JRadioButton public_check = new JRadioButton("share with others", true);
						public_check.setBackground(new Color(248, 255, 221));
						ButtonGroup visibility_buttons = new ButtonGroup();
						visibility_buttons.add(public_check);
						visibility_buttons.add(private_check);
						right_panel.setLayout(new MigLayout("wrap 3"));
						right_panel.add(title_sub);
						create_title.setBorder(
								BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
						create_title.setBackground(new Color(248, 255, 221));
						right_panel.add(create_title, "span 3, wrap, growx");
						// right_panel.setBackground(Color.magenta);

						right_panel.add(content_sub);
						right_panel.add(content_creation_scroll, "span 3, wrap, growx");

						ButtonGroup day_rate = new ButtonGroup();
						create_pic.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								JFileChooser file = new JFileChooser();
								file.setCurrentDirectory(new File(System.getProperty("user.home")));
								file.addChoosableFileFilter(new FileNameExtensionFilter("*.Images", "jpg", "png"));
								int result = file.showSaveDialog(null);

								if (result == JFileChooser.APPROVE_OPTION) {
									post_editing = true;

									File selectedFile = file.getSelectedFile();
									new_pic_path = selectedFile.getAbsolutePath();
									BufferedImage uploaded_img = null;
									try {
										uploaded_img = ImageIO.read(new File(new_pic_path));
									} catch (Exception exception) {
										exception.printStackTrace();
									}
									created_post_pic.setSize(250, 250);
									Image uimg = uploaded_img.getScaledInstance(created_post_pic.getWidth(),
											created_post_pic.getHeight(), Image.SCALE_SMOOTH);
									ImageIcon user_pic_source = new ImageIcon(uimg);
									created_post_pic.setIcon(user_pic_source);
									created_post_pic.addMouseListener(new MouseAdapter() {
										public void mouseClicked(MouseEvent e) {

											JFileChooser file = new JFileChooser();
											file.setCurrentDirectory(new File(System.getProperty("user.home")));
											file.addChoosableFileFilter(
													new FileNameExtensionFilter("*.Images", "jpg", "png"));
											int result = file.showSaveDialog(null);

											if (result == JFileChooser.APPROVE_OPTION) {
												post_editing = true;

												File selectedFile = file.getSelectedFile();
												new_pic_path = selectedFile.getAbsolutePath();
												BufferedImage uploaded_img = null;
												try {
													uploaded_img = ImageIO.read(new File(new_pic_path));
												} catch (Exception exception) {
													exception.printStackTrace();
												}
												Image uimg = uploaded_img.getScaledInstance(created_post_pic.getWidth(),
														created_post_pic.getHeight(), Image.SCALE_SMOOTH);
												ImageIcon user_pic_source = new ImageIcon(uimg);
												created_post_pic.setIcon(user_pic_source);

											} else {

												return;

											}

										}

									});
									right_panel.add(created_post_pic, "center, wrap");
									right_panel.add(SubmitPost);
									right_panel.remove(create_pic);
									right_panel.revalidate();
									preview.remove(right_panel);
									preview.add(right_panel, "w 500, height 600!");
									preview.revalidate();

								}

							}

						});

						day_rate.add(good_day);
						day_rate.add(average_day);
						day_rate.add(bad_day);
						bad_day.setActionCommand("1");
						average_day.setActionCommand("2");
						good_day.setActionCommand("3");
						right_panel.add(day_sub, "wrap");
						right_panel.add(bad_day, "left");
						right_panel.add(average_day, "center");
						right_panel.add(good_day, "center, wrap");
						right_panel.add(visibility, "wrap");
						private_check.setActionCommand("0");
						public_check.setActionCommand("1");

						right_panel.add(public_check);
						right_panel.add(private_check, "wrap, span 3");
						right_panel.add(create_pic, "wrap, center");
						right_panel.add(SubmitPost, "span 3, wrap, right");
						right_panel.revalidate();
						preview.remove(right_panel);
						preview.add(right_panel, "w 500, height 600!");
						preview.revalidate();
						SubmitPost.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {

								if (create_title.getText().isEmpty() || create_Content.getText().isEmpty()) {
									create_title.setBackground(Color.lightGray);
									create_Content.setBackground(Color.lightGray);
								} else {

									try {
										user.setuser_login(user_login);
										user.setuser_password(user_pass);

										User us = userManagement.getUser(user);
										Connection con = MySQLConnection.getConnection();
										
										String sql_existing_user = "insert into posts(user_login, post_date, post_title, post_content, post_rate, post_visibility, post_pic) values(?, CURDATE(), ?, ?, ?, ?, ?);";
										;
										PreparedStatement pst;
										pst = con.prepareStatement(sql_existing_user);
										pst.setString(1, us.getuser_login());
										pst.setString(2, create_title.getText());
										pst.setString(3, create_Content.getText());
										pst.setString(4, day_rate.getSelection().getActionCommand());
										pst.setString(5, visibility_buttons.getSelection().getActionCommand());

										if (new_pic_path == null) {
											pst.setNull(6, java.sql.Types.BLOB);
										} else if (new_pic_path != null) {
											InputStream created_image = new FileInputStream(new_pic_path);

											pst.setBlob(6, created_image);
										}

										pst.executeUpdate();

									} catch (Exception exe) {
										JOptionPane.showMessageDialog(null, "image heavy");
									}
									PostManagement postManagement = new PostManagement();
									if (visibility_buttons.getSelection().getActionCommand() == "1") {
										Post in_post = postManagement.getPulicPost();
									} else if (visibility_buttons.getSelection().getActionCommand() == "0") {
										Post usersPost = new Post();
										usersPost.setuser_login(user.getuser_login());
										Post in_post = postManagement.getPrivatePost(usersPost);
									}
									create_title.setText("");
									create_Content.setText("");
									right_panel.remove(created_post_pic);
									bad_day.setSelected(true);
									public_check.setSelected(true);
									right_panel.add(create_pic);
									right_panel.add(SubmitPost);
									right_panel.revalidate();
									preview.remove(right_panel);
									preview.add(right_panel, "w 500, height 600!");
									preview.revalidate();
									content.removeAll();
									content.revalidate();
									charts_display.remove(posts_scroll);
									charts_display.revalidate();
									display_posts();
									posts_scroll = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
											JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
									charts_display.add(posts_scroll, "w 550!, height 450!");
									tabbedPane.remove(pnl);
									pnl_scrol = new JScrollPane(pnl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
											JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

									tabbedPane.add("charts", pnl_scrol);

									tabbedPane.add("session", session_managemet);
								}

							}
						});
						
						right_panel.revalidate();
						preview.remove(right_panel);
						preview.add(right_panel);
						preview.revalidate();
						preview.add(right_panel, "w 500, height 600!");
						preview.revalidate();
					}

				} catch (Exception exception) {
					exception.printStackTrace();

				}

			}
		}
		if (e.getActionCommand() == "all posts") {

			charts_display.remove(posts_scroll);
			charts_display.revalidate();
			PostManagement postManagement = new PostManagement();

			Post in_post = postManagement.getPulicPost();
			content.removeAll();
			content.revalidate();
			display_posts();
			posts_scroll = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			charts_display.add(posts_scroll, "w 500!,height 450!");
		}
		if (e.getActionCommand() == "my posts") {

			charts_display.remove(posts_scroll);
			charts_display.revalidate();
			PostManagement postManagement = new PostManagement();
			Post usersPost = new Post();
			usersPost.setuser_login(user.getuser_login());
			Post private_post = postManagement.getPrivatePost(usersPost);
			content.removeAll();
			content.revalidate();
			display_posts();
			posts_scroll = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			charts_display.add(posts_scroll, "w 500!, height 450!");

		}

	}

}
