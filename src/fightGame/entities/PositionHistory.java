package fightGame.entities;

import java.util.ArrayList;
import java.util.List;

public class PositionHistory {
	private static final float HISTORY_TIME = 0.1f;
	
	private List<Float> x, y, time;
	private Entity entity;
	
	public PositionHistory(Entity e) {
		this.entity = e;
		this.x = new ArrayList<Float>();
		this.y = new ArrayList<Float>();
		this.time = new ArrayList<Float>();
	}
	
	/**
	 * Checks if this history has any points below or equal to the given value
	 * @param y
	 * @return
	 */
	public boolean hasBeenNorthOf(float y) {
		for (float cy : this.y) 
			if (cy <= y)
				return true;
		return false;
	}
	
	/**
	 * Checks if this history has any points above or equal to the given value
	 * @param y
	 * @return
	 */
	public boolean hasBeenSouthOf(float y) {
		for (float cy : this.y)
			if (cy >= y)
				return true;
		return false;
	}
	
	public boolean hasBeenEastOf(float x) {
		for (float cx : this.x)
			if (cx >= x)
				return true;
		return false;
	}
	
	public boolean hasBeenWestOf(float x) {
		for (float cx : this.x)
			if (cx <= x)
				return true;
		return false;
	}
	
	public void update() {
		x.add(entity.getPosition().x);
		y.add(entity.getPosition().y);
		time.add(entity.getAge());
		
		for (int i = 0; i < x.size(); i++) {
			if (entity.getAge() - time.get(i) > HISTORY_TIME) {
				time.remove(i);
				x.remove(i);
				y.remove(i);
				i--;
			}
		}
	}
}
