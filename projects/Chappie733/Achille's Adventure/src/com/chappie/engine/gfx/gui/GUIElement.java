package com.chappie.engine.gfx.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.chappie.engine.math.MathUtils;
import com.chappie.engine.math.Vector2i;
import com.chappie.engine.window.Window;

public abstract class GUIElement {
	
	protected Vector2i pos, size;
	protected static Window window;
	
	public GUIElement(Vector2i pos, Vector2i size) {
		this.pos = pos;
		this.size = size;
	}
	
	public abstract void update();
	public abstract void render(Graphics2D g);
	public abstract void onMousePressed(int mouseButton);
	public abstract void onMouseReleased(int mouseButton);
	
	protected boolean isMouseOnMe() {
		Rectangle mouse = new Rectangle(window.getMousePosition().getX(), window.getMousePosition().getY()-32, 5, 5);
		Rectangle gui_element = new Rectangle(pos.getX(), pos.getY(), size.getX(), size.getY());
		return MathUtils.Intersect(mouse, gui_element);
	}
	
	public Vector2i getPos() {
		return pos;
	}

	public void setPos(Vector2i pos) {
		this.pos = pos;
	}

	public Vector2i getSize() {
		return size;
	}

	public void setSize(Vector2i size) {
		this.size = size;
	}
	
	public static void setWindowInstance(Window win) {
		window = win;
	}
	
}
