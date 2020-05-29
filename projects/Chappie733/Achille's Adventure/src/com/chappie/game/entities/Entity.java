package com.chappie.game.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.chappie.engine.math.Vector2f;
import com.chappie.engine.math.Vector2i;
import com.chappie.main.Handler;

public abstract class Entity {
	
	protected Vector2f pos;
	protected Vector2f speed;
	protected int width, height, health;
	protected float abs_speed;
	protected BufferedImage texture;
	protected List<Component> components;
	protected Handler handler;

	public Entity(Vector2f pos, int width, int height, BufferedImage texture, Handler handler,
			float abs_speed) {
		this.pos = pos;
		this.width = width;
		this.height = height;
		this.texture = texture;
		this.handler = handler;
		this.abs_speed = abs_speed;
		components = new ArrayList<Component>();
		speed = new Vector2f(0,0);
	}
	
	public Entity(Vector2f pos, int width, int height) {
		this.pos = pos;
		this.width = width;
		this.height = height;
		components = new ArrayList<Component>();
		speed = new Vector2f(0,0);
	}
	
	public Entity(float x, float y, int width, int height) {
		pos = new Vector2f(x,y);
		this.width = width;
		this.height = height;
		components = new ArrayList<Component>();
		speed = new Vector2f(0,0);
	}
	
	public Entity(float x, float y) {
		pos = new Vector2f(x,y);
		width = height = 32;
		components = new ArrayList<Component>();
		speed = new Vector2f(0,0);
	}
	
	public abstract void init();
	
	public void update() {
		for (Component c : components)
			if (c.isEnabled()) c.onUpdate();
	}
	
	public void render(Graphics2D graphics) {
		for (Component c : components)
			if (c.isEnabled()) c.onRender(graphics);
	}

	public Rectangle getHitBox() {
		return new Rectangle((int) pos.getX(), (int) pos.getY(), width, height);
	}
	
	public Vector2f getPos() {
		return pos;
	}
	public void setPos(Vector2f pos) {
		this.pos = pos;
	}
	public Vector2i getTilePosition() {
		return Vector2i.getDivision(pos, new Vector2i(handler.getMap().getTileSize()));
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public float getX() { 
		return pos.getX(); 
	}
	public float getY() { 
		return pos.getY(); 
	}
	public BufferedImage getTexture() {
		return texture;
	}
	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}
	public boolean hasComponent(Component comp) {
		return components.contains(comp);
	}
	public void addComponent(Component comp) {
		components.add(comp);
	}
	public void setSpeed(Vector2f speed) {
		this.speed = speed;
	}
	public Vector2f getSpeed() {
		return speed;
	}
	public void disableComponents() {
		for (Component c : components)
			c.disable();
	}
	public void enableComponents() {
		for (Component c : components)
			c.enable();
	}
	public float getAbsoluteSpeed() {
		return abs_speed;
	}
	public void setAbsoluteSpeed(float abs_speed) {
		this.abs_speed = abs_speed;
	}
	public Handler getHandler() {
		return handler;
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	public int getHealth() {
		return health;
	}
	public void damage(int amount) {
		health -= amount;
	}
	public boolean isDead() {
		return health <= 0;
	}
	public void addHealth(int amount) {
		health += amount;
	}
}
