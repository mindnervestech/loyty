package com.mnt.core.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageCropper {
	static Rectangle clip;

	public static BufferedImage cropImage(BufferedImage img, int cropWidth,
			int cropHeight, int cropStartX, int cropStartY) throws Exception {
		BufferedImage clipped = null;
		Dimension size = new Dimension(cropWidth, cropHeight);

		createClip(img, size, cropStartX, cropStartY);

		try {
			int w = clip.width;
			int h = clip.height;

			System.out.println("Crop Width " + w);
			System.out.println("Crop Height " + h);
			System.out.println("Crop Location " + "(" + clip.x + "," + clip.y
					+ ")");

			clipped = img.getSubimage(clip.x, clip.y, w, h);

			System.out.println("Image Cropped. New Image Dimension: "
					+ clipped.getWidth() + "w X " + clipped.getHeight() + "h");
		} catch (RasterFormatException rfe) {
			System.out.println("Raster format error: " + rfe.getMessage());
			return null;
		}
		return clipped;
	}

	/**
	 * This method crops an original image to the crop parameters provided.
	 *
	 * If the crop rectangle lies outside the rectangle (even if partially),
	 * adjusts the rectangle to be included within the image area.
	 *
	 * @param img = Original Image To Be Cropped
	 * @param size = Crop area rectangle
	 * @param clipX = Starting X-position of crop area rectangle
	 * @param clipY = Strating Y-position of crop area rectangle
	 * @throws Exception
	 */
	private static void createClip(BufferedImage img, Dimension size,
			int clipX, int clipY) throws Exception {
		/**
		 * Some times clip area might lie outside the original image,
		 * fully or partially. In such cases, this program will adjust
		 * the crop area to fit within the original image.
		 *
		 * isClipAreaAdjusted flas is usded to denote if there was any
		 * adjustment made.
		 */
		boolean isClipAreaAdjusted = false;

		/**Checking for negative X Co-ordinate**/
		if (clipX < 0) {
			clipX = 0;
			isClipAreaAdjusted = true;
		}
		/**Checking for negative Y Co-ordinate**/
		if (clipY < 0) {
			clipY = 0;
			isClipAreaAdjusted = true;
		}

		/**Checking if the clip area lies outside the rectangle**/
		if ((size.width + clipX) <= img.getWidth()
				&& (size.height + clipY) <= img.getHeight()) {

			/**
			 * Setting up a clip rectangle when clip area
			 * lies within the image.
			 */

			clip = new Rectangle(size);
			clip.x = clipX;
			clip.y = clipY;
		} else {

			/**
			 * Checking if the width of the clip area lies outside the image.
			 * If so, making the image width boundary as the clip width.
			 */
			if ((size.width + clipX) > img.getWidth())
				size.width = img.getWidth() - clipX;

			/**
			 * Checking if the height of the clip area lies outside the image.
			 * If so, making the image height boundary as the clip height.
			 */
			if ((size.height + clipY) > img.getHeight())
				size.height = img.getHeight() - clipY;

			/**Setting up the clip are based on our clip area size adjustment**/
			clip = new Rectangle(size);
			clip.x = clipX;
			clip.y = clipY;

			isClipAreaAdjusted = true;

		}
		if (isClipAreaAdjusted)
			System.out.println("Crop Area Lied Outside The Image."
					+ " Adjusted The Clip Rectangle\n");
	}

	/**
	 * This method reads an image from the file
	 *
	 * @param fileLocation -- >
	 *            eg. "C:/testImage.jpg"
	 * @return BufferedImage of the file read
	 */
	public static BufferedImage readImage(File file) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int type){
		BufferedImage resizedImage = new BufferedImage(100, 100, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 100, 100, null);
		g.dispose();
	 
		return resizedImage;
	}

	public static BufferedImage avg(BufferedImage original) {
		 
        int alpha, red, green, blue;
        int newPixel;
 
        BufferedImage avg_gray = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[] avgLUT = new int[766];
        for(int i=0; i<avgLUT.length; i++) avgLUT[i] = (int) (i / 3);
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                newPixel = red + green + blue;
                newPixel = avgLUT[newPixel];
                // Return back to original format
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
 
                // Write pixels into image
                avg_gray.setRGB(i, j, newPixel);
 
            }
        }
 
        return avg_gray;
 
    }
	
	 // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
	/**
	 * This method writes a buffered image to a file
	 *
	 * @param img -- > BufferedImage
	 * @param fileLocation --> e.g. "C:/testImage.jpg"
	 * @param extension --> e.g. "jpg","gif","png"
	 */
	public static void writeImage(BufferedImage img, File file,
			String extension) {
		try {
			BufferedImage bi = img;
			
			ImageIO.write(bi, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
