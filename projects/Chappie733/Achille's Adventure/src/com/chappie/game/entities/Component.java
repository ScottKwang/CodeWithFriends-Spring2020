package com.chappie.game.entities;

import java.awt.Graphics2D;

public abstract class Component {
	
	protected boolean enabled;
	
	public Component() { enabled = true; }
	
	public abstract void onUpdate();
	public abstract void onRender(Graphics2D g);
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void enable() {
		enabled = true;
	}
	public void disable() {
		enabled = false;
	}
	
}
