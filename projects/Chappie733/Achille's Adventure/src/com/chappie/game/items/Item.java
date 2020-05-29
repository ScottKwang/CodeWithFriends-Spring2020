package com.chappie.game.items;

import java.awt.image.BufferedImage;

import com.chappie.game.items.potions.HealingPotion;

/**
 * An abstract representation of an Item, not used for rendering
 * 
 * @author Chappie
 * @since 8/5/2020 (dd/mm/yyyy)
 */
public abstract class Item {
	
	protected ItemCondition condition;
	protected int damage, weight, speed; // dmg bonus, weight influences moving speed, usage speed/a sword can be slow
	
	public Item(int damage, int weight, int speed, ItemCondition condition) {
		this.damage = damage;
		this.weight = weight;
		this.speed = speed;
		this.condition = condition;
	}
	
	public Item(int damage, int weight, int speed) {
		this.damage = damage;
		this.weight = weight;
		this.speed = speed;
		condition = ItemCondition.PERFECT;
	}

	public ItemCondition getCondition() {
		return condition;
	}

	public void setCondition(ItemCondition condition) {
		this.condition = condition;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public abstract BufferedImage getIcon();
	
	public static void LoadIcons() {
		Sword.LoadIcon();
		HealingPotion.LoadIcon();
	}
}
