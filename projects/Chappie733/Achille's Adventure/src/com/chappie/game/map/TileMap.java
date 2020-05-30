package com.chappie.game.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.chappie.engine.files.Loader;
import com.chappie.engine.math.MathUtils;
import com.chappie.engine.math.Vector2f;
import com.chappie.engine.math.Vector2i;
import com.chappie.game.entities.components.CFollowPlayer;
import com.chappie.game.map.structures.Structure;
import com.chappie.main.Game;
import com.chappie.main.Handler;

public class TileMap {
	
	private int map[][]; // each tile has an id and is represented by it
	private int tileSize, width, height;
	private boolean solid[][];
	
	private List<BufferedImage> tileTextures; // the position in the array is the index of the tile
	// at each index there is the int representing 
	private List<Integer> tileColors; // the color in rgb of the tile with that id (equal to the index)
	
	private List<Structure> structures;

	private Handler handler;
	private Vector2f pos_offSet;
	
	private static final Vector2f mvm_offsetDelay = new Vector2f(Game.WIDTH/8, Game.HEIGHT/8);
	public static final int DEFAULT_TILESIZE = 32;
	
	public TileMap(int tileSize, Handler handler) {
		this.tileSize = tileSize;
		this.handler = handler;
		CFollowPlayer.setDefaultRange(15*tileSize);
		tileColors = new LinkedList<Integer>();
		structures = new ArrayList<Structure>();
		pos_offSet = new Vector2f(0,0);
	}
	
	public void LoadMap(BufferedImage source, String tileData, 
			BufferedImage structSource, String structData) {
		width = source.getWidth();
		height = source.getHeight();
		
		map = new int[width][height];
		solid = new boolean[width][height];
		
		LoadTileData(tileData);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				map[x][y] = tileColors.indexOf(source.getRGB(x, y));
				solid[x][y] = Tile.getTile(map[x][y]).isSolid();
			}
		}
		
		LoadStructures(structSource, structData);
	}
	
	public void render(Graphics2D g) {
		int xTileStart = (int) Math.max(0, pos_offSet.getX()/tileSize);
		int xTileEnd = (int) Math.min(width-1, (pos_offSet.getX()+Game.WIDTH)/tileSize)+1;
		int yTileStart = (int) Math.max(0, pos_offSet.getY()/tileSize);
		int yTileEnd = (int) Math.min(height-1, (pos_offSet.getY()+Game.HEIGHT)/tileSize)+1;
		
		for (int y = yTileStart; y < yTileEnd; y++) {
			for (int x = xTileStart; x < xTileEnd; x++) {
				g.drawImage(tileTextures.get(map[x][y]), (int) (x*tileSize-pos_offSet.getX()), (int) (y*tileSize-pos_offSet.getY()), tileSize, tileSize, null);
			}
		}
		for (Structure s : structures) 
			if (MathUtils.Intersect(s.getPos().getX(), s.getPos().getY(), 
								    s.getWidth(), s.getHeight(), 
								    xTileStart, yTileStart, (xTileEnd-xTileStart), (yTileEnd-yTileStart)))
				s.render(g);
	}
	
	// the file has this format: {tile texture} {red} {green} {blue}
	private void LoadTileData(String tileData) {
		if (tileTextures == null) tileTextures = new ArrayList<BufferedImage>();
		String lines[] = null; // each line
		try {
			lines = Loader.LoadFile(tileData).split("\n");
		} catch (IOException e) { e.printStackTrace(); }
		Tile.initializeTileStorage(lines.length);
		for (String str : lines) {
			String cTileData[] = str.split("\\s+");
			Tile.registerTile(new Tile(tileTextures.size(), cTileData[4].startsWith("true"), Loader.LoadImage("tiles/"+cTileData[0])));
			tileTextures.add(Loader.LoadImage("tiles/" + cTileData[0]));
			tileColors.add(new Color(Integer.parseInt(cTileData[1]),
									 Integer.parseInt(cTileData[2]),
									 Integer.parseInt(cTileData[3])).getRGB());
		}
	}

	// the file has this format: {struct texture} {red} {green} {blue} {width} {height} {collision Width} {collision Height}
	private void LoadStructures(BufferedImage structSource, String structData) {
		List<Integer> colors = new LinkedList<Integer>();
		List<BufferedImage> textures = new LinkedList<BufferedImage>();
		List<Vector2i> sizes = new ArrayList<Vector2i>();
		List<Vector2i> coll_sizes = new ArrayList<Vector2i>();
		String lines[] = null; // each line
		try {
			lines = Loader.LoadFile(structData).split("\n");
		} catch (IOException e) { e.printStackTrace(); }
		int curr_line = 0;
		for (String str : lines) {
			String[] cStructData = str.split("\\s+");
			textures.add(Loader.LoadImage("structures/" + cStructData[0]));
			colors.add(new Color(Integer.parseInt(cStructData[1]),
							     Integer.parseInt(cStructData[2]),
							     Integer.parseInt(cStructData[3])).getRGB());
			sizes.add(new Vector2i(Integer.parseInt(cStructData[4]),
					   Integer.parseInt(cStructData[5])));
			if (cStructData.length == 8) {
				coll_sizes.add(curr_line, new Vector2i(Integer.parseInt(cStructData[6]),
						   			   				   Integer.parseInt(cStructData[7])));
				
			} else coll_sizes.add(curr_line, sizes.get(sizes.size()-1));
			curr_line++;
		}
		
		for (int y = 0; y < structSource.getHeight(); y++) {
			for (int x = 0; x < structSource.getWidth(); x++) {
				if (colors.contains(structSource.getRGB(x, y))) {
					int index = colors.indexOf(structSource.getRGB(x, y));
					Vector2i size = sizes.get(index);
					Vector2i coll_size = coll_sizes.get(index);
					structures.add(new Structure(new Vector2i(x,y), 
								                 textures.get(index),
								                 handler, 
								                 size.getX(),
								                 size.getY(),
								                 coll_size.getX(),
								                 coll_size.getY()));
					// set the map position to be collidable 
					for (int yt = 0; yt < coll_size.getY(); yt++) {
						for (int xt = 0; xt < coll_size.getX(); xt++) {
							solid[x+xt][y+yt] = true;
						}
					}
					
				}
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		return Tile.getTile(map[x][y]);
	}
	
	public boolean isSolid(float x, float y) {
		if (x < 0 || x/tileSize >= width || y < 0 || y/tileSize >= height) return true;
		return solid[(int) (x/tileSize)][(int) (y/tileSize)];
	}

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Vector2f getPosOffSet() {
		return pos_offSet;
	}
	
	public static Vector2f getMovementDelay() {
		return mvm_offsetDelay;
	}
	
	public int[][] getMap() {
		return map;
	}
	
	public boolean[][] getCollisionMap() {
		return solid;
	}
	
}
