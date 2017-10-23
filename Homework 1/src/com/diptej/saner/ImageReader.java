package com.diptej.saner;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

public class ImageReader {

	static byte[] readFile(String str) throws IOException {
		File file = new File(str);
		InputStream is = new FileInputStream(file);

		long len = file.length();
		byte[] bytes = new byte[(int) len];

		// Loop for reading file into byte array
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		
		is.close();
		return bytes;
	}

	static void displayRgbImage(BufferedImage img) {
		// Use a panel and label to display the image
		JPanel panel = new JPanel();
		panel.add(new JLabel(new ImageIcon(img)));

		JFrame frame = new JFrame("Display images");

		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	static int getAverageOfPixels(BufferedImage img, int x, int y, int wd, int ht) {
		int rsum = 0;
		int gsum = 0;
		int bsum = 0;
		int numPixels = 0;

		// TODO refactor the conditionals
		int pixel = img.getRGB(x, y);
		int r = (pixel >> 16) & 0x000000FF;
		int g = (pixel >> 8) & 0x000000FF;
		int b = (pixel) & 0x000000FF;

		rsum += r;
		gsum += g;
		bsum += b;

		numPixels++;

		if ((x - 1) > 0) {
			pixel = img.getRGB(x - 1, y);
			r = (pixel >> 16) & 0x000000FF;
			g = (pixel >> 8) & 0x000000FF;
			b = (pixel) & 0x000000FF;

			rsum += r;
			gsum += g;
			bsum += b;

			numPixels++;
		}
		if ((y - 1) > 0) {
			pixel = img.getRGB(x, y - 1);
			r = (pixel >> 16) & 0x000000FF;
			g = (pixel >> 8) & 0x000000FF;
			b = (pixel) & 0x000000FF;

			rsum += r;
			gsum += g;
			bsum += b;

			numPixels++;
		}
		if ((x + 1) < wd) {
			pixel = img.getRGB(x + 1, y);
			r = (pixel >> 16) & 0x000000FF;
			g = (pixel >> 8) & 0x000000FF;
			b = (pixel) & 0x000000FF;

			rsum += r;
			gsum += g;
			bsum += b;

			numPixels++;
		}
		if ((y + 1) < ht) {
			pixel = img.getRGB(x, y + 1);
			r = (pixel >> 16) & 0x000000FF;
			g = (pixel >> 8) & 0x000000FF;
			b = (pixel) & 0x000000FF;

			rsum += r;
			gsum += g;
			bsum += b;

			numPixels++;
		}
		if ((x + 1) < wd && (y + 1) < ht) {
			pixel = img.getRGB(x + 1, y + 1);
			r = (pixel >> 16) & 0x000000FF;
			g = (pixel >> 8) & 0x000000FF;
			b = (pixel) & 0x000000FF;

			rsum += r;
			gsum += g;
			bsum += b;

			numPixels++;
		}
		if ((x - 1) > 0 && (y + 1) < ht) {
			pixel = img.getRGB(x - 1, y + 1);
			r = (pixel >> 16) & 0x000000FF;
			g = (pixel >> 8) & 0x000000FF;
			b = (pixel) & 0x000000FF;

			rsum += r;
			gsum += g;
			bsum += b;

			numPixels++;
		}
		if ((x - 1) > 0 && (y - 1) > 0) {
			pixel = img.getRGB(x - 1, y - 1);
			r = (pixel >> 16) & 0x000000FF;
			g = (pixel >> 8) & 0x000000FF;
			b = (pixel) & 0x000000FF;

			rsum += r;
			gsum += g;
			bsum += b;

			numPixels++;
		}
		if ((x + 1) < wd && (y - 1) > 0) {
			pixel = img.getRGB(x + 1, y - 1);
			r = (pixel >> 16) & 0x000000FF;
			g = (pixel >> 8) & 0x000000FF;
			b = (pixel) & 0x000000FF;

			rsum += r;
			gsum += g;
			bsum += b;

			numPixels++;
		}

		int avg = (int) (0xff000000 | (((rsum / numPixels) & 0xff) << 16) | (((gsum / numPixels) & 0xff) << 8)
				| (((bsum / numPixels)) & 0xff));
		return avg;
	}

	static int getAverageOfPixels(byte[] bytes, int x, int y, int wd, int ht, int wdRatio, int htRatio) {
		int ind = 0;
		int avg = 0;
		long r_avg = 0;
		long g_avg = 0;
		long b_avg = 0;

		for (int j = y; j < y + htRatio; j++) {
			for (int i = x; i < x + wdRatio; i++) {
				// avg += img.getRGB(i, j);
				byte r = bytes[ind];
				byte g = bytes[ind + ht * wd];
				byte b = bytes[ind + ht * wd * 2];

				r_avg += r;
				g_avg += g;
				b_avg += b;

				ind++;
			}
		}
		System.out.println("ravg " + r_avg + " gavg " + g_avg + " bavg " + b_avg);
		r_avg = r_avg / (wdRatio * htRatio);
		g_avg = g_avg / (wdRatio * htRatio);
		b_avg = b_avg / (wdRatio * htRatio);
		// System.out.println(avg / (wdRatio * htRatio));
		// return (avg / (wdRatio * htRatio));
		avg = (int) (0xff000000 | ((r_avg & 0xff) << 16) | ((g_avg & 0xff) << 8) | (b_avg & 0xff));

		return avg;
	}

	static Color getWeightedAverage(int a, float p, int b, Color ca, Color cb) {
		float avgRed = (p - a) * cb.getRed() + (b - p) * ca.getRed();
		float avgGreen = (p - a) * cb.getGreen() + (b - p) * ca.getGreen();
		float avgBlue = (p - a) * cb.getBlue() + (b - p) * ca.getBlue();

		Color c = new Color((int) (0xff000000 | (((int) avgRed & 0xff) << 16) | (((int) avgGreen & 0xff) << 8)
				| ((int) avgBlue & 0xff)));

		return c;
	}

	static int getWeightedAverage(int a, float p, int b, int pixel1, int pixel2) {
		int r1, r2, g1, g2, b1, b2;

		r1 = (pixel1 >> 16) & 0xff;
		g1 = (pixel1 >> 8) & 0xff;
		b1 = (pixel1 & 0xff);

		r2 = (pixel2 >> 16) & 0xff;
		g2 = (pixel2 >> 8) & 0xff;
		b2 = (pixel2 & 0xff);

		float avgRed = (p - a) * r2 + (b - p) * r1;
		float avgGreen = (p - a) * g2 + (b - p) * g1;
		float avgBlue = (p - a) * b2 + (b - p) * b1;

		int c = ((int) (0xff000000 | (((int) avgRed & 0xff) << 16) | (((int) avgGreen & 0xff) << 8)
				| ((int) avgBlue & 0xff)));

		return c;
	}

	public static void main(String[] args) {
//		String fileName = args[0];
		int width = Integer.parseInt(args[1]);
		int height = Integer.parseInt(args[2]);
		int resamplingMethod = Integer.parseInt(args[3]);

		String outputFormat = args[4];
		Boolean highRes;

		if (width > 2000)
			highRes = true;
		else
			highRes = false;

		int newWidth = OutputFormats.valueOf(outputFormat).getWidth();
		int newHeight = OutputFormats.valueOf(outputFormat).getHeight();

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage nimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

		try {
			byte[] bytes = readFile(args[0]);

			int pixels[][] = new int[width][height];
			System.out.println("height = " + height + " width = " + width);

			// Loop to initialize bufferedImage
			int ind = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {

//					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind + height * width];
					byte b = bytes[ind + height * width * 2];

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					// int pix = ((a << 24) + (r << 16) + (g << 8) + b);

					pixels[x][y] = pix;
					img.setRGB(x, y, pix);
					ind++;
				}
			}

			// downsampling
			if (highRes) {
				System.out.println("outputFormat: " + newWidth + " x " + newHeight);

				float wdRatio = (float) (width * 1.0 / newWidth);
				float htRatio = (float) (height * 1.0 / newHeight);
				System.out.println("wdr : " + wdRatio + " htr : " + htRatio);

				// specific sampling
				if (resamplingMethod == 1) {
					float y = 0;
					float x = 0;
					for (int j = 0; j < newHeight; y += htRatio, j++) {
						x = 0;
						for (int i = 0; i < newWidth; x += wdRatio, i++) {
							System.out.println("x : " + (int) x + " y : " + (int) y);
							nimg.setRGB(i, j, img.getRGB((int) x, (int) y));
						}
					}
					// averaging the 9 pixels
				} else if (resamplingMethod == 2) {
					float y = 0;
					float x = 0;

					for (int j = 0; j < newHeight; y += htRatio, j++) {
						x = 0;
						for (int i = 0; i < newWidth; x += wdRatio, i++) {
							nimg.setRGB(i, j, getAverageOfPixels(img, (int) x, (int) y, width, height));
						}
					}
				}
			}
			// upsampling
			else {
				System.out.println("outputFormat: " + newWidth + " x " + newHeight);

				float wdRatio = (float) (width * 1.0 / newWidth);
				float htRatio = (float) (height * 1.0 / newHeight);
				System.out.println("wdr : " + wdRatio + " htr : " + htRatio);

				float y = 0;
				float x = 0;

				// nearest neighbor
				if (resamplingMethod == 1) {
					for (int j = 0; j < newHeight; y += htRatio, j++) {
						x = 0;
						for (int i = 0; i < newWidth; x += wdRatio, i++) {
							nimg.setRGB(i, j, img.getRGB((int) x, (int) y));
						}
					}
					// Bilinear interpolation
				} else if (resamplingMethod == 2) {
					int x1, y1, x2, y2, x3, y3, x4, y4;

					wdRatio = (float) ((width - 1) * 1.0 / (newWidth - 1));
					htRatio = (float) ((height - 1) * 1.0 / (newHeight - 1));

					int pixel1, pixel2, pixel3, pixel4;

					for (int j = 0; j < newHeight; j++) {
						for (int i = 0; i < newWidth; i++) {
							x = i * wdRatio;
							y = j * htRatio;

							// boundary conditions
							if ((int) x == width - 1 || (int) y == height - 1) {
								nimg.setRGB(i, j, img.getRGB((int) x, (int) y));
								continue;
							}

							// calculating the nearest 4 corners
							x1 = (int) (x - (x % 1));
							y1 = (int) (y - (y % 1));

							x2 = x1 + 1;

							y2 = y1;

							x3 = x1;
							y3 = y1 + 1;

							x4 = x1 + 1;
							y4 = y1 + 1;

							pixel1 = img.getRGB(x1, y1);
							pixel2 = img.getRGB(x2, y2);

							int R1 = getWeightedAverage(x1, x, x2, pixel1, pixel2);

							pixel3 = img.getRGB(x3, y3);
							pixel4 = img.getRGB(x4, y4);

							int R2 = getWeightedAverage(x3, x, x4, pixel3, pixel4);

							// weighted average along y axis
							int c = getWeightedAverage(y1, y, y3, R1, R2);

							nimg.setRGB(i, j, c);
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// original image
		displayRgbImage(img);

		// new image
		displayRgbImage(nimg);
	}

}