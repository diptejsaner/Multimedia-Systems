package com.diptej.saner;

public class MatrixUtils {

	public static double[] getColumn(double[][] array, int index) {
		double[] column = new double[array[0].length];
		for (int i = 0; i < column.length; i++) {
			column[i] = array[i][index];
		}
		return column;
	}

	public static double[] getRow(double[][] array, int index) {
		return array[index];
	}

	public static double[][] castMatrixIntToDouble(int[][] array, int height, int width) {
		double[][] newArr = new double[width][height];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				newArr[i][j] = array[i][j];
			}
		}
		return newArr;
	}

	public static int[][] castMatrixDoubleToInt(double[][] array, int height, int width) {
		int[][] newArr = new int[width][height];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				newArr[i][j] = (int) array[i][j];
			}
		}
		return newArr;
	}

	public static void setColumn(double[][] array, double[] col, int index) {
		for (int i = 0; i < col.length; i++) {
			array[i][index] = col[i];
		}
	}

	public static double[][] convertToZigZag(double[][] array, int numCoeffs) {
		int width = array[0].length;
		int height = array.length;

		double[][] zarr = new double[width][height];

		int tc = 0;

		int row = 0, col = 0, d = 1;

		for (int i = 0; i < width * height; i++) {
			if (tc >= numCoeffs) {
				zarr[row][col] = 0;
			} else {
				zarr[row][col] = array[row][col];
			}

			row -= d;
			col += d;

			if (row >= height) {
				row = height - 1;
				col += 2;
				d = -d;
			}
			if (col >= width) {
				col = width - 1;
				row += 2;
				d = -d;
			}
			if (row < 0) {
				row = 0;
				d = -d;
			}
			if (col < 0) {
				col = 0;
				d = -d;
			}
			tc++;
		}

		return zarr;
	}

	public static Coefficient[][] dctConvertToZigZag(Coefficient[][] array, int numCoeffs) {
		int width = array[0].length;
		int height = array.length;

		Coefficient[][] zarr = new Coefficient[width][height];

		int tc = 0;

		int row = 0, col = 0, d = 1;

		for (int i = 0; i < width * height; i++) {
			if (tc >= numCoeffs) {
				zarr[row][col].rCoeff = 0;
				zarr[row][col].gCoeff = 0;
				zarr[row][col].bCoeff = 0;
			} else {
				zarr[row][col] = array[row][col];
			}

			row -= d;
			col += d;

			if (row >= height) {
				row = height - 1;
				col += 2;
				d = -d;
			}
			if (col >= width) {
				col = width - 1;
				row += 2;
				d = -d;
			}
			if (row < 0) {
				row = 0;
				d = -d;
			}
			if (col < 0) {
				col = 0;
				d = -d;
			}
			tc++;
		}

		return zarr;
	}
}
