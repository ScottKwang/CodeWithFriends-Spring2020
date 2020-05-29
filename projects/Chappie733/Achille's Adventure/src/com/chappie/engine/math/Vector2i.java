package com.chappie.engine.math;

public class Vector2i {
	
	private int x,y;
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i(int val) {
		this.x = val;
		this.y = val;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
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
	
	public void addX(int xt) {
		x += xt;
	}
	
	public void addY(int yt) {
		y += yt;
	}
	
	public Vector2f asVector2f() {
		return new Vector2f(x,y);
	}
	
	public float getMagnitude() {
		return (float) Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}
	
	public static boolean equals(Vector2i first, Vector2i second) {
		return (first.getX() == second.getX())&&(first.getY() == second.getY());
	}
	
	public static Vector2i getSum(Vector2i f, Vector2i s) {
		return new Vector2i(f.getX()+s.getX(), f.getY()+s.getY());
	}
	
	public static Vector2i getDifference(Vector2i f, Vector2i s) {
		return new Vector2i(f.getX()-s.getX(), f.getY()-s.getY());
	}
	
	public static Vector2i getDivision(Vector2i f, Vector2i s) {
		return new Vector2i(f.getX()/s.getX(), f.getY()/s.getY());
	}
	
	public static Vector2i getDivision(Vector2f f, Vector2i s) {
		return new Vector2i((int)f.getX()/s.getX(), (int)f.getY()/s.getY());
	}
	
	public static Vector2i getProduct(Vector2i f, Vector2i s) {
		return new Vector2i(f.getX()*s.getX(), f.getY()*s.getY());
	}
	
	public static double getDistance(Vector2i f, Vector2i s) {
		return Math.sqrt(Math.pow(s.getX()-f.getX(), 2)+Math.pow(s.getY()-f.getY(), 2));
	}
	
}
