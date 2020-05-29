package com.chappie.game.entities.enemies;

import java.awt.Graphics2D;

import com.chappie.engine.math.Vector2f;
import com.chappie.game.entities.Entity;
import com.chappie.main.Game;
import com.chappie.main.Handler;

public class Enemy extends Entity {
	
	protected float range;
	protected int damage;
	protected static final float DEFAULT_ENEMY_SPEED = 3f;
	
	public Enemy(Vector2f pos, int width, int height, Handler handler, float abs_speed, float range,
			int damage, int health) {
		super(pos, width, height);
		this.handler = handler;
		this.abs_speed = abs_speed;
		this.range = range;
		this.damage = damage;
		this.health = health;
	}
	
	public Enemy(Vector2f pos, int width, int height, Handler handler, float abs_speed, float range,
			int damage) {
		super(pos, width, height);
		this.handler = handler;
		this.abs_speed = abs_speed;
		this.range = range;
		this.damage = damage;
		health = 100;
	}
	
	public Enemy(Vector2f pos, int width, int height, Handler handler, float abs_speed, float range) {
		super(pos, width, height);
		this.handler = handler;
		this.abs_speed = abs_speed;
		this.range = range;
		health = 100;
	}
	
	public Enemy(Vector2f pos, int width, int height, Handler handler, float abs_speed) {
		super(pos, width, height);
		this.handler = handler;
		this.abs_speed = abs_speed;
		health = 100;
	}
	
	public Enemy(Vector2f pos, int width, int height, Handler handler) {
		super(pos, width, height);
		this.handler = handler;
		abs_speed = DEFAULT_ENEMY_SPEED;
		health = 100;
	}
	
	public Enemy(Vector2f pos, int width, int height) {
		super(pos, width, height);
		abs_speed = DEFAULT_ENEMY_SPEED;
		health = 100;
	}
	
	@Override
	public void init() {}
	
	public void update() {
		super.update();
	}
	
	@Override
	public void render(Graphics2D g) {}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public static float getDefaultEnemySpeed() {
		return DEFAULT_ENEMY_SPEED;
	}
	
	public boolean isOnScreen() {
		return (Vector2f.getDistance(pos, handler.getPlayer().getPos()) <= Vector2f.getDistance(handler.getPlayer().getGamePos(), 
																							    new Vector2f(handler.getPlayer().getGamePos().getX()+Game.WIDTH/2, handler.getPlayer().getGamePos().getY()+Game.HEIGHT/2)));
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
}
