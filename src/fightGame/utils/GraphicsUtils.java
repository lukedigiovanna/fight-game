package fightGame.utils;

import java.awt.Color;
import java.awt.Graphics2D;

public class GraphicsUtils {
	
	private static Color outlineColor = Color.black;
	public static void setOutlineColor(Color color) {
		outlineColor = color;
	}
	
	public static void drawStringOutline(Graphics2D g, String s, int x, int y, int width) {
		Color fillColor = g.getColor();
		g.setColor(outlineColor);
		if (width < 1)
			width = 1;
		for (int sx = x - width; sx <= x + width; sx += width)
			for (int sy = y - width; sy <= y + width; sy += width) 
				g.drawString(s, sx, sy);
		
		g.setColor(fillColor);
		g.drawString(s, x, y);
	}
}
