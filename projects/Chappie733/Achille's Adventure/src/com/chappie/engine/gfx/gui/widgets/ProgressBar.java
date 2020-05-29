package com.chappie.engine.gfx.gui.widgets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.chappie.engine.gfx.gui.GUIElement;
import com.chappie.engine.math.Vector2i;

public class ProgressBar extends GUIElement {
	
	private int length, progress;
	private BufferedImage texture;

	public ProgressBar(Vector2i pos, Vector2i size, int length, int progress, BufferedImage texture) {
		super(pos, size);
		this.length = length;
		this.progress = progress;
		this.texture = texture;
	}
	
	public ProgressBar(Vector2i pos, Vector2i size, int length, int progress) {
		super(pos, size);
		this.length = length;
		this.progress = progress;
	}
	
	public ProgressBar(Vector2i pos, Vector2i size, int length) {
		super(pos, size);
		this.length = length;
		progress = 0;
	}
	

	@Override
	public void update() {}
	
	@Override
	public void render(Graphics2D g) {
		if (texture == null) {
			g.setColor(Color.black);
			g.drawRect(pos.getX(), pos.getY(), size.getX(), size.getY());
		}
		g.setColor(Color.green);
		g.fillRect(pos.getX(), pos.getY()+size.getY()/8, (int) (((float)progress/(float)length)*size.getX()), size.getY()*3/4);
		g.drawImage(texture, pos.getX(), pos.getY(), size.getX(), size.getY(), null);
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = (progress>=0)?progress:this.progress;
	}

	@Override
	public void onMousePressed(int mouseButton) {}

	@Override
	public void onMouseReleased(int mouseButton) {}
}
