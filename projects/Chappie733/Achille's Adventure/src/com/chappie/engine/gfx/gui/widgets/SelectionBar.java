package com.chappie.engine.gfx.gui.widgets;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.gui.GUIElement;
import com.chappie.engine.math.Vector2i;

public class SelectionBar extends GUIElement {
	
	private int yOffSet;
	private byte length, chosen;
	private BufferedImage texture, marker;
	private Vector2i markerSize;
	private String options[];
	private Callable<Void> onSettingChange;
	
	public static BufferedImage DEFAULT_TEXTURE = Loader.LoadImage("gui/widgets/selection_bar.png"),
								DEFAULT_MARKER_TEXTURE = Loader.LoadImage("gui/widgets/selectionbar_marker.png");
	
	public SelectionBar(Vector2i pos, Vector2i size, String options[], BufferedImage marker,
			Vector2i markerSize, BufferedImage texture, int yOffSet, Callable<Void> onSettingChange,
			byte chosen) {
		super(pos, size);
		this.options = options;
		this.texture = texture;
		this.marker = marker;
		this.markerSize = markerSize;
		this.length = (byte) (options.length-1);
		this.yOffSet = yOffSet;
		this.onSettingChange = onSettingChange;
		this.chosen = chosen;
	}
	
	public SelectionBar(Vector2i pos, Vector2i size, String options[], BufferedImage marker,
			Vector2i markerSize, BufferedImage texture, int yOffSet, Callable<Void> onSettingChange) {
		super(pos, size);
		this.options = options;
		this.texture = texture;
		this.marker = marker;
		this.markerSize = markerSize;
		this.length = (byte) (options.length-1);
		this.yOffSet = yOffSet;
		this.onSettingChange = onSettingChange;
	}
	
	public SelectionBar(Vector2i pos, Vector2i size, String options[], BufferedImage marker,
			Vector2i markerSize, BufferedImage texture, int yOffSet) {
		super(pos, size);
		this.options = options;
		this.texture = texture;
		this.marker = marker;
		this.markerSize = markerSize;
		this.length = (byte) (options.length-1);
		this.yOffSet = yOffSet;
	}
	
	public SelectionBar(Vector2i pos, Vector2i size, String options[], BufferedImage marker) {
		super(pos, size);
		this.options = options;
		this.marker = marker;
		this.markerSize = new Vector2i(1,1);
		this.length = (byte) (options.length-1);
	}
	
	public SelectionBar(Vector2i pos, Vector2i size, String options[]) {
		super(pos, size);
		this.options = options;
		this.markerSize = new Vector2i(32, 64);
		this.length = (byte) (options.length-1);
	}
	
	public SelectionBar(Vector2i pos, Vector2i size, byte length) {
		super(pos, size);
		this.length = length;
		this.markerSize = new Vector2i(32, 64);
	}

	@Override
	public void update() {}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, pos.getX(), pos.getY(), size.getX(), size.getY(), null);
		if (marker != null) g.drawImage(marker, (int) (pos.getX()+((float)size.getX()/length)*chosen)-marker.getWidth()/2, pos.getY()+yOffSet, markerSize.getX(), markerSize.getY(), null);
	}

	@Override
	public void onMousePressed(int mouseButton) {
		
	}

	@Override
	public void onMouseReleased(int mouseButton) {
		if (isMouseOnMe()) {
			chosen = (byte) ((window.getMousePosition().getX()-pos.getX())/(size.getX()/length-1));
			if (onSettingChange != null) {
				try {
					onSettingChange.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public byte getLength() {
		return length;
	}

	public void setLength(byte length) {
		this.length = length;
	}

	public byte getChosen() {
		return chosen;
	}

	public void setChosen(byte chosen) {
		this.chosen = chosen;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}
	
	public String getSelected() {
		return options[chosen];
	}
	
	public void setOptions(String options[]) {
		this.options = options;
	}

	public BufferedImage getMarker() {
		return marker;
	}

	public void setMarker(BufferedImage marker) {
		this.marker = marker;
	}

	public String[] getOptions() {
		return options;
	}

	public Vector2i getMarkerSize() {
		return markerSize;
	}

	public void setMarkerSize(Vector2i markerSize) {
		this.markerSize = markerSize;
	}

	public int getyOffSet() {
		return yOffSet;
	}

	public void setyOffSet(int yOffSet) {
		this.yOffSet = yOffSet;
	}

	public Callable<Void> getOnSettingChange() {
		return onSettingChange;
	}

	public void setOnSettingChange(Callable<Void> onSettingChange) {
		this.onSettingChange = onSettingChange;
	}
	
}
