package com.chappie.engine.gfx.gui.widgets;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.gui.GUIElement;
import com.chappie.engine.math.Vector2i;

/**
 * Represents and handles all that a button is
 * 
 * @author Chappie
 * @since 19/05/2020 (dd/mm/yyyy)
 */
public class Button extends GUIElement {
	
	private List<BufferedImage> textures;
	private Callable<Void> action;
	private byte state = 0;
	
	private static byte DEFAULT = 0,
						SELECTED = 1,
						CLICKED = 2;
	
	public Button(Vector2i pos, Vector2i size, List<BufferedImage> textures, Callable<Void> action) {
		super(pos, size);
		this.textures = textures;
		this.action = action;
	}
	
	public Button(Vector2i pos, Vector2i size, List<BufferedImage> textures) {
		super(pos, size);
		this.textures = textures;
	}
	
	public Button(Vector2i pos, Vector2i size) {
		super(pos, size);
		// set textures to default parameters
		textures = Arrays.asList(Loader.LoadImage("gui/widgets/button/button_default.png"),
								 Loader.LoadImage("gui/widgets/button/button_selected.png"),
								 Loader.LoadImage("gui/widgets/button/button_clicked.png"));
	}

	@Override
	public void update() {
		if (isMouseOnMe()) {
			if (state == DEFAULT)
				state = SELECTED;
		} else if (state != DEFAULT) state = DEFAULT;
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(textures.get(state), pos.getX(), pos.getY(), size.getX(), size.getY(), null);
	}

	@Override
	public void onMousePressed(int mouseButton) {
		if (isMouseOnMe()) 
			state = CLICKED;
	}

	@Override
	public void onMouseReleased(int mouseButton) {
		if (isMouseOnMe()) {
			try {
				action.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setAction(Callable<Void> action) {
		this.action = action;
	}
	
}
