package fightGame;

import java.awt.image.BufferedImage;

public interface Screen {
	void set(); // called when we set to this screen
	void update(float dt); // called on an update loop
	void draw(BufferedImage screen); // called to draw
	void checkController() throws Exception;
}
