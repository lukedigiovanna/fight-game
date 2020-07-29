package fightGame.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fightGame.GameEngine;

public class Window extends JFrame {
	
	private boolean wantsToClose = false;
	
	public Window(String title) {
		super(title);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("closing...");
				GameEngine.close();
				System.exit(0);
			}
		});
		
		double widthToHeightRatio = 4.0/3.0;
		int frameWidth = (int)(Program.SCREEN_SIZE.width * 0.5);
		int frameHeight = (int)(frameWidth/widthToHeightRatio);
		
		this.setSize(frameWidth, frameHeight);
		this.setLocation((Program.SCREEN_SIZE.width-frameWidth)/2,(Program.SCREEN_SIZE.height-frameHeight)/2);
		
		this.requestFocus();
	}
	
	public boolean wantsToClose() {
		return wantsToClose;
	}
	
	public void attachPanel(MainPanel panel) {
		this.setContentPane(panel);
		
		this.setVisible(true);
	}
}
