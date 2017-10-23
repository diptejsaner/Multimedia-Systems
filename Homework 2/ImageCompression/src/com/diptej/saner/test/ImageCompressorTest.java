package com.diptej.saner.test;

import org.junit.Assert;
import org.junit.Test;

import com.diptej.saner.ImageCompressor;

public class ImageCompressorTest {

	@Test
	public void testDecompose_shouldReturnValidResult_whenValidInput() {
		double[] input = {7, 5, 8, 1};
		double[] expected = {6, 4.5, 1, 3.5};

		int len = 4;

		double[] actual = ImageCompressor.decompose(input, len);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void testRegenerate_shouldReturnValidResult_whenValidInput() {
		double[] input = { 3, 7, 4, 8, -1, -1, -1, -1 };
		double[] expected = { 2, 4, 6, 8, 3, 5, 7, 9 };

		int len = 4;

		double[] actual = ImageCompressor.regenerate(input, len);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void testWaveletTransformPass() {
		double[][] input = {
				{1, 2, 3, 4},
				{5, 8, 11, 14},
				{6, 9, 12, 15},
				{7, 10, 13, 8}
		};

		double[][] expected = {
				{1.5, 3.5, -0.5, -0.5},
				{6.5, 12.5, -1.5, -1.5},
				{7.5, 13.5, -1.5, -1.5},
				{8.5, 10.5, -1.5, 2.5}
		};

		ImageCompressor.WaveletTransformPass(input, 4, 4);
	}

	@Test
	public void testWaveletTransform() {
		double[][] input = {
				{1, 2, 3, 4},
				{5, 8, 11, 14},
				{6, 9, 12, 15},
				{7, 10, 13, 8}
		};

		double[][] expected = {
				{8.0, -2.0, -1.0, -1.0},
				{-2.0, 0, -1.5, 0.5},
				{-2.5, -4.5, 0.5, 0.5},
				{-0.5, 1.5, 0, -2.0}
		};

		double[][] actual = ImageCompressor.WaveletTransform(input, 4, 4);

		Assert.assertArrayEquals(expected, actual);
	}

	@Test
	public void testInverseWaveletTransform() {
		double[][] input = {
				{8.0, -2.0, -1.0, -1.0},
				{-2.0, 0, -1.5, 0.5},
				{-2.5, -4.5, 0.5, 0.5},
				{-0.5, 1.5, 0, -2.0}
		};

		double[][] expected = {
				{1, 2, 3, 4},
				{5, 8, 11, 14},
				{6, 9, 12, 15},
				{7, 10, 13, 8}
		};

		double[][] actual = ImageCompressor.InverseWaveletTransform(input, 4, 4);

		Assert.assertArrayEquals(expected, actual);
	}

}
