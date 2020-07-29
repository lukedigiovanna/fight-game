package fightGame.entities;

import java.awt.Color;

import fightGame.utils.MathUtils;
import fightGame.world.Camera;

public class PowerupSpawner extends Entity {
	public PowerupSpawner(float x, float y) {
		super(x,y,0.5f,0.5f);
		
		this.hasGravity = false;
	}
	
	private float averagePeriod = 60.0f; // about every 30 seconds a new powerup spawns
	private float timeSinceLastDrop = 0.0f;
	
	private final float tryPeriod = 5.0f;
	private float tryTimer = tryPeriod; 
	
	public void update(float dt) {
		timeSinceLastDrop += dt;
		
		float chance = (timeSinceLastDrop - (averagePeriod/2))/averagePeriod;
		
		tryTimer -= dt;
		if (tryTimer < 0) {
			tryTimer = tryPeriod;
			// try 
			if (Math.random() < chance) {
				// then we drop a powerup
				Powerup powerup = Powerup.values()[MathUtils.random(Powerup.values().length)];
				this.world.add(new ItemEntity(powerup,this.position.x,this.position.y));
				timeSinceLastDrop = 0.0f;
			}
		}
	}
	
	public void draw(Camera c) {
//		c.setColor(Color.CYAN);
//		c.fillRect(this.position.x, this.position.y, this.hitbox.width, this.hitbox.height);
	}
}
