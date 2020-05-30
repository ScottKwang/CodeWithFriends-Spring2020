package com.chappie.game.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.concurrent.Callable;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.gui.Menu;
import com.chappie.engine.math.Vector2i;
import com.chappie.main.Game;
import com.chappie.main.Handler;

public class PausedState extends GameState {
	
	private Menu menu;
	
	public PausedState(GameStateManager gsm, Handler handler) {
		super(gsm, handler);
		init();
	}

	@Override
	public void init() {
		String options[] = { "Resume", "Options", "Main Menu" };
		Callable<Void> resume = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				gsm.setState(State.PLAYING_STATE, false);
				return null;
			}
		};
		Callable<Void> options_action = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				gsm.setState(State.OPTIONS_STATE, true);
				return null;
			}
		};
		Callable<Void> main_m = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				gsm.setState(State.MAIN_MENU, false);
				Thread.sleep(150);
				return null;
			}
			
		};
		
		menu = new Menu(options, new Vector2i(Game.WIDTH/2-150, Game.HEIGHT/2+50), 75);
		menu.setAction((byte) 0, resume);
		menu.setAction((byte) 1, options_action);
		menu.setAction((byte) 2, main_m);
		menu.setBackground(Loader.LoadImage("gui/backgrounds/pause_background.png"));
		menu.init();
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
	public void onKeyPress(int keyPressed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyRelease(int keyReleased) {
		if (keyReleased == KeyEvent.VK_ESCAPE) gsm.setState(State.MAIN_MENU, false);
	}

	@Override
	public void onMouseClick(int mouseButton) {}

	@Override
	public void onMouseRelease(int mouseButton) {}

	@Override
	public void onStateEnter() {
		menu.setSelected((byte) 0);
	}
	
	
}
