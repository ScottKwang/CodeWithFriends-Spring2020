package com.chappie.main;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.chappie.engine.gfx.gui.GUIElement;
import com.chappie.engine.input.Input;
import com.chappie.engine.sounds.SoundManager;
import com.chappie.engine.window.Window;
import com.chappie.game.states.GameStateManager;

public class Game implements Runnable {
	
	private Thread main_thread;
	private boolean running;
	
	private Window window;
	public static final int WIDTH = 800,
							HEIGHT = 600;
	
	private BufferStrategy bs;
	private Graphics2D graphics;
	
	private Handler handler;
	private GameStateManager gsm;
	
	public Game() {}
	
	public void init() {
		window = new Window(800, 600, "Game");
		window.create();
		window.initGraphics(2);
		window.setFramesPerSecond(60);
		Input in = new Input();
		window.getCanvas().addKeyListener(in);
		window.getCanvas().addMouseListener(in);
		window.getCanvas().addMouseWheelListener(in);
		
		bs = window.getBufferStrategy();
		
		handler = new Handler();
		gsm = new GameStateManager(handler, this);
		gsm.init();
		Input.setGameStateManager(gsm);
		SoundManager.init();
		GUIElement.setWindowInstance(window);
	}
	
	@Override
	public void run() {
		init();
		
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running){
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1){
				update();
				render();
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000){
				System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
		}
		
		stop();
	}
	
	private void update() {
		gsm.update();
	}
	
	private void render() {
		graphics = (Graphics2D) bs.getDrawGraphics();
		graphics.clearRect(0, 0, WIDTH, HEIGHT);
		
		gsm.render(graphics);
		
		graphics.dispose();
		bs.show();
	}
	
	public synchronized void start() {
		if (running) return;
		if (main_thread == null) {
			main_thread = new Thread(this);
			main_thread.start();
			running = true;
		}
	}
	
	public synchronized void stop() {
		if (!running) return;
		SoundManager.dispose();
		try {
			main_thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = false;
	}
	
}
