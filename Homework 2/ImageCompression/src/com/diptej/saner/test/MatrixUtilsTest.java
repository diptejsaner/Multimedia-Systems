package com.diptej.saner.test;

import org.junit.Test;

import com.diptej.saner.MatrixUtils;

public class MatrixUtilsTest {
	@Test
	public void testConvertToZigZag() {
		double[][] input = {
				{1, 2, 3, 4},
				{5, 6, 7, 8},
				{9, 10, 11, 12},
				{13, 14, 15, 16}
		};
		
		MatrixUtils.convertToZigZag(input, 10);
		
	}
}
