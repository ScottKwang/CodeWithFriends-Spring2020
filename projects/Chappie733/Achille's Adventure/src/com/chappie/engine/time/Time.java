package com.chappie.engine.time;

public class Time {
	
	protected long passed, last;
	
	public Time() {
		last = System.currentTimeMillis();
	}
	
	public void update() {
		passed += System.currentTimeMillis()-last;
		last = System.currentTimeMillis();
	}
	
	public float getTimeAsSeconds() {
		return (float)passed/1000f;
	}
	
	public long getTimePassed() {
		return passed;
	}
	
	public void restart() {
		passed = 0;
	}
	
}
