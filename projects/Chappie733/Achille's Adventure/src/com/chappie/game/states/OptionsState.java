package com.chappie.game.states;

import java.awt.Graphics2D;
import java.util.concurrent.Callable;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.gui.GUI;
import com.chappie.engine.gfx.gui.widgets.Button;
import com.chappie.engine.gfx.gui.widgets.SelectionBar;
import com.chappie.engine.math.Vector2i;
import com.chappie.engine.sounds.SoundManager;
import com.chappie.game.settings.Settings;
import com.chappie.main.Game;
import com.chappie.main.Handler;

public class OptionsState extends GameState {

	private GUI widgets;
	private State previous;
	
	public OptionsState(GameStateManager gsm, Handler handler) {
		super(gsm, handler);
	}

	@Override
	public void init() {
		widgets = new GUI(Loader.LoadImage("gui/backgrounds/options_background.png"));
		String options[] = { "0", "40", "60", "75", "100" };
		Callable<Void> selectionbar_musicVolume = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				SelectionBar sb = (SelectionBar) widgets.getElement(0);
				Settings.setMusicVolume(Integer.parseInt(sb.getSelected()));
				SoundManager.setScheduledVolume(Integer.parseInt(sb.getSelected()));
				return null;
			}
		};
		widgets.addElement(new SelectionBar(new Vector2i(350, 150), new Vector2i(128, 32),
											options, SelectionBar.DEFAULT_MARKER_TEXTURE,
											new Vector2i(5, 19), SelectionBar.DEFAULT_TEXTURE, 6,
											selectionbar_musicVolume, (byte) 3));
		Callable<Void> selectionbar_effectsVolume = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				// TODO: change volume of effects
				return null;
			}
		};
		widgets.addElement(new SelectionBar(new Vector2i(350, 200), new Vector2i(128, 32),
										    options, SelectionBar.DEFAULT_MARKER_TEXTURE,
										    new Vector2i(5,19), SelectionBar.DEFAULT_TEXTURE, 6,
										    selectionbar_effectsVolume, (byte) 4));
		Callable<Void> selectionbar_masterVolume = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				SelectionBar bar = (SelectionBar) widgets.getElement(2);
				Settings.setMasterVolume(Integer.parseInt(bar.getSelected()));
				SoundManager.setVolumeMultiplier((float) (Integer.parseInt(bar.getSelected())/100f));
				return null;
			}
		};
		widgets.addElement(new SelectionBar(new Vector2i(350, 250), new Vector2i(128, 32),
			    options, SelectionBar.DEFAULT_MARKER_TEXTURE,
			    new Vector2i(5,19), SelectionBar.DEFAULT_TEXTURE, 6,
			    selectionbar_masterVolume, (byte) 4));
		
		Button button = new Button(new Vector2i(75, Game.HEIGHT-100), new Vector2i(100, 50));
		Callable<Void> on_button_click = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				System.out.println(previous.getState());
				gsm.setState(previous, false);
				return null;
			}
		};
		button.setAction(on_button_click);
		widgets.addElement(button);
	}

	@Override
	public void update() {
		widgets.update();
	}

	@Override
	public void render(Graphics2D g) {
		widgets.render(g);
	}

	@Override
	public void onKeyPress(int keyPressed) {
		
	}

	@Override
	public void onKeyRelease(int keyReleased) {
		
	}

	@Override
	public void onMouseClick(int mouseButton) {
		widgets.onMouseClick(mouseButton);
	}

	@Override
	public void onMouseRelease(int mouseButton) {
		widgets.onMouseRelease(mouseButton);
	}
	
	public void setPreviousState(State previous) {
		this.previous = previous;
	}

	@Override
	public void onStateEnter() {}
}
