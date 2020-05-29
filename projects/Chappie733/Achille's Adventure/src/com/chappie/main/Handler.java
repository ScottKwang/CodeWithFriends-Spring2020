package com.chappie.main;

import com.chappie.game.entities.Player;
import com.chappie.game.map.TileMap;
import com.chappie.game.map.World;

public class Handler {
	
	private Player player;
	private TileMap map;
	private World world;
	
	public Handler(Player player, TileMap map, World world) {
		this.player = player;
		this.map = map;
		this.world = world;
	}
	
	public Handler(Player player, TileMap map) {
		this.player = player;
		this.map = map;
	}
	
	public Handler(Player player) {
		this.player = player;
	}
	
	public Handler() {}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public TileMap getMap() {
		return map;
	}
	public void setMap(TileMap map) {
		this.map = map;
	}
	public void setWorld(World world) {
		this.world = world;
	}
	public World getWorld() {
		return world;
	}
	
}
