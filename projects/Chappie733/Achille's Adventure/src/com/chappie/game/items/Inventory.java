package com.chappie.game.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.particles.ParticleType;
import com.chappie.engine.math.Vector2i;
import com.chappie.engine.time.Timer;
import com.chappie.game.entities.Entity;
import com.chappie.game.items.potions.Potion;
import com.chappie.main.Game;

public class Inventory {
	
	private List<Item> items;
	private byte size, selected;
	private boolean active;
	private Timer active_timer, selection_timer;
	private static BufferedImage texture, marker;
	private static final int IconSize = 32;
	private Vector2i pos, offSet;
	
	/**
	 * 
	 * @param size how many items can there be in this inventory
	 * @param selected the item selected
	 * @param pos position of the inventory
	 * @param offSet x -> offset from top to first item, y -> offset from bottom to last item
	 */
	public Inventory(byte size, byte selected, Vector2i pos, Vector2i offSet) {
		this.size = size;
		this.selected = selected;
		this.offSet = offSet;
		this.pos = pos;
	}
	
	public Inventory(byte size, byte selected, Vector2i pos) {
		this.size = size;
		this.selected = selected;
		this.pos = pos;
	}
	
	public Inventory(byte size, byte selected) {
		this.size = size;
		this.selected = selected;
		pos = new Vector2i(Game.WIDTH-222, Game.HEIGHT*1/5);
	}
	
	public Inventory(byte size) {
		this.size = size;
		this.selected = 1;
		offSet = new Vector2i(32, 70);
		pos = new Vector2i(Game.WIDTH-222, Game.HEIGHT*1/5);
	}
	
	public void init() {
		items = new ArrayList<Item>();
		active_timer = new Timer(250);
		selection_timer = new Timer(250);
		texture = Loader.LoadImage("gui/inventory/inventory.png");
		marker = Loader.LoadImage("gui/inventory/item_marker.png");
		
	}
	
	public void update() {
		active_timer.update();
		selection_timer.update();
	}
	
	public void render(Graphics2D g) {
		if (active) {
			if (items.size() == 0) return;
			g.drawImage(texture, pos.getX(), pos.getY(), null);
			
			if (selected-1 >= 0) g.drawImage(items.get(selected-1).getIcon(), pos.getX()+offSet.getX(), pos.getY()+offSet.getY(), IconSize, IconSize, null);
			g.drawImage(items.get(selected).getIcon(), pos.getX()+offSet.getX(), pos.getY()+offSet.getY()+IconSize*3/2, IconSize, IconSize, null);
			if (selected+1 <= items.size()-1) g.drawImage(items.get(selected+1).getIcon(), pos.getX()+offSet.getX(), pos.getY()+offSet.getY()+IconSize*3, IconSize, IconSize, null);
			
			g.drawImage(marker, pos.getX()+texture.getWidth()/2, pos.getY()+texture.getHeight()/2-marker.getHeight()/2, null);
		} else 
			g.drawImage(items.get(selected).getIcon(), Game.WIDTH-IconSize*3/2, IconSize*1/2, IconSize, IconSize, null);
		
	}
	
	public Item getItem(int index) {
		return items.get(index);
	}
	
	public boolean addItem(Item item) {
		if (items.size() <= size) {
			items.add(item);
			return true;
		}
		return false;
	}
	public void selectNext() {
		if (selection_timer.isOver()) {
			if (selected != items.size()-1)
				++selected;
			selection_timer.restart();
		}
	}
	public void selectPrevious() {
		if (selection_timer.isOver()) {
			if (selected != 0)
				--selected;
			selection_timer.restart();
		}
	}
	public ParticleType onItemIteraction(Entity e) {
		ParticleType particles = ParticleType.EMPTY;
		if (items.get(selected) instanceof Potion && selection_timer.isOver()) {
			Potion p = (Potion) items.get(selected);
			particles = p.consume(e);
			items.remove(selected);
			selection_timer.restart();
			--selected;
		}
		return particles;
	}
	public void removeItem(int index) {
		items.remove(index);
	}
	public void removeItem(Item item) {
		items.remove(item);
	}
	public Item getSelected() {
		return items.get(selected);
	}
	public void setSize(byte size) {
		this.size = size;
	}
	public byte getSize() {
		return size;
	}
	public void setSelected(byte selected) {
		this.selected = selected;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		if (active_timer.isOver()) {
			this.active = active;
			active_timer.restart();
		}
	}
	
}
