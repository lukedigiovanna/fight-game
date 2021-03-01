package fightGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import fightGame.input.Keyboard;
import fightGame.main.Program;
import fightGame.utils.GraphicsUtils;
import fightGame.utils.ImageTools;

public class MainScreen implements Screen {
	
	public void set() {
		
	}
	
	public void checkController() throws Exception {
		
	}
	
	public void update(float dt) {
		if (Keyboard.isKeyPressed(KeyEvent.VK_ENTER))
			actions[selectedButton].run();
		
		if (Keyboard.isKeyPressed(KeyEvent.VK_UP))
			selectedButton = (selectedButton + 1)%buttons.length;
		if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN))
			selectedButton = (selectedButton - 1);
		if (selectedButton < 0)
			selectedButton = buttons.length-1;
	}
	
	private BufferedImage background = ImageTools.getImage("res/textures/title.png");
	
	private String[] buttons = new String[] {
		"START",
		"QUIT"
	};
	private Runnable[] actions = new Runnable[] {
			new Runnable() {
				public void run() {
					GameEngine.setScreen(GameEngine.MAP_SCREEN);
				}
			},
			new Runnable() {
				public void run() {
					GameEngine.close();
					System.exit(0);
				}
			}
	};
	private int selectedButton = 0;
	
	public void draw(BufferedImage screen) {
		Graphics2D g = screen.createGraphics();
		
		g.drawImage(background, 0, 0, screen.getWidth(), screen.getHeight(), null);
		
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Bauhaus 93", Font.PLAIN, 100));
		String s = Program.GAME_NAME;
		int y = 250;
		GraphicsUtils.setOutlineColor(Color.WHITE);
		GraphicsUtils.drawStringOutline(g, s, screen.getWidth()/2-g.getFontMetrics().stringWidth(s)/2, y, 3);
		y += 100;
		g.setFont(new Font("Bauhaus 93", Font.PLAIN, 70));
		for (int i = 0; i < buttons.length; i++) {
			g.setColor(Color.GRAY);
			String buttonText = buttons[i];
			if (i == selectedButton) {
				g.setColor(Color.CYAN);
				buttonText = "-"+buttonText+"-";
			}
			GraphicsUtils.setOutlineColor(Color.BLACK);
			GraphicsUtils.drawStringOutline(g, buttonText, screen.getWidth()/2-g.getFontMetrics().stringWidth(buttonText)/2,y, 3);
			y += 80; 
		}
	}
}
