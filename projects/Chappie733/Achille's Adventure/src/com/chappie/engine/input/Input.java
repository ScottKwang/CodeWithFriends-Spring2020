package com.chappie.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.chappie.game.states.GameStateManager;

public class Input implements MouseListener, KeyListener, MouseWheelListener {
	
	private static boolean keys[] = new boolean[256];
	private static boolean m_buttons[] = new boolean[3];
	private static float scrollAmount;
	private static GameStateManager gsm;
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		gsm.onKeyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		gsm.onKeyReleased(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		m_buttons[e.getButton()-1] = true;
		gsm.onMouseClicked(e.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		m_buttons[e.getButton()-1] = false;
		gsm.onMouseReleased(e.getButton());
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		scrollAmount = e.getScrollAmount();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}
	
	public static boolean isKeyPressed(int keyCode) {
		return keys[keyCode];
	}
	
	public static boolean isMousePressed(int button) {
		return m_buttons[button-1];
	}
	
	public static float getScrollAmount() {
		return scrollAmount;
	}
	
	public static void setGameStateManager(GameStateManager gsm) {
		Input.gsm = gsm;
	}
	
}
