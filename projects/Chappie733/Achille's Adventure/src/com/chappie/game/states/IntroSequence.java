package com.chappie.game.states;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.chappie.engine.files.Loader;
import com.chappie.engine.time.Timer;
import com.chappie.main.Game;
import com.chappie.main.Handler;

public class IntroSequence extends GameState {

	private BufferedImage background;
	private Timer timer;
	
	public IntroSequence(GameStateManager gsm, Handler handler) {
		super(gsm, handler);
	}

	@Override
	public void init() {
		background = Loader.LoadImage("gui/intro_image.png");
		timer = new Timer(4000);
	}

	@Override
	public void update() {
		timer.update();
		if (timer.isOver())
			gsm.setState(State.MAIN_MENU, true);
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, Game.WIDTH, Game.HEIGHT, null);
	}

	@Override
	public void onKeyPress(int keyPressed) {
		
	}

	@Override
	public void onKeyRelease(int keyReleased) {
		
	}

	@Override
	public void onMouseClick(int mouseButton) {
		
	}

	@Override
	public void onMouseRelease(int mouseButton) {
		
	}

	@Override
	public void onStateEnter() {
		
	}

}
