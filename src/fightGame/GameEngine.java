package fightGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import fightGame.entities.*;
import fightGame.input.Keyboard;
import fightGame.main.Main;
import fightGame.main.MainPanel;
import fightGame.main.Program;
import fightGame.sound.SoundCodex;
import fightGame.world.Map;
import fightGame.world.MapCodex;
import fightGame.world.World;

public class GameEngine {
	public static final int TARGET_TPS = 50;
	
	private static MainPanel panel;
	private static BufferedImage screen;
	
	public static Screen MAIN_SCREEN, GAME_SCREEN;
	private static Screen currentScreen;
	
	private static Process pythonProcess;
	
	public static void initialize(MainPanel inPanel) {
		panel = inPanel;
		screen = panel.getScreen();
		
		MAIN_SCREEN = new MainScreen();
		GAME_SCREEN = new GameScreen();
		
		setScreen(MAIN_SCREEN);
		
		Thread updateThread = new Thread(
				new Runnable() {
					public void run() {
						updateLoop();
					}
				}
			);
		updateThread.start();
		
		Thread renderThread = new Thread(
				new Runnable() {
					public void run() {
						renderLoop();
					}
				}
			);
		renderThread.start();
		
		try {
			ProcessBuilder pb = new ProcessBuilder("python","res/scripts/ps4controller.py");
			pythonProcess = pb.start();
		} catch (Exception e) {
			
		}
		Thread controllerThread = new Thread(
				new Runnable() {
					public void run() {
						checkController();
					}
				}
			);
		controllerThread.start();
	}
	
	public static void setScreen(Screen screen) {
		currentScreen = screen;
		currentScreen.set();
	}
	
	private static double rollingTPS = 0.0;
	
	public static void updateLoop() {
		long last = System.currentTimeMillis();
		double targetWait = 1.0/TARGET_TPS*1000.0;
		while (!Main.wantsToClose()) { // while the game is active
			long now = System.currentTimeMillis();
			float dt = (now - last)/1000.0f;
			last = now;
			
			double instantTPS = 1.0/dt;
			rollingTPS = instantTPS;
			
			long start = System.currentTimeMillis();
			
			currentScreen.update(dt);
			
			double elapsed = System.currentTimeMillis() - start;
			
			try {
				Thread.sleep((long)(targetWait - elapsed));
			} catch (Exception e) {
				
			}
		}
	}
	
	public static void checkController() {
		while (!Main.wantsToClose()) {
			try {
				currentScreen.checkController();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void renderLoop() {
		while (!Main.wantsToClose()) {
			if (screen != null) {
				currentScreen.draw(screen);
				panel.repaint();
			}
			
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				
			}
		}
	}
	
	public static void close() {
		pythonProcess.destroy();
		System.out.println("Ended python process");
	}
	
	public static double getTPS() {
		return rollingTPS;
	}
}
