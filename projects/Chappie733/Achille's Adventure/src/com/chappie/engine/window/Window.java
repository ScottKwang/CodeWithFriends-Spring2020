package com.chappie.engine.window;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.chappie.engine.math.Vector2i;

public class Window {
	
	private JFrame win;
	private Canvas canvas;
	private int width, height;
	private String title;
	private boolean fullscreen;
	
	private int fps;

	public Window(int width, int height, boolean fullscreen) {
		this.width = width;
		this.height = height;
		this.title = "Game";
		this.fullscreen = fullscreen;
	}
	
	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
		fullscreen = false;
	}
	
	public Window(int width, int height) {
		this.width = width;
		this.height = height;
		this.title = "Game";
		fullscreen = false;
	}
	
	public void create() {
		win = new JFrame(title);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setAlwaysOnTop(false);
		win.setSize(width, height);
		win.setLocationRelativeTo(null);
		win.setResizable(false);
		
		canvas = new Canvas();
		canvas.setSize(new Dimension(width, height));
		canvas.setFocusable(true);
		
		win.add(canvas);
		if (fullscreen) {
			win.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			win.setUndecorated(true);
		}
		win.setVisible(true);
	}
	
	public void initGraphics(int buffers) {
		canvas.createBufferStrategy(buffers);
	}
	
	public BufferStrategy getBufferStrategy() {
		return canvas.getBufferStrategy();
	}
	
	public Vector2i getMousePosition() {
		try { return new Vector2i((int) win.getMousePosition().getX(), (int) win.getMousePosition().getY()); } 
		catch (Exception e) { return new Vector2i(1,1); }
	}
	
	public Canvas getCanvas() { return canvas; }
	public void setFramesPerSecond(int fps) { this.fps = fps; }
	public int getFramesPerSecond() { return fps; }
	
}
