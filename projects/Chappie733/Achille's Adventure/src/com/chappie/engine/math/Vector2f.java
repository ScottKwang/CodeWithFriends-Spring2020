package com.chappie.engine.math;

public class Vector2f {
	
	private float x,y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(float val) {
		this.x = val;
		this.y = val;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public String toString() {
		return "[" + x + "; " + y + "]";
	}
	
	public void add(Vector2f other) {
		x += other.getX();
		y += other.getY();
	}
	
	public void subtract(Vector2f other) {
		x -= other.getX();
		y -= other.getY();
	}
	
	public float getMagnitude() {
		return (float) Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}
	
	public void addX(float xt) {
		x += xt;
	}
	
	public void addY(float yt) {
		y += yt;
	}
	
	public Vector2i asVector2i() {
		return new Vector2i(Math.round(x), Math.round(y));
	}
	
	public static Vector2f getSum(Vector2f f, Vector2f s) {
		return new Vector2f(f.getX()+s.getX(), f.getY()+s.getY());
	}
	
	public static Vector2f getDifference(Vector2f f, Vector2f s) {
		return new Vector2f(f.getX()-s.getX(), f.getY()-s.getY());
	}
	
	public static double getDistance(Vector2f f, Vector2f s) {
		return Math.sqrt(Math.pow(s.getX()-f.getX(), 2)+Math.pow(s.getY()-f.getY(), 2));
	}
	
	public static Vector2f getProduct(Vector2f f, Vector2f s) {
		return new Vector2f(f.getX()*s.getX(), f.getY()*s.getY());
	}
	
}
