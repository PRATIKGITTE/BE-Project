package com.project.algo;


import ij.process.ColorProcessor;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.imageio.ImageIO;

public class ColorExtraction {
	
	public static float[][] R;
	public static float[][] G;
	public static float[][] B;
	public static float[][] H;
	public static float[][] S;
	public static float[][] V;
	
	public static double[] colorFeatures=new double[18];
	
	public static double[] getColorFeature(){
		return colorFeatures;
		
	}
	
	public static ColorProcessor cp;
	
	public ColorExtraction(ColorProcessor cp1){
		
		cp=cp1;
	}
	
    public static void extract(){
    	
                
        RGB_HSV_Decompose(cp);
        
        double[] a=new double[3];
        
        a=meanStdSkew(R,cp.getHeight(),cp.getWidth());
        colorFeatures[0]=a[0]/255;
        colorFeatures[1]=a[1]/255;
        colorFeatures[2]=a[2];
        
        a=meanStdSkew(G,cp.getHeight(),cp.getWidth());
        colorFeatures[3]=a[0]/255;
        colorFeatures[4]=a[1]/255;
        colorFeatures[5]=a[2];

        a=meanStdSkew(B,cp.getHeight(),cp.getWidth());
        colorFeatures[6]=a[0]/255;
        colorFeatures[7]=a[1]/255;
        colorFeatures[8]=a[2];

        a=meanStdSkew(H,cp.getHeight(),cp.getWidth());
        colorFeatures[9]=a[0];
        colorFeatures[10]=a[1];
        colorFeatures[11]=a[2];

        a=meanStdSkew(S,cp.getHeight(),cp.getWidth());
        colorFeatures[12]=a[0];
        colorFeatures[13]=a[1];
        colorFeatures[14]=a[2];

        a=meanStdSkew(V,cp.getHeight(),cp.getWidth());
        colorFeatures[15]=a[0];
        colorFeatures[16]=a[1];
        colorFeatures[17]=a[2];
        
    }
    
    public static void RGB_HSV_Decompose(ColorProcessor cp){
    	
    	R=new float[cp.getHeight()][cp.getWidth()];
    	G=new float[cp.getHeight()][cp.getWidth()];
    	B=new float[cp.getHeight()][cp.getWidth()];
    	H=new float[cp.getHeight()][cp.getWidth()];
    	S=new float[cp.getHeight()][cp.getWidth()];
    	V=new float[cp.getHeight()][cp.getWidth()];
    	int rgb[] =new int[3];
    	float hsv[]=new float[3];
    	for (int i=0;i<cp.getHeight();i++){
    		for (int j=0;j<cp.getWidth();j++){
    			rgb=cp.getPixel(i, j, rgb);
    			R[i][j]=rgb[0];
    			G[i][j]=rgb[1];
    			B[i][j]=rgb[2];
    			hsv=java.awt.Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsv);
    			H[i][j]=hsv[0];
    			S[i][j]=hsv[1];
    			V[i][j]=hsv[2];
    		}
    	}
    }
    
    public static double[] meanStdSkew ( float[][] data, int h, int w )
    {
	    double mean = 0;
	    double[] out=new double[3];
	    
	    for (int i=0;i<h;i++){
        	for (int j=0;j<w;j++){
        		mean += data[i][j];
        	}
	    }
	    mean /= (h*w);
	    out[0]=mean;
	    double sum = 0;
	    for (int i=0;i<h;i++){
        	for (int j=0;j<w;j++){
        		final double v = data[i][j] - mean;
        		sum += v * v;
        	}
	    }
	    out[1]=Math.sqrt( sum / ( h*w - 1 ) );
	    
	    sum = 0;
	    for (int i=0;i<h;i++){
        	for (int j=0;j<w;j++){
        		final double v = (data[i][j] - mean)/out[1];
        		sum += v * v * v;        		
        	}
	    }
	    
	    out[2]=Math.pow(1+sum/(h*w-1),1./3);
	    return out;
    }

    
    public static int[][] rgb2bin(BufferedImage in){
    	int h=in.getHeight();
    	int w=in.getWidth();
    	int[][] out=new int[h][w];
    	long rgb;
    	int r,g,b;
    	ColorProcessor cp=new ColorProcessor(in);
    	for (int i=0;i<h;i++){
    		for (int j=0;j<w;j++){
    			rgb=cp.getPixel(i, j);
    	        r   = (int)(rgb % 0x1000000 / 0x10000);  
    	        g = (int)(rgb % 0x10000 / 0x100);  
    	        b = (int)(rgb % 0x100);  
    	        if (r==0 && g==0 && b==0){
    	        	out[i][j]=0;
    	        }
    	        else{
    	        	out[i][j]=1;
    	        }
    		}
    	}
    	
    	return out;
    	
    }
}