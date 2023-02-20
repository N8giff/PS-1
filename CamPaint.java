import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * @title Cam Paint
 * @subtitle Assignment: PS-1
 * @Author Nathan Giffard
 * @class Dartmouth CS 10, Winter 2023
 * @date January 18th, 2023
 * @description Webcam based painting
 */
public class CamPaint extends Webcam {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press)
	private final Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece
	private BufferedImage recoloredImage;	 //recolored image
	private ArrayList<Point> largestRegion;  // array list of points from the largest region in finder
	private boolean paintOn;	//tracks if painting or not

	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		finder = new RegionFinder();
		clearPainting();
		processImage();
	}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or painting, 
	 * depending on display variable ('w', 'r', or 'p')
	 */
	@Override
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE
		if(displayMode =='w'){
			g.drawImage(image, 0, 0, null);
		}
		else if(displayMode == 'r'){
			g.drawImage(recoloredImage, 0, 0, null);
		}
		else if(displayMode == 'p'){
			g.drawImage(painting, 0, 0, null);
		}
	}

	/**
	 * Webcam method, here finding regions and updating the painting.
	 */
	@Override
	public void processImage() {
		// TODO: YOUR CODE HERE
		finder.setImage(image);

		if (paintOn) {
			finder.findRegions(targetColor);
			finder.recolorImage();
			recoloredImage = finder.getRecoloredImage();
			largestRegion = finder.largestRegion();

			//Repaint the pixels in the largest region with the paintColor
			for (Point p : largestRegion) {
				int currX = (int) p.getX();
				int currY = (int) p.getY();
				painting.setRGB(currX, currY, paintColor.getRGB());
			}
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 * Sets the targetColor to the color at the x,y of mouse
	 * Sets paintOn to true
	 */
	@Override
	public void handleMousePress(int x, int y) {
		// TODO: YOUR CODE HERE
		targetColor =  new Color(image.getRGB(x, y));
		paintOn = true;
		System.out.println("Paint on! Target color has been selected");
		processImage();
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 */
	@Override
	public void handleKeyPress(char k) {
		if (k == 'r') { // display: painting, recolored image, or webcam
			paintOn = false;
			displayMode = k;
		}
		else if(k == 'w'){
			paintOn = false;
			displayMode = k;
		}
		else if(k == 'p'){
			paintOn = true;
			System.out.println("Start painting!"); //display: painting and begin painting
			displayMode = k;
		}
		else if (k == 'c') { // clear
			clearPainting();
			recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}
