package fightGame.utils;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageTools {
	public static final BufferedImage IMAGE_NOT_FOUND = getImageNotFound(),
									  BLANK = getBlank();
	
	public static BufferedImage toBufferedImage(Image image) {
		if (image == null || image.getWidth(null) < 0 || image.getHeight(null) < 0)
			return IMAGE_NOT_FOUND;
		
		BufferedImage img = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		img.getGraphics().drawImage(image, 0, 0, null);
		return img;
	}
	
	public static final int ROTATE_0 = 0, ROTATE_90 = 1, ROTATE_180 = 2, ROTATE_270 = 3;
	
	public static BufferedImage rotate(BufferedImage image, int rotation) {
		if (rotation == ROTATE_0)
			return image;
		int width, height;
		switch (rotation) {
		case ROTATE_90: case ROTATE_270:
			width = image.getHeight();
			height = image.getWidth();
			break;
		case ROTATE_180:
			width = image.getWidth();
			height = image.getHeight();
			break;
		default:
			return image;
		}
		BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) 
			for (int y = 0; y < height; y++) {
				int rgb = 0;
				switch (rotation) {
				case ROTATE_90:
					rgb = image.getRGB(height-y-1, x);
					break;
				case ROTATE_180:
					rgb = image.getRGB(width-x-1, height-y-1);
					break;
				case ROTATE_270:
					rgb = image.getRGB(y, width-x-1);
					break;
				}
				newImg.setRGB(x, y, rgb);
			}
		return newImg;
	}
	
	/**
	 * Removes a percentage of the image
	 * @param image the image to splice
	 * @param leftPercent how much to remove from the left side of the image
	 * @param rightPercent how much to remove from the right side of the image
	 * @return
	 */
	public static BufferedImage spliceHorizontal(BufferedImage image, float leftPercent, float rightPercent) {
		leftPercent = MathUtils.clip(0, 1, leftPercent);
		rightPercent = MathUtils.clip(0, 1, rightPercent);
		int width = (int)(image.getWidth() * (1-leftPercent-rightPercent));
		int x = (int)(image.getWidth() * leftPercent);
		if (width == 0)
			return BLANK;
		else
			return image.getSubimage(x, 0, width, image.getHeight());
	}
	
	public static BufferedImage rescale(BufferedImage image, int newWidth, int newHeight) {
		BufferedImage rescaled = new BufferedImage(newWidth, newHeight, image.getType());
		float widthFactor = (float)image.getWidth()/newWidth, heightFactor = (float)image.getHeight()/newHeight; 
		System.out.println(widthFactor);
		for (int x = 0; x < newWidth; x++) 
			for (int y = 0; y < newHeight; y++) 
				rescaled.setRGB(x, y, image.getRGB((int)(x*widthFactor), (int)(y*heightFactor)));
		return rescaled;
	}
	
	public static BufferedImage rescale(Image image, int newWidth, int newHeight) {
		return rescale(toBufferedImage(image),newWidth,newHeight);
	}
	
	public static BufferedImage grayscale(BufferedImage image) {
		return apply(image,new Modifiable() {
			public Color modify(Color c) {
				int avg = (c.getRed()+c.getGreen()+c.getBlue())/3;
				return new Color(avg,avg,avg,c.getAlpha());
			}
		});
	}
	
	public static BufferedImage colorscale(BufferedImage image, Color color) {
		return apply(image,new Modifiable() {
			public Color modify(Color c) {
				int avg = (c.getRed()+c.getGreen()+c.getBlue())/3;
				int red = (int)(color.getRed()/255.0*avg),
					green = (int)(color.getGreen()/255.0*avg),
					blue = (int)(color.getBlue()/255.0*avg),
					alpha = c.getAlpha();
				return new Color(red,green,blue,alpha);
			}
		});
	}
	
	public static BufferedImage setTransparency(BufferedImage image, int alpha) {
		return apply(image,new Modifiable() {
			public Color modify(Color c) {
				if (c.getAlpha() == 0)
					return c;
				return new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha);
			}
		});
	}
	
	public static BufferedImage invert(BufferedImage image) {
		return apply(image,new Modifiable() {
			public Color modify(Color c) {
				return new Color(255-c.getRed(),255-c.getGreen(),255-c.getBlue(),c.getAlpha());
			}
		});
	}
	
	public static final int TOP_EDGE = 0, RIGHT_EDGE = 1, BOTTOM_EDGE = 2, LEFT_EDGE = 3;
	/**
	 * Zippers the images edges together
	 * @param image
	 * @param other
	 * @param edge
	 * @param depth
	 * @return
	 */
	public static BufferedImage zipper(BufferedImage image, BufferedImage other, int edge, int depth) {
		BufferedImage newImg = copy(image);
		switch (edge) {
		case TOP_EDGE:
			for (int x = 0; x < image.getWidth(); x+=2)
				newImg.setRGB(x, 0, other.getRGB(x, other.getHeight()-1));
			break;
		case BOTTOM_EDGE:
			for (int x = 1; x < image.getWidth(); x+=2)
				newImg.setRGB(x, image.getHeight()-1, other.getRGB(x, 0));
			break;
		case LEFT_EDGE:
			for (int y = 0; y < image.getHeight(); y+=2)
				newImg.setRGB(0, y, other.getRGB(other.getWidth()-1, y));
			break;
		case RIGHT_EDGE:
			for (int y = 1; y < image.getHeight(); y+=2)
				newImg.setRGB(image.getWidth()-1, y, other.getRGB(0, y));
		}
		return newImg;
	}
	
	/**
	 * Darkens the image according to a value
	 * @param image
	 * @param darkness a value from [0.0,1.0] where 0 is black and 1 is the same
	 * @return
	 */
	public static BufferedImage darken(BufferedImage image, float darkness) {
		if (darkness < 0 || darkness > 1) 
			throw new RuntimeException("darkness should be between 0 and 1");	
		darkness = MathUtils.clip(0, 1, darkness);
		BufferedImage newImg = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
		int[][] rgbs = getRGB(image);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				newImg.setRGB(x, y, (new Color((int)(c.getRed()*darkness),(int)(c.getGreen()*darkness),(int)(c.getBlue()*darkness),c.getAlpha())).getRGB());
			}
		}
		return newImg;
	}
	
	public static BufferedImage flipHorizontal(BufferedImage image) {
		int[][] rgbs = getRGB(image);
		int[][] nrgb = new int[rgbs.length][rgbs[0].length];
		for (int x = 0; x < rgbs.length; x++) 
			for (int y = 0; y < rgbs[x].length; y++)
				nrgb[x][y] = rgbs[x][rgbs[x].length-y-1];
		return get(nrgb);
	}
	
	public static List<BufferedImage> flipHorizontal(List<BufferedImage> images) {
		List<BufferedImage> newList = new ArrayList<BufferedImage>();
		for (BufferedImage bi : images)
			newList.add(flipHorizontal(bi));
		return newList;
	}
	
	public static BufferedImage flipVertical(BufferedImage image) {
		int[][] rgbs = getRGB(image);
		int[][] nrgb = new int[rgbs.length][rgbs[0].length];
		for (int x = 0; x < rgbs.length; x++) 
			for (int y = 0; y < rgbs[x].length; y++)
				nrgb[x][y] = rgbs[rgbs.length-x-1][y];
		return get(nrgb);
	}
	
	public static List<BufferedImage> flipVerticle(List<BufferedImage> images) {
		List<BufferedImage> newList = new ArrayList<BufferedImage>();
		for (BufferedImage bi : images)
			newList.add(flipVertical(bi));
		return newList;
	}
	
	/**
	 * Fades the corners of the image based on the intensity inputted
	 * @param image
	 * @param intensity how hard the fade is -- 0 is no fade, 1 is very faded
	 * @return
	 */
	public static BufferedImage fade(BufferedImage image, float intensity) {
		BufferedImage newImg = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
		int[][] rgbs = getRGB(image);
		double maxDistance = Math.sqrt(image.getWidth()/2.0 * image.getWidth()/2.0 + image.getHeight()/2.0 * image.getHeight()/2.0);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				double dx = x - image.getWidth() / 2.0, dy = y - image.getHeight() / 2.0;
				double distance = Math.sqrt(dx * dx + dy * dy);
				double percent = distance/maxDistance;
				float fade = 1f-(float)(percent * intensity);
				Color c = new Color(rgbs[x][y]);
				newImg.setRGB(x, y, (new Color((int)(c.getRed() * fade), (int)(c.getGreen() * fade), (int)(c.getBlue() * fade), c.getAlpha())).getRGB());
			}
		}
		return newImg;
	}
	
	private static BufferedImage apply(BufferedImage image, Modifiable mod) {
		if (image == null)
			return null;
		BufferedImage newImg = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
		int[][] rgbs = getRGB(image);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				newImg.setRGB(x, y, mod.modify(c).getRGB());
			}
		}
		return newImg;
	}
	
	private interface Modifiable {
		public abstract Color modify(Color color);
	}
	
	private static int[][] getRGB(BufferedImage img) {
		int[][] rgbs = new int[img.getWidth()][img.getHeight()];
		for (int x = 0; x < rgbs.length; x++) 
			for (int y = 0; y < rgbs[x].length; y++) 
				rgbs[x][y] = img.getRGB(x, y);
		return rgbs;
	}
	
	private static Color getColor(int rgb) {
		return new Color(rgb,true);
	}
	
	private static BufferedImage get(int[][] rgbs) {
		BufferedImage newImg = new BufferedImage(rgbs.length,rgbs[0].length,BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < rgbs.length; x++) 
			for (int y = 0; y < rgbs[x].length; y++) 
				newImg.setRGB(x, y, rgbs[x][y]);
		return newImg;
	}
	
	private static BufferedImage copy(BufferedImage image) {
		int[][] rgbs = getRGB(image);
		return get(rgbs);
	}
	
	public static BufferedImage getImage(String filePath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filePath));
			if (img == null)
				return IMAGE_NOT_FOUND;
			return img;
		} catch (IOException e) {
			System.err.println("no image "+filePath);
			return IMAGE_NOT_FOUND;
		}
	}
	
	/**
	 * Path is path to the folder
	 * @param folderPath
	 * @param prefix
	 * @return
	 */
	public static List<BufferedImage> getImages(String folderPath, String prefix) {	
		if (folderPath.indexOf("assets/") < 0)
			folderPath = "assets/images/"+folderPath;
		List<BufferedImage> frames = new ArrayList<BufferedImage>();
		int i = 0;
		boolean cont = true;
		do {
			String path = folderPath+"/"+prefix+i+".png";
			try {
				File file = new File(path);
				if (file.exists()) {
					BufferedImage img = ImageTools.getImage(path);
					if (img != null && !img.equals(ImageTools.IMAGE_NOT_FOUND)) {
						//img = convertTo8Bit(img);
						frames.add(img);
						i++;
					}
				} else {
					cont = false;
				}
			} catch (Exception e) {
				cont = false; //something occured so stop adding frames.
			}
			
		} while (cont);
		return frames;
	}
	
	//for one time call to make static const of these
	
	private static BufferedImage getImageNotFound() {
		BufferedImage notFound = new BufferedImage(2,2,BufferedImage.TYPE_INT_ARGB);
		notFound.setRGB(0, 0, Color.black.getRGB());
		notFound.setRGB(1, 1, Color.black.getRGB());
		notFound.setRGB(0, 1, Color.magenta.getRGB());
		notFound.setRGB(1, 0, Color.magenta.getRGB());
		return notFound;
	}
	
	private static BufferedImage getBlank() {
		BufferedImage blank = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		return blank;
	}
}
