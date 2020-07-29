package fightGame.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class Keyboard implements KeyListener {
	private static Keyboard instance;
	
	public static void initialize(JPanel panel) {
		instance = new Keyboard();
		
		panel.addKeyListener(instance);
	}
	
	public static boolean isKeyDown(int keycode) {
		return instance._isKeyDown(keycode);
	}
	
	public static boolean isKeyUp(int keycode) {
		return instance._isKeyUp(keycode);
	}
	
	public static boolean isKeyPressed(int keycode) {
		return instance._isKeyPressed(keycode);
	}
	
	private static final int KEY_COUNT = 200;
	private boolean[] keyDown, keyUp, keyPressed;
	
	private Keyboard() {
		keyDown = new boolean[KEY_COUNT];
		keyUp = new boolean[KEY_COUNT];
		for (int i = 0; i < KEY_COUNT; i++)
			keyUp[i] = true;
		keyPressed = new boolean[KEY_COUNT];
	}
	
	/**
	 * Returns true if the key is currently pressed 
	 */
	public boolean _isKeyDown(int keycode) {
		if (validateKeyCode(keycode))
			return keyDown[keycode];
		else
			return false;
	}
	
	public boolean _isKeyUp(int keycode) {
		if (validateKeyCode(keycode))
			return keyUp[keycode];
		else
			return false;
	}
	
	/**
	 * Returns true once if the key is pressed
	 */
	public boolean _isKeyPressed(int keycode) {
		if (!validateKeyCode(keycode))
			return false;
		
		if (keyDown[keycode]) {
			keyDown[keycode] = false;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!validateKeyCode(e.getKeyCode()))
			return;
		
		keyDown[e.getKeyCode()] = true;
		keyUp[e.getKeyCode()] = false;
		
		keyPressed[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!validateKeyCode(e.getKeyCode()))
			return;
		
		keyDown[e.getKeyCode()] = false;
		keyUp[e.getKeyCode()] = true;
		
		keyPressed[e.getKeyCode()] = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// do nothing
	}
	
	private boolean validateKeyCode(int code) {
		return code <= KEY_COUNT;
	}
}
