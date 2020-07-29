package fightGame.entities;

import java.awt.image.BufferedImage;

import fightGame.utils.ImageTools;

public enum Powerup {
	BUBBLE("bubble_powerup.png"),
	SHOTGUN("shotgun_powerup.png"),
	BOULDER("boulder_powerup.png"),
	SHRINK("shrink_powerup.png"),
	MACHINE("machine_powerup.png");
	
	BufferedImage iconTexture;
	
	Powerup(String iconName) {
		iconTexture =  ImageTools.getImage("res/textures/"+iconName);
	}
}	
