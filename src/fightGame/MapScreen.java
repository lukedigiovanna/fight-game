package fightGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import fightGame.input.Keyboard;
import fightGame.main.Program;
import fightGame.utils.GraphicsUtils;
import fightGame.world.MapCodex;

public class MapScreen implements Screen {
	
	private int selectedIndex = 0;
	
	private String[] maps = {
			"MAP 1",
			"MOON",
			"SAND"
	};
	
	@Override
	public void set() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float dt) {
		if (Keyboard.isKeyPressed(KeyEvent.VK_ENTER)) {
			String mapName = maps[selectedIndex];
			switch (mapName) {
			case "MAP_1":
				GameEngine.map = MapCodex.MAP_1;
				break;
			case "MOON":
				GameEngine.map = MapCodex.MOON;
				break;
			case "SAND":
				GameEngine.map = MapCodex.SAND;
				break;
			}
			GameEngine.setScreen(GameEngine.GAME_SCREEN);
		}
		if (Keyboard.isKeyPressed(KeyEvent.VK_UP)) {
			selectedIndex--;
			if (selectedIndex < 0) selectedIndex = maps.length-1;
		}
		if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) 
			selectedIndex = (selectedIndex + 1)%maps.length;
	}

	@Override
	public void draw(BufferedImage screen) {
		Graphics2D g = screen.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Bauhaus 93", Font.PLAIN, 100));
		String s = Program.GAME_NAME;
		int y = 250;
		GraphicsUtils.setOutlineColor(Color.WHITE);
		GraphicsUtils.drawStringOutline(g, s, screen.getWidth()/2-g.getFontMetrics().stringWidth(s)/2, y, 3);
		y += 100;
		g.setFont(new Font("Bauhaus 93", Font.PLAIN, 70));
		for (int i = 0; i < maps.length; i++) {
			g.setColor(Color.GRAY);
			String buttonText = maps[i];
			if (i == selectedIndex) {
				g.setColor(Color.CYAN);
				buttonText = "-"+buttonText+"-";
			}
			GraphicsUtils.setOutlineColor(Color.BLACK);
			GraphicsUtils.drawStringOutline(g, buttonText, screen.getWidth()/2-g.getFontMetrics().stringWidth(buttonText)/2,y, 3);
			y += 80; 
		}
	}

	@Override
	public void checkController() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
