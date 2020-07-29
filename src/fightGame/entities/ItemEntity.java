package fightGame.entities;

import java.util.List;

import fightGame.sound.SoundCodex;
import fightGame.world.Camera;

public class ItemEntity extends Entity {

	private Powerup powerup;
	
	public ItemEntity(Powerup powerup, float x, float y) {
		super(x, y, 0.5f, 0.5f);
		
		this.powerup = powerup;
	}
	
	public void onCollision(Entity e) {
		if (e instanceof Player) {
			switch (powerup) {
			case BUBBLE: // PUTS ALL OTHER PLAYERS INTO A BUBBLE FOR 10 SECONDS
				List<Player> players = this.world.getPlayers();
				for (Player p : players) {
					if (p != e) {
						p.usePowerup(powerup);
					}
				}
				break;
			default:
				((Player)e).usePowerup(powerup);
				break;
			}
			
			SoundCodex.play(SoundCodex.POWERUP);
			
			this.destroy(); // destroy after being used
		}
	}
	
	private final float lifeTime = 10.0f;
	
	public void update(float dt) {
		if (this.age >= lifeTime)
			this.destroy(); // only lasts in the world for a few seoncds
	}
	
	public void draw(Camera c) {
		if (this.lifeTime - this.age < 3) {
			if (this.age % 0.6 < 0.3f)
				return; // dont draw.. this produces a blinking affect
		}
		
		c.drawImage(powerup.iconTexture, this.position.x, this.position.y, this.hitbox.width, this.hitbox.height);
	}

}
