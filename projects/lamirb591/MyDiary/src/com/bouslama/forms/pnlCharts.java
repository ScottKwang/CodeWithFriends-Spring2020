package com.bouslama.forms;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.bouslama.maintenance.PostManagement;
import com.bouslama.maintenance.UsersManagement;
import com.bouslama.processing.Post;
import com.bouslama.processing.User;

import net.miginfocom.swing.MigLayout;

public class pnlCharts extends JPanel{
		
	public static ArrayList<Post> community_posts_list = new ArrayList<>();
	public static ArrayList<Post> user_posts_list = new ArrayList();
	User user;
	JPanel left_charts_panel, right_charts_panel;
	public int user_gd_count = 0, user_ad_count = 0, user_bd_count = 0, community_gd_count = 0, community_ad_count = 0, community_bd_count = 0;
	@SuppressWarnings("deprecation")
	public pnlCharts() {
		UsersManagement userManagement = new UsersManagement();
		
		
		User us = userManagement.getUser(frmManagement.user);
		
		PostManagement postManagement = new PostManagement();
		Post usersPost = new Post();
		usersPost.setuser_login(us.getuser_login());
		Post private_post = postManagement.getUserPosts(usersPost);
		Post community_post = postManagement.getCommunityPost();
		
		System.out.println(user_posts_list + " 3asba " + community_posts_list);
		
		
		
		left_charts_panel = new JPanel(new MigLayout("wrap"));
		right_charts_panel = new JPanel(new MigLayout("wrap"));
		setLayout(new WrapLayout());
		
		//user
		DefaultPieDataset userpieDataset = new DefaultPieDataset();
		DefaultCategoryDataset community_days = new DefaultCategoryDataset();
		String current_post_date = community_posts_list.get(0).getPost_date();
		 int community_days_count_g = 0, community_days_count_a = 0, community_days_count_b = 0;   
			
		for(Post post : community_posts_list) {
			if(community_posts_list.get(community_posts_list.size() - 1).getPost_id() == post.getPost_id()){
			 	if(post.getPost_rate().contentEquals("3")) {
					community_days_count_g += 1;
					}
				if(post.getPost_rate().contentEquals("2")) {
						community_days_count_a += 1;
					}
				if(post.getPost_rate().contentEquals("1")) {
						community_days_count_b += 1;
					}
				   community_days.addValue(new Integer(community_days_count_b), "BAD DAYS", current_post_date.substring(6));
				   community_days.addValue(new Integer(community_days_count_a), "AVERAGE DAYS", current_post_date.substring(6));
				   
				   community_days.addValue(new Integer(community_days_count_g), "GOOD DAYS", current_post_date.substring(6));					   
				
		 }
			else if(post.getPost_date().equals(current_post_date) && post.getPost_id() != (community_posts_list.get(community_posts_list.size() - 1).getPost_id())) {
					if(post.getPost_rate().contentEquals("3")) {
						community_days_count_g += 1;
						}
					if(post.getPost_rate().contentEquals("2")) {
							community_days_count_a += 1;
						}
					if(post.getPost_rate().contentEquals("1")) {
							community_days_count_b += 1;
						}	 
			 }
			else if(!post.getPost_date().equals(current_post_date) && community_posts_list.get(community_posts_list.size() - 1).getPost_id() != post.getPost_id()){
				
				 community_days.addValue(new Integer(community_days_count_b), "BAD DAYS", current_post_date.substring(6));
				   community_days.addValue(new Integer(community_days_count_a), "AVERAGE DAYS", current_post_date.substring(6));
				   community_days.addValue(new Integer(community_days_count_g), "GOOD DAYS", current_post_date.substring(6));
				   current_post_date = post.getPost_date();
				   community_days_count_g = 0;
				   community_days_count_b = 0;
				   community_days_count_b = 0;
				   if(post.getPost_rate().contentEquals("3")) {
						community_days_count_g += 1;
						}
					if(post.getPost_rate().contentEquals("2")) {
							community_days_count_a += 1;
						}
					if(post.getPost_rate().contentEquals("1")) {
							community_days_count_b += 1;
						}	
				   
				   
			 }
			 
			
		}
		JFreeChart chart3 = ChartFactory.createStackedBarChart("day rats by day", "date", "days count", community_days, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel community_days_rate = new ChartPanel(chart3);
		for(Post post: user_posts_list) {
			if(post.getPost_rate().contentEquals("1")) {
			user_bd_count += 1;
			}
			if(post.getPost_rate().contentEquals("2")) {
				user_ad_count += 1;
				}
			if(post.getPost_rate().contentEquals("3")) {
				user_gd_count += 1;
				}
			
		}
		PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0} = {1}");
		
		userpieDataset.setValue("BAD DAYS", new Integer(user_bd_count));
		userpieDataset.setValue("AVERAGE DAYS", new Integer(user_ad_count));
		userpieDataset.setValue("GOOD DAYS", new Integer(user_gd_count));
		
		JFreeChart user_chart = ChartFactory.createPieChart("My Days", userpieDataset, true, true, true);
		
		PiePlot p_user = (PiePlot)user_chart.getPlot();
		p_user.setLabelGenerator(labelGenerator);
		ChartPanel chart_panel_left = new ChartPanel(user_chart);
		
		//community
		DefaultPieDataset communitypieDataset = new DefaultPieDataset();
		for(Post post : community_posts_list) {
			if(post.getPost_rate().contentEquals("1")) {
					community_bd_count += 1;
				}
			if(post.getPost_rate().contentEquals("2")) {
					community_ad_count += 1;
				}
			if(post.getPost_rate().contentEquals("3")) {
					community_gd_count += 1;
				}
			
			
		}
		
		communitypieDataset.setValue("GOOD DAYS", new Integer(community_gd_count));
		communitypieDataset.setValue("AVERAGE DAYS", new Integer(community_ad_count));
		communitypieDataset.setValue("BAD DAYS", new Integer(community_ad_count));		
		JFreeChart community_chart = ChartFactory.createPieChart("community rate", communitypieDataset, true, true, true);	
		PiePlot p_community = (PiePlot)community_chart.getPlot();
		p_community.setLabelGenerator(labelGenerator);
		ChartPanel chart_panel_right = new ChartPanel(community_chart);
		right_charts_panel.add(chart_panel_right, "w 500!, height 300!");
		left_charts_panel.add(chart_panel_left, "w 500!, height 500!");
		right_charts_panel.add(community_days_rate, "w 500!, height 300!");
		add(left_charts_panel);
		add(right_charts_panel);
	
	}
	
}
