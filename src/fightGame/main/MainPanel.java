package fightGame.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import fightGame.input.Keyboard;

public class MainPanel extends JPanel {
	private BufferedImage screen;
	
	public MainPanel() {
		this.setFocusable(true);
		
		screen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		
	}
	
	public BufferedImage getScreen() {
		return this.screen;
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
	}
}
