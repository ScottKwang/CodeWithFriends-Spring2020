package com.chappie.engine.time;

public class Timer extends Time {
	
	private long time;
	private boolean started;

	public Timer(long time, boolean started) {
		super();
		this.time = time;
		this.started = started;
	}
	
	public Timer(long time) {
		super();
		this.time = time;
		started = true;
	}
	
	@Override
	public void update() {
		if (!started) return;
		passed += System.currentTimeMillis()-last;
		last = System.currentTimeMillis();
	}
	
	public void start() {
		started = true;
		passed = 0;
		last = System.currentTimeMillis();
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public boolean isOver() {
		return getTimePassed()>=time;
	}
	
}
