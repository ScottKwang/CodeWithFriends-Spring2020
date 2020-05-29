package com.chappie.game.entities.components;

import java.awt.Graphics2D;

import com.chappie.engine.math.MathUtils;
import com.chappie.engine.math.Vector2f;
import com.chappie.engine.math.Vector2i;
import com.chappie.engine.time.Timer;
import com.chappie.game.entities.Component;
import com.chappie.game.entities.Entity;
import com.chappie.main.Handler;

public class CNpcAi extends Component {
	
	private Vector2i target; // tiles
	private Entity npc;
	private Handler handler;
	private int movingRange; // in tiles
	private Timer target_delay; // how much till the npc gives up (yeah im too lazy to implement path finding)
	
	public CNpcAi(Entity npc, Handler handler, int movingRange) {
		this.npc = npc;
		this.handler = handler;
		this.movingRange = movingRange;
		target_delay = new Timer(7000);
	}
	
	@Override
	public void onUpdate() {
		target_delay.update();
		if (target == null || Vector2i.equals(npc.getTilePosition(), target) || target_delay.isOver()) {
			target = new Vector2i(npc.getTilePosition().getX()+MathUtils.getRandom().nextInt(movingRange)*((MathUtils.getRandom().nextBoolean())?1:-1), 
								  npc.getTilePosition().getX()+MathUtils.getRandom().nextInt(movingRange)*((MathUtils.getRandom().nextBoolean())?1:-1));
			while (handler.getMap().getTile(target.getX(), target.getY()).isSolid() && (target.getX() < 0 && target.getY() < 0))
				target = new Vector2i(npc.getTilePosition().getX()+MathUtils.getRandom().nextInt(movingRange)*((MathUtils.getRandom().nextBoolean())?1:-1), 
						  			  npc.getTilePosition().getX()+MathUtils.getRandom().nextInt(movingRange)*((MathUtils.getRandom().nextBoolean())?1:-1));
			target_delay.restart();
		} else {
			Vector2i distance = Vector2i.getDifference(npc.getTilePosition(), target);
			Vector2f speed = new Vector2f(npc.getAbsoluteSpeed()*-MathUtils.normalize(distance.getX()),
					  npc.getAbsoluteSpeed()*-MathUtils.normalize(distance.getY()));
			npc.setSpeed(speed);
		}
	}

	@Override
	public void onRender(Graphics2D g) {}
	
}