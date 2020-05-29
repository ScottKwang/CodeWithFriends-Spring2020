package com.chappie.game.states;

import java.awt.Graphics2D;
import java.util.concurrent.Callable;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.gui.Menu;
import com.chappie.engine.math.Vector2i;
import com.chappie.engine.sounds.SoundManager;
import com.chappie.main.Handler;

public class MenuState extends GameState {

	private Menu menu;
	
	public MenuState(GameStateManager gsm, Handler handler) {
		super(gsm, handler);
	}

	@Override
	public void init() {
		String options[] = {"Play", "Options", "Quit"};
		menu = new Menu(options, new Vector2i(100, 350), 75, Loader.LoadImage("gui/backgrounds/menu_background.png"));
		menu.init();
		Callable<Void> play = new Callable<Void>() { 
			@Override
			public Void call() throws Exception {
				gsm.setState(State.PLAYING_STATE, true);
				return null;
			}  };
		Callable<Void> options_action = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				gsm.setState(State.OPTIONS_STATE, true);
				return null;
			}
		};
		Callable<Void> quit = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				SoundManager.dispose();
				System.exit(0);
				return null;
			}};
		menu.setAction((byte) 0, play);
		menu.setAction((byte) 1, options_action);
		menu.setAction((byte) 2, quit);
	}

	@Override
	public void update() {
		menu.update();
	}

	@Override
	public void render(Graphics2D g) {
		menu.render(g);
	}

	@Override
	public void onKeyPress(int keyPressed) {}

	@Override
	public void onKeyRelease(int keyReleased) {}

	@Override
	public void onMouseClick(int mouseButton) {}

	@Override
	public void onMouseRelease(int mouseButton) {}

	@Override
	public void onStateEnter() {
		menu.setSelected((byte) 0);
	}
	
}
