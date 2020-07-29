package fightGame.main;

import fightGame.GameEngine;
import fightGame.input.Keyboard;

public class Main {
	private static Window window;
	private static MainPanel panel;
	
	public static boolean wantsToClose() {
		return window.wantsToClose();
	}
	
	public static void main(String[] args) {
		window = new Window(Program.GAME_NAME);
		panel = new MainPanel();
		Keyboard.initialize(panel);
		window.attachPanel(panel);
		
		GameEngine.initialize(panel);
	}
}
