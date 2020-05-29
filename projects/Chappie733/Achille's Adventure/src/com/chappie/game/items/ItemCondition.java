package com.chappie.game.items;

public enum ItemCondition {
	BROKEN(0), SEVERELY_DAMAGED(1), DAMAGED(2),
	SLIGHTLY_DAMAGED(3), GOOD(4), PERFECT(5);
	
	private int condition;
	
	ItemCondition(int condition) {
		this.condition = condition;
	}
	
	public int getCondition() { return condition; }
}
