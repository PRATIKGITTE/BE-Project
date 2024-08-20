package com.project.algo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.IOException;

import javax.imageio.ImageIO;

public class GrayDemo{
 
	public static BufferedImage original, grayscale;
	


    public static void writeImage(String output) throws IOException {
        File file = new File(output+".jpg");
        ImageIO.write(grayscale, "jpg", file);
       
    }
 
    // Return histogram of grayscale image
    public static int[] imageHistogram(BufferedImage input) {
 
        int[] histogram = new int[256];
 
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
 
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
 
        return histogram;
 
    }
 
    // The luminance method
    
    public static BufferedImage toGray(File file) throws IOException {
       
    	BufferedImage original=null;
    			
    	original=ImageIO.read(file);
    	
        int alpha, red, green, blue;
        int newPixel;
 
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);
 
                // Write pixels into image
                result.setRGB(i, j, newPixel);
 
            }
        }
 
        return result;
 
    }
 
    // Convert R, G, B, Alpha to standard 8 bit
    public static int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
 
}