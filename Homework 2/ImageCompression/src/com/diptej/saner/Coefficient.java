package com.diptej.saner;

public class Coefficient {
	double rCoeff;
	double gCoeff;
	double bCoeff;

	Coefficient() {
		rCoeff = 0.0;
		gCoeff = 0.0;
		bCoeff = 0.0;
	}

	public static double[][] getChannelCoefficients(Coefficient[][] cMatrix, int channel) {
		int width = cMatrix[0].length;
		int height = cMatrix.length;

		double[][] values = new double[width][height];

		if (channel == 0) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					values[i][j] = cMatrix[i][j].rCoeff;
				}
			}
		}
		if (channel == 1) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					values[i][j] = cMatrix[i][j].gCoeff;
				}
			}
		}
		if (channel == 2) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					values[i][j] = cMatrix[i][j].bCoeff;
				}
			}
		}
		return values;
	}
}
