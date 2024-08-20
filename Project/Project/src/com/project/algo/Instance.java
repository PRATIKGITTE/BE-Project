package com.project.algo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/** 
 * This is the class for each image instance.
 * @author Yuting Liu
 */
public class Instance {
	// Store the bufferedImage.
	private BufferedImage image;
	private String label;
	private int width, height;
	

	private int[][] gray_image;

	/** Constructs the Instance from a BufferedImage. */
	public Instance() {

	}
	
	/** Construct the Instance from a 3D array. */
	public Instance(int[][][] image, String label) {
		this.label = label;
		height = image[0].length;
		width = image[0][0].length;
		
		if (image.length == 4) {
			gray_image = image[3];
		} else {
			gray_image = new int[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					gray_image[i][j] = (image[0][i][j] + image[1][i][j] + image[2][i][j]) / 3;
				}
			}
		}
	}

	/** Gets the gray scale image. */
	public int[][] getGrayImage() {
		// Gray filter
		BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		byte[] dstBuff = ((DataBufferByte) grayImage.getRaster().getDataBuffer()).getData();

		for (int row = 0; row < height; ++row) {
			for (int col = 0; col < width; ++col) {
				gray_image[row][col] = dstBuff[col + row * width] & 0xFF;
			}
		}
		return gray_image;
	}

	/** Gets the image width. */
	public int getWidth() {
		return width;
	}

	/** Gets the image height. */
	public int getHeight() {
		return height;
	}

	/** Gets the image label. */
	public String getLabel() {
		return label;
	}
}
