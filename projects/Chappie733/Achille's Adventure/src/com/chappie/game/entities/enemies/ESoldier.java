package com.chappie.game.entities.enemies;

import java.awt.Graphics2D;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.animations.AnimationManager;
import com.chappie.engine.math.Vector2f;
import com.chappie.game.entities.components.CCollider;
import com.chappie.game.entities.components.CFollowPlayer;
import com.chappie.main.Handler;

public class ESoldier extends Enemy {

	private AnimationManager AM;
	private boolean facingRight;
	
	public ESoldier(Vector2f pos, Handler handler) {
		super(pos, 60, 60, handler, 2f, 10*handler.getMap().getTileSize(), 15);
	}
	
	@Override
	public void init() {
		AM = new AnimationManager();
		AM.addAnimation(Loader.LoadAnimation("res/textures/entities/esoldier/idle", 500, true));
		AM.addAnimation(Loader.LoadAnimation("res/textures/entities/esoldier/walk", 100, true));
		AM.addAnimation(Loader.LoadAnimation("res/textures/entities/esoldier/dead", 0, false));
		facingRight = true;
		addComponent(new CFollowPlayer(handler, this, range));
		addComponent(new CCollider(this, handler));
	}
	
	@Override
	public void update() {
		super.update();
		handleAnimations();
		AM.update();
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawImage(AM.getTexture(), (int) (pos.getX()-handler.getMap().getPosOffSet().getX()+(facingRight?0:width)), 
				(int)  (pos.getY() - handler.getMap().getPosOffSet().getY()), 
				facingRight?width:-width, height, null);
	}
	
	private void handleAnimations() {
		if (speed.getX() > 0) {
			AM.setState((byte) 1);
			facingRight = true;
		}
		else if (speed.getX() < 0) {
			AM.setState((byte) 1);
			facingRight = false;
		}
		else if (!isDead()) AM.setState((byte) 0);
		
		if (speed.getY() > 0) AM.setState((byte) 1);
		else if (speed.getY() < 0) AM.setState((byte) 1);
		else if (!isDead() && speed.getX() == 0) AM.setState((byte) 0);
	}
	
	@Override
	public void damage(int amount) {
		health -= damage;
		if (isDead()) {
			AM.setState((byte) 2); 
			disableComponents();
		}
	}
	
}
