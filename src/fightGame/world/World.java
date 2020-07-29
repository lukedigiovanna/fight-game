package fightGame.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import fightGame.entities.CollisionType;
import fightGame.entities.Entity;
import fightGame.entities.MapEntity;
import fightGame.entities.Player;
import fightGame.entities.PowerupSpawner;
import fightGame.input.Keyboard;
import fightGame.utils.ImageTools;
import fightGame.utils.Position;
import fightGame.utils.Rectangle;

public class World {
	private Map map;
	
	private Camera camera;
	
	private List<Entity> entities;
	/* These players are also stored in our entity list */
	private List<Player> players; 
	
	private List<Entity> toRemove;
	private List<Entity> toAdd;
	
	public World() {
		entities = new ArrayList<Entity>();
		players = new ArrayList<Player>();
		toRemove = new ArrayList<Entity>();
		toAdd = new ArrayList<Entity>();
		
		camera = new Camera(this, -6, -4, 12, 8);
	}
	
	private float width, height, leftX, topY;
	public void loadMap(Map map, Player[] players) {
		this.map = map;
		
		if (this.map == MapCodex.MOON) {
			Entity.GRAVITY = 5.0f;
		} else {
			Entity.GRAVITY = 9.0f;
		}
		
		for (int i = 0; i < map.layout.length(); i++) {
			width++;
			if (map.layout.charAt(i) == 'n') {
				height++;
				width = 0;
			}
		}
		
		float size = 0.5f;
	
		width *= size;
		height *= size;
			
		leftX = -width/2; 
		topY = -height/2;
		float x = leftX, y = topY;
		for (int i = 0; i < map.layout.length(); i++) {
			switch (map.layout.charAt(i)) {
			case '!': //platform
				this.add(new MapEntity(map.get(CollisionType.WALL, 0), CollisionType.WALL, x, y));
				break;
			case '@': 
				this.add(new MapEntity(map.get(CollisionType.WALL, 1), CollisionType.WALL, x, y));
				break;
			case '#': 
				this.add(new MapEntity(map.get(CollisionType.WALL, 2), CollisionType.WALL, x, y));
				break;
			case '$': 
				this.add(new MapEntity(map.get(CollisionType.PLATFORM, 0), CollisionType.PLATFORM, x, y));
				break;
			case '%': 
				this.add(new MapEntity(map.get(CollisionType.PLATFORM, 1), CollisionType.PLATFORM, x, y));
				break;
			case '^': 
				this.add(new MapEntity(map.get(CollisionType.PLATFORM, 2), CollisionType.PLATFORM, x, y));
				break;
			case '&': 
				this.add(new MapEntity(map.get(CollisionType.BOX, 0), CollisionType.BOX, x, y));
				break;
			case '*': 
				this.add(new MapEntity(map.get(CollisionType.BOX, 1), CollisionType.BOX, x, y));
				break;
			case '(': 
				this.add(new MapEntity(map.get(CollisionType.BOX, 2), CollisionType.BOX, x, y));
				break;
			case 'p':
				this.add(new PowerupSpawner(x,y));
				break;
			case '1':
				players[0].setPosition(x,y);
				break;
			case '2':
				players[1].setPosition(x, y);
				break;
			}
			
			x += size;
			if (map.layout.charAt(i) == 'n') {
				y += size;
				x = leftX;
			}
		}
	}
	
	public void add(Entity e) {
		this.toAdd.add(e);
	}
	
	public void remove(Entity e) {
		this.toRemove.add(e);
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
	
	public Position averagePositionOfPlayers() {
		Position center = new Position(0,0);
		for (Player player : players) {
			if (player.isDead())
				continue;
			center.x += player.getPosition().x;
			center.y += player.getPosition().y;
		}
		center.x /= players.size();
		center.y /= players.size();
		return center;
	}
	
	public Rectangle maxDistanceBetweenPlayers() {
		Rectangle max = new Rectangle(0,0);
		for (Player player : players) {
			if (player.isDead())
				continue;
			for (Player other : players) {
				if (other.isDead())
					continue;
				float widthDist = Math.abs(other.getPosition().x - player.getPosition().x);
				float heightDist = Math.abs(other.getPosition().y - player.getPosition().y);
				if (widthDist > max.width)
					max.width = widthDist;
				if (heightDist > max.height)
					max.height = heightDist;
			}
		}
		return max;
	}
	
	public void update(float dt) {
		for (Entity e : entities) {
			e.generalUpdate(dt);
			e.checkCollision(entities);
		}
		
		// now remove/add entities
		for (Entity e : toRemove) 
			this.entities.remove(e);
		this.toRemove.clear();
		
		for (Entity e : toAdd) {
			if (e instanceof Player)
				players.add((Player)e);
			entities.add(e);
			e.setWorld(this);
		}
		this.toAdd.clear();
		
		camera.update(dt);
	}
	
	public void draw() {
		if (map == null)
			return;
		
		camera.drawImageAtDistance(map.background, 0, 0, 5, width * 8, height * 8);
		
		Entity[] entitiesToDraw = new Entity[entities.size()];
		entities.toArray(entitiesToDraw);
		for (Entity e : entitiesToDraw) {
			camera.rotate(e.getPosition().rotation, e.getPosition().x + e.getHitbox().width/2, e.getPosition().y + e.getHitbox().height/2);
			e.draw(camera);
			camera.rotate(-e.getPosition().rotation, e.getPosition().x + e.getHitbox().width/2, e.getPosition().y + e.getHitbox().height/2);
		}
	}
	
	public Camera getCamera() {
		return this.camera;
	}
}
