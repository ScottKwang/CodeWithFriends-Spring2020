package com.chappie.engine.gfx.animations;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.chappie.engine.math.Vector2f;

public class AnimationManager {
	
	private List<Animation> animations;
	private byte state, default_state;
	
	public AnimationManager(List<Animation> animations) {
		this.animations = animations;
		state = 0;
	}
	
	public AnimationManager() {
		animations = new ArrayList<Animation>();
		state = 0;
	}
	
	public void setState(byte state) {
		this.state = state;
	}
	
	public byte getState() {
		return state;
	}
	
	public Animation getAnimation() {
		return animations.get(state);
	}
	
	public void update() {
		animations.get(state).update();
		if (animations.get(state).isOver()) state = default_state;
	}
	
	public BufferedImage getTexture() {
		return getAnimation().getTexture();
	}
	
	public Vector2f getScaling() {
		return getAnimation().getScaling();
	}
	
	public void addAnimation(Animation anim) {
		animations.add(anim);
	}
	
	public void setDefaultState(byte default_state) {
		this.default_state = (byte) default_state;
	}
	
	public byte getDefaultState() {
		return default_state;
	}
	
}
