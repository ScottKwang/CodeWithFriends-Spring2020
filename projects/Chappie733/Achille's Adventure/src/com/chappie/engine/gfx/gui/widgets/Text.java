package com.chappie.engine.gfx.gui.widgets;

import java.awt.Graphics2D;

import com.chappie.engine.gfx.gui.GUIElement;
import com.chappie.engine.math.Vector2i;

public class Text extends GUIElement {
	
	private String content;
	
	public Text(Vector2i pos, String content) {
		super(pos, new Vector2i(0,0));
		this.content = content;
	}

	@Override
	public void update() {}

	@Override
	public void render(Graphics2D g) {
		g.drawString(content, pos.getX(), pos.getY());
	}

	@Override
	public void onMousePressed(int mouseButton) {}

	@Override
	public void onMouseReleased(int mouseButton) {}
	
}
