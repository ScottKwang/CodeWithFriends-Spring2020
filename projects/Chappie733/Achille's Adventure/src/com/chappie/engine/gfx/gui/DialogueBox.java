package com.chappie.engine.gfx.gui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.chappie.engine.files.Loader;
import com.chappie.engine.math.Vector2i;

public class DialogueBox extends GUIElement {
	
	private static BufferedImage texture = Loader.LoadImage("gui/widgets/dialog_box.png");
	private String textLines[];
	private Font font;
	
	public DialogueBox(Vector2i pos, Vector2i size, Font font) {
		super(pos, size);
		this.font = font;
	}
	
	public DialogueBox(Vector2i pos, Vector2i size) {
		super(pos, size);
		font = new Font("TimesRoman", Font.PLAIN, 25);
	}

	@Override
	public void update() {}

	@Override
	public void render(Graphics2D g) {
		g.setFont(font);
		g.drawImage(texture, pos.getX(), pos.getY(), size.getX(), size.getY(), null);
		for (int i = 0; i < textLines.length; i++)
			g.drawString(textLines[i], pos.getX()+(float) size.getX()/10 , pos.getY()+(float)size.getY()/5+font.getSize()*i);
	}
	
	public void setText(String text) {
		textLines = text.split("\n");
	}
	public String[] getText() {
		return this.textLines;
	}

	@Override
	public void onMousePressed(int mouseButton) {
		
	}

	@Override
	public void onMouseReleased(int mouseButton) {
		
	}
	
}
