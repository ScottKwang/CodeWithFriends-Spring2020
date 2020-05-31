package com.chappie.game.entities.components;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import com.chappie.engine.algorithms.Pathfinder;
import com.chappie.engine.algorithms.Utils;
import com.chappie.engine.math.MathUtils;
import com.chappie.engine.math.Vector2f;
import com.chappie.engine.math.Vector2i;
import com.chappie.game.entities.Component;
import com.chappie.game.entities.enemies.Enemy;
import com.chappie.main.Handler;

public class CPlayerFinder extends Component {
	
	private Handler handler;
	private Enemy enemy; 
	private List<Vector2i> path;
	private Pathfinder pathfinder;
	private Vector2i lastPlayerPos;
	private int currTilesMA; // curr Tiles moved along
	private float range;
	
	public CPlayerFinder(Handler handler, Enemy enemy, float range) {
		super();
		this.handler = handler;
		this.enemy = enemy;
		this.range = range;
		path = new LinkedList<Vector2i>();	
		currTilesMA = 0;
	}
	
	@Override
	public void onUpdate() {
		if (Vector2i.getDistance(enemy.getTilePosition(), handler.getPlayer().getTilePosition()) <= range) {
			managePathFinding();
			Vector2i curr = enemy.getTilePosition();
			Vector2i next = path.get(currTilesMA);
			Vector2f speed = new Vector2f(MathUtils.normalize(next.getX()-curr.getX()) * enemy.getAbsoluteSpeed(),
										  MathUtils.normalize(next.getY()-curr.getY()) * enemy.getAbsoluteSpeed());
			enemy.setSpeed(speed);
		} else path.clear();
	}

	@Override
	public void onRender(Graphics2D g) {}
	
	private void managePathFinding() {
		if (path.isEmpty()) {
			Vector2i enemyTilePos = enemy.getTilePosition(); // self explanatory lol
			Vector2i playerTilePos = handler.getPlayer().getTilePosition(); // ""
			int distance = (int) Vector2i.getDistance(enemyTilePos, playerTilePos); // distance in tiles between player and enemy
			Vector2i subPos = new Vector2i(Math.min(enemyTilePos.getX(), playerTilePos.getX()), // position in the map of array representing the map in the
										   Math.min(enemyTilePos.getY(), playerTilePos.getY())); // zone from the enemy up to the player
			int map_section[][] = Utils.getSubArray(handler.getMap().getMap(), subPos, new Vector2i(distance+1, distance+1)); // distance+1 because otherwise it might round the distance down
			pathfinder = new Pathfinder(map_section, enemyTilePos, playerTilePos); // the pathfinder
			path = pathfinder.getPath(); // get the path
			// need it to make a copy and not just get a refence, i don't need to change the position
			lastPlayerPos = new Vector2i(playerTilePos.getX(), playerTilePos.getY()); // save the pos of the player (in tiles) to not find the path again when he moves
		} else {
			if (!Vector2i.equals(lastPlayerPos, handler.getPlayer().getTilePosition())) { // if the player moved from the current tile to an another
				path.add(handler.getPlayer().getTilePosition()); // add it to the path
				lastPlayerPos = new Vector2i(handler.getPlayer().getTilePosition().getX(), handler.getPlayer().getTilePosition().getY()); // save the pos of the player (in tiles) to not find the path again when he moves
			}
		}
	}
	
}
