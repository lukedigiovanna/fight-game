package fightGame;

import java.awt.event.KeyEvent;

public enum ControlSet {
	PLAYER_1(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_F),
	PLAYER_2(KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_K, KeyEvent.VK_O);
	
	int jump, moveLeft, moveRight, throwRock, fall;
	ControlSet(int jump, int moveLeft, int moveRight, int fall, int throwRock) {
		this.jump = jump;
		this.moveLeft = moveLeft;
		this.moveRight = moveRight;
		this.fall = fall;
		this.throwRock = throwRock;
	}
}
