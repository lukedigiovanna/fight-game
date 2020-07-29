package fightGame.world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import fightGame.main.Program;
import fightGame.utils.*;

public class Camera {
	private Position position;
	private Rectangle dimension, minDimension;
	
	private BufferedImage display;
	private Graphics2D g;
	
	private World world;
	
	private float aspectRatio;
	
	private static final float MARGIN = 0.4f;
	
	public Camera(World world, float x, float y, float width, float height) {
		this.world = world;
		this.position = new Position(x,y);
		this.dimension = new Rectangle(width,height);
		this.minDimension = new Rectangle(width,height);
		aspectRatio = width/height;
		display = new BufferedImage(Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = display.createGraphics();
	}
	
	private final float strafeSpeed = 2.0f, resizeSpeed = 2.0f;
	public void update(float dt) {
		Position position = this.world.averagePositionOfPlayers();
		Position targetPosition = new Position(0,0);
		targetPosition.x = position.x - this.dimension.width/2;
		targetPosition.y = position.y - this.dimension.height/2;
		if (!MathUtils.isWithin(this.position.x, targetPosition.x, 0.05f))
			this.position.x += MathUtils.sign(targetPosition.x - this.position.x) * strafeSpeed * dt;
		if (!MathUtils.isWithin(this.position.y, targetPosition.y, 0.05f))
			this.position.y += MathUtils.sign(targetPosition.y - this.position.y) * strafeSpeed * dt;
		
		this.position = targetPosition;
		
		Rectangle targetDimension = new Rectangle(0, 0);
		Rectangle max = this.world.maxDistanceBetweenPlayers();
		// normalize to determine which one to adjust
		float width = max.width/this.dimension.width, height = max.height/this.dimension.height;
		if (width > height) {
			targetDimension.width = max.width + max.width * MARGIN * 2;
			targetDimension.height = this.dimension.width / aspectRatio;
		} else {
			targetDimension.height = max.height;
			targetDimension.width = targetDimension.height * aspectRatio;
			targetDimension.width += targetDimension.width * MARGIN * 2;
			targetDimension.height = targetDimension.width / aspectRatio;
		}
		
		if (targetDimension.width < this.minDimension.width) {
			targetDimension.width = this.minDimension.width;
			targetDimension.height = this.minDimension.height;
		}
		
		if (!MathUtils.isWithin(this.dimension.width, targetDimension.width, 0.05f))
			this.dimension.width += MathUtils.sign(targetDimension.width - this.dimension.width) * resizeSpeed * dt;
		if (!MathUtils.isWithin(this.dimension.height, targetDimension.height, 0.05f))
			this.dimension.height += MathUtils.sign(targetDimension.height - this.dimension.height) * resizeSpeed * dt;
	
		this.dimension = targetDimension;
		
		this.g = this.display.createGraphics();
	}
	
	public BufferedImage getDisplay() {
		return this.display;
	}
	
	public void clear(Color backgroundColor) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
	}
	
	public void clear(BufferedImage backgroundImage) {
		g.drawImage(backgroundImage, 0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT, null);
	}
	
	private float z = 1;
	public void setZ(float z) {
		this.z = z;
	}
	
	public void setColor(Color c) {
		g.setColor(c);
	}
	
	public void fillRect(float x, float y, float width, float height) {
		g.fillRect(toPX(x), toPY(y), toPW(width), toPH(height));
	}
	
	public void drawRect(float x, float y, float width, float height) {
		g.drawRect(toPX(x), toPY(y), toPW(width), toPH(height));
	}
	
	public void fillOval(float x, float y, float width, float height) {
		g.fillOval(toPX(x), toPY(y), toPW(width), toPH(height));
	}
	
	public void drawOval(float x, float y, float width, float height) {
		g.drawOval(toPX(x), toPY(y), toPW(width), toPH(height));
	}
	
	public void drawImageAtDistance(Image image, float cx, float cy, float z, float width, float height) {
		// convert to x, y + w, h
		float newWidth = width/z, newHeight = height/z;
		float offX = cx - centerX(), offY = cy - centerY();
		drawImage(image, cx + offX/z - newWidth/2, cy - newHeight/2 + offY/z, newWidth, newHeight);
	}
	
	public void drawImage(Image image, float x, float y, float width, float height) {
		g.drawImage(image, toPX(x), toPY(y), toPW(width), toPH(height), null);
	}
	
	public void setLineWidth(float width) {
		g.setStroke(new BasicStroke(toPW(width)));
	}
	
	public void drawLine(float x1, float y1, float x2, float y2) {
		g.drawLine(toPX(x1), toPY(y1), toPX(x2), toPY(y2));
	}
	
	public void rotate(float theta, float rx, float ry) {
		g.rotate(theta, toPX(rx), toPY(ry));
	}
	
	private int toPX(float x) {
		return (int)((x-position.x)/dimension.width*display.getWidth());
	}
	
	private int toPY(float y) {
		return (int)((y-position.y)/dimension.height*display.getHeight());
	}
	
	private int toPW(float width) {
		return (int)(width/dimension.width * display.getWidth()) + 1;
	}
	
	private int toPH(float height) {
		return (int)(height/dimension.height * display.getHeight()) + 1;
	}
	
	public float centerX() {
		return this.position.x + this.dimension.width/2;
	}
	
	public float centerY() {
		return this.position.y + this.dimension.height/2;
	}
}
