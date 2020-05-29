package com.chappie.game.items;

import java.awt.image.BufferedImage;

import com.chappie.engine.files.Loader;

public class Sword extends Item {

	private static BufferedImage icon;
	
	public Sword(ItemCondition condition) {
		super(60, 4, 5, condition);
	}
	
	public Sword() {
		super(60, 4, 5);
	}

	@Override
	public BufferedImage getIcon() {	
		return icon;
	}
	
	public static void LoadIcon() {
		icon = Loader.LoadImage("items/sword.png");
	}

}
