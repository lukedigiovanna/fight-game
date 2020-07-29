package fightGame.entities;

public class Impulse {
	private float age, duration, xv, yv;
	private Entity e;
	
	public Impulse(Entity e, float duration, float angle, float magnitude) {
		this.e = e;
		this.age = 0;
		this.duration = duration;
		this.xv = (float)Math.cos(angle) * magnitude;
		this.yv = (float)Math.sin(angle) * magnitude;
	}
	
	public void apply(float dt) {
		this.age += dt;
		e.velocity.x += xv * dt;
		e.velocity.y += yv * dt;
	}
	
	public boolean isDead() {
		return this.age >= duration;
	}
}
