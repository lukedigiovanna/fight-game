package fightGame.entities;

import java.util.ArrayList;
import java.util.List;

import fightGame.utils.*;
import fightGame.world.Camera;
import fightGame.world.World;

public abstract class Entity {
	public static float GRAVITY = 9.0f;
	public static final float AIR_RESISTANCE = 1.00f; // applied in the opposite direction of motion
	
	protected double[] health; // first index represents the current health, second index represents the max health
	
	protected PositionHistory positionHistory;
	
	protected Position position;
	protected Rectangle hitbox;
	protected Vector2 velocity;
	
	protected World world;
	
	protected CollisionType collisionType;
	protected CollisionResponse collisionResponse;
	
	private List<Impulse> activeImpulses;
	
	protected boolean hasGravity = true;
	protected boolean isOnGround = false;
	
	protected float age; // represents the age of this entity in seconds
	
	public Entity(float x, float y, float width, float height) {
		this.position = new Position(x, y);
		this.hitbox = new Rectangle(width, height);
		
		this.positionHistory = new PositionHistory(this);
		
		this.velocity = new Vector2(0, 0);
		
		collisionType = CollisionType.NONE;
		collisionResponse = CollisionResponse.STOP;
		
		this.activeImpulses = new ArrayList<Impulse>();
		
		this.health = new double[2];
		this.health[1] = 1; //default
		this.health[0] = this.health[1]; // set to max health to start
	}
	
	public void generalUpdate(float dt) {
		update(dt);
		
		float curSpeed = this.velocity.getLength();
		if (curSpeed > 0) {
			float direction = this.velocity.getAngle();
			float newSpeed = curSpeed - AIR_RESISTANCE * dt;
			this.setVelocityByDirection(direction, newSpeed);
		}
		if (this.hasGravity)
			this.velocity.y += GRAVITY * dt;
		// apply air resistance
		
		for (int i = 0; i < this.activeImpulses.size(); i++) {
			Impulse imp = this.activeImpulses.get(i);
			imp.apply(dt);
			if (imp.isDead()) {
				this.activeImpulses.remove(i);
				i--;
			}
		}

		
		this.position.x += this.velocity.x * dt;
		this.position.y += this.velocity.y * dt;
		this.position.rotation += this.velocity.r * dt;
		
		if (this.position.y > 30)
			this.hurt(dt * 100);
	
		
		if (this.isDead()) {
			this.destroy();
		}
		
		this.age += dt;
		
		this.positionHistory.update();
	}
	
	/**
	 * Checks for collisions with this entity and an inputted list of entities
	 * If a collision is found, then it will be handled appropriately
	 * @param entities
	 */
	public void checkCollision(List<Entity> entities) {
		this.isOnGround = false;
		for (Entity e : entities) {
			if (this == e)
				continue; // don't check a collision with itself
			
			//now check for simple bounding-box collision
			if (this.position.x + this.hitbox.width > e.position.x && this.position.x < e.position.x + e.hitbox.width) { // validates the x-oriented collision
				if (this.position.y + this.hitbox.height > e.position.y && this.position.y < e.position.y + e.hitbox.height) { // validates the y-oriented collision
					this.onCollision(e);
					
					boolean adjustedY = false;
					if (e.collisionType == CollisionType.PLATFORM || e.collisionType == CollisionType.BOX) {
						// if this object is moving downwards or is stationary then we stop it.. otherwise we don't care
						if (this.velocity.y >= 0 && this.positionHistory.hasBeenNorthOf(e.position.y - this.hitbox.height + 0.05f)) {
							this.position.y = e.position.y - this.hitbox.height;
							// stop movement
							switch (this.collisionResponse) {
							case STOP:
								this.velocity.y = 0;
								break;
							case BOUNCE:
								this.velocity.y *= -1;
							}
							this.isOnGround = true;
							adjustedY = true;
						}
						
					} 
					if (!adjustedY && (e.collisionType == CollisionType.WALL || e.collisionType == CollisionType.BOX)) {
						if (this.velocity.x > 0) { // moving to the right
							// correct positioning
							this.position.x = e.position.x - this.hitbox.width;
						} else {
							this.position.x = e.position.x + e.hitbox.width;
						}
						
						switch (this.collisionResponse) {
						case STOP:
							this.velocity.x = 0;
							break;
						case BOUNCE:
							this.velocity.x *= -1;
						} 
					}
				}
			}
		}
	}
	
	public void setMaxHealth(double value, boolean setHealth) { 
		this.health[1] = value;
		if (setHealth) {
			this.health[0] = this.health[1];
		}
	}
	
	public void hurt(double amount) {
		this.health[0] -= amount;
		this.health[0] = MathUtils.max(this.health[0], 0);
	}
	
	public void applyImpulse(float duration, float angle, float magnitude) {
		this.activeImpulses.add(new Impulse(this, duration, angle, magnitude));
	}
	
	public void destroy() {
		this.world.remove(this);
	}
	
	public void clearVelocity() {
		this.velocity.x = 0;
	}
	
	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public float centerX() {
		return this.position.x + this.hitbox.width / 2;
	}
	
	public float centerY() {
		return this.position.y + this.hitbox.height / 2;
	}
	
	public Rectangle getHitbox() {
		return this.hitbox;
	}
	
	public Vector2 getVelocity() {
		return this.velocity;
	}
	
	public void setVelocityByDirection(float direction, float speed) {
		this.velocity.x = (float)Math.cos(direction) * speed;
		this.velocity.y = (float)Math.sin(direction) * speed;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public int getHealth() {
		return (int)health[0];
	}
	
	public int getMaxHealth() {
		return (int)health[1];
	}
	
	public float getAge() {
		return this.age;
	}
	
	public boolean isDead() {
		return (int)health[0] <= 0;
	}
	
	// optional method to define in a subclass.. called when this entity detects a collision with a different entity
	public void onCollision(Entity e) {
		
	}
	
	public abstract void update(float dt);
	public abstract void draw(Camera c);
}	
