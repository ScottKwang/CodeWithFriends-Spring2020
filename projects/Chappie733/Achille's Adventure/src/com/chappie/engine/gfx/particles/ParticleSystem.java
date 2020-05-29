package com.chappie.engine.gfx.particles;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.chappie.engine.time.Timer;

public class ParticleSystem {
	
	private List<Particles> particles;
	private Timer playTimer;
		
	public ParticleSystem(List<Particles> particles) {
		this.particles = particles;
		playTimer = new Timer(125);
	}
	
	public ParticleSystem() {
		this.particles = new ArrayList<Particles>();
		playTimer = new Timer(125);
	}
	
	public void update() {
		for (Particles p : particles)
			p.update();
	}

	public void render(Graphics2D g) {
		for (Particles p : particles)
			p.render(g);
	}
	
	public void addParticles(Particles p) {
		particles.add(p);
	}
	
	public void play(int particles_index) {
		playTimer.update();
		if (playTimer.isOver()) {
			particles.get(particles_index).play();
			playTimer.restart();
		}
	}
	
}
