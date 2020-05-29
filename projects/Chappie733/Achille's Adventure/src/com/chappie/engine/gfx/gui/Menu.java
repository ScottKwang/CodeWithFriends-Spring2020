package com.chappie.engine.gfx.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.chappie.engine.input.Input;
import com.chappie.engine.math.Vector2i;
import com.chappie.engine.time.Timer;
import com.chappie.main.Game;

public class Menu {
	
	private String options[];
	private byte selected;
	private HashMap<Byte, Callable<Void>> actions;
	private Timer action_timer;
	private BufferedImage background;
	private Vector2i textPos;
	private int textSize;
	private Font font;
	
	public Menu(String options[], HashMap<Byte, Callable<Void>> actions, BufferedImage background,
			Vector2i textPos, int textSize) {
		this.options = options;
		this.actions = actions;
		this.background = background;
		this.textPos = textPos;
		this.textSize = textSize;
	}
	
	public Menu(String options[], Vector2i position, int textSize, BufferedImage background) {
		this.options = options;
		this.background = background;
		this.textPos = position;
		this.textSize = textSize;
		this.actions = new HashMap<Byte, Callable<Void>>();
	}
	
	public Menu(String options[], BufferedImage background) {
		this.options = options;
		this.background = background;
		this.textPos = new Vector2i(100, 350);
		this.textSize = 75;
		this.actions = new HashMap<Byte, Callable<Void>>();
	}
	
	public Menu(String options[], HashMap<Byte, Callable<Void>> actions, BufferedImage background) {
		this.options = options;
		this.actions = actions;
		this.background = background;
	}
	
	public Menu(String options[], Vector2i textPos, int textSize) {
		this.options = options;
		this.textPos = textPos;
		this.textSize = textSize;
		this.actions = new HashMap<Byte, Callable<Void>>();
	}
	
	public Menu(String options[], HashMap<Byte, Callable<Void>> actions) {
		this.options = options;
		this.actions = actions;
	}
	
	public Menu(String options[]) {
		this.options = options;
		actions = new HashMap<Byte, Callable<Void>>();
	}
	
	public void init() {
		selected = 0;
		action_timer = new Timer(150);
		font = new Font("TimesRoman", Font.PLAIN, textSize);
	}
	
	public void update() {
		action_timer.update();
		if (action_timer.isOver()) {
			if (Input.isKeyPressed(KeyEvent.VK_UP)) {
				--selected;
				if (selected < 0) selected = (byte) (options.length-1);
				action_timer.restart();
			}
			if (Input.isKeyPressed(KeyEvent.VK_DOWN)) {
				++selected;
				if (selected == options.length) selected = 0;
				action_timer.restart();
			}
		}
		if (Input.isKeyPressed(10)) { // if ENTER is pressed
			try {
				actions.get(selected).call();
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void render(Graphics2D g) {
		if (g.getFont() != font) g.setFont(font);
		g.drawImage(background, 0, 0, Game.WIDTH, Game.HEIGHT, null);
		for (int i = 0; i < options.length; i++) {
			if (i == selected) g.setColor(Color.red);
			else if (g.getColor() != Color.black) g.setColor(Color.black);
			g.drawString(options[i], textPos.getX(), textPos.getY()+(textSize*7/8)*(i-1));
		}
	}
	
	public void setAction(byte option_index, Callable<Void> action) {
		actions.put(option_index, action);
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public byte getSelected() {
		return selected;
	}

	public void setSelected(byte selected) {
		this.selected = selected;
	}

	public HashMap<Byte, Callable<Void>> getActions() {
		return actions;
	}

	public void setActions(HashMap<Byte, Callable<Void>> actions) {
		this.actions = actions;
	}

	public Timer getAction_timer() {
		return action_timer;
	}

	public void setAction_timer(Timer action_timer) {
		this.action_timer = action_timer;
	}

	public BufferedImage getBackground() {
		return background;
	}

	public void setBackground(BufferedImage background) {
		this.background = background;
	}

	public Vector2i getTextPos() {
		return textPos;
	}

	public void setTextPos(Vector2i textPos) {
		this.textPos = textPos;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
}
