package com.project.algo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Thresholding 
{
		public static BufferedImage Threshold(BufferedImage img,int requiredThresholdValue) 
		{
		int height = img.getHeight();
		int width = img.getWidth();
		BufferedImage finalThresholdImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB) ;
		
		int red = 0;
		int green = 0;
		int blue = 0;
		
		for (int x = 0; x < width; x++) {
			try {

				for (int y = 0; y < height; y++) {
					int color = img.getRGB(x, y);

					red = ImageOperations.getRed(color);
					green = ImageOperations.getGreen(color);
					blue = ImageOperations.getBlue(color);

					if((red+green+green)/3 < (int) (requiredThresholdValue)) {
							finalThresholdImage.setRGB(x,y,ImageOperations.mixColor(0, 0,0));
						}
						else {
							finalThresholdImage.setRGB(x,y,ImageOperations.mixColor(255, 255,255));
						}
					
				}
			} catch (Exception e) {
				 e.getMessage();
			}
		}
		
		return finalThresholdImage;
		
	}
	
}
