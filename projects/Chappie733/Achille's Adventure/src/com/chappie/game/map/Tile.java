package com.chappie.game.map;

import java.awt.image.BufferedImage;

public class Tile {
	
	private int id;
	private boolean solid;
	private BufferedImage texture;
	
	private static Tile tiles[];
	
	public Tile(int id, boolean solid, BufferedImage texture) {
		this.id = id;
		this.solid = solid;
	}
	
	public Tile(int id, boolean solid) {
		this.id = id;
		this.solid = solid;
	}
	
	public Tile(int id) {
		this.id = id;
		solid = false;
	}
	
	public int getId() { return id; }
	public BufferedImage getTexture() { return texture; }
	public boolean isSolid() { return solid; }
	
	public static BufferedImage getTexture(int index) { return tiles[index].getTexture(); }
	public static int getNumberOfTiles() { return tiles.length; }
	public static void registerTile(Tile t) { tiles[t.getId()] = t;}
	public static Tile getTile(int index) { return tiles[index]; }
	public static void initializeTileStorage(int storageSize) { tiles = new Tile[storageSize];}
}
