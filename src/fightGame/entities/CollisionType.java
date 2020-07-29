package fightGame.entities;

/**
 * Indicates collision types for rigid-body type collisions
 * this means collisions which affect the movement of OTHER objects
 * i.e. if an entity is set to have "NONE" collision type, this does not mean it 
 * cannot have collisions with other bodies. This just means that other bodies
 * will not collide with it
 * e.g. a platform would have "PLATFORM" collision and a player would have "NONE" collision. 
 */
public enum CollisionType {
	PLATFORM, // collision only on the top 
	WALL, // collision on left and right
	BOX, // collision on all sides
	NONE // no collision
}
