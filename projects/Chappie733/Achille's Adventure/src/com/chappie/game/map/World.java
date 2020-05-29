package com.chappie.game.map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.chappie.engine.math.MathUtils;
import com.chappie.engine.math.Vector2f;
import com.chappie.engine.math.Vector2i;
import com.chappie.game.entities.Entity;
import com.chappie.game.entities.Player;
import com.chappie.game.entities.enemies.Enemy;

public class World {
	
	private TileMap map;
	private List<Entity> entities;
	private List<Enemy> enemies;
	private Player player;
	
	public World(TileMap map, Player player, List<Enemy> enemies) {
		this.map = map;
		this.player = player;
		this.enemies = enemies;
	}
	
	public World(TileMap map, Player player) {
		this.map = map;
		this.player = player;
	}
	
	public void init() {
		if (entities == null) entities = new ArrayList<Entity>();
		if (enemies == null) enemies = new ArrayList<Enemy>();
		for (Entity e : entities)
			e.init();
		for (Enemy e : enemies) 
			e.init();
	}
	
	public void update() {
		player.update();
		for (Entity e : entities)
			if (!(e instanceof Enemy)) 
				e.update();
		for (Enemy e : enemies) {
			e.update();
			if (e.isDead())
				e.disableComponents();
		}
	}
	
	public void render(Graphics2D g) {
		map.render(g);
		for (Entity e : entities)
			e.render(g);
		player.render(g);
	}
	
	public ArrayList<Entity> getEntitiesInArea(Vector2f pos, Vector2i size) {
		Rectangle rect = new Rectangle((int) pos.getX(), (int) pos.getY(), size.getX(), size.getY());
		ArrayList<Entity> in_area = new ArrayList<Entity>();
		for (Entity e : entities)
			if (MathUtils.Intersect(e.getHitBox(), rect))
				in_area.add(e);
		return in_area;
	}
	
	public ArrayList<Enemy> getEnemiesInArea(Vector2f pos, Vector2i size) {
		Rectangle rect = new Rectangle((int) pos.getX(), (int) pos.getY(), size.getX(), size.getY());
		ArrayList<Enemy> in_area = new ArrayList<Enemy>();
		for (Enemy e : enemies)
			if (MathUtils.Intersect(e.getHitBox(), rect))
				in_area.add(e);
		return in_area;
	}
	
	public ArrayList<Enemy> getEnemiesInScreenArea(Vector2f pos, Vector2i size) {
		Rectangle rect = new Rectangle((int) pos.getX(), (int) pos.getY(), size.getX(), size.getY());
		ArrayList<Enemy> in_area = new ArrayList<Enemy>();
		for (Enemy e : enemies)
			if (e.isOnScreen())
				if (MathUtils.Intersect(e.getHitBox(), rect))
					in_area.add(e);
		return in_area;
	}
	
	public void generateEnemies() {
		// TODO: enemy generation
	}
	
	public void logEnemy(Enemy e) {
		enemies.add(e);
		logEntity(e);
	}
	
	public void logEntity(Entity e) {
		entities.add(e);
	}

	public TileMap getMap() {
		return map;
	}

	public void setMap(TileMap map) {
		this.map = map;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(List<Enemy> enemies) {
		this.enemies = enemies;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<Entity> getEntities() {
		return entities;
	}
	
}
