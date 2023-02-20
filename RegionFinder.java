import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @title Region Finder
 * @subtitle Assignment: PS-1
 * @Author Nathan Giffard
 * @class Dartmouth CS 10, Winter 2023
 * @date January 18th, 2023
 * @description Region growing algorithm: finds and holds regions in an image.
 *  Each region is a list of contiguous points with colors similar to a target color.
 */
public class RegionFinder {
	private static final int maxColorDiff = 50;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage visited;							// marks pixel as visited
	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored
	private ArrayList<Point> currRegion = new ArrayList<>(); 		// keeps track of pixels in the current region being created
	private ArrayList<ArrayList<Point>> regions = new ArrayList<>();	// keeps track of the regions found

	//constructor
	public RegionFinder() {

		this.image = null;
	}

	//constructor
	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}

	//set the image
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	//get the image
	public BufferedImage getImage() {
		return this.image;
	}

	//get the recolored image
	public BufferedImage getRecoloredImage() {
		return this.recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		regions = new ArrayList<ArrayList<Point>>();
		visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB); //black copy of img that tracks visited pixels
		boolean matchingColor; //tracks if the pixel color matches the target

		// Loop over all pixels of the image to find all the color matching pixels,
		//  add those pixels to the current region and search neighbors to grow the region,
		//  mark all pixels as 'visited' and add found regions to the regions ArrayList<ArrayList<Point>>
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color c = new Color(image.getRGB(x, y)); // color of current pixel

				//If the current pixel hasn't been marked as visited and there's a color match...
				// create a new point object using the current x,y,
				// add the point to the current region ArrayList<Point>,
				// mark the point as 'visited',
				// find all pixel neighbors (N,S,E,W) that also match the target color,
				// and add those points to the currRegion until there is a region to add to the regions array
				if (visited.getRGB(x, y) == 0 && colorMatch(c, targetColor)){
					ArrayList<Point> toVisit = new ArrayList<>(); //keeps track of pixel neighbors to visit
					Point currPoint = new Point(x, y); //current x,y as point object

					currRegion.add(currPoint); //add currPoint to currRegion
					visited.setRGB(x, y, 1); // mark currPoint as visited
					toVisit.add(currPoint); //create initial list of neighbors to visit

					//Test neighbors (8 adjacency) and add color matching pixels to currRegion
					testNeighbors(toVisit,targetColor);

					//When the current region is finalized in currRegion ArrayList<Point>,
					// add currRegion to the regions ArrayList<ArrayList<Point>>,
					// and reset the currRegion array to build the next region
					regions.add(currRegion);
					currRegion = new ArrayList<Point>();
				}
			}
		}
	}

	//While the toVisit ArrayList<Point> is not empty...
	// get the first point in toVisit ArrayList<Point> and its x,y coordinates,
	// test neighbors for color matching,
	// add any color matching pixels to the currRegion and toVisit,
	// mark them as visited and remove the first point from toVisit
	private void testNeighbors(ArrayList<Point> toVisit, Color targetColor){
		Color currColor; // keeps track of the RGB color of the pixel being tested

		while (toVisit.size() != 0) {
			Point p = toVisit.get(0);
			int currX = (int)p.getX();
			int currY = (int)p.getY();

			//If the N pixel is within the image bounds and has not been visited...
			// get the color of the pixel
			if(imageBounds(currX,currY+1) &&(visited.getRGB(currX,currY+1)==0)) {
				currColor = new Color(image.getRGB(currX,currY+1)); //N pixel color

				//If there's a color match...
				// add the pixel to the currRegion,
				// add teh pixel to toVisit,
				// mark the pixel as visited
				if (colorMatch(currColor,targetColor)){
					currRegion.add(new Point(currX,currY+1));
					toVisit.add(new Point(currX,currY+1));
					visited.setRGB(currX,currY+1,1);
				}
			}

			//If the NE pixel is within the image bounds and has not been visited...
			// get the color of the pixel
			if(imageBounds(currX+1,currY+1) &&(visited.getRGB(currX+1,currY+1)==0)) {
				currColor = new Color(image.getRGB(currX+1,currY+1)); //NE pixel color

				//If there's a color match...
				// add the pixel to the currRegion,
				// add teh pixel to toVisit,
				// mark the pixel as visited
				if (colorMatch(currColor,targetColor)){
					currRegion.add(new Point(currX+1,currY+1));
					toVisit.add(new Point(currX+1,currY+1));
					visited.setRGB(currX+1,currY+1,1);
				}
			}

			//If the NW pixel is within the image bounds and has not been visited...
			// get the color of the pixel
			if(imageBounds(currX-1,currY+1) &&(visited.getRGB(currX-1,currY+1)==0)) {
				currColor = new Color(image.getRGB(currX-1,currY+1)); //NW pixel color

				//If there's a color match...
				// add the pixel to the currRegion,
				// add teh pixel to toVisit,
				// mark the pixel as visited
				if (colorMatch(currColor,targetColor)){
					currRegion.add(new Point(currX-1,currY+1));
					toVisit.add(new Point(currX-1,currY+1));
					visited.setRGB(currX-1,currY+1,1);
				}
			}

			//If the E pixel is within the image bounds and has not been visited...
			// get the color of the pixel
			if(imageBounds(currX-1,currY)&&(visited.getRGB(currX-1,currY)==0)) {
				currColor = new Color(image.getRGB(currX-1,currY)); // E pixel color

				//If there's a color match...
				// add the pixel to the currRegion,
				// add teh pixel to toVisit,
				// mark the pixel as visited
				if (colorMatch(currColor,targetColor)){
					currRegion.add(new Point(currX-1,currY));
					toVisit.add(new Point(currX-1,currY));
					visited.setRGB(currX-1,currY,1);
				}
			}

			//If the W pixel is within the image bounds and has not been visited...
			// get the color of the pixel
			if (imageBounds(currX+1,currY)&&(visited.getRGB(currX+1,currY)==0)) {
				currColor = new Color(image.getRGB(currX+1,currY)); // W pixel color

				//If there's a color match...
				// add the pixel to the currRegion,
				// add the pixel to toVisit,
				// mark the pixel as visited
				if (colorMatch(currColor,targetColor)){
					currRegion.add(new Point(currX+1,currY));
					toVisit.add(new Point(currX+1,currY));
					visited.setRGB(currX+1,currY,1);
				}
			}

			//If the SE pixel below is within the image bounds and has not been visited...
			// get the color of the pixel
			if(imageBounds(currX+1,currY-1)&&(visited.getRGB(currX+1,currY-1)==0)) {
				currColor = new Color(image.getRGB(currX+1,currY-1)); // SE pixel color

				//If there's a color match...
				// add the pixel to the currRegion,
				// add the pixel to toVisit,
				// mark the pixel as visited
				if (colorMatch(currColor,targetColor)){
					currRegion.add(new Point(currX+1,currY-1));
					toVisit.add(new Point(currX+1,currY-1));
					visited.setRGB(currX+1,currY-1,1);
				}
			}

			//If the SW pixel below is within the image bounds and has not been visited...
			// get the color of the pixel
			if(imageBounds(currX-1,currY-1)&&(visited.getRGB(currX-1,currY-1)==0)) {
				currColor = new Color(image.getRGB(currX-1,currY-1)); // SW pixel color

				//If there's a color match...
				// add the pixel to the currRegion,
				// add the pixel to toVisit,
				// mark the pixel as visited
				if (colorMatch(currColor,targetColor)){
					currRegion.add(new Point(currX-1,currY-1));
					toVisit.add(new Point(currX-1,currY-1));
					visited.setRGB(currX-1,currY-1,1);
				}
			}
			//remove current pixel from toVisit
			toVisit.remove(p);
		}
	}

	//Tests if a pixel is within the bounds of the image
	private boolean imageBounds(int x, int y) {
		if(x < image.getWidth() && (x>0) && (y<image.getHeight()) && (y>0)) {
			return true; //return true if in bounds
		}
		return false; //return false if not in bounds
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		//get the sum of the abs difference between each color channel of c1 and c2
		int d = Math.abs(c1.getRed() - c2.getRed())
				+ Math.abs(c1.getGreen() - c2.getGreen())
				+ Math.abs(c1.getBlue() - c2.getBlue());
		//if the difference is less than or equal to the max difference (20), return true
		return d <= maxColorDiff;
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		int largestSize = 0; //tracks size of the largest region
		int indexLargest = 0; //tracks index of the largest region

		//Loop over each region in the regions ArrayList<ArrayList<Point>>
		for(ArrayList<Point> currRegion : regions){
			//if the size of the current region is larger than the largest...
			// set largestSize equal to the size of the current region,
			// and set indexLargest equal to the index of currRegion in regions
			if(currRegion.size() > largestSize){
				largestSize = currRegion.size();
				indexLargest = regions.indexOf(currRegion);
			}
		}
		//return the ArrayList<Point> of the largest region
		return regions.get(indexLargest);
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// For each region in the regions ArrayList<ArrayList<Point>>...
		for(ArrayList<Point> currRegion : regions){
			//if the size of the region is >= the min region size...
			// create a new random color represented by a random value between 0 and 16777216
			if(currRegion.size() >= minRegion) {
				int currColor = (int) (Math.random() * 16777216); //cast as int
				//for each pixel in the currRegion...
				// get the x,y coordinates
				// and set the corresponding pixel in the recolored image to the random color
				for (Point p : currRegion) {
					int currX = (int) p.getX();
					int currY = (int) p.getY();
					recoloredImage.setRGB(currX, currY, currColor);
				}
			}
		}
	}
}
