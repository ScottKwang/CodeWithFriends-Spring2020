package com.chappie.game.entities.components;

import java.awt.Graphics2D;

import com.chappie.engine.math.MathUtils;
import com.chappie.engine.math.Vector2f;
import com.chappie.engine.math.Vector2i;
import com.chappie.game.entities.Component;
import com.chappie.game.entities.enemies.Enemy;
import com.chappie.main.Handler;

public class CFollowPlayer extends Component {
	
	private Handler handler;
	private Enemy enemy;
	private float range;
	private static float DEFAULT_RANGE = 10;
	
	public CFollowPlayer(Handler handler, Enemy enemy, float range) {
		this.handler = handler;
		this.enemy = enemy;
		this.range = range;
	}
	
	public CFollowPlayer(Handler handler, Enemy enemy) {
		this.handler = handler;
		this.enemy = enemy;
		this.range = DEFAULT_RANGE;
	}
	
	@Override
	public void onUpdate() {
		Vector2f playerPos = handler.getPlayer().getGamePos();
		Vector2i distance = Vector2i.getDifference(handler.getPlayer().getTilePosition(), enemy.getTilePosition());
		if (distance.getMagnitude() <= range) {
			Vector2f direction = new Vector2f(MathUtils.normalize(playerPos.getX()-enemy.getPos().getX()) * enemy.getAbsoluteSpeed(), 
											  MathUtils.normalize(playerPos.getY()-enemy.getPos().getY()) * enemy.getAbsoluteSpeed());
			if (distance.getX() != 0) enemy.getSpeed().setX(direction.getX());
			else enemy.getSpeed().setX(0);
			if (distance.getY() != 0) enemy.getSpeed().setY(direction.getY());
			else enemy.getSpeed().setY(0);
		} else enemy.setSpeed(new Vector2f(0));
	}

	@Override
	public void onRender(Graphics2D g) {}

	public static void setDefaultRange(float range) {
		DEFAULT_RANGE = range;
	}
	
}
