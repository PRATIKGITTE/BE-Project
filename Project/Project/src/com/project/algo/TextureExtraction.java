package com.project.algo;

public class TextureExtraction {

	public static int[][] binImage;
	public static int height;
	public static int width;
	
	public static double[] coocFeatures=new double[48];
	
	
	public static double[] getCoocFeatures(){
		return coocFeatures;
	}
	
	public TextureExtraction(int[][] in, int h, int w){
		binImage=in;
		height=h;
		width=w;
	}
	
	public static void extract(){
		int[][] c;
		//V(1:48)=0;
		int sum=0;
		for (int i=1;i<=3;i++){
		    for (int j=1;j<=4;j++){
		        int p=16*(i-1)+4*(j-1);
		        c=coocMatirx(i,j);
		        coocFeatures[p]=c[0][0];
		        coocFeatures[p+1]=c[0][1];
		        coocFeatures[p+2]=c[1][0];
		        coocFeatures[p+3]=c[1][1];
		        sum+=c[0][0];
		        sum+=c[0][1];
		        sum+=c[1][0];
		        sum+=c[1][1];
		        //V(p+1:p+4)=comatrix(I,i,j);
		    }
		}
		for (int i=0;i<48;i++)
		    coocFeatures[i]/=sum;
		
	}
	
	public static int[][] coocMatirx(int dir, int ang){
		int[] D=new int[2];
		int[][] C=new int[2][2];
		C[0][0]=0;
		C[0][1]=0;
		C[1][0]=0;
		C[1][1]=0;
		if (ang==1){
		    D[0]=0;
		    D[1]=dir;
		}
		else if (ang==2){
			D[0]=-dir;
		    D[1]=dir;
		}
		else if (ang==3){
			D[0]=-dir;
		    D[1]=0;
		}
		else if (ang==4){
			D[0]=-dir;
		    D[1]=-dir;
		}
		int a,b,C1,C2;
		for (int i=1;i<height;i++){
		    for (int j=0;j<width;j++){
		        C1=binImage[i][j];
		        a=i+D[0];
		        b=j+D[1];
		        if (a<0 || b<0 || a>=height || b>=width)
		            continue;
		        C2=binImage[a][b];
		        C[C1][C2]=C[C1][C2]+1;
		    }
		}
		return C;
	}
	
}
