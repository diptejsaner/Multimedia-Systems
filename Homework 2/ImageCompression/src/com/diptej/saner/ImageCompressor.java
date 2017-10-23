package com.diptej.saner;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageCompressor {

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

	public static Coefficient[][] zigzagConversion(Coefficient[][] inputValues, int numCoeffs) {
		int width = inputValues[0].length;
		int height = inputValues.length;

		Coefficient[][] newValues = new Coefficient[width][height];

		// double[][] rch = Coefficient.getChannelCoefficients(newValues, 0);
		// double[][] gch = Coefficient.getChannelCoefficients(newValues, 1);
		// double[][] bch = Coefficient.getChannelCoefficients(newValues, 2);

		for (int block = 0; block < 4096; block++) {
			// double[][] procBlock = new double[8][8];
			Coefficient[][] procBlock = new Coefficient[8][8];

			for (int x = block / 64 * 8; x < block / 64 * 8 + 8; x++) {
				for (int y = block % 64 * 8; y < block % 64 * 8 + 8; y++) {
					procBlock[x][y] = inputValues[x][y];
				}
			}

			procBlock = MatrixUtils.dctConvertToZigZag(procBlock, numCoeffs / 4096);

			for (int x = block / 64 * 8; x < block / 64 * 8 + 8; x++) {
				for (int y = block % 64 * 8; y < block % 64 * 8 + 8; y++) {
					newValues[x][y] = procBlock[x][y];
				}
			}
		}

		return newValues;
	}

	static Coefficient[][] EncodeToDCTcoefficientMatrix(int pixels[][], int width, int height) {
		Coefficient coeffMatrix[][] = new Coefficient[width][height];

		for (int block = 0; block < 4096; block++) {
			// calculate for 8x8 block
			double uconstant = 1.0;
			double vconstant = 1.0;
			double constant = 1.0;
			// operator overload to operate on all r g b coeffs

			for (int u = 0; u < 8; u++) {
				for (int v = 0; v < 8; v++) {
					Coefficient co = new Coefficient();

					if (u == 0) {
						uconstant = 1.0 / Math.sqrt(2);
					}
					else {
						uconstant = 1.0;
					}
					if (v == 0) {
						vconstant = 1.0 / Math.sqrt(2);
					}
					else {
						vconstant = 1.0;
					}

					constant = uconstant * vconstant * 0.25;

					for (int x = block / 64 * 8; x < block / 64 * 8 + 8; x++) {
						for (int y = block % 64 * 8; y < block % 64 * 8 + 8; y++) {

							Color c = new Color(pixels[x][y]);

							// int R = Utils.getRed(pixels[x][y]);
							// int G = Utils.getGreen(pixels[x][y]);
							// int B = Utils.getBlue(pixels[x][y]);

							int R = c.getRed();
							int G = c.getGreen();
							int B = c.getBlue();

							co.rCoeff += R * Math.cos((2.0 * x + 1) * u * Math.PI / 16.0)
									* Math.cos((2.0 * y + 1) * v * Math.PI / 16.0);

							co.gCoeff += G * Math.cos((2.0 * x + 1) * u * Math.PI / 16.0)
									* Math.cos((2.0 * y + 1) * v * Math.PI / 16.0);

							co.bCoeff += B * Math.cos((2.0 * x + 1) * u * Math.PI / 16.0)
									* Math.cos((2.0 * y + 1) * v * Math.PI / 16.0);
						}
					}

					co.rCoeff *= constant;
					co.gCoeff *= constant;
					co.bCoeff *= constant;

					coeffMatrix[u + block/64 * 8][v + block%64 * 8] = co;
				}
			}
		}

		return coeffMatrix;
	}

	static int[][] DecodeDCTcoefficientMatrix(Coefficient cmatrix[][], int width, int height) {

		int pixels[][] = new int[width][height];

		for (int block = 0; block < 4096; block++) {

			double uconstant = 1.0;
			double vconstant = 1.0;
			double constant = 1.0;

			for (int x = block / 64 * 8; x < block / 64 * 8 + 8; x++) {
				for (int y = block % 64 * 8; y < block % 64 * 8 + 8; y++) {

					double R = 0, G = 0, B = 0;
					constant = 1.0;

					for (int u = 0; u < 8; u++) {
						for (int v = 0; v < 8; v++) {

							if (u == 0) {
								uconstant = 1.0 / Math.sqrt(2);
							} else {
								uconstant = 1.0;
							}
							if (v == 0) {
								vconstant = 1.0 / Math.sqrt(2);
							} else {
								vconstant = 1.0;
							}

							constant = uconstant * vconstant;

							R += constant * (cmatrix[u][v].rCoeff * Math.cos((2.0 * x + 1) * u * Math.PI / 16.0)
									* Math.cos((2.0 * y + 1) * v * Math.PI / 16.0));

							G += constant * (cmatrix[u][v].gCoeff * Math.cos((2.0 * x + 1) * u * Math.PI / 16.0)
									* Math.cos((2.0 * y + 1) * v * Math.PI / 16.0));

							B += constant * (cmatrix[u][v].bCoeff * Math.cos((2.0 * x + 1) * u * Math.PI / 16.0)
									* Math.cos((2.0 * y + 1) * v * Math.PI / 16.0));
						}
					}

					R *= 0.25;
					G *= 0.25;
					B *= 0.25;

					// int Rc = Utils.correctClipping((int) R);
					// int Gc = Utils.correctClipping((int) G);
					// int Bc = Utils.correctClipping((int) B);

					// Color co = new Color((int) R, (int) G, (int) B);
					int pixel = 0xff000000 | ((int) R & 0xff) << 16 | ((int) G & 0xff) << 8 | (int) B & 0xff;
					pixels[x][y] = pixel;
				}
			}
		}
		return pixels;
	}

	public static double[] decompose(double[] values, int processLen) {

		double[] decomposedValues = new double[values.length];

		for (int i = processLen / 2; i < values.length; i++) {
			decomposedValues[i] = values[i];
		}

		for (int i = 0, j = 0; i < processLen / 2; i++, j += 2) {
			decomposedValues[i] = Utils.lowPassFilter(values[j], values[j + 1]);
			decomposedValues[i + processLen / 2] = Utils.highPassFilter(values[j], values[j + 1]);
		}

		return decomposedValues;
	}

	public static double[] regenerate(double[] values, int processLen) {
		double[] rvalues = new double[values.length];

		for (int i = processLen * 2; i < values.length; i++) {
			rvalues[i] = values[i];
		}

		for (int i = 0, j = 0; j < processLen * 2; i++, j += 2) {
			rvalues[j] = Utils.inverseAdd(values[i], values[processLen + i]);
			rvalues[j + 1] = Utils.inverseSubtract(values[i], values[processLen + i]);
		}

		return rvalues;
	}

	public static void WaveletTransformPass(double[][] values, int width, int height) {
		for (int rowIndex = 0; rowIndex < height; rowIndex++) {
			values[rowIndex] = decompose(MatrixUtils.getRow(values, rowIndex), width);
		}

		for (int colIndex = 0; colIndex < width; colIndex++) {
			double[] col = decompose(MatrixUtils.getColumn(values, colIndex), height);
			MatrixUtils.setColumn(values, col, colIndex);
		}
	}

	// values should be initialized with the original pixel channel values
	public static double[][] WaveletTransform(double[][] values, int width, int height) {
		double[][] tvalues = new double[width][height];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				tvalues[i][j] = values[i][j];
			}
		}

		for (int len = width; len > 1; len /= 2) {
			WaveletTransformPass(tvalues, len, len);
		}

		return tvalues;
	}

	public static void InverseWaveletTransformPass(double[][] tvalues, int width, int height) {
		for (int colIndex = 0; colIndex < width * 2; colIndex++) {
			double[] col = regenerate(MatrixUtils.getColumn(tvalues, colIndex), height);
			MatrixUtils.setColumn(tvalues, col, colIndex);
		}
		for (int rowIndex = 0; rowIndex < height * 2; rowIndex++) {
			tvalues[rowIndex] = regenerate(MatrixUtils.getRow(tvalues, rowIndex), width);
		}
	}

	public static double[][] InverseWaveletTransform(double[][] tvalues, int width, int height) {
		double[][] itvalues = new double[width][height];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				itvalues[i][j] = tvalues[i][j];
			}
		}

		for (int len = 1; len < width; len *= 2) {
			InverseWaveletTransformPass(itvalues, len, len);
		}

		return itvalues;
	}

	public static void main(String[] args) {
		int coefficients = Integer.parseInt(args[1]);
		// int coefficients = 4096;
		// /Users/diptejsaner/Downloads/test images/rgb images
		final int width = 512;
		final int height = 512;
		Coefficient[][] coeffMatrix;

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		try {
			byte[] bytes = readFile(args[0]);

			int pixels[][] = new int[width][height];
			RGB[][] rgbValues = new RGB[width][height];
			RGB.initializeRGB2DMatrix(rgbValues, width, height);

			// Loop to initialize bufferedImage
			int ind = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					// byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind + height * width];
					byte b = bytes[ind + height * width * 2];

					int R = Byte.toUnsignedInt(r);
					int G = Byte.toUnsignedInt(g);
					int B = Byte.toUnsignedInt(b);

					rgbValues[x][y].R = R;
					rgbValues[x][y].G = G;
					rgbValues[x][y].B = B;

					int pix = 0xff000000 | (R & 0xff) << 16 | (G & 0xff) << 8 | B & 0xff;
					// int pix = ((a << 24) + (r << 16) + (g << 8) + b);

					pixels[y][x] = pix;
					img.setRGB(x, y, pix);
					ind++;
				}
			}

			coeffMatrix = EncodeToDCTcoefficientMatrix(pixels, width, height);

			int[][] dispPixels = DecodeDCTcoefficientMatrix(coeffMatrix, width, height);

			double[][] redDpixels = null, greenDpixels = null, blueDpixels = null;

			// since height and width are equal
			int nlen = width;

			redDpixels = MatrixUtils.castMatrixIntToDouble(RGB.getChannelMatrix(rgbValues, nlen, nlen, 0), nlen, nlen);
			double[][] tred = WaveletTransform(redDpixels, nlen, nlen);
			greenDpixels = MatrixUtils.castMatrixIntToDouble(RGB.getChannelMatrix(rgbValues, nlen, nlen, 1), nlen, nlen);
			double[][] tgreen = WaveletTransform(greenDpixels, nlen, nlen);
			blueDpixels = MatrixUtils.castMatrixIntToDouble(RGB.getChannelMatrix(rgbValues, nlen, nlen, 2), nlen, nlen);
			double[][] tblue = WaveletTransform(blueDpixels, nlen, nlen);

			tred = MatrixUtils.convertToZigZag(tred, coefficients);
			tgreen = MatrixUtils.convertToZigZag(tgreen, coefficients);
			tblue = MatrixUtils.convertToZigZag(tblue, coefficients);

			// animation
			// for (int nc = 4096; nc <= 262144; nc *= 2) {
			//
			// }

			double[][] itred = InverseWaveletTransform(tred, width, height);
			double[][] itgreen = InverseWaveletTransform(tgreen, width, height);
			double[][] itblue = InverseWaveletTransform(tblue, width, height);

			int[][] ired = null, igreen = null, iblue = null;

			ired = MatrixUtils.castMatrixDoubleToInt(itred, nlen, nlen);
			igreen = MatrixUtils.castMatrixDoubleToInt(itgreen, nlen, nlen);
			iblue = MatrixUtils.castMatrixDoubleToInt(itblue, nlen, nlen);

			BufferedImage dctimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			BufferedImage nimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {

					ired[x][y] = Utils.correctClipping(ired[x][y]);
					igreen[x][y] = Utils.correctClipping(igreen[x][y]);
					iblue[x][y] = Utils.correctClipping(iblue[x][y]);

					nimg.setRGB(x, y, Utils.getPixelFromRGB(ired[x][y], igreen[x][y], iblue[x][y]));
					dctimg.setRGB(x, y, dispPixels[y][x]);
				}
			}

			// displayRgbImage(img);
			displayRgbImage(nimg);
			displayRgbImage(dctimg);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}