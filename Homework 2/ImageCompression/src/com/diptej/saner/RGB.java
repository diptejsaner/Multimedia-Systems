package com.diptej.saner;

public class RGB {
	int R;
	int G;
	int B;
	
	public RGB() {
		R = 0;
		G = 0;
		B = 0;
	}
	
	public int getColor(int c) {
		if(c == 0)
			return this.R;
		else if(c == 1)
			return this.G;
		else if(c == 2)
			return this.B;
		else
			return -999999;
	}
	
	public static void initializeRGB2DMatrix(RGB[][] values, int width, int height) {
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				values[i][j] = new RGB();
			}
		}
	}
	
	public static int[][] getChannelMatrix(RGB[][] values, int width, int height, int channel) {
		int[][] channelValues = new int[width][height];
		
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				channelValues[i][j] = values[i][j].getColor(channel);
			}
		}
		
		return channelValues;
	}
}
