package fightGame.utils;

import java.io.Serializable;

public class Vector2 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/*
	 * simulates a mathematical vector with some operations:
	 * mutators:
	 * add another vector
	 * subtract another vector
	 * multiply by scalar
	 * divide by scalar
	 * normalize
	 * set x
	 * set y
	 * accessors:
	 * get x
	 * get y
	 * get length
	 * get angle
	 */
	
	public float x, y;
	
	//this value does not play a role in the actual rotation of the vector..
	//it merely holds information
	public float r; //not necessarily used but can be useful in some circumstances -- denotes rotation
	
	public Vector2(float x, float y) {
		this(x,y,0);
	}
	
	public Vector2(float x, float y, float r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	//this + other
	public void add(Vector2 other) {
		this.x += other.x;
		this.y += other.y;
		this.r += other.r;
	}
	
	//this - other
	public void subtract(Vector2 other) {
		this.x -= other.x;
		this.y -= other.y;
		this.r -= other.r;
	}
	
	public void multiply(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
	}
	
	public void divide(float scalar) {
		if (scalar == 0)
			return;
		else {
			this.x/=scalar;
			this.y/=scalar;
		}
	}
	
	public void setMagnitude(float value) {
		//maintains the angle, changes the length
		float angle = this.getAngle();
		value = Math.abs(value);
		this.x = (float)Math.cos(angle)*value;
		this.y = (float)Math.sin(angle)*value;
	}
	
	public void setAngle(float angle) {
		float magnitude = this.getLength();
		this.x = magnitude*(float)Math.cos(angle);
		this.y = magnitude*(float)Math.sin(angle); 
	}
	
	public float getDistanceSquared(Vector2 other) {
		float dx = other.x - this.x, dy = other.y - this.y;
		return dx * dx + dy * dy;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setR(float r) {
		this.r = r;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getR() {
		return r;
	}
	
	//returns the radian value of the angle
	public float getAngle() {
		return (float)MathUtils.getAngle(this.x, this.y);
	}
	
	public float getLength() {
		return (float)Math.sqrt(x*x+y*y);
	}
	
	public void normalize() {
		float length = getLength();
		if (length == 0)
			return;
		else
			this.divide(length);
	}
	
	/**
	 * Gets the dot product of this vector and another inputted vector
	 * @param b
	 * @return
	 */
	public float dot(Vector2 b) {
		return x*b.x+y*b.y;
	}
	
	public Vector2 getNormalVector() {
		return new Vector2(-this.y,this.x);
	}
	
	public void round(float decimalPoint) {
		this.x = MathUtils.round(this.x, decimalPoint);
		this.y = MathUtils.round(this.y, decimalPoint);
		this.r = MathUtils.round(this.r, decimalPoint);
	}
	
	public void zero() {
		this.x = 0;
		this.y = 0;
		this.r = 0;
	}
	
	/**
	 * Returns a new vector with the same information
	 * @return
	 */
	public Vector2 copy() {
		return new Vector2(this.x,this.y,this.r);
	}
	
	public boolean equals(Vector2 other) {
		return Math.abs(this.x - other.x) < 0.01 && Math.abs(this.y - other.y) < 0.01;   
	}
	
	public String toString() {
		return "<"+MathUtils.round(this.x, 0.001f)+", "+MathUtils.round(this.y, 0.001f)+", "+MathUtils.round(this.r,0.001f)+">";
	}
}