package fightGame.world;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;

import fightGame.entities.CollisionType;
import fightGame.utils.ImageTools;

public class Map {
	public String layout;
	private BufferedImage spriteSheet;
	public BufferedImage background;
	
	public Map(String folderPath) {	
		/* First initialize the layout */
		try {
			BufferedReader in = new BufferedReader(new FileReader(folderPath+"/layout.txt"));
			String line;
			this.layout = "";
			while ((line = in.readLine()) != null) {
				layout += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* Get our images*/
		this.spriteSheet = ImageTools.getImage(folderPath+"/spritesheet.png");
		this.background = ImageTools.getImage(folderPath+"/background.png");
	}
	
	private static final int SPRITE_SIZE = 16;
	public BufferedImage get(CollisionType type, int index) {
		int y = 0;
		switch (type) {
		case WALL:
			y = 0;
			break;
		case PLATFORM:
			y = 1;
			break;
		case BOX:
			y = 2;
		}
		int px = index * SPRITE_SIZE;
		int py = y * SPRITE_SIZE;
		
		return this.spriteSheet.getSubimage(px, py, SPRITE_SIZE, SPRITE_SIZE);
	}
	
}
