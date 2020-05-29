package com.chappie.game.items.potions;

import com.chappie.engine.gfx.particles.ParticleType;
import com.chappie.game.entities.Entity;
import com.chappie.game.items.Item;
import com.chappie.game.items.ItemCondition;

public abstract class Potion extends Item {
	
	public Potion() {
		super(0, 0, 0, ItemCondition.PERFECT);
	}
	
	public abstract ParticleType consume(Entity e);
	
}
