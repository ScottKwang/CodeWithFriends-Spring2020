package com.chappie.game.entities.npcs;

import java.awt.Graphics2D;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.animations.AnimationManager;
import com.chappie.engine.math.Vector2f;
import com.chappie.game.entities.Entity;
import com.chappie.game.entities.components.CCollider;
import com.chappie.game.entities.components.CNpcAi;
import com.chappie.main.Handler;

public class NPC extends Entity {
	
	private AnimationManager AM;
	private boolean facingRight;
	
	public NPC(Vector2f pos, int width, int height, Handler handler) {
		super(pos, width, height);
		this.handler = handler;
		this.health = 100;
		abs_speed = 2f;
	}

	@Override
	public void init() {
		AM = new AnimationManager();
		LoadAnimations();
		addComponent(new CNpcAi(this, handler, 3));
		addComponent(new CCollider(this, handler));
		facingRight = true;
	}
	
	@Override
	public void update() {
		super.update();
		manageAnimations();
		AM.update();
	}
	
	@Override
	public void render(Graphics2D g) {
		if (isDead()) return;
		super.render(g);
		g.drawImage(AM.getTexture(), (int) (pos.getX()-handler.getMap().getPosOffSet().getX())+((facingRight)?0:width), 
								     (int) (pos.getY()-handler.getMap().getPosOffSet().getY()), width*((facingRight)?1:-1), height, null);
	}
	
	private void manageAnimations() {
		if (speed.getX() > 0) {
			facingRight = true;
			AM.setState((byte) 1);
		}
		else if (speed.getX() < 0) {
			facingRight = false;
			AM.setState((byte) 1);
		}
		else if (!isDead()) AM.setState((byte) 0);
		
		if (speed.getY() > 0) 
			AM.setState((byte) 1);
		else if (speed.getY() < 0) 
			AM.setState((byte) 1);
		else if (!isDead() && speed.getX() == 0) AM.setState((byte) 0);
	}
	
	private void LoadAnimations() {
		AM.addAnimation(Loader.LoadAnimation("res/textures/entities/npcs/idle", 500, true));
		AM.addAnimation(Loader.LoadAnimation("res/textures/entities/npcs/walk", 150, true));
	}
}
