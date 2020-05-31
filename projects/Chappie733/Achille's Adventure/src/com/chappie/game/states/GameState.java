package com.chappie.game.states;

import java.awt.Graphics2D;

import com.chappie.main.Handler;

public abstract class GameState {
	
	protected GameStateManager gsm;
	protected Handler handler;
	
	public GameState(GameStateManager gsm, Handler handler) {
		this.gsm = gsm;
		this.handler = handler;
	}
	
	public abstract void init();
	public abstract void onStateEnter();
	public abstract void update();
	public abstract void render(Graphics2D g);
	
	public abstract void onKeyPress(int keyPressed);
	public abstract void onKeyRelease(int keyReleased);
	public abstract void onMouseClick(int mouseButton);
	public abstract void onMouseRelease(int mouseButton);
	
}
