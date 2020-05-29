package com.chappie.engine.gfx.particles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.chappie.engine.math.MathUtils;
import com.chappie.engine.math.Vector2f;
import com.chappie.engine.math.Vector2i;
import com.chappie.engine.time.Timer;

/**
 * Class maintaining and handling a basic set of particles
 * 
 * @author Chappie
 * @since 24/05/2020 (dd/mm/yyyy)
 */
public class Particles {
	
	private List<Vector2f> particles; // pos of each particle/texture
	private Vector2i pos, offSet;
	private Timer duration;
	private BufferedImage texture;
	private float speed, curr_speed, accelleration;
	
	public Particles(Vector2i pos, Vector2i offSet, long duration, BufferedImage texture, int amount, float speed, float accelleration) {
		this.duration = new Timer(duration, false);
		this.texture = texture;
		this.pos = pos;
		this.offSet = offSet;
		this.speed = speed;
		curr_speed = speed;
		this.accelleration = accelleration;
		particles = new ArrayList<Vector2f>();
		for (int i = 0; i < amount; i++)
			particles.add(new Vector2i(pos.getX()+MathUtils.getRandom().nextInt(offSet.getX()),
									   pos.getY()+MathUtils.getRandom().nextInt(offSet.getY())).asVector2f());
	}
	
	/**
	 * @param pos position of the particles
	 * @param offSet max offSet from the position of the particles 
	 * @param duration duration of the particles
	 * @param texture texture of each particle
	 * @param amount the amount of particles
	 */
	public Particles(Vector2i pos, Vector2i offSet, long duration, BufferedImage texture, int amount, float speed) {
		this.duration = new Timer(duration, false);
		this.texture = texture;
		this.pos = pos;
		this.offSet = offSet;
		this.speed = speed;
		curr_speed = speed;
		particles = new ArrayList<Vector2f>();
		for (int i = 0; i < amount; i++)
			particles.add(new Vector2i(pos.getX()+MathUtils.getRandom().nextInt(offSet.getX()),
									   pos.getY()+MathUtils.getRandom().nextInt(offSet.getY())).asVector2f());
	}
	
	public void play() {
		duration.start();
		curr_speed = speed;
		for (int i = 0; i < particles.size(); i++)
			particles.set(i, new Vector2i(pos.getX()+MathUtils.getRandom().nextInt(offSet.getX()),
			 	    		 			  pos.getY()+MathUtils.getRandom().nextInt(offSet.getY())).asVector2f());
	}
	
	public void update() {
		if (duration.isOver() || !duration.hasStarted()) return;
		duration.update();
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).addY(-curr_speed);
			curr_speed-=accelleration;
		}
	}
	
	public void render(Graphics2D g) {
		if (!duration.isOver() && duration.hasStarted())
			for (Vector2f vec : particles)
				g.drawImage(texture, (int) vec.getX(), (int) vec.getY(), null);
	}

	public List<Vector2f> getParticles() {
		return particles;
	}

	public void setParticles(List<Vector2f> particles) {
		this.particles = particles;
	}

	public Timer getDuration() {
		return duration;
	}

	public void setDuration(Timer duration) {
		this.duration = duration;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
