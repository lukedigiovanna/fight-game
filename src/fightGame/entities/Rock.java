package fightGame.entities;

import java.awt.image.BufferedImage;

import fightGame.sound.SoundCodex;
import fightGame.utils.ImageTools;
import fightGame.world.Camera;

public class Rock extends Entity {

	private static BufferedImage texture = ImageTools.getImage("res/textures/rock.png");
	
	private Player owner;
	
	public Rock(Player owner, float x, float y, float direction, float speed, float size) {
		super(x,y,size,size);
		
		this.owner = owner;
		
		this.setVelocityByDirection(direction, speed);
		this.velocity.r = (float)Math.PI*3;
		
		this.collisionResponse = CollisionResponse.BOUNCE;
	}
	
	int bounces = 0;
	
	public void onCollision(Entity e) {
		if (e instanceof Player && e != owner) {
			float speed = this.velocity.getLength();
			float modifiedArea = (1 + this.hitbox.width) * (1 + this.hitbox.height); 
			e.hurt(speed * modifiedArea * 2);
			e.applyImpulse(0.5f, this.velocity.getAngle(), speed * 2);
			
			SoundCodex.play(SoundCodex.HIT);
			
			this.destroy();
		}
		
		if (e instanceof MapEntity) {
			bounces++;
		}
	}
	
	public void update(float dt) {
		if (this.age >= 15 || bounces > 4)
			this.destroy();
	}
	
	public void draw(Camera c) {
		c.drawImage(texture, this.position.x, this.position.y, this.hitbox.width, this.hitbox.height);
	}
}
