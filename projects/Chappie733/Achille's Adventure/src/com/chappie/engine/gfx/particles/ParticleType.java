package com.chappie.engine.gfx.particles;

public enum ParticleType {
	HEALING(0), EMPTY(255);
	
	int id;
	
	ParticleType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
