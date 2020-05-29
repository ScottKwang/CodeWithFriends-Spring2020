package com.chappie.game.states;

public enum State {
	INTRO_SEQUENCE(0),
	MAIN_MENU(1),
	OPTIONS_STATE(2),
	PLAYING_STATE(3),
	PAUSED_STATE(4),
	CLOSING(5);
	
	private int state;
	
	State(int state) {
		this.state = state;
	}
	
	int getState() {
		return state;
	}
}
