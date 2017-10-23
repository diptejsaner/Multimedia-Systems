package com.diptej.saner;

public enum OutputFormats {
	O1(1920, 1080),
	O2(1280, 720),
	O3(640, 480);
	
	private int width;
	private int height;
	
	OutputFormats(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	int getWidth() {
		return width;
	}
	
	int getHeight() {
		return height;
	}
}
