package fightGame.entities;

import java.awt.Color;
import java.awt.image.BufferedImage;

import fightGame.sound.SoundCodex;
import fightGame.utils.ImageTools;
import fightGame.utils.Rectangle;
import fightGame.world.Camera;

public class Player extends Entity {
	
	private static BufferedImage red = ImageTools.getImage("res/textures/red_guy.png"),
			green = ImageTools.getImage("res/textures/green_guy.png");
	
	private int jumps = 0;
	
	private String color;
	private final float defaultThrowRate = 2.0f;
	private float throwRate = defaultThrowRate;
	private float rockTimer = throwRate;
	private float rockSize = 0.2f;
	
	private float fallTimer = 0f;
	
	private final float defaultMovementSpeed = 2.0f;
	private float movementSpeed = defaultMovementSpeed;
	
	// POWER UP/POWER DOWNS
	private float bubbleTimer = 0.0f;
	private float shotgunTimer = 0.0f;
	private float boulderTimer = 0.0f;
	private float shrinkTimer = 0.0f;
	private boolean isShrunk = false;
	private final float shrinkScale = 0.5f;
	private float machineTimer = 0.0f;
	
	private Rectangle defaultSize;
	
	public Player(float x, float y, String color) {
		super(x,y,0.6f,0.6f/(14f/23f));
		
		this.defaultSize = new Rectangle(0,0);
		this.defaultSize.width = this.hitbox.width;
		this.defaultSize.height = this.hitbox.height;
		
		this.setMaxHealth(100, true);
		
		this.color = color;
	}
	
	public void jump() {
		if (!hasControl())
			return;
		
		jumps++;
		if (jumps < 2) {
			SoundCodex.play(SoundCodex.JUMP);
			this.velocity.y = -7f;
		}
	}
	
	private int direction = 1;
	
	private boolean movingLeft = false, movingRight = false;
	
	public void moveLeft() {
		if (!hasControl())
			return;
		
		movingLeft = true;
		
		this.velocity.x = -movementSpeed;
		direction = -1;
	}
	
	public void moveRight() {
		if (!hasControl())
			return;
		
		movingRight = true;
		
		this.velocity.x = movementSpeed;
		direction = 1;
	}
	
	public void stopMovingLeft() {
		if (movingLeft)
			this.velocity.x = 0;
	}
	
	public void stopMovingRight() {
		if (movingRight)
			this.velocity.x = 0;
	}
	
	public void fall() {
		if (!hasControl())
			return;
		
		if (this.isOnGround && fallTimer <= 0) {
			this.position.y += 0.15f;
			fallTimer = 0.01333f;
		}
	}
	
	public boolean hasControl() {
		return bubbleTimer <= 0;
	}
	
	private static final float ANGLE_INCLINATION = (float)Math.PI / 14.0f;
	public void throwRock() {
		if (rockTimer > 0 || !hasControl())
			return;
		
		float angleDirection = (direction == 1) ? -ANGLE_INCLINATION : (float)Math.PI+ANGLE_INCLINATION;
		float speed = 10.0f;
		throwRock(angleDirection, speed);
		
		if (this.shotgunTimer > 0) {
			for (int i = -1; i <= 1; i+=2) {
				float angle = angleDirection + i * (float)Math.PI/8;
				throwRock(angle, speed);
			}
		}
		
		SoundCodex.play(SoundCodex.THROW);
		
		rockTimer = throwRate;
	}
	
	private void throwRock(float direction, float speed) {
		Rock rock = new Rock(this, this.position.x, this.position.y, direction, speed, rockSize);
		this.world.add(rock);
	}
	
	public void usePowerup(Powerup powerup) {
		switch (powerup) {
		case BUBBLE:
			this.bubbleTimer = 5.0f;
			break;
		case SHOTGUN:
			this.shotgunTimer = 5.0f;
			break;
		case BOULDER:
			this.boulderTimer = 7.5f;
			break;
		case SHRINK:
			this.shrinkTimer = 7.5f;
			break;
		case MACHINE:
			this.machineTimer = 5.0f;
		}
	}
	
	public void update(float dt) {
		if (isOnGround)
			jumps = 0;
		
		if (movingLeft && movingRight) {
			this.velocity.x = 0;
			this.movingLeft = false;
			this.movingRight = false;
		}
		
		// move up if we are in a bubble
		if (bubbleTimer > 0) 
			this.velocity.y = -1.0f;
		
		if (boulderTimer > 0)
			this.rockSize = 0.6f;
		else
			this.rockSize = 0.2f;
		
		if (shrinkTimer > 0) {
			if (!isShrunk) {
				float newWidth = this.hitbox.width * shrinkScale, newHeight = this.hitbox.height * shrinkScale;
				this.position.x += (this.hitbox.width - newWidth)/2;
				this.position.y += (this.hitbox.height - newHeight)/2;
				this.hitbox.width = newWidth;
				this.hitbox.height = newHeight;
			}
			isShrunk = true;
			this.movementSpeed = this.defaultMovementSpeed * 2;
		} else {
			if (isShrunk) {
				this.position.x -= (this.defaultSize.width - this.hitbox.width)/2;
				this.position.y -= (this.defaultSize.height - this.hitbox.height);
				this.hitbox.width = this.defaultSize.width;
				this.hitbox.height = this.defaultSize.height;
			}
			isShrunk = false;
			this.movementSpeed = this.defaultMovementSpeed;
		}
		
		if (this.machineTimer > 0) {
			this.throwRate = 0.2f;
		} else {
			this.throwRate = this.defaultThrowRate;
		}
		
		rockTimer -= dt;
		bubbleTimer -= dt;
		shotgunTimer -= dt;
		boulderTimer -= dt;
		shrinkTimer -= dt;
		machineTimer -= dt;
		
		fallTimer -= dt;
	}
	
	public void draw(Camera c) {
		BufferedImage toDraw = ImageTools.IMAGE_NOT_FOUND;
		switch (this.color) {
		case "green":
			toDraw = green;
			break;
		case "red":
			toDraw = red;
			break;
		}
		if (this.direction == -1) {
			toDraw = ImageTools.flipVertical(toDraw);
		}
		c.drawImage(toDraw, this.position.x, this.position.y, this.hitbox.width, this.hitbox.height);
		if (bubbleTimer > 0.0f) {
			c.setColor(Color.WHITE);
			c.setLineWidth(0.025f);
			float radius = (this.hitbox.height + 0.1f)/2;
			c.drawOval(centerX() - radius, centerY() - radius, radius * 2, radius * 2);
		}
	}
	
}
