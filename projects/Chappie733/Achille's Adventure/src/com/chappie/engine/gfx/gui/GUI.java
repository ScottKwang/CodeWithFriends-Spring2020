package com.chappie.engine.gfx.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.chappie.main.Game;

public class GUI {
	
	private List<GUIElement> elements;
	private BufferedImage background;
	
	public GUI(List<GUIElement> elements) {
		this.elements = elements;
	}
	
	public GUI(BufferedImage background) {
		elements = new ArrayList<GUIElement>();
		this.background = background;
	}
	
	public GUI() {
		elements = new ArrayList<GUIElement>();
	}
	
	public void update() {
		for (GUIElement e : elements)
			e.update();
	}
	
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, Game.WIDTH, Game.HEIGHT, null);
		for (GUIElement e : elements)
			e.render(g);
	}
	
	public void onMouseClick(int buttonClicked) {
		for (GUIElement e : elements)
			e.onMousePressed(buttonClicked);
	}
	
	public void onMouseRelease(int buttonClicked) {
		for (GUIElement e : elements)
			e.onMouseReleased(buttonClicked);
	}
	
	public void addElement(GUIElement element) {
		this.elements.add(element);
	}
	
	public void removeElement(GUIElement element) {
		elements.remove(element);
	}
	
	public GUIElement getElement(int index) {
		return elements.get(index);
	}

}
