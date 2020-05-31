package com.chappie.game.map.structures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.chappie.engine.math.Vector2i;
import com.chappie.main.Handler;

public class Structure {
	
	private Vector2i position; // in tiles
	private int width, height; // ""
	private int coll_width, coll_height;
	protected BufferedImage texture;
	private Handler handler;
	
	public Structure(Vector2i position, BufferedImage texture, Handler handler, 
			int width, int height, int coll_width, int coll_height) {
		this.position = position;
		this.texture = texture;
		this.handler = handler;
		this.width = width;
		this.height = height;
		this.coll_width = coll_width;
		this.coll_height = coll_height;
	}
	
	public Structure(Vector2i position, BufferedImage texture, Handler handler, 
			int width, int height) {
		this.position = position;
		this.texture = texture;
		this.handler = handler;
		this.width = width;
		this.height = height;
	}
	
	public Structure(Vector2i position, BufferedImage texture, Handler handler) {
		this.position = position;
		this.texture = texture;
		this.handler = handler;
	}
	
	public Structure(BufferedImage texture, Handler handler) {
		this.handler = handler;
		this.texture = texture;
	}
	
	public void init() {}
	
	public void render(Graphics2D g) {
		g.drawImage(texture, position.getX()*handler.getMap().getTileSize()-(int)handler.getMap().getPosOffSet().getX(), 
					         position.getY()*handler.getMap().getTileSize()-(int)handler.getMap().getPosOffSet().getY(),
					         width*handler.getMap().getTileSize(),
					         height*handler.getMap().getTileSize(),
					         null);
	}
	
/*	public boolean onScreen() {
		double distance = Vector2i.getDistance(handler.getPlayer().getTilePosition(), position);
		return (distance <= Math.abs(new Vector2f((Game.WIDTH/2+width)/handler.getMap().getTileSize(), (Game.HEIGHT/2+height)/handler.getMap().getTileSize()).getMagnitude()));
	} */
	
	public Vector2i getPos() {
		return position;
	}
	public void setPos(Vector2i position) {
		this.position = position;
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
	public String toString() {
		return "Structure:" + "\nposition: " + position.toString() +  
			   "\nsize: " + new Vector2i(width, height).toString();
	}
	public int getColl_width() {
		return coll_width;
	}
	public void setColl_width(int coll_width) {
		this.coll_width = coll_width;
	}
	public int getColl_height() {
		return coll_height;
	}
	public void setColl_height(int coll_height) {
		this.coll_height = coll_height;
	}
	public Vector2i getPosition() {
		return position;
	}
	
}