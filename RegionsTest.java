import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

/**
 * @title Regions Test
 * @subtitle Assignment: PS-1
 * @Author Nathan Giffard
 * @class Dartmouth CS 10, Winter 2023
 * @date January 18th, 2023
 * @description Testing code for RegionFinder class
 */
public class RegionsTest extends DrawingGUI {
	private BufferedImage image;

	/**
	 * Test your RegionFinder by passing an image filename and a color to find.
	 */
	public RegionsTest(String name, RegionFinder finder, Color targetColor) {
		super(name, finder.getImage().getWidth(), finder.getImage().getHeight());

		// Do the region finding and recolor the image.
		finder.findRegions(targetColor);
		finder.recolorImage();
		//update the image
		this.image = finder.getRecoloredImage();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	public static void main(String[] args) {
		String filename1 = "pictures/smiley.png"; //image file
		Color color1 = new Color(0, 0, 0); //target color

		String filename2 = "pictures/baker.jpg"; //image file
		Color color2 = new Color(130, 100, 100); //target color

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//new RegionsTest("smiley", new RegionFinder(loadImage(filename1)), color1);
				new RegionsTest("baker", new RegionFinder(loadImage(filename2)), color2);
			}
		});
	}
}
