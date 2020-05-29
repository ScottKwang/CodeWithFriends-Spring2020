package com.chappie.game.entities.components;

import java.awt.Graphics2D;

import com.chappie.engine.math.Vector2f;
import com.chappie.game.entities.Component;
import com.chappie.game.entities.Entity;
import com.chappie.game.entities.Player;
import com.chappie.main.Handler;

/**
 * Collider component which handles collision detection for any entity
 * and in the case of the player handles the movement of the map
 * 
 * @author Chappie
 * @since 5/5/2020 
 */
public class CCollider extends Component {

	private Entity entity;
	private Handler handler;
	
	public CCollider(Entity entity, Handler handler) {
		super();
		this.entity = entity;
		this.handler = handler;
	}
	
	@Override
	public void onUpdate() {
		Vector2f next_pos = Vector2f.getSum(entity.getPos(), entity.getSpeed());
		if (entity.getSpeed().getX() > 0) {
			// top right and bottom right
			if (!handler.getMap().isSolid(next_pos.getX()+entity.getWidth(), entity.getPos().getY())
					&& !handler.getMap().isSolid(next_pos.getX()+entity.getWidth(), entity.getPos().getY()+entity.getHeight())) {
				entity.getPos().addX(entity.getSpeed().getX());
				if (entity instanceof Player) 
					handler.getMap().getPosOffSet().addX(entity.getSpeed().getX());
			}
				
		} else if (entity.getSpeed().getX() < 0) {
			// top left and bottom left
			if (!handler.getMap().isSolid(next_pos.getX(), entity.getPos().getY())
					&& !handler.getMap().isSolid(next_pos.getX(), entity.getPos().getY()+entity.getHeight())) {
				entity.getPos().addX(entity.getSpeed().getX());
				if (entity instanceof Player) 
					handler.getMap().getPosOffSet().addX(entity.getSpeed().getX());
			} 
		}
		if (entity.getSpeed().getY() < 0) {
			// top left and top right
			if (!handler.getMap().isSolid(entity.getPos().getX(), next_pos.getY())
					&& !handler.getMap().isSolid(next_pos.getX()+entity.getWidth(), next_pos.getY())) {
				entity.getPos().addY(entity.getSpeed().getY());
				if (entity instanceof Player) 
					handler.getMap().getPosOffSet().addY(entity.getSpeed().getY());
			} 
		}
		else if (entity.getSpeed().getY() > 0) {
			// bottom left and bottom right
			if (!handler.getMap().isSolid(entity.getPos().getX(), next_pos.getY()+entity.getHeight())
					&& !handler.getMap().isSolid(entity.getPos().getX()+entity.getWidth(), next_pos.getY()+entity.getHeight())) {
				entity.getPos().addY(entity.getSpeed().getY());
				if (entity instanceof Player) 
					handler.getMap().getPosOffSet().addY(entity.getSpeed().getY());
			} 
		}
	}

	@Override
	public void onRender(Graphics2D g) {}
	
}
