package fightGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import fightGame.entities.Player;
import fightGame.input.Keyboard;
import fightGame.main.Program;
import fightGame.sound.SoundCodex;
import fightGame.utils.GraphicsUtils;
import fightGame.utils.ImageTools;
import fightGame.world.Map;
import fightGame.world.MapCodex;
import fightGame.world.World;

public class GameScreen implements Screen {
	private World world;
	
	private Player[] players;
	private ControlSet[] controls;
	private int[] scores;
	
	private boolean gameOver = false;
	
	public GameScreen() {
		
	}
	
	public void set() {
		players = new Player[2];
		controls = new ControlSet[] {ControlSet.PLAYER_1, ControlSet.PLAYER_2};
		scores = new int[] { 0, 0 };
		
		reset();
		
		SoundCodex.loop(SoundCodex.SONG);
	}
	
	private int gameWinner = -1;
	
	private void hardReset() {
		this.gameOver = false;
		this.gameWinner = -1;
		for (int i = 0; i < scores.length; i++)
			scores[i] = 0;
		
		this.roundNumber = 1;
		this.betweenRoundTimer = 0;
		this.roundOver = false;
		this.winner = -1;
		
		this.reset();
	}
	
	private void reset() {
		for (int i = 0; i < scores.length; i++)
			if (scores[i] >= WINNING_SCORE) {
				gameOver = true;
				gameWinner = i;
				return;
			}
		
		// resets the map
		world = new World();
		
		players[0] = new Player(0,0,"green");
		players[1] = new Player(0,0,"red");
		world.add(players[0]);
		world.add(players[1]);
		
		world.loadMap(GameEngine.map, players);
	}
	
	private boolean isXDown = false;
	public void checkController() throws Exception {
		System.out.println("fuck nigga");
			// read three lines
			BufferedReader in = new BufferedReader(new FileReader(new File("res/scripts/controller_out.txt")));
			String x = in.readLine();
			String o = in.readLine();
			String axis = in.readLine();
			String yAxis = in.readLine();
			try {
				float axisVal = Float.parseFloat(axis);
				float yAxisVal = Float.parseFloat(yAxis);
				boolean xVal = Boolean.parseBoolean(x);
				boolean oVal = Boolean.parseBoolean(o);
				
				if (xVal && !isXDown)
					players[0].jump();
				isXDown = xVal;
				
				if (axisVal < -0.5)
					players[0].moveLeft();
				if (axisVal > 0.5)
					players[0].moveRight();
				
				if (yAxisVal > 0.5)
					players[0].fall();
				
				if (oVal)
					players[0].throwRock();
			} catch (Exception e) {
				
			}
			System.out.println(x);
			in.close();
		
	}
	
	private final float betweenRoundCooldown = 5f;
	private float betweenRoundTimer = 0;
	private boolean roundOver = false;
	private int winner = -1;
	private int roundNumber = 1;
	
	@Override
	public void update(float dt) {
		if (!gameOver) {
			for (int i = 0; i < players.length; i++) {
				Player p = players[i];
				ControlSet c = controls[i];
				
				if (Keyboard.isKeyPressed(c.jump))
					p.jump();
				if (Keyboard.isKeyDown(c.moveLeft))
					p.moveLeft();
				else
					p.stopMovingLeft();
				if (Keyboard.isKeyDown(c.moveRight))
					p.moveRight();
				else
					p.stopMovingRight();
				if (Keyboard.isKeyDown(c.fall))
					p.fall();
				if (Keyboard.isKeyDown(c.throwRock))
					p.throwRock();
			}
		}
		
		world.update(dt);
		
		// check for winning.. check if all players except 1 are dead
		int deadCount = 0;
		for (Player p : players)
			if (p.isDead())
				deadCount++;
		if (!roundOver && deadCount >= players.length-1) {
			if (deadCount == players.length-1) { // if we have a winner
				// find the live player
				for (int i = 0; i < players.length; i++) {
					if (!players[i].isDead()) {
						scores[i]++;
						this.winner = i;
					}
				}
			} else {} // ended in draw
			roundOver = true;
		}
		
		if (roundOver) {
			this.betweenRoundTimer += dt;
			if (this.betweenRoundTimer >= this.betweenRoundCooldown) {
				this.betweenRoundTimer = 0f;
				this.roundOver = false;
				this.winner = -1;
				this.roundNumber++;
				reset();
			}
		}
		
		if (gameOver) {
			if (Keyboard.isKeyPressed(KeyEvent.VK_ENTER)) {
				this.hardReset();
				GameEngine.setScreen(GameEngine.MAIN_SCREEN);
			}
		}
	}

