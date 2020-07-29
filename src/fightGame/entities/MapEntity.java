package fightGame.entities;

import java.awt.image.BufferedImage;

import fightGame.utils.ImageTools;
import fightGame.world.Camera;

/**
 * Represents a wall or platform
 * @author luked
 *
 */
public class MapEntity extends Entity {
	
	private BufferedImage texture;
	
	public MapEntity(BufferedImage texture, CollisionType cType, float x, float y) {
		super(x, y, 0.5f, 0.5f);
		
		this.collisionType = cType;
		this.texture = texture;
		
		this.hasGravity = false;
	}
	
	public void update(float dt) {
		
	}
	
	public void draw(Camera c) {
		c.drawImage(this.texture, this.position.x, this.position.y, this.hitbox.width, this.hitbox.height);
	}
}
