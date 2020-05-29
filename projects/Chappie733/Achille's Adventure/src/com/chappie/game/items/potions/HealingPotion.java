package com.chappie.game.items.potions;

import java.awt.image.BufferedImage;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.particles.ParticleType;
import com.chappie.engine.sounds.SoundManager;
import com.chappie.game.entities.Entity;

public class HealingPotion extends Potion {
	
	private static BufferedImage icon;
	private static int HEALTH_PER_TIER = 25, SOUND;
	private int tier;
	
	public HealingPotion(int tier) {
		this.tier = tier;
	}
	
	public static void LoadIcon() {
		icon = Loader.LoadImage("items/healing_potion.png");
		SOUND = SoundManager.LoadSound("feedback/healing.wav");
	}
	
	@Override
	public BufferedImage getIcon() {
		return icon;
	}
	
	@Override
	public ParticleType consume(Entity e) {
		e.addHealth(HEALTH_PER_TIER*tier);
		SoundManager.PlaySound(SOUND);
		return ParticleType.HEALING;
	}
	
}
