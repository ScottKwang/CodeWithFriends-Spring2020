package com.chappie.game.states;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.chappie.main.Game;
import com.chappie.main.Handler;

public class GameStateManager {
	
	private List<GameState> gameStates;
	private State state;
	private Game game;
	
	public GameStateManager(Handler handler, Game game) {
		this.game = game;
		state = State.INTRO_SEQUENCE;
		gameStates = new ArrayList<GameState>();
		gameStates.add(0, new IntroSequence(this, handler));
		gameStates.add(1, new MenuState(this, handler));
		gameStates.add(2, new OptionsState(this, handler));
		gameStates.add(3, new PlayingState(this, handler));
		gameStates.add(4, new PausedState(this, handler));
	}
	
	public void init() {
		gameStates.get(state.getState()).init();
	}
	
	public void update() {
		gameStates.get(state.getState()).update();
	}
	
	public void render(Graphics2D g) {
		gameStates.get(state.getState()).render(g);
	}
	
	public void onKeyPressed(int keyPressed) {
		gameStates.get(state.getState()).onKeyPress(keyPressed);
	}
	
	public void onKeyReleased(int keyReleased) {
		gameStates.get(state.getState()).onKeyRelease(keyReleased);
	}
	
	public void onMouseClicked(int mouseButton) {
		gameStates.get(state.getState()).onMouseClick(mouseButton);
	}
	
	public void onMouseReleased(int mouseButton) {
		gameStates.get(state.getState()).onMouseRelease(mouseButton);
	}
	
	public void setState(State state, boolean initialize) {
		if (state == State.CLOSING) game.stop();
		if (state == State.OPTIONS_STATE) {
			OptionsState opt_state = (OptionsState) gameStates.get(State.OPTIONS_STATE.getState());
			opt_state.setPreviousState(this.state);
		}
		this.state = state;
		if (initialize) init();
		else gameStates.get(state.getState()).onStateEnter();
	}
}
