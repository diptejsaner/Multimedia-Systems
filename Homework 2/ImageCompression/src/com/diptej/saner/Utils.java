package com.diptej.saner;

public class Utils {
	public static int getRed(int pixel) {
		return pixel >> 16 & 0xff;
	}

	public static int getGreen(int pixel) {
		return pixel >> 8 & 0xff;
	}

	public static int getBlue(int pixel) {
		return pixel & 0xff;
	}

	public static int getPixelFromRGB(int r, int g, int b) {
		int pix = 0xff000000 | (r & 0xff) << 16 | (g & 0xff) << 8 | b & 0xff;
		return pix;
	}

	public static int correctClipping(int x) {
		if (x > 255) {
			return 255;
		} else if (x < 0) {
			return 0;
		} else {
			return x;
		}
	}

	public static double highPassFilter(double a, double b) {
		return (a - b) / 2.0;
	}

	public static double lowPassFilter(double a, double b) {
		return (a + b) / 2.0;
	}

	public static double inverseAdd(double a, double b) {
		return a + b;
	}

	public static double inverseSubtract(double a, double b) {
		return a - b;
	}
}
