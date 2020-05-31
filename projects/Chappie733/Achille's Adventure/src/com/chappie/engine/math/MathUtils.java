package com.chappie.engine.math;

import java.awt.Rectangle;
import java.util.Random;

import com.chappie.game.entities.Entity;

public class MathUtils {
	
	private static Random rand = new Random();
	
	public static boolean Intersect(Entity f, Entity s) {
		if ((f.getX() >= s.getX() && f.getX() <= s.getX()+s.getWidth()) || (f.getX() <= s.getX() && s.getX() <= f.getX()+f.getWidth()) || (f.getX() >= s.getX() && f.getX()+f.getWidth()<=s.getX()+s.getWidth()))
			if ((f.getY() >= s.getY() && f.getY() <= s.getY()+s.getHeight()) || (f.getY() <= s.getY() && s.getY() <= f.getY()+f.getHeight()) || (f.getY() >= s.getY() && f.getY()+f.getHeight()<=s.getY()+s.getHeight()))
				return true;
		return false;
	}
	
	public static boolean Intersect(Rectangle f, Rectangle s) {
		if ((f.getX() >= s.getX() && f.getX() <= s.getX()+s.getWidth()) || (f.getX() <= s.getX() && s.getX() <= f.getX()+f.getWidth()) || (f.getX() >= s.getX() && f.getX()+f.getWidth()<=s.getX()+s.getWidth()))
			if ((f.getY() >= s.getY() && f.getY() <= s.getY()+s.getHeight()) || (f.getY() <= s.getY() && s.getY() <= f.getY()+f.getHeight()) || (f.getY() >= s.getY() && f.getY()+f.getHeight()<=s.getY()+s.getHeight()))
				return true;
		return false;
	}
	
	public static boolean Intersect(int xf, int yf, int wf, int hf, int xs, int ys, int ws, int hs) {
		if ((xf >= xs && xf <= xs+ws) || (xf <= xs && xs <= xf+wf) || (xf >= xs && xf+wf<=xs+ws))
			if ((yf >= ys && yf <= ys+hs) || (yf <= ys && ys <= yf+hf) || (yf >= ys && yf+hf<=ys+hs))
				return true;
		return false;
	}
	
	public static int normalize(float x) {
		if (x == 0) return 0;
		return (x>0)?1:-1;
	}
	
	public static double getAngle(Vector2f from, Vector2f to) {
		return Math.atan2(to.getY() - from.getY(), to.getX()-from.getX()) * (180 / Math.PI);
	}
	
	public static double getAngle(Vector2i from, Vector2i to) {
		return Math.atan2(to.getY() - from.getY(), to.getX()-from.getX()) * (180 / Math.PI);
	}
	
	public static Random getRandom() {
		return rand;
	}
	
//	public static int roundUp(int num, int divisor) {
//	    int sign = (num > 0 ? 1 : -1) * (divisor > 0 ? 1 : -1);
//	    return sign * (Math.abs(num) + Math.abs(divisor) - 1) / Math.abs(divisor);
//	}
	
}
