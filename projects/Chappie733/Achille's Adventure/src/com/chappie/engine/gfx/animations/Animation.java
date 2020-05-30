package com.chappie.engine.gfx.animations;

import java.awt.image.BufferedImage;

import com.chappie.engine.math.Vector2f;
import com.chappie.engine.time.Timer;

public class Animation {
	
	private Timer timer;
	private BufferedImage frames[];
	private Vector2f frame_scaling[];
	private int curr_frame;
	private boolean looping;
	
	public Animation(BufferedImage frames[], long millisDelay, boolean looping, Vector2f frame_scaling[]) {
		this.frames = frames;
		this.looping = looping;
		this.frame_scaling = frame_scaling;
		timer = new Timer(millisDelay);
		curr_frame = 0;
	}
	
	public Animation(BufferedImage frames[], long millisDelay, boolean looping) {
		this.frames = frames;
		this.looping = looping;
		timer = new Timer(millisDelay);
		curr_frame = 0;
	}
	
	public Animation(BufferedImage frames[], long millisDelay) {
		this.frames = frames;
		timer = new Timer(millisDelay);
		curr_frame = 0;
		looping = false;
	}
	
	public void update() {
		if (frames.length == 1) return;
		timer.update();
		if (timer.isOver()) {
			timer.restart();
			++curr_frame;
		}
		if (isOver()) {
			if (looping) restart(); 
		}
	}
	
	public boolean isOver() {
		return curr_frame>=frames.length;
	}
	
	public void restart() {
		curr_frame = 0;
	}
	
	public BufferedImage getTexture() {
		return frames[curr_frame];
	}
	
	public Vector2f getScaling() {
		if (curr_frame >= frames.length-1) return new Vector2f(1,1);
		return (frame_scaling!=null)?frame_scaling[curr_frame]:new Vector2f(1,1);
	}
	
	public boolean isLooping() { return looping; }
	public void setLopping(boolean looping) { this.looping = looping; }
	
}