	private final int WINNING_SCORE = 2;
	@Override
	public void draw(BufferedImage screen) {
		Graphics2D g = screen.createGraphics();
		
		if (world != null) {
			world.draw();
			g.drawImage(world.getCamera().getDisplay(), 0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT, null);
		}
		g.setColor(Color.WHITE);
		
		g.setFont(new Font("Courier", Font.BOLD, 30));
		
		String s = "TPS: "+(int)GameEngine.getTPS();
		g.drawString(s, Program.DISPLAY_WIDTH - g.getFontMetrics().stringWidth(s) - 10, 35);
		
		int sectionSize = Program.DISPLAY_WIDTH / players.length;
		int y = Program.DISPLAY_HEIGHT - 75;
		for (int i = 0; i < players.length; i++) {
			y = Program.DISPLAY_HEIGHT - 75;
			int lx = i * sectionSize;
			int cx = lx + sectionSize / 2;
			g.setFont(new Font("Courier",Font.BOLD,24));
			g.setColor(Color.WHITE);
			String name = "Player "+(i + 1);
			GraphicsUtils.setOutlineColor(Color.BLACK);
			GraphicsUtils.drawStringOutline(g, name, cx - g.getFontMetrics().stringWidth(name)/2, y, 1);
			g.setColor(Color.RED);
			String health = "HP: "+players[i].getHealth()+"/"+players[i].getMaxHealth();
			y += 30;
			GraphicsUtils.drawStringOutline(g, health, cx-g.getFontMetrics().stringWidth(health)/2, y, 1);
			y+=5;
			// draw circles for the score
			int circleSize = 25, gap = 5;
			int leftX = cx - ((WINNING_SCORE - 1) * gap + WINNING_SCORE * circleSize)/2;
			for (int j = 0; j < WINNING_SCORE; j++) {
				int x = leftX + j * circleSize + j * gap;
				g.setColor(Color.BLACK);
				g.fillOval(x, y, circleSize, circleSize);
				g.setColor(Color.GRAY);
				if (j < scores[i])
					g.setColor(Color.YELLOW);
				g.fillOval(x+3, y+3, circleSize-6, circleSize-6);
			}
		}
		
		if (roundOver && !gameOver) {
			g.setFont(new Font("Bauhaus 93",Font.BOLD,100));
			GraphicsUtils.setOutlineColor(Color.WHITE);
			g.setColor(Color.GRAY);
			String text = "Round "+roundNumber+" Over";
			y = 300;
			g.setStroke(new BasicStroke(3));
			GraphicsUtils.drawStringOutline(g, text, screen.getWidth()/2-g.getFontMetrics().stringWidth(text)/2, y, 3);
			y += 100;
			g.setFont(new Font("Bauhaus 93",Font.BOLD, 60));
			g.setColor(Color.BLUE);
			text = "Player "+(1 + winner)+" Won!";
			GraphicsUtils.drawStringOutline(g, text, screen.getWidth()/2-g.getFontMetrics().stringWidth(text)/2, y, 3);
		}
		
		if (gameOver) {
			g.setFont(new Font("Bauhaus 93", Font.BOLD, 100));
			GraphicsUtils.setOutlineColor(Color.WHITE);
			g.setColor(Color.BLACK);
			y = 300;
			String text = "GAME OVER!";
			GraphicsUtils.drawStringOutline(g, text, screen.getWidth()/2-g.getFontMetrics().stringWidth(text)/2, y, 3);
			g.setColor(Color.BLUE);
			text = "Player "+(1 + gameWinner)+" Won!";
			y+=100;
			g.setFont(new Font("Bauhaus 93",Font.BOLD, 60));
			GraphicsUtils.drawStringOutline(g, text, screen.getWidth()/2-g.getFontMetrics().stringWidth(text)/2, y, 3);
			g.setColor(Color.BLACK);
			y+=60;
			text = "Press -enter- to continue";
			g.setFont(new Font("Bauhaus 93", Font.ITALIC, 50));
			GraphicsUtils.drawStringOutline(g, text, screen.getWidth()/2-g.getFontMetrics().stringWidth(text)/2, y, 2);
		}
	}
}
