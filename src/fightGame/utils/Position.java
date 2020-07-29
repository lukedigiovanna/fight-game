package fightGame.utils;

public class Position {
	public float x, y, rotation;
	
	public Position(float x, float y) {
		this(x, y, 0);
	}
	
	public Position(float x, float y, float rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getRotation() {
		return this.rotation;
	}
}
